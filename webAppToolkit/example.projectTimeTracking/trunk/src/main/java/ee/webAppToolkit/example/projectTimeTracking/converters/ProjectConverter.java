package ee.webAppToolkit.example.projectTimeTracking.converters;

import javax.inject.Inject;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.example.projectTimeTracking.domain.Project;
import ee.webAppToolkit.storage.Store;

public class ProjectConverter implements Converter<String, Project> {

	private Store _store;

	@Inject
	public ProjectConverter(Store store) {
		_store = store;
	}
	
	@Override
	public Project convert(String value) throws EmptyValueException,
			ConversionFailedException {
		
		if (value.length() == 0) {
			throw new EmptyValueException("The given value is empty");
		}
		
		return _store.load(Project.class, Long.parseLong(value));
	}
	
}
