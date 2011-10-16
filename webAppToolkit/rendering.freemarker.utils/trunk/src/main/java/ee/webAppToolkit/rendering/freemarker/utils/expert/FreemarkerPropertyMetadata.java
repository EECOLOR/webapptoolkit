package ee.webAppToolkit.rendering.freemarker.utils.expert;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public interface FreemarkerPropertyMetadata {
	public String getName();
	public Map<String, Annotation> getAnnotations();
	public Class<?> getType();
	public Map<String, FreemarkerPropertyMetadata> getProperties();
	public Map<String, FreemarkerPropertyMetadata> getDisplayProperties();
	public boolean getIsList();
	public Map<String, FreemarkerPropertyMetadata> getComponentProperties();
	public Map<String, FreemarkerPropertyMetadata> getComponentDisplayProperties();
	public List<?> getEnumeration();
}
