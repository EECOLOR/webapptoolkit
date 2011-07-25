[#include "/shared/dump.ftl" /]
[#include "/message.ftl" /]

[#if model??]
	[@dump data=model.testObject.title_metadata /]
[/#if]

[#import "/form.ftl" as form /]

[@message /]

[@form.create action=path submitLabel="Save" value=model.testObject /]