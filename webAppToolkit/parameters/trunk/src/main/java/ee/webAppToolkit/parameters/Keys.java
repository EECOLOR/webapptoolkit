package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.google.inject.Key;

/**
 * Utility method to create keys for some parameterized types
 * 
 * @author EECOLOR
 */
public class Keys
{
	static public <T> Key<Converter<T, ?>> converter(Class<T> from, Type to)
	{
		return Key.get(TypeLiterals.converter(from, to));
	}	
	
	static public <T extends Throwable> Key<ExceptionConverter<T>> exceptionConverter(Class<T> type)
	{
		return Key.get(TypeLiterals.exceptionConverter(type));
	}
	
	static public <T extends Annotation> Key<Validator<?, T>> validator(Type type, Class<T> annotationType)
	{
		return Key.get(TypeLiterals.validator(type, annotationType));
	}
}
