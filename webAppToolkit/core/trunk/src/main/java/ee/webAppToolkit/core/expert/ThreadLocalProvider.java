package ee.webAppToolkit.core.expert;

import javax.inject.Provider;

import com.google.inject.ImplementedBy;

@ImplementedBy(ThreadLocalProviderImpl.class)
public interface ThreadLocalProvider <T> extends Provider<T> {
	public void set(T value);
}
