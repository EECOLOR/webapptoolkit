package ee.webAppToolkit.website;

import java.util.Locale;

import javax.servlet.ServletContext;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.util.Modules;

import ee.webAppToolkit.core.WebAppToolkitModule;
import ee.webAppToolkit.freemarker.FreemarkerModule;
import ee.webAppToolkit.localization.LocalizationModule;
import ee.webAppToolkit.localization.LocalizedStrings;
import ee.webAppToolkit.parameters.ParametersModule;
import ee.webAppToolkit.render.ModelWrapper;
import ee.webAppToolkit.render.RenderModule;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;

public class WebsiteModule extends WebAppToolkitModule {

	@Override
	protected void configureControllers() {
		
		install(new LocalizationModule());
		install(new ParametersModule());
		
		//Override the ModelWrapper in order to create a different kind of Model
		install(Modules.override(new RenderModule()).with(new AbstractModule() {

			@Override
			protected void configure() {
				install(new FactoryModuleBuilder().implement(Object.class, Model.class).build(
						ModelWrapper.class));
			}

		}));
		
		//Override the TemplateLoader in order to add WEB-INF/templates
		install(new FreemarkerModule() {

			@Override
			protected TemplateLoader templateLoader(ServletContext servletContext) {
				
				WebappTemplateLoader webappTemplateLoader = new WebappTemplateLoader(servletContext, "WEB-INF/templates");
				TemplateLoader classTemplateLoader = super.templateLoader(servletContext);
				
				TemplateLoader[] loaders = new TemplateLoader[] {webappTemplateLoader, classTemplateLoader};
				
				return new MultiTemplateLoader(loaders);
			}
			
		});
		
		bindPropertiesToLocale("website.validationMessages", Locale.ENGLISH);
	}

	protected void bindPropertiesToLocale(String propertiesName, Locale locale)
	{
		LocalizedStrings.bindPropertiesToLocale(binder(), getClass().getClassLoader(), propertiesName, locale);
	}
}
