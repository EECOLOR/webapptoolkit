package ee.webAppToolkit.example.website;

import javax.inject.Inject;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.navigation.HideFromNavigation;
import ee.webAppToolkit.navigation.SiteMap;
import ee.webAppToolkit.render.RenderingController;

@SubController(name="rest", type=RestController.class)
public class MainController extends RenderingController {

	@Inject
	protected SiteMap siteMap;

	@HideFromNavigation
	public Result index() {
		return output("This is index of the main controller");
	}

	@Override
	public Result wrapResult(Result result, String memberName, Object controller) {
		WrapperModel model = new WrapperModel(siteMap, memberName, result.getContent());
		
		return render(model, "wrapper");
	}

	public class WrapperModel
	{
		public SiteMap siteMap;
		public String content;
		public String title;
		
		WrapperModel(SiteMap siteMap, String title, String content)
		{
			this.title = title;
			this.siteMap = siteMap;
			this.content = content;
		}
	}
}
