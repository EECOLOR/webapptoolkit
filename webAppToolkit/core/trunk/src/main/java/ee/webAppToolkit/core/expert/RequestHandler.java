package ee.webAppToolkit.core.expert;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.HttpException;
import ee.webAppToolkit.core.expert.impl.RequestHandlerImpl;


@ImplementedBy(RequestHandlerImpl.class)
public interface RequestHandler {
	public void init(String context) throws ConfigurationException;
	public Result handleRequest() throws HttpException;
}
