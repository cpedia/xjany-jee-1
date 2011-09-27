<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Customize Page Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="adjHiehgt()">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/regioncustomize" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Region Customize Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/regioncustomize" includeParams="none">
				</s:url>
				<s:a href="%{url_create}">Create A New Region Customize</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="subtitle">Edit Customize Page</p>
		<s:actionmessage/>
		<s:form method="post" namespace="/jsp/admin/regioncustomize">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:hidden name="action"></s:hidden>
				<s:hidden name="ID" value="%{ID}"></s:hidden>
				<s:textfield name="regionCustomize.ID" label="ID " disabled="true"/>
				<s:textfield name="regionCustomize.PageName" label="Page Name "/>
				<!--  <s:textfield name="regionCustomize.RegionName" label="Region Name "/> -->
				<s:select list="#request.positions" label="Region Name" name="regionCustomize.RegionName"></s:select>
				<s:select list="#request.cps" label="Region Content" listKey="Name" listValue="Name" name="regionCustomize.RegionContent"></s:select>
				<s:select list="#request.groups" name="regionCustomize.GroupID" label="Group ID" listKey="ID" listValue="Name"></s:select>
				<s:if test="%{action=='create'}">
					<s:submit action="Save" >Save</s:submit>
				</s:if>
				<s:else>
					<s:submit action="Update">Save</s:submit>
				</s:else>
		</s:form>
   			
	</body>
</html>
