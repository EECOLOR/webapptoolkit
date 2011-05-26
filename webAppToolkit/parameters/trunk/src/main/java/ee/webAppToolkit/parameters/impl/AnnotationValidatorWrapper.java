package ee.webAppToolkit.parameters.impl;

import java.lang.annotation.Annotation;

import ee.parameterConverter.Validator;
import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.parameters.ValidationResult;
import ee.webAppToolkit.parameters.ValidationResultException;

public class AnnotationValidatorWrapper implements Validator 
{
	private Annotation _annotation;
	private AnnotationValidator<Object, Annotation> _validator;

	public AnnotationValidatorWrapper(Annotation annotation, AnnotationValidator<Object, Annotation> validator)
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
