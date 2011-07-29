package ee.webAppToolkit.freemarker.forms.expert.impl;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import ee.metadataUtils.PropertyMetadata;
import ee.metadataUtils.PropertyMetadataAdapterFactory;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.freemarker.forms.expert.FreemarkerPropertyMetadataFactory;
import ee.webAppToolkit.freemarker.metadata.expert.FreemarkerPropertyMetadata;

public class DisplayOnlyFreemarkerPropertyMetadataAdapterFactoryImpl implements PropertyMetadataAdapterFactory<FreemarkerPropertyMetadata> {

	private FreemarkerPropertyMetadataFactory _freemarkerPropertyMetadataFactory;

	@Inject
	public DisplayOnlyFreemarkerPropertyMetadataAdapterFactoryImpl(FreemarkerPropertyMetadataFactory freemarkerPropertyMetadataFactory)
	{
		_freemarkerPropertyMetadataFactory = freemarkerPropertyMetadataFactory;
	}
	
	@Override
	public FreemarkerPropertyMetadata getAdapter(PropertyMetadata propertyMetadata) {
		
		Annotation[] annotations = propertyMetadata.getAnnotations();
		
		for (Annotation annotation : annotations)
		{
			if (annotation instanceof Display)
			{
				return _freemarkerPropertyMetadataFactory.create(propertyMetadata);
			}
		}
		
		return null;
	}
	
}
