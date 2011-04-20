package ee.webAppToolkit.parameters;

import javax.inject.Provider;

public class ValidationResultsProvider implements Provider<ValidationResults> {

	private ThreadLocal<ValidationResults> _validationResults;

	public ValidationResultsProvider() {
		_validationResults = new ThreadLocal<ValidationResults>() {
			@Override
			protected ValidationResults initialValue() {
				return new ValidationResults();
			}
		};
	}

	@Override
	public ValidationResults get() {
		return _validationResults.get();
	}

}
