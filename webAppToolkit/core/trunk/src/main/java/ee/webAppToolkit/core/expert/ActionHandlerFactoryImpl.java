package ee.webAppToolkit.core.expert;

import javax.inject.Inject;
import javax.inject.Provider;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.exceptions.ConfigurationException;

/*
 * Had to implement it manually because of the limitation of the FactoryModuleBuilder that does not
 * allow providers to be arguments. 
 */
public class ActionHandlerFactoryImpl implements ActionHandlerFactory {

	private Provider<RequestMethod> _requestMethodProvider;
	private ThreadLocalProvider<String> _contextProvider;

	@Inject
	public ActionHandlerFactoryImpl(Provider<RequestMethod> requestMethodProvider, @Context ThreadLocalProvider<String> contextProvider)
	{
		_requestMethodProvider = requestMethodProvider;
		_contextProvider = contextProvider;
	}
	
	@Override
	public ActionHandler create(Action action) throws ConfigurationException {
		return new ActionHandlerImpl(_requestMethodProvider, action);
	}

	@Override
	public ActionHandler create(Action action, Provider<?> controllerProvider, String context) throws ConfigurationException {
		return new ActionHandlerImpl(_requestMethodProvider, action, controllerProvider, _contextProvider, context);
	}

}
