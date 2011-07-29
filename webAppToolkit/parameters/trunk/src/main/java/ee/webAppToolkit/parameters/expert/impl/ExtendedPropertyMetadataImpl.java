package ee.webAppToolkit.parameters.expert.impl;

import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.ConfigurationException;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

import ee.metadataUtils.PropertyMetadata;
import ee.parameterConverter.Validator;
import ee.parameterConverter.guice.GuiceParameterPropertyMetadata;
import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.parameters.DefaultValueProvider;
import ee.webAppToolkit.parameters.annotations.Default;
import ee.webAppToolkit.parameters.annotations.ValidationAnnotation;
import ee.webAppToolkit.parameters.expert.AnnotationValidatorResolver;

public class ExtendedPropertyMetadataImpl extends GuiceParameterPropertyMetadata {

	private Injector _injector;
	private DefaultValueProvider<?> _defaultValueProvider;
	private Validator _validator;
	private AnnotationValidatorResolver _annotationValidatorResolver;

	@Inject
	public ExtendedPropertyMetadataImpl(Injector injector,
			AnnotationValidatorResolver annotationValidatorResolver,
			@Named("optionalAnnotationName") String optionalAnnotationName,
			@Assisted PropertyMetadata propertyMetadata) throws ConfigurationException {
		super(optionalAnnotationName, propertyMetadata);

		_injector = injector;
		_annotationValidatorResolver = annotationValidatorResolver;
		
		_processAnnotations(propertyMetadata.getAnnotations());
	}

	private void _processAnnotations(Annotation[] annotations) throws ConfigurationException {

		Validator validator = null;

		for (Annotation annotation : annotations) {
			if (annotation instanceof Default) {
				_defaultValueProvider = _injector.getInstance(((Default) annotation).value());
			} else {
				Class<? extends Annotation> annotationType = annotation.annotationType();
				if (annotationType.isAnnotationPresent(ValidationAnnotation.class)) {
					AnnotationValidator<Object, Annotation> annotationValidator = _annotationValidatorResolver
							.resolve(getGenericType(), annotationType);

					if (annotationValidator == null) {
						throw new ConfigurationException("Could not find validator for "
								+ getGenericType() + " and annotation type " + annotationType);
					}

					AnnotationValidatorWrapper annotationValidatorWrapper = new AnnotationValidatorWrapper(
							annotation, annotationValidator);

					if (validator == null) {
						validator = annotationValidatorWrapper;
					} else if (validator instanceof AnnotationValidatorWrapper) {
						ComboValidator comboValidator = new ComboValidator(validator);
						comboValidator.addValidator(annotationValidatorWrapper);
						validator = comboValidator;
					} else if (validator instanceof ComboValidator) {
						((ComboValidator) validator).addValidator(annotationValidatorWrapper);
					}
				}
			}
		}

		_validator = validator;
	}

	@Override
	public Object getDefaultValue(Object context) {
		if (_defaultValueProvider == null) {
			return null;
		} else {
			return _defaultValueProvider.provide(context);
		}
	}

	@Override
	public Validator getValidator() {
		return _validator;
	}
}
