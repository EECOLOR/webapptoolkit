package ee.webAppToolkit.parameters.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ee.webAppToolkit.parameters.DefaultValueProvider;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Default
{
	Class<? extends DefaultValueProvider<?>> value();
}
