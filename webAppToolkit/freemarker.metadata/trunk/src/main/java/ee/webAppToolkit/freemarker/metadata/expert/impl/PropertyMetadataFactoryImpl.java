package ee.webAppToolkit.freemarker.metadata.expert.impl;

import java.lang.reflect.Field;

import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.PropertyMetadataFactory;

public class PropertyMetadataFactoryImpl implements PropertyMetadataFactory {

	@Override
	public PropertyMetadata create(Field field) {
		return new PropertyMetadataImpl(field);
	}

}
