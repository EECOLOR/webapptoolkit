package ee.webAppToolkit.example.website;

import ee.webAppToolkit.parameters.DefaultValueProvider;

public class DefaultTitleProvider implements DefaultValueProvider<String> {

	@Override
	public String provide(Object context) {
		return "No title";
	}
	
}
