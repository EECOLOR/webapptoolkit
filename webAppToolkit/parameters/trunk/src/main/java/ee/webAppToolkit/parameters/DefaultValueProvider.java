package ee.webAppToolkit.parameters;

public interface DefaultValueProvider<T>
{
	public T provide(Object context);
}
