package ee.webAppToolkit.localization.impl;

import java.util.HashMap;
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
	private Map<Locale, String> _localizedStrings;
	private LocaleResolver _localeResolver;

	public LocalizedStringProvider()
	{
		_localizedStrings = new HashMap<Locale, String>();
	}

	@Inject
	/**
	 * We can not use constructor injection because this instance is create manually
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
		return _localizedStrings.get(_localeResolver.getLocale());
	}
}
