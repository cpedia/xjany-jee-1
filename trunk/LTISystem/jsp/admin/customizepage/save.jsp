<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>


<html>
	<head>
		<title>Customize Page Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<script type="text/javascript" src="../../images/jquery-1.2.6.pack.js"></script>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<script src="images/save.js"></script>
		<style>
		
		</style>

	</head>
	<body onload="adjHiehgt()">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/customizepage" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Customize Page Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/customizepage" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Customize Page</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="title"><s:property value="%{title}"/></p>
		<p class="subtitle">Edit Customize Page</p>
		<s:actionmessage/>
		<s:form id="dataform" action="Save" method="post">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:hidden id="Action" name="action" value="%{action}"/>
				<s:hidden name="ID" value="%{ID}"/>

				<s:textfield name="ID" label="ID " disabled="true"/>

				<s:textfield id="Name" name="name" label="Name " required="true"/><a href="../../ajax/CustomizePage.action?pageName=<s:property value='name'/>" target="_blank">View this page in browser</a>

				<s:fielderror id="NameError" name="NameError"></s:fielderror>

				<s:textarea name="functions" id="Functions" label="Functions " cols="80" rows="8"></s:textarea>

				<s:textarea name="code" id="Code" label="Code " cols="80" rows="8"></s:textarea>

				<td width="10%"><s:text name="Template:"></s:text></td>
				<td id="Template_text" width="50%" height="20" style="border:1px #8DB6CD solid" height="50"><s:property value="template" escape="false"/>&nbsp;</td>
				<td id="Template_FCK" width="50%">
				<table width="100%">
				<td>
				<div><input type="hidden" id="Template" name="Template" value='<s:property value="template" escape="false"/>'><input type="hidden" id="Template___Config" value=""><iframe id="Template___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no"></iframe></div>
				</td>
				</table>
				</td>
				<td>
				<input type="button" onclick="showTemplateFck()" id="Template_edit" value="Edit">
				</td>
				</tr>
				<s:submit id="Saveas" value='SaveAs' onclick='var Name=prompt("Filling the new name","");
			if(Name)
			{
			$("#Action").val("saveas");
	        	$("#Name").val(Name);
	        	return true;
	        }				
	        else
	            return false;'/>
					
				<s:submit id="submit" value="Submit"/>
	
		</s:form>

	</body>
</html>
