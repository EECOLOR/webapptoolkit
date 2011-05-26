package ee.webAppToolkit.parameters.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.ConfigurationException;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

import ee.parameterConverter.Validator;
import ee.parameterConverter.impl.PropertyMetadataImpl;
import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.parameters.AnnotationValidatorResolver;
import ee.webAppToolkit.parameters.Default;
import ee.webAppToolkit.parameters.DefaultValueProvider;
import ee.webAppToolkit.parameters.ValidationAnnotation;

public class ExtendedPropertyMetadataImpl extends PropertyMetadataImpl {

	private Injector _injector;
	private DefaultValueProvider<?> _defaultValueProvider;
	private Validator _validator;
	private AnnotationValidatorResolver _annotationValidatorResolver;
	
	@Inject
	public ExtendedPropertyMetadataImpl(Injector injector, AnnotationValidatorResolver annotationValidatorResolver, @Named("optionalAnnotationName") String optionalAnnotationName, @Assisted Field field) throws ConfigurationException
	{
		super(field, optionalAnnotationName);
		
		_annotationValidatorResolver = annotationValidatorResolver;

		_processAnnotations(field.getAnnotations());
	}
	
	private void _processAnnotations(Annotation[] annotations) throws ConfigurationException {
		
		Validator validator = null;
		
		for (Annotation annotation : annotations)
		{
			if (annotation instanceof Default)
			{
				_defaultValueProvider = _injector.getInstance(((Default) annotation).value());
			} else
			{
				Class<? extends Annotation> annotationType = annotation.annotationType();
				if (annotationType.isAnnotationPresent(ValidationAnnotation.class))
				{
					AnnotationValidator<Object, Annotation> annotationValidator = _annotationValidatorResolver.resolve(getType(), annotationType);
					
					if (annotationValidator == null)
					{
						throw new ConfigurationException("Could not find validator for " + getType() + " and annotation type " + annotationType);
					}
					
					AnnotationValidatorWrapper annotationValidatorWrapper = new AnnotationValidatorWrapper(annotation, annotationValidator);
					
					if (validator == null)
					{
						validator = annotationValidatorWrapper;
					} else if (validator instanceof AnnotationValidatorWrapper)
					{
						ComboValidator comboValidator = new ComboValidator(validator);
						comboValidator.addValidator(annotationValidatorWrapper);
						validator = comboValidator;
					} else if (validator instanceof ComboValidator)
					{
						((ComboValidator) validator).addValidator(annotationValidatorWrapper);
					}
				}
			}
		}
		
		_validator = validator;
	}

	@Override
	public Object getDefaultValue(Object context) {
		if (_defaultValueProvider == null)
		{
			return null;
		} else
		{
			return _defaultValueProvider.provide(context);
		}
	}
	
	@Override
	public Validator getValidator() {
		return _validator;
	}
}
