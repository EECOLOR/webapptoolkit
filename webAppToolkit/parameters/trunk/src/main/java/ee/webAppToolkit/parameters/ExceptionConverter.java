package ee.webAppToolkit.parameters;


/**
 * <pre><code>
 * bind(new TypeLiteral&lt;ExceptionConverter&lt;ExceptionType&gt;&gt;(){}).to(...);
 * </code></pre>
 * 
 * @author EECOLOR
 *
 * @param <T> The type of exception this converter can convert
 */
public interface ExceptionConverter<T extends Throwable>
{
	public ValidationResult convert(T e, Object originalValue);
}
