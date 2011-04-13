package ee.webAppToolkit.core.expert;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.inject.Provider;

import ee.webAppToolkit.core.RequestMethod;

public class RequestMethodProvider implements Provider<RequestMethod> {

	private Provider<HttpServletRequest> _httpServletRequestProvider;

	@Inject
	public RequestMethodProvider(Provider<HttpServletRequest> httpServletRequestProvider)
	{
		_httpServletRequestProvider = httpServletRequestProvider;
	}
	
	@Override
	public RequestMethod get() {
		HttpServletRequest httpServletRequest = _httpServletRequestProvider.get();
		return RequestMethod.valueOf(httpServletRequest.getMethod());
	}
	
}
