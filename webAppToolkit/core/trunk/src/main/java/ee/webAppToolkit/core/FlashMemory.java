package ee.webAppToolkit.core;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.expert.impl.FlashMemoryImpl;

@ImplementedBy(FlashMemoryImpl.class)
public interface FlashMemory extends Map<Object, Object>, Serializable
{
	public void next();
	
	/**
	 * Stores an object by type
	 */
	public void put(Object object);
	
	/**
	 * Returns null or an object of type T
	 */
	public <T> T get(Class<T> type);
	
	/**
	 * Returns null or an object of the given type
	 */
	public Object get(Type type);
}
