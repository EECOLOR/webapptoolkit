package ee.webAppToolkit.core;

import javax.inject.Inject;
import javax.inject.Provider;

import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.exceptions.RedirectException;
import ee.webAppToolkit.core.expert.impl.DefaultResult;

public class BasicController {
	
	@Inject protected @Context Provider<String> contextProvider; 
	@Inject protected FlashMemory flash;
	
	protected void redirect(String action)
	{
		redirect(action, null);
	}

	protected void redirect(String action, QueryString query)
	{
		redirect(action, query, null);
	}

	protected void redirect(String action, QueryString queryString, String fragment)
	{
		StringBuilder stringBuilder = new StringBuilder(contextProvider.get());
		stringBuilder.append('/');
		stringBuilder.append(action);
		if (queryString != null)
		{
			stringBuilder.append("?");
			stringBuilder.append(queryString.toString());
		}
		if (fragment != null)
		{
			stringBuilder.append("#");
			stringBuilder.append(fragment);
		}

		throw new RedirectException(stringBuilder.toString());
	}
	
	protected Result output(String content)
	{
		return new DefaultResult(content);
	}
}
