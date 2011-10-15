package ee.webAppToolkit.freemarker.forms.expert.impl;

import java.util.Set;

import ee.webAppToolkit.parameters.ValidationResults;
import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

public class ValidationResultsTemplateModel extends BeanModel implements TemplateHashModel
{
	private ValidationResults _validationResults;

	public ValidationResultsTemplateModel(ValidationResults validationResults, BeansWrapper beansWrapper)
	{
		super(validationResults, beansWrapper);
		
		_validationResults = validationResults;
	}

	@Override
	protected Set<String> keySet() {
		return _validationResults.keySet();
	}
}
