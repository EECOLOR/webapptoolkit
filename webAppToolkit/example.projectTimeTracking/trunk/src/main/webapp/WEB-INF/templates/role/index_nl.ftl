[#include "/message.ftl" /]
[#import "/form.ftl" as form /]
[#import "/table.ftl" as table /]
[#include "/navigation.ftl" /]

[@navigation siteMap=siteMap /]

[@message /]

<p>
	[#assign metadata = model.role_metadata /]

	[#if model.role??]
		<a href="${path}">Nieuwe rol maken</a><br />
	[/#if]

	<b>
	${((model.role.id)??)?string("Rol bewerken", "Nieuw rol")}
	</b>
	
	[@form.create 
		action=path 
		submitLabel="Opslaan" 
		metadata=metadata 
		value=model.role! 
		name="role" /]
</p>

[#assign properties = metadata.displayProperties?values?sort_by(["annotations", "Display", "order"]) /]

[@table.create 
	list=model.roles 
	properties=properties
	editLabel="Bewerken"
	removeLabel="Verwijderen" /]