package ee.webAppToolkit.website;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;

import ee.parameterConverter.Converter;
import ee.webAppToolkit.core.WebAppToolkitModule;
import ee.webAppToolkit.localization.LocalizationModule;
import ee.webAppToolkit.localization.LocalizedStrings;
import ee.webAppToolkit.metadata.MetadataModule;
import ee.webAppToolkit.navigation.NavigationModule;
import ee.webAppToolkit.parameters.ExceptionConverter;
import ee.webAppToolkit.parameters.ParametersModule;
import ee.webAppToolkit.rendering.ModelWrapper;
import ee.webAppToolkit.rendering.RenderingModule;
import ee.webAppToolkit.rendering.freemarker.FreemarkerModule;
import ee.webAppToolkit.rendering.freemarker.utils.FreemarkerUtilsModule;
import ee.webAppToolkit.website.expert.impl.GuiceWebappTemplateLoader;
import ee.webAppToolkit.website.parameters.DateConverter;
import ee.webAppToolkit.website.validation.NumberFormatExceptionConverter;
import ee.webAppToolkit.website.validation.ParseExceptionConverter;
import freemarker.cache.TemplateLoader;

abstract public class WebsiteModule extends WebAppToolkitModule {

	@Override
	protected void configureControllers() {
		
		handle("").with(getRootController());
		
		configureApplication();
	}

	abstract protected void configureApplication();
	abstract protected Class<?> getRootController();
	
	@Override
	protected Module getDefaultBindingsModule()
	{
		Module installedModules = Modules.combine(
				new LocalizationModule(),
				new ParametersModule(),
				new NavigationModule(),
				new MetadataModule(),
				new FreemarkerModule(),
				new FreemarkerUtilsModule());
		
		//Override the ModelWrapper in order to create a different kind of Model
		Module modelModule = Modules.override(new RenderingModule()).with(new AbstractModule() {
					@Override
					protected void configure() {
						install(new FactoryModuleBuilder().implement(Object.class, Model.class).build(
								ModelWrapper.class));
					}});
		
		//default bindings
		Module defaultBindings = new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("templatePath")).toInstance("WEB-INF/templates");
				
				Multibinder<TemplateLoader> templateLoaders = Multibinder.newSetBinder(binder(), TemplateLoader.class);
				templateLoaders.addBinding().to(GuiceWebappTemplateLoader.class);
				
				bind(new TypeLiteral<ExceptionConverter<NumberFormatException>>(){}).to(NumberFormatExceptionConverter.class);
				bind(new TypeLiteral<ExceptionConverter<ParseException>>(){}).to(ParseExceptionConverter.class);
				bind(new TypeLiteral<Converter<String, Date>>(){}).to(DateConverter.class);
				
				bindPropertiesToLocale("website.validationMessages", Locale.ENGLISH);
			}};
		
		return Modules.combine(super.getDefaultBindingsModule(), installedModules, modelModule, defaultBindings);
	}
	
	protected void bindPropertiesToLocale(String propertiesName, Locale locale)
	{
		LocalizedStrings.bindPropertiesToLocale(binder(), getClass().getClassLoader(), propertiesName, locale);
	}
}
