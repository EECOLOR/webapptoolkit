package ee.webAppToolkit.rendering.freemarker.expert.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import freemarker.ext.util.ModelFactory;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * @author erik
 * 
 * This Object Wrapper can be used with Freemarker to have an object wrapper that can easily
 * be modified. This means you can add wrappers for arbitrary types without writing a special 
 * wrapper. It works by adding model factories for specific types.
 */
@Singleton
public class DynamicObjectWrapper extends DefaultObjectWrapper implements GuiceObjectWrapper
{
	private Map<Class<?>, ModelFactory> _registeredModelFactories;
	private Map<Class<?>, ModelFactory> _typeCache;
	
	@Inject
	public DynamicObjectWrapper(Map<Class<?>, ModelFactory> modelFactories)
	{
		setExposureLevel(EXPOSE_PROPERTIES_ONLY);
		setExposeFields(true);
		
		_registeredModelFactories = Collections.synchronizedMap(modelFactories);
		_typeCache = Collections.synchronizedMap(new HashMap<Class<?>, ModelFactory>());
	}
	
	/**
	 * Overridden in order check if a registered model factory can handle the conversion. If no 
	 * model factory can handle it, the wrap method of the super class is used.
	 */
	@Override
	public TemplateModel wrap(Object object) throws TemplateModelException
    {
		if (object == null)
		{
			return super.wrap(object);
		}
		
    	TemplateModel result = null;
    	Class<?> type = object.getClass();
    	ModelFactory modelFactory = null;
    	
    	//check if we have a model factory for this specific type
    	if (_typeCache.containsKey(type))
    	{
    		modelFactory = _typeCache.get(type);
    		
    		if (modelFactory == null)
    		{
    			//we have no model factory, let super handle it
    			result = super.wrap(object);
    		} else
    		{
    			//use the factory to wrap the model
    			result = modelFactory.create(object, this);
    		}
    		
    	} else
    	{
    		Iterator<Class<?>> iterator = _registeredModelFactories.keySet().iterator();
    		
    		//loop through all registered model types
    		while (iterator.hasNext())
    		{
    			Class<?> targetType = iterator.next();
    			
    			//check if the object is eligible for wrapping by the current registration  
    			if (targetType.isAssignableFrom(type))
    			{
    				modelFactory = _registeredModelFactories.get(targetType);
    				
    				//add the type to the cache and wrap the object
    				_typeCache.put(type, modelFactory);
    				result = modelFactory.create(object, this);
    				break;
    			}
    		}
    		
    		if (result == null)
    		{
    			//we can not handle this type, to prevent trying next time we add it to the cache
    			_typeCache.put(type, null);
    			result = super.wrap(object);
    		}
    	}
    	
    	return result;
    }
}