[#macro create list properties editLabel="Edit" removeLabel="Remove" editPath="" removePath="/remove" labelMethod="" ignoredProperties=[] ]
	[#local hasContent = false /]
	[#local result]
		<table>
			<thead>
				[#list properties as property]
					[#local display = property.annotations.Display /]
					[#if display.type?lower_case != "hidden" && !ignoredProperties?seq_contains(property.name)]
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
							[#local propertyName = property.name /]
							[#if display.type?lower_case != "hidden" && !ignoredProperties?seq_contains(property.name)]
								<td>
									[#if labelMethod?length > 0 && .main[labelMethod]??]
										${.main[labelMethod](element, propertyName)?string}
									[#else]
										${element[propertyName]?string}
									[/#if]
								</td>
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