package ee.webAppToolkit.parameters.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ee.parameterConverter.Validator;
import ee.webAppToolkit.parameters.ValidationResult;
import ee.webAppToolkit.parameters.ValidationResultContainer;
import ee.webAppToolkit.parameters.ValidationResultException;

public class ComboValidator implements Validator 
{
	private List<Validator> _validators;
	
	public ComboValidator(Validator validator)
	{
		_validators = new ArrayList<Validator>();
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

	public void addValidator(Validator validator)
	{
		_validators.add(validator);
	}
}
