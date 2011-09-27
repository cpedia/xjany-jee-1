<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Holiday Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />	
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/holiday">
				</s:url>
				<s:a href="%{url_main}">Holiday Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/holiday">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Holiday</s:a>				
			</td>
			<td width="15%">
				<s:url value="importholiday.jsp" id="url_import" namespace="/jsp/admin/holiday" includeParams="none">
				</s:url>
				<s:a href="%{url_import}">Import Holiday</s:a>				
			</td>
			<td width="15%">
				<s:url value="exportholiday.jsp" id="url_export" namespace="/jsp/admin/holiday" includeParams="none">
				</s:url>
				<s:a href="%{url_export}">Export Holiday</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="title">Edit Holiday</p>
		
		<s:form action="Save" method="post">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:hidden name="action" value="%{action}"/>
				<s:hidden name="ID" value="%{ID}"/>
				<s:textfield name="ID" label="ID " disabled="true"/>
				<s:textfield name="symbol" label="Symbol "/>
				<s:textfield name="description" label="Description "/>
				<s:textfield name="date" label="Date "/>
	 			<s:submit>Save</s:submit>
	
		</s:form>
   
	</body>
</html>
