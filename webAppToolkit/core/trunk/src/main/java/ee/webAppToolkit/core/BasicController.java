package ee.webAppToolkit.core;

import javax.inject.Inject;
import javax.inject.Provider;

import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.exceptions.RedirectException;
import ee.webAppToolkit.core.expert.ControllerDescription;
import ee.webAppToolkit.core.expert.impl.DefaultResult;

public class BasicController {
	
	@Inject protected @Context Provider<String> contextProvider; 
	@Inject protected FlashMemory flash;
	
	/**
	 * Calls redirect(action, null)
	 */
	protected void redirect(String action)
	{
		redirect(action, null);
	}

	/**
	 * Calls redirect(action, queryString, null)
	 */
	protected void redirect(String action, QueryString queryString)
	{
		redirect(action, queryString, null);
	}

	/**
	 * <p>
	 * Stops execution by throwing a RedirectException. It builds the target url by 
	 * appending the action to the context and adding the query string and fragment if 
	 * they are present.
	 * </p>
	 * <p>
	 * The resulting redirect will be <code>context/action?queryString#fragment</code>
	 * </p>
	 * 
	 * @param action		The action to redirect to.
	 * @param queryString	The query string 
	 * @param fragment		The fragment, added after a #
	 */
	protected void redirect(String action, QueryString queryString, String fragment)
	{
		if (action.equals(ControllerDescription.INDEX))
		{
			action = "";
		}
		
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
	
	/**
	 * Calls output(content, false)
	 */
	protected Result output(String content)
	{
		return output(content, false);
	}
	
	/**
	 * Creates an instance of <code>Result</code> with the given content.
	 * 
	 * @param content			The content
	 * @param preventWrapping	If set to <code>true</code> the result will not be wrapped
	 * 
	 * @return A <code>Result</code> instance representing the given content
	 */
	protected Result output(String content, boolean preventWrapping)
	{
		return new DefaultResult(content, preventWrapping);
	}
}
