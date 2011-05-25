package ee.webAppToolkit.parameters;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.inject.Key;
import com.google.inject.servlet.RequestParameters;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.EmptyValueException;
import ee.parameterConverter.ParameterConverter;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;

public class ParameterActionArgumentResolver implements ActionArgumentResolver {

	private ParameterConverter _parameterConverter;
	private Provider<Map<String, String[]>> _requestParameterProvider;

	@Inject
	public ParameterActionArgumentResolver(
			@RequestParameters Provider<Map<String, String[]>> requestParameterProvider,
			ParameterConverter parameterConverter,
			Provider<ValidationResults> validationResultsProvider) {
		_requestParameterProvider = requestParameterProvider;
		_parameterConverter = parameterConverter;
	}

	@Override
	public <T> T resolve(Key<T> key, Action action) throws ConfigurationException {

		Parameter parameter = (Parameter) key.getAnnotation();

		try {
			@SuppressWarnings("unchecked")
			T result = (T) _parameterConverter.convert(_requestParameterProvider.get(), key
					.getTypeLiteral().getType(), parameter.value());
			return result;
		} catch (ConversionFailedException e) {
			throw new ConfigurationException(e);
		} catch (EmptyValueException e) {
			return null;
		}
	}

}
