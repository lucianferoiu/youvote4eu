<#setting url_escaping_charset='ISO-8859-1'>
<!DOCTYPE html>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><@yield to="title"/></title>
    <link href="${context_path}/css/bootstrap.min.css" rel="stylesheet" media="screen">
  </head>
  <body>
	<#include "header.ftl" >

	${page_content}
    
    <#include "footer.ftl" >
    
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="${context_path}/js/bootstrap.min.js"></script>
  </body>
</html>
