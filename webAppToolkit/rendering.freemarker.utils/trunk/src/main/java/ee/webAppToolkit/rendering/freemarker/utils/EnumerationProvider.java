package ee.webAppToolkit.rendering.freemarker.utils;

import java.util.List;

public interface EnumerationProvider<T> {
	public List<Enumeration<T>> get();
	public Enumeration<T> get(T instance);
}
