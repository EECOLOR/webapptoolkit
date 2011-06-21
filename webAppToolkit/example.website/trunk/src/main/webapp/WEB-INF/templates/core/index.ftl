<form action="${path}" method="POST">
	<select name="requestMethod">
		<option>GET</option>
		<option>PUT</option>
		<option>DELETE</option>
	</select>
	Key:<input type="text" name="key" />
	Value:<input type="text" name="value" />
	<input type="submit" value="Test" />
</form>
<hr />
[#if model??]
	<h2>Result:</h2>
	
	<p>
		${model}
	</p>
[/#if]