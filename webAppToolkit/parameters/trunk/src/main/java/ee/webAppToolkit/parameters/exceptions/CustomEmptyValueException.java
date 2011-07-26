package ee.webAppToolkit.parameters.exceptions;

import ee.parameterConverter.EmptyValueException;

public class CustomEmptyValueException extends EmptyValueException {

	private static final long serialVersionUID = 1L;

	public CustomEmptyValueException(String message) {
		super(message);
	}
	
}
