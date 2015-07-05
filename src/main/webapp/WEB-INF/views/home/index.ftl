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
		<div id="qq${q.id}" class="qq hover">
			<div class="q-votes">${q.votesCount}[#if q.new && !(q.archivedOn??)][#assign diff = (.now?long / 86400000)?floor - (q.publishedOn?long / 86400000)?floor /]<span class="glyphicon glyphicon-asterisk q-new-hint" aria-hidden="true"></span><span class="q-new-hint"> (${diff} days ago)</span>
				[/#if]</div>
			<div class="q-title">${q.title}</div>
			
			<div class="q-desc">
				${q.description}
				<br/>
				<p class="q-pub pull-right">Published: ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</p>
			</div>
			<div class="q-vote-bar">
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

<div class="side-cont hidden-xs" style="margin-top:0px;">
	<div class="panel panel-default">
		<div class="panel-heading">Archived recently</div>
		  <div class="panel-body">
			[#list last3ArchivedQuestions as q]
			<div id="aq${q.id}" class="aq" data-q-id="${q.id}" [#if q.archivedOn??]data-q-arch-date='${q.archivedOn?string["dd/MM/yyyy HH:mm"]}'[/#if]>
					<div class="aq-title">${q.title}</div>
					<hr/>
					[#if q.archivedOn??]<div class="aq-arch"><em>Archived: ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</em></div>[/#if]
					[#if q.voteTally??]
					<div class="container-fluid">
						<div class="aq-vote-tally row">
							<span class="label label-success text-left">Yes: ${((q.voteTally)*100)?string["0.##"]} %</span>&nbsp;
							<span class="label label-danger text-right">No: ${((1-(q.voteTally))*100)?string["0.##"]} %</span>
							<button class="btn btn-default pull-right"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></button>
						</div>
					</div>
					[/#if]
			</div>
			[/#list]
		</div>
	</div>
</div>

[@content for="footer_script"]
<script type="text/javascript">
(function() {
	var root = this;
	var idx = window.location.href.lastIndexOf("${context_path}");
	idx = (idx<=0?window.location.href.lastIndexOf("?")-1:idx);
	var App = {
		contextPath:'${context_path}',
		rootPath: idx<=0?window.location.href:window.location.href.substr(0,idx)
	};
	root.App = App;
}());
</script>
[/@content]