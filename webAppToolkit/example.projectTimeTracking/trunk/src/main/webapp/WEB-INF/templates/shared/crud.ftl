[#macro crud elementName elementListName newLabel editLabel tableLabelMethod="" tableIgnoredProperties=[] ]
	[#import "/form.ftl" as form /]
	[#import "/table.ftl" as table /]
	
	[#local metadata = model[elementName + "_metadata"] /]
	<p>
		[#if model[elementName]??]
			<a href="${context}">${newLabel}</a>
		[/#if]
	</p>
	
	<p>
	
		<b>${((model[elementName].id)??)?string(editLabel, newLabel)}</b>
		
		[@form.create 
			action=path 
			submitLabel="Opslaan" 
			metadata=metadata 
			value=model[elementName]!
			name=elementName /]
	</p>
	
	<p>
		[#local properties = metadata.displayProperties?values?sort_by(["annotations", "Display", "order"]) /]
		
		[@table.create 
			list=model[elementListName] 
			properties=properties
			editLabel="Bewerken"
			removeLabel="Verwijderen"
			labelMethod=tableLabelMethod
			ignoredProperties=tableIgnoredProperties /]
	</p>
[/#macro]