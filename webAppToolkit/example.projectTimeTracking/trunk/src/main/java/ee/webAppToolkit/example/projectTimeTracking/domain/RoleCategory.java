package ee.webAppToolkit.example.projectTimeTracking.domain;

import ee.webAppToolkit.localization.LocalizedString;

public enum RoleCategory {
	@LocalizedString("enum.roleCategory.operations")
	OPERATIONS,
	@LocalizedString("enum.roleCategory.inventors")
	INVENTORS, 
	@LocalizedString("enum.roleCategory.creators")
	CREATORS,
	@LocalizedString("enum.roleCategory.guardians")
	GUARDIANS 
}
