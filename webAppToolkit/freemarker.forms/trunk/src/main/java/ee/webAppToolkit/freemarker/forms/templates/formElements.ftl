[#include "/c.ftl" /]
[#import "/validation.ftl" as validation /]

[#macro hidden name value]
	<input type="hidden" name="${name}" value="[@c value /]" />
[/#macro]

[#macro label label name optional error]
	<label ${error?string('class="error"', '')} for=${name}>${label}${optional?string("", " *")}</label>	
[/#macro]

[#macro text label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#if error]
		[#local value = validationResults[name].originalValue!]
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
[/#macro]

[#macro textarea label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[#if error]
		[#local value = validationResults[name].originalValue!]
	[/#if]
	[#local value][@c value /][/#local]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<textarea 
		${error?string('class="error"', '')} 
		id="${name}" name="${name}" 
	>${value}</textarea>
[/#macro]

[#macro list label name value optional=true property=false]
	[#local error = validation.hasValidationErrors(name) /]
	[@.namespace.label label=label name=name optional=optional error=error /]
	<select ${error?string('class="error"', '')} name="${name}">
		[#if property.annotations.List??]
			<option value="">${property.annotations.List.defaultValue}</option>
		[/#if]
		[#list property.enumeration as element]
			[#assign selected = '' /]
			[#if (value.value)?? && element.value == value.value]
				[#assign selected = ' selected="selected"' /]
			[/#if]
			<option value="${element.value}"${selected}>${element.label}</option>
		[/#list]
	</select>
[/#macro]