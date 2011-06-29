<p>
	This form allows you to perform a http request with the given request method.
	
	<ul>
		<li>GET - requires no keys or values to be passed in, lists the entries in the store</li>
		<li>PUT - requires you to give a key and value, it stores this in the store</li>
		<li>DELETE - requires you to give a key, it removes this from the store</li>
	</ul>
</p>

<hr />

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
[#if model??]
	<hr />
	
	<strong>Result:</strong>
	
	<p>
		${model}
	</p>
[/#if]
<hr />

<p>
	<strong>GET</strong>
	<code><pre>
	@Get
	@HideFromNavigation
	public Result rest(@Named("store") Map<String, String> store)
	{
		return render(store, true);
	}
	</pre></code>	
	<p>
		The 'rest' action annotated with <code>@Get</code> is executed if a request 
		comes in with the http request method <code>GET</code>.
	</p>
	<p>
		<code>@HideFromNavigation</code> is an annotation that is provided by the 
		<code>navigation</code> module and makes sure this method is not shown in the navigation.
	</p>
	<p>
		The argument 'store' is provided by Guice.
	</p>
	<p>
		The result is obtained by rendering the 'rest' template with the 'store' as 
		model. If no name is given to the 'render' method, it used the name of the 
		action that is currently being executed. The boolean indicates that the 
		result should be returned as is; wrapping is prevented. The 'render' method is 
		provided by the <code>RenderingController</code> from the <code>render</code> 
		module.
	</p>
	<strong>PUT</strong>
	<code><pre>
	@Put
	public Result rest(@Named("store") Map<String, String> store, 
		@Parameter("key") String key,
		@Parameter("value") String value) 
	{
		store.put(key, value);
		return output(key + " stored", true);
	}
	</pre></code>
	<p>
		The 'rest' action annotated with <code>@Put</code> is executed if a request 
		comes in with the http request method <code>PUT</code>.
	</p>
	<p>
		The argument 'store' is provided by Guice. The 'key' and 'value' arguments 
		are provided by the <code>parameters</code> module which provides a 
		<code>@Parameter</code> annotation. This annotation allows you to convert 
		the request parameters from the <code>ServletRequest</code> to the given type.
	</p>
	<p>
		No template is rendered and only output is returned. Wrapping is prevented.
	</p>
	<strong>DELETE</strong>
	<code><pre>
	@Delete
	public Result rest(@Named("store") Map<String, String> store, 
			@Parameter("key") String key) 
	{
		store.remove(key);
		return output(key + " removed", true);
	}
	</pre></code>
	<p>
		The 'rest' action annotated with <code>@Delete</code> is executed if a request 
		comes in with the http request method <code>DELETE</code>.
	</p>
	<p>
		The argument 'store' is provided by Guice. The 'key' argument 
		is provided by the <code>parameters</code> module.
	</p>
	<p>
		No template is rendered and only output is returned. Wrapping is prevented.
	</p>
</p>

