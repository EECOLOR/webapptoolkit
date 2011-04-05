package ee.webAppToolkit.core;

public interface WrappingController {
	
	public void beforeHandling(String memberName);
	public Result wrapResult(Result result, String memberName, Object controller);
}
