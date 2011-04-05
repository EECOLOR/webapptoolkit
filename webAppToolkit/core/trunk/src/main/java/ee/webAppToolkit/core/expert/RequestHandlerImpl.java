package ee.webAppToolkit.core.expert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.Injector;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.HttpException;

public class RequestHandlerImpl implements RequestHandler {

	// store all handlers without trailing /

	private Map<String, Handler> _handlers;

	@Inject
	public RequestHandlerImpl(Injector injector, ControllerDescriptionFactory descriptionFactory,
			@WebAppToolkit Map<String, Class<?>> bindings) throws ConfigurationException {

		_handlers = new HashMap<String, Handler>();
		
		List<String> keys = new ArrayList<String>(bindings.keySet());
		// sort the keys so we only need to loop once
		Collections.sort(keys);

		// to prevent excessive use of processing power we cache the controller
		// descriptions
		Map<Class<?>, ControllerDescription> descriptionCache = new HashMap<Class<?>, ControllerDescription>();

		// loop through all keys so they can be processed
		for (String key : keys) {
			Class<?> controllerType = bindings.get(key);

			if (key.endsWith("/")) {
				throw new ConfigurationException("Keys should not end with a /, found '" + key + "'");
			}

			System.out.println("_processController: " + key + " : " + controllerType);
			_processController(injector, descriptionFactory, bindings, descriptionCache, key, controllerType);
		}
		
		//TODO remove
		ArrayList<String> test = new ArrayList<String>(_handlers.keySet());
		Collections.sort(test);
		for (String s : test)
		{
			int r = 4 - (s.length() / 8);
			StringBuffer buf = new StringBuffer (r);
			for (int i = 0; i < r; i++) {
				buf.append("\t");
			}
			
			System.out.println("'" + s + "' " + buf.toString() + _handlers.get(s));
		}
	}

	private void _processController(Injector injector, ControllerDescriptionFactory descriptionFactory,
			Map<String, Class<?>> bindings, Map<Class<?>, ControllerDescription> descriptionCache, String key,
			Class<?> controllerType) throws ConfigurationException {

		ControllerDescription description;

		// get the controller description and put it in the cache
		if (descriptionCache.containsKey(controllerType)) {
			description = descriptionCache.get(controllerType);
		} else {
			description = descriptionFactory.create(controllerType);
			descriptionCache.put(controllerType, description);
		}

		// determine the chain of controllers
		List<ParentController> parents = new ArrayList<ParentController>();

		String chain = key;
		
		if (WrappingController.class.isAssignableFrom(controllerType)) {
			
			if (chain.length() > 0)
			{
				chain = chain.substring(0, chain.lastIndexOf('/'));
			}
			_addParent(controllerType, parents, chain);
		}
		
		System.out.println(chain);
		while (chain.length() > 0) {
			chain = chain.substring(0, chain.lastIndexOf('/'));

			if (bindings.containsKey(chain)) {
				Class<?> parentControllerType = bindings.get(chain);
				if (WrappingController.class.isAssignableFrom(parentControllerType)) {
					System.out.println("_addParent :: " + chain + " :: " + parentControllerType);
					_addParent(parentControllerType, parents, chain);
				}
			}
		}

		// get all actions
		List<Action> actions = description.getActions();

		boolean hasParents = parents.size() > 0;

		// create the handler chains
		for (Action action : actions) {
			// only provide a controller provider if the handler has no parents
			Handler handler = new ActionHandler(action, hasParents ? null : injector.getProvider(controllerType));

			for (ParentController parentController : parents) {
				handler = new ControllerHandler(injector.getProvider(parentController.controllerType), handler,
						parentController.name);
			}

			String actionName = action.getName();
			_handlers.put(key + "/" + actionName, handler);

			// also add it at the current key if the name is 'index'
			if (actionName.equals(ControllerDescription.INDEX)) {
				_handlers.put(key, handler);
			}
		}

		// create controller chains
		List<SubController> subControllers = description.getSubControllers();

		for (SubController subController : subControllers) {
			_processController(injector, descriptionFactory, bindings, descriptionCache,
					key + "/" + subController.name(), subController.type());
		}
	}

	private void _addParent(Class<?> controllerType, List<ParentController> parents, String chain) {
		String name = chain.length() > 0 ? chain.substring(chain.lastIndexOf('/') + 1) : null;
		parents.add(new ParentController(name, controllerType));
	}

	class ParentController {
		protected Class<?> controllerType;
		protected String name;

		ParentController(String name, Class<?> controllerType) {
			this.name = name;
			this.controllerType = controllerType;
		}
	}

	@Override
	public Result handleRequest(String path) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

}
