[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="header_css"]<link href='${context_path}/css/jquery-ui.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/jquery-ui.min.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/js/highmaps.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/js/eu.js'></script>[/@content]
[@content for="header_css"]<link href='${context_path}/css/flags.css' rel="stylesheet">[/@content]
[@content for="header_css"]<link href='${context_path}/css/bootstrap-select.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/bootstrap-select.min.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/question.js'></script>[/@content]


<div class="nav-buffer">
	&nbsp;
</div>

<div class="container-fluid">
	<div class="row">
		<div class="pub-q-cont col-md-8">
			<div class="pub-q-title">
				${questionTitle}
			</div>
			<div class="pub-q-desc">
				<blockquote>
					<p>${questionDescription}</p>
					<footer>Published on ${question.open_at?string["dd/MM/yyyy HH:mm"]}</footer>
				</blockquote>
			</div>
			<div class="pub-q-info">
				
			</div>
			<div class="pub-q-content">
				${questionHtmlContent}
			</div>
			<div class="pub-q-votes container-fluid">
				${question.popular_votes} votes and counting..
				<br/>
				<div id="votesDistributionEU" class="pub-q-vote-distribution">
				
				</div>
			</div>
		</div>
		<div class="pub-q-sidebar col-md-4">
			<div class="panel panel-default">
				<div class="panel-heading">Vote now!</div>
				<div class="panel-body">
					[#if canVote]
						<form class="form container-fluid" action="/cast/vote" method="post" data-toggle="validator" role="form">
							<input type="hidden" name="qId" value=${question.id} />
						[#if validatedCitizen]
							<br/>
						[#else]
							[#if pendingValidation]
								<p>Email validation pending&hellip;</p>
								<p>Check your email and click the validation link we sent you, so your vote may be counted.</p>
							[#else]
								<div class="form-group">
									<label for="citizen-email" class="">Your Email</label>
									<div class="">
										<div class="input-group">
											<span class="input-group-addon">@</span>
											<input id="citizen-email" name="citizen-email" type="email" class="form-control" required>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="citizen-country" class="">Your Country</label>
									<div class="">
									<select class="selectpicker show-tick" name="citizen-country" data-size="7" data-live-search="true" data-show-subtext="true" data-width="auto">
										[#list euCountries as c] <option value="${c.code}" data-content="<img src='/img/blank.gif' class='flag flag-${c.code}'/>&nbsp;<span class='text-muted'>[${c.code?upper_case}]</span>&nbsp;&nbsp;${c.label} " [#if (c.code==guessedCountry)] selected="selected" [/#if] data-subtext="${c.code?upper_case}">${c.label}</option>[/#list]
									</select>
									</div>
								</div>
							[/#if]
						[/#if]
						<div class="row">
							<div class="btn-group can-vote col-xs-12" role="group">
								<button class="btn btn-success vote-yes col-xs-6" type="submit" name="voteValue" value="1">YES</button>
								<button class="btn btn-danger vote-no col-xs" type="submit" name="voteValue" value="0">NO</button>
							</div>
						</div>
						
						</form>
					[#else]
						[#if pendingValidation]
							<p>Email validation pending&hellip;</p>
							<p>Check your email and click the validation link we sent you, so your vote may be counted.</p>
						[/#if]
						<div class="q-already-voted">
							<div class="q-vote-tally text-center">
								<p>Vote tally so far:<br/></p>
								YES: <span class="yes-tally">${((question.popular_vote_tally!0)*100)?string["0.##"]}</span> 
								/ 
								NO: <span class="no-tally">${((1-(question.popular_vote_tally!0))*100)?string["0.##"]}</span>
							</div>
							[#if voted??]
								[#if voted==1]
								<div class="q-citizen-voted text-center">You voted: <span class="citizen-vote-value">YES</span></div>
								[/#if]
								[#if voted==0]
								<div class="q-citizen-voted text-center">You voted: <span class="citizen-vote-value">NO</span></div>
								[/#if]
							[/#if]
						</div>
						
					[/#if]
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">Can I do more?</div>
				<div class="panel-body">
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">Statistics</div>
				<div class="panel-body">
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<br/><br/><br/>
	</div>
</div>


[@content for="footer_script"]
<script type="text/javascript">
	var App = {
		reqURL: '${reqURL}',
		reqURI: '${reqURI}',
		reqHostname: '${reqHostname}',
		reqQuery: '${reqQuery}',
		isMobileAgent: ${isMobileAgent?c},
		validatedCitizen: ${validatedCitizen?c},
		pendingValidation: ${pendingValidation?c}
		//
		,qId: ${question.id}
	};
</script>
[/@content]