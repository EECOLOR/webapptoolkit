package ee.webAppToolkit.core.expert;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.servlet.ServletScopes;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.annotations.DefaultCharacterEncoding;
import ee.webAppToolkit.core.annotations.Path;

public class DefaultBindingsModule extends AbstractModule {

	@Override
	protected void configure() {
		bindDefaultCharacterEncoding();
		bindPath();
		bindControllerDescriptionFactory();
		bindActionFactory();
		bindRequestMethod();
	}

	private void bindRequestMethod() {
		bind(RequestMethod.class).toProvider(RequestMethodProvider.class).in(ServletScopes.REQUEST);
	}

	protected void bindDefaultCharacterEncoding() {
		bind(String.class).annotatedWith(DefaultCharacterEncoding.class).toInstance("UTF-8");
		requestStaticInjection(DefaultResult.class);
	}

	protected void bindPath() {
		bind(String.class).annotatedWith(Path.class).toProvider(PathProvider.class).in(ServletScopes.REQUEST);
	}
	
	protected void bindControllerDescriptionFactory() {
		install(new FactoryModuleBuilder().implement(ControllerDescription.class, ControllerDescriptionImpl.class)
				.build(ControllerDescriptionFactory.class));
	}

	protected void bindActionFactory() {
		install(new FactoryModuleBuilder().implement(Action.class, ActionImpl.class).build(ActionFactory.class));
	}
}
