package ee.webAppToolkit.freemarker.forms.expert.impl;

import ee.webAppToolkit.parameters.ValidationResults;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class ValidationResultsTemplateModel extends SimpleHash implements TemplateHashModel
{
	private static final long serialVersionUID = 1L;
	private ValidationResults _validationResults;

	public ValidationResultsTemplateModel(ValidationResults validationResults, ObjectWrapper objectWrapper)
	{
		super(validationResults, objectWrapper);
		
		_validationResults = validationResults;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException
	{
		if ("validated".equals(key))
		{
			return wrap(_validationResults.getValidated());
		} else if ("originalValue".equals(key))
		{
			return wrap(_validationResults.getOriginalValue());
		} else
		{
			return super.get(key);
		}
	}
}
