package ee.webAppToolkit.example.projectTimeTracking.providers;

import javax.inject.Inject;
import javax.inject.Singleton;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;
import ee.webAppToolkit.example.projectTimeTracking.administration.CustomerContext;
import ee.webAppToolkit.example.projectTimeTracking.domain.Employee;
import ee.webAppToolkit.example.projectTimeTracking.domain.Role;
import ee.webAppToolkit.example.projectTimeTracking.domain.RoleCategory;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.exceptions.CustomEmptyValueException;
import ee.webAppToolkit.rendering.freemarker.utils.AbstractEnumerationProvider;
import ee.webAppToolkit.storage.Store;

@Singleton
public class EmployeeEnumerationService extends
		AbstractEnumerationProvider<Employee> implements
		Converter<String, Employee> {

	private Store _store;
	private String _noEmployeeSelected;
	private ThreadLocalProvider<CustomerContext> _customerContextProvider;

	@Inject
	public EmployeeEnumerationService(
			Store store,
			@LocalizedString("validation.noEmployeeSelected") String noEmployeeSelected,
			ThreadLocalProvider<CustomerContext> customerContextProvider) {
		_store = store;
		_noEmployeeSelected = noEmployeeSelected;
		_customerContextProvider = customerContextProvider;
	}

	@Override
	protected Iterable<Employee> getElements() {
		CustomerContext customerContext = _customerContextProvider.get();

		switch (customerContext) {
		case ALL:
			return _store.list(Employee.class, "name asc");
		case OPERATIONS_ONLY:
			Employee employee = new Employee();
			Role role = new Role();
			role.category = RoleCategory.OPERATIONS;
			employee.role = role;
			return _store.find(employee, "name asc");
		default:
			throw new RuntimeException("EmployeeEnumerationService does not support CustomerContext." + customerContext);
		}
	}

	@Override
	protected String getLabel(Employee element) {
		return element.name;
	}

	@Override
	protected Object getValue(Employee element) {
		return element.getId();
	}

	@Override
	public Employee convert(String value) throws EmptyValueException,
			ConversionFailedException {
		if (value.length() == 0) {
			throw new CustomEmptyValueException(_noEmployeeSelected);
		}
		return _store.load(Employee.class, Long.parseLong(value));
	}
}
