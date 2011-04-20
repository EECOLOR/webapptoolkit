package ee.webAppToolkit.parameters;

public class ConversionFailedException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ConversionFailedException(String message, Throwable throwable)
	{
		super(message, throwable);
	}

	public ConversionFailedException(String message)
	{
		super(message);
	}
	
}
