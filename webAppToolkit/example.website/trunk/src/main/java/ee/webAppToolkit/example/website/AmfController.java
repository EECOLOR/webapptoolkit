package ee.webAppToolkit.example.website;

import ee.webAppToolkit.amf.AmfResult;
import ee.webAppToolkit.amf.annotations.Amf;
import ee.webAppToolkit.core.Result;

public class AmfController {

	public Result echo(@Amf Object obj)
	{
		return new AmfResult(obj);
	}
}
