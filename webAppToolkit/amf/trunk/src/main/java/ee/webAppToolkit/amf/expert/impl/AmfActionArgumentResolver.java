package ee.webAppToolkit.amf.expert.impl;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import com.exadel.flamingo.flex.messaging.amf.io.AMF3Deserializer;
import com.google.inject.Key;

import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;

public class AmfActionArgumentResolver implements ActionArgumentResolver {

	private Provider<HttpServletRequest> _requestProvider;

	@Inject
	public AmfActionArgumentResolver(Provider<HttpServletRequest> requestProvider) {
		_requestProvider = requestProvider;
	}

	@Override
	public <T> T resolve(Key<T> key, Action action) throws ConfigurationException {

		HttpServletRequest request = _requestProvider.get();

		String contentType = request.getHeader("Content-type");
		
		if (contentType != null && contentType.equals("application/x-amf")) {
			try {
				AMF3Deserializer amf3Deserializer = new AMF3Deserializer(request.getInputStream());
				
				Object object = amf3Deserializer.readObject();

				Class<? super T> rawType = key.getTypeLiteral().getRawType();
				if (rawType.isArray() && object.getClass().isArray() && !rawType.getComponentType().equals(Object.class))
				{
					Object[] objectArray = (Object[]) object;
					//a cast is not enough, we need to create a new array
					object = Arrays.copyOf(objectArray, objectArray.length, rawType.asSubclass(Object[].class));
				}
				
				@SuppressWarnings("unchecked")
				T result = (T) object;
				
				return result;
			} catch (IOException e) {
				throw new ConfigurationException(e);
			}
		} else {
			throw new ConfigurationException(
					"The current request does not have a 'Content-type' header that contains 'application/x-amf'");
		}
	}

}
