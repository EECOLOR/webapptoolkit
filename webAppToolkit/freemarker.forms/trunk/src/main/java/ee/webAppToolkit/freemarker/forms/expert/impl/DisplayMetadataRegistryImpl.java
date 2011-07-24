package ee.webAppToolkit.freemarker.forms.expert.impl;

import java.lang.reflect.Field;

import javax.inject.Inject;

import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadataFactory;
import ee.webAppToolkit.freemarker.metadata.expert.impl.MetadataRegistryImpl;

public class DisplayMetadataRegistryImpl extends MetadataRegistryImpl {

	@Inject
	public DisplayMetadataRegistryImpl(PropertyMetadataFactory propertyMetadataFactory)
	{
		super(propertyMetadataFactory);
	}
	
	@Override
	protected boolean isAllowed(Field field) {
		//TODO make sure this is in the documentation
		return field.isAnnotationPresent(Display.class) && super.isAllowed(field);
	}
	
}
