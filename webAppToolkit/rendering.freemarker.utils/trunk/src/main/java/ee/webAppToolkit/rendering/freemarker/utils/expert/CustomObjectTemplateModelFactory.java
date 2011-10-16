package ee.webAppToolkit.rendering.freemarker.utils.expert;

import freemarker.template.ObjectWrapper;

public interface CustomObjectTemplateModelFactory
{
	public CustomObjectTemplateModel create(Object object, ObjectWrapper objectWrapper);
}
