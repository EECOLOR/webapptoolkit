package ee.webAppToolkit.navigation;

import java.util.Map;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.navigation.expert.impl.SiteMapImpl;

@ImplementedBy(SiteMapImpl.class)
public interface SiteMap extends Map<String, SiteMap> {

}
