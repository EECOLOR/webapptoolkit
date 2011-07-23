package ee.webAppToolkit.example.website;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.annotations.SubControllers;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.render.RenderingController;

@SubControllers({
	@SubController(name="rest", type=RestController.class),
	@SubController(name="amf", type=AmfController.class),
	@SubController(name="json", type=JsonController.class),
	@SubController(name="parameters", type=ParametersController.class)
})
public class MainController extends RenderingController {

	@HideFromNavigation
	public Result index() {
		return output("This is index of the main controller");
	}

	@Override
	public Result wrapResult(Result result, String memberName, Object controller) {
		WrapperModel model = new WrapperModel(memberName, result.getContent());
		
		return render(model, "wrapper");
	}

	public class WrapperModel
	{
		public String content;
		public String title;
		
		WrapperModel(String title, String content)
		{
			this.title = title;
			this.content = content;
		}
	}
}
