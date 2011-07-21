package ee.webAppToolkit.core.expert.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Injector;

import ee.webAppToolkit.core.FlashMemory;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.annotations.Path;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.HttpException;
import ee.webAppToolkit.core.exceptions.RedirectException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionHandlerFactory;
import ee.webAppToolkit.core.expert.ActionRegistrationListener;
import ee.webAppToolkit.core.expert.ControllerDescription;
import ee.webAppToolkit.core.expert.ControllerDescriptionFactory;
import ee.webAppToolkit.core.expert.ControllerHandlerFactory;
import ee.webAppToolkit.core.expert.ControllerRegistrationListener;
import ee.webAppToolkit.core.expert.Handler;
import ee.webAppToolkit.core.expert.RequestHandler;
import ee.webAppToolkit.core.expert.WebAppToolkit;

public class RequestHandlerImpl implements RequestHandler {

	// store all handlers without trailing /
	private Map<String, Handler> _handlers;
	private Provider<String> _pathProvider;
	private BindingProcessor _bindingProcessor;
	private Provider<FlashMemory> _flashMemoryProvider;
	
	@Inject
	public RequestHandlerImpl(Injector injector, ControllerDescriptionFactory descriptionFactory,
			ActionHandlerFactory actionHandlerFactory,
			ControllerHandlerFactory controllerHandlerFactory,
			@WebAppToolkit Map<String, Class<?>> bindings, @Path Provider<String> pathProvider,
			Set<ActionRegistrationListener> actionRegistrationListeners,
			Set<ControllerRegistrationListener> controllerRegistrationListeners,
			Provider<FlashMemory> flashMemoryProvider)
			throws ConfigurationException {

		_pathProvider = pathProvider;
		_flashMemoryProvider = flashMemoryProvider;	
		
		/*
		 * Handlers are created using an instance in order to keep their methods simple and have members like injector,
		 * descriptionFactory and bindings released for garbage collection.
		 */
		_bindingProcessor = new BindingProcessor(injector, descriptionFactory,
				actionHandlerFactory, controllerHandlerFactory, bindings,
				actionRegistrationListeners, controllerRegistrationListeners);

	}

	@Override
	public void init(String context) throws ConfigurationException {
		_handlers = _bindingProcessor.getHandlers(context);
		// mark binding processor for garbage collection
		_bindingProcessor = null;
	}

