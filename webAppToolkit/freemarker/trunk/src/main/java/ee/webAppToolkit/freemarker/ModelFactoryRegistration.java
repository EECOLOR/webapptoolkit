package ee.webAppToolkit.freemarker;

import freemarker.ext.util.ModelFactory;

public class ModelFactoryRegistration {
	public ModelFactory modelFactory;
	public Class<?> type;

	public ModelFactoryRegistration(Class<?> type, ModelFactory modelFactory)
	{
		this.type = type;
		this.modelFactory = modelFactory;
	}
}
