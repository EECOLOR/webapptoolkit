package ee.webAppToolkit.example.website;

import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.example.website.validation.Length;
import ee.webAppToolkit.parameters.annotations.Default;

public class ParametersTestObject {
	
	@Default(DefaultTitleProvider.class)
	@Optional
	public String title;
	
	public int version;
	
	@Length(min=12)
	public String content;
	
	@Override
	public String toString()
	{
		return "[ParametersTestObject(title: \"" + title + "\", version: " + version + ", content: \"" + content + "\")]";
	}
}
