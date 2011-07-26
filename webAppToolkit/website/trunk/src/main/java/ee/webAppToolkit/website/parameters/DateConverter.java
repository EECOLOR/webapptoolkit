package ee.webAppToolkit.website.parameters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ee.parameterConverter.ConversionFailedException;
import ee.parameterConverter.Converter;
import ee.parameterConverter.EmptyValueException;

public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String value) throws EmptyValueException, ConversionFailedException {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (ParseException e) {
			throw new ConversionFailedException("Problem converting date", value, e);
		}
	}
	
}
