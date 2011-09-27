<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Indicator Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />		
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/indicator" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Indicator Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_view" namespace="/jsp/admin/indicator" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="ID" value="ID"/>
				</s:url>
				<s:a href="%{url_view}">Indicator</s:a>				
			</td>
			<td width="15%">
				<s:url action="Main.action" id="url_dailydata" namespace="/jsp/admin/indicator/dailydata" includeParams="none">
					<s:param name="indicatorID" value="ID"/>
				</s:url>
				<s:a href="%{url_dailydata}">Daily Datas</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/indicator/dailydata" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="indicatorID" value="ID"/>
				</s:url>
				<s:a href="%{url_create}">Create a Daily Data</s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Edit Indicator</p>
	<s:actionmessage/>			
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield name="ID" label="ID " disabled="true"/>
			<s:textfield name="symbol" label="Symbol "/>
			<s:textfield name="description" label="Description "/>
 			<s:submit>Save</s:submit>

	</s:form>
   
	</body>
</html>
