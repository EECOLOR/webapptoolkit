[#include "/navigation.ftl" /]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <html>
 	<head>
 	</head>
 	<body>
 		<h1>${model.title}</h1>
 	
 		<div id="navigation">
 			[@navigation model.siteMap /]	
 		</div>
 		
 		<div id="content">${model.content}</div>
 	<body>
 </html>