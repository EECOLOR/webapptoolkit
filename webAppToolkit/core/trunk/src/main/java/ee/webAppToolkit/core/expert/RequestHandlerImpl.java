package ee.webAppToolkit.core.expert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Injector;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.SiteMap;
import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.HttpException;

public class RequestHandlerImpl implements RequestHandler {

	// store all handlers without trailing /
	private Map<String, Handler> _handlers;

	@Inject
	public RequestHandlerImpl(Injector injector, ControllerDescriptionFactory descriptionFactory,
			@WebAppToolkit Map<String, Class<?>> bindings,
			SiteMap siteMap) throws ConfigurationException {

		/*
		 * Handlers are created using an instance in order to keep their methods simple
		 * and have members like injector, descriptionFactory and bindings released for 
		 * garbage collection. 
		 */
		BindingProcessor bindingProcessor = new BindingProcessor(injector, descriptionFactory, bindings, siteMap);
		_handlers = bindingProcessor.getHandlers();
		
		System.out.println(siteMap);
	}

	@Override
	public Result handleRequest(String path) throws HttpException {
		// TODO Implement caching
		
		String testPath = path;
		Handler handler = _handlers.get(testPath);
		
		while (handler == null && testPath.length() > 0)
		{
			testPath = testPath.substring(0, testPath.lastIndexOf('/'));
			handler = _handlers.get(testPath);
		}
		
		try {
			return handler.handle(testPath);
		} catch (Throwable e) {
			throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
		}
	}

	public class BindingProcessor {

		private Map<Class<?>, ControllerDescription> _descriptionCache;
		private Injector _injector;
		private ControllerDescriptionFactory _descriptionFactory;
		private Map<String, Class<?>> _bindings;
		private SiteMap _siteMap;

		public BindingProcessor(Injector injector, ControllerDescriptionFactory descriptionFactory,
				Map<String, Class<?>> bindings, SiteMap siteMap) throws ConfigurationException {

			_injector = injector;
			_descriptionFactory = descriptionFactory;
			_bindings = bindings;
			_siteMap = siteMap;

			// to prevent excessive use of processing power we cache the controller descriptions
			_descriptionCache = new HashMap<Class<?>, ControllerDescription>();

			_gatherHandlers();
		}

		private void _gatherHandlers() throws ConfigurationException {
			_handlers = new HashMap<String, Handler>();
			
			/* 
			 * We need to sort the keys in order to make sure deep mapped controllers
			 * have the correct parents
			 */
			List<String> keys = new ArrayList<String>(_bindings.keySet());
			Collections.sort(keys);
			
			// loop through all keys so they can be processed
			for (String key : keys) {
				Class<?> controllerType = _bindings.get(key);

				if (key.endsWith("/")) {
					throw new ConfigurationException("Keys should not end with a /, found '" + key + "'");
				}

				_processController(key, controllerType);
			}
		}

		public Map<String, Handler> getHandlers() throws ConfigurationException {
			return _handlers;
		}

		private ControllerDescription _getDescription(Class<?> controllerType) throws ConfigurationException {
			ControllerDescription description;

			// get the controller description and put it in the cache
			if (_descriptionCache.containsKey(controllerType)) {
				description = _descriptionCache.get(controllerType);
			} else {
				description = _descriptionFactory.create(controllerType);
				_descriptionCache.put(controllerType, description);
			}

			return description;
		}

		private void _processController(String path, Class<?> controllerType) throws ConfigurationException {

			ControllerDescription description = _getDescription(controllerType);

			// build a chain of controllers
			List<ParentController> controllerChain = new ArrayList<ParentController>();

			// add the current controller to the chain
			boolean controllerIsParent = _addToChain(controllerType, controllerChain, path);

			String parentPath = path;

			while (parentPath.length() > 0) {
				// remove the last segment
				parentPath = parentPath.substring(0, parentPath.lastIndexOf('/'));

				// check if we have a controller mapped at the given path
				if (_bindings.containsKey(parentPath)) {
					Class<?> parentControllerType = _bindings.get(parentPath);
					_addToChain(parentControllerType, controllerChain, parentPath);
				}
			}

			// get all actions
			List<Action> actions = description.getActions();

			// create the handler chains for each action
			for (Action action : actions) {
				// only provide a controller provider if the handler has no direct parent
				Handler handler;
				if (controllerIsParent) {
					handler = new ActionHandler(action);
				} else {
					handler = new ActionHandler(_injector.getProvider(controllerType), action);
				}

				for (ParentController parentController : controllerChain) {
					handler = new ControllerHandler(_injector.getProvider(parentController.controllerType), handler,
							parentController.name);
				}

				String actionName = action.getName();

				// add it at the current key if the name is 'index'
				if (actionName.equals(ControllerDescription.INDEX)) {
					_addHandler(path, handler);
				}
				
				_addHandler(path + "/" + actionName, handler);
			}

			// create controller chains
			List<SubController> subControllers = description.getSubControllers();

			for (SubController subController : subControllers) {
				String newPath = path + "/" + subController.name();
				Class<?> subControllerType = subController.type();
				//add the path to the binding in order for other controllers to be able to find it
				_bindings.put(newPath, subControllerType);
				_processController(newPath, subControllerType);
			}
		}

		private Handler _addHandler(String path, Handler handler) throws ConfigurationException {
			
			if (_siteMap.containsPath(path))
			{
				throw new ConfigurationException("The path '" + path + "' is registered more then once.");
			}
			
			if (path.length() > 0)
			{
				_siteMap.addPagesForPath(path);
			}
			
			return _handlers.put(path, handler);
		}

		private boolean _addToChain(Class<?> controllerType, List<ParentController> controllerChain, String chain) {
			boolean added = WrappingController.class.isAssignableFrom(controllerType);
			if (added) {
				String name = chain.length() > 0 ? chain.substring(chain.lastIndexOf('/') + 1) : null;
				controllerChain.add(new ParentController(name, controllerType));
			}
			return added;
		}

		class ParentController {
			protected Class<?> controllerType;
			protected String name;

			ParentController(String name, Class<?> controllerType) {
				this.name = name;
				this.controllerType = controllerType;
			}
		}
	}
}
