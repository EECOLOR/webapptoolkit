package ee.webAppToolkit.amf;

import com.google.inject.AbstractModule;

import ee.webAppToolkit.amf.annotations.Amf;
import ee.webAppToolkit.amf.expert.impl.AmfActionArgumentResolver;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;

public class AmfModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ActionArgumentResolver.class).annotatedWith(Amf.class).to(
				AmfActionArgumentResolver.class);
	}

}
