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
		<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
		<!--[if lt IE 9]>
			<script src="${context_path}/js/html5.js"></script>
			<script src="${context_path}/js/json2.js"></script>
		<![endif]-->
	</head>
<body>
	[#include "header.ftl"]

${page_content}

	[#include "footer.ftl"]
	<script src='${context_path}/js/jquery.min.js'></script>
	<script src='${context_path}/js/aw.js'></script>
	<script src='${context_path}/js/flatstrap.min.js'></script>
	<script src='${context_path}/js/underscore.min.js'></script>
	<script src='${context_path}/js/backbone.min.js'></script>
	<script src='${context_path}/app/[@yield to="app_path"/].js'></script>
	[@yield to="footer_script"/]
</body>
</html>
