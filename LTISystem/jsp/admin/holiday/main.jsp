<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<s:head theme="ajax"/>
	</head>
	<body  onload="adjHiehgt()">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/holiday">
				</s:url>
				<s:a href="%{url_main}">Holiday Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/holiday">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Holiday</s:a>				
			</td>
			<td width="15%">
				<s:url value="importholiday.jsp" id="url_import" namespace="/jsp/admin/holiday" includeParams="none">
				</s:url>
				<s:a href="%{url_import}">Import Holiday</s:a>				
			</td>
			<td width="15%">
				<s:url value="exportholiday.jsp" id="url_export" namespace="/jsp/admin/holiday" includeParams="none">
				</s:url>
				<s:a href="%{url_export}">Export Holiday</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="title">Holiday List</p>
		<s:form action="Save.action">
			<s:hidden name="action" value="view"></s:hidden>
			Go To<s:datetimepicker name="date" theme="simple"></s:datetimepicker><s:submit></s:submit>
		</s:form>
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Name
				</td>	
				<td>
					Description
				</td>				
				<td>
					Date
				</td>		
				<td>
					Remove
				</td>																								
			</tr>>
			<s:iterator value="#request.holidays.items">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/holiday">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="symbol"/></s:a>
						
					</td>
					<td>
						<s:property value="description"/>
					</td>
					<td>
						<s:property value="date"/>
					</td>
					<td>
						<s:url action="Save.action" id="url_del" namespace="/jsp/admin/holiday">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">delete</s:param>						
						</s:url>
						<s:a href="%{url_del}">remove</s:a>
					</td>
				</tr>
			</s:iterator>

		</table>
		<center>
		Change to
			<s:url action="Main.action" id="url_first" includeParams="none">
				<s:param name="startIndex" value="#request.holidays.firstParameter.startIndex"></s:param>
				<s:param name="date" value="#request.date"></s:param>
			</s:url>
							
			<s:a href="%{url_first}">
				First
			</s:a>	
							
			<s:iterator value="#request.holidays.parameters10" id="parameter" status="st">
				<s:url action="Main.action" id="pageurl" includeParams="none">
					<s:param name="startIndex" value="#parameter.startIndex"></s:param>
				</s:url>
				<s:if test="#request.holidays.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
				<s:a href="%{pageurl}">
					<s:property value="#parameter.startIndex/#request.holidays.pageSize+1"/>
				</s:a>
				<s:if test="#request.holidays.startIndex==#parameter.startIndex"></class></s:if>	
			</s:iterator>
			
			<s:url action="Main.action" id="url_last"  includeParams="none">
				<s:param name="startIndex" value="#request.holidays.lastParameter.startIndex"></s:param>
			</s:url>
							
			<s:a href="%{url_last}">
				Last
			</s:a>	
		<s:url action="Main.action" id="url_m"  includeParams="none">
		</s:url>	<br>				
		Page
		<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
			<input type="text" name="startIndex" value=""/>
			<input type="hidden" name="pageSize" value='<s:property value="#request.holidays.pageSize"/>'/>
			<input type="submit" value="Go"/>
		<form>
		
		</center>	
		
	</body>
</html>
