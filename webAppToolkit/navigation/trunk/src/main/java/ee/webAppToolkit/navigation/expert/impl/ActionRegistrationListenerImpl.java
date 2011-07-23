package ee.webAppToolkit.navigation.expert.impl;

import javax.inject.Inject;

import com.google.inject.Injector;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionRegistrationListener;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.SiteMap;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;

public class ActionRegistrationListenerImpl implements ActionRegistrationListener {

	private Injector _injector;
	private SiteMap _siteMap;
	
	@Inject
	public ActionRegistrationListenerImpl(Injector injector, SiteMap siteMap)
	{
		_injector = injector;
		_siteMap = siteMap;
	}
	
	private String[] _getPathSegments(String path) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		return path.split("/");
	}

	private void _addPagesForPath(String[] pathSegments, String displayName, LocalizedString localizedString) {

		SiteMap current = _siteMap;
		SiteMap newSiteMap;

		for (int i = 0; i < pathSegments.length; i++) {
			String pathSegment = pathSegments[i];
			if (current.containsKey(pathSegment)) {
				current = current.get(pathSegment);
			} else {
				newSiteMap = new SiteMapImpl(_injector);
				
				if (i == pathSegments.length - 1)
				{
					newSiteMap.setDisplayName(displayName);
					newSiteMap.setDisplayName(localizedString);
				}
				
				current.put(pathSegment, newSiteMap);
				current = newSiteMap;
			}
		}
	}

	private boolean _containsPath(String[] pathSegments) {

		SiteMap current = _siteMap;

		for (String pathSegment : pathSegments) {
			if (current.containsKey(pathSegment)) {
				current = current.get(pathSegment);
			} else {
				return false;
			}
		}

		return true;
	}
	
	@Override
	public void actionRegistered(String path, Action action) {
		String[] pathSegments = _getPathSegments(path);
		
		if (action.getRequestMethods().contains(RequestMethod.GET) &&
			!action.isAnnotationPresent(HideFromNavigation.class) &&
			!_containsPath(pathSegments))
		{
			String displayName = null;
			LocalizedString localizedString = null;
			
			if (action.isAnnotationPresent(NavigationDisplayName.class))
			{
				NavigationDisplayName displayNameAnnotation = action.getAnnotation(NavigationDisplayName.class);
				localizedString = displayNameAnnotation.value();
			} else
			{
				displayName = pathSegments[pathSegments.length - 1];
			}

			_addPagesForPath(pathSegments, displayName, localizedString);
		}
	}

}
