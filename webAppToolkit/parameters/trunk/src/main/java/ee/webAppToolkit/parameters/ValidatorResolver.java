package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.google.inject.ImplementedBy;

@ImplementedBy(ValidatorResolverImpl.class)
public interface ValidatorResolver
{
	public Validator<?, ? extends Annotation> resolve(Type type, Class<? extends Annotation> annotationType);
}