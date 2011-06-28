package ee.webAppToolkit.navigation;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import ee.webAppToolkit.core.expert.ActionRegistrationListener;
import ee.webAppToolkit.navigation.expert.impl.ActionRegistrationListenerImpl;

public class NavigationModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<ActionRegistrationListener> listeners = Multibinder.newSetBinder(binder(), ActionRegistrationListener.class);
		listeners.addBinding().to(ActionRegistrationListenerImpl.class).asEagerSingleton();
	}

}
