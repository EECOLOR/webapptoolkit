package ee.webAppToolkit.rendering.freemarker.utils;

import java.lang.annotation.Annotation;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

import ee.metadataUtils.guice.MetadataUtilsModule;
import ee.webAppToolkit.navigation.SiteMap;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.rendering.freemarker.expert.impl.GuiceObjectWrapper;
import ee.webAppToolkit.rendering.freemarker.utils.expert.CustomObjectTemplateModel;
import ee.webAppToolkit.rendering.freemarker.utils.expert.CustomObjectTemplateModelFactory;
import ee.webAppToolkit.rendering.freemarker.utils.expert.FreemarkerPropertyMetadata;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.AnnotationModelFactory;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.ClassModelFactory;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.FreemarkerPropertyMetadataImpl;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.MetadataObjectWrapper;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.SiteMapModelFactory;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.UtilsTemplateModel;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.ValidationResultsTemplateModelFactory;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.util.ModelFactory;

public class FreemarkerUtilsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GuiceObjectWrapper.class).to(MetadataObjectWrapper.class);

		install(new FactoryModuleBuilder().implement(CustomObjectTemplateModel.class,
				UtilsTemplateModel.class).build(CustomObjectTemplateModelFactory.class));
		
		MapBinder<Class<?>, ModelFactory> modelFactories = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<ModelFactory>(){});
		modelFactories.addBinding(Annotation.class).to(AnnotationModelFactory.class);
		modelFactories.addBinding(Class.class).to(ClassModelFactory.class);
		modelFactories.addBinding(SiteMap.class).to(SiteMapModelFactory.class);		
		modelFactories.addBinding(ValidationResults.class).to(ValidationResultsTemplateModelFactory.class);
		
		MetadataUtilsModule.bindAdapter(binder(), FreemarkerPropertyMetadata.class, FreemarkerPropertyMetadataImpl.class);
		
		Multibinder<TemplateLoader> listeners = Multibinder.newSetBinder(binder(), TemplateLoader.class);
		listeners.addBinding().toInstance(new ClassTemplateLoader(getClass(), "templates"));
	}
}
