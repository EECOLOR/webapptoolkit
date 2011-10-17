package ee.webAppToolkit.example.projectTimeTracking;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;
import com.wideplay.warp.persist.db4o.Db4Objects;

import ee.webAppToolkit.localization.LocaleResolver;
import ee.webAppToolkit.storage.db4o.Db4oModule;
import ee.webAppToolkit.website.WebsiteModule;

public class ApplicationModule extends WebsiteModule {

	static private final Locale DUTCH = new Locale("nl");
	
	@Override
	protected void configureApplication() {
		bind(LocaleResolver.class).toInstance(new LocaleResolver() {
			@Override
			public List<Locale> getLocaleChain() {
				return Lists.newArrayList(DUTCH, Locale.ENGLISH);
			}
		});
		
		bindPropertiesToLocale("forms", DUTCH);
		bindPropertiesToLocale("enums", DUTCH);
		bindPropertiesToLocale("messages", DUTCH);
		bindPropertiesToLocale("validation", DUTCH);
		bindPropertiesToLocale("navigation", DUTCH);
		
		install(new Db4oModule());
		
		bindConstant().annotatedWith(Db4Objects.class).to("projectTimeTracking.dat");
	}
	
	@Override
	protected Class<?> getRootController() {
		return MainController.class;
	}
	
}
