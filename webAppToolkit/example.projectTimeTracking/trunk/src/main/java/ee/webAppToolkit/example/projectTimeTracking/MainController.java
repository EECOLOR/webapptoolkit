package ee.webAppToolkit.example.projectTimeTracking;

import com.google.inject.Provider;

import ee.webAppToolkit.core.BasicController;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.annotations.SubControllers;
import ee.webAppToolkit.example.projectTimeTracking.domain.Customer;
import ee.webAppToolkit.example.projectTimeTracking.domain.Employee;
import ee.webAppToolkit.example.projectTimeTracking.domain.Role;
import ee.webAppToolkit.example.projectTimeTracking.domain.RoleCategory;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.storage.Store;

@NavigationDisplayName(@LocalizedString("navigation.main"))
@SubControllers({
	@SubController(name="administration", type=AdministrationController.class),
	@SubController(name="work", type=WorkController.class)
})
public class MainController extends BasicController {

    public void index() {
        redirect("work");
    }
    
	public Result insertTestData(Store store, Provider<Role> roleProvider, Provider<Employee> employeeProvider, Provider<Customer> customerProvider) {
		if (store.count(Customer.class) < 1) {
			Role role;
			Employee employee;
			Customer customer;
			
			role = roleProvider.get();
			role.name = "Account manager";
			role.category = RoleCategory.OPERATIONS;
			role.rate = 85;
			role.rateCommercial = 120;
			store.save(role);
			
			employee = employeeProvider.get();
			employee.name = "Nienke van Heusden";
			employee.role = role;
			store.save(employee);
			
			employee = employeeProvider.get();
			employee.name = "Sander van Pijkeren";
			employee.role = role;
			store.save(employee);
			
			customer = customerProvider.get();
			customer.name = "Test customer";
			customer.accountManager = employee;
			store.save(customer);
			
			role = roleProvider.get();
			role.name = "Project manager";
			role.category = RoleCategory.OPERATIONS;
			role.rate = 85;
			role.rateCommercial = 120;
			store.save(role);
			
			employee = employeeProvider.get();
			employee.name = "Roy Pereira";
			employee.role = role;
			store.save(employee);
			
			employee = employeeProvider.get();
			employee.name = "Dennis Suicies";
			employee.role = role;
			store.save(employee);
			
			role = roleProvider.get();
			role.name = "Administrator";
			role.category = RoleCategory.GUARDIANS;
			role.rate = 75;
			role.rateCommercial = 100;
			store.save(role);
			
			employee = employeeProvider.get();
			employee.name = "Paul Brekelmans";
			employee.role = role;
			store.save(employee);
			
			role = roleProvider.get();
			role.name = "Back-end developer";
			role.category = RoleCategory.CREATORS;
			role.rate = 80;
			role.rateCommercial = 110;
			store.save(role);
			
			employee = employeeProvider.get();
			employee.name = "Eelco Eggen";
			employee.role = role;
			store.save(employee);
			
			role = roleProvider.get();
			role.name = "Technical director";
			role.category = RoleCategory.OPERATIONS;
			role.rate = 90;
			role.rateCommercial = 140;
			store.save(role);
			
			employee = employeeProvider.get();
			employee.name = "Erik Westra";
			employee.role = role;
			store.save(employee);
			
			role = roleProvider.get();
			role.name = "Creative director";
			role.category = RoleCategory.INVENTORS;
			role.rate = 90;
			role.rateCommercial = 140;
			store.save(role);
			
			employee = employeeProvider.get();
			employee.name = "Ronald van Schaik";
			employee.role = role;
			store.save(employee);
			
			return output("Inserted test data");
		} else
		{
			return output("Did not insert test data, there is already data present");
		}
	}

}
