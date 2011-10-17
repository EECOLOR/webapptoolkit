[#macro create list properties editLabel="Edit" removeLabel="Remove" editPath="" removePath="/remove"]
	[#local hasContent = false /]
	[#local result]
		<table>
			<thead>
				[#list properties as property]
					[#local display = property.annotations.Display /]
					[#if display.type?lower_case != "hidden"]
						<th>${property.annotations.Display.label}</th>
					[/#if]
				[/#list]
				<th></th>
				<th></th>
			</thead>
			<tbody>
				[#list list as element]
					[#local hasContent = true /]
					<tr>
						[#list properties as property]
							[#local display = property.annotations.Display /]
							[#if display.type?lower_case != "hidden"]
								<td>${element[property.name]}</td>
							[/#if]
						[/#list]
						<td><a href="${context}${editPath}?id=${element.id}" class="edit">${editLabel}</a></td>
						<td><a href="${context}${removePath}?id=${element.id}" class="remove">${removeLabel}</a></td>
					</tr>
				[/#list]
			</tbody>
		</table>
	[/#local]
	[#if hasContent]
		${result}
	[/#if]
[/#macro]