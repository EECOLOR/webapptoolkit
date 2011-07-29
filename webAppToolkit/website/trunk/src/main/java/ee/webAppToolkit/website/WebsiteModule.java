package ee.webAppToolkit.website;

import java.util.Date;
import java.util.Locale;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;

import ee.metadataUtils.PropertyMetadataType;
import ee.parameterConverter.Converter;
import ee.webAppToolkit.core.WebAppToolkitModule;
import ee.webAppToolkit.freemarker.forms.FreemarkerFormsModule;
import ee.webAppToolkit.freemarker.metadata.FreemarkerMetadataModule;
import ee.webAppToolkit.freemarker.navigation.FreemarkerNavigationModule;
import ee.webAppToolkit.localization.LocalizationModule;
import ee.webAppToolkit.localization.LocalizedStrings;
import ee.webAppToolkit.metadata.MetadataModule;
import ee.webAppToolkit.navigation.NavigationModule;
import ee.webAppToolkit.parameters.ExceptionConverter;
import ee.webAppToolkit.parameters.ParametersModule;
import ee.webAppToolkit.rendering.ModelWrapper;
import ee.webAppToolkit.rendering.RenderingModule;
import ee.webAppToolkit.rendering.freemarker.FreemarkerModule;
import ee.webAppToolkit.website.expert.impl.GuiceWebappTemplateLoader;
import ee.webAppToolkit.website.parameters.DateConverter;
import ee.webAppToolkit.website.validation.NumberFormatExceptionConverter;
import freemarker.cache.TemplateLoader;

public class WebsiteModule extends WebAppToolkitModule {

	@Override
	protected void configureControllers() {
		
		install(new LocalizationModule());
		install(new ParametersModule());
		
		//Override the ModelWrapper in order to create a different kind of Model
		install(Modules.override(new RenderingModule()).with(new AbstractModule() {

			@Override
			protected void configure() {
				install(new FactoryModuleBuilder().implement(Object.class, Model.class).build(
						ModelWrapper.class));
			}

		}));
		
		install(new NavigationModule());
		install(new FreemarkerModule());
		install(new FreemarkerNavigationModule());
		install(Modules.override(new MetadataModule()).with(new AbstractModule(){

			@Override
			protected void configure() {
				bind(PropertyMetadataType.class).toInstance(PropertyMetadataType.FIELDS);
			}
		}));
		install(Modules.override(new FreemarkerMetadataModule()).with(new FreemarkerFormsModule()));
		
		bind(String.class).annotatedWith(Names.named("templatePath")).toInstance("WEB-INF/templates");
		
		Multibinder<TemplateLoader> templateLoaders = Multibinder.newSetBinder(binder(), TemplateLoader.class);
		templateLoaders.addBinding().to(GuiceWebappTemplateLoader.class);
		
		bind(new TypeLiteral<ExceptionConverter<NumberFormatException>>(){}).to(NumberFormatExceptionConverter.class);
		bind(new TypeLiteral<Converter<String, Date>>(){}).to(DateConverter.class);
		
		bindPropertiesToLocale("website.validationMessages", Locale.ENGLISH);
	}

	protected void bindPropertiesToLocale(String propertiesName, Locale locale)
	{
		LocalizedStrings.bindPropertiesToLocale(binder(), getClass().getClassLoader(), propertiesName, locale);
	}
}
