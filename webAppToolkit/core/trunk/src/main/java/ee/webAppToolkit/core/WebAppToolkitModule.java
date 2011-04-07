package ee.webAppToolkit.core;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.ServletModule;

import ee.webAppToolkit.core.expert.DefaultBindingsModule;
import ee.webAppToolkit.core.expert.WebAppToolkit;
import ee.webAppToolkit.core.expert.WebAppToolkitServlet;

public abstract class WebAppToolkitModule extends ServletModule {
	
	protected Binder handle(String path)
	{
		return new Binder(path);
	}
	
	private Map<String, Class<?>> _bindings = new HashMap<String, Class<?>>();
	
	@Override
	protected void configureServlets() {
		bind(new TypeLiteral<Map<String, Class<?>>>(){}).annotatedWith(WebAppToolkit.class).toInstance(_bindings);
		
		install(getDefaultBindingsModule());
		
		serve("/*").with(WebAppToolkitServlet.class);
		
		configureControllers();
	}

	protected Module getDefaultBindingsModule() {
		return new DefaultBindingsModule();
	}

	abstract protected void configureControllers();

	protected class Binder
	{
		private String _path;
		
		public Binder(String path) {
			_path = path;
		}

		public void with(Class<?> controller)
		{
			_bindings.put(_path, controller);
		}
	}
}
