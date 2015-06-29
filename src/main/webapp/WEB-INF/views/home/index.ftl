[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="header_css"]<link href='${context_path}/css/jquery-ui.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/jquery-ui.min.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/home.js'></script>[/@content]


<div id="questionsContainer" class="q-cont">
	
	[#list questions as q]
	<div id="q${q.id}" class="q" data-q-pub-date='${q.publishedOn?string["dd/MM/yyyy HH:mm"]}' data-q-votes="${q.votesCount}" data-q-rank="${q.rank}" data-q-sort="[#if q.new]newer[#else]popular[/#if]">
		<div id="qq${q.id}" class="qq">
			<div class="q-title">${q.title}</div>
			<div class="q-desc">${q.description}</div>
			<div class="q-pub hide">Published: ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</div>
		</div>
	</div>
	[/#list]
	
	
	[#-- [#list questions as q]
	<div id="q${q.id}" class="q">
		<div id="qq${q.id}" class="qq">
			<div class="q-title">${q.title}</div>
			<br/>
			<div class="q-counter text-muted">
				&nbsp;
				<br/>
				<strong>${q.popular_votes}</strong> votes so far...
			</div>
			<div class="q-desc">
				<em class="pull-right muted" style="color:gray;">Published at ${q.open_at?string["dd/MM/yyyy HH:mm"]}</em>
				<br/><br/>
				${q.description}
			</div>
			<div class="q-voting">
				<div>
					<br/>
					<strong>${q.popular_votes}</strong> votes so far...
				</div>
				<div>
					<span class="label label-info pull-right" style="margin:2px;"> Details.. </span>
					<span class="label label-danger pull-right" style="margin:2px;"> No </span>
					<span class="label label-success pull-right" style="margin:2px;"> Yes </span>
					<span class="pull-right" style="margin:2px;"> Vote now! </span>
				</div>
			</div>
		</div>
	</div>
	[/#list]
	<br/>
	<div id="q-loading" class="loading" style="display:none; color:red;">
		Loading more questions...
	</div> --]
</div>
<div class="aq-cont">
	Archived questions...
	
</div>



[#--[@content for="footer_script"]<script  type="text/javascript">

	var cells=${cellsAsJSON};
	$(function () {
		YV.init(cells,{
			maxOffset: ${maxGridHeight}
		});
	});
</script>[/@content] --]