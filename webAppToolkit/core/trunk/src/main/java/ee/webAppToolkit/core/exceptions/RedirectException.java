package ee.webAppToolkit.core.exceptions;

//Runtime exception to keep the API simple
public class RedirectException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private String _location;
	
	public RedirectException(String location)
	{
		_location = location;
	}
	
	public String getLocation()
	{
		return _location;
	}
}
