package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.WrappingController;

public class ControllerHandler extends Handler {

	private Provider<?> _controllerProvider;
	private Handler _childHandler;

	public ControllerHandler(Provider<?> controllerProvider, Handler childHandler)
	{
		_controllerProvider = controllerProvider;
		_childHandler = childHandler;
	}
	
	public ControllerHandler(Provider<?> controllerProvider, Handler childHandler, String name)
	{
		super(name);
		
		_controllerProvider = controllerProvider;
		_childHandler = childHandler;
	}
	
	@Override
	public HandlerResult handle(String path) throws Throwable {
		WrappingController actualController = (WrappingController) _controllerProvider.get();
		
		String memberName = _childHandler.getName();
		
		actualController.beforeHandling(memberName);
		
		HandlerResult handlerResult = _childHandler.handle(path, actualController);
		
		Result result = actualController.wrapResult(handlerResult.result, memberName, handlerResult.controller);
		
		return new HandlerResult(result, actualController);
	}

	@Override
	public HandlerResult handle(String path, Object controller) throws Throwable
	{
		return handle(path);
	}
	
	@Override
	public String toString()
	{
		return _controllerProvider.get().getClass().getSimpleName() + " > " + _childHandler;
	}
}
