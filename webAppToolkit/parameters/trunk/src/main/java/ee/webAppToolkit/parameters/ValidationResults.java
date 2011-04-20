package ee.webAppToolkit.parameters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ValidationResults extends ValidationResult implements Map<String, ValidationResultContainer>
{
	private Map<String, ValidationResultContainer> _validationResults;
	
	public ValidationResults()
	{
		this(null);
	}
	
	public ValidationResults(String errorMessage)
	{
		super(errorMessage);
		_validationResults = new HashMap<String, ValidationResultContainer>();
	}
	
	private boolean _getSubResultsValidated()
	{
		if (size() > 0)
		{
			Iterator<ValidationResultContainer> iterator = values().iterator();
			
			boolean validated = true;
			
			while (iterator.hasNext())
			{
				validated = iterator.next().getValidated();
				
				if (!validated)
				{
					break;
				}
			}
			
			return validated;
		} else
		{
			return true;
		}
	}
	
	@Override
	public boolean getValidated()
	{
		return super.getValidated() && _getSubResultsValidated();
	}
	
	public ValidationResultContainer put(String key, ValidationResult value)
	{
		ValidationResultContainer validationResultContainer;
		
		if (value instanceof ValidationResultContainer)
		{
			validationResultContainer = (ValidationResultContainer) value;
		} else
		{
			validationResultContainer = new ValidationResultContainer();
			validationResultContainer.add(value);
		}
		
		return _validationResults.put(key, validationResultContainer);
	}
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		s.append("[ValidationResults validated=" + getValidated() + "]");
		for (Map.Entry<String, ValidationResultContainer> entry : entrySet())
		{
			s.append("\n" + entry.getKey() + ": " + entry.getValue());
		}
		
		return s.toString();
	}
	
	@Override
	public void clear()
	{
		_validationResults.clear();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return _validationResults.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return _validationResults.containsKey(value);
	}

	@Override
	public Set<Map.Entry<String, ValidationResultContainer>> entrySet()
	{
		return _validationResults.entrySet();
	}

	@Override
	public ValidationResultContainer get(Object key)
	{
		return _validationResults.get(key);
	}

	@Override
	public boolean isEmpty()
	{
		return _validationResults.isEmpty();
	}

	@Override
	public Set<String> keySet()
	{
		return _validationResults.keySet();
	}

	@Override
	public ValidationResultContainer put(String key, ValidationResultContainer value)
	{
		return _validationResults.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends ValidationResultContainer> map)
	{
		_validationResults.putAll(map);
	}

	@Override
	public ValidationResultContainer remove(Object key)
	{
		return _validationResults.remove(key);
	}

	@Override
	public int size()
	{
		return _validationResults.size();
	}

	@Override
	public Collection<ValidationResultContainer> values()
	{
		return _validationResults.values();
	}
	
}
