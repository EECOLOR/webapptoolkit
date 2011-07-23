package ee.webAppToolkit.example.website;

import java.util.Map;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.json.JsonResult;
import ee.webAppToolkit.json.annotations.Json;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.render.RenderingController;

public class JsonController extends RenderingController {
	
	@NavigationDisplayName(@LocalizedString("navigation.json"))
	public Result index()
	{
		return render();
	}
	
	@HideFromNavigation
	public Result echo(@Json("data") Map<String, String> o)
	{
		return new JsonResult(o);
	}
}
