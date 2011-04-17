package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.expert.impl.ControllerHandlerFactoryImpl;
import ee.webAppToolkit.core.expert.impl.ControllerHandlerImpl;

@ImplementedBy(ControllerHandlerFactoryImpl.class)
public interface ControllerHandlerFactory {
	public ControllerHandlerImpl create(Provider<? extends WrappingController> controllerProvider, String context, Handler childHandler, String memberName, boolean childIsMember);
}
