package ee.webAppToolkit.freemarker.metadata.expert.impl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ee.webAppToolkit.freemarker.metadata.expert.MetadataRegistry;
import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadataFactory;

public class MetadataRegistryImpl implements MetadataRegistry {

	private PropertyMetadataFactory _propertyMetadataFactory;
	
	private Map<Class<?>, Map<String, PropertyMetadata>> _propertyMetadata;
	
	@Inject
	public MetadataRegistryImpl(PropertyMetadataFactory propertyMetadataFactory)
	{
		_propertyMetadataFactory = propertyMetadataFactory;
		
		_propertyMetadata = Collections.synchronizedMap(new HashMap<Class<?>, Map<String, PropertyMetadata>>());
	}

	@Override
	public PropertyMetadata getPropertyMetadata(Class<?> type, String propertyName) {
		
		if (!_propertyMetadata.containsKey(type))
		{
			_gatherPropertyMetadata(type);
		}
		
		return _propertyMetadata.get(type).get(propertyName);
	}
	
	@Override
	public Map<String, PropertyMetadata> getPropertyMetadata(Class<? extends Object> type) {
		
		if (!_propertyMetadata.containsKey(type))
		{
			_gatherPropertyMetadata(type);
		}
		
		return _propertyMetadata.get(type);
	}

	private synchronized void _gatherPropertyMetadata(Class<?> type)
	{
		if (_propertyMetadata.containsKey(type))
		{
			return;
		}
		
		Class<?> currentType = type;
		Map<String, PropertyMetadata> propertyMetadataMap = new HashMap<String, PropertyMetadata>();
		
		//TODO add a switch to allow the use getters and setters as well
		while (!Object.class.equals(currentType))
		{
			Field[] declaredFields = currentType.getDeclaredFields();
			
			for (Field field : declaredFields)
			{
				if (isAllowed(field))
				{
					PropertyMetadata propertyMetadata = _propertyMetadataFactory.create(field);
					propertyMetadataMap.put(propertyMetadata.getName(), propertyMetadata);
				}
			}
			
			currentType = currentType.getSuperclass();
		}
		
		_propertyMetadata.put(type, propertyMetadataMap);
	}
	
	protected boolean isAllowed(Field field)
	{
		return !field.getName().startsWith("this$");
	}
}
