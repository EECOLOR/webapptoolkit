package ee.webAppToolkit.core.expert.impl;

import java.lang.annotation.Annotation;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provider;

import ee.webAppToolkit.core.FlashMemory;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;

public class FlashActionArgumentResolver implements ActionArgumentResolver
{
	private Provider<FlashMemory> _flashMemoryProvider;
	
	@Inject
	public FlashActionArgumentResolver(Provider<FlashMemory> flashMemoryProvider)
	{
		_flashMemoryProvider = flashMemoryProvider;
	}
	
	@Override
	public <T> T resolve(Key<T> key, Action action) throws ConfigurationException
	{
		Annotation annotation = key.getAnnotation();
		Flash flash = (Flash) annotation;
		
		FlashMemory flashMemory = _flashMemoryProvider.get();
		String flashKeyName = flash.value();
		
		if (flashKeyName.length() > 0)
		{
			@SuppressWarnings("unchecked")
			T value = (T) flashMemory.get(flashKeyName);
			return value;
		} else
		{
			@SuppressWarnings("unchecked")
			T value = (T) flashMemory.get(key.getTypeLiteral().getType());
			return value;
		}
	}

}
