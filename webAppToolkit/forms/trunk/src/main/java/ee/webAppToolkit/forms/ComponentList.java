package ee.webAppToolkit.forms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ee.webAppToolkit.localization.LocalizedString;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentList
{
	/**
	 * If set to true an empty component will be rendered
	 */
	boolean addEmptyComponent() default true;
	
	/**
	 * Creates a link as follows: <code>&lt;a href="${context}/remove${componentType}?id=${component.id}>${removeLinkLabel}&lt;a></code> 
	 */
	boolean createRemoveLink() default false;
	LocalizedString removeLinkLabel() default @LocalizedString("");
}
