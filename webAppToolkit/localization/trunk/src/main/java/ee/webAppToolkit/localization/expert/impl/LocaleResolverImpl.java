package ee.webAppToolkit.localization.expert.impl;

import java.util.Locale;

import ee.webAppToolkit.localization.LocaleResolver;

public class LocaleResolverImpl implements LocaleResolver
{

	@Override
	public Locale getLocale()
	{
		return Locale.ENGLISH;
	}
	
}
