package ee.webAppToolkit.rendering.freemarker.utils.expert;

import java.util.List;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.EnumerationResolverImpl;

@ImplementedBy(EnumerationResolverImpl.class)
public interface EnumerationResolver
{
	public <T> List<T> resolve(Class<T> type);
}
