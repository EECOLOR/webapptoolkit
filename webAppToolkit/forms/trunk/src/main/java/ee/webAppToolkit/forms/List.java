package ee.webAppToolkit.forms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ee.webAppToolkit.localization.LocalizedString;

@Retention(RetentionPolicy.RUNTIME)
public @interface List
{
	LocalizedString defaultLabel() default @LocalizedString("");
}
