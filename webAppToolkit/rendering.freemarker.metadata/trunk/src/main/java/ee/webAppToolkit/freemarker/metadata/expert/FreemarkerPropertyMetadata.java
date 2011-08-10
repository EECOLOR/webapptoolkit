package ee.webAppToolkit.freemarker.metadata.expert;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface FreemarkerPropertyMetadata {
	public String getName();
	public Map<String, Annotation> getAnnotations();
	public Class<?> getType();
	public Map<String, FreemarkerPropertyMetadata> getProperties();
	public boolean getIsList();
	public Map<String, FreemarkerPropertyMetadata> getComponentProperties();
}
