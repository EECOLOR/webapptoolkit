package ee.webAppToolkit.core.expert;

import ee.webAppToolkit.core.Result;

public interface Handler {
	public Result handle() throws Throwable;
	public Result handle(Object controller) throws Throwable;
}
