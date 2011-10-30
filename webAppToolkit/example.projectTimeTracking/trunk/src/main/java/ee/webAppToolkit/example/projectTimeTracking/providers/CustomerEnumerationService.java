package ee.webAppToolkit.example.projectTimeTracking.providers;

import javax.inject.Inject;
import javax.inject.Singleton;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.example.projectTimeTracking.domain.Customer;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.exceptions.CustomEmptyValueException;
import ee.webAppToolkit.rendering.freemarker.utils.AbstractEnumerationProvider;
import ee.webAppToolkit.storage.Store;

@Singleton
public class CustomerEnumerationService extends
		AbstractEnumerationProvider<Customer> implements
		Converter<String, Customer> {

	private Store _store;
	private String _noCustomerSelected;

	@Inject
	public CustomerEnumerationService(
			Store store,
			@LocalizedString("validation.noCustomerSelected") String noCustomerSelected) {
		_store = store;
		_noCustomerSelected = noCustomerSelected;
	}

	@Override
	protected Iterable<Customer> getElements() {
		return _store.list(Customer.class, "name asc");
	}

	@Override
	protected String getLabel(Customer element) {
		return element.name;
	}

	@Override
	protected Object getValue(Customer element) {
		return element.getId();
	}

	@Override
	public Customer convert(String value) throws EmptyValueException,
			ConversionFailedException {
		if (value.length() == 0) {
			throw new CustomEmptyValueException(_noCustomerSelected);
		}
		return _store.load(Customer.class, Long.parseLong(value));
	}
}
