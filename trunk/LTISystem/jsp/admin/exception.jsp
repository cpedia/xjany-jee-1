<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Exception</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
	</head>
	<body>
	<div align="left">
		<center>
			<s:url action="Main.action" id="url_main" >
			</s:url>
			<s:a href="%{url_main}">Go Back</s:a>	
		</center>
		<pre>
			<s:property value="exception.message"/>
			
			<s:property value="exceptionStack"/>
		</pre>
	</div>
	</body>
</html>
