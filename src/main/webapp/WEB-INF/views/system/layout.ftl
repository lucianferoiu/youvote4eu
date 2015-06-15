[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[#setting url_escaping_charset='ISO-8859-1']
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${context_path}/img/favicon.ico" type="image/x-icon">
		<title>[@yield to="title"/]</title>
		<link href='${context_path}/css/flatstrap.min.css' rel="stylesheet" media="screen">
		<!--[if lt IE 9]>
			<script src="${context_path}/js/html5.js"></script>
			<script src="${context_path}/js/json2.js"></script>
		<![endif]-->
	</head>
<body>
	
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${context_path}/home">You Vote For Europe</a>
			</div>
			<!-- authenticated partner -->
			<div id="navbar" class="navbar-collapse collapse pull-right">
				<ul class="nav navbar-nav">
					<li class=""><a href="${context_path}/home">Public</a></li>
					<li role="presentation" class="divider"></li>
					<li class=""><a href="${context_path}/platform/home">Partners Platform</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">
		<br/><br/>
	</div>
	
	<div class="container">
		${page_content}
	</div>

	[#include "../footer.ftl"]

	<script src='${context_path}/js/jquery.min.js'></script>
	<script src='${context_path}/js/aw.js'></script>
	<script src='${context_path}/js/flatstrap.min.js'></script>
	<script src='${context_path}/js/angular.min.js'></script>
	[@yield to="footer_script"/]
</body>
</html>
