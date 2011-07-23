package ee.webAppToolkit.json.expert.impl;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.inject.Key;
import com.google.inject.servlet.RequestParameters;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;
import ee.webAppToolkit.json.annotations.Json;

public class JsonActionArgumentResolver implements ActionArgumentResolver {

	private Provider<Map<String, String[]>> _requestParameterProvider;
	private Provider<HttpServletRequest> _requestProvider;
	
	@Inject
	public JsonActionArgumentResolver(@RequestParameters Provider<Map<String, String[]>> requestParameterProvider,
									Provider<HttpServletRequest> requestProvider)
	{
		_requestParameterProvider = requestParameterProvider;
		_requestProvider = requestProvider;
	}
	
	@Override
	public <T> T resolve(Key<T> key, Action action) throws ConfigurationException {
		
		Json annotation = (Json) key.getAnnotation();
		
		Object object;
		Type type = key.getTypeLiteral().getType();
		Gson gson = new Gson();
		String annotationValue = annotation.value();
		
		if (annotationValue.length() > 0)
		{
			//get it from the parameter
			
			Map<String, String[]> requestParameters = _requestParameterProvider.get();
			String value = requestParameters.get(annotationValue)[0];
			
			object = gson.fromJson(value, type);
		} else
		{
			//get it from the request body
			
			HttpServletRequest request = _requestProvider.get();
			
			try {
				object = gson.fromJson(request.getReader(), type);
			} catch (Exception e) {
				throw new ConfigurationException(e);
			}
		}
		
		@SuppressWarnings("unchecked")
		T result = (T) object; 
		
		return result;
	}

}
