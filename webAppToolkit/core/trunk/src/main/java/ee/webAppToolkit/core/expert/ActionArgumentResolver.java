package ee.webAppToolkit.core.expert;

import com.google.inject.ImplementedBy;
import com.google.inject.Key;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.EmptyValueException;
import ee.webAppToolkit.core.expert.impl.ActionArgumentResolverImpl;

@ImplementedBy(ActionArgumentResolverImpl.class)
public interface ActionArgumentResolver
{
	public <T> T resolve(Key<T> key, boolean optional, Action action) throws EmptyValueException, ConfigurationException;
}
