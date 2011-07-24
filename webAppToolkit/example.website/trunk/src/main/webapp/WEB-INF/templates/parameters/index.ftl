[#include "/shared/dump.ftl" /]

[#if model??]
	[@dump data=model.title_metadata /]
[/#if]

[#import "/form.ftl" as form /]

[@form.create action=path submitLabel="Save" key="model" /]

<form action="${path}" method="POST">
	[#assign value = (model.title)! /]
	[#if (validationResults.title.validated)?? && !validationResults.title.validated]
		<em>${validationResults.title.errorMessage}</em><br />
		[#assign value = validationResults.title.originalValue! /]
	[/#if]
	Title: <input type="text" name="title" value="${value}" /><br />
	
	[#assign value = (model.version)! /]
	[#if (validationResults.version.validated)?? && !validationResults.version.validated]
		<em>${validationResults.version.errorMessage}</em><br />
		[#assign value = validationResults.version.originalValue! /]
	[/#if]
	Version: <input type="text" name="version" value="${value}" /><br />
	
	[#assign value = (model.content)! /]
	[#if (validationResults.content.validated)?? && !validationResults.content.validated]
		<em>${validationResults.content.errorMessage}</em><br />
		[#assign value = validationResults.content.originalValue! /]
	[/#if]
	Content: <br />
	<textarea name="content">${value}</textarea><br />
	<input type="submit" />
</form>