package ee.webAppToolkit.navigation;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import ee.webAppToolkit.core.expert.ActionRegistrationListener;
import ee.webAppToolkit.core.expert.ControllerRegistrationListener;
import ee.webAppToolkit.navigation.expert.impl.ActionRegistrationListenerImpl;

public class NavigationModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<ActionRegistrationListener> actionListeners = Multibinder.newSetBinder(binder(), ActionRegistrationListener.class);
		actionListeners.addBinding().to(ActionRegistrationListenerImpl.class).asEagerSingleton();
		
		Multibinder<ControllerRegistrationListener> constrollerListeners = Multibinder.newSetBinder(binder(), ControllerRegistrationListener.class);
		constrollerListeners.addBinding().to(ActionRegistrationListenerImpl.class).asEagerSingleton();
	}

}
