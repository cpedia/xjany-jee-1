<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="adjHiehgt()">
		<table class="nav" width="100%">
			<td width="20%">
				<s:url action="Main" id="url_main" namespace="/jsp/admin/group">
				</s:url>
				<s:a href="%{url_main}">Group Manager</s:a>				
			</td>		
			<td width="20%">
				<s:url action="Save" id="url_create" namespace="/jsp/admin/group" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Group</s:a>				
			</td>
			<td>
			</td>
		</table>
	
	<p class="title">Group List</p>
	
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Name
				</td>	
				<td>
					Description
				</td>		
				<td>
					Portfolios
				</td>		
				<td>
					Strategies
				</td>		
				<td>
					Users
				</td>	
				<td>
					E-mails
				</td>	
				<td>
					Remove
				</td>																	
			</tr>
			<s:iterator value="#request.groups.items">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/group">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="name"/></s:a>
						
					</td>
					<td>
						<s:property value="description"/>
					</td>
					<td>
						<s:url action="Main.action" id="url_portfolio" namespace="/jsp/admin/group/portfolio">
							<s:param name="groupID" value="ID"></s:param>	
						</s:url>
						<s:a href="%{url_portfolio}">portfolios</s:a>
						
					</td>
					<td>
						<s:url action="Main.action" id="url_strategy" namespace="/jsp/admin/group/strategy">
							<s:param name="groupID" value="ID"></s:param>	
						</s:url>
						<s:a href="%{url_strategy}">strategies</s:a>					
						
					</td>
					<td>
						<s:url action="Main.action" id="url_user" namespace="/jsp/admin/group/user">
							<s:param name="groupID" value="ID"></s:param>	
						</s:url>
						<s:a href="%{url_user}">users</s:a>						
						<s:url action="createCSV.action" id="url_csv" namespace="/jsp/admin/group/user">
							<s:param name="groupID" value="ID"></s:param>	
						</s:url>
						<s:a href="%{url_csv}">IntoCSV</s:a>	
					</td>
					<td>
						<s:url action="Main.action" id="url_emails" namespace="/jsp/admin/group/emails">
							<s:param name="groupID" value="ID"></s:param>	
						</s:url>
						<s:a href="%{url_emails}">emails</s:a>						
						
					</td>
					<td>
						<s:url action="Save.action" id="url_del">
							<s:param name="ID" value="ID"></s:param>
							<s:param name="action">delete</s:param>
						</s:url>
						<s:a href="%{url_del}">Remove</s:a>					
						
					</td>					

				</tr>
			</s:iterator>
			<tr>
				<td colspan="8">
					<center>
					Change to
						<s:url action="Main.action" id="url_first">
							<s:param name="startIndex" value="#request.groups.firstParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_first}">
							First
						</s:a>	
										
						<s:iterator value="#request.groups.parameters10" id="parameter" status="st">
							<s:url action="Main.action" id="pageurl">
								<s:param name="startIndex" value="#parameter.startIndex"></s:param>
							</s:url>
							<s:if test="#request.groups.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
							<s:a href="%{pageurl}">
								<s:property value="#parameter.startIndex/#request.groups.pageSize+1"/>
							</s:a>
							<s:if test="#request.groups.startIndex==#parameter.startIndex"></class></s:if>	
						</s:iterator>
						
						<s:url action="Main.action" id="url_last">
							<s:param name="startIndex" value="#request.groups.lastParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_last}">
							Last
						</s:a>	
					<s:url action="Main.action" id="url_m" includeParams="none">
					</s:url>								
					Page
					<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						Page <input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.groups.pageSize"/>'/>
						<input type="submit" value="Go"/>
					<form>
					</center>				
				</td>
			</tr>

		</table>


		
	</body>
</html>
