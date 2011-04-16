package ee.webAppToolkit.core;

import java.util.HashMap;
import java.util.Map;

public class QueryString
{
	private Map<String, String> _queryString;
	
	/**
	 * Creates a new query string. Use like this: new QueryString("id", value, "somethingElse", otherValue)
	 */
	public QueryString(Object ...nameValues)
	{
		_queryString = new HashMap<String, String>();
		
		int counter = 0;
		
		String key = null;
		
		for (Object nameValue : nameValues)
		{
			counter++;
			if ((counter & 1) == 0)
			{
				_queryString.put(key, nameValue.toString());
			} else
			{
				key = nameValue.toString();
			}
		}
	}
	
	public Map<String, String> get()
	{
		return _queryString;
	}
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		
		boolean addedEntry = false;
		
		for (Map.Entry<String, String> entry : _queryString.entrySet())
		{
			if (addedEntry)
			{
				s.append("&");
			} else
			{
				addedEntry = true;
			}
			
			s.append(entry.getKey() + "=" + entry.getValue());
		}
		
		return s.toString();
	}
}
