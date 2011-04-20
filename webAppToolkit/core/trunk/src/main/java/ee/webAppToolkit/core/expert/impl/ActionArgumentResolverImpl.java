package ee.webAppToolkit.core.expert.impl;

import java.lang.annotation.Annotation;

import javax.inject.Singleton;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.EmptyValueException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionArgumentAnnotation;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;

@Singleton
public class ActionArgumentResolverImpl implements ActionArgumentResolver
{
	private Injector _injector;
	
	@Inject
	public ActionArgumentResolverImpl(Injector injector)
	{
		_injector = injector;
	}
	
	@Override
	public <T> T resolve(Key<T> key, Action action) throws EmptyValueException, ConfigurationException
	{
		Annotation annotation = key.getAnnotation();

		//Does this key represent an action argument annotation
		if (annotation != null && annotation.annotationType().isAnnotationPresent(ActionArgumentAnnotation.class))
		{
			ActionArgumentResolver realResolver = _injector.getInstance(Key.get(ActionArgumentResolver.class, annotation));
			
			return realResolver.resolve(key, action);
		} else
		{
			try
			{
				return _injector.getInstance(key);
			} catch (com.google.inject.ConfigurationException e)
			{
				throw new ConfigurationException(e);
			}
		}
	}
	
}
