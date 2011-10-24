package ee.webAppToolkit.example.projectTimeTracking.administration;

import javax.inject.Inject;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.example.projectTimeTracking.domain.Employee;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.rendering.RenderingController;
import ee.webAppToolkit.storage.Store;

@NavigationDisplayName(@LocalizedString("navigation.employees"))
public class EmployeeController extends RenderingController {

	@Inject
	@LocalizedString("employee.saved")
	private String _savedMessage;
	
	@Inject
	@LocalizedString("employee.removed")
	private String _removedMessage;
	
	private Store _store;

	@Inject
	public EmployeeController(Store store) {
		_store = store;
	}
	
	public Result index(
			@Optional @Parameter("employee") Employee employee,
			@Optional @Parameter("id") Long id,
			ValidationResults validationResults,
			@Flash String message)
	{
		if (validationResults.getValidated("employee"))
		{
			if (employee == null)
			{
				if (id != null)
				{
					employee = _store.load(Employee.class, id);
				}
			} else
			{
				_store.save(employee);
				flash.put(_savedMessage);
				redirect();
			}
		}
		
		Model model = new Model(_store.list(Employee.class), employee, message);
		
		return render(model);
	}
	
	@HideFromNavigation
	public Result remove(@Parameter("id") Long id) {
		Employee employee = _store.load(Employee.class, id);
		return render(new Model(employee));
	}
	
	@HideFromNavigation
	public void removeConfirm(@Parameter("id") Long id) {
		_store.removeByKey(id);
		
		flash.put(_removedMessage);
		redirect();
	}
	
	public class Model
	{
		public Iterable<Employee> employees;
		public Employee employee;
		public String message;

		public Model(Employee employee)
		{
			this(null, employee, null);
		}
		
		public Model(Iterable<Employee> employees, Employee employee, String message)
		{
			this.employees = employees;
			this.employee = employee;
			this.message = message;
		}
	}
}
