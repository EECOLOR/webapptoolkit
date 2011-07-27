package ee.webAppToolkit.rendering.expert;

import com.google.inject.ImplementedBy;

@ImplementedBy(TemplateResolverImpl.class)
public interface TemplateResolver {
	public String resolveTemplate(Object controllerObject, String template);
}
