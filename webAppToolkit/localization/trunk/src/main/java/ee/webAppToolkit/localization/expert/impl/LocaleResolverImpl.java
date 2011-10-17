package ee.webAppToolkit.localization.expert.impl;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;

import ee.webAppToolkit.localization.LocaleResolver;

public class LocaleResolverImpl implements LocaleResolver
{

	@Override
	public List<Locale> getLocaleChain()
	{
		return Lists.newArrayList(Locale.ENGLISH);
	}
	
}
