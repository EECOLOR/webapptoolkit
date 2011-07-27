package ee.webAppToolkit.freemarker.navigation.expert.impl;

import ee.webAppToolkit.navigation.SiteMap;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class SiteMapModel extends SimpleHash {
	
	private static final long serialVersionUID = 1L;
	private SiteMap _siteMap;

	public SiteMapModel(SiteMap siteMap, ObjectWrapper objectWrapper)
	{
		super(siteMap, objectWrapper);
		
		_siteMap = siteMap;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		
		if (key == null)
		{
			return super.get(key);
		}
		if (key.equals("displayName"))
		{
			return wrap(_siteMap.getDisplayName());
		} else
		{
			return super.get(key);
		}
	}
	
	
}
