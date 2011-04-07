package ee.webAppToolkit.core.expert;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import ee.webAppToolkit.core.annotations.DefaultCharacterEncoding;

public class DefaultBindingsModule extends AbstractModule {

	@Override
	protected void configure() {
		bindDefaultCharacterEncoding();
		bindControllerDescriptionFactory();
		bindActionFactory();

	}

	protected void bindDefaultCharacterEncoding() {
		bind(String.class).annotatedWith(DefaultCharacterEncoding.class).toInstance("UTF-8");
		requestStaticInjection(DefaultResult.class);
	}

	protected void bindActionFactory() {
		install(new FactoryModuleBuilder().implement(Action.class, ActionImpl.class).build(ActionFactory.class));
	}

	protected void bindControllerDescriptionFactory() {
		install(new FactoryModuleBuilder().implement(ControllerDescription.class, ControllerDescriptionImpl.class)
				.build(ControllerDescriptionFactory.class));
	}

}
