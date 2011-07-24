package ee.webAppToolkit.freemarker.navigation;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

import ee.webAppToolkit.freemarker.navigation.expert.impl.SiteMapModelFactory;
import ee.webAppToolkit.navigation.SiteMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.util.ModelFactory;

public class FreemarkerNavigationModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<TemplateLoader> listeners = Multibinder.newSetBinder(binder(), TemplateLoader.class);
		listeners.addBinding().toInstance(new ClassTemplateLoader(getClass(), "templates"));
		
		MapBinder<Class<?>, ModelFactory> modelFactories = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<ModelFactory>(){});
		modelFactories.addBinding(SiteMap.class).to(SiteMapModelFactory.class);
	}
	
}
