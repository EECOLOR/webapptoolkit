package ee.webAppToolkit.example.projectTimeTracking.providers;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import com.google.inject.Provider;

import ee.webAppToolkit.localization.LocaleResolver;

public class CalendarProvider implements Provider<Calendar> {

	private LocaleResolver _localeResolver;
	
	@Inject
	public CalendarProvider(LocaleResolver localeResolver) {
		_localeResolver = localeResolver;
	}
	
	@Override
	public Calendar get() {
		Locale locale = _localeResolver.getLocaleChain().get(0);
		
		return Calendar.getInstance(locale);
	}
	
}
