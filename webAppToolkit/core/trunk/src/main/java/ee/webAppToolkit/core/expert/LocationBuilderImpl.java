package ee.webAppToolkit.core.expert;


public class LocationBuilderImpl implements LocationBuilder
{
	private String _location;
	
	@Override
	public void setLocation(String action, QueryString query, String fragment)
	{
		_location = "/" + action 
			+ (query == null ? "" : "?" + query.toString()) 
			+ (fragment == null ? "" : "#" + fragment);
	}

	@Override
	public void in(String context)
	{
		_location = context + _location;
	}

	@Override
	public String getLocation()
	{
		return _location;
	}

}
