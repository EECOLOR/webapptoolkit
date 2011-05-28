package ee.webAppToolkit.parameters.expert.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.parameters.expert.AnnotationValidatorResolver;

public class AnnotationValidatorResolverImpl implements AnnotationValidatorResolver
{
	private Injector _injector;

	@Inject
	public AnnotationValidatorResolverImpl(Injector injector)
	{
		_injector = injector;
	}
	
	/* (non-Javadoc)
	 * @see ee.webAppToolkit.validation.ValidatorResolver#resolve(java.lang.reflect.Type, java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public AnnotationValidator<Object, Annotation> resolve(Type type, Class<? extends Annotation> annotationType)
	{
		TypeLiteral<?> typeLiteral = TypeLiteral.get(Types.newParameterizedType(AnnotationValidator.class, type, annotationType));
		List<?> bindings = _injector.findBindingsByType(typeLiteral); 
		
		if (bindings.size() > 0)
		{
			return (AnnotationValidator<Object, Annotation>) ((Binding<?>) bindings.get(0)).getProvider().get();
		} else
		{
			return null;
		}
	}
}
