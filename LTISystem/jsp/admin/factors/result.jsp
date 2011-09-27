<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Information</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
	</head>
	<body>
		<center>
			Successfully!<br>
			<s:actionmessage/><br>
			<s:url value="factors.jsp" id="url_factors_main" >
				<s:param name="groupID" value="%{groupID}"></s:param>
			</s:url>
			<s:a href="%{url_factors_main}">Go Back</s:a>	
		</center>
	
	</body>
</html>
