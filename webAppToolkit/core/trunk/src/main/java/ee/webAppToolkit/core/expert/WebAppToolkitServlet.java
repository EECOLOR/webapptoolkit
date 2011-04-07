package ee.webAppToolkit.core.expert;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.exceptions.HttpException;
import ee.webAppToolkit.core.exceptions.RedirectException;

//TODO think about caching. What types of results can be predicted to stay the same? What needs to be calculated each request?
//TODO limit injection of the Injector to classes that absolutely need it
//TODO Create singletons for the default classes that should be
@Singleton
public class WebAppToolkitServlet extends HttpServlet {
	@Inject
	private Logger _logger;

	private static final long serialVersionUID = 1L;

	private RequestHandler _requestHandler;

	@Inject
	public WebAppToolkitServlet(RequestHandler requestHandler) {
		_requestHandler = requestHandler;
	}

	@Override
	public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		// we don't need to take into account the servlet context since we are mapped to /*
		String contextPath = httpServletRequest.getContextPath();
		String pathInfo = httpServletRequest.getPathInfo();

		_logger.info("Attempt to service for contextPath '" + contextPath + "' and pathInfo '" + pathInfo + "'");

		// create the path
		String path = contextPath + pathInfo;

		// Grab the request method
		RequestMethod requestMethod = RequestMethod.valueOf(httpServletRequest.getMethod());

		
		try {
			// Handle the request
			Result result = _requestHandler.handleRequest(path);

			if (result != null) {
				String characterEncoding = result.getCharacterEncoding();
				String contentType = result.getContentType();

				// Create the response
				_logger.info("Writing result to output stream, character encoding '" + characterEncoding
						+ "', content type '" + contentType + "'");
				for (Entry<String, String> entry : result.getHeaders().entrySet()) {
					httpServletResponse.addHeader(entry.getKey(), entry.getValue());
				}
				httpServletResponse.setCharacterEncoding(characterEncoding);
				httpServletResponse.setContentType(contentType);
				httpServletResponse.getOutputStream().write(result.getBytes());
			}
		} catch (RedirectException e) {
			// handle redirects
			LocationBuilder locationBuilder = e.getLocationBuilder();
			locationBuilder.in(contextPath);

			String location = locationBuilder.getLocation();
			_logger.info("Redirecting to location '" + location + "'");
			httpServletResponse.sendRedirect(location);
		} catch (HttpException e) {
			int statusCode = e.getStatusCode();
			String message = e.getMessage();
			if (statusCode == 500) {
				_logger.info("Error processing request, message '" + message + "'");
				StringWriter s = new StringWriter();
				e.printStackTrace(new PrintWriter(s));
				httpServletResponse.sendError(statusCode, s.toString());
			} else {
				httpServletResponse.sendError(statusCode, message);
			}
		}
	}
}
