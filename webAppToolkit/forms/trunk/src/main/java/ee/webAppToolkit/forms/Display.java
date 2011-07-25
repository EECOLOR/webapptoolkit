package ee.webAppToolkit.forms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ee.webAppToolkit.localization.LocalizedString;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Display
{
	enum Type {HIDDEN, TEXT, LIST, TEXTAREA, DATE, COMPONENT, COMPONENT_LIST};
	
	LocalizedString label() default @LocalizedString("");
	int order() default 0;
	Type type() default Type.TEXT;
}
