package ee.webAppToolkit.core.expert.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Delete;
import ee.webAppToolkit.core.annotations.Get;
import ee.webAppToolkit.core.annotations.Post;
import ee.webAppToolkit.core.annotations.Put;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;

public class ActionImpl implements Action
{
	static private Set<RequestMethod> ALL_REQUEST_METHODS = _getAllRequestMethods();
	
	static private Set<RequestMethod> _getAllRequestMethods()
	{
		HashSet<RequestMethod> requestMethods = new HashSet<RequestMethod>();
		requestMethods.add(RequestMethod.GET);
		requestMethods.add(RequestMethod.POST);
		requestMethods.add(RequestMethod.PUT);
		requestMethods.add(RequestMethod.DELETE);
		
		return Collections.unmodifiableSet(requestMethods);
	}
	
	private ActionArgumentResolver _actionArgumentResolver;
	private Method _method;
	private String _name;
	
	private Set<RequestMethod> _requestMethods;
	private List<ArgumentInformation> _argumentInformation;
	
	@Inject
	public ActionImpl(ActionArgumentResolver actionArgumentResolver, @Assisted Method method) throws ConfigurationException
	{
		_actionArgumentResolver = actionArgumentResolver;
		_method = method;
		_name = method.getName();

		_determineRequestMethods();
		_getParameterKeys();
	}

	private void _determineRequestMethods()
	{
		Set<RequestMethod> requestMethods = new HashSet<RequestMethod>();
		if (_method.isAnnotationPresent(Get.class)) requestMethods.add(RequestMethod.GET);
		if (_method.isAnnotationPresent(Post.class)) requestMethods.add(RequestMethod.POST);
		if (_method.isAnnotationPresent(Put.class)) requestMethods.add(RequestMethod.PUT);
		if (_method.isAnnotationPresent(Delete.class)) requestMethods.add(RequestMethod.DELETE);
		
		if (requestMethods.size() > 0)
		{
			//make it unmodifiable
			_requestMethods = Collections.unmodifiableSet(requestMethods);
		} else
		{
			_requestMethods = ALL_REQUEST_METHODS;
		}
	}
	
	private void _getParameterKeys() throws ConfigurationException
	{
		Type[] parameterTypes = _method.getGenericParameterTypes();
		Annotation[][] parameterAnnotations = _method.getParameterAnnotations();
		
		_argumentInformation = new ArrayList<ArgumentInformation>(parameterTypes.length);
		
		for (int i = 0; i < parameterTypes.length; i++)
		{
			Annotation currentAnnotation = null;
			boolean optional = false;
			
			for (Annotation annotation : parameterAnnotations[i])
			{
				Class<? extends Annotation> annotationType = annotation.annotationType();
				
				if (annotationType.getSimpleName().equals("Optional"))
				{
					optional = true;
				} else
				{
					if (annotationType.isAnnotationPresent(BindingAnnotation.class))
					{
						if (currentAnnotation == null)
						{
							currentAnnotation = annotation;
							
							//check if the annotation itself is annotated with optional
							
							Annotation[] annotations = annotationType.getAnnotations();
							
							for (Annotation annotationAnnotation : annotations)
							{
								if (annotationAnnotation.annotationType().getSimpleName().equals("Optional"))
								{
									optional = true;
									break;
								}
							}
						} else
						{
							throw new ConfigurationException("Only a single annotation annotated with @BindingAnnotation can be present at parameter " + i + " of '" + _method.getDeclaringClass().getName() + "." + getName());
						}
					}
					
				}
			}
			
			Type parameterType = parameterTypes[i];
			
			Key<?> key;
			if (currentAnnotation == null)
			{
				key = Key.get(parameterType);
			} else
			{
				key = Key.get(parameterType, currentAnnotation);
			}
			
			boolean isPrimitive = parameterType instanceof Class && ((Class<?>) parameterType).isPrimitive();
			
			_argumentInformation.add(i, new ArgumentInformation(key, optional, isPrimitive));
		}
	}
	
	@Override
	public Set<RequestMethod> getRequestMethods() {
		return _requestMethods;
	}

	/* (non-Javadoc)
	 * @see ee.webAppToolkit.Action#invoke(java.lang.Object)
	 */
	@Override
	public Result invoke(Object obj) throws Throwable
	{
		Object[] args = new Object[_argumentInformation.size()];
		
		int l = args.length;
		
		while (l-- > 0)
		{
			ArgumentInformation parameterInformation = _argumentInformation.get(l);
			boolean optional = parameterInformation.optional;
			Key<?> key = parameterInformation.key;
			try
			{
				Object result = _actionArgumentResolver.resolve(key, this);
				
				if (result == null)
				{
					if (optional)
					{
						//no problem
						result = _getDefaultValue(key.getTypeLiteral().getRawType(), parameterInformation.isPrimitive);
					} else
					{
						throw new ConfigurationException("There was a problem resolving the argument value for argument " + l + " of method '" + getName() + "'. If it's optional, mark it (or the annotation that was used for resolving it) with @Optional");
					}
				}
				
				args[l] = result;
			} catch (ConfigurationException e)
			{
				throw new ConfigurationException("There was a problem resolving the argument value for argument " + l + " of method '" + getName() + "'", e);
			}
		}
		
		Object result;
		try
		{
			result = _method.invoke(obj, args);
		} catch (InvocationTargetException e)
		{
			throw e.getCause();
		} catch (IllegalArgumentException e)
		{
			throw new ConfigurationException("Problem invoking method: '" + getName() + "'. Illigal argument, probably a type mismatch. \nTypes:\n" + Arrays.toString(_method.getParameterTypes()) + "\nValues\n" + Arrays.toString(args), e);
		}
		
		if (result != null)
		{
			if (!(result instanceof Result))
			{
				throw new ConfigurationException("Result of invocation of action '" + getName() + "' in '" + _method.getDeclaringClass().getName() + "' did not return an instance of Result");
			}
		}
		
		return (Result) result;
	}
	
	private Object _getDefaultValue(Class<?> rawType, boolean isPrimitive)
	{
		if (isPrimitive)
		{
			if (rawType.equals(Byte.class))
			{
				return 0;
			} else if (rawType.equals(Short.class))
			{
				return 0;
			} else if (rawType.equals(Integer.class))
			{
				return 0;
			} else if (rawType.equals(Long.class))
			{
				return 0L;
			} else if (rawType.equals(Float.class))
			{
				return 0.0f;
			} else if (rawType.equals(Double.class))
			{
				return 0.0d;
			} else if (rawType.equals(Character.class))
			{
				return '\u0000';
			} else if (rawType.equals(Boolean.class))
			{
				return false;
			} else
			{
				return null;
			}
		} else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ee.webAppToolkit.Action#getName()
	 */
	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public Class<?> getReturnType()
	{
		return _method.getReturnType();
	}
	
	@Override
	public Annotation[] getAnnotations() {
		return _method.getAnnotations();
	}
	
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass)
	{
		return _method.isAnnotationPresent(annotationClass);
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return _method.getAnnotation(annotationClass);
	}

	class ArgumentInformation
	{
		Key<?> key;
		boolean isPrimitive;
		boolean optional;
		
		ArgumentInformation(Key<?> key, boolean optional, boolean isPrimitive)
		{
			this.key = key;
			this.optional = optional;
			this.isPrimitive = isPrimitive;
		}
	}
}
