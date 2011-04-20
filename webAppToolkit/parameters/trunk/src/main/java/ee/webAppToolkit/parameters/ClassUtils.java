package ee.webAppToolkit.parameters;

import java.util.HashMap;
import java.util.Map;

public class ClassUtils
{
	private static final Map<Class<?>, Class<?>> WRAPPER_TYPES = getWrapperTypes();
	
	private static Map<Class<?>, Class<?>> getWrapperTypes()
    {
		Map<Class<?>, Class<?>> wrapperTypes = new HashMap<Class<?>, Class<?>>();
        wrapperTypes.put(Boolean.TYPE, Boolean.class);
        wrapperTypes.put(Character.TYPE, Character.class);
        wrapperTypes.put(Byte.TYPE, Byte.class);
        wrapperTypes.put(Short.TYPE, Short.class);
        wrapperTypes.put(Integer.TYPE, Integer.class);
        wrapperTypes.put(Long.TYPE, Long.class);
        wrapperTypes.put(Float.TYPE, Float.class);
        wrapperTypes.put(Double.TYPE, Double.class);
        wrapperTypes.put(Void.TYPE, Void.class);
        
        return wrapperTypes;
    }
	
	static public Class<?> getWrapper(Class<?> primitiveType)
	{
		return WRAPPER_TYPES.get(primitiveType);
	}
	
	static public boolean isPrimitive(Class<?> type)
	{
		return type.isPrimitive() || WRAPPER_TYPES.containsValue(type);
	}
}
