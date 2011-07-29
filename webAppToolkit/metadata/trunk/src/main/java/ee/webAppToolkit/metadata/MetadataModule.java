package ee.webAppToolkit.metadata;

import com.google.inject.AbstractModule;

import ee.metadataUtils.guice.MetadataUtilsModule;

public class MetadataModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new MetadataUtilsModule());
	}

}
