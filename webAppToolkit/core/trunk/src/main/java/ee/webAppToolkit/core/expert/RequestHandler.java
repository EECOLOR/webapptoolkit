package ee.webAppToolkit.core.expert;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.exceptions.HttpException;


@ImplementedBy(RequestHandlerImpl.class)
public interface RequestHandler {
	public Result handleRequest(String path) throws HttpException;
}
