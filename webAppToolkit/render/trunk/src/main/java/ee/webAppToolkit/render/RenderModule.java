package ee.webAppToolkit.render;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class RenderModule extends AbstractModule {

	@Override
	protected void configure() {
		
		install(new FactoryModuleBuilder().implement(Object.class,
				SimpleModel.class).build(ModelWrapper.class));
		
	}

}
