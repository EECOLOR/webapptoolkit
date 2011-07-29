package ee.webAppToolkit.freemarker.metadata;

import java.lang.annotation.Annotation;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

import ee.metadataUtils.guice.MetadataUtilsModule;
import ee.webAppToolkit.freemarker.metadata.expert.CustomObjectTemplateModel;
import ee.webAppToolkit.freemarker.metadata.expert.CustomObjectTemplateModelFactory;
import ee.webAppToolkit.freemarker.metadata.expert.FreemarkerPropertyMetadata;
import ee.webAppToolkit.freemarker.metadata.expert.impl.AnnotationModelFactory;
import ee.webAppToolkit.freemarker.metadata.expert.impl.ClassModelFactory;
import ee.webAppToolkit.freemarker.metadata.expert.impl.FreemarkerPropertyMetadataImpl;
import ee.webAppToolkit.freemarker.metadata.expert.impl.MetadataObjectWrapper;
import ee.webAppToolkit.freemarker.metadata.expert.impl.MetadataTemplateModel;
import ee.webAppToolkit.rendering.freemarker.expert.impl.GuiceObjectWrapper;
import freemarker.ext.util.ModelFactory;

public class FreemarkerMetadataModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GuiceObjectWrapper.class).to(MetadataObjectWrapper.class);

		install(new FactoryModuleBuilder().implement(CustomObjectTemplateModel.class,
				MetadataTemplateModel.class).build(CustomObjectTemplateModelFactory.class));
		
		MapBinder<Class<?>, ModelFactory> modelFactories = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<ModelFactory>(){});
		modelFactories.addBinding(Annotation.class).to(AnnotationModelFactory.class);
		modelFactories.addBinding(Class.class).to(ClassModelFactory.class);
		
		MetadataUtilsModule.bindAdapter(binder(), FreemarkerPropertyMetadata.class, FreemarkerPropertyMetadataImpl.class);
	}
}
