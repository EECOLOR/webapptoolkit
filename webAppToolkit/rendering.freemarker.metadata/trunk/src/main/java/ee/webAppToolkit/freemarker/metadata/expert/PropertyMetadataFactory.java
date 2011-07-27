package ee.webAppToolkit.freemarker.metadata.expert;

import java.lang.reflect.Field;

public interface PropertyMetadataFactory {
	public PropertyMetadata create(Field field);
}
