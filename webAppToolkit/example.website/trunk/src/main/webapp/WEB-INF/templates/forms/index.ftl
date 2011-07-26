[#include "/message.ftl" /]
[#import "/form.ftl" as form /]

[@message /]

[@form.create action=path submitLabel="Save" value=model.testObject /]
