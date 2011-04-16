package ee.webAppToolkit.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.ServletModule;
import com.google.inject.util.Types;

import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.annotations.Path;
import ee.webAppToolkit.core.expert.DefaultBindingsModule;
import ee.webAppToolkit.core.expert.StaticServlet;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;
import ee.webAppToolkit.core.expert.ThreadLocalProviderImpl;
import ee.webAppToolkit.core.expert.WebAppToolkit;
import ee.webAppToolkit.core.expert.WebAppToolkitServlet;

public abstract class WebAppToolkitModule extends ServletModule {

	protected Binder handle(String path) {
		return new Binder(path);
	}

	/**
	 * This allows you to bind a thread local provider to Guice provider, it's a shortcut 
	 * for statements like this:
	 * 
	 * <pre><code>
	 * Key&lt;ThreadLocalProvider&lt;String>> key = Key.get(new TypeLiteral&lt;ThreadLocalProvider&lt;String>>(){}, Context.class);
	 * 
	 * bind(key).to(new TypeLiteral&lt;ThreadLocalProviderImpl&lt;String>>(){}).asEagerSingleton();
	 * bind(String.class).annotatedWith(Context.class).toProvider(key);
	 * </code></pre>
	 * 
	 * <p>The new call would look like this: <code>bindThreadLocalProvider(String.class, Context.class)</code></p>
	 * 
	 * <p>
	 * This gives you the ability to request a <code>ThreadLocalProvider&lt;String></code> annotated 
	 * with @Context in order to set the value for the current thread.
	 * </p>
	 * <p>
	 * Then in the rest of the application you can simply request a String or Provider&lt;String> annotated 
	 * with @Context to retrieve the value.
	 * </p>
	 * 
	 * @param type				The type that you want to bind.
	 * @param annotationType	The annotation that you want to bind it with
	 */
	protected <T> void bindThreadLocalProvider(Class<T> type, Class<? extends Annotation> annotationType) {
		@SuppressWarnings("unchecked")
		TypeLiteral<ThreadLocalProvider<T>> threadLocalType = (TypeLiteral<ThreadLocalProvider<T>>) TypeLiteral
				.get(Types.newParameterizedType(ThreadLocalProvider.class, type));
		Key<ThreadLocalProvider<T>> key = Key.get(threadLocalType, annotationType);

		@SuppressWarnings("unchecked")
		TypeLiteral<ThreadLocalProviderImpl<T>> threadLocalImplementation = (TypeLiteral<ThreadLocalProviderImpl<T>>) TypeLiteral
				.get(Types.newParameterizedType(ThreadLocalProviderImpl.class, type));

		bind(key).to(threadLocalImplementation).asEagerSingleton();
		bind(type).annotatedWith(annotationType).toProvider(key);
	}

	private Map<String, Class<?>> _bindings = new HashMap<String, Class<?>>();

	@Override
	protected void configureServlets() {
		bind(new TypeLiteral<Map<String, Class<?>>>() {
		}).annotatedWith(WebAppToolkit.class).toInstance(_bindings);

		bindThreadLocalProvider(String.class, Path.class);
		bindThreadLocalProvider(String.class, Context.class);
		
		install(getDefaultBindingsModule());

		bindStaticServlet();
		
		serve("/*").with(WebAppToolkitServlet.class);

		configureControllers();
	}

	protected void bindStaticServlet()
	{
		serve("/favicon.ico").with(StaticServlet.class);
		serve("/static/*").with(StaticServlet.class);
	}
	
	protected Module getDefaultBindingsModule() {
		return new DefaultBindingsModule();
	}

	abstract protected void configureControllers();

	protected class Binder {
		private String _path;

		public Binder(String path) {
			_path = path;
		}

		public void with(Class<?> controller) {
			_bindings.put(_path, controller);
		}
	}
}
