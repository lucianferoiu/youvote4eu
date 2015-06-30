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
	
</div>
<div class="aq-cont">
	TODO: Archived questions...
</div>
