package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.rendering.freemarker.utils.Enumeration;
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
	public <T> List<Enumeration<T>> getEnumeration(Class<T> type) {
		
		if (type.isEnum())
		{
			return _createEnumEnumeration(type);
		}
		
		return _getEnumerationProvider(type).get();
	}

	@Override
	public <T> Enumeration<T> getEnumeration(T instance) {
		if (instance instanceof Enum)
		{
			return _createEnumEnumeration(instance);
		}
		
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) instance.getClass();
		
		return _getEnumerationProvider(type).get(instance);
	}

	private <T> EnumerationProvider<T> _getEnumerationProvider(Class<T> type) {
		TypeLiteral<?> typeLiteral = TypeLiteral.get(Types.newParameterizedType(EnumerationProvider.class, type));
		
		List<?> bindings = _injector.findBindingsByType(typeLiteral);
		
		if (bindings.size() > 0)
		{
			@SuppressWarnings("unchecked")
			EnumerationProvider<T> provider = (EnumerationProvider<T>) ((Binding<?>) bindings.get(0)).getProvider().get();
			
			return provider;
		} else
		{
			throw new RuntimeException("Could not find an enumeration provider for " + type);
		}
	}
	
	protected <T> List<Enumeration<T>> _createEnumEnumeration(Class<T> type) {
		List<T> enums = Lists.newArrayList(type.getEnumConstants()); 
		
		return Lists.transform(enums, new Function<T, Enumeration<T>>() {

			@Override
			public Enumeration<T> apply(T enumConstant) {
				return _createEnumEnumeration(enumConstant);
			}
			
		});
	}
	
	protected <T> Enumeration<T> _createEnumEnumeration(T enumConstant) {
		Enum<?> cast = Enum.class.cast(enumConstant);
		
		final String value = cast.name();
		final String name;
		
		LocalizedString localizedString;
		try {
			localizedString = cast.getDeclaringClass().getField(value).getAnnotation(LocalizedString.class);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
		
		if (localizedString == null) {
			name = value;
		} else
		{
			name = _injector.getInstance(Key.get(String.class, localizedString));
		}
		
		return new Enumeration<T>() {

			@Override
			public String getLabel() {
				return name;
			}

			@Override
			public Object getValue() {
				return value;
			}
		};
	}
	
}
