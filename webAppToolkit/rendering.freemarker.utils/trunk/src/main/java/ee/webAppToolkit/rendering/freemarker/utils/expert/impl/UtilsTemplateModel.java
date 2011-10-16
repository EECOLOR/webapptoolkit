package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.metadataUtils.PropertyMetadataRegistry;
import ee.metadataUtils.impl.AdapterNotFoundException;
import ee.webAppToolkit.rendering.freemarker.utils.expert.CustomObjectTemplateModel;
import ee.webAppToolkit.rendering.freemarker.utils.expert.EnumerationResolver;
import ee.webAppToolkit.rendering.freemarker.utils.expert.FreemarkerPropertyMetadata;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class UtilsTemplateModel extends StringModel implements CustomObjectTemplateModel
{
	private EnumerationResolver _enumerationResolver;
	private PropertyMetadataRegistry _propertyMetadataRegistry;
	
	@Inject
	public UtilsTemplateModel(
			PropertyMetadataRegistry propertyMetadataRegistry,
			EnumerationResolver enumerationResolver, 
			@Assisted Object object, 
			@Assisted ObjectWrapper wrapper)
	{
		super(object, (BeansWrapper) wrapper);
		
		_enumerationResolver = enumerationResolver;
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
		} else if (key.equals("_displayProperties"))
		{
			try {
				
				Map<String, FreemarkerPropertyMetadata> propertyMetadata = _propertyMetadataRegistry.getPropertyMetadataMap(object.getClass(), FreemarkerPropertyMetadata.class);
				propertyMetadata = Maps.filterValues(propertyMetadata, new DisplayOnly());
				return wrap(propertyMetadata);
			} catch (AdapterNotFoundException e) {
				throw new TemplateModelException(e);
			}
		} else if (key.endsWith("_enumeration"))
		{
			key = key.replace("_enumeration", "");
			
			FreemarkerPropertyMetadata propertyMetadata;
			try {
				propertyMetadata = _propertyMetadataRegistry.getPropertyMetadata(object.getClass(), key, FreemarkerPropertyMetadata.class);
			} catch (AdapterNotFoundException e) {
				throw new TemplateModelException(e);
			}
			
			if (propertyMetadata == null)
			{
				throw new TemplateModelException("Could not find property metadata for '" + key + "' in " + object.getClass().getName());
			} else
			{
				return wrap(_enumerationResolver.resolve(propertyMetadata.getType()));
			}
		} else
		{
			return super.get(key);
		}
	}
	
	
}
