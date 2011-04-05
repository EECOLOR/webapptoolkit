package ee.webAppToolkit.core.expert;

import java.lang.reflect.Method;

import ee.webAppToolkit.core.exceptions.ConfigurationException;

public interface ActionFactory
{
	public Action create(Method method) throws ConfigurationException;
}
