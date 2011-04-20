package ee.webAppToolkit.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.inject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface TypeKindAnnotation
{
	TypeKind value();
}
