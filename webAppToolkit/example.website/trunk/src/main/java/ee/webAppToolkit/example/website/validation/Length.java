package ee.webAppToolkit.example.website.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ee.webAppToolkit.parameters.annotations.ValidationAnnotation;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidationAnnotation
public @interface Length
{
	int max() default 0;
	int min() default 0;
}
