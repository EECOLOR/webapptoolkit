[#function hasValidationErrors name]
	[#if validationResults??]
		[#local currentValidationResults = validationResults /]
	
		[#list name?split(".") as namePart]
			[#if currentValidationResults[namePart]??]
				[#local currentValidationResults = currentValidationResults[namePart] /]
				
				[#if currentValidationResults.validated]
					[#return false /]
				[/#if]
			[#else]
				[#return false /]
			[/#if]
		[/#list]
		[#return true /]
	[#else]
		[#return false /]
	[/#if]
[/#function]

[#function getOriginalValue name]
	[#local result = "" /]
	[#if validationResults??]
		[#local currentValidationResults = validationResults /]
	
		[#list name?split(".") as namePart]
			[#local currentValidationResults = currentValidationResults[namePart] /]
		[/#list]
		
		[#if currentValidationResults.list]
			[#list currentValidationResults as validationResult]
				[#if validationResult.originalValue??]
					[#local result = validationResult.originalValue /]
					[#break /]
				[/#if]
			[/#list]
		[#else]
			[#local result = currentValidationResults.originalValue!]
		[/#if]
		
	[/#if]
	[#return result /]
[/#function]

[#macro showValidationError name]
	[#if validationResults??]
		[#local currentValidationResults = validationResults /]
		[#local hasValidationError = true /]
	
		[#list name?split(".") as namePart]
			[#if currentValidationResults[namePart]??]
				[#local currentValidationResults = currentValidationResults[namePart] /]
			[#else]
				[#local hasValidationError = false /]
				[#break /]
			[/#if]
		[/#list]
		
		[#if hasValidationError]
			[#if currentValidationResults.list]
				[#list currentValidationResults as validationResult]
					[#if validationResult.errorMessage??]
						<span class="validationError">${validationResult.errorMessage}</span>
						[#break /]
					[/#if]
				[/#list]
			[#else]
				<span class="validationError">${currentValidationResults.errorMessage}</span>
			[/#if]
		[/#if]
	[/#if]
[/#macro]

[#macro displayValidationResults validationResults prefix=""]
	[#if !validationResults.validated]
		[#list validationResults?keys as key]
			[#local name = key /]
			
			[#if prefix?length > 0]
				[#local name = prefix + "." + key /]
			[/#if]
			
			[#local validationResult = validationResults[key] /]
			[#if validationResult.map]
				[@displayValidationResults validationResults=validationResult prefix=name /]
			[#elseif validationResult.list]
				[#list validationResult as validationResultElement]
					[#if !validationResultElement.validated]
						<li><label for="${name}">${validationResultElement.errorMessage}</label></li>
					[/#if]
				[/#list]
			[#else]
				[#if !validationResult.validated]
					<li><label for="${name}">${validationResult.errorMessage}</label></li>
				[/#if]
			[/#if]
		[/#list]
	[/#if]
[/#macro]

[#macro displayValidation]

	[#if validationResults??]
		<ul>
			[@displayValidationResults validationResults=validationResults /]
		</ul>
	[/#if]
[/#macro]