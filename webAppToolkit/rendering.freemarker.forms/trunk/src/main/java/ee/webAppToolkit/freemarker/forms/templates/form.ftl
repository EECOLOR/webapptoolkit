[#import "/formElements.ftl" as formElements /]

[#import "/validation.ftl" as validation /]

[#macro create action submitLabel value showValidationErrorList=false]
	
	[#if showValidationErrorList]
		[@validation.displayValidation /]
	[/#if]
	
	<form action="${action}" method="POST">
		
		[@formElements.component value=value /]
		
		<input type="submit" value="${submitLabel}" />
	</form>
[/#macro]
