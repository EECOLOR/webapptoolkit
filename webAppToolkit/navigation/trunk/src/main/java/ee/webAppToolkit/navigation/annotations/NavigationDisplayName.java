package ee.webAppToolkit.navigation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ee.webAppToolkit.localization.LocalizedString;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NavigationDisplayName {
	LocalizedString value();
}
