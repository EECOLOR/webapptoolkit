package ee.webAppToolkit.core.expert;

import com.google.inject.Singleton;

@Singleton
public class ContextProviderImpl implements ContextProvider {

	private ThreadLocal<String> _context;

	public ContextProviderImpl()
	{
		_context = new ThreadLocal<String>();
	}
	
	/* (non-Javadoc)
	 * @see ee.webAppToolkit.core.expert.ContextProvider#setContext(java.lang.String)
	 */
	@Override
	public void setContext(String context)
	{
		_context.set(context);
	}
	
	/* (non-Javadoc)
	 * @see ee.webAppToolkit.core.expert.ContextProvider#get()
	 */
	@Override
	public String get() {
		return _context.get();
	}
	
}
