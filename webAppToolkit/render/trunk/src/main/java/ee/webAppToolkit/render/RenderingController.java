package ee.webAppToolkit.render;

import javax.inject.Inject;

import ee.webAppToolkit.core.BasicController;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.expert.impl.DefaultResult;
import ee.webAppToolkit.render.exceptions.RenderFailedException;
import ee.webAppToolkit.render.expert.TemplateResolver;

public class RenderingController extends BasicController implements WrappingController {

	@Inject protected Renderer renderer;
	@Inject protected ModelWrapper modelWrapper;
	@Inject protected TemplateResolver templateResolver;
	
	protected ThreadLocal<String> currentMember;
	
	public RenderingController()
	{
		currentMember = new ThreadLocal<String>();
	}
	
	/**
	 * Sets the currentMember to the given memberName
	 */
	@Override
	public void beforeHandling(String memberName, Object controller) {
		currentMember.set(memberName);
	}

	/**
	 * Simply returns the given result
	 */
	@Override
	public Result wrapResult(Result result, String memberName, Object controller) {
		return result;
	}

	/**
	 * Calls render(null)
	 */
	protected Result render()
	{
		return render(null);
	}
	
	/**
	 * Calls render(model, currentMember.get(), preventWrapping)
	 * 
	 * @param model
	 * @param preventWrapping
	 */
	protected Result render(Object model, boolean preventWrapping)
	{
		return render(model, currentMember.get(), preventWrapping);
	}

	/**
	 * Calls render(model, currentMember.get())
	 * 
	 * @param model
	 */
	protected Result render(Object model)
	{
		return render(model, currentMember.get());
	}

	/**
	 * Calls render(model, template, null)
	 * 
	 * @param model
	 * @param template
	 * @return
	 */
	protected Result render(Object model, String template)
	{
		return render(model, template, null);
	}
	
	/**
	 * Calls render(model, template, null, preventWrapping)
	 * 
	 * @param model
	 * @param template
	 * @return
	 */
	protected Result render(Object model, String template, boolean preventWrapping)
	{
		return render(model, template, null, preventWrapping);
	}

	/**
	 * Calls render(model, template, contentType, null)
	 * 
	 * @param model
	 * @param template
	 * @param contentType
	 * @return
	 */
	protected Result render(Object model, String template, String contentType)
	{
		return render(model, template, contentType, null);
	}
	
	/**
	 * Calls render(model, template, contentType, null, preventWrapping)
	 * 
	 * @param model
	 * @param template
	 * @param contentType
	 * @return
	 */
	protected Result render(Object model, String template, String contentType, boolean preventWrapping)
	{
		return render(model, template, contentType, null, preventWrapping);
	}

	/**
	 * Calls render(model, template, contentType, characterEncoding, false)
	 * 
	 * @param model
	 * @param template
	 * @param contentType
	 * @param characterEncoding
	 * @return
	 */
	protected Result render(Object model, String template, String contentType, String characterEncoding)
	{
		return render(model, template, contentType, characterEncoding, false);
	}
	
	//TODO place comment
	/**
	 * 
	 * @param model
	 * @param template
	 * @param contentType
	 * @param characterEncoding
	 * @return
	 */
	protected Result render(Object model, String template, String contentType, String characterEncoding, boolean preventWrapping)
	{
		model = modelWrapper.wrap(model);
		
		String templatePath = templateResolver.resolveTemplate(this, template);
		
		String content;
		try
		{
			content = renderer.render(model, templatePath);
		} catch (RenderFailedException e)
		{
			throw new RuntimeException(e);
		}
		
		if (contentType == null)
		{
			contentType = "text/html";
		}
		
		return new DefaultResult(contentType, content, characterEncoding, preventWrapping);
	}
}
