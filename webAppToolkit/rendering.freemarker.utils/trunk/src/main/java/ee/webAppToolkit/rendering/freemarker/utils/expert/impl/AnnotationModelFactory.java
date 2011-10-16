package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.Injector;
import com.google.inject.Key;

import ee.webAppToolkit.localization.LocalizedString;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class AnnotationModelFactory implements ModelFactory {

	private Injector _injector;
	
	@Inject
	public AnnotationModelFactory(Injector injector)
	{
		_injector = injector;
	}
	
	private Map<String, Object> _createAnnotationMap(Annotation annotation)
	{
		Map<String, Object> annotationValues = new HashMap<String, Object>();
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		
		for (Method method : methods)
		{
			if (method.getParameterTypes().length == 0)
			{
				String name = method.getName();
				Object value = null;
				try
				{
					value = method.invoke(annotation);
				} catch (Exception e)
				{
					throw new RuntimeException(e);
				}
				
				//TODO add to documentation that this ignores class values
				if (!(value instanceof Class<?>))
				{
					annotationValues.put(name, value);
				}
			}
		}
		
		return annotationValues;
	}
	
	@Override
	public TemplateModel create(Object object, ObjectWrapper wrapper) {
		if (object instanceof LocalizedString)
		{
			//return the actual string
			return new SimpleScalar(_injector.getInstance(Key.get(String.class, (LocalizedString) object)));
		}
		
		Map<String, Object> annotationValues = _createAnnotationMap((Annotation) object);
		
		if (annotationValues.size() == 1 && annotationValues.containsKey("value"))
		{
			try {
				//return the actual value
				return wrapper.wrap(annotationValues.get("value"));
			} catch (TemplateModelException e) {
				throw new RuntimeException(e);
			}
		} else if (annotationValues.size() == 0)
		{
			//used for marker annotations, indicates the presence
			return TemplateBooleanModel.TRUE;
		} else
		{
			//return the values of the annotation
			return new SimpleHash(annotationValues, (BeansWrapper) wrapper);
		}
		
	}

}
