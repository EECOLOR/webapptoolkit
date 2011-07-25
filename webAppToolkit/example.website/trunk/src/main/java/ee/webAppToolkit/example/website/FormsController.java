package ee.webAppToolkit.example.website;

import java.util.ArrayList;
import java.util.Arrays;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.annotations.Get;
import ee.webAppToolkit.core.annotations.Post;
import ee.webAppToolkit.example.website.forms.TestObject;
import ee.webAppToolkit.example.website.forms.TestSubObject;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.render.RenderingController;

public class FormsController extends RenderingController {
	
	@Get
	public Result index(@Flash TestObject testObject)
	{
		if (testObject == null)
		{
			testObject = new TestObject();
			testObject.testSubObject = new TestSubObject();
			testObject.testSubObjects = new ArrayList<TestSubObject>(Arrays.asList(new TestSubObject[] {new TestSubObject(), new TestSubObject()}));
		}
		
		return render(new IndexModel(testObject));
	}
	
	@Post
	public Result index(@Parameter TestObject testObject, ValidationResults validationResults)
	{
		String message = validationResults.getValidated() ? "Test object saved" : "Test object not saved";
		
		return render(new IndexModel(testObject, message));
	}
	
	public class IndexModel
	{
		public TestObject testObject;
		public String message;

		IndexModel(TestObject testObject)
		{
			this(testObject, null);
		}
		
		IndexModel(TestObject testObject, String message)
		{
			this.testObject = testObject;
			this.message = message;
		}
	}
}
