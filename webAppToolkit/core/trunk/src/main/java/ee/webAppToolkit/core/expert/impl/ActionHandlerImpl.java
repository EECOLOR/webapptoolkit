package ee.webAppToolkit.core.expert.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Provider;
import javax.servlet.http.HttpServletResponse;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.HttpException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionHandler;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;

//TODO refactor parts of this with the controller handler
public class ActionHandlerImpl implements ActionHandler {

	private Map<RequestMethod, Action> _actions;
	private ThreadLocalProvider<String> _contextProvider;
	private String _context;
	private Provider<?> _controllerProvider;
	private Provider<RequestMethod> _requestMethodProvider;
	
	public ActionHandlerImpl(Provider<RequestMethod> requestMethodProvider, Action action) throws ConfigurationException
	{
		this(requestMethodProvider, action, null, null, null);
	}
	
	public ActionHandlerImpl(Provider<RequestMethod> requestMethodProvider, Action action, Provider<?> controllerProvider, ThreadLocalProvider<String> contextProvider, String context) throws ConfigurationException
	{
		_requestMethodProvider = requestMethodProvider;
		_contextProvider = contextProvider;
		_context = context;
		_controllerProvider = controllerProvider;
		
		_actions = new HashMap<RequestMethod, Action>();
		addAction(action);
	}
	
	@Override
	public Result handle() throws Throwable
	{
		//because we create the controller we need to set the context
		_contextProvider.set(_context);
		
		Object controller = _controllerProvider.get();
		return handle(controller);
	}
	
	@Override
	public Result handle(Object controller) throws Throwable
	{
		RequestMethod requestMethod = _requestMethodProvider.get();
		if (_actions.containsKey(requestMethod))
		{
			return _actions.get(requestMethod).invoke(controller);
		}
		throw new HttpException(HttpServletResponse.SC_FORBIDDEN);
	}

	@Override
	public void addAction(Action action) throws ConfigurationException {
		Set<RequestMethod> requestMethods = action.getRequestMethods();
		
		for (RequestMethod requestMethod : requestMethods)
		{
			if (_actions.containsKey(requestMethod))
			{
				throw new ConfigurationException("Could not register double action with name '" + action.getName() + "' for request method " + requestMethod);
			}
			_actions.put(requestMethod, action);
		}
	}
	
	@Override
	public String toString()
	{
		String actionName = null;
		String requestMethods = "(";
		Set<Entry<RequestMethod, Action>> entrySet = _actions.entrySet();
		
		for (Entry<RequestMethod, Action> entry : entrySet)
		{
			if (actionName == null)
			{
				actionName = entry.getValue().getName();
			} else
			{
				requestMethods += ", ";
			}
			requestMethods += entry.getKey();
		}
		requestMethods += ")";
		return (_controllerProvider == null ? "" : _controllerProvider.get().getClass().getSimpleName() + " > ") + actionName + " " + requestMethods;
	}
}
