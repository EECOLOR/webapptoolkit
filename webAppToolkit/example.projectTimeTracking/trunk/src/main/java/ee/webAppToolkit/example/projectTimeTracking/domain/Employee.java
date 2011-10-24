package ee.webAppToolkit.example.projectTimeTracking.domain;

import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.forms.List;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.website.domain.DisplayAwareIdEntity;

public class Employee extends DisplayAwareIdEntity {
	
	@Display(type=Type.TEXT, order=0, label=@LocalizedString("employee.name"))
	public String name;
	
	@Display(type=Type.LIST, order=1, label=@LocalizedString("employee.role"))
	@List(defaultLabel=@LocalizedString("employee.selectRole"))
	public Role role;
}
