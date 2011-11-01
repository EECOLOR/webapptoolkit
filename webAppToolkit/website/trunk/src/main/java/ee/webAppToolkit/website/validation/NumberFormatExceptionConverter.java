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
	
	//TODO make sure all localized strings are request through a provider, otherwise they could mis runtime changes of the locale
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
