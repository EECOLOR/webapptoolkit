package ee.webAppToolkit.example.website;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ee.webAppToolkit.website.WebsiteModule;

public class ExampleWebsiteModule extends WebsiteModule {

	@Override
	protected void configureControllers() {
		super.configureControllers();
		
		bind(new TypeLiteral<Map<String, String>>(){}).annotatedWith(Names.named("store")).to(new TypeLiteral<HashMap<String, String>>(){}).asEagerSingleton();
		
		handle("").with(MainController.class);
	}

}
