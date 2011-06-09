<form action="${context}/indexHandler" method="POST">
	<select name="requestMethod">
		<option>GET</option>
		<option>POST</option>
		<option>PUT</option>
		<option>DELETE</option>
	</select>
	Key:<input type="text" name="key" />
	Value:<input type="text" name="value" />
	<input type="submit" value="Test" />
</form>
<hr />
[#if model.result??]
	<h2>Result:</h2>
	
	<p>
		${model.result}
	</p>
[/#if]