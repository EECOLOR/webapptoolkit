package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import java.util.Map;

import javax.inject.Inject;

import ee.webAppToolkit.rendering.freemarker.expert.impl.DynamicObjectWrapper;
import ee.webAppToolkit.rendering.freemarker.utils.expert.CustomObjectTemplateModelFactory;
import freemarker.ext.util.ModelFactory;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class MetadataObjectWrapper extends DynamicObjectWrapper {

	private CustomObjectTemplateModelFactory _customObjectTemplateModelFactory;

	@Inject
	public MetadataObjectWrapper(Map<Class<?>, ModelFactory> modelFactories, CustomObjectTemplateModelFactory customObjectTemplateModelFactory) {
		super(modelFactories);
		
		_customObjectTemplateModelFactory = customObjectTemplateModelFactory;
	}

	@Override
	protected TemplateModel handleUnknownType(Object object) throws TemplateModelException
	{
		return _customObjectTemplateModelFactory.create(object, this);
	}
}
