package ee.webAppToolkit.freemarker.forms;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

import ee.metadataUtils.PropertyMetadataAdapterFactory;
import ee.webAppToolkit.freemarker.forms.expert.FreemarkerPropertyMetadataFactory;
import ee.webAppToolkit.freemarker.forms.expert.impl.DisplayOnlyFreemarkerPropertyMetadataAdapterFactoryImpl;
import ee.webAppToolkit.freemarker.forms.expert.impl.EnumerationTemplateModel;
import ee.webAppToolkit.freemarker.forms.expert.impl.ValidationResultsTemplateModelFactory;
import ee.webAppToolkit.freemarker.metadata.expert.CustomObjectTemplateModel;
import ee.webAppToolkit.freemarker.metadata.expert.CustomObjectTemplateModelFactory;
import ee.webAppToolkit.freemarker.metadata.expert.FreemarkerPropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.impl.FreemarkerPropertyMetadataImpl;
import ee.webAppToolkit.parameters.ValidationResults;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.util.ModelFactory;

public class FreemarkerFormsModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(CustomObjectTemplateModel.class,
				EnumerationTemplateModel.class).build(CustomObjectTemplateModelFactory.class));
		
		Multibinder<TemplateLoader> listeners = Multibinder.newSetBinder(binder(), TemplateLoader.class);
		listeners.addBinding().toInstance(new ClassTemplateLoader(getClass(), "templates"));
		
		//Override the default factory
		install(new FactoryModuleBuilder().implement(FreemarkerPropertyMetadata.class, FreemarkerPropertyMetadataImpl.class).build(FreemarkerPropertyMetadataFactory.class));
		bind(new TypeLiteral<PropertyMetadataAdapterFactory<FreemarkerPropertyMetadata>>(){}).to(DisplayOnlyFreemarkerPropertyMetadataAdapterFactoryImpl.class);
		
		MapBinder<Class<?>, ModelFactory> modelFactories = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<ModelFactory>(){});
		modelFactories.addBinding(ValidationResults.class).to(ValidationResultsTemplateModelFactory.class);
	}

}
