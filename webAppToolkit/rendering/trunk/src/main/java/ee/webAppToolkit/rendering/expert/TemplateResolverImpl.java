package ee.webAppToolkit.rendering.expert;
public class TemplateResolverImpl implements TemplateResolver
{

	@Override
	public String resolveTemplate(Object controllerObject, String template)
	{
		String simpleName = controllerObject.getClass().getSimpleName();
		int index = simpleName.indexOf("Controller");
		
		String className = simpleName;
		if (index > -1)
		{
			className = simpleName.substring(0, index);
		}
		String controllerName = className.substring(0, 1).toLowerCase() + className.substring(1);
		return controllerName + "/" + template;
	}
}