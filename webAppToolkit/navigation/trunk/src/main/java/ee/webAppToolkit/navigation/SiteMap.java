package ee.webAppToolkit.navigation;

import java.util.Map;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.expert.impl.SiteMapImpl;

@ImplementedBy(SiteMapImpl.class)
public interface SiteMap extends Map<String, SiteMap> {
	public String getDisplayName();
	public void setDisplayName(String displayName);
	public void setDisplayName(LocalizedString localizedString);
}
