package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.expert.impl.ActionHandlerFactoryImpl;

@ImplementedBy(ActionHandlerFactoryImpl.class)
public interface ActionHandlerFactory {
	public ActionHandler create(Action action) throws ConfigurationException;
	public ActionHandler create(Action action, Provider<?> controllerProvider, String context) throws ConfigurationException;
}
