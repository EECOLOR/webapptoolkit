package ee.webAppToolkit.example.projectTimeTracking.domain;

import ee.webAppToolkit.localization.LocalizedString;

public enum ProjectStatus {

	@LocalizedString("enum.projectStatus.proposed")
	PROPOSED,
	
	@LocalizedString("enum.projectStatus.active")
	ACTIVE,
	
	@LocalizedString("enum.projectStatus.canceled")
	CANCELED,
	
	@LocalizedString("enum.projectStatus.completed")
	COMPLETED
}
