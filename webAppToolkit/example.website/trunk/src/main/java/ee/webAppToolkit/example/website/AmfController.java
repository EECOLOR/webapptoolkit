package ee.webAppToolkit.example.website;

import ee.webAppToolkit.amf.AmfResult;
import ee.webAppToolkit.amf.annotations.Amf;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.rendering.RenderingController;

public class AmfController extends RenderingController {

	@NavigationDisplayName(@LocalizedString("navigation.amf"))
	public Result index()
	{
		return render();
	}
	
	@HideFromNavigation
	public Result echo(@Amf Object obj)
	{
		return new AmfResult(obj);
	}
}
