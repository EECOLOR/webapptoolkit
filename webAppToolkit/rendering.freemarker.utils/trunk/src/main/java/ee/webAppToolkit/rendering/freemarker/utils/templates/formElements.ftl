[#include "/c.ftl" /]
[#import "/validation.ftl" as validation /]

[#macro property property parentName parentValue='' ]
	
	[#local propertyName = property.name /]

	[#local subName = propertyName /]
	[#if parentName?length > 0]
		[#local subName = parentName + "." + subName]
	[/#if]
	
	[#local annotations = property.annotations /]
	[#local display = annotations.Display /]
	[#local type = display.type?lower_case /]
	<div class="${type}">

		[#local currentValue = "" /]
		
		[#if parentValue?is_hash]
			[#local currentValue = parentValue[propertyName]! /]
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
[/#macro]

[#macro component label properties value name="" optional=true]
	[#local error = validation.hasValidationErrors(name) /]
	[@.namespace.label label=label name=name optional=optional error=error /]
	
	[#list properties?values?sort_by(["annotations", "Display", "order"]) as property]
		[@.namespace.property property=property parentName=name parentValue=value /] 
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
					[#if createRemoveLink && listComponent.id??]
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

[#macro list label name value optional=true property=false enumeration='' ]
	[#local error = validation.hasValidationErrors(name) /]
	[#local valueHasContent = value?has_content /]
	[#if valueHasContent]
		[#local value = value._enumeration /]
	[/#if]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<select ${error?string('class="error"', '')} name="${name}">
		[#if property.annotations.List??]
			<option value="">${property.annotations.List.defaultLabel}</option>
		[/#if]
		
		[#if enumeration?is_enumerable]
			[#local enumerationList = enumeration /]
		[#else]
			[#local enumerationList = property.enumeration /]
		[/#if]
		[#list enumerationList as element]
			[#local elementAsEnumeration = element._enumeration /]
			[#local selected = '' /]
			[#if valueHasContent && value.value?? && elementAsEnumeration.value == value.value]
				[#local selected = ' selected="selected"' /]
			[/#if]
			<option value="${elementAsEnumeration.value}"${selected}>${elementAsEnumeration.label}</option>
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

[#macro time label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#if error]
		[#local value = validation.getOriginalValue(name) /]
	[#else]
		[#if value?is_date]
			[#local value = value?string("HH:mm") ]
		[/#if]
	[/#if]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<input type="time" 
		${error?string('class="error"', '')} 
		id="${name}" name="${name}" 
		value="${value}"
	/>
	[@validation.showValidationError name=name /]
[/#macro]