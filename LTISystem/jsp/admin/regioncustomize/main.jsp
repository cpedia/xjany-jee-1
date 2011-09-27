<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Region Customize Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>

		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/regioncustomize">
				</s:url>
				<s:a href="%{url_main}">Customize Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Create.action" id="url_create" namespace="/jsp/admin/regioncustomize"  includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Region Customize</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="title">Region Customize List</p>
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Page Name
				</td>	
				<td>
				Region Name
				</td>
				<td>
				Region Content
				</td>
				<td>
				Authorized Group ID
				</td>
				<td>
					Remove
				</td>																																					
			</tr>		
			<s:iterator value="#request.rcs.items">
				<tr>
					<td>
						<s:url action="View.action" id="urladdr" namespace="/jsp/admin/regioncustomize">
							<s:param name="ID" value="ID"></s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="ID"/></s:a>
					</td>
					<td>
						<s:property value="PageName"/>
					</td>
					<td>
					<s:property value="RegionName"/>
					</td>
					<td>
					<s:property value="RegionContent"/>
					</td>
					<td>
					<s:property value="GroupID"/>
					</td>
					
					<td>
						<s:url action="Delete.action" id="urldelete" namespace="/jsp/admin/regioncustomize">
							<s:param name="ID" value="ID"></s:param>					
						</s:url>
						<s:a href="%{urldelete}">Remove</s:a>						
					</td>														
				</tr>
			</s:iterator>
		</table>
		<center>
		Change to
			<s:url action="Main.action" id="url_first">
				<s:param name="startIndex" value="#request.startIndex"></s:param>
			</s:url>
							
			<s:a href="%{url_first}">
				First
			</s:a>	
							
			<s:iterator value="#request.cps.parameters10" id="parameter" status="st">
				<s:url action="Main.action" id="pageurl">
					<s:param name="startIndex" value="#parameter.startIndex"></s:param>
				</s:url>
				<s:if test="#request.cps.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
				<s:a href="%{pageurl}">
					<s:property value="#parameter.startIndex/#request.cps.pageSize+1"/>
				</s:a>
				<s:if test="#request.cps.startIndex==#parameter.startIndex"></class></s:if>	
			</s:iterator>
			
			<s:url action="Main.action" id="url_last">
				<s:param name="startIndex" value="#request.cps.lastParameter.startIndex"></s:param>
			</s:url>
							
			<s:a href="%{url_last}">
				Last
			</s:a>	
		<s:url action="Main.action" id="url_m" includeParams="none">
		</s:url>								
		Page
		<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
			Page <input type="text" name="startIndex" value=""/>
			<input type="hidden" name="pageSize" value='<s:property value="#request.cps.pageSize"/>'/>
			<input type="submit" value="Go"/>
		<form>
		</center>	
		
	</body>
</html>
