<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="10%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/group" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Group Manager</s:a>		
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_view" namespace="/jsp/admin/group" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="ID" value="ID"/>
				</s:url>
				<s:a href="%{url_view}">Edit Group</s:a>				
			</td>			
			<td width="10%">
				<s:url action="Main.action" id="url_main1" namespace="/jsp/admin/group/portfolio" includeParams="none">
					<s:param value="ID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main1}">Portfolios</s:a>				
			</td>	
			<td width="10%">
				<s:url action="Main.action" id="url_main2" namespace="/jsp/admin/group/strategy" includeParams="none">
					<s:param value="ID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main2}">Strategies</s:a>				
			</td>	
			<td width="10%">
				<s:url action="Main.action" id="url_main3" namespace="/jsp/admin/group/user" includeParams="none">
					<s:param value="ID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main3}">Users</s:a>				
			</td>											
			<td width="10%">
				<s:url action="Save.action" id="url_create1" namespace="/jsp/admin/group/portfolio" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="ID"/>
				</s:url>
				<s:a href="%{url_create1}">Add a Portfolio</s:a>				
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_create2" namespace="/jsp/admin/group/strategy" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="ID"/>
				</s:url>
				<s:a href="%{url_create2}">Add a Strategy</s:a>				
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_create3" namespace="/jsp/admin/group/user" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="ID"/>
				</s:url>
				<s:a href="%{url_create3}">Add a User</s:a>				
			</td>
			<td>
			</td>
		</table>	
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Edit Group</p>
	<s:actionmessage/>
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid; height:auto" cellSpacing=0 cellPadding=3 width="95%" align="left">

			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield name="ID" label="ID " disabled="true"/>
			<s:textfield name="name" label="Name "/>
			<s:textfield name="description" label="Description"/>
			<s:iterator value="#request.roles">
			<tr>
				<td>
					<s:property value="key"/>
				</td>
				<td>
					<select name='roles.<s:property value="key"/>'>
						<option value="true" <s:if test="value==true">selected</s:if> >true</option>
						<option value="false" <s:if test="value==false">selected</s:if> >false</option>
					</select>
				</td>
			</tr>
			</s:iterator>
			
 			<s:submit>Save</s:submit>
 		</table>

	</s:form>
   
	</body>
</html>
