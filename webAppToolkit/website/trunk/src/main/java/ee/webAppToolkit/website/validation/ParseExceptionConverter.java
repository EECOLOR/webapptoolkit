package ee.webAppToolkit.website.validation;

import java.text.MessageFormat;
import java.text.ParseException;

import com.google.inject.Inject;
import com.google.inject.Provider;

import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.ExceptionConverter;
import ee.webAppToolkit.parameters.ValidationResult;

public class ParseExceptionConverter implements ExceptionConverter<ParseException> {
	private Provider<String> _conversion;
	
	@Inject
	public ParseExceptionConverter(
			@LocalizedString("validation.date.conversion") Provider<String> conversion)
	{
		_conversion = conversion;
	}
	
	@Override
	public ValidationResult convert(ParseException e, Object originalValue)
	{
		return new ValidationResult(originalValue, MessageFormat.format(_conversion.get(), originalValue));
	}
}
