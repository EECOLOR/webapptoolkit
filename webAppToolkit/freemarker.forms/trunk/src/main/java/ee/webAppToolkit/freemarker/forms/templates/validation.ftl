[#function hasValidationErrors propertyName]
	[#return validationResults?? && validationResults[propertyName]?? && !validationResults[propertyName].validated /]
[/#function]

[#macro displayValidation]
	[#if validationResults?? && !validationResults.validated]
		<ul>
			[#list validationResults?keys as key]
				[#local validationResult = validationResults[key] /]
				[#if validationResult?is_hash]
					<li><label for="${key}">${validationResult.errorMessage}</label></li>
				[#else]
					[#list validationResult as validationResultElement]
						[#if !validationResultElement.validated]
							<li><label for="${key}">${validationResultElement.errorMessage}</label></li>
						[/#if]
					[/#list]
				[/#if]
				
			[/#list]
		</ul>
	[/#if]
[/#macro]