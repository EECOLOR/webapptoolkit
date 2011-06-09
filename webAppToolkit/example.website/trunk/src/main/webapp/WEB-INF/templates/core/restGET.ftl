<ul>
	[#list model.store?keys as key]
		<li>${key} - ${model.store[key]}</li>
	[/#list]
</ul>