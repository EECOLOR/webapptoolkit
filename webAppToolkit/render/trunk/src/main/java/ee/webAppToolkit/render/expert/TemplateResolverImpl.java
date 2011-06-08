package ee.webAppToolkit.render.expert;
public class TemplateResolverImpl implements TemplateResolver
{

	@Override
	public String resolveTemplate(Object controllerObject, String template)
	{
		String simpleName = controllerObject.getClass().getSimpleName();
		String className = simpleName.substring(0, simpleName.indexOf("Controller"));
		String controllerName = className.substring(0, 1).toLowerCase() + className.substring(1);
		return controllerName + "/" + template;
	}
}