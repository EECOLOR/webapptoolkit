package ee.webAppToolkit.core;

import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.annotations.SubControllers;

@SubControllers({
	@SubController(name="test", type=BasicController.class)
})
public class BasicController implements WrappingController{

	@Override
	public void beforeHandling(String memberName) {
		
	}

	@Override
	public Result wrapResult(Result result, String memberName, Object controller) {
		return result;
	}
	
	
}
