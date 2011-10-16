package ee.webAppToolkit.rendering.freemarker.utils;

import java.util.List;

public interface EnumerationProvider<T> {
	public List<T> get();
}
