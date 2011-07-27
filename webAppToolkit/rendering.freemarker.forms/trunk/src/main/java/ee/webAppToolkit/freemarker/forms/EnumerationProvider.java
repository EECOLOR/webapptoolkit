package ee.webAppToolkit.freemarker.forms;

import java.util.List;

public interface EnumerationProvider<T> {
	public List<T> get();
}
