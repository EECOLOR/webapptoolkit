package ee.webAppToolkit.website.domain;

import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.storage.IdEntity;

public class DisplayAwareIdEntity extends IdEntity {
	
	@Override
	@Display(type=Type.HIDDEN)
	@Optional
	public Long getId() {
		return super.getId();
	}
}
