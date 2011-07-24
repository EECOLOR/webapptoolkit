package ee.webAppToolkit.freemarker.metadata.expert.impl;

import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;

public class ClassModelFactory implements ModelFactory {

	@Override
	public TemplateModel create(Object object, ObjectWrapper wrapper) {
		return new SimpleScalar(((Class<?>) object).getName());
	}
	
}
