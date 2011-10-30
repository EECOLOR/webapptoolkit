package ee.webAppToolkit.example.projectTimeTracking;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;
import com.google.inject.TypeLiteral;
import com.wideplay.warp.persist.db4o.Db4Objects;

import ee.parameterConverter.Converter;
import ee.webAppToolkit.example.projectTimeTracking.administration.EmployeeContext;
import ee.webAppToolkit.example.projectTimeTracking.domain.Customer;
import ee.webAppToolkit.example.projectTimeTracking.domain.Employee;
import ee.webAppToolkit.example.projectTimeTracking.domain.ProjectNumber;
import ee.webAppToolkit.example.projectTimeTracking.domain.Role;
import ee.webAppToolkit.example.projectTimeTracking.providers.CalendarProvider;
import ee.webAppToolkit.example.projectTimeTracking.providers.CustomerEnumerationService;
import ee.webAppToolkit.example.projectTimeTracking.providers.EmployeeEnumerationService;
import ee.webAppToolkit.example.projectTimeTracking.providers.ProjectNumberConverter;
import ee.webAppToolkit.example.projectTimeTracking.providers.RoleEnumerationService;
import ee.webAppToolkit.localization.LocaleResolver;
import ee.webAppToolkit.rendering.freemarker.utils.EnumerationProvider;
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
		
		bind(new TypeLiteral<EnumerationProvider<Role>>(){}).to(RoleEnumerationService.class);
		bind(new TypeLiteral<Converter<String, Role>>(){}).to(RoleEnumerationService.class);
		bind(new TypeLiteral<EnumerationProvider<Employee>>(){}).to(EmployeeEnumerationService.class);
		bind(new TypeLiteral<Converter<String, Employee>>(){}).to(EmployeeEnumerationService.class);
		bind(new TypeLiteral<EnumerationProvider<Customer>>(){}).to(CustomerEnumerationService.class);
		bind(new TypeLiteral<Converter<String, Customer>>(){}).to(CustomerEnumerationService.class);
		bind(new TypeLiteral<Converter<String, ProjectNumber>>(){}).to(ProjectNumberConverter.class);
		
		bind(Calendar.class).toProvider(CalendarProvider.class);
		
		bindThreadLocalProvider(EmployeeContext.class);
		
		
	}
	
	@Override
	protected Class<?> getRootController() {
		return MainController.class;
	}
	
}
