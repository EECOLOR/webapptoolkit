package ee.webAppToolkit.parameters.expert.impl;

import java.text.MessageFormat;

import com.google.inject.Inject;
import com.google.inject.Provider;

import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.ExceptionConverter;
import ee.webAppToolkit.parameters.ValidationResult;

public class EmptyValueExceptionConverter implements ExceptionConverter<EmptyValueException>
{
	private Provider<String> _empty;
	
	@Inject
	public EmptyValueExceptionConverter(
			@LocalizedString("validation.empty") Provider<String> empty)
	{
		_empty = empty;
	}
	
	@Override
	public ValidationResult convert(EmptyValueException e, Object originalValue)
	{
		return new ValidationResult(originalValue, MessageFormat.format(_empty.get(), originalValue));
	}
	
}
