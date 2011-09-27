<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Customize Region Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>

		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main" id="url_main" namespace="/jsp/admin/customizeregion">
				</s:url>
				<s:a href="%{url_main}">Customize Manager</s:a>				
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
		<p class="title">Customize Region List</p>
		<table width="100%">
			<tr class="trHeader">
				<td>Page Name</td>
				<td>Center</td>	
				<td>East</td>
				<td>South</td>
				<td>West</td>																																			
			</tr>		
			<s:iterator value="crList">
				<tr>
					<td>
						<s:url action="View" id="CRPage_url" namespace="/jsp/admin/customizeregion">
							<s:param name="ID" value="ID"></s:param>
						</s:url>
						<s:a href="%{CRPage_url}"><s:property value="PageName"/></s:a>
					</td>
					<td><s:property value="centerRegionName"/></td>
					<td><s:property value="eastRegionName"/></td>
					<td><s:property value="southRegionName"/></td>
					<td><s:property value="westRegionName"/></td>														
				</tr>
			</s:iterator>
		</table>		
	</body>
</html>
