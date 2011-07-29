package ee.webAppToolkit.freemarker.metadata.expert.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import ee.metadataUtils.PropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.FreemarkerPropertyMetadata;

public class FreemarkerPropertyMetadataImpl implements FreemarkerPropertyMetadata {

	private PropertyMetadata _propertyMetadata;
	private Map<String, Annotation> _annotations;
	
	@Inject
	public FreemarkerPropertyMetadataImpl(@Assisted PropertyMetadata propertyMetadata)
	{
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
}
