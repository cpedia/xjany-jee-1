<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group's Portfolio</title>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="10%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/group" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Group Manager</s:a>		
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_view" namespace="/jsp/admin/group" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="ID" value="groupID"/>
				</s:url>
				<s:a href="%{url_view}">Edit Group</s:a>				
			</td>			
			<td width="10%">
				<s:url action="Main.action" id="url_main1" namespace="/jsp/admin/group/portfolio" includeParams="none">
					<s:param value="groupID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main1}">Portfolios</s:a>				
			</td>	
			<td width="10%">
				<s:url action="Main.action" id="url_main2" namespace="/jsp/admin/group/strategy" includeParams="none">
					<s:param value="groupID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main2}">Strategies</s:a>				
			</td>	
			<td width="10%">
				<s:url action="Main.action" id="url_main3" namespace="/jsp/admin/group/user" includeParams="none">
					<s:param value="groupID" name="groupID"/>
				</s:url>
				<s:a href="%{url_main3}">Users</s:a>				
			</td>											
			<td width="10%">
				<s:url action="Save.action" id="url_create1" namespace="/jsp/admin/group/portfolio" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="groupID"/>
				</s:url>
				<s:a href="%{url_create1}">Add a Portfolio</s:a>				
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_create2" namespace="/jsp/admin/group/strategy" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="groupID"/>
				</s:url>
				<s:a href="%{url_create2}">Add a Strategy</s:a>				
			</td>
			<td width="10%">
				<s:url action="Save.action" id="url_create3" namespace="/jsp/admin/group/user" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="groupID" value="groupID"/>
				</s:url>
				<s:a href="%{url_create3}">Add a User</s:a>				
			</td>
			<td>
			</td>
		</table>
		<p class="title"><s:property value="%{title}"/></p>
		<p class="subtitle">Portfolio List</p>
		<table width="100%">
			<tr class="trHeader">
				<td>
					Portfolio ID
				</td>
				<td>
					Portfolio Name
				</td>	
				<td>
					Portfolio Description
				</td>		
				<td>
					Remove
				</td>																	
			</tr>
			<s:iterator value="#request.portfolios.items">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:property value="name"/>
					</td>
					<td>
						<s:property value="description"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/group/portfolio">
							<s:param name="groupID" value="groupID"></s:param>
							<s:param name="portfolioID" value="ID"></s:param>		
							<s:param name="action">delete</s:param>						
						</s:url>
						<s:a href="%{urladdr}">Remove</s:a>
						
					</td>

				</tr>
			</s:iterator>
			<tr>
				<td colspan="4">
					<center>
					Change to
						<s:url action="Main.action" id="url_first">
							<s:param name="startIndex" value="#request.portfolios.firstParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_first}">
							First
						</s:a>	
										
						<s:iterator value="#request.portfolios.parameters10" id="parameter" status="st">
							<s:url action="Main.action" id="pageurl">
								<s:param name="startIndex" value="#parameter.startIndex"></s:param>
							</s:url>
							<s:if test="#request.portfolios.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
							<s:a href="%{pageurl}">
								<s:property value="#parameter.startIndex/#request.portfolios.pageSize+1"/>
							</s:a>
							<s:if test="#request.portfolios.startIndex==#parameter.startIndex"></class></s:if>	
						</s:iterator>
						
						<s:url action="Main.action" id="url_last">
							<s:param name="startIndex" value="#request.portfolios.lastParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_last}">
							Last
						</s:a>	
					<s:url action="Main.action" id="url_m" includeParams="none">
					</s:url>								
					Page
					<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						Page <input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.portfolios.pageSize"/>'/>
						<input type="submit" value="Go"/>
					<form>
					</center>				
				</td>
			</tr>
		</table>

		
	</body>
</html>
