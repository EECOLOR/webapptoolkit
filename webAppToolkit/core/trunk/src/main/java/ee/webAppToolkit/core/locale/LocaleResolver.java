package ee.webAppToolkit.core.locale;

import java.util.Locale;

import com.google.inject.ImplementedBy;

@ImplementedBy(LocaleResolverImpl.class)
public interface LocaleResolver
{
	public Locale getLocale();
}
