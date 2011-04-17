package ee.webAppToolkit.core.expert.impl;

import java.util.HashMap;

import javax.inject.Singleton;

import ee.webAppToolkit.core.SiteMap;
import ee.webAppToolkit.core.expert.ControllerDescription;

@Singleton
public class SiteMapImpl extends HashMap<String, SiteMap> implements SiteMap {
	private static final long serialVersionUID = 1L;

	private String[] _getPathSegments(String path) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		return path.split("/");
	}

	@Override
	public void addPagesForPath(String path) {

		String[] pathSegments = _getPathSegments(path);
		SiteMap current = this;
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

	@Override
	public boolean containsPath(String path) {

		String[] pathSegments = _getPathSegments(path);

		SiteMap current = this;

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
	public String toString() {
		return toString("");
	}

	@Override
	public String toString(String basePath) {
		if (isEmpty()) return basePath + "\n";
		
		String s = containsKey(ControllerDescription.INDEX) ? basePath + "/\n" : "";

		for (Entry<String, SiteMap> entry : entrySet()) {
			s += entry.getValue().toString(basePath + "/" + entry.getKey());
		}

		return s;
	}
}
