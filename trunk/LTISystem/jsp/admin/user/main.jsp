<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>User Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>

		<table class="nav" width="100%">
			<td width="16%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/user">
				</s:url>
				<s:a href="%{url_main}">User Manager</s:a>				
			</td>		
			<td width="16%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/user"  includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New User</s:a>				
			</td>
			<td width="17%">
				<s:url action="UserEmail_reportStatistics.action" id="url_reportStatistics" namespace="/jsp/admin/user"  includeParams="none">
				</s:url>
					<s:a href="%{url_reportStatistics}">EmailNotification Report</s:a>	
			</td>
			<td width="17%">
				<s:url action="UserProfile.action" id="url_userprofile" namespace="/jsp/admin/user"  includeParams="none">
				</s:url>
				<s:a href="%{url_userprofile}">User Profile</s:a>
			</td>
			<td width="17%">
				<s:url action="UserProfile.action" id="url_userprofile" namespace="/jsp/admin/user"  includeParams="none">
					<s:param name="action">payitem</s:param>
				</s:url>
				<s:a href="%{url_userprofile}">User PayItem</s:a>		
			</td>
			<td width="17%">
			    <s:url action="UserQuota.action" id="url_userquota" namespace="/jsp/admin/user" includeParams="none">
			     </s:url>
			     <s:a href="%{url_userquota}">User Quota</s:a>
			</td>
		</table>	
		<p class="title">User List</p>
		<table width="100%">
		<tr><td>
		<form action='UserDetail.action?action=Name' method="post">
			<h2>&nbsp;Search User(Name):&nbsp;&nbsp;<input type="text" name="userName" value=""/>
			<input type="submit" value="Go"/></h2>
		</form></td><td>
		<form action='UserDetail.action?action=Email' method="post">
			<h2>&nbsp;Search User(Email):&nbsp;&nbsp;<input type="text" name="Email" value=""/>
			<input type="submit" value="Go"/></h2>
		</form>
		</td></tr>
		</table>
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Name
				</td>	
				<td>
					password
				</td>		
				<td>
					enabled
				</td>		
				<td>
					authority
				</td>		
				<td>
					address
				</td>	
				<td>
					city
				</td>	
				<td>
					state
				</td>
				<td>
					ZIP/Postal Code
				</td>
				<td>
					country
				</td>
				<td>
					QQ
				</td>	
				<td>
					telephone
				</td>
				<td>
					EMail
				</td>

				<td>
					Remove
				</td>																																					
			</tr>		
			<s:iterator value="#request.users.items">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/user">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="userName"/></s:a>
						
					</td>
					<td>
						<s:property value="password"/>
					</td>
					<td>
						<s:property value="enabled"/>
					</td>	
					<td>
						<s:property value="authority"/>
					</td>	
					<td>
						<s:property value="address"/>
					</td>	
					<td>
						<s:property value="addressCity"/>
					</td>
					<td>
						<s:property value="addressState"/>
					</td>
					<td>
						<s:property value="addressZip"/>
					</td>
					<td>
						<s:property value="addressCountry"/>
					</td>
					<td>
						<s:property value="QQ"/>
					</td>	
					<td>
						<s:property value="telephone"/>
					</td>			
					<td>
						<s:property value="EMail"/>
					</td>		
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/user">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">delete</s:param>						
						</s:url>
						<s:a href="%{urladdr}">Remove</s:a>						
					</td>														
				</tr>
			</s:iterator>
		</table>
		<center>
		Change to
			<s:url action="Main.action" id="url_first">
				<s:param name="startIndex" value="#request.users.firstParameter.startIndex"></s:param>
			</s:url>
							
			<s:a href="%{url_first}">
				First
			</s:a>	
							
			<s:iterator value="#request.users.parameters10" id="parameter" status="st">
				<s:url action="Main.action" id="pageurl">
					<s:param name="startIndex" value="#parameter.startIndex"></s:param>
				</s:url>
				<s:if test="#request.users.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
				<s:a href="%{pageurl}">
					<s:property value="#parameter.startIndex/#request.users.pageSize+1"/>
				</s:a>
				<s:if test="#request.users.startIndex==#parameter.startIndex"></class></s:if>	
			</s:iterator>
			
			<s:url action="Main.action" id="url_last">
				<s:param name="startIndex" value="#request.users.lastParameter.startIndex"></s:param>
			</s:url>
							
			<s:a href="%{url_last}">
				Last
			</s:a>	
		<s:url action="Main.action" id="url_m" includeParams="none">
		</s:url>								
		Page
		<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
			Page <input type="text" name="startIndex" value=""/>
			<input type="hidden" name="pageSize" value='<s:property value="#request.users.pageSize"/>'/>
			<input type="submit" value="Go"/>
		<form>
		</center>	
		
	</body>
</html>
