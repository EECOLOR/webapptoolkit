package ee.webAppToolkit.example.website.forms;

import java.util.Date;
import java.util.List;

import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.localization.LocalizedString;

public class TestObject {
	
	@Display(type=Type.HIDDEN)
	public long id;
	
	@Display(label=@LocalizedString("forms.testObject.title"), type=Type.TEXT)
	public String title;
	
	@Display(label=@LocalizedString("forms.testObject.dateCreated"), type=Type.DATE)
	public Date dateCreated;
	
	@Display(label=@LocalizedString("forms.testObject.testEnumeration"), type=Type.LIST)
	@ee.webAppToolkit.forms.List(defaultLabel=@LocalizedString("forms.selectValue"))
	@Optional
	public TestEnumeration testEnumeration;
	
	@Display(label=@LocalizedString("forms.testObject.testSubObject"), type=Type.COMPONENT)
	public TestSubObject testSubObject;
	
	@Display(label=@LocalizedString("forms.testObject.testSubObject"), type=Type.COMPONENT_LIST)
	public List<TestSubObject> testSubObjects;
}
