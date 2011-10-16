package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import ee.webAppToolkit.parameters.ValidationResults;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;

public class ValidationResultsTemplateModelFactory implements ModelFactory
{

	@Override
	public TemplateModel create(Object obj, ObjectWrapper objectWrapper)
	{
		return new ValidationResultsTemplateModel((ValidationResults) obj, (BeansWrapper) objectWrapper);
	}
}
