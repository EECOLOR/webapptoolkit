package ee.webAppToolkit.core.expert.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.annotations.Forbidden;
import ee.webAppToolkit.core.annotations.SubController;
import ee.webAppToolkit.core.annotations.SubControllers;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionFactory;
import ee.webAppToolkit.core.expert.ControllerDescription;

public class ControllerDescriptionImpl implements ControllerDescription
{
	private ActionFactory _actionFactory;
	private Class<?> _controllerType;
	
	private List<Action> _actions;
	private List<SubController> _subControllers;
	
	private Set<String> _ignoredMethods;
	
	@Inject
	public ControllerDescriptionImpl(
			ActionFactory actionFactory, 
			@Assisted Class<?> controllerType) throws ConfigurationException
	{
		_actionFactory = actionFactory;
		_controllerType = controllerType;
		
		_ignoredMethods = new HashSet<String>();
		Method[] methods = WrappingController.class.getMethods();
		for (Method method : methods)
		{
			_ignoredMethods.add(method.getName());
		}
		
		_getActions();
		_getSubControllers();
	}
	
	private void _getActions() throws ConfigurationException
	{
		_actions = new ArrayList<Action>();
		
		Method[] methods = _controllerType.getMethods();
		
		for (Method method : methods)
		{
			if (_isAction(method))
			{
				Action action = _actionFactory.create(method);
				
				_actions.add(action);
			}
		}
	}
	
	private boolean _isAction(Method method)
	{
		return !method.isAnnotationPresent(Forbidden.class)
		&& Modifier.isPublic(method.getModifiers())
		&& !method.getDeclaringClass().equals(Object.class)
		&& !_ignoredMethods.contains(method.getName());
	}
	
	private void _getSubControllers() throws ConfigurationException
	{
		_subControllers = new ArrayList<SubController>();
		
		SubControllers subControllers = _controllerType.getAnnotation(SubControllers.class);
		
		if (subControllers != null)
		{
			for (SubController subController : subControllers.value())
			{
				_subControllers.add(subController);
			}
		}
		
		SubController subController = _controllerType.getAnnotation(SubController.class);
		
		if (subController != null)
		{
			_subControllers.add(subController);
		}
	}
	
	/* (non-Javadoc)
	 * @see ee.webAppToolkit.ControllerDescriptor#getAction(java.lang.String)
	 */
	@Override
	public List<Action> getActions()
	{
		return _actions;
	}

	@Override
	public List<SubController> getSubControllers()
	{
		return _subControllers;
	}

	@Override
	public Class<?> getControllerType() {
		return _controllerType;
	}

}
