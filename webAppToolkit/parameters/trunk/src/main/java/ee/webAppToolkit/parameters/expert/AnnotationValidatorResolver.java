package ee.webAppToolkit.parameters.expert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.parameters.expert.impl.AnnotationValidatorResolverImpl;

@ImplementedBy(AnnotationValidatorResolverImpl.class)
public interface AnnotationValidatorResolver
{
	public AnnotationValidator<Object, Annotation> resolve(Type type, Class<? extends Annotation> annotationType);
}