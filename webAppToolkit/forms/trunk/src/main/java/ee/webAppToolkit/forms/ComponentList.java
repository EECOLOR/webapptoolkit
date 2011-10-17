package ee.webAppToolkit.forms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentList
{
	/**
	 * If set to true an empty component will be rendered
	 */
	boolean addEmptyComponent() default true;
}
