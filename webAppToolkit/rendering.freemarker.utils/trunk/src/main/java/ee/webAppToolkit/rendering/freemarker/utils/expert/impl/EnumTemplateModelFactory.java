package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

import ee.webAppToolkit.localization.LocalizedString;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class EnumTemplateModelFactory implements ModelFactory {

	private Injector _injector;

	@Inject
	public EnumTemplateModelFactory(Injector injector)
	{
		_injector = injector;
	}
	
	@Override
	public TemplateModel create(Object object, ObjectWrapper wrapper) {
		
		String value = object.toString();
		String label = null;
		
		LocalizedString annotation;
		try {
			annotation = object.getClass().getDeclaredField(value).getAnnotation(LocalizedString.class);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
		
		if (annotation != null)
		{
			label = _injector.getProvider(Key.get(String.class, annotation)).get();
		}
		
		if (label == null)
		{
			return new SimpleScalar(value);
		}
		
		try {
			return wrapper.wrap(new EnumModel(label, value));
		} catch (TemplateModelException e) {
			throw new RuntimeException(e);
		}
	}
	
	public class EnumModel
	{
		public String label;
		public String value;

		public EnumModel(String label, String value) {
			this.label = label;
			this.value = value;
		}
		
		public String toString() {
			return label;
		}
	}
}