package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;



public interface Validator<T, U extends Annotation>
{
	public ValidationResult validate(T value, U validationAnnotation);
}
