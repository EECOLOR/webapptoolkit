package ee.webAppToolkit.core.exceptions;

public class HttpException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private int _statusCode;
	
	public HttpException(int statusCode)
	{
		_statusCode = statusCode;
	}
	
	public HttpException(int statusCode, Throwable cause)
	{
		super(cause);
		_statusCode = statusCode;
	}
	
	public HttpException(int statusCode, String message)
	{
		super(message);
		_statusCode = statusCode;
	}
	
	public int getStatusCode()
	{
		return _statusCode;
	}

	@Override
	public String getMessage()
	{
		String message = super.getMessage();
		
		if (message == null)
		{
			Throwable cause = getCause();
			if (cause != null)
			{
				message = cause.getMessage();
			}
		}
		return message;
	}
}
