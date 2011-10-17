package ee.webAppToolkit.example.projectTimeTracking.domain;

import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.forms.List;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.website.domain.DisplayAwareIdEntity;

public class Role extends DisplayAwareIdEntity {
	
	@Display(type=Type.TEXT, order=0, label=@LocalizedString("role.name"))
	public String name;
	
	@Display(type=Type.LIST, order=1, label=@LocalizedString("role.category"))
	@List(defaultLabel=@LocalizedString("role.selectCategory"))
	public RoleCategory category;
	
	@Display(type=Type.TEXT, order=2, label=@LocalizedString("role.rate"))
	public int rate;
	
	@Display(type=Type.TEXT, order=2, label=@LocalizedString("role.rateCommercial"))
	public int rateCommercial;
}
