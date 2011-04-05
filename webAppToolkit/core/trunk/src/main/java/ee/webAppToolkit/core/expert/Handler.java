package ee.webAppToolkit.core.expert;

import ee.webAppToolkit.core.Result;

abstract public class Handler {
	private String _name;

	public Handler()
	{
	}
	
	public Handler(String name)
	{
		_name = name;
	}
	
	abstract public HandlerResult handle(String path) throws Throwable;
	abstract public HandlerResult handle(String path, Object controller)throws Throwable;

	public String getName() {
		return _name;
	}
	
	class HandlerResult
	{
		Result result;
		Object controller;
		
		public HandlerResult(Result result, Object controller) {
			this.result = result;
			this.controller = controller;
		}
	}

}
