package ee.webAppToolkit.example.projectTimeTracking;

import ee.webAppToolkit.core.BasicController;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.annotations.SubControllers;
import ee.webAppToolkit.example.projectTimeTracking.administration.EmployeeController;
import ee.webAppToolkit.example.projectTimeTracking.administration.RoleController;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;

@NavigationDisplayName(@LocalizedString("navigation.main"))
@SubControllers({
	@SubController(name="roles", type=RoleController.class),
	@SubController(name="employees", type=EmployeeController.class)
})
public class MainController extends BasicController {

    public Result index() {
            return output("Hello world!");
    }
}
