package ee.webAppToolkit.localization.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import ee.webAppToolkit.localization.LocalizedString;

@SuppressWarnings("all")
public final class LocalizedStringImp implements LocalizedString, Serializable
{
	private static final long serialVersionUID = 0;	
	
	private final String value;

	public LocalizedStringImp(String value)
	{
		this.value = value;
	}

	public String value()
	{
		return this.value;
	}

	@Override
	public int hashCode()
	{
		// This is specified in java.lang.Annotation.
		return (127 * "value".hashCode()) ^ value.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof LocalizedString))
		{
			return false;
		}

		LocalizedString other = (LocalizedString) o;
		return value.equals(other.value());
	}

	@Override
	public String toString()
	{
		return "@" + LocalizedString.class.getName() + "(\"" + value + "\")";
	}

	@Override
	public Class<? extends Annotation> annotationType()
	{
		return LocalizedString.class;
	}
}
