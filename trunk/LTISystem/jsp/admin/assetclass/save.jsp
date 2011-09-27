<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Asset Class Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="adjHiehgt()">

<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/assetclass">
				</s:url>
				<s:a href="%{url_main}">Asset Class Manager</s:a>				
			</td>	
			<s:if test="#request.action!='save'">
				<td width="20%">
					<s:url action="Save.action" id="url_create" namespace="/jsp/admin/assetclass" includeParams="none">
						<s:param name="parentID" value="ID"/>
						<s:param name="action">create</s:param>
					</s:url>
					<s:a href="%{url_create}">Create A Sub Class</s:a>				
				</td>
			
				<td width="20%">
					<s:url action="Save.action" id="url_del">
						<s:param name="ID" value="ID"></s:param>
						<s:param name="action">delete</s:param>
					</s:url>
					<s:a href="%{url_del}">Delete <s:property value="name"/></s:a>			
				</td>		
			</s:if>	
			<td width="45%">
			</td>
</table>		
	
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield name="ID" label="ID " disabled="true"/>
			<s:textfield name="name" label="Name "/>
			<s:textfield name="parentID" label="Parent ID " disabled="false"/>
			<s:textfield name="parentName" label="Parent Name " disabled="true"/>
			<s:textfield name="benchmarkID" label="Benchmark ID "/>
 			<s:submit>Save</s:submit>

	</s:form>
   
	</body>
</html>
