package ee.webAppToolkit.parameters;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.servlet.ServletScopes;

import ee.parameterConverter.EmptyValueException;
import ee.parameterConverter.PropertyMetadata;
import ee.parameterConverter.PropertyMetadataFactory;
import ee.parameterConverter.ValidationResultCollector;
import ee.parameterConverter.guice.ParameterConverterModule;
import ee.webAppToolkit.parameters.impl.ExtendedPropertyMetadataImpl;
import ee.webAppToolkit.parameters.impl.ValidationResultCollectorImpl;

public class ParametersModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ParameterConverterModule());

		bind(ValidationResultCollector.class).to(ValidationResultCollectorImpl.class)
				.asEagerSingleton();
		bind(ValidationResults.class).in(ServletScopes.REQUEST);

		bind(new TypeLiteral<ExceptionConverter<EmptyValueException>>() {})
			.to(EmptyValueExceptionConverter.class).asEagerSingleton();
		
		install(new FactoryModuleBuilder()
	     .implement(PropertyMetadata.class, ExtendedPropertyMetadataImpl.class)
	     .build(PropertyMetadataFactory.class));
	}

}
