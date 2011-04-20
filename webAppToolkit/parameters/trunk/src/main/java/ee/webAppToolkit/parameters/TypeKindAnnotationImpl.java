package ee.webAppToolkit.parameters;

import java.io.Serializable;
import java.lang.annotation.Annotation;

@SuppressWarnings("all")
final class TypeKindAnnotationImpl implements TypeKindAnnotation, Serializable
{
	private static final long serialVersionUID = 0;	
	
	private final TypeKind value;

	public TypeKindAnnotationImpl(TypeKind value)
	{
		this.value = value;
	}

	@Override
	public TypeKind value()
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
		if (!(o instanceof TypeKindAnnotation))
		{
			return false;
		}

		TypeKindAnnotation other = (TypeKindAnnotation) o;
		return value.equals(other.value());
	}

	@Override
	public String toString()
	{
		return "@" + TypeKindAnnotation.class.getName() + "(\"" + value + "\")";
	}

	@Override
	public Class<? extends Annotation> annotationType()
	{
		return TypeKindAnnotation.class;
	}
}
