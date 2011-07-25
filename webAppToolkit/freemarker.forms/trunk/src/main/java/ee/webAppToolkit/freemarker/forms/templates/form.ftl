[#import "/formElements.ftl" as formElements /]

[#import "/validation.ftl" as validation /]

[#macro create action submitLabel value]
	
	[@validation.displayValidation /]
	
	<form action="${action}" method="POST">
		
		[@formElements.component value=value /]
		
		<input type="submit" value="${submitLabel}" />
	</form>
[/#macro]
