[#macro navigation siteMap context="/"]
	<ul>
		[#list siteMap?keys as key]
			[#local value = siteMap[key] /]
			[#local url = context /] 
			[#if !context?ends_with("/")]
				[#local url = url + "/" /]
			[/#if]
			[#local url = url + key /]
			
			<li>
				<a[#if path?starts_with(url)] class="active"[/#if] href="${url}">${value.displayName!"[no display name]"}</a>
				[#if value?values?size > 0]
					[@navigation siteMap=value context=url /]	
				[/#if]
			</li>
		[/#list]
	</ul>
[/#macro]