package ee.webAppToolkit.freemarker.metadata.expert;

import freemarker.template.ObjectWrapper;

public interface CustomObjectTemplateModelFactory
{
	public CustomObjectTemplateModel create(Object object, ObjectWrapper objectWrapper);
}
