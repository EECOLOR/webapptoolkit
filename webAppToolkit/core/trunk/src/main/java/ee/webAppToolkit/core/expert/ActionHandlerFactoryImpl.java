package ee.webAppToolkit.core.expert;

import javax.inject.Inject;
import javax.inject.Provider;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.exceptions.ConfigurationException;

/*
 * Had to implement it manually because of the limitation of the FactoryModuleBuilder that does not
 * allow providers to be arguments. 
 */
public class ActionHandlerFactoryImpl implements ActionHandlerFactory {

	private Provider<RequestMethod> _requestMethodProvider;

	@Inject
	public ActionHandlerFactoryImpl(Provider<RequestMethod> requestMethodProvider)
	{
		_requestMethodProvider = requestMethodProvider;
	}
	
	@Override
	public ActionHandler create(Action action) throws ConfigurationException {
		return new ActionHandlerImpl(_requestMethodProvider, action);
	}

	@Override
	public ActionHandler create(Action action, Provider<?> controllerProvider) throws ConfigurationException {
		return new ActionHandlerImpl(_requestMethodProvider, action, controllerProvider);
	}

}
