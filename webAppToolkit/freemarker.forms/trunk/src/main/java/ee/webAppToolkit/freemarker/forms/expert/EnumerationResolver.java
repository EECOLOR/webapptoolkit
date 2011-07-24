package ee.webAppToolkit.freemarker.forms.expert;

import java.util.List;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.freemarker.forms.expert.impl.EnumerationResolverImpl;

@ImplementedBy(EnumerationResolverImpl.class)
public interface EnumerationResolver
{
	public <T> List<T> resolve(Class<T> type);
}
