package ee.webAppToolkit.freemarker.forms.expert.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.webAppToolkit.freemarker.forms.expert.EnumerationResolver;
import ee.webAppToolkit.freemarker.metadata.expert.MetadataRegistry;
import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.impl.MetadataTemplateModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class EnumerationTemplateModel extends MetadataTemplateModel {

	private EnumerationResolver _enumerationResolver;
	private MetadataRegistry _propertyMetadataRegistry;
	
	@Inject
	public EnumerationTemplateModel(
			MetadataRegistry propertyMetadataRegistry,
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
			
			PropertyMetadata propertyMetadata = _propertyMetadataRegistry.getPropertyMetadata(object.getClass(), key);
			
			if (propertyMetadata == null)
			{
				throw new TemplateModelException("Could not find property metadata for '" + key + "' in " + object.getClass().getName());
			} else
			{
				return wrap(_enumerationResolver.resolve(propertyMetadata.getType()));
			}
		} else if (object instanceof PropertyMetadata && key.equals("enumeration"))
		{
			return wrap(_enumerationResolver.resolve(((PropertyMetadata) object).getType()));
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
