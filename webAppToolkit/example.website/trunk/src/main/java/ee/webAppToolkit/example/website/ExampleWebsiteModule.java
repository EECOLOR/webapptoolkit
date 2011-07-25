package ee.webAppToolkit.example.website;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ee.parameterConverter.Converter;
import ee.webAppToolkit.amf.AmfModule;
import ee.webAppToolkit.example.website.forms.TestEnumeration;
import ee.webAppToolkit.example.website.forms.TestEnumerationService;
import ee.webAppToolkit.example.website.validation.Length;
import ee.webAppToolkit.example.website.validation.LengthValidator;
import ee.webAppToolkit.freemarker.forms.EnumerationProvider;
import ee.webAppToolkit.json.JsonModule;
import ee.webAppToolkit.parameters.AnnotationValidator;
import ee.webAppToolkit.website.WebsiteModule;

public class ExampleWebsiteModule extends WebsiteModule {

	@Override
	protected void configureControllers() {
		super.configureControllers();
		
		install(new AmfModule());
		install(new JsonModule());
		
		bind(new TypeLiteral<AnnotationValidator<String, Length>>(){}).to(LengthValidator.class);
		
		bind(new TypeLiteral<Map<String, String>>(){}).annotatedWith(Names.named("store")).to(new TypeLiteral<HashMap<String, String>>(){}).asEagerSingleton();
		
		bind(new TypeLiteral<EnumerationProvider<TestEnumeration>>(){}).to(TestEnumerationService.class).asEagerSingleton();
		bind(new TypeLiteral<Converter<String, TestEnumeration>>(){}).to(TestEnumerationService.class).asEagerSingleton();
		
		handle("").with(MainController.class);
		
		bindPropertiesToLocale("navigation", Locale.ENGLISH);
		bindPropertiesToLocale("example.website.validationMessages", Locale.ENGLISH);
		bindPropertiesToLocale("forms", Locale.ENGLISH);
	}

}
