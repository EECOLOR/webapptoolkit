package ee.webAppToolkit.example.website.validation;

import java.text.MessageFormat;

import com.google.inject.Inject;
import com.google.inject.Provider;

import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.parameters.ValidationResult;

public class LengthValidator implements AnnotationValidator<String, Length>
{
	private Provider<String> _min;
	private Provider<String> _max;
	
	@Inject
	public LengthValidator(
			@LocalizedString("validation.length.min") Provider<String> min,
			@LocalizedString("validation.length.max") Provider<String> max)
	{
		_min = min;
		_max = max;
	}
	
	/* (non-Javadoc)
	 * @see ee.webAppToolkit.Validator#validate(java.lang.Object, ee.webAppToolkit.annotation.ValidationAnnotation)
	 */
	@Override
	public ValidationResult validate(String value, Length validationAnnotation)
	{
		int length = value.length();
		
		ValidationResult validationResult = new ValidationResult();
		
		int minLength = validationAnnotation.min();
		if (minLength > 0 && length < minLength)
		{
			validationResult.setErrorMessage(MessageFormat.format(_min.get(), minLength));
		}
		
		int maxLength = validationAnnotation.max();
		if (maxLength > 0 && length > maxLength)
		{
			validationResult.setErrorMessage(MessageFormat.format(_max.get(), maxLength));
		}
		
		if (!validationResult.getValidated())
		{
			validationResult.setOriginalValue(value);
		}
		
		return validationResult;
	}
	
}
