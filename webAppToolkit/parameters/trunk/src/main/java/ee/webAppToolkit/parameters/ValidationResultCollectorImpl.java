package ee.webAppToolkit.parameters;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.ValidationResultCollector;

public class ValidationResultCollectorImpl implements ValidationResultCollector {

	private Provider<ValidationResults> _validationResultsProvider;
	private Injector _injector;
	
	@Inject
	public ValidationResultCollectorImpl(Injector injector, Provider<ValidationResults> validationResultsProvider)
	{
		_validationResultsProvider = validationResultsProvider;
		_injector = injector;
	}
	
	@Override
	public void processException(List<String> parameterPath, Throwable exception) {
		
		Object originalValue = null;
		
		if (exception instanceof ConversionFailedException)
		{
			ConversionFailedException conversionFailedException = (ConversionFailedException) exception;
			originalValue = conversionFailedException.getOriginalValue();
			exception = conversionFailedException.getCause();
		}
		
		@SuppressWarnings("unchecked")
		TypeLiteral<?> typeLiteral = (TypeLiteral<ExceptionConverter<?>>) TypeLiteral.get(Types.newParameterizedType(ExceptionConverter.class, exception.getClass()));
		
		List<?> bindings = _injector.findBindingsByType(typeLiteral);
		
		ValidationResult validationResult;
		
		if (bindings.size() > 0)
		{
			@SuppressWarnings("unchecked")
			ExceptionConverter<Throwable> converter = (ExceptionConverter<Throwable>) ((Binding<?>) bindings.get(0)).getProvider().get();
			
			validationResult = converter.convert(exception, originalValue);
		} else
		{
			throw new RuntimeException("Could not find an exception converter for " + exception);
		}

		//add the validation result
		
		ValidationResults validationResults = _validationResultsProvider.get();
		
		Iterator<String> pathIterator = parameterPath.iterator();
		
		while (pathIterator.hasNext())
		{
			String pathPart = pathIterator.next();
			
			ValidationResult currentValidationResult;
			
			if (validationResults.containsKey(pathPart))
			{
				currentValidationResult = validationResults.get(pathPart);
				
				if (pathIterator.hasNext())
				{
					if (currentValidationResult.isMap())
					{
						validationResults = currentValidationResult.asMap();
					} else
					{
						throw new RuntimeException("Could not add validation results, path overrides another path");
					}
				} else
				{
					if (currentValidationResult.isList())
					{
						currentValidationResult.asList().add(validationResult);
					} else
					{
						ValidationResultContainer validationResultContainer = new ValidationResultContainer();
						validationResultContainer.add(currentValidationResult);
						validationResultContainer.add(validationResult);
						validationResults.put(pathPart, validationResultContainer);
					}
				}
			} else
			{
				if (pathIterator.hasNext())
				{
					currentValidationResult = new ValidationResults();
					validationResults.put(pathPart, currentValidationResult);
					validationResults = currentValidationResult.asMap();
				} else
				{
					validationResults.put(pathPart, validationResult);
				}
			}
		}
		
	}
	
}
