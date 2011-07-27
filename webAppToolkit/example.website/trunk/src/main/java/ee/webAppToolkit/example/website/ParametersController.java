package ee.webAppToolkit.example.website;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Get;
import ee.webAppToolkit.core.annotations.Post;
import ee.webAppToolkit.example.website.parameters.TestObject;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.rendering.RenderingController;

public class ParametersController extends RenderingController {
	
	@Get
	@NavigationDisplayName(@LocalizedString("navigation.parameters"))
	public Result index()
	{
		return render();
	}
	
	@Post
	public Result index(@Parameter TestObject testObject)
	{
		System.out.println(testObject);
		
		return render(testObject);
	}
}
