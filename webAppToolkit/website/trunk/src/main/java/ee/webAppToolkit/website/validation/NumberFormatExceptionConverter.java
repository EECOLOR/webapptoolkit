package ee.webAppToolkit.website.validation;

import java.text.MessageFormat;

import com.google.inject.Inject;
import com.google.inject.Provider;

import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.ExceptionConverter;
import ee.webAppToolkit.parameters.ValidationResult;

public class NumberFormatExceptionConverter implements ExceptionConverter<NumberFormatException>
{
	private Provider<String> _conversion;
	
	@Inject
	public NumberFormatExceptionConverter(
			@LocalizedString("validation.number.conversion") Provider<String> conversion)
	{
		_conversion = conversion;
	}
	
	@Override
	public ValidationResult convert(NumberFormatException e, Object originalValue)
	{
		return new ValidationResult(originalValue, MessageFormat.format(_conversion.get(), originalValue));
	}
}
