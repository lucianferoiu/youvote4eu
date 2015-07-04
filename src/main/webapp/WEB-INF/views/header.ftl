[#ftl encoding='UTF-8']
<div class="navbar navbar-default navbar-fixed-top container-fluid hidden-xs hidden-sm nav-block" role="navigation">
	<div class="row">
		<div class="logo col-sm-2 navbar-brand">
			Logo<br/>
			Logo<br/>
			Logo
		</div>
		<div class="col-sm-5">
			<div class="slogan">
				Slogan
			</div>
		</div>
		<div class="dropdown input-group pull-right">
			<button class="btn btn-default dropdown-toggle " type="button" id="lang-dropdown" 
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">English<span class="caret"></span></button>
			<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="lang-dropdown">
			[#list langs as lg]
				<li><a href="#">${lg.native}</a></li>
			[/#list]
			</ul>
		</div>
	</div>
	<div class="row hidden-sm">
		<div class="tags col-sm-offset-2 col-sm-7">
			<ol class="breadcrumb pull-right" style="margin-bottom:2px;">
				<li><strong>Filter</strong>: &nbsp;&nbsp;<a href="?filter=newest">Newest</a></li>
				<li><a href="?filter=archived">Archived</a></li>
				[#list tags as tg]
				[#if (tg_index<3) ]
				<li># <a href="?tag=${tg.id}">${tg.text}</a> <span class="badge">${tg.count}</span></li>
				[/#if]
				[/#list]
				<li> 
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">more tags</a>..
				<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="tags-dropdown">
					[#list tags as tg]
					[#if (tg_index>=3) ]
					<li>
						<a href="?tag=${tg.id}"># ${tg.text} <span class="badge">${tg.count}</span></a>
					</li>
					[/#if]
					[/#list]
				</ul>
				</li>
			</ol>
		</div>
		<form class="input-group col-sm-3 pull-right" action="?" method="get" accept-charset="utf-8">
			<input type="text" class="form-control" id="searchKeyword" name="searchKeyword" placeholder="search by keyword" role="search" value="${searchKeyword!}">
			<span class="input-group-btn">
				<button class="btn btn-default" type="submit"><span class="glyphicon glyphicon-search"></button>
			</span>
		</form>
	</div>
</div>

<div class="navbar navbar-default navbar-fixed-top container-fluid visible-xs-block visible-sm-block" role="navigation">
	<div class="row">
		<div class="logo col-xs-7 navbar-brand">
			You Vote for EU
		</div>
		<div class="prefs col-xs-5" style="padding:8px 2px 2px 0px;">
				<div class="dropdown input-group pull-right">
					<button class="btn btn-default dropdown-toggle" type="button" id="lang-dropdown" 
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">English &nbsp;<span class="caret"></span></button>
					<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="lang-dropdown">
					[#list langs as lg]
						<li><a href="#">${lg.native} <span class="label label-default">${lg.code}</span></a></li>
					[/#list]
					</ul>
				</div>
		</div>
	</div>
	<div class="row">
		<div class="tags col-xs-9" style="padding: 8px 4px 2px 8px;">
			<ol class="breadcrumb" style="padding: 0px;">
				<li><a href="?filter=newest">Newest</a></li>
				<li><a href="?filter=archived">Archived</a></li>
				<li> 
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">by #tag</a>..
				<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="tags-dropdown">
					[#list tags as tg]
					<li><a href="?tag=${tg.id}"># ${tg.text} <span class="badge">${tg.count}</span></a></li>
					[/#list]
				</ul>
				</li>
			</ol>
		</div>
		<form action="?" method="get" accept-charset="utf-8" class="pull-right col-xs-3"  style="padding: 2px 4px 2px 8px;">
			<div class="input-group">
				<input type="text" class="form-control" id="searchKeyword" name="searchKeyword" placeholder="search" role="search" value="${searchKeyword!}">
			</div>
		</form>
	</div>
</div>

[#-- <div class="nav-buffer">
	&nbsp;
</div> --]