package ee.webAppToolkit.core.expert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Injector;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.SiteMap;
import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.annotations.Path;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.HttpException;

public class RequestHandlerImpl implements RequestHandler {

	// store all handlers without trailing /
	private Map<String, Handler> _handlers;
	private Provider<String> _pathProvider;
	
	@Inject
	public RequestHandlerImpl(Injector injector, ControllerDescriptionFactory descriptionFactory,
			ActionHandlerFactory actionHandlerFactory, ControllerHandlerFactory controllerHandlerFactory,
			@WebAppToolkit Map<String, Class<?>> bindings,
			SiteMap siteMap, @Path Provider<String> pathProvider) throws ConfigurationException {

		_pathProvider = pathProvider;
		
		/*
		 * Handlers are created using an instance in order to keep their methods simple
		 * and have members like injector, descriptionFactory and bindings released for 
		 * garbage collection. 
		 */
		BindingProcessor bindingProcessor = new BindingProcessor(injector, descriptionFactory, actionHandlerFactory, controllerHandlerFactory, bindings, siteMap);
		_handlers = bindingProcessor.getHandlers();
		
		System.out.println(siteMap);
	}

	@Override
	public Result handleRequest() throws HttpException {
		// TODO Implement caching
		
		String testPath = _pathProvider.get();
		Handler handler = _handlers.get(testPath);
		
		while (handler == null && testPath.length() > 0)
		{
			testPath = testPath.substring(0, testPath.lastIndexOf('/'));
			handler = _handlers.get(testPath);
		}
		
		try {
			return handler.handle();
		} catch (HttpException e)
		{
			throw e;
		} catch (Throwable e) {
			throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
		}
	}

	public class BindingProcessor {

		private Map<Class<?>, ControllerDescription> _descriptionCache;
		private Injector _injector;
		private ControllerDescriptionFactory _descriptionFactory;
		private ActionHandlerFactory _actionHandlerFactory;
		private ControllerHandlerFactory _controllerHandlerFactory;
		private Map<String, Class<?>> _bindings;
		private SiteMap _siteMap;

		public BindingProcessor(Injector injector, ControllerDescriptionFactory descriptionFactory,
				ActionHandlerFactory actionHandlerFactory, ControllerHandlerFactory controllerHandlerFactory, Map<String, Class<?>> bindings, SiteMap siteMap) throws ConfigurationException {

			_injector = injector;
			_descriptionFactory = descriptionFactory;
			_actionHandlerFactory = actionHandlerFactory;
			_controllerHandlerFactory = controllerHandlerFactory;
			_bindings = bindings;
			_siteMap = siteMap;

			// to prevent excessive use of processing power we cache the controller descriptions
			_descriptionCache = new HashMap<Class<?>, ControllerDescription>();

			//TODO remove try catch
			try
			{
				_gatherHandlers();
			} catch (Throwable e)
			{
				e.printStackTrace();
			}
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
			boolean parentIsWrapping = _addToChain(controllerType, controllerChain, path, true);

			boolean directChild = parentIsWrapping;
			String parentPath = path;

			while (parentPath.length() > 0) {
				// remove the last segment
				parentPath = parentPath.substring(0, parentPath.lastIndexOf('/'));

				// check if we have a controller mapped at the given path
				directChild = _bindings.containsKey(parentPath);
				if (directChild) {
					Class<?> parentControllerType = _bindings.get(parentPath);
					directChild = _addToChain(parentControllerType, controllerChain, parentPath, directChild);
				}
			}

			// get all actions
			List<Action> actions = description.getActions();

			// create the handler chains for each action
			for (Action action : actions) {
				
				
				String memberName = action.getName();
				Handler handler;
				if (parentIsWrapping) {
					handler = _actionHandlerFactory.create(action);
				} else {
					// only provide a controller provider if the direct parent is not wrapping
					handler = _actionHandlerFactory.create(action, _injector.getProvider(controllerType), path);
				}

				Provider<? extends WrappingController> controllerProvider;
				for (ParentController parentController : controllerChain) {
					controllerProvider = _injector.getProvider(parentController.controllerType);
					handler = _controllerHandlerFactory.create(controllerProvider, parentController.path, handler, memberName, parentController.previousIsMember);
					memberName = parentController.name;
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

		private boolean _addToChain(Class<?> controllerType, List<ParentController> controllerChain, String path, boolean previousIsMember) {
			boolean added = WrappingController.class.isAssignableFrom(controllerType);
			if (added) {
				String name = path.length() > 0 ? path.substring(path.lastIndexOf('/') + 1) : null;
				controllerChain.add(new ParentController(name, controllerType.asSubclass(WrappingController.class), previousIsMember, path));
			}
			return added;

		}

		class ParentController {
			protected Class<? extends WrappingController> controllerType;
			protected String name;
			protected boolean previousIsMember;
			protected String path;

			ParentController(String name, Class<? extends WrappingController> controllerType, boolean previousIsMember, String path) {
				this.name = name;
				this.controllerType = controllerType;
				this.previousIsMember = previousIsMember;
				this.path = path;
			}
		}
	}
}
