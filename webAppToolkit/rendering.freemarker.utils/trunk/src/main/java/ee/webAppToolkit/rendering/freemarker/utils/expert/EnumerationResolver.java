package ee.webAppToolkit.rendering.freemarker.utils.expert;

import java.util.List;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.rendering.freemarker.utils.Enumeration;
import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.EnumerationResolverImpl;

@ImplementedBy(EnumerationResolverImpl.class)
public interface EnumerationResolver
{
	public <T> List<Enumeration<T>> getEnumeration(Class<T> type);
	public <T> Enumeration<T> getEnumeration(T instance);
}
