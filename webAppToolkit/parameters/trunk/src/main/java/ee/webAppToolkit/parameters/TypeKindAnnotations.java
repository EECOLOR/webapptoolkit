package ee.webAppToolkit.parameters;


public class TypeKindAnnotations {
	static public TypeKindAnnotation create(TypeKind typeKind)
	{
		return new TypeKindAnnotationImpl(typeKind);
	}
}
