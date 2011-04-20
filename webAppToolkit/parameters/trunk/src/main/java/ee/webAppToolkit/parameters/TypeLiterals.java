package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

public class TypeLiterals
{
	@SuppressWarnings("unchecked")
	static public <T> TypeLiteral<Converter<T, ?>> converter(Class<T> from, Type to)
	{
		return (TypeLiteral<Converter<T, ?>>) TypeLiteral.get(Types.newParameterizedType(Converter.class, from, to));
	}
	
	@SuppressWarnings("unchecked")
	static public <T extends Throwable> TypeLiteral<ExceptionConverter<T>> exceptionConverter(Class<T> type)
	{
		return (TypeLiteral<ExceptionConverter<T>>) TypeLiteral.get(Types.newParameterizedType(ExceptionConverter.class, type));
	}
	
	@SuppressWarnings("unchecked")
	static public <T extends Annotation> TypeLiteral<Validator<?, T>> validator(Type type, Class<T> annotationType)
	{
		return (TypeLiteral<Validator<?, T>>) TypeLiteral.get(Types.newParameterizedType(Validator.class, type, annotationType));
	}
}
