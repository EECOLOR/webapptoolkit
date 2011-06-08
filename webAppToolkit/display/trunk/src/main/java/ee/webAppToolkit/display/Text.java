package ee.webAppToolkit.display;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Text
{
	int maxLength() default 0;
}
