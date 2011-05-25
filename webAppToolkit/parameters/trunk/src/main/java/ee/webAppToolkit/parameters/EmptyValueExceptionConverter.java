package ee.webAppToolkit.parameters;

import java.text.MessageFormat;

import com.google.inject.Inject;

import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.core.locale.LocalizedString;

public class EmptyValueExceptionConverter implements ExceptionConverter<EmptyValueException>
{
	private String _empty;
	
	@Inject
	public EmptyValueExceptionConverter(
			@LocalizedString("validation.empty") String empty)
	{
		_empty = empty;
	}
	
	@Override
	public ValidationResult convert(EmptyValueException e, Object originalValue)
	{
		return new ValidationResult(originalValue, MessageFormat.format(_empty, originalValue));
	}
	
}
