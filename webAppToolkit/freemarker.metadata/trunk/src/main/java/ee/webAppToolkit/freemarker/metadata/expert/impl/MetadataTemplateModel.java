package ee.webAppToolkit.freemarker.metadata.expert.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.webAppToolkit.freemarker.metadata.expert.CustomObjectTemplateModel;
import ee.webAppToolkit.freemarker.metadata.expert.MetadataRegistry;
import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadata;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class MetadataTemplateModel extends StringModel implements CustomObjectTemplateModel
{
	private MetadataRegistry _metadataRegistry;
	
	@Inject
	public MetadataTemplateModel(
			MetadataRegistry propertyMetadataRegistry,
			@Assisted Object object, 
			@Assisted ObjectWrapper wrapper)
	{
		super(object, (BeansWrapper) wrapper);
		
		_metadataRegistry = propertyMetadataRegistry;
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
			//find the setter for the requested property
			key = key.replace("_metadata", "");
			
			PropertyMetadata propertyMetadata = _metadataRegistry.getPropertyMetadata(object.getClass(), key);
			
			if (propertyMetadata == null)
			{
				throw new TemplateModelException("Could not find property metadata for '" + key + "'");
			} else
			{
				return wrap(propertyMetadata);
			}
		} else if (key.endsWith("_properties"))
		{
			key = key.replace("_properties", "");
			TemplateModel templateModel = super.get(key);

			if (templateModel instanceof MetadataTemplateModel)
			{
				MetadataTemplateModel metadataTemplateModel = (MetadataTemplateModel) templateModel;
				
				return wrap(_metadataRegistry.getPropertyMetadata(metadataTemplateModel.object.getClass()));
			} else
			{
				return super.get(key);
			}
		} else
		{
			return super.get(key);
		}
	}
	
	
}
