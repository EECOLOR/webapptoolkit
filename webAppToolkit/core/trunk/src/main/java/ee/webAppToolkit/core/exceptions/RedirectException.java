package ee.webAppToolkit.core.exceptions;

import ee.webAppToolkit.core.expert.LocationBuilder;


//Runtime exception to keep the API simple
public class RedirectException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	private LocationBuilder _locationBuilder;
	
	public RedirectException(LocationBuilder locationBuilder)
	{
		_locationBuilder = locationBuilder;
	}
	
	public LocationBuilder getLocationBuilder()
	{
		return _locationBuilder;
	}
}
