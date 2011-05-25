package ee.webAppToolkit.parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class ValidatorResolverImpl implements ValidatorResolver
{
	private Injector _injector;

	@Inject
	public ValidatorResolverImpl(Injector injector)
	{
		_injector = injector;
	}
	
	/* (non-Javadoc)
	 * @see ee.webAppToolkit.validation.ValidatorResolver#resolve(java.lang.reflect.Type, java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Validator<?, ? extends Annotation> resolve(Type type, Class<? extends Annotation> annotationType)
	{
		List<?> bindings = _injector.findBindingsByType(TypeLiterals.validator(type, annotationType)); 
		
		if (bindings.size() > 0)
		{
			return (Validator<?, ? extends Annotation>) ((Binding<?>) bindings.get(0)).getProvider().get();
		} else
		{
			return null;
		}
	}
}
