[#include "/message.ftl" /]
[#include "/navigation.ftl" /]
[#include "/shared/crud.ftl" /]

[@navigation siteMap=siteMap /]

[@message /]

[@crud 
	elementName="role"
	elementListName="roles"
	newLabel="Nieuwe rol"
	editLabel="Rol bewerken" /]
