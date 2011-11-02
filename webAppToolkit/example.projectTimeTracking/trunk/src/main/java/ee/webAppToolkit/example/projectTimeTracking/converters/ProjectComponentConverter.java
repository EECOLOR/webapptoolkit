package ee.webAppToolkit.example.projectTimeTracking.converters;

import javax.inject.Inject;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectComponent;
import ee.webAppToolkit.storage.Store;

public class ProjectComponentConverter implements Converter<String, ProjectComponent> {

	private Store _store;

	@Inject
	public ProjectComponentConverter(Store store) {
		_store = store;
	}
	
	@Override
	public ProjectComponent convert(String value) throws EmptyValueException,
			ConversionFailedException {
		
		if (value.length() == 0) {
			throw new EmptyValueException("The given value is empty");
		}
		
		return _store.load(ProjectComponent.class, Long.parseLong(value));
	}
	
}
