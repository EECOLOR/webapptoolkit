# Converters #

Converters can be used to convert data from the parameters to another type. You can specify converters for `Map<String, String[]>`, `String[]` and `String`.

An example converter:

```
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
```

You register a `Converter` like this:

```
bind(new TypeLiteral<Converter<String, Date>>(){}).to(DateConverter.class);
```