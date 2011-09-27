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
		<s:form action="Save" method="post">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:hidden id="Action" name="action" value="%{action}"/>
				<s:hidden name="ID" value="%{ID}"/>
				<s:hidden name="nofck" value="%{nofck}"></s:hidden>
				
				<s:textfield name="ID" label="ID " disabled="true"/>

				<s:textfield id="Name" name="name" label="Name " required="true"/>

				<s:fielderror id="NameError" name="NameError"></s:fielderror>

				<s:textarea name="functions" id="Functions" label="Functions " cols="80" rows="8"></s:textarea>

				<s:textarea name="code" id="Code" label="Code " cols="80" rows="8"></s:textarea>

				<s:textarea name="template" id="Template" label="Template " cols="80" rows="14"></s:textarea>
				<s:submit id="Saveas" value='SaveAs' onclick='var Name=prompt("Filling the new name","");
			if(Name)
			{
			$("#Action").val("saveas");
	        	$("#Name").val(Name);
	        	return true;
	        }				
	        else
	            return false;'/>
				<s:submit></s:submit>
	
		</s:form>
   			
	</body>
</html>
