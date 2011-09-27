<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Main Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
	</head>
	<body>
		<center>
			Error!<br>
			<s:url action="Main.action" id="url_main">
			</s:url>
			<s:a href="%{url_main}">Go Back</s:a>	
		</center>
	</body>
</html>
