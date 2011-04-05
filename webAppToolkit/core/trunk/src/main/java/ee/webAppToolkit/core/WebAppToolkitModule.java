package ee.webAppToolkit.core;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionFactory;
import ee.webAppToolkit.core.expert.ActionImpl;
import ee.webAppToolkit.core.expert.ControllerDescription;
import ee.webAppToolkit.core.expert.ControllerDescriptionFactory;
import ee.webAppToolkit.core.expert.ControllerDescriptionImpl;
import ee.webAppToolkit.core.expert.RequestHandler;
import ee.webAppToolkit.core.expert.RequestHandlerImpl;
import ee.webAppToolkit.core.expert.WebAppToolkit;

public abstract class WebAppToolkitModule extends AbstractModule {
	
	protected Binder handle(String path)
	{
		return new Binder(path);
	}
	
	private Map<String, Class<?>> _bindings = new HashMap<String, Class<?>>();
	
	@Override
	final protected void configure() {
		bind(new TypeLiteral<Map<String, Class<?>>>(){}).annotatedWith(WebAppToolkit.class).toInstance(_bindings);
		
		install(new FactoryModuleBuilder()
			.implement(ControllerDescription.class, ControllerDescriptionImpl.class)
			.build(ControllerDescriptionFactory.class));
		install(new FactoryModuleBuilder()
			.implement(Action.class, ActionImpl.class)
			.build(ActionFactory.class));
		
		//TODO remove
		bind(RequestHandler.class).to(RequestHandlerImpl.class).asEagerSingleton();
		
		configureControllers();
	}

	abstract protected void configureControllers();

	protected class Binder
	{
		private String _path;
		
		public Binder(String path) {
			_path = path;
		}

		public void with(Class<?> controller)
		{
			_bindings.put(_path, controller);
		}
	}
}
