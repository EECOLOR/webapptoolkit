package ee.webAppToolkit.core.expert;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.EmptyValueException;

public class ActionArgumentResolverImpl implements ActionArgumentResolver
{
	private Injector _injector;
	
	@Inject
	public ActionArgumentResolverImpl(Injector injector)
	{
		_injector = injector;
	}
	
	@Override
	public <T> T resolve(Key<T> key, boolean optional, Action action) throws EmptyValueException, ConfigurationException
	{
		try
		{
			return _injector.getInstance(key);
		} catch (com.google.inject.ConfigurationException e)
		{
			if (optional)
			{
				//no problem
				return null;
			} else
			{
				throw e;
			}
		}
	}
	
}
