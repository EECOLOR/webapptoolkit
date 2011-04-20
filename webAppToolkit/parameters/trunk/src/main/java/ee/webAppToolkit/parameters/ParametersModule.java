package ee.webAppToolkit.parameters;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ParametersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GenericConverter.class).annotatedWith(Names.named(TypeKind.Complex.toString()));
	}

}
