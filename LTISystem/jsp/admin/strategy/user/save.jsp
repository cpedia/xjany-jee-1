<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<html>
	<head>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />
		<SCRIPT src="../../images/jquery.latest.js" type="text/javascript" ></SCRIPT>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
	</head>
	<body onload="adjHiehgt()">
		<s:url action="Main" id="url_main" namespace="/jsp/admin/strategy" includeParams="none">
		</s:url>
		<p align="left"><s:a href="%{url_main}">Strategy Manager</s:a></p>
		<p>
			<s:actionerror/>
			<s:actionmessage/>
		</p>
		<s:form namespace="/jsp/admin/portfolio/user" theme="simple">
			<p><s:property value="strategy.ID"/> <s:property value="strategy.name"/></p>
			<p><s:textfield id="oldName" name="oldName" label="old name" readonly="true"></s:textfield></p>
			<s:hidden name="strategyID"></s:hidden>
			<p><s:textfield id="userName" name="userName" label="new name"></s:textfield></p>
			<p><s:submit action="Save" value="submit"></s:submit></p>
		</s:form>
	</body>
</html>