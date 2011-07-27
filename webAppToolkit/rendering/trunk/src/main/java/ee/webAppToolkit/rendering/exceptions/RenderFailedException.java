package ee.webAppToolkit.rendering.exceptions;

public class RenderFailedException extends Exception
{
	private static final long serialVersionUID = 1L;

	public RenderFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RenderFailedException(String message)
	{
		super(message);
	}

	public RenderFailedException(Throwable cause)
	{
		super(cause);
	}

}
