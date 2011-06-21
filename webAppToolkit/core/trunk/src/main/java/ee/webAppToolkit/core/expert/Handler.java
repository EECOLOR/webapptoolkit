package ee.webAppToolkit.core.expert;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.exceptions.ConfigurationException;

public interface Handler {
	public Result handle() throws Throwable;
	public Result handle(Object controller) throws Throwable;
	public void addAction(Action action) throws ConfigurationException;
}
