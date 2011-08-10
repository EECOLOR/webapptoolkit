package ee.webAppToolkit.example.website.forms;

import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.localization.LocalizedString;

public class TestSubObject {

	@Display(type=Type.HIDDEN)
	@Optional
	public Long id;
	
	@Display(label=@LocalizedString("forms.testSubObject.title"))
	public String title;
	
	@Display(label=@LocalizedString("forms.testSubObject.description"), type=Type.TEXTAREA)
	public String description;
}
