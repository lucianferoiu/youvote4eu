[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="header_css"]<link href='${context_path}/css/jquery-ui.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/jquery-ui.min.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/home.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/layout.js'></script>[/@content]


<div id="questionsContainer" class="q-cont">
	
	[#list questions as q]
	<div id="q${q.id}" class="q" data-q-votes="${q.votesCount}" data-q-rank="${q.rank}" 
		data-q-id="${q.id}" data-q-sort="[#if q.new]newer[#else]popular[/#if]" [#if q.arch] data-q-archived="yes"[/#if]
		data-q-pub-date='${q.publishedOn?string["dd/MM/yyyy HH:mm"]}' [#if q.archivedOn??]data-q-arch-date='${q.archivedOn?string["dd/MM/yyyy HH:mm"]}'[/#if]>
		<div id="qq${q.id}" class="qq">
			<div class="q-votes">${q.votesCount}</div>
			<div class="q-title">${q.title}</div>
			<div class="q-desc">${q.description}</div>
			<div class="q-pub hide">Published: ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</div>
			<div class="q-vote-bar con">
				 Vote now: 
				 <button class="btn btn-success">Yes</button> 
				 <button class="btn btn-danger">No</button> 
				 <button class="btn btn-default"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></button>
			</div>
			[#if q.archivedOn??]<div class="q-arch hide">Archived: ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</div>[/#if]
		</div>
	</div>
	[/#list]
	
</div>

<div class="side-cont hidden-xs">
	TODO: Archived questions...
</div>

[@content for="footer_script"]
<script type="text/javascript">
(function() {
	var root = this;

	$(document).ready(function(){
		$('.logo').click(function () {
			window.location='${context_path}';
		});
	});

}());
</script>
[/@content]