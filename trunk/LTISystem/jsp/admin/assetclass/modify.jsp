<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Asset Class Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
	<br>
	<s:label>Merge two asset classes to one</s:label>
	<s:form action="ModifyAssetClass" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<tr></tr>
			<input type="hidden" name="action" value="Merge"/>
			<s:textfield name="originalID" label="Original ID " />
			<s:textfield name="targetID" label="Target ID " />
 			<s:submit></s:submit>
 		</table>
	</s:form>
	<br>
	<s:label>Catalogue asset classes</s:label>
	<s:form action="ModifyAssetClass" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<tr></tr>
			<input type="hidden" name="action" value="Catalogue"/>
			<s:textfield name="originalID" label="Original ID " />
			<s:textfield name="targetID" label="Target ID " />
 			<s:submit></s:submit>
 		</table>
	</s:form>
	</body>
</html>
