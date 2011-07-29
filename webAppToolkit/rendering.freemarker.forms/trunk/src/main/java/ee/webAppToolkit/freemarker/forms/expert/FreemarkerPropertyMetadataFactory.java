package ee.webAppToolkit.freemarker.forms.expert;

import ee.metadataUtils.PropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.FreemarkerPropertyMetadata;

public interface FreemarkerPropertyMetadataFactory {
	public FreemarkerPropertyMetadata create(PropertyMetadata propertyMetadata);
}
