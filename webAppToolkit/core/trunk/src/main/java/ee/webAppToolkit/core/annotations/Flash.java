package ee.webAppToolkit.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

import ee.webAppToolkit.core.expert.ActionArgumentAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@ActionArgumentAnnotation
@Target(ElementType.PARAMETER)
@BindingAnnotation
@Optional
public @interface Flash
{
	String value() default "";
}
