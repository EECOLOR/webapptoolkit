package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.WrappingController;

@ImplementedBy(ControllerHandlerFactoryImpl.class)
public interface ControllerHandlerFactory {
	public ControllerHandlerImpl create(Provider<? extends WrappingController> controllerProvider, Handler childHandler, String memberName, boolean childIsMember);
}
