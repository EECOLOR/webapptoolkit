package ee.webAppToolkit.example.projectTimeTracking.converters;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;
import ee.parameterConverter.ParameterConverter;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.ValidationResult;
import ee.webAppToolkit.parameters.exceptions.ValidationResultException;

public class TimeConverter implements Converter<String[], Date> {

	private Provider<Map<String, String[]>> _requestParameterProvider;
	private ParameterConverter _parameterConverter;
	private Provider<Calendar> _calendarProvider;
	private String _conversionProblem;

	@Inject
	public TimeConverter(
			@RequestParameters Provider<Map<String, String[]>> requestParameterProvider,
			ParameterConverter parameterConverter,
			Provider<Calendar> calendarProvider,
			@LocalizedString("validation.time.conversion") String conversionProblem) {
		_requestParameterProvider = requestParameterProvider;
		_parameterConverter = parameterConverter;
		_calendarProvider = calendarProvider;
		_conversionProblem = conversionProblem;
	}

	@Override
	public Date convert(String[] value) throws EmptyValueException,
			ConversionFailedException {

		//Check if the value has length
		if (value.length != 1) {
			throw new ConversionFailedException(
					"Could not convert for time, no value was given");
		}

		//Get the value to convert
		String timeString = value[0];

		//Check the length
		if (timeString.length() == 0) {
			throw new EmptyValueException("The given value is empty");
		}
		
		Calendar parsedTime;
		try {
			//parse the time and put it into a calendar
			parsedTime = _calendarProvider.get();
			parsedTime.setTime(new SimpleDateFormat("HH:mm").parse(timeString));
		} catch (ParseException e) {
			//Get the error message and create an exception containing the validation result
			String conversionProblem = MessageFormat.format(_conversionProblem, timeString);
			throw new ConversionFailedException(new ValidationResultException(
					new ValidationResult(timeString, conversionProblem)));
		}
		
		// first get the date for this item
		Map<String, String[]> requestParameters = _requestParameterProvider
				.get();
		if (requestParameters.containsKey("date")) {
			Date date = _parameterConverter.convert(requestParameters,
					Date.class, "date");
			Calendar calendar = _calendarProvider.get();
			//Set the date
			calendar.setTime(date);
			//Set the time
			calendar.set(Calendar.HOUR_OF_DAY, parsedTime.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, parsedTime.get(Calendar.MINUTE));
			
			return calendar.getTime();
		} else {
			throw new ConversionFailedException(
					"Could not convert to a time, could not find a 'date' key in the given parameters");
		}
	}
}
