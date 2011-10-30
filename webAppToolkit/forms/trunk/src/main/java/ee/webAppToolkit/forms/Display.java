package ee.webAppToolkit.forms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ee.webAppToolkit.localization.LocalizedString;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Display
{
	enum Type {
		/**
		 * Displays the property as hidden
		 */
		HIDDEN,
		
		/**
		 * Displays the property as text
		 */
		TEXT, 
		
		/**
		 * Displays the property as a list
		 */
		LIST,
		
		/**
		 * Displays the property as a text area
		 */
		TEXTAREA, 
		
		/**
		 * Displays the property as a date
		 */
		DATE, 
		
		/**
		 * Displays the property as a component
		 */
		COMPONENT, 
		
		/**
		 * Displays the property as a component list, note that by default an empty component is displayed. 
		 * If you want to change the default behavior, annotate the property with @ComponentList 
		 */
		COMPONENT_LIST, 
		
		/**
		 * Displays the property as checkbox
		 */
		CHECKBOX
	};
	
	LocalizedString label() default @LocalizedString("");
	int order() default 0;
	Type type() default Type.TEXT;
}
