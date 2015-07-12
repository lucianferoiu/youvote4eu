[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="footer_script"]<script src='${context_path}/app/home.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/layout.js'></script>[/@content]
[#if isMobileAgent]
	[#-- [@content for="header_css"]<link href='${context_path}/css/jquery.mobile.min.css' rel="stylesheet">[/@content] --]
	[@content for="footer_script"]<script src='${context_path}/js/jquery.mobile.min.js'></script>[/@content]
[/#if]

[#include "../other/utils.ftl"]

<div class="nav-buffer">
	&nbsp;
</div>

<!-- questions container: "mobile" rendering -->
<div id="qContainerFlow" class="q-cont q-cont-static container-fluid visible-xs-block">
	[#list questions as q]
	<div id="q${q.id}" class="q hover" 
		data-q-id="${q.id}" data-q-votes="${q.votesCount}" [#if q.arch] data-q-archived="yes"[/#if]
		data-q-pub-date='${q.publishedOn?string["dd/MM/yyyy HH:mm"]}' [#if q.archivedOn??]data-q-arch-date='${q.archivedOn?string["dd/MM/yyyy HH:mm"]}'[/#if]>
			<div class="q-title">${q.title}</div>
			<div class="q-desc visible-xs-block">
				<div class="col-xs-3">
					<div class="q-votes"><span>${q.votesCount}&nbsp;&nbsp;</span><br/>votes<br/></div>
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
<div id="qContainerGrid" class="q-cont q-cont-carousel container-fluid hidden-xs">
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
		<div class="col-md-9 container-fluid" >
			<div class="section-title row">Recently archived questions..</div>
			<div class="row">
				[#assign cnt=rand(1,10)]
				[#list last3ArchivedQuestions as q]
				<div id="q${q.id}" class="q aq col-md-4 q-bg-${(cnt%3)+2}" 
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
			<div class="section-title row"><div style="padding-left:1em;"> What can I do more?</div></div>
			<div class="row">
					<br/>
					<ul style="border-left:1px solid #e7e7e7; margin-left:1em;">
						<li><a href="/platform">Register</a> or <a href="/platform">Login</a> as <em>YouVote</em> Partner and propose questions, help with the translations of texts and support other proposed questions into publication</li>
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

<div class="templates hide">
	<div class="q-voting-booth q-bg-1">
		<div class="btn-group" role="group">
			<div class="voting-buttons">
				<a class="col-xs-3 vote-yes btn btn-default">YES</a>
				<a class="col-xs-3 vote-no btn btn-default">NO</a>
			</div>
			<div class="already-voted hide">
				<a class="col-xs-6 btn disabled">YES</a>
			</div>
			<a class="col-xs-6 vote-details btn btn-default">Details..</a>
		</div>
		<div class="vote-validation-pending" style="padding:8px;">
			<div class="col-xs-12 bg-default">Email validation pending.. <span class="glyphicon glyphicon-info-sign pull-right"></span></div>
		</div>
	</div>
</div>


[@content for="footer_script"]
<script type="text/javascript">
	var App = {
		reqURL: '${reqURL}',
		reqURI: '${reqURI}',
		reqHostname: '${reqHostname}',
		reqQuery: '${reqQuery}',
		isMobileAgent: ${isMobileAgent?c}
	};
</script>
[/@content]