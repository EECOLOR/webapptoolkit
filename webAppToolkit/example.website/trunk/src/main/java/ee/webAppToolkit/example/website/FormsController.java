package ee.webAppToolkit.example.website;

import java.util.List;

import com.google.inject.name.Named;

import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.annotations.Get;
import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.core.annotations.Post;
import ee.webAppToolkit.example.website.forms.TestObject;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.rendering.RenderingController;

public class FormsController extends RenderingController {

	@Get
	public Result index(
			@Named("store") List<TestObject> store,
			@Optional @Parameter("id") Integer id,
			@Flash TestObject testObject) 
	{
		if (testObject == null && id != null)
		{
			testObject = store.get(id);
		}
		
		return render(new IndexModel(store, testObject));
	}

	@Post
	public Result index(
			@Named("store") List<TestObject> store,
			@Parameter("testObject") TestObject testObject,
			ValidationResults validationResults) {
		
		boolean validated = validationResults.getValidated();

		if (validated)
		{
			if (testObject.id == null)
			{
				store.add(testObject);
				testObject.id = store.size() - 1;
			} else
			{
				store.set(testObject.id, testObject);
			}
		}
		
		String message = validated ? "Test object saved"
				: "Test object not saved";
		
		return render(new IndexModel(store, testObject, message));
	}

	@Get
	@HideFromNavigation
	public void remove(@Named("store") List<TestObject> store, @Parameter("id") int id)
	{
		flash.put(store.remove(id));
		redirect("");
	}
	
	public class IndexModel {
		@Display
		public TestObject testObject;
		public String message;
		public List<TestObject> store;

		IndexModel(List<TestObject> store, TestObject testObject) {
			this(store, testObject, null);
		}

		IndexModel(List<TestObject> store, TestObject testObject, String message) {
			this.store = store;
			this.testObject = testObject;
			this.message = message;
		}
	}
}
