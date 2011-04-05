package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

public class ActionHandler extends Handler {

	private Action _action;
	private Provider<?> _controllerProvider;

	public ActionHandler(Action action)
	{
		this(action, null);
	}
	
	public ActionHandler(Action action, Provider<?> controllerProvider)
	{
		super(action.getName());
		
		_action = action;
		_controllerProvider = controllerProvider;
	}
	
	@Override
	public HandlerResult handle(String path) throws Throwable
	{
		Object controller = _controllerProvider.get();
		return handle(path, controller);
	}
	
	@Override
	public HandlerResult handle(String path, Object controller) throws Throwable
	{
		return new HandlerResult(_action.invoke(controller), controller);
	}
	
	@Override
	public String toString()
	{
		return (_controllerProvider == null ? "" : _controllerProvider.get().getClass().getSimpleName() + " > ") + getName() + "()";
	}
}
