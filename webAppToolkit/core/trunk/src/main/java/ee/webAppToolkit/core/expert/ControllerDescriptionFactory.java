package ee.webAppToolkit.core.expert;

import ee.webAppToolkit.core.exceptions.ConfigurationException;

public interface ControllerDescriptionFactory
{
	public ControllerDescription create(Class<?> controllerType) throws ConfigurationException;
}
