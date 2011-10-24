package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import com.google.inject.assistedinject.Assisted;

import ee.metadataUtils.PropertyMetadata;
import ee.metadataUtils.PropertyMetadataRegistry;
import ee.metadataUtils.impl.AdapterNotFoundException;
import ee.webAppToolkit.rendering.freemarker.utils.expert.EnumerationResolver;
import ee.webAppToolkit.rendering.freemarker.utils.expert.FreemarkerPropertyMetadata;

public class FreemarkerPropertyMetadataImpl implements FreemarkerPropertyMetadata {

	private EnumerationResolver _enumerationResolver;
	private PropertyMetadata _propertyMetadata;
	private Map<String, Annotation> _annotations;
	private PropertyMetadataRegistry _propertyMetadataRegistry;
	
	@Inject
	public FreemarkerPropertyMetadataImpl(PropertyMetadataRegistry propertyMetadataRegistry,
			EnumerationResolver enumerationResolver,
			@Assisted PropertyMetadata propertyMetadata)
	{
		_propertyMetadataRegistry = propertyMetadataRegistry;
		_enumerationResolver = enumerationResolver;
		_propertyMetadata = propertyMetadata;
	}
	
	@Override
	public String getName() {
		return _propertyMetadata.getName();
	}

	@Override
	public Map<String, Annotation> getAnnotations() {
		
		if (_annotations == null)
		{
			_annotations = new HashMap<String, Annotation>();
			
			Annotation[] annotations = _propertyMetadata.getAnnotations();
			
			for (Annotation annotation : annotations)
			{
				_annotations.put(annotation.annotationType().getSimpleName(), annotation);
			}
		}
		
		return _annotations;
	}

	@Override
	public Class<?> getType() {
		return _propertyMetadata.getType();
	}
	
	@Override
	public Map<String, FreemarkerPropertyMetadata> getProperties()
	{
		try {
			return _propertyMetadataRegistry.getPropertyMetadataMap(getType(), FreemarkerPropertyMetadata.class);
		} catch (AdapterNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getIsList() {
		Class<?> type = getType();
		return List.class.isAssignableFrom(type) || type.isArray();
	}

	@Override
	public Map<String, FreemarkerPropertyMetadata> getComponentProperties() {
		Class<?> type = getType();
		
		if (type.isArray())
		{
			type = type.getComponentType();
		} else if (List.class.isAssignableFrom(type))
		{
			
			ParameterizedType genericType = (ParameterizedType) _propertyMetadata.getGenericType();
			type = (Class<?>) genericType.getActualTypeArguments()[0];
		}
		
		try {
			return _propertyMetadataRegistry.getPropertyMetadataMap(type, FreemarkerPropertyMetadata.class);
		} catch (AdapterNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, FreemarkerPropertyMetadata> getDisplayProperties() {
		return Maps.filterValues(getProperties(), new DisplayOnly());
	}

	@Override
	public Map<String, FreemarkerPropertyMetadata> getComponentDisplayProperties() {
		return Maps.filterValues(getComponentProperties(), new DisplayOnly());
	}

	@Override
	public List<?> getEnumeration() {
		return _enumerationResolver.getEnumeration(getType());
	}
	
}
