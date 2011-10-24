package ee.webAppToolkit.example.projectTimeTracking.providers;

import javax.inject.Inject;
import javax.inject.Singleton;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.example.projectTimeTracking.domain.Role;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.exceptions.CustomEmptyValueException;
import ee.webAppToolkit.rendering.freemarker.utils.AbstractEnumerationProvider;
import ee.webAppToolkit.storage.Store;

@Singleton
public class RoleEnumerationService extends AbstractEnumerationProvider<Role> implements Converter<String, Role> {

	private Store _store;
	private String _noRoleSelected;

	@Inject
	public RoleEnumerationService(Store store, @LocalizedString("validation.noRoleSelected") String noRoleSelected) {
		_store = store;
		_noRoleSelected = noRoleSelected;
	}
	
	@Override
	protected Iterable<Role> getElements() {
		return _store.list(Role.class, "name asc");
	}

	@Override
	protected String getLabel(Role element) {
		return element.name;
	}
	
	@Override
	protected Object getValue(Role element) {
		return element.getId();
	}

	@Override
	public Role convert(String value) throws EmptyValueException, ConversionFailedException {
		if (value.length() == 0)
		{
			throw new CustomEmptyValueException(_noRoleSelected);
		}
		return _store.load(Role.class, Long.parseLong(value));
	}
}
