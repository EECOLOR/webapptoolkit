[#include "/message.ftl" /]
[#include "/navigation.ftl" /]
[#include "/shared/crud.ftl" /]

[@navigation siteMap=siteMap /]

[@message /]

[#function employeePropertyLabel employee property]
	[#if property == "role"]
		[#local role = employee.role!{"id":"null", "name":"null"} /]
		[#return '<a href="/roles?id=${role.id}">${role.name}</a>' /]
	[/#if]
	[#return employee[property] /]
[/#function]

[@crud 
	elementName="employee"
	elementListName="employees"
	newLabel="Nieuwe medewerker"
	editLabel="Medewerker bewerken"
	tableLabelMethod="employeePropertyLabel" /]
