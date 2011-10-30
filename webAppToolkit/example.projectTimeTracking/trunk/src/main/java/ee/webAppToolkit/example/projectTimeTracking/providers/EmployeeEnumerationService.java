package ee.webAppToolkit.example.projectTimeTracking.providers;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;
import ee.webAppToolkit.example.projectTimeTracking.administration.EmployeeContext;
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
	private ThreadLocalProvider<EmployeeContext> _employeeContext;

	@Inject
	public EmployeeEnumerationService(
			Store store,
			@LocalizedString("validation.noEmployeeSelected") String noEmployeeSelected,
			ThreadLocalProvider<EmployeeContext> employeeContextProvider) {
		_store = store;
		_noEmployeeSelected = noEmployeeSelected;
		_employeeContext = employeeContextProvider;
	}

	@Override
	protected Iterable<Employee> getElements() {
		EmployeeContext employeeContext = _employeeContext.get();

		Iterable<Employee> employees = _store.list(Employee.class, "name asc");
		if (employeeContext == null || employeeContext.equals(EmployeeContext.ALL)) {
			return employees;
		} else if (employeeContext.equals(EmployeeContext.OPERATIONS_ONLY))
		{
			employees = Iterables.filter(employees, new Predicate<Employee>() {
				
				@Override
				public boolean apply(Employee employee) {
					Role role = employee.role;
					return role != null && role.category == RoleCategory.OPERATIONS;
				}
				
			});
			return employees;
			
		} else
		{
			throw new RuntimeException("EmployeeEnumerationService does not support CustomerContext." + employeeContext);
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
