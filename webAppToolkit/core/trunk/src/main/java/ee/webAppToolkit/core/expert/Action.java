package ee.webAppToolkit.core.expert;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.Result;

@ImplementedBy(ActionImpl.class)
public interface Action
{
	public String getName();
	public boolean allows(RequestMethod requestMethod);
	public Class<?> getReturnType();
	public Result invoke(Object instance) throws Throwable;
}