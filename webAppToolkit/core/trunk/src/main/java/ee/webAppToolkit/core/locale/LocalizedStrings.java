package ee.webAppToolkit.core.locale;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.google.inject.Binder;
import com.google.inject.Key;


public class LocalizedStrings
{
	static public LocalizedString create(String value)
	{
		return new LocalizedStringImp(value);
	}

	static private Key<String> EMPTY_VALUE = Key.get(String.class, new LocalizedStringImp(""));
	static private Map<Key<String>, LocalizedStringProvider> _providers = new HashMap<Key<String>, LocalizedStringProvider>();
	
	static public void bindProperties(Binder binder, Properties properties, Locale locale)
	{
		binder = binder.skipSources(LocalizedStrings.class);

		LocalizedStringProvider provider;
		
		if (!_providers.containsKey(EMPTY_VALUE))
		{
			provider = _createProvider(binder, EMPTY_VALUE);
			provider.addLocalizedString(locale, "");
		}
		
		// use enumeration to include the default properties
		for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();)
		{
			String propertyName = (String) e.nextElement();
			String value = properties.getProperty(propertyName);
			
			Key<String> key = Key.get(String.class, new LocalizedStringImp(propertyName));
			
			if (_providers.containsKey(key))
			{
				provider = _providers.get(key);
			} else
			{
				provider = _createProvider(binder, key);
			}
			
			provider.addLocalizedString(locale, value);
		}
	}

	private static LocalizedStringProvider _createProvider(Binder binder, Key<String> key)
	{
		LocalizedStringProvider provider = new LocalizedStringProvider();
		binder.bind(key).toProvider(provider);
		_providers.put(key, provider);
		
		return provider;
	}
}
