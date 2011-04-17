package ee.webAppToolkit.core.expert.impl;

import javax.inject.Inject;
import javax.inject.Provider;

import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.expert.ControllerHandlerFactory;
import ee.webAppToolkit.core.expert.Handler;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;

/*
 * Had to implement it manually because of the limitation of the FactoryModuleBuilder that does not
 * allow providers to be arguments. 
 */
public class ControllerHandlerFactoryImpl implements ControllerHandlerFactory {

	private ThreadLocalProvider<String> _contextProvider;

	@Inject
	public ControllerHandlerFactoryImpl(@Context ThreadLocalProvider<String> contextProvider) {
		_contextProvider = contextProvider;
	}

	@Override
	public ControllerHandlerImpl create(Provider<? extends WrappingController> controllerProvider, String context,
			Handler childHandler, String memberName, boolean childIsMember) {
		return new ControllerHandlerImpl(controllerProvider, _contextProvider, context, childHandler, memberName,
				childIsMember);
	}

}
