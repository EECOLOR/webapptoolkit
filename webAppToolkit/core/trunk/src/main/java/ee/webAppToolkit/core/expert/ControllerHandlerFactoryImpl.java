package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import ee.webAppToolkit.core.WrappingController;

/*
 * Had to implement it manually because of the limitation of the FactoryModuleBuilder that does not
 * allow providers to be arguments. 
 */
public class ControllerHandlerFactoryImpl implements ControllerHandlerFactory {

	@Override
	public ControllerHandlerImpl create(Provider<? extends WrappingController> controllerProvider,
			Handler childHandler, String memberName, boolean childIsMember) {
		return new ControllerHandlerImpl(controllerProvider, childHandler, memberName, childIsMember);
	}

}
