package ee.webAppToolkit.navigation.freemarker;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;

public class NavigationFreemarkerModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<TemplateLoader> listeners = Multibinder.newSetBinder(binder(), TemplateLoader.class);
		listeners.addBinding().toInstance(new ClassTemplateLoader(NavigationFreemarkerModule.class, "templates"));
	}
	
}
