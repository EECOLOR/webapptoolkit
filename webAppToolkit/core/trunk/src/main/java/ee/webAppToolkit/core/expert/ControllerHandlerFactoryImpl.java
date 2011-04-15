package ee.webAppToolkit.core.expert;

import javax.inject.Inject;
import javax.inject.Provider;

import ee.webAppToolkit.core.WrappingController;

/*
 * Had to implement it manually because of the limitation of the FactoryModuleBuilder that does not
 * allow providers to be arguments. 
 */
public class ControllerHandlerFactoryImpl implements ControllerHandlerFactory {

	private ContextProvider _contextProvider;

	@Inject
	public ControllerHandlerFactoryImpl(ContextProvider contextProvider) {
		_contextProvider = contextProvider;
	}

	@Override
	public ControllerHandlerImpl create(Provider<? extends WrappingController> controllerProvider, String context,
			Handler childHandler, String memberName, boolean childIsMember) {
		return new ControllerHandlerImpl(controllerProvider, _contextProvider, context, childHandler, memberName,
				childIsMember);
	}

}
