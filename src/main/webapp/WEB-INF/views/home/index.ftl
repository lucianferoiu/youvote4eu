[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="footer_script"]<script src='${context_path}/app/home.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/layout.js'></script>[/@content]
[#if isMobileAgent]
	[#-- [@content for="header_css"]<link href='${context_path}/css/jquery.mobile.min.css' rel="stylesheet">[/@content] --]
	[#-- [@content for="footer_script"]<script src='${context_path}/js/jquery.mobile.min.js'></script>[/@content] --]
[/#if]
[@content for="header_css"]<link href='${context_path}/css/flags.css' rel="stylesheet">[/@content]
[@content for="header_css"]<link href='${context_path}/css/bootstrap-select.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/bootstrap-select.min.js'></script>[/@content]


[#include "../other/utils.ftl"]

<div class="nav-buffer">
	&nbsp;
</div>

<!-- questions container: "mobile" rendering -->
<div id="qContainerFlow" class="q-cont q-cont-static container-fluid visible-xs-block">
	[#list questions as q]
	<div id="q${q.id}" class="q hover" data-q-id="${q.id}">
			<div class="q-title">${q.title}</div>
			<div class="q-desc visible-xs-block">
				<div class="col-xs-3">
					<div class="q-votes"><span class="q-popular-votes">${q.votesCount}&nbsp;&nbsp;</span><br/>votes<br/></div>
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

<div id="votingBoothFlyweight" class="voting-booth">
	<div class="voting-booth-container">
		<div class="voting-booth-controls">
			<div id="votingSpinner" class="voting-in-progress text-center"><span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> Voting &hellip;&hellip;</div>
			<div class="can-vote text-center">
				<div class="btn-group">
					<button class="btn btn-default vote-yes">YES</button>
					<button class="btn btn-default vote-no">NO</button>
				</div>
			</div>
			<div class="already-voted">
				<div class="vote-tally text-center">YES: <span class="yes-tally">30%</span> / NO: <span class="no-tally">70%</span></div>
				<div id="citizen-voted-yes" class="citizen-voted text-center">You voted: <span class="citizen-vote-value">YES</span></div>
				<div id="citizen-voted-no" class="citizen-voted text-center">You voted: <span class="citizen-vote-value">NO</span></div>
				<div id="citizen-vote-pending" class="text-center voting-info-text">Your vote will be counted after you validate your email</div>
			</div>
			<div id="votingClosed" class="citizen-voted text-center">Voting closed</div>
			<div class="question-details">
				<button type="button" class="btn btn-default btn-block">More Details&hellip;</button>
			</div>
			<div class="voting-email-pending text-center voting-info-text">
				Email validation pending&hellip;
			</div>
		</div>
	</div>
</div>


<hr/>
<div class="aq-cont container-fluid q-cont-static">
	<div class="row">
		<div class="col-md-9 container-fluid" >
			<div class="section-title row">Recently archived questions..</div>
			<div class="row">
				[#assign cnt=rand(1,7)]
				[#list last3ArchivedQuestions as q]
				<div id="aq${q.id}" class="q aq col-md-4 q-bg-${(cnt)}" data-q-id="${q.id}">
						<div class="q-title">${q.title}</div>
						<div class="aq-votes-tally">
							<div class="q-votes"><span>${q.votesCount}</span> votes</div>
							<div class="q-tally"><span>Yes: ${((q.voteTally)*100)?string["0.##"]}% <strong> — </strong> No: ${((1-(q.voteTally))*100)?string["0.##"]}%</span></div>
						</div>
						<blockquote class="aq-milestones">
							[#if q.archivedOn??]<footer>Archived on ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</footer>[/#if]
							<footer>Published on ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</footer>
						</blockquote>
						<div class="trailing-space"><br/></div>
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
						<li>Write <a href="http://www.europarl.europa.eu/meps/${preferredLang.code}/map.html" target="_new">your MEPs</a> and tell them your position on the discussed issues</li>
						<li>Join Campains and Movements of like-minded citizen</li>
					</ul>
			</div>
		</div>
	</div>
</div>

<div class="nav-buffer">
	&nbsp;
</div>

<script type="text/javascript">
	var App = {
		reqURL: '${reqURL}',
		reqURI: '${reqURI}',
		reqHostname: '${reqHostname}',
		reqQuery: '${reqQuery}',
		isMobileAgent: ${isMobileAgent?c},
		validatedCitizen: ${validatedCitizen?c},
		pendingValidation: ${pendingValidation?c},
		questions: {
			'na': {}
			[#list questions as q]
			,${q.id}: {votes:${q.votesCount},[#if q.voted??]voted:${q.voted},[/#if]archived:${q.arch?c},canVote:${q.canVote?c},voteTally:${q.voteTally}}
			[/#list]
		}
	};
</script>
