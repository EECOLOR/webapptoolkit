package ee.webAppToolkit.rendering;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

//TODO rename to Rendering, also rename freemarker to rendering.freemarker (and for all freemarker.subModule)
public class RenderingModule extends AbstractModule {

	@Override
	protected void configure() {
		
		install(new FactoryModuleBuilder().implement(Object.class,
				SimpleModel.class).build(ModelWrapper.class));
		
	}

}
