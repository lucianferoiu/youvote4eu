[#ftl]
<div class="navbar navbar-default navbar-fixed-top container-fluid hidden-xs nav-block" role="navigation">
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
		<div class="prefs">
			<form class="search navbar-form">
					<div class="input-group col-sm-3 pull-right">
						<input type="text" class="form-control" id="questionSearch" placeholder="search by keyword" role="search">
						<span class="input-group-btn">
							<button class="btn btn-default" type="button"><span class="glyphicon glyphicon-search"></button>
						</span>
					</div>
					<div class="dropdown col-sm-1 pull-right">
						<button class="btn btn-default dropdown-toggle " type="button" id="lang-dropdown" 
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">English<span class="caret"></span></button>
						<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="lang-dropdown">
						[#list langs as lg]
							<li><a href="#"><span class="label label-default">${lg.code}</span> ${lg.label_native}</a></li>
						[/#list]
						</ul>
					</div>
			</form>
		</div>
	</div>
	<div class="row hidden-sm">
		<div class="tags col-sm-offset-2 col-sm-10">
			<ol class="breadcrumb pull-right" style="margin-bottom:2px;">
				<li><a href="#">#citizen <span class="badge">43</span></a></li>
				<li><a href="#">#privacy <span class="badge">37</span></a></li>
				<li><a href="#">#social <span class="badge">31</span></a></li>
				<li><a href="#">#environment <span class="badge">24</span></a></li>
				<li><a href="#">#policy <span class="badge">17</span></a></li>
				<li><a href="#">#environment <span class="badge">24</span></a></li>
				<li><a href="#">#policy <span class="badge">17</span></a></li>
				<li><a href="#">#trade <span class="badge">11</span></a></li>
				<li> 
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">...</a>
				<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="tags-dropdown">
					<li>
						<a href="#">#law <span class="badge">9</span></a>
					</li>
					<li>
						<a href="#">#emigration <span class="badge">9</span></a>
					</li>
					<li>
						<a href="#">#EP-2015 <span class="badge">7</span></a>
					</li>
					<li>
						<a href="#">#EP-2016 <span class="badge">2</span></a>
					</li>
				</ul>
				</li>
			</ol>
		</div>
	</div>
</div>

<div class="navbar navbar-default navbar-fixed-top container-fluid visible-xs-block" role="navigation">
	<div class="row">
		<div class="logo col-xs-4 navbar-brand">
			You Vote for EU
		</div>
		<div class="prefs">
			<form class="search navbar-form">
					<div class="col-xs-2">
						<select class="form-control">
							<option value="en" selected>en</option>
							<option value="fr">fr</option>
							<option value="de">de</option>
							<option value="ro">ro</option>
						</select>
					</div>
					<div class="input-group col-xs-6">
						<input type="text" class="form-control" id="questionSearch" placeholder="search by keyword" role="search">
						<span class="input-group-btn">
							<button class="btn btn-default" type="button"><span class="glyphicon glyphicon-search"></button>
						</span>
					</div>
			</form>
		</div>
	</div>
</div>

<div class="nav-buffer hidden-xs">
	&nbsp;
</div>