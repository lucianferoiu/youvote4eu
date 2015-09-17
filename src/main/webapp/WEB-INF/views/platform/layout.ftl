[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[#setting url_escaping_charset='UTF-8']
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${context_path}/img/favicon.ico" type="image/x-icon">
		<title>[@yield to="title"/]</title>
		<link href='${context_path}/css/bootstrap.min.css' rel="stylesheet" media="screen">
		-->
		[@yield to="header_css"/]
		<link href='${context_path}/css/youvote4eu.css' rel="stylesheet" media="screen">
		<!--[if lt IE 9]>
			<script src="${context_path}/js/html5.js"></script>
			<script src="${context_path}/js/json2.js"></script>
		<![endif]-->
	</head>
<body>
	<!-- header -->
	[#include "navbar.ftl"]

	<!-- main-content -->
	${page_content}
	<!-- /main-content -->

	<!-- footer -->
	[#include "../footer.ftl"]

	<script src='${context_path}/js/jquery.min.js'></script>
	<script src='${context_path}/js/bootstrap.min.js'></script>
	<script src='${context_path}/js/angular.min.js'></script>
	[@yield to="footer_script"/]
</body>
</html>
