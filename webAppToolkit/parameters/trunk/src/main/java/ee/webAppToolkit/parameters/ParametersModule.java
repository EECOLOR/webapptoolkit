package ee.webAppToolkit.parameters;

import java.util.Locale;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.util.Modules;

import ee.metadataUtils.PropertyMetadataFactory;
import ee.parameterConverter.EmptyValueException;
import ee.parameterConverter.ParameterPropertyMetadata;
import ee.parameterConverter.ValidationResultCollector;
import ee.parameterConverter.guice.ParameterConverterModule;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;
import ee.webAppToolkit.localization.LocalizedStrings;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.parameters.exceptions.CustomEmptyValueException;
import ee.webAppToolkit.parameters.expert.impl.CustomEmptyValueExceptionConverter;
import ee.webAppToolkit.parameters.expert.impl.EmptyValueExceptionConverter;
import ee.webAppToolkit.parameters.expert.impl.ExtendedPropertyMetadataImpl;
import ee.webAppToolkit.parameters.expert.impl.ParameterActionArgumentResolver;
import ee.webAppToolkit.parameters.expert.impl.ValidationResultCollectorImpl;

public class ParametersModule extends AbstractModule {

	@Override
	protected void configure() {

		install(Modules.override(new ParameterConverterModule()).with(new AbstractModule() {
			@Override
			protected void configure() {
				bind(ValidationResultCollector.class).to(ValidationResultCollectorImpl.class)
						.asEagerSingleton();
				
				install(new FactoryModuleBuilder().implement(ParameterPropertyMetadata.class,
						ExtendedPropertyMetadataImpl.class).build(PropertyMetadataFactory.class));
			}
		}));

		bind(ValidationResults.class).in(ServletScopes.REQUEST);

		LocalizedStrings.bindPropertiesToLocale(binder(), getClass().getClassLoader(),
				"parameters.validationMessages", Locale.ENGLISH);

		bind(new TypeLiteral<ExceptionConverter<EmptyValueException>>() {
		}).to(EmptyValueExceptionConverter.class).asEagerSingleton();
		bind(new TypeLiteral<ExceptionConverter<CustomEmptyValueException>>() {
		}).to(CustomEmptyValueExceptionConverter.class).asEagerSingleton();
		
		bind(ActionArgumentResolver.class).annotatedWith(Parameter.class).to(
				ParameterActionArgumentResolver.class);
	}

}
