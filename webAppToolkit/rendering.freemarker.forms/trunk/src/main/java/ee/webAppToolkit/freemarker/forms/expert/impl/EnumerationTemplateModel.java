package ee.webAppToolkit.freemarker.forms.expert.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.metadataUtils.PropertyMetadataRegistry;
import ee.metadataUtils.impl.AdapterNotFoundException;
import ee.webAppToolkit.freemarker.forms.expert.EnumerationResolver;
import ee.webAppToolkit.freemarker.metadata.expert.FreemarkerPropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.impl.MetadataTemplateModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class EnumerationTemplateModel extends MetadataTemplateModel {

	private EnumerationResolver _enumerationResolver;
	private PropertyMetadataRegistry _propertyMetadataRegistry;
	
	@Inject
	public EnumerationTemplateModel(
			PropertyMetadataRegistry propertyMetadataRegistry,
			@Assisted Object object, 
			@Assisted ObjectWrapper wrapper,
			EnumerationResolver enumerationResolver)
	{
		super(propertyMetadataRegistry, object, wrapper);
		
		_enumerationResolver = enumerationResolver;
		_propertyMetadataRegistry = propertyMetadataRegistry;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		
		if (key == null)
		{
			return super.get(key);
		}
		
		if (_enumerationResolver != null && key.endsWith("_enumeration"))
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
		} else if (object instanceof FreemarkerPropertyMetadata && key.equals("enumeration"))
		{
			return wrap(_enumerationResolver.resolve(((FreemarkerPropertyMetadata) object).getType()));
		} else
		{
			try
			{
				return super.get(key);
			} catch (TemplateModelException e)
			{
				throw new TemplateModelException("An exception was throw, this might be caused by a missing @Display annotation.", e);
			}
		}
	}
	
}
