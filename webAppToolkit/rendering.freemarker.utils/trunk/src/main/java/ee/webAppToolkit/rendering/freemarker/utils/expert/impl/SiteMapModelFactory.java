package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import ee.webAppToolkit.navigation.SiteMap;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;

public class SiteMapModelFactory implements ModelFactory {

	@Override
	public TemplateModel create(Object object, ObjectWrapper wrapper) {
		return new SiteMapModel((SiteMap) object, wrapper);
	}

}
