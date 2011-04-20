package ee.webAppToolkit.parameters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface TypeKindResolver
{
	public TypeKind resolve(Class<?> to);
	public TypeKind resolve(Type type);
	public TypeKind resolve(ParameterizedType type);
}