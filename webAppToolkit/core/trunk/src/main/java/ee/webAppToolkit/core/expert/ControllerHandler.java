package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.WrappingController;

public class ControllerHandler extends Handler {

	private Handler _childHandler;

	public ControllerHandler(Provider<?> controllerProvider, Handler childHandler)
	{
		super(controllerProvider);
		
		_childHandler = childHandler;
	}
	
	public ControllerHandler(Provider<?> controllerProvider, Handler childHandler, String name)
	{
		super(controllerProvider, name);
		
		_childHandler = childHandler;
	}
	
	@Override
	public Result handle(String path) throws Throwable {
		WrappingController actualController = (WrappingController) getControllerProvider().get();
		
		String memberName = _childHandler.getName();
		
		Object controller;
		
		if (_childHandler instanceof ControllerHandler)
		{
			controller = _childHandler.getControllerProvider().get();
		} else
		{
			controller = actualController;
		}
		
		actualController.beforeHandling(memberName, controller);
		
		Result result = _childHandler.handle(path, controller);
		
		result = actualController.wrapResult(result, memberName, controller);
		
		return result;
	}

	@Override
	public Result handle(String path, Object controller) throws Throwable
	{
		return handle(path);
	}
	
	@Override
	public String toString()
	{
		return getControllerProvider().get().getClass().getSimpleName() + " > " + _childHandler;
	}
}
