package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;



public interface AnnotationValidator<T, U extends Annotation>
{
	public ValidationResult validate(T value, U validationAnnotation);
}
