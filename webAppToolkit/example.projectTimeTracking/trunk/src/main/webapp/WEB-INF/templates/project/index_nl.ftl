[#include "/message.ftl" /]
[#include "/navigation.ftl" /]
[#include "/shared/crud.ftl" /]

[@navigation siteMap=siteMap /]

[@message /]

[#function projectPropertyLabel project property]
	[#if property == "customer"]
		[#local customer = project.customer!{"id":"null", "name":"null"} /]
		[#return '<a href="/customers?id=${customer.id}">${customer.name}</a>' /]
	[/#if]
	[#if property == "accountManager" || property == "projectManager"]
		[#local employee = project[property]!{"id":"null", "name":"null"} /]
		[#return '<a href="/employees?id=${employee.id}">${employee.name}</a>' /]
	[/#if]
	[#return project[property] /]
[/#function]

[#if model.project.id??]
[/#if]
	<a href="${context}?followUpProjectNumber=${model.project.projectNumber}">Vervolg project</a>

[@crud 
	elementName="project"
	elementListName="projects"
	newLabel="Nieuw project"
	editLabel="Project bewerken"
	tableLabelMethod="projectPropertyLabel"
	tableIgnoredProperties=["components", "purchasesAndSales"] /]
