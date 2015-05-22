[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${context_path}/platform/home">You Vote for Europe - Platform Partners</a>
		</div>
		<!-- authenticated partner -->
		<div id="navbar" class="navbar-collapse collapse pull-right">
			<ul class="nav navbar-nav">
				<li class="[#if crtMenu=='home']active[/#if]"><a href="${context_path}/platform/home">Home</a></li>
				<li class="[#if crtMenu=='questions']active[/#if]"><a href="${context_path}/platform/questions">Questions</a></li>
				<li class="[#if crtMenu=='statistics']active[/#if]"><a href="${context_path}/platform/statistics">Statistics</a></li>				
				<li class="[#if crtMenu=='partners']active[/#if]"><a href="${context_path}/platform/partners">Partners</a></li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Hello ${session.authPartner.email} <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="${context_path}/platform/home/my_questions">My Questions</a></li>
							<li><a href="${context_path}/platform/home/preferences">Preferences</a></li>
							<li><a href="${context_path}/platform/home/help">Help</a></li>
							<li class="divider"></li>
							<li><a href="${context_path}/platform/auth/signout">Sign out</a></li>
						</ul>
					</li>
			</ul>
		</div>
	</div>
</nav>

