package ee.webAppToolkit.rendering;

import ee.webAppToolkit.rendering.exceptions.RenderFailedException;

public interface Renderer
{
	public String render(Object model, String template) throws RenderFailedException;
}
