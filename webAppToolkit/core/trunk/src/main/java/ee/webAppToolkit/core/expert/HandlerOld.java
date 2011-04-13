package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import ee.webAppToolkit.core.Result;

abstract public class HandlerOld {
	private String _name;
	private Provider<?> _controllerProvider;

	public HandlerOld(Provider<?> controllerProvider)
	{
		this(controllerProvider, null);
	}
	
	public HandlerOld(Provider<?> controllerProvider, String name)
	{
		_controllerProvider = controllerProvider;
		_name = name;
	}
	
	public Provider<?> getControllerProvider()
	{
		return _controllerProvider;
	}
	
	abstract public Result handle(String path) throws Throwable;
	abstract public Result handle(String path, Object controller)throws Throwable;

	public String getName() {
		return _name;
	}
}
