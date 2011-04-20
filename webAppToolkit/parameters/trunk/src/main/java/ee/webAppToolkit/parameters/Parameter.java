package ee.webAppToolkit.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

import ee.webAppToolkit.core.expert.ActionArgumentAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ActionArgumentAnnotation
@BindingAnnotation
public @interface Parameter
{
	String value() default "";
}
