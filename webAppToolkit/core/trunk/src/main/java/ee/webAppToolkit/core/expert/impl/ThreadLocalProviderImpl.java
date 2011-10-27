package ee.webAppToolkit.core.expert.impl;

import ee.webAppToolkit.core.expert.ThreadLocalProvider;


public class ThreadLocalProviderImpl <T> implements ThreadLocalProvider<T> {

	private ThreadLocal<T> _threadLocal;

	public ThreadLocalProviderImpl()
	{
		_threadLocal = new ThreadLocal<T>();
	}
	
	@Override
	public T get() {
		T value = _threadLocal.get();
		
		if (value == null) {
			throw new RuntimeException("No value has been set or the ThreadLocalProvider as not bound in the module using bindThreadLocalProvider");
		}
		
		return value;
	}

	@Override
	public void set(T value) {
		_threadLocal.set(value);
	}
	
}
