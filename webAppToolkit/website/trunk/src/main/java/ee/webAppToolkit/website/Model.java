package ee.webAppToolkit.website;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.annotations.Nullable;
import ee.webAppToolkit.core.annotations.Path;
import ee.webAppToolkit.navigation.SiteMap;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.render.SimpleModel;

public class Model extends SimpleModel {
	
	private ValidationResults _validationResults;
	private SiteMap _siteMap;
	
	@Inject
	public Model(@Context String context,
			@Path String path,
			ValidationResults validationResults,
			SiteMap siteMap,
			@Nullable @Assisted Object model)
	{
		super(context, path, model);
		
		_validationResults = validationResults;
		_siteMap = siteMap;
	}

	public ValidationResults getValidationResults() {
		return _validationResults;
	}
	
	public SiteMap getSiteMap()
	{
		return _siteMap;
	}
}
