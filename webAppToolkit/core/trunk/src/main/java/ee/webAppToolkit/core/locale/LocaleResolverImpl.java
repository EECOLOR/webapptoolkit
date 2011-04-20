package ee.webAppToolkit.core.locale;

import java.util.Locale;

public class LocaleResolverImpl implements LocaleResolver
{

	/* (non-Javadoc)
	 * @see ee.webAppToolkit.LocaleResolver#getLocale()
	 */
	@Override
	public Locale getLocale()
	{
		return Locale.ENGLISH;
	}
	
}
