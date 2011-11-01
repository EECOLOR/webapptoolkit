package ee.webAppToolkit.parameters.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Converter
{
	Class<? extends ee.parameterConverter.Converter<String[], ?>> value();
}
