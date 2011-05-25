package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ee.parameterConverter.Validator;

public class ComboValidator implements ee.parameterConverter.Validator 
{
	private List<ee.parameterConverter.Validator> _validators;
	
	public ComboValidator(ee.parameterConverter.Validator validator)
	{
		_validators = new ArrayList<ee.parameterConverter.Validator>();
	}
	
	@Override
	public void validate(Object value) throws Throwable {
		
		ValidationResult validationResults = null;
		
		Iterator<Validator> iterator = _validators.iterator();
		
		while (iterator.hasNext())
		{
			Validator validator = iterator.next();
			try
			{
				validator.validate(value);
			} catch (ValidationResultException e)
			{
				ValidationResult validationResult = e.getValidationResult();
				if (validationResults == null)
				{
					validationResults = validationResult;
				} else if (validationResults.isList())
				{
					validationResults.asList().add(validationResult);
				} else
				{
					ValidationResultContainer validationResultContainer = new ValidationResultContainer(); 
					validationResultContainer.add(validationResults);
					validationResultContainer.add(validationResult);
					
					validationResults = validationResultContainer;
				}
			}
		}
		
		if (validationResults != null)
		{
			throw new ValidationResultException(value, validationResults);
		}
	}

	public void addValidator(ee.parameterConverter.Validator validator)
	{
		_validators.add(validator);
	}
}
