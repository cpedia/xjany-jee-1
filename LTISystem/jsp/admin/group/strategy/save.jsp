<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group's Strategy Page</title>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />		
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
					<s:param name="ID" value="groupID"/>
				</s:url>
				<s:a href="%{url_view}">Edit Group</s:a>				
			</td>			
			<td width="10%">
				<s:url action="Main.action" id="url_main1" namespace="/jsp/admin/group/portfolio" includeParams="none">
					<s:param value="groupID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main1}">Portfolios</s:a>				
			</td>	
			<td width="10%">
				<s:url action="Main.action" id="url_main2" namespace="/jsp/admin/group/strategy" includeParams="none">
					<s:param value="groupID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main2}">Strategies</s:a>				
			</td>	
			<td width="10%">
				<s:url action="Main.action" id="url_main3" namespace="/jsp/admin/group/user" includeParams="none">
					<s:param value="groupID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main3}">Users</s:a>				
			</td>											
			<td width="10%">
				<s:url action="Save.action" id="url_create1" namespace="/jsp/admin/group/portfolio" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="groupID"/>
				</s:url>
				<s:a href="%{url_create1}">Add a Portfolio</s:a>				
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_create2" namespace="/jsp/admin/group/strategy" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="groupID"/>
				</s:url>
				<s:a href="%{url_create2}">Add a Strategy</s:a>				
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_create3" namespace="/jsp/admin/group/user" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="groupID"/>
				</s:url>
				<s:a href="%{url_create3}">Add a User</s:a>				
			</td>
			<td>
			</td>
		</table>	
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Edit Strategy  </p>
	<s:actionmessage/>		
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:hidden name="groupID" value="%{groupID}"/>
			<s:textfield name="groupID" label="Group ID "  disabled="true"/>
			<s:textfield name="resourceType" label="ResourceType" value="Strategy" disabled="true"/>
			<s:textfield name="roleID" label="Role ID"/>
			<s:textfield name="strategyID" label="Strategy ID "/>
 			<s:submit>Save</s:submit>
         </table>  
	</s:form>
    <br>
      <br>
        <br>
          <br>
          <p>  &nbsp;</p>
    <p>  role list:</p>      
    <p>  1 role_strategy_operation   </p>
    <p>  2 role_strategy_code 	</p>
    <p>  9 role_strategy_read 	  </p>
	</body>
</html>
