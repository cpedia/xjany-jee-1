<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<SCRIPT src="../images/jquery.latest.js" type="text/javascript" ></SCRIPT>
	</head>
	<body>	
	<p class="title"><s:property value="title"/></p>
	<p class="subtitle">Edit the menu of the system</p>
	<s:actionmessage/>
	<s:form id="editForm" method="post" namespace="/jsp/admin/header">
	<table>
		<tr>
			<td>
				<s:textarea cols="100" rows="30" cssStyle="width:99%;border:solid 1px" name="fileString"></s:textarea>
			</td>
		</tr>
		<tr>
			<td>
				<s:submit action="Save" value="Save"></s:submit>
			</td>
		</tr>
	</table>
	 </s:form>
	</body>
</html>
