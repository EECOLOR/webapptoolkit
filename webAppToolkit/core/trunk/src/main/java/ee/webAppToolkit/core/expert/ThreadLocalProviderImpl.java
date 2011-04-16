package ee.webAppToolkit.core.expert;


public class ThreadLocalProviderImpl <T> implements ThreadLocalProvider<T> {

	private ThreadLocal<T> _threadLocal;

	public ThreadLocalProviderImpl()
	{
		_threadLocal = new ThreadLocal<T>();
	}
	
	@Override
	public T get() {
		return _threadLocal.get();
	}

	@Override
	public void set(T value) {
		_threadLocal.set(value);
	}
	
}
