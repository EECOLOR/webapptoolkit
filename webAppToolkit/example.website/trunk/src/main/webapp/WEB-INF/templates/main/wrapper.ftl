<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <html>
 	<head>
 	</head>
 	<body>
 		<h1>${model.title}</h1>
 	
 		<div id="menu">
 			[#macro createNavigation siteMap context=""]
 				<ul>
	 				[#list siteMap?keys as key]
	 					[#local value = siteMap[key] /]
	 					[#local url = context /] 
	 					[#if !context?ends_with("/")]
	 						[#local url = url + "/" /]
	 					[/#if]
	 					[#local url = url + key /]
	 					
	 					<li>
		 					[#if value?values?size > 0]
		 						[@createNavigation siteMap=value context=url /]	
		 					[#else]
		 						<a href="${url}">${url}</a>
		 					[/#if]
	 					</li>
	 				[/#list]
 				</ul>
 			[/#macro]
 			[@createNavigation model.siteMap "/" /]	
 		</div>
 		
 		<div id="content">${model.content}</div>
 	<body>
 </html>