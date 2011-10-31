[#include "/c.ftl" /]
[#import "/validation.ftl" as validation /]

[#macro component label properties value name="" optional=true]
	[#local error = validation.hasValidationErrors(name) /]
	[@.namespace.label label=label name=name optional=optional error=error /]
	
	[#list properties?values?sort_by(["annotations", "Display", "order"]) as property]
		[#local propertyName = property.name /]

		[#local subName = propertyName /]
		[#if name?length > 0]
			[#local subName = name + "." + subName]
		[/#if]
		
		[#local annotations = property.annotations /]
		[#local display = annotations.Display /]
		[#local type = display.type?lower_case /]
		<div class="${type}">
	
			[#local currentValue = "" /]
			
			[#if value?is_hash]
				[#local currentValue = value[propertyName]! /]
			[/#if]
	
			[#attempt]
				[#switch type]
					[#case "component"]
						[@.namespace[type] 
							label=display.label
							properties=property.displayProperties
							value=currentValue
							name=subName 
							optional=annotations.Optional??
						/]
						[#break /]
					[#case "component_list"]
						[#if property.isList]
													
							[#local addEmptyComponent = !(annotations.ComponentList??) || annotations.ComponentList.addEmptyComponent /]
							[#local createRemoveLink = annotations.ComponentList?? && annotations.ComponentList.createRemoveLink /]
							[#local removeLinkLabel = (annotations.ComponentList.removeLinkLabel)! /]
						
							[@.namespace[type] 
								label=display.label
								properties=property.componentDisplayProperties
								value=currentValue
								name=subName 
								addEmptyComponent=addEmptyComponent
								createRemoveLink=createRemoveLink
								removeLinkLabel=removeLinkLabel
								optional=annotations.Optional??
							/]
						[#else]
							Error rendering @formElements[${type}]:<br />
							Could not render the component_list as the property is not a list type (List or array)
						[/#if]
						[#break /]
					[#case "hidden"]
						[@hidden value=currentValue name=subName /]
						[#break /]
					[#default]
						[@.namespace[type] 
							label=display.label 
							name=subName 
							value=currentValue
							optional=annotations.Optional??
							property=property
						/]
				[/#switch]
			[#recover]
				Error rendering @formElements[${type}]:<br />
				${.error}
			[/#recover]
		</div>
	[/#list]
[/#macro]

[#macro component_list label properties value name="" addEmptyComponent=true createRemoveLink=false removeLinkLabel="" optional=true ]
	[#local index = -1 /]
	
	[#local error = validation.hasValidationErrors(name) /]
	[@.namespace.label label=label name=name optional=optional error=error /]
	
	[#if !value?is_string && value?size > 0]
		[#list value as listComponent]
			[#local index = listComponent_index /]
			[#local subName = index /]
			[#if name?length > 0]
				[#local subName = name + "." + subName]
			[/#if]
			
			<div class="component">
				[#if listComponent??]
					[@.namespace.component label="" properties=properties value=listComponent name=subName /]
					[#if createRemoveLink]
						 <button type="submit" name="remove${listComponent.class?split(".")?last}" value="${listComponent.id}">${removeLinkLabel}</button>
					[/#if]
				[#else]
					<b>Error: A component in this list is null, this should not happen</b>
				[/#if]
			</div>
		[/#list]
	[/#if]
	
	[#if addEmptyComponent && !error]
		<div class="component">
			[@.namespace.component label="" properties=properties value="" name=name + "." + (index + 1) /]
		</div>
	[/#if]

[/#macro]

[#macro label label name optional error]
	<label ${error?string('class="error"', '')} for=${name}>${label}${optional?string("", " *")}</label>	
[/#macro]

[#macro text label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#if error]
		[#local value = validation.getOriginalValue(name)]
	[/#if]
	[#local value][@c value /][/#local]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<input type="text" 
		${error?string('class="error"', '')} 
		id="${name}" name="${name}" 
		value="${value}"
		[#if property.annotations.Text??]
			maxLength="${property.annotations.Text.maxLength}"
			${property.annotations.Text.readonly?string('readonly="readonly"', '')}
		[/#if] 
	/>
	[@validation.showValidationError name=name /]
[/#macro]

[#macro textarea label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#if error]
		[#local value = validation.getOriginalValue(name)]
	[/#if]
	[#local value][@c value /][/#local]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<textarea 
		${error?string('class="error"', '')} 
		id="${name}" name="${name}" 
	>${value}</textarea>
	[@validation.showValidationError name=name /]
[/#macro]

[#macro list label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#local valueIsHash = value?is_hash /]
	[#if valueIsHash && value._enumeration??]
		[#local value = value._enumeration /]
	[/#if]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<select ${error?string('class="error"', '')} name="${name}">
		[#if property.annotations.List??]
			<option value="">${property.annotations.List.defaultLabel}</option>
		[/#if]
		[#list property.enumeration as element]
			[#local selected = '' /]
			[#if valueIsHash && value.value?? && element.value == value.value]
				[#local selected = ' selected="selected"' /]
			[/#if]
			<option value="${element.value}"${selected}>${element.label}</option>
		[/#list]
	</select>
	[@validation.showValidationError name=name /]
[/#macro]

[#macro date label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#if error]
		[#local value = validation.getOriginalValue(name) /]
	[#else]
		[#if value?is_date]
			[#local value = value?string("yyyy-MM-dd") ]
		[/#if]
	[/#if]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<input type="date" 
		${error?string('class="error"', '')} 
		id="${name}" name="${name}" 
		value="${value}"
	/>
	[@validation.showValidationError name=name /]
[/#macro]

[#macro hidden name value]
	<input type="hidden" name="${name}" value="[@c value /]" />
[/#macro]

[#macro checkbox label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#if error]
		[#local value = validation.getOriginalValue(name)]
	[/#if]

	[#if value?is_string]
		[#local value = false /]
	[/#if]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<input type="checkbox" 
		${error?string('class="error"', '')} 
		id="${name}" name="${name}" 
		${value?string('checked="checked"', '')}
		value="true"
	/>
	[@validation.showValidationError name=name /]
[/#macro]