package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import ee.webAppToolkit.core.Result;

public class ActionHandlerImpl implements Handler {

	private Action _action;
	private Provider<?> _controllerProvider;
	
	public ActionHandlerImpl(Action action)
	{
		this(action, null);
	}
	
	public ActionHandlerImpl(Action action, Provider<?> controllerProvider)
	{
		_action = action;
		_controllerProvider = controllerProvider;
	}
	
	@Override
	public Result handle() throws Throwable
	{
		Object controller = _controllerProvider.get();
		return handle(controller);
	}
	
	@Override
	public Result handle(Object controller) throws Throwable
	{
		return _action.invoke(controller);
	}

	@Override
	public String toString()
	{
		return (_controllerProvider == null ? "" : _controllerProvider.get().getClass().getSimpleName() + " > ") + _action.getName() + "()";
	}
}
