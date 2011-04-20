package ee.webAppToolkit.parameters;

import ee.webAppToolkit.core.exceptions.EmptyValueException;

/**
 * <pre><code>
 * bind(new TypeLiteral&lt;Converter&lt;ValueType, TargetType&gt;&gt;(){}).to(...);
 * bind(TypeLiterals.converter(ValueType.class, TargetType.class)).to(...);
 * </code></pre>
 * 
 * @author EECOLOR
 *
 * @param <V> The type of value this converter can convert
 * @param <T> The target type of the convertion
 */
public interface Converter<V, T>
{
	/**
	 * Converts the value to the set type
	 * 
	 * @param value	The value to convert
	 * 
	 * @return an instance of the set type
	 * 
	 * @throws EmptyValueException
	 * @throws ConversionFailedException
	 */
	public T convert(V value) throws EmptyValueException, ConversionFailedException;
}
