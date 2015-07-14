[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="header_css"]<link href='${context_path}/css/jquery-ui.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/jquery-ui.min.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/archived.js'></script>[/@content]

<div class="nav-buffer">
	&nbsp;
</div>

<div class="container-fluid">
	<div class="row">
		<div class="arch-q-cont col-sm-8">
			<div class="arch-q-title">
				${questionTitle}
			</div>
			<div class="arch-q-desc">
				<blockquote>
				<p>${questionDescription}</p>
				<footer>Published on ${question.open_at?string["dd/MM/yyyy HH:mm"]}</footer>
				<footer>Closed on ${question.closed_at?string["dd/MM/yyyy HH:mm"]}</footer>
				<footer>Archived on ${question.archived_at?string["dd/MM/yyyy HH:mm"]}</footer>
				</blockquote>
			</div>
			<div class="arch-q-content">
				${questionHtmlContent}
			</div>
			<div class="arch-q-votes container-fluid">
				<div class="row">
					<div class="arch-q-vote-citizen col-sm-6">
						<div class="arch-q-citizen-votes text-center">
							Votes cast by citizen: ${question.popular_votes}
						</div>
						<div class="arch-q-citizen-tally text-center">
							<span class="label label-success text-left">Yes: ${((question.popular_vote_tally)*100)?string["0.##"]} %</span>&nbsp;
							<span class="label label-danger text-right">No: ${((1-(question.popular_vote_tally))*100)?string["0.##"]} %</span>
						</div>
						<hr/>
						<div class="arch-q-citizen-chart">
							[chart]
						</div>
						<hr/>
						<div class="arch-q-citizen-dist">
							[small Europe map]
						</div>
					</div>
					<div class="arch-q-vote-offcial col-sm-6">
						<div class="container-fluid">
							<div class="row">
								<div class="col-xs-6 arch-q-vote-offcial-parliament text-center">
									<strong>European Parliament decision</strong>
									<br/>
									<br/>
									[#if question.parliament_vote_tally??]
										[#if (question.parliament_vote_tally>=0.5) ]
											<span class="text-center arch-q-vote-tally">Yes (${((question.parliament_vote_tally)*100)?string["0.##"]}%)</span>
										[#else]
											<span class="text-center arch-q-vote-tally">No (${((1-(question.parliament_vote_tally))*100)?string["0.##"]}%)</span>
										[/#if]
									[/#if]
									<br/>
									<blockquote>[#if question.parliament_vote_link??]Access the decision details 
										<a href="${question.parliament_vote_link}" target="_new">here</a>[/#if]
									[#if question.parliament_voted_on?? ]<footer>Decision taken on 
										${question.parliament_voted_on?string["dd/MM/yyyy"]}</footer>[/#if]
									</blockquote>
								</div>
								<div class="col-xs-6 arch-q-vote-offcial-council text-center">
									<strong>European Council decision</strong>
									<br/>
									<br/>
									[#if question.council_vote_tally??]
										[#if (question.council_vote_tally>=0.5) ]
											<span class="text-center arch-q-vote-tally">Yes (${((question.council_vote_tally)*100)?string["0.##"]}%)</span>
										[#else]
											<span class="text-center arch-q-vote-tally">No (${((1-(question.council_vote_tally))*100)?string["0.##"]}%)</span>
										[/#if]
									[/#if]
									<br/>
									<blockquote>[#if question.council_vote_link??]Access the decision details 
										<a href="${question.council_vote_link}" target="_new">here</a>[/#if]
									<br/>
									[#if question.council_voted_on?? ]<footer>Decision taken on 
										${question.council_voted_on?string["dd/MM/yyyy"]}</footer>[/#if]
									</blockquote>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-offset-3 col-xs-6 arch-q-vote-offcial-commission text-center">
									<strong>European Commission decision</strong>
									<br/>
									<br/>
									[#if question.commission_decision??]
										[#if (question.commission_decision) ]
											<span class="text-center arch-q-vote-tally">Yes</span>
										[#else]
											<span class="text-center arch-q-vote-tally">No</span>
										[/#if]
									[#else]
										<span class="text-center arch-q-vote-tally">[#if question.commission_decided_on?? ]Undecided/Posponed[/#if]</span>
									[/#if]
									<br/>
									<blockquote>[#if question.commission_decision_link??]Access the decision details 
										<a href="${question.commission_decision_link}" target="_new">here</a>[/#if]
									<br/>
									[#if question.commission_decided_on?? ]<footer>Decision taken on 
										${question.commission_decided_on?string["dd/MM/yyyy"]}</footer>[/#if]
									</blockquote>
								</div>
							</div>
						</div>
					</div>
				</div>
					<div class="row">
						<div class="arch-q-vote-citizen col-sm-6">
							<div class="arch-q-vote-citizen-concl text-center">
								[#if question.popular_vote_tally?? ]
									[#if (question.popular_vote_tally>=0.5) ] YES [#else] NO  [/#if]
								[#else]
									<span class="muted">?</span>
								[/#if]
							</div>
						</div>
						<div class="arch-q-vote-official col-sm-6">
							<div class="arch-q-vote-official-concl text-center">
								[#if question.official_vote_tally?? ]
									[#if (question.official_vote_tally>=0.5) ] YES [#else] NO [/#if]
								[#else]
									<span class="muted">?</span>
								[/#if]
							</div>
						</div>
					</div>
				<hr/>
				<div class="row">
					<div class="col-xs-offset-1 col-xs-10">
						<strong>Conclusion:</strong>
						<br/>
						<em>${question.archival_conclusion}</em>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-offset-1 col-xs-10">
						<hr/>
						Not happy with your representatives' decision?
					</div>
				</div>
				<div class="row">
					<div class="col-xs-offset-1 col-xs-5">
						Tell <a href="#">them</a>!
					</div>
					<div class="col-xs-5">
						Join <a href="#">movement/campaign</a>.
					</div>
				</div>
			</div>
		</div>
		<div class="arch-q-sidebar col-sm-4">
			<div class="panel panel-default"  style="overflow-y:scroll">
				<div class="panel-heading">More archived subjects..</div>
				<div class="panel-body">
					[#list moreArchivedQuestions as q]
					<div class="arch-q-summary" data-q-id="${q.id}" data-q-archived="yes">
							<div class="arch-q-s-title">${q.title}</div>
							<div class="arch-q-s-desc">${q.description}</div>
							[#if q.archivedOn??]<div class="arch-q-s-date"><blockquote>Archived: ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</blockquote></div>[/#if]
							<div class="container-fluid arch-q-s-vote-tally">
								<div class="row">
									<div class="col-xs-6">
										Citizens decision
									</div>
									<div class="col-xs-6">
										Official decision
									</div>
								</div>
								<div class="row">
									<div class="col-xs-4 col-xs-offset-1">
										[#if q.voteTally?? ]
											[#if (q.voteTally>=0.5) ]
												<span class="label label-success text-left">Yes (${((q.voteTally)*100)?string["0.##"]}%)</span>&nbsp;
											[#else]
												<span class="label label-danger text-right">No (${((1-(q.voteTally))*100)?string["0.##"]}%)</span>
											[/#if]
										[#else]
											<span class="text-right muted">?</span>
										[/#if]
									</div>
									<div class="col-xs-4 col-xs-offset-1">
										[#if q.officialVoteTally??]
											[#if (q.officialVoteTally>=0.5) ]
												<span class="label label-success text-left">Yes (${((q.officialVoteTally)*100)?string["0.##"]}%)</span>&nbsp;
											[#else]
												<span class="label label-danger text-right">No (${((1-(q.officialVoteTally))*100)?string["0.##"]}%)</span>
											[/#if]
										[#else]
											<span class="text-right muted">?</span>
										[/#if]
									</div>
								</div>
							</div>
					</div>
					<hr/>
					[/#list]
				</div>
			</div>
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
		isMobileAgent: ${isMobileAgent?c},
		validatedCitizen: ${validatedCitizen?c},
		pendingValidation: ${pendingValidation?c}
	};
</script>
[/@content]