package ee.webAppToolkit.parameters.expert.impl;

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
import ee.webAppToolkit.parameters.ExceptionConverter;
import ee.webAppToolkit.parameters.ValidationResult;
import ee.webAppToolkit.parameters.ValidationResultContainer;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.exceptions.ValidationResultException;

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
		
		ValidationResult validationResult;
		
		if (exception instanceof ValidationResultException)
		{
			validationResult = ((ValidationResultException) exception).getValidationResult();
		} else
		{
			TypeLiteral<?> typeLiteral = TypeLiteral.get(Types.newParameterizedType(ExceptionConverter.class, exception.getClass()));
			
			List<?> bindings = _injector.findBindingsByType(typeLiteral);
			
			if (bindings.size() > 0)
			{
				@SuppressWarnings("unchecked")
				ExceptionConverter<Throwable> converter = (ExceptionConverter<Throwable>) ((Binding<?>) bindings.get(0)).getProvider().get();
				
				validationResult = converter.convert(exception, originalValue);
			} else
			{
				throw new RuntimeException("Could not find an exception converter for " + exception, exception);
			}
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
