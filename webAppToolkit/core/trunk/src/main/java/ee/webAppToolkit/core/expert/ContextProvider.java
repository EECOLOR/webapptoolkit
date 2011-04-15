package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import com.google.inject.ImplementedBy;

@ImplementedBy(ContextProviderImpl.class)
public interface ContextProvider extends Provider<String> {

	public void setContext(String context);
}