package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import ee.webAppToolkit.core.Result;

public class ActionHandler extends Handler {

	private Action _action;
	private Provider<?> _controllerProvider;

	public ActionHandler(Action action)
	{
		this(null, action);
	}
	
	public ActionHandler(Provider<?> controllerProvider, Action action)
	{
		super(controllerProvider, action.getName());
		
		_action = action;
		_controllerProvider = controllerProvider;
	}
	
	@Override
	public Result handle(String path) throws Throwable
	{
		Object controller = _controllerProvider.get();
		return handle(path, controller);
	}
	
	@Override
	public Result handle(String path, Object controller) throws Throwable
	{
		return _action.invoke(controller);
	}
	
	@Override
	public String toString()
	{
		return (_controllerProvider == null ? "" : _controllerProvider.get().getClass().getSimpleName() + " > ") + getName() + "()";
	}
}
