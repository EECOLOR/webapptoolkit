package ee.webAppToolkit.core.expert.impl;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.inject.servlet.SessionScoped;

import ee.webAppToolkit.core.FlashMemory;

@SessionScoped
public class FlashMemoryImpl implements FlashMemory
{
	private static final long serialVersionUID = 1L;
	
	private Map<Object, Object> _current;
	private Map<Object, Object> _next;
	
	public FlashMemoryImpl()
	{
		_current = Collections.synchronizedMap(new HashMap<Object, Object>());
		_next = Collections.synchronizedMap(new HashMap<Object, Object>());
	}

	@Override
	public void next()
	{
		_current.clear();
		_current.putAll(_next);
		_next.clear();
	}

	@Override
	public void put(Object object)
	{
		_next.put(object.getClass(), object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type)
	{
		if (_next.containsKey(type))
		{
			return (T) _next.get(type);
		} else
		{
			return (T) _current.get(type);
		}
	}
	
	@Override
	public Object get(Type type)
	{
		if (_next.containsKey(type))
		{
			return _next.get(type);
		} else
		{
			return _current.get(type);
		}
	}
	
	@Override
	public void clear()
	{
		_current.clear();
		_next.clear();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return _next.containsKey(key) || _current.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return _next.containsValue(value) || _current.containsValue(value);
	}

	@Override
	public Set<Map.Entry<Object, Object>> entrySet()
	{
		Set<Map.Entry<Object, Object>> entrySet = new HashSet<Map.Entry<Object,Object>>();
		entrySet.addAll(_current.entrySet());
		entrySet.addAll(_next.entrySet());
		return entrySet;
	}

	@Override
	public Object get(Object key)
	{
		if (_next.containsKey(key))
		{
			return _next.get(key);
		} else
		{
			return _current.get(key);
		}
	}

	@Override
	public boolean isEmpty()
	{
		return _next.isEmpty() && _current.isEmpty();
	}

	@Override
	public Set<Object> keySet()
	{
		Set<Object> keySet = new HashSet<Object>();
		keySet.addAll(_current.keySet());
		keySet.addAll(_next.keySet());
		return keySet;
	}

	@Override
	public Object put(Object key, Object value)
	{
		return _next.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Object, ? extends Object> m)
	{
		_next.putAll(m);
	}

	@Override
	public Object remove(Object key)
	{
		if (_current.containsKey(key))
		{
			return _current.remove(key);
		} else
		{
			return _next.remove(key);
		}
	}

	@Override
	public int size()
	{
		return _next.size() + _current.size();
	}

	@Override
	public Collection<Object> values()
	{
		Set<Object> values = new HashSet<Object>();
		values.addAll(_current.values());
		values.addAll(_next.values());
		return values;
	}
}