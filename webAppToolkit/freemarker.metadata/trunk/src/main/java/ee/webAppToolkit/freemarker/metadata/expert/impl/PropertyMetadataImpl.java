package ee.webAppToolkit.freemarker.metadata.expert.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadata;

public class PropertyMetadataImpl implements PropertyMetadata {

	private Field _field;
	private String _name;
	private Map<String, Annotation> _annotations;
	private Class<?> _type;
	
	@Inject
	public PropertyMetadataImpl(@Assisted Field field)
	{
		_field = field;
	}
	
	@Override
	public String getName() {
		if (_name == null)
		{
			_name = _field.getName();
		}
		return _name;
	}

	@Override
	public Map<String, Annotation> getAnnotations() {
		
		if (_annotations == null)
		{
			_annotations = new HashMap<String, Annotation>();
			
			Annotation[] annotations = _field.getAnnotations();
			
			for (Annotation annotation : annotations)
			{
				_annotations.put(annotation.annotationType().getSimpleName(), annotation);
			}
		}
		
		return _annotations;
	}

	@Override
	public Class<?> getType() {
		
		if (_type == null)
		{
			_type = _field.getType();
		}
		
		return _type;
	}
}
