[#ftl encoding='UTF-8']

<div class="navbar navbar-default navbar-fixed-top container-fluid" role="navigation">
	<div class="row">
		<div class="col-md-3 col-sm-4">
			<div class="logo logo-big visible-md-block visible-lg-block"></div>
			<div class="logo logo-sml visible-sm-block"></div>
			<div class="brand-sm visible-sm-block">You VOTE for EU</div>
		</div>
		<div class="col-md-9 col-sm-8">
			<div class="container">
				<div class="row visible-md-block visible-lg-block">
					<div class="col-sm-12 slogan">
						Show the MEPs how to vote!
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 navbar-header">
						<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-filters" aria-expanded="false">
							<span class="sr-only">Toggle navigation</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
						<div class="logo logo-sml visible-xs-block"></div>
						<div class="brand-xs visible-xs-block">You VOTE for EU</div>
					</div>
					<div class="collapse navbar-collapse col-xs-12" id="navbar-filters">
						<ul class="nav navbar-nav">
							<li class="hidden-sm [#if activeFilter=='popular'] active[/#if]"><a href="/list/popular">Popular</a></li>
							<li class="hidden-sm [#if activeFilter=='newest'] active[/#if]"><a href="/list/newest">Newest</a></li>
							<li class="hidden-sm [#if activeFilter=='archived'] active[/#if]"><a href="/list/archived">Archived</a></li>
							<!-- collapsed menu filters for rather small resolutions -->
							<li class="dropdown visible-sm-block ">
								<a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false">Filters <span class="caret"></span></a>
								<ul class="dropdown-menu multi-level">
									<li class="[#if activeFilter=='popular'] disabled[/#if]"><a href="/list/popular">Popular</a></li>
									<li class="[#if activeFilter=='newest'] disabled[/#if]"><a href="/list/newest">Newest</a></li>
									<li class="[#if activeFilter=='archived'] disabled[/#if]"><a href="/list/archived">Archived</a></li>
									<li class="dropdown-submenu ">
										<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
											aria-expanded="false">Filter by tag: </span></a>
										<ul class="dropdown-menu">
											[#list tags as tg]
											<li class="[#if filterTagId??][#if filterTagId==tg.id]active[/#if][/#if]"><a href="/list/tag/${tg.id}"># ${tg.text} <span class="badge">${tg.count}</span></a></li>
											[/#list]
										</ul>
									</li>
								</ul>
							</li>
							<!-- /menu filters -->
							<li class="dropdown hidden-sm [#if activeFilter=='tag']active[/#if]">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false">Filter by tag <span class="caret"></span></a>
								<ul class="dropdown-menu">
									[#list tags as tg]
									<li class="[#if filterTagId??][#if filterTagId==tg.id]active[/#if][/#if]"><a href="/list/tag/${tg.id}" ># ${tg.text} <span class="badge">${tg.count}</span></a></li>
									[/#list]
								</ul>
							</li>
							<form class="navbar-form navbar-left" action="?" method="get" accept-charset="utf-8">
								<div class="form-group">
									<div class="input-group">
										<input type="text" class="form-control" id="searchKeyword" name="searchKeyword" placeholder="search by keyword" role="search"
											 value="${searchKeyword!}"><span class="input-group-btn"><button class="btn btn-default" type="submit"><span
												 class="glyphicon glyphicon-search"></button></span>
									</div>
								</div>
							</form>
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"><span class="">[#if preferredLang??]${preferredLang.native}[#else]English[/#if]</span> <span class="caret"></span></a>
								<ul class="dropdown-menu scrollable-menu">
									[#list langs as lg]
										<li [#if preferredLang??][#if preferredLang.code==lg.code]class="disabled"[/#if][/#if]><a href="/lang/${lg.code}">${lg.native}</a></li>
									[/#list]
								</ul>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>
