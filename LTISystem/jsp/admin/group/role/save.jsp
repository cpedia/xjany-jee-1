<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Page</title>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="20%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/group/role">
				</s:url>
				<s:a href="%{url_main}">Role Manager</s:a>				
			</td>		
			<td width="20%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/group/role" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Role</s:a>				
			</td>
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Edit Role</p>
	<s:actionmessage/>
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">

			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield name="ID" label="ID " disabled="true"/>
			<s:textfield name="name" label="Name "/>
			<s:submit>Save</s:submit>
 		</table>

	</s:form>
   
	</body>
</html>
