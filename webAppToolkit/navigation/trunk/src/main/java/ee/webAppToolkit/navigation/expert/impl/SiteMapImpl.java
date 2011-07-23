package ee.webAppToolkit.navigation.expert.impl;

import java.util.HashMap;

import javax.inject.Inject;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;

import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.SiteMap;

@Singleton
public class SiteMapImpl extends HashMap<String, SiteMap> implements SiteMap {

	private static final long serialVersionUID = 1L;
	private String _displayName;
	private LocalizedString _localizedString;
	private Injector _injector;

	@Inject
	public SiteMapImpl(Injector injector)
	{
		_injector = injector;
	}
	
	@Override
	public String getDisplayName() {
		
		if (_displayName == null)
		{
			try
			{
				return _injector.getInstance(Key.get(String.class, _localizedString));
			} catch (ConfigurationException e)
			{
				throw new RuntimeException("Problem with @LocalizedString annotation in @NavigationDisplayName annotation", e);
			}
		} else
		{
			return _displayName;
		}
	}

	@Override
	public void setDisplayName(String displayName) {
		_displayName = displayName;
	}

	@Override
	public void setDisplayName(LocalizedString localizedString) {
		_localizedString = localizedString;
	}
}
