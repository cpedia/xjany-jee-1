<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Role Main Page</title>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="adjHiehgt()">
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
		
	<p class="title">Role List</p>
	
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Name
				</td>	
				<td>
					FullName
				</td>		
					
				<td>
					Remove
				</td>																	
			</tr>
			<s:iterator value="#request.roles">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/group/role">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="name"/></s:a>
						
					</td>
					<td>
						<s:property value="fullName"/>
					</td>
					<td>
						<s:url action="Save.action" id="url_del" namespace="/jsp/admin/group/role">
							<s:param name="ID" value="ID"></s:param>
							<s:param name="action">delete</s:param>
						</s:url>
						<s:a href="%{url_del}">Remove</s:a>					
					</td>					

				</tr>
			</s:iterator>
			<tr>
				<td colspan="7">
	
				</td>
			</tr>

		</table>


		
	</body>
</html>
