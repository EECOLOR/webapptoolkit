package ee.webAppToolkit.render.expert;

import com.google.inject.ImplementedBy;

@ImplementedBy(TemplateResolverImpl.class)
public interface TemplateResolver {
	public String resolveTemplate(Object controllerObject, String template);
}
