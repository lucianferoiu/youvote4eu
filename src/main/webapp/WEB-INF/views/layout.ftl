[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[#setting url_escaping_charset='UTF-8']
<!DOCTYPE html>
<html lang="[#if preferredLang??]${preferredLang.code}[#else]en[/#if]">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<link rel="shortcut icon" href="${context_path}/img/favicon.ico" type="image/x-icon">
		<title>[@yield to="title"/]</title>
		<link href='${context_path}/css/flatstrap.min.css' rel="stylesheet" media="screen">
		<link href='${context_path}/css/yv4eu.css' rel="stylesheet" media="screen">
		[@yield to="header_css"/]
		<!--[if lt IE 9]>
			<script src="${context_path}/js/html5.js"></script>
			<script src="${context_path}/js/json2.js"></script>
		<![endif]-->
	</head>
<body>
	
	[#include "header.ftl"]

	${page_content}

	[#include "footer.ftl"]
	[#include "home/validate.ftl"]
	
	<script src='${context_path}/js/jquery.min.js'></script>
	<script src='${context_path}/js/aw.js'></script>
	<script src='${context_path}/js/flatstrap.min.js'></script>
	[@yield to="footer_script"/]
</body>
</html>
