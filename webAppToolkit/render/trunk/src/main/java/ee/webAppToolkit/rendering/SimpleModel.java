package ee.webAppToolkit.rendering;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.annotations.Nullable;
import ee.webAppToolkit.core.annotations.Path;

public class SimpleModel
{
	private Object _model ;
	private String _context;
	private String _path;
	
	@Inject
	public SimpleModel(@Context String context,
			@Path String path, 
			@Nullable @Assisted Object model)
	{
		_context = context;
		_path = path;
		_model = model;
	}

	public Object getModel()
	{
		return _model;
	}
	
	public String getContext()
	{
		return _context;
	}
	
	public String getPath()
	{
		return _path;
	}
}
