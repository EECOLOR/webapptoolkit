[#include "/message.ftl" /]
[#include "/navigation.ftl" /]
[#include "/shared/crud.ftl" /]

[@navigation siteMap=siteMap /]

[@message /]

[#function customerPropertyLabel customer property]
	[#if property == "accountManager"]
		[#local employee = customer.accountManager!{"id":"null", "name":"null"} /]
		[#return '<a href="/administration/employees?id=${employee.id}">${employee.name}</a>' /]
	[/#if]
	[#return customer[property] /]
[/#function]

[@crud 
	elementName="customer"
	elementListName="customers"
	newLabel="Nieuwe opdrachtgever"
	editLabel="Opdrachtgever bewerken"
	tableLabelMethod="customerPropertyLabel" /]
