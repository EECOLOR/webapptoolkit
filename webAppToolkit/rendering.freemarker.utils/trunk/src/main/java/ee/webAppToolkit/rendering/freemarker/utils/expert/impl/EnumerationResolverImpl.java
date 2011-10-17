package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import ee.webAppToolkit.rendering.freemarker.utils.EnumerationProvider;
import ee.webAppToolkit.rendering.freemarker.utils.expert.EnumerationResolver;

public class EnumerationResolverImpl implements EnumerationResolver {

	private Injector _injector;

	@Inject
	public EnumerationResolverImpl(Injector injector)
	{
		_injector = injector;
	}
	
	@Override
	public <T> List<T> resolve(Class<T> type) {
		
		if (type.isEnum())
		{
			return Lists.newArrayList(type.getEnumConstants());
		}
		
		TypeLiteral<?> typeLiteral = TypeLiteral.get(Types.newParameterizedType(EnumerationProvider.class, type));
		
		List<?> bindings = _injector.findBindingsByType(typeLiteral);
		
		if (bindings.size() > 0)
		{
			@SuppressWarnings("unchecked")
			EnumerationProvider<T> provider = (EnumerationProvider<T>) ((Binding<?>) bindings.get(0)).getProvider().get();
			
			return provider.get();
		} else
		{
			throw new RuntimeException("Could not find an enumeration provider for " + type);
		}
	}
	
}
