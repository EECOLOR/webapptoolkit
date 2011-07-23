package ee.webAppToolkit.example.website.validation;

import java.text.MessageFormat;

import com.google.inject.Inject;

import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.parameters.ValidationResult;

public class LengthValidator implements AnnotationValidator<String, Length>
{
	private String _min;
	private String _max;
	
	@Inject
	public LengthValidator(
			@LocalizedString("validation.length.min") String min,
			@LocalizedString("validation.length.max") String max)
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
			validationResult.setErrorMessage(MessageFormat.format(_min, minLength));
		}
		
		int maxLength = validationAnnotation.max();
		if (maxLength > 0 && length > maxLength)
		{
			validationResult.setErrorMessage(MessageFormat.format(_max, maxLength));
		}
		
		if (!validationResult.getValidated())
		{
			validationResult.setOriginalValue(value);
		}
		
		return validationResult;
	}
	
}
