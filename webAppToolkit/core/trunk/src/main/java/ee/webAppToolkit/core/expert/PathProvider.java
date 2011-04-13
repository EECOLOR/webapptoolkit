package ee.webAppToolkit.core.expert;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

public class PathProvider implements Provider<String> {

	private Provider<HttpServletRequest> _httpServletRequestProvider;

	@Inject
	public PathProvider(Provider<HttpServletRequest> httpServletRequestProvider)
	{
		_httpServletRequestProvider = httpServletRequestProvider;
	}
	
	@Override
	public String get() {
		HttpServletRequest httpServletRequest = _httpServletRequestProvider.get();

		return httpServletRequest.getServletPath();
	}
	
}
