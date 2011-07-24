[#macro message]
	[#if model.message??]
		<div class="message">
			${model.message}
		</div>
	[/#if]
[/#macro]