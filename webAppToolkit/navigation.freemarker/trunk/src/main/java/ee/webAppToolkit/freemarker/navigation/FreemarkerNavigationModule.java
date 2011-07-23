package ee.webAppToolkit.freemarker.navigation;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import ee.webAppToolkit.freemarker.ModelFactoryRegistration;
import ee.webAppToolkit.freemarker.navigation.expert.impl.SiteMapModelFactory;
import ee.webAppToolkit.navigation.SiteMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;

public class FreemarkerNavigationModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<TemplateLoader> listeners = Multibinder.newSetBinder(binder(), TemplateLoader.class);
		listeners.addBinding().toInstance(new ClassTemplateLoader(FreemarkerNavigationModule.class, "templates"));
		
		Multibinder<ModelFactoryRegistration> modelFactories = Multibinder.newSetBinder(binder(), ModelFactoryRegistration.class);
		modelFactories.addBinding().toInstance(new ModelFactoryRegistration(SiteMap.class, new SiteMapModelFactory()));

	}
	
}
