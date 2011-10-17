package ee.webAppToolkit.localization.expert.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

import ee.webAppToolkit.localization.LocaleResolver;

/**
 * Is bound to a strings annotated with the LocalizedString annotation. Will retrieve the 
 * string for the current locale. The current locale is determined through the injected 
 * LocaleResolver.
 * 
 * @author EECOLOR
 */
public class LocalizedStringProvider implements Provider<String>
{
	private String _key;
	private Map<Locale, String> _localizedStrings;
	private LocaleResolver _localeResolver;

	public LocalizedStringProvider(String key)
	{
		_localizedStrings = new HashMap<Locale, String>();
	}

	@Inject
	/**
	 * We can not use constructor injection because this instance is created manually
	 */
	public void injectLocaleResolver(LocaleResolver localeResolver)
	{
		_localeResolver = localeResolver;
	}
	
	public void addLocalizedString(Locale locale, String string)
	{
		_localizedStrings.put(locale, string);
	}
	
	@Override
	public String get()
	{
		Iterator<Locale> locales = _localeResolver.getLocaleChain().iterator();
		
		String localizedString;
		
		do
		{
			Locale locale = locales.next();
			localizedString = _localizedStrings.get(locale);
		} while (localizedString == null && locales.hasNext());
		
		if (localizedString == null)
		{
			return _key;
		} else
		{
			return localizedString;
		}
	}
}
