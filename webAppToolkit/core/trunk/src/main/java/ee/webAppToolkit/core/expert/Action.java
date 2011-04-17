package ee.webAppToolkit.core.expert;

import java.util.Set;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.expert.impl.ActionImpl;

@ImplementedBy(ActionImpl.class)
public interface Action
{
	public String getName();
	public Set<RequestMethod> getRequestMethods();
	public Class<?> getReturnType();
	public Result invoke(Object instance) throws Throwable;
}