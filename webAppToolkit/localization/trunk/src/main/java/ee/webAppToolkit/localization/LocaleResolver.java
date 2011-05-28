package ee.webAppToolkit.localization;

import java.util.Locale;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.localization.impl.LocaleResolverImpl;

@ImplementedBy(LocaleResolverImpl.class)
public interface LocaleResolver
{
	public Locale getLocale();
}
