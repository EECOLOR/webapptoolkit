[#import "/formElements.ftl" as formElements /]
[#import "/validation.ftl" as validation /]

[#macro create action submitLabel metadata value="" showValidationErrorList=false name=""]
	
	[#if showValidationErrorList]
		[@validation.displayValidation /]
	[/#if]
	
	<form action="${action}" method="POST">
		
		[@formElements.component label="" properties=metadata.displayProperties value=value name=name /]
		
		<input type="submit" value="${submitLabel}" />
	</form>
[/#macro]
