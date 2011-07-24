[#import "/formElements.ftl" as formElements /]

[#import "/validation.ftl" as validation /]

[#macro create action submitLabel key base=.data_model]
	
	[@validation.displayValidation /]
	
	<form action="${action}" method="POST">
		
		[@createFields key base /]
		
		<input type="submit" value="${submitLabel}" />
	</form>
[/#macro]

[#macro createFields key base=.data_model]
	[#list base[key + "_properties"]?values?sort_by(["annotations", "Display", "order"]) as property]
		[#local annotations = property.annotations /]
		[#local display = annotations.Display /]
		[#local value = base[key][property.name]! /]
		
		[#local type = display.type?lower_case /]
		[#attempt]
			<div>
				[@formElements[type] 
					label=display.label 
					name=property.name 
					value=value 
					optional=annotations.Optional??
					property=property
				/]
			</div>
		[#recover]
			Error rendering @formElements[${type}]:<br />
			${.error}
		[/#recover]
	[/#list]
[/#macro]