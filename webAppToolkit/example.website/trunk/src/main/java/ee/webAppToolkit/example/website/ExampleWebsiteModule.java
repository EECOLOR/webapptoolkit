package ee.webAppToolkit.example.website;

import ee.webAppToolkit.website.WebsiteModule;

public class ExampleWebsiteModule extends WebsiteModule {

	@Override
	protected void configureControllers() {
		super.configureControllers();
		
		handle("").with(MainController.class);
	}

}
