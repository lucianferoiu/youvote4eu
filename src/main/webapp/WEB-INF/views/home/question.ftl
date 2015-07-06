[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="header_css"]<link href='${context_path}/css/jquery-ui.min.css' rel="stylesheet">[/@content]
[@content for="footer_script"]<script src='${context_path}/js/jquery-ui.min.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/question.js'></script>[/@content]

<div class="nav-buffer">
	&nbsp;
</div>

<div class="container-fluid">
	<div class="row">
		<div class="pub-q-cont col-sm-8">
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
				<div class="row">
					<div class="pub-q-vote-stats col-sm-6">
						${question.popular_votes} votes so far..
					</div>
					<div class="pub-q-vote-distribution col-sm-6">
						[Europe map]
					</div>
				</div>
			</div>
		</div>
		<div class="pub-q-sidebar col-sm-4">
			<div class="panel panel-default">
				<div class="panel-heading">Vote now!</div>
				<div class="panel-body">
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
</div>


[@content for="footer_script"]
<script type="text/javascript">
(function() {
}());
</script>
[/@content]