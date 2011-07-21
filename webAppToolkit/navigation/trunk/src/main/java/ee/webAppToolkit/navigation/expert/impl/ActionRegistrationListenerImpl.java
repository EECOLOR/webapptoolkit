package ee.webAppToolkit.navigation.expert.impl;

import javax.inject.Inject;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.expert.Action;
import ee.webAppToolkit.core.expert.ActionRegistrationListener;
import ee.webAppToolkit.navigation.HideFromNavigation;
import ee.webAppToolkit.navigation.SiteMap;

public class ActionRegistrationListenerImpl implements ActionRegistrationListener {

	private SiteMap _siteMap;
	
	@Inject
	public ActionRegistrationListenerImpl(SiteMap siteMap)
	{
		_siteMap = siteMap;
	}
	
	private String[] _getPathSegments(String path) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		return path.split("/");
	}

	private void _addPagesForPath(String path) {

		String[] pathSegments = _getPathSegments(path);
		SiteMap current = _siteMap;
		SiteMap newSiteMap;

		for (String pathSegment : pathSegments) {
			newSiteMap = new SiteMapImpl();
			if (current.containsKey(pathSegment)) {
				current = current.get(pathSegment);
			} else {
				current.put(pathSegment, newSiteMap);
				current = newSiteMap;
			}
		}
	}

	private boolean _containsPath(String path) {

		String[] pathSegments = _getPathSegments(path);

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
		if (action.getRequestMethods().contains(RequestMethod.GET) &&
			!action.isAnnotationPresent(HideFromNavigation.class) &&
			!_containsPath(path))
		{
			_addPagesForPath(path);
		}
	}

}
