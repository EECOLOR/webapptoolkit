package ee.webAppToolkit.rendering.freemarker;

import java.util.Collection;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import ee.webAppToolkit.localization.LocaleResolver;
import ee.webAppToolkit.rendering.Renderer;
import ee.webAppToolkit.rendering.freemarker.expert.impl.GuiceObjectWrapper;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.util.ModelFactory;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;

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
		
		MapBinder<Class<?>, ModelFactory> modelFactories = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<ModelFactory>(){});
		modelFactories.addBinding(Iterable.class).toInstance(new ModelFactory() {
			@Override
			public TemplateModel create(Object object, ObjectWrapper wrapper) {
				if (object instanceof Collection) {
					return new SimpleSequence((Collection<?>) object, wrapper);
				} else
				{
					return new SimpleCollection(((Iterable<?>) object).iterator(), wrapper);
				}
			}
		});
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
		configuration.setLocale(localeResolver.getLocaleChain().get(0));
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
