package ee.webAppToolkit.parameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.EmptyValueException;

//TODO this class will need heavy refactoring. For sanity I am writing all methods out during development.
public class ParameterConverterImpl implements ParameterConverter {

	private Injector _injector;
	private TypeKindResolver _typeKindResolver;

	@Inject
	public ParameterConverterImpl(Injector injector, TypeKindResolver typeKindResolver)
	{
		_injector = injector;
		_typeKindResolver = typeKindResolver;
	}

	@Override
	public Object convert(Map<String, String[]> parameters, Type type,
			ValidationResults validationResults, String parameterKey) throws EmptyValueException,
			ConfigurationException {

		// determine the target type kind
		TypeKind typeKind = _typeKindResolver.resolve(type);

		switch (typeKind) {
		case Simple:
			return _convertToSimple(parameters, type, validationResults, parameterKey);
		case List:
			return _convertToList(parameters, type, validationResults, parameterKey);
		case Map:
			return _convertMap(parameters, type, validationResults, parameterKey);
			break;
		case Complex:
			break;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private Converter<String[], ?> _getArrayConverter(Type type) {

		TypeLiteral<? extends Converter<?, ?>> converterTypeLiteral = Keys.converter(
				String[].class, type).getTypeLiteral();

		List<?> bindings = _injector.findBindingsByType(converterTypeLiteral);

		if (bindings.size() > 0) {
			return (Converter<String[], ?>) ((Binding<?>) bindings.get(0)).getProvider().get();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private Converter<String, ?> _getConverter(Type type) {
		TypeLiteral<? extends Converter<?, ?>> converterTypeLiteral = Keys.converter(String.class,
				type).getTypeLiteral();

		List<?> bindings = _injector.findBindingsByType(converterTypeLiteral);

		if (bindings.size() > 0) {
			return (Converter<String, ?>) ((Binding<?>) bindings.get(0)).getProvider().get();
		}

		return null;
	}

	private <U extends Throwable> ExceptionConverter<U> _getExceptionConverter(Class<U> exceptionType)
	{
		return _injector.getBinding(Keys.exceptionConverter(exceptionType)).getProvider().get();
	}

	@SuppressWarnings("unchecked")
	private <U extends Throwable> ValidationResult _getValidationResult(U exception, Object originalValue) {
		if (exception instanceof ConversionFailedException) {
			// check if we have a cause
			Throwable cause = exception.getCause();
			if (cause != null) {
				exception = (U) cause;
			}
		}

		Class<U> exceptionType = (Class<U>) exception.getClass();
		
		ExceptionConverter<U> exceptionConverter = _getExceptionConverter(exceptionType);
		return exceptionConverter.convert(exception, originalValue);
	}

	private Object _convertToList(Map<String, String[]> parameters, Type type, ValidationResults validationResults, String parameterKey) throws ConfigurationException, EmptyValueException
	{
		if (parameterKey.length() == 0) {
			throw new ConfigurationException(
					"Could not supply parameter for argument with @Parameter annotation, it is probably missing a parameter value, for example: @Parameter(\"id\"). Argument type: "
							+ type);
		}
		
		if (parameters.containsKey(parameterKey)) {
			String[] parameterValue = parameters.get(parameterKey);

			// check if an array converter was registered
			Converter<String[], ?> converter = _getArrayConverter(type);

			if (converter != null) {
				try {
					return converter.convert(parameterValue);
				} catch (ConversionFailedException e) {
					validationResults
							.put(parameterKey, _getValidationResult(e, parameterValue));
					return null;
				}
			}

			// use a generic converter
			try {
				return _convertList(parameterValue, type);
			} catch (ConversionFailedException e) {
				validationResults.put(parameterKey, _getValidationResult(e, parameterValue));
				return null;
			}
		} else {
			throw new EmptyValueException("Could not find parameter with key '" + parameterKey
					+ "'");
		}
	}
	
	private Object _convertList(String[] values, Type type) throws ConversionFailedException
	{
		
	}
	
	private Object _convertToSimple(Map<String, String[]> parameters, Type type,
			ValidationResults validationResults, String parameterKey)
			throws ConfigurationException, EmptyValueException {

		if (parameterKey.length() == 0) {
			throw new ConfigurationException(
					"Could not supply parameter for argument with @Parameter annotation, it is probably missing a parameter value, for example: @Parameter(\"id\"). Argument type: "
							+ type);
		}

		if (parameters.containsKey(parameterKey)) {
			String[] parameterValue = parameters.get(parameterKey);

			if (parameterValue.length == 0) {
				throw new EmptyValueException("Could not find parameter with key '" + parameterKey
						+ "'");
			}

			if (parameterValue.length == 1) {
				// check if a non array converter was registered
				Converter<String, ?> converter = _getConverter(type);

				String singleParameterValue = parameterValue[0];

				if (converter != null) {
					try {
						return converter.convert(parameterValue[0]);
					} catch (ConversionFailedException e) {
						validationResults.put(parameterKey,
								_getValidationResult(e, singleParameterValue));
						return null;
					}
				}

				// use a generic converter
				try {
					return _convertSimple(singleParameterValue, type);
				} catch (ConversionFailedException e) {
					validationResults.put(parameterKey,
							_getValidationResult(e, singleParameterValue));
					return null;
				}
			} else {
				// check if an array converter was registered
				Converter<String[], ?> converter = _getArrayConverter(type);

				if (converter != null) {
					try {
						return converter.convert(parameterValue);
					} catch (ConversionFailedException e) {
						validationResults
								.put(parameterKey, _getValidationResult(e, parameterValue));
						return null;
					}
				}

				// use a generic converter
				try {
					return _convertSimple(parameterValue, type);
				} catch (ConversionFailedException e) {
					validationResults.put(parameterKey, _getValidationResult(e, parameterValue));
					return null;
				}
			}
		} else {
			throw new EmptyValueException("Could not find parameter with key '" + parameterKey
					+ "'");
		}
	}

	private Object _convertSimple(String value, Type type) throws ConversionFailedException, EmptyValueException {
		if (value == null)
		{
			throw new EmptyValueException("The given value is null");
		}
		
		if (value.length() == 0)
		{
			throw new EmptyValueException("The given String value has no length");
		}
		
		Class<?> typeClass;
		
		if (type instanceof Class)
		{
			typeClass = (Class<?>) type;
		} else
		{
			throw new ConversionFailedException("Can only convert to Class<?> types, given type: " + type + " (" + type.getClass().getName() + ")");
		}
		
		Class<String> stringClass = String.class;
		
		if (typeClass.isAssignableFrom(stringClass))
		{
			return value;
		}
		
		if (typeClass.isPrimitive())
		{
			typeClass = ClassUtils.getWrapper(typeClass);
		}
		
		try
		{
			//try a constructor
			Constructor<?> constructor = typeClass.getConstructor(stringClass);
			return constructor.newInstance(value);
		} catch (NoSuchMethodException e)
		{
			try
			{
				//Some classes have a valueOf method
				Method method = typeClass.getMethod("valueOf", stringClass);

				//only call it if it is static and returns T
				if (!typeClass.isAssignableFrom(method.getReturnType()))
				{
					throw new ConversionFailedException("The return type of the " + typeClass.getName() + "#valueOf(String) method is not compatible with '" + typeClass.getName() + "'");
				} else if (!Modifier.isStatic(method.getModifiers()))
				{
					throw new NoSuchMethodException(typeClass.getName() + "#valueOf(String)");
				} else
				{
					return method.invoke(null, value);
				}
			} catch (ConversionFailedException e1)
			{
				throw e1;
			} catch (NoSuchMethodException e1)
			{
				throw new ConversionFailedException("Could not find a way to convert " + value + "' to '" + typeClass.getName() + "'");
			} catch (Exception e1)
			{
				throw new ConversionFailedException("Conversion failed while calling the ", e1);
			}
		} catch (InvocationTargetException e)
		{
			throw new ConversionFailedException("Conversion failed while calling the constructor of '" + typeClass.getName() + "'", e.getTargetException());
		} catch (Exception e)
		{
			throw new ConversionFailedException("Conversion failed while calling the constructor of '" + typeClass.getName() + "'", e);
		}
	}

	private Object _convertSimple(String[] values, Type type) throws ConversionFailedException, EmptyValueException {
		if (value == null)
		{
			throw new EmptyValueException("The given value is null");
		}
		
		Class<?> valueType = value.getClass();
		if (type.isAssignableFrom(valueType))
		{
			return type.cast(value);
		}
		
		boolean isArray = valueType.isArray();
		
		if (String.class.isAssignableFrom(type) && !isArray)
		{
			return type.cast(value.toString());
		}
		
		if (isArray)
		{
			Object[] valueArray = (Object[]) value;
			switch (valueArray.length)
			{
				case 0:
				{
					throw new EmptyValueException("The given array value has no length");
				}
				case 1:
				{
					value = valueArray[0];
					valueType = value.getClass();
					break;
				}
			}
		}
		
		//if it's a String check the length
		if (value instanceof String && ((String) value).length() == 0)
		{
			throw new EmptyValueException("The given String value has no length");
		}
		
		if (type.isPrimitive())
		{
			@SuppressWarnings("unchecked")
			Class<T> wrapperClass = (Class<T>) ClassUtils.getWrapper(type);
			type = wrapperClass;
		}
		
		//if it's a Character[] array check the length
		if (value instanceof char[] && ((char[]) value).length == 0)
		{
			throw new EmptyValueException("The given char[] value has no length");
		}
		
		try
		{
			//try a constructor
			Constructor<T> constructor = type.getConstructor(valueType);
			return constructor.newInstance(value);
		} catch (NoSuchMethodException e)
		{
			try
			{
				//Some classes have a valueOf method
				Method method = type.getMethod("valueOf", valueType);

				//only call it if it is static and returns T
				if (!type.isAssignableFrom(method.getReturnType()))
				{
					throw new ConversionFailedException("The return type of the valueOf(" + valueType.getName() + ") method is not compatible with '" + type.getClass().getName() + "'");
				} else if (!Modifier.isStatic(method.getModifiers()))
				{
					throw new NoSuchMethodException();
				} else
				{
					return type.cast(method.invoke(null, value));
				}
			} catch (ConversionFailedException e1)
			{
				throw e1;
			} catch (NoSuchMethodException e1)
			{
				throw new ConversionFailedException("Could not find a way to convert " + (isArray ? "array '" + Arrays.toString((Object[]) value) : "'" + value) + "' (" + value.getClass().getName() + ") to '" + type.getName() + "'");
			} catch (Exception e1)
			{
				throw new ConversionFailedException("Conversion failed while calling the ", e1);
			}
		} catch (InvocationTargetException e)
		{
			throw new ConversionFailedException("Conversion failed while calling the constructor of '" + type.getName() + "'", e.getTargetException());
		} catch (Exception e)
		{
			throw new ConversionFailedException("Conversion failed while calling the constructor of '" + type.getName() + "'", e);
		}
	}
	
}
