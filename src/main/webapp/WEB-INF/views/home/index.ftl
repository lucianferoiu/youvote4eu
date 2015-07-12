[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="footer_script"]<script src='${context_path}/app/home.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/layout.js'></script>[/@content]
[#include "../other/utils.ftl"]

<div class="nav-buffer">
	&nbsp;
</div>

<!-- questions container: "mobile" rendering -->
<div id="qContainerFlow" class="q-cont q-cont-static container-fluid visible-xs-block">
	[#list questions as q]
	<div id="q${q.id}" class="q" 
		data-q-id="${q.id}" data-q-votes="${q.votesCount}" [#if q.arch] data-q-archived="yes"[/#if]
		data-q-pub-date='${q.publishedOn?string["dd/MM/yyyy HH:mm"]}' [#if q.archivedOn??]data-q-arch-date='${q.archivedOn?string["dd/MM/yyyy HH:mm"]}'[/#if]>
			<div class="q-title">${q.title}</div>
			<div class="q-desc visible-xs-block">
				<div class="col-xs-3">
					<div class="q-votes"><span>${q.votesCount}</span><br/>votes</div>
				</div>
				<blockquote class="col-xs-9">
					[#if q.archivedOn??]<footer>Archived on ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</footer>[/#if]
					<footer>Published on ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</footer>
				</blockquote>
				<p class="q-desc-p">${q.description}</p>
			</div>
	</div>
	[/#list]
</div>

<!-- questions container: grid rendering -->
<div id="qContainerGrid" class="q-cont container-fluid hidden-xs">
	<div id="qCarousel" class="carousel slide q-carousel" data-ride="carousel" data-wrap="true" data-interval="6000000" data-keyboard="true">
		<!-- Indicators -->
		<ol class="carousel-indicators" >
		</ol>

		<!-- Wrapper for slides -->
		<div class="carousel-inner" role="listbox">
			
		</div>

		<!-- Controls -->
		<a class="left carousel-control" href="#qCarousel" role="button" data-slide="prev">
			<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
			<span class="sr-only">Previous</span>
		</a>
		<a class="right carousel-control" href="#qCarousel" role="button" data-slide="next">
			<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			<span class="sr-only">Next</span>
		</a>
	</div>
</div>

<hr/>

<div class="aq-cont container-fluid q-cont-static">
	<div class="row">
		<div class="col-md-9 container-fluid">
			<div class="section-title row">Recently archived questions..</div>
			<div class="row">
				[#assign cnt=2]
				[#list last3ArchivedQuestions as q]
				<div id="q${q.id}" class="q aq col-md-4 q-bg-${cnt}" 
					data-q-id="${q.id}" data-q-votes="${q.votesCount}" [#if q.arch] data-q-archived="yes"[/#if]
					data-q-pub-date='${q.publishedOn?string["dd/MM/yyyy HH:mm"]}' [#if q.archivedOn??]data-q-arch-date='${q.archivedOn?string["dd/MM/yyyy HH:mm"]}'[/#if]>
						<div class="q-title">${q.title}</div>
						<div class="aq-votes-tally">
							<div class="q-votes"><span>${q.votesCount}</span> votes</div>
							<div class="q-tally"><span>Yes: ${((q.voteTally)*100)?string["0.##"]}% <strong> — </strong> No: ${((1-(q.voteTally))*100)?string["0.##"]}%</span></div>
						</div>
						<blockquote class="aq-milestones">
							[#if q.archivedOn??]<footer>Archived on ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</footer>[/#if]
							<footer>Published on ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</footer>
						</blockquote>
				</div>
				[#assign cnt=cnt+1]
				[/#list]
			</div>
		</div>
		<div class="col-md-3 container-fluid">
			<div class="section-title row">&nbsp;&nbsp;&nbsp;What can I do more?</div>
			<div class="row">
					<br/>
					<ul>
						<li><a href="/platform">Register</a> as Partner and propose questions, help with the translations of texts, support upcoming questions into publication</li>
						<li>Write your MEPs and tell them your position on the discussed issues</li>
						<li>Join Campains and Movements of like-minded citizen</li>
					</ul>
			</div>
		</div>
	</div>
</div>

<div class="nav-buffer">
	&nbsp;
</div>


[@content for="footer_script"]
<script type="text/javascript">
	var App = {
		reqURL: '${reqURL}',
		reqURI: '${reqURI}',
		reqHostname: '${reqHostname}',
		reqQuery: '${reqQuery}'
	};
</script>
[/@content]