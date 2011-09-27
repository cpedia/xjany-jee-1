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
		
		<p class="title">Export Holiday</p>
		<s:form action="ExportHoliday.action" method="post" namespace="/jsp/admin/holiday">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:textfield id="Filename" name="filename" label="File Name "></s:textfield>
	 			<s:submit></s:submit>
		</s:form>
   
	   <script>		
			var defaultFile="allHoliday.csv";
			$('#Filename').val(defaultFile);
		</script>
	   
	</body>
</html>
