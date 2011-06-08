package ee.webAppToolkit.render;

import ee.webAppToolkit.render.exceptions.RenderFailedException;

public interface Renderer
{
	public String render(Object model, String template) throws RenderFailedException;
}
