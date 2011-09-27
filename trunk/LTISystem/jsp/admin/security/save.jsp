<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />		
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/security" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Security Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_view" namespace="/jsp/admin/security" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="ID" value="ID"/>
				</s:url>
				<s:a href="%{url_view}">Security</s:a>				
			</td>
			<td width="15%">
				<s:url action="Main.action" id="url_dailydata" namespace="/jsp/admin/security/dailydata" includeParams="none">
					<s:param name="securityID" value="ID"/>
				</s:url>
				<s:a href="%{url_dailydata}">Daily Datas</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/security/dailydata" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="securityID" value="ID"/>
				</s:url>
				<s:a href="%{url_create}">Create a Daily Data</s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Edit Security</p>
	<s:actionmessage/>			
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield name="ID" label="ID " disabled="true"/>
			<s:textfield name="symbol" label="Symbol "/>
			<s:textfield name="name" label="Name "/>
			<s:textfield name="classID" label="Class ID "/>
			<s:textfield name="currentPrice" label="Current Price "/>
			<s:textfield name="diversified" label="Diversified "/>
			<s:textfield name="securityType" label="Security Type "/>
			
 			<s:submit>Save</s:submit>
 			
	</s:form>
   
	</body>
</html>
