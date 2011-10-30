package ee.webAppToolkit.example.projectTimeTracking.domain;

import java.util.List;

import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.forms.ComponentList;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.forms.Text;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.website.domain.DisplayAwareIdEntity;

public class Project extends DisplayAwareIdEntity {
	
	@Display(type=Type.TEXT, order=0, label=@LocalizedString("project.projectNumber"))
	@Text(readonly=true)
	public ProjectNumber projectNumber;
	
	@Display(type=Type.TEXT, order=1, label=@LocalizedString("project.name"))
	public String name;
	
	@Display(type=Type.LIST, order=2, label=@LocalizedString("project.customer"))
	@ee.webAppToolkit.forms.List(defaultLabel=@LocalizedString("project.selectCustomer"))
	public Customer customer;
	
	@Optional
	@Display(type=Type.CHECKBOX, order=3, label=@LocalizedString("project.internal"))
	public boolean internal;
	
	@Optional
	@Display(type=Type.CHECKBOX, order=4, label=@LocalizedString("project.service"))
	public boolean service;
	
	@Display(type=Type.LIST, order=5, label=@LocalizedString("project.status"))
	public ProjectStatus status;
	
	@Display(type=Type.LIST, order=6, label=@LocalizedString("project.accountManager"))
	@ee.webAppToolkit.forms.List(defaultLabel=@LocalizedString("project.selectAccountManager"))
	public Employee accountManager;
	
	@Display(type=Type.LIST, order=7, label=@LocalizedString("project.projectManager"))
	@ee.webAppToolkit.forms.List(defaultLabel=@LocalizedString("project.selectProjectManager"))
	public Employee projectManager;
	
	@Optional
	@Display(type=Type.COMPONENT_LIST, order=8, label=@LocalizedString("project.components"))
	@ComponentList(createRemoveLink=true, removeLinkLabel=@LocalizedString("project.componentRemove"))
	public List<ProjectComponent> components;
	
	@Optional
	@Display(type=Type.COMPONENT_LIST, order=9, label=@LocalizedString("project.purchasesAndSales"))
	@ComponentList(createRemoveLink=true, removeLinkLabel=@LocalizedString("project.purchaseAndSaleRemove"))
	public List<ProjectPurchaseAndSale> purchasesAndSales;
}
