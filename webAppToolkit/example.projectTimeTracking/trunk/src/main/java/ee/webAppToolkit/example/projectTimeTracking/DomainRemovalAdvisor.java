package ee.webAppToolkit.example.projectTimeTracking;

import ee.webAppToolkit.example.projectTimeTracking.domain.Role;

public class DomainRemovalAdvisor {
	public boolean canRemove(Role role) {
		return false;
	}
}
