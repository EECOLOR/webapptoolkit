package ee.webAppToolkit.example.projectTimeTracking.domain;

import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.website.domain.DisplayAwareIdEntity;

public class ProjectPurchaseAndSale extends DisplayAwareIdEntity {
	
	@Display(type=Type.TEXT, order=0, label=@LocalizedString("projectPurchaseAndSale.description"))
	public String description;
	
	@Display(type=Type.TEXT, order=1, label=@LocalizedString("projectPurchaseAndSale.purchaseAmount"))
	public int purchaseAmount;
	
	@Display(type=Type.TEXT, order=2, label=@LocalizedString("projectPurchaseAndSale.saleAmount"))
	public int saleAmount;
}
