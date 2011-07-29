[#include "/c.ftl" /]
[#import "/validation.ftl" as validation /]

[#macro component value name=""]
	[#list value._properties?values?sort_by(["annotations", "Display", "order"]) as property]
		[#local propertyName = property.name /]

		[#local subName = propertyName /]
		[#if name?length > 0]
			[#local subName = name + "." + subName]
		[/#if]
		
		[#local annotations = property.annotations /]
		[#local display = annotations.Display /]
		[#local type = display.type?lower_case /]
		
		[#attempt]
			[#switch type]
				[#case "component"]
				[#case "component_list"]
					[#if value[propertyName]??]
						<div class="${type}">
							[@.namespace[type] 
								value=value[propertyName] 
								name=subName 
							/]
						</div>
					[/#if] 
					[#break /]
				[#case "hidden"]
					<div>
						[@hidden value=value[propertyName] name=subName /]
					</div>
					[#break /]
				[#default]
					<div>
						[@.namespace[type] 
							label=display.label 
							name=subName 
							value=value[propertyName]! 
							optional=annotations.Optional??
							property=property
						/]
					</div>
			[/#switch]
		[#recover]
			Error rendering @formElements[${type}]:<br />
			${.error}
		[/#recover]
	[/#list]
[/#macro]

[#macro component_list value name=""]
	[#list value as component]
		[#local subName = component_index /]
		[#if name?length > 0]
			[#local subName = name + "." + subName]
		[/#if]
	
		<div class="component">
			[@.namespace.component value=component name=subName /]
		</div>
	[/#list]
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
	[@.namespace.label label=label name=name optional=optional error=error /]
	<select ${error?string('class="error"', '')} name="${name}">
		[#if property.annotations.List??]
			<option value="">${property.annotations.List.defaultLabel}</option>
		[/#if]
		[#list property.enumeration as element]
			[#assign selected = '' /]
			[#if (value.value)?? && element.value == value.value]
				[#assign selected = ' selected="selected"' /]
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