<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>User Page</title>
		<script type="text/javascript" src="/LTISystem/jsp/images/tiny_mce/tiny_mce.js"></script>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="../../images/md5.js"></script>
			<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>	
		<script>
			function getMD5(s){
				if($('#oldpassword').val()==s){
					return s;
				}else{
					var md5=MD5(s)
					$('#oldpassword').val(md5);
					return md5;
				}
			}
			tinyMCE.init({
			    mode : "none",
			    theme : "advanced",
			    plugins : "save",
				theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,fontselect,fontsizeselect",
				theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,blockquote,link,unlink,anchor,image,|,forecolor,backcolor",
				theme_advanced_buttons3 : "",
				theme_advanced_toolbar_location : "top",
				theme_advanced_toolbar_align : "left",
				theme_advanced_statusbar_location : "bottom",
				save_onsavecallback : "save_for_mce"
			});
			$(function() {
				tinyMCE.execCommand('mceAddControl', false, "description");
			});
		</script>
		<STYLE type="text/css">
			textarea{
				width:500px;
				height:400px;
			}
		</STYLE>
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="20%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/user" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">User Manager</s:a>				
			</td>		
			<td width="20%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/user" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New User</s:a>				
			</td>
			<td width="20%">
				<s:url action="UserEmail_reportStatistics.action" id="url_reportStatistics" namespace="/jsp/admin/user"  includeParams="none">
				</s:url>
					<s:a href="%{url_reportStatistics}">EmailNotification Report</s:a>	
			</td>
			<td width="20%">
			<s:url action="UserProfile.action" id="url_userprofile" namespace="/jsp/admin/user"  includeParams="none">
				</s:url>
				<s:a href="%{url_userprofile}">User Profile</s:a>
			</td>
			<td width="20%">
				<s:url action="UserProfile.action" id="url_userprofile" namespace="/jsp/admin/user"  includeParams="none">
					<s:param name="action">payitem</s:param>
				</s:url>
				<s:a href="%{url_userprofile}">User PayItem</s:a>
			</td>
		</table>	
		<p class="title"><s:property value="%{title}"/></p>
		<p class="subtitle">Edit User</p>
		<s:actionmessage/>
		
		Once you left the editor of the password, the script will encode the plain text to md5 string immediately.
		<br>
		Created Date: <s:property value="createdDate"/>
		<s:form action="Save" method="post">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:hidden name="action" value="%{action}"/>
				<s:hidden name="ID" value="%{ID}"/>
				<s:textfield name="ID" label="ID " disabled="true"/>
				<s:textfield name="userName" label="User Name "/>
				<input type="hidden" id='oldpassword' value='<s:property value="password"/>'>
				<s:textfield name="password" label="Password" id='password' onblur="this.value=getMD5(this.value)"  />
				<s:textfield name="enabled" label="Enabled "/>
				<s:textfield name="authority" label="Authority "/>
				<s:textfield name="address" label="Address "/>
				<s:textfield name="QQ" label="QQ "/>
				<s:textfield name="telephone" label="Telephone "/>
				<s:textfield name="EMail" label="EMail"/>
				<s:textfield name="license" label="License "/>
				<s:textfield name="company" label="Company"/>
				<s:textarea name="description" label="Description" id="description"/>
				<tr>
				<td>Groups</td>
				<s:iterator value="groups" status="st">
				<td>
				<s:url id="groupUrl" action="Save" namespace="/jsp/admin/group">
					<s:param name="ID" value="ID"></s:param>	
					<s:param name="action">view</s:param>
				</s:url>
				<a href='<s:property value="groupUrl" escape="false"/>'><s:property value="name"/></a>
				</td>
				</s:iterator>
				</tr>
	 			<s:submit>Save</s:submit>
	
		</s:form>
   			
	</body>
</html>
