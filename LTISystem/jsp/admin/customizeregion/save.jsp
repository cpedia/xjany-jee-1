<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Customize Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="adjHiehgt()">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main" id="url_main" namespace="/jsp/admin/customizeregion" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Customize Region Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="View" id="url_create" namespace="/jsp/admin/customizeregion"  includeParams="none">
					<s:param name="ID">0</s:param>
					<s:param name="action">create</s:param>
					
				</s:url>
				<s:a href="%{url_create}">Create A Customize Page</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="subtitle">Edit Customize Page</p>
		<s:actionerror/>
		<s:actionmessage/>
		<s:form method="post" namespace="/jsp/admin/regioncustomize">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:hidden name="ID" value="%{ID}"/>
				<s:hidden name="regionCustomize.ID"/>
				<s:hidden name="action" value="%{action}"></s:hidden>
				<s:if test="CRPage.ID!=0">
					<s:textfield name="CRPage.PageName" label="Customize Page Name " disabled="true"/>
					<s:hidden name="CRPage.PageName"></s:hidden>
				</s:if>
				<s:else>
					<!--<s:textfield name="CRPage.PageName" label="Customize Page Name"></s:textfield>-->
					<s:select list="pages" listKey="ID" listValue="PageName" name="CRPage.PageID" label="Customize Page Name"></s:select>
				</s:else>
				<s:textfield name="CRPage.EastTitle" label="East Side Title"></s:textfield>
				<s:select list="cps" label="East Region Page" listKey="Name" listValue="Name" name="CRPage.eastRegionName"></s:select>
				<s:textfield name="CRPage.eastWidthStr" label="East Side Width"></s:textfield>
				<s:select list="groups" name="CRPage.EastGroupID" label="East Group ID" listKey="ID" listValue="Name"></s:select>
				<!--<s:textfield name="CRPage.eastRegionName" label="East Region Page"></s:textfield>-->
				<s:textfield name="CRPage.SouthTitle" label="South Side Title"></s:textfield>
				<s:select list="cps" label="South Region Page" listKey="Name" listValue="Name" name="CRPage.southRegionName"></s:select>
				<s:textfield name="CRPage.southHeightStr" label="South Side Height"></s:textfield>
				<s:select list="groups" name="CRPage.SouthGroupID" label="South Group ID" listKey="ID" listValue="Name"></s:select>
				<!--<s:textfield name="CRPage.southRegionName" label="South Region Page"></s:textfield>-->
				<s:textfield name="CRPage.WestTitle" label="West Side Title"></s:textfield>
				<s:select list="cps" label="West Region Page" listKey="Name" listValue="Name" name="CRPage.westRegionName"></s:select>
				<s:textfield name="CRPage.westWidthStr" label="West Side Width"></s:textfield>
				<s:select list="groups" name="CRPage.WestGroupID" label="West Group ID" listKey="ID" listValue="Name"></s:select>
				<!--<s:textfield name="CRPage.westRegionName" label="West Region Page"></s:textfield>-->
				<s:textfield name="CRPage.CenterTitle" label="Center Title"></s:textfield>
				<s:select list="centerCPs" label="Center Region Page" listKey="Name" listValue="Name" name="CRPage.centerRegionName"></s:select>
				<s:if test="%{action=='create'}">
					<s:submit action="Create">Save</s:submit>
				</s:if>
				<s:else>
					<s:submit action="Save">Save</s:submit>
				</s:else>
			</table>
		</s:form>
   			
	</body>
</html>
