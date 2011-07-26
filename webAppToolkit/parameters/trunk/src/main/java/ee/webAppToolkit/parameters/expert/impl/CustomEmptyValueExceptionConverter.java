package ee.webAppToolkit.parameters.expert.impl;

import java.text.MessageFormat;

import ee.webAppToolkit.parameters.ExceptionConverter;
import ee.webAppToolkit.parameters.ValidationResult;
import ee.webAppToolkit.parameters.exceptions.CustomEmptyValueException;

public class CustomEmptyValueExceptionConverter implements ExceptionConverter<CustomEmptyValueException>
{
	@Override
	public ValidationResult convert(CustomEmptyValueException e, Object originalValue)
	{
		return new ValidationResult(originalValue, MessageFormat.format(e.getMessage(), originalValue));
	}
	
}
