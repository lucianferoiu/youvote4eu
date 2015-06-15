<@wrap with="/system/layout">

<hr/>

<div class="jumbotron">
	<h2>Error: <code>${message}</code></h2>
	<div class="row">
		<br/>
		<button class="btn btn-danger col-xs-2" id="show_link" onclick='show();'>See Stack Trace</button>
		<div id="stack_trace" style="display:none" class="col-xs-10">
			<pre class="bg-warning">${stack_trace}</pre>
		</div>
	</div>
</div>
<script type="text/javascript">
	function show() {
		document.getElementById("stack_trace").removeAttribute("style");
	}
</script>

</@>