	@Override
	public Result handleRequest() throws HttpException {
		
		//notify the FlashMemory that we are moving on to the next request
		_flashMemoryProvider.get().next();
		
		// TODO Implement caching

		String testPath = _pathProvider.get();
		Handler handler = _handlers.get(testPath);

		while (handler == null && testPath.length() > 0) {
			testPath = testPath.substring(0, testPath.lastIndexOf('/'));
			handler = _handlers.get(testPath);
		}

		try {
			return handler.handle();
		} catch (HttpException e) {
			throw e;
		} catch (RedirectException e) {
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
		private String _context;
		private Set<ActionRegistrationListener> _actionRegistrationListeners;
		private Set<ControllerRegistrationListener> _controllerRegistrationListeners;

		public BindingProcessor(Injector injector, ControllerDescriptionFactory descriptionFactory,
				ActionHandlerFactory actionHandlerFactory,
				ControllerHandlerFactory controllerHandlerFactory, Map<String, Class<?>> bindings,
				Set<ActionRegistrationListener> actionRegistrationListeners,
				Set<ControllerRegistrationListener> controllerRegistrationListeners) throws ConfigurationException {

			_injector = injector;
			_descriptionFactory = descriptionFactory;
			_actionHandlerFactory = actionHandlerFactory;
			_controllerHandlerFactory = controllerHandlerFactory;
			_bindings = bindings;
			_actionRegistrationListeners = actionRegistrationListeners;
			_controllerRegistrationListeners = controllerRegistrationListeners;

			// to prevent excessive use of processing power we cache the controller descriptions
			_descriptionCache = new HashMap<Class<?>, ControllerDescription>();
		}

		private void _gatherHandlers() throws ConfigurationException {
			_handlers = new HashMap<String, Handler>();

			/*
			 * We need to sort the keys in order to make sure deep mapped controllers have the correct parents
			 */
			List<String> keys = new ArrayList<String>(_bindings.keySet());
			Collections.sort(keys);

			// loop through all keys so they can be processed
			for (String key : keys) {
				Class<?> controllerType = _bindings.get(key);

				if (key.endsWith("/")) {
					throw new ConfigurationException("Keys should not end with a /, found '" + key
							+ "'");
				}

				_processController(key, controllerType);
			}
		}

		public Map<String, Handler> getHandlers(String context) throws ConfigurationException {
			_context = context;
			_gatherHandlers();

			return _handlers;
		}

		private ControllerDescription _getDescription(Class<?> controllerType)
				throws ConfigurationException {
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

		private void _processController(String path, Class<?> controllerType)
				throws ConfigurationException {

			for (ControllerRegistrationListener controllerRegistrationListener : _controllerRegistrationListeners)
			{
				controllerRegistrationListener.controllerRegistered(path, controllerType);
			}
			
			ControllerDescription description = _getDescription(controllerType);

			// build a chain of controllers
			List<ParentController> controllerChain = new ArrayList<ParentController>();

			// add the current controller to the chain
			_addToChain(controllerType, controllerChain, path, true);
			boolean parentIsWrapping = WrappingController.class.isAssignableFrom(controllerType);

			boolean directChild = parentIsWrapping;
			String parentPath = path;

			while (parentPath.length() > 0) {
				// remove the last segment
				parentPath = parentPath.substring(0, parentPath.lastIndexOf('/'));

				// check if we have a controller mapped at the given path
				if (_bindings.containsKey(parentPath)) {
					Class<?> parentControllerType = _bindings.get(parentPath);
					_addToChain(parentControllerType, controllerChain, parentPath, directChild);
					directChild = WrappingController.class.isAssignableFrom(controllerType);
				}
			}

			// get all actions
			List<Action> actions = description.getActions();

			// create the handler chains for each action
			for (Action action : actions) {

				controllerType = _processAction(action, path, controllerType, controllerChain,
						parentIsWrapping);
			}

			// create controller chains
			List<SubController> subControllers = description.getSubControllers();

			for (SubController subController : subControllers) {
				String newPath = path + "/" + subController.name();
				Class<?> subControllerType = subController.type();
				// add the path to the binding in order for other controllers to be able to find it
				_bindings.put(newPath, subControllerType);
				_processController(newPath, subControllerType);
			}
		}

		private Class<?> _processAction(Action action, String path, Class<?> controllerType,
				List<ParentController> controllerChain, boolean parentIsWrapping)
				throws ConfigurationException {

			String memberName = action.getName();
			Handler handler;

			String fullPath = _context + path;

			boolean index = memberName.equals(ControllerDescription.INDEX);
			String actualPath = fullPath + "/" + memberName;

			for (ActionRegistrationListener actionRegistrationListener : _actionRegistrationListeners)
			{
				if (index)
				{
					actionRegistrationListener.actionRegistered(fullPath, action);
				}
				
				actionRegistrationListener.actionRegistered(actualPath, action);
			}
			
			if (_handlers.containsKey(actualPath)) {
				// we already have a handler registered for this path
				Handler existingHandler = _handlers.get(actualPath);

				existingHandler.addAction(action);
			} else {
				if (parentIsWrapping) {
					handler = _actionHandlerFactory.create(action);
				} else {
					// only provide a controller provider if the direct parent is not wrapping
					handler = _actionHandlerFactory.create(action,
							_injector.getProvider(controllerType), fullPath);
				}

				Provider<? extends WrappingController> controllerProvider;
				for (ParentController parentController : controllerChain) {
					controllerType = parentController.controllerType;
					if (WrappingController.class.isAssignableFrom(controllerType)) {
						controllerProvider = _injector.getProvider(controllerType
								.asSubclass(WrappingController.class));
						handler = _controllerHandlerFactory.create(controllerProvider, _context
								+ parentController.path, handler, memberName,
								parentController.previousIsMember);
					}
					memberName = parentController.name;
				}

				// add it at the current key if the name is 'index'
				if (index) {
					_handlers.put(fullPath, handler);
				} else
				{
					_handlers.put(actualPath, handler);
				}
			}

			return controllerType;
		}

		private void _addToChain(Class<?> controllerType, List<ParentController> controllerChain,
				String path, boolean previousIsMember) {
			String name = path.length() > 0 ? path.substring(path.lastIndexOf('/') + 1) : null;
			controllerChain.add(new ParentController(name, controllerType, previousIsMember, path));
		}

		class ParentController {
			protected Class<?> controllerType;
			protected String name;
			protected boolean previousIsMember;
			protected String path;

			ParentController(String name, Class<?> controllerType, boolean previousIsMember,
					String path) {
				this.name = name;
				this.controllerType = controllerType;
				this.previousIsMember = previousIsMember;
				this.path = path;
			}
		}
	}
}
