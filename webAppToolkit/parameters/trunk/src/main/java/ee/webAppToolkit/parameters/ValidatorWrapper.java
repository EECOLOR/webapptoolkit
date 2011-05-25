package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;

import ee.parameterConverter.Validator;

public class ValidatorWrapper implements Validator 
{
	private Annotation _annotation;
	private ee.webAppToolkit.parameters.Validator<Object, Annotation> _validator;

	public ValidatorWrapper(Annotation annotation, ee.webAppToolkit.parameters.Validator<Object, Annotation> validator)
	{
		_annotation = annotation;
		_validator = validator;
	}

	@Override
	public void validate(Object value) throws Throwable {
		ValidationResult validationResult = _validator.validate(value, _annotation);
		
		if (!validationResult.getValidated())
		{
			throw new ValidationResultException(value, validationResult);
		}
	}
	
	
}
