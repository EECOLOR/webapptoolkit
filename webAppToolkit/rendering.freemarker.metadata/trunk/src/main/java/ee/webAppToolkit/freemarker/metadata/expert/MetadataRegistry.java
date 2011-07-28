package ee.webAppToolkit.freemarker.metadata.expert;

import java.util.Map;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.freemarker.metadata.expert.impl.MetadataRegistryImpl;

@ImplementedBy(MetadataRegistryImpl.class)
public interface MetadataRegistry {
	public PropertyMetadata getPropertyMetadata(Class<?> type, String propertyName);
	public Map<String, PropertyMetadata> getPropertyMetadata(Class<?> class1);
}
