[#include "/message.ftl" /]
[#import "/form.ftl" as form /]

[@message /]

<table>
	<thead>
		<th>Title</th>
		<th>Action</th>
	</thead>
	<tbody>
		[#list model.store as element]
			<tr>
				<td><a href="${path}?id=${element.id}">${element.title}</a></td>
				<td><a href="${context}/remove?id=${element.id}">Remove</a></td>
			</tr>
		[/#list]
	</tbody>
</table>

[#if model.testObject??]
	<a href="${path}">New</a>
[/#if]

[@form.create action=path submitLabel="Save" metadata=model.testObject_metadata value=model.testObject! name="testObject" /]
