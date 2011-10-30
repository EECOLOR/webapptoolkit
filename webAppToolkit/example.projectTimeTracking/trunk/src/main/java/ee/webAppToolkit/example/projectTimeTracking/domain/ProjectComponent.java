package ee.webAppToolkit.example.projectTimeTracking.domain;

import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.website.domain.DisplayAwareIdEntity;

public class ProjectComponent extends DisplayAwareIdEntity {

	@Display(type=Type.TEXT, order=0, label=@LocalizedString("projectComponent.description"))
	public String description;
	
	@Display(type=Type.LIST, order=1, label=@LocalizedString("projectComponent.type"))
	@ee.webAppToolkit.forms.List(defaultLabel=@LocalizedString("projectComponent.selectType"))
	public ProjectComponentType type;
	
	@Display(type=Type.TEXT, order=2, label=@LocalizedString("projectComponent.estimatedHours"))
	public float estimatedHours;
	
	@Display(type=Type.TEXT, order=3, label=@LocalizedString("projectComponent.billedHours"))
	public float billedHours;
	
	@Display(type=Type.TEXT, order=4, label=@LocalizedString("projectComponent.billed"))
	public float billed;
}
