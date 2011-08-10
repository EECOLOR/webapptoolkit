package ee.webAppToolkit.freemarker.metadata.expert.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.metadataUtils.PropertyMetadataRegistry;
import ee.metadataUtils.impl.AdapterNotFoundException;
import ee.webAppToolkit.freemarker.metadata.expert.CustomObjectTemplateModel;
import ee.webAppToolkit.freemarker.metadata.expert.FreemarkerPropertyMetadata;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class MetadataTemplateModel extends StringModel implements CustomObjectTemplateModel
{
	private PropertyMetadataRegistry _propertyMetadataRegistry;
	
	@Inject
	public MetadataTemplateModel(
			PropertyMetadataRegistry propertyMetadataRegistry,
			@Assisted Object object, 
			@Assisted ObjectWrapper wrapper)
	{
		super(object, (BeansWrapper) wrapper);
		
		_propertyMetadataRegistry = propertyMetadataRegistry;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException
	{
		if (key == null)
		{
			return super.get(key);
		}
		
		if (key.endsWith("_metadata"))
		{
			//find the getter for the requested property
			key = key.replace("_metadata", "");
			
			FreemarkerPropertyMetadata propertyMetadata;
			try {
				propertyMetadata = _propertyMetadataRegistry.getPropertyMetadata(object.getClass(), key, FreemarkerPropertyMetadata.class);
			} catch (AdapterNotFoundException e) {
				throw new TemplateModelException(e);
			}
			
			if (propertyMetadata == null)
			{
				throw new TemplateModelException("Could not find property metadata for '" + key + "' in type '" + object.getClass() + "'");
			} else
			{
				return wrap(propertyMetadata);
			}
		} else if (key.equals("_properties"))
		{
			try {
				return wrap(_propertyMetadataRegistry.getPropertyMetadataMap(object.getClass(), FreemarkerPropertyMetadata.class));
			} catch (AdapterNotFoundException e) {
				throw new TemplateModelException(e);
			}
		} else
		{
			return super.get(key);
		}
	}
	
	
}
