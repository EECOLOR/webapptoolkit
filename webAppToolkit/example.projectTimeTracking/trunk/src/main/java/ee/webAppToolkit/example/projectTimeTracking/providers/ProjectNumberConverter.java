package ee.webAppToolkit.example.projectTimeTracking.providers;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectNumber;

public class ProjectNumberConverter implements Converter<String, ProjectNumber> {

	@Override
	public ProjectNumber convert(String value) throws EmptyValueException,
			ConversionFailedException {
		return new ProjectNumber(value);
	}
	
}
