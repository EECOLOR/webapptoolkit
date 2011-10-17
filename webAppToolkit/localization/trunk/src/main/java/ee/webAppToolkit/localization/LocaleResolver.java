package ee.webAppToolkit.localization;

import java.util.List;
import java.util.Locale;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.localization.expert.impl.LocaleResolverImpl;

@ImplementedBy(LocaleResolverImpl.class)
public interface LocaleResolver
{
	public List<Locale> getLocaleChain();
}
