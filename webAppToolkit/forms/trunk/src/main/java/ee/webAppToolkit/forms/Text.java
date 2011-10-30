package ee.webAppToolkit.forms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Text
{
	int maxLength() default 0;
	boolean readonly() default false;
}
