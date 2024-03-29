package ee.webAppToolkit.core.exceptions;

public class ConfigurationException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message)
	{
		super(message);
	}
	
	public ConfigurationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}
}
