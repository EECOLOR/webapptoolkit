package ee.webAppToolkit.freemarker;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

import ee.webAppToolkit.localization.LocaleResolver;
import ee.webAppToolkit.render.Renderer;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;

public class FreemarkerModule extends AbstractModule {

	static public final String TAG_SYNTAX = "freemarkerTagSyntax";
	
	@Override
	protected void configure() {
		bind(Renderer.class).to(FreemarkerRenderer.class);
		
		bindConstant().annotatedWith(Names.named(TAG_SYNTAX)).to(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		
		//TODO change to another ObjectWrapper
		bind(ObjectWrapper.class).to(DefaultObjectWrapper.class);
	}
	
	@Provides
	protected Configuration freemarkerConfiguration(TemplateLoader templateLoader, 
			ObjectWrapper objectWrapper,
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
	protected TemplateLoader templateLoader(ServletContext servletContext)
	{
		return new ClassTemplateLoader(FreemarkerModule.class, "templates");
	}
}
