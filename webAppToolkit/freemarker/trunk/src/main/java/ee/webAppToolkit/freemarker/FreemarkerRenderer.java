package ee.webAppToolkit.freemarker;

import java.io.StringWriter;
import java.io.Writer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ee.webAppToolkit.render.Renderer;
import ee.webAppToolkit.render.exceptions.RenderFailedException;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Singleton
public class FreemarkerRenderer implements Renderer
{
	private Configuration _configuration;
	
	@Inject
	public FreemarkerRenderer(Configuration configuration)
	{
		_configuration = configuration;
	}
	
	@Override
	public String render(Object model, String template) throws RenderFailedException
	{
		try
		{
			Template ftl = _configuration.getTemplate(template + ".ftl");

			Writer writer = new StringWriter();
			ftl.process(model, writer);
			
			return writer.toString();
		} catch (Exception e)
		{
			throw new RenderFailedException(e); 
		}
	}
}
