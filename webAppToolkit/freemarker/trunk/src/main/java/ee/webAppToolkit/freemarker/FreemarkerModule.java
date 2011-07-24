package ee.webAppToolkit.freemarker;

import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import ee.webAppToolkit.freemarker.expert.impl.GuiceObjectWrapper;
import ee.webAppToolkit.localization.LocaleResolver;
import ee.webAppToolkit.render.Renderer;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.util.ModelFactory;
import freemarker.template.Configuration;

public class FreemarkerModule extends AbstractModule {

	static public final String TAG_SYNTAX = "freemarkerTagSyntax";
	
	public FreemarkerModule()
	{
	}
	
	@Override
	protected void configure() {
		bind(Renderer.class).to(FreemarkerRenderer.class);
		
		bindConstant().annotatedWith(Names.named(TAG_SYNTAX)).to(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		
		Multibinder<TemplateLoader> templateLoaders = Multibinder.newSetBinder(binder(), TemplateLoader.class);
		templateLoaders.addBinding().toInstance(new ClassTemplateLoader(FreemarkerModule.class, "templates"));
	}
	
	protected void bindModelFactory(Class<?> type, Class<? extends ModelFactory> modelFactory)
	{
		MapBinder<Class<?>, ModelFactory> modelFactories = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<ModelFactory>(){});
		modelFactories.addBinding(type).to(modelFactory);
	}
	
	@Provides
	protected Configuration freemarkerConfiguration(TemplateLoader templateLoader, 
			GuiceObjectWrapper objectWrapper,
			@Named(TAG_SYNTAX) int tagSyntax,
			LocaleResolver localeResolver)
	{
		Configuration configuration = new Configuration();
		configuration.setObjectWrapper(objectWrapper);
		configuration.setTagSyntax(tagSyntax);
		configuration.setLocale(localeResolver.getLocale());
		configuration.setTemplateLoader(templateLoader);
		
		return configuration;
	}
	
	@Provides
	@Singleton
	protected TemplateLoader templateLoader(Set<TemplateLoader> templateLoaders)
	{
		return new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]));
	}
}
