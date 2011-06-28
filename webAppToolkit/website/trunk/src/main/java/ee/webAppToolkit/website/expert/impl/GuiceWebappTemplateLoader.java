package ee.webAppToolkit.website.expert.impl;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.google.inject.name.Named;

import freemarker.cache.WebappTemplateLoader;

public class GuiceWebappTemplateLoader extends WebappTemplateLoader {
	
	@Inject
	public GuiceWebappTemplateLoader(ServletContext servletContext, @Named("templatePath") String templatePath)
	{
		super(servletContext, templatePath);
	}
}
