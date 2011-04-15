package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.WrappingController;

public class ControllerHandlerImpl implements ControllerHandler {

	private Provider<? extends WrappingController> _controllerProvider;
	private String _context;
	private ContextProvider _contextProvider;
	private Handler _childHandler;
	private boolean _childIsMember;
	private String _memberName;
	
	public ControllerHandlerImpl(Provider<? extends WrappingController> controllerProvider, ContextProvider contextProvider, String context, Handler childHandler, String memberName, boolean childIsMember)
	{
		_controllerProvider = controllerProvider;
		_context = context;
		_contextProvider = contextProvider;
		_childHandler = childHandler;
		_memberName = memberName;
		_childIsMember = childIsMember;
	}
	
	@Override
	public Result handle() throws Throwable {
		 return handle(_controllerProvider.get());
	}

	@Override
	public Result handle(WrappingController controller) throws Throwable
	{
		WrappingController subController = null;
		
		// set the current context
		_contextProvider.setContext(_context);
		
		if (_childIsMember)
		{
			if (_childHandler instanceof ControllerHandler)
			{
				subController = ((ControllerHandler) _childHandler).getControllerProvider().get();
			} else
			{
				subController = controller;
			}
		}
		
		//allow the current controller to do something with the request before we call the sub handler
		controller.beforeHandling(_memberName, subController);
		
		Result result;
		if (_childIsMember)
		{
			result = _childHandler.handle(subController);
		} else
		{
			result = _childHandler.handle();
		}
		
		// the context might have been changed in the child handler, we need to set it again
		_contextProvider.setContext(_context);
		
		if (result != null && !result.preventWrapping())
		{
			//allow the current controller to modify the result
			result = controller.wrapResult(result, _memberName, subController);
		}
		
		return result;
	}

	@Override
	public Result handle(Object controller) throws Throwable {
		return handle((WrappingController) controller);
	}

	@Override
	public Provider<? extends WrappingController> getControllerProvider() {
		return _controllerProvider;
	}

	@Override
	public String toString()
	{
		return _controllerProvider.get().getClass().getSimpleName() + " > " + _childHandler;
	}

}
