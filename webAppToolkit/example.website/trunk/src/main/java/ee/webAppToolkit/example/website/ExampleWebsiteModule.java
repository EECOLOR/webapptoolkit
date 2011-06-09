package ee.webAppToolkit.example.website;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.name.Names;
import com.google.inject.servlet.SessionScoped;

import ee.webAppToolkit.website.WebsiteModule;

public class ExampleWebsiteModule extends WebsiteModule {

	@Override
	protected void configureControllers() {
		super.configureControllers();
		
		bind(Map.class).annotatedWith(Names.named("store")).to(HashMap.class).in(SessionScoped.class);
		
		handle("").with(MainController.class);
	}

}
