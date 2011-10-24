package ee.webAppToolkit.rendering.freemarker.utils;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

abstract public class AbstractEnumerationProvider<T> implements EnumerationProvider<T> {
	
	@Override
	public List<Enumeration<T>> get() {
		Iterable<T> elements = getElements();
		Iterable<Enumeration<T>> enumerations = Iterables.transform(elements, new Function<T, Enumeration<T>>() {

			@Override
			public Enumeration<T> apply(final T instance) {
				return get(instance);
			}
			
		});
		
		return Lists.newArrayList(enumerations);
	}

	abstract protected Iterable<T> getElements();
	abstract protected String getLabel(T element);
	abstract protected Object getValue(T element);
	
	@Override
	public Enumeration<T> get(final T instance) {
		return new Enumeration<T>() {
			
			@Override
			public String getLabel() {
				return AbstractEnumerationProvider.this.getLabel(instance);
			}

			@Override
			public Object getValue() {
				return AbstractEnumerationProvider.this.getValue(instance);
			}
			
		};
	}
}
