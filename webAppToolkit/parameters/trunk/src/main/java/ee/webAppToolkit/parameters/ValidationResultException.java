package ee.webAppToolkit.parameters;

public class ValidationResultException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private Object _originalValue;
	private ValidationResult _validationResult;

	public ValidationResultException(Object originalValue, ValidationResult validationResult)
	{
		_originalValue = originalValue;
		_validationResult = validationResult;
	}

	public Object getOriginalValue() {
		return _originalValue;
	}

	public ValidationResult getValidationResult() {
		return _validationResult;
	}
}
