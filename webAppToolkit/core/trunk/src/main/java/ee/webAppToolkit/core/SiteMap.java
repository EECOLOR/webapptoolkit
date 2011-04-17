package ee.webAppToolkit.core;

import java.util.Map;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.core.expert.impl.SiteMapImpl;

@ImplementedBy(SiteMapImpl.class)
public interface SiteMap extends Map<String, SiteMap> {

	/**
	 * Adds pages for each element in the given path.
	 * 
	 * @param path A path separated by /
	 */
	public void addPagesForPath(String path);
	
	public boolean containsPath(String path);
	
	public String toString(String basePath);
}
