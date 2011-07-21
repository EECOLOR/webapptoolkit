package ee.webAppToolkit.localization;

import java.util.Locale;

import com.google.inject.AbstractModule;

public class LocalizationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LocaleResolver.class).toInstance(new LocaleResolver()
		{
			@Override
			public Locale getLocale()
			{
				return Locale.ENGLISH;
			}
		});
	}
}
