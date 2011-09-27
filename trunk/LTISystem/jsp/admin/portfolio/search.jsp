<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Portfolio Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div>
			<s:form namespace="/jsp/admin/portfolio">
				<s:textfield name="key" theme="simple"></s:textfield>
				<s:submit value="search" action="Search" theme="simple"></s:submit>
			</s:form>
		</div>
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Name
				</td>
				<td>
					Keywords
				</td>
				<td>
					Deleted Keywords
				</td>	
				<td>
					User 
				</td>		
																																				
			</tr>			
		
			<s:iterator value="#request.portfolios.items">
				<tr>
					<td style="border-bottom:1px solid">
						<s:property value="ID"/>
					</td>
					<td style="border-bottom:1px solid">
						<s:url action="Edit.action" id="urladdr" namespace="/jsp/portfolio">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="name"/></s:a>
					</td>
					<td style="border-bottom:1px solid">
						<s:url action="View" id="keywordUrl" namespace="/jsp/admin/portfolio/keyword" includeParams="none">
							<s:param name="portfolioID" value="ID"></s:param>		
							<s:param name="action">view</s:param>				
						</s:url>
						<s:a href="%{keywordUrl}">Manage</s:a>
					</td>
					<td style="border-bottom:1px solid">
						<s:url action="View" id="trashyKeywordUrl" namespace="/jsp/admin/portfolio/trashykeyword" includeParams="none">
							<s:param name="portfolioID" value="ID"></s:param>		
							<s:param name="action">view</s:param>				
						</s:url>
						<s:a href="%{trashyKeywordUrl}">Manage</s:a>
					</td>
					<td style="border-bottom:1px solid">
						<s:property value="userName"/>
						<s:url action="View" id="userUrl" namespace="/jsp/admin/portfolio/user" includeParams="none">
							<s:param name="portfolioID" value="ID"></s:param>			
						</s:url>
						<s:a href="%{userUrl}">change</s:a>
					</td>
																	
				</tr>
			</s:iterator>

		</table>
		<center>
		Change to
			<s:url action="Search" id="url_first">
				<s:param name="startIndex" value="#request.portfolios.firstParameter.startIndex"></s:param>
				<s:param name="key" value="key"></s:param>
			</s:url>
							
			<s:a href="%{url_first}">
				First
			</s:a>	
							
			<s:iterator value="#request.portfolios.parameters10" id="parameter" status="st">
				<s:url action="Search" id="pageurl">
					<s:param name="startIndex" value="#parameter.startIndex"></s:param>
					<s:param name="key" value="key"></s:param>
				</s:url>
				<s:if test="#request.portfolios.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
				<s:a href="%{pageurl}">
					<s:property value="#parameter.startIndex/#request.portfolios.pageSize+1"/>
				</s:a>
				<s:if test="#request.portfolios.startIndex==#parameter.startIndex"></class></s:if>	
			</s:iterator>
			
			<s:url action="Search" id="url_last">
				<s:param name="startIndex" value="#request.portfolios.lastParameter.startIndex"></s:param>
				<s:param name="key" value="key"></s:param>
			</s:url>
							
			<s:a href="%{url_last}">
				Last
			</s:a>	
		<s:url action="Search" id="url_m" includeParams="none">
		</s:url>								
		Page
		<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
			Page <input type="text" name="startIndex" value=""/>
			<input type="hidden" name="pageSize" value='<s:property value="#request.portfolios.pageSize"/>'/>
			<input type="submit" value="Go"/>
		<form>
		</center>	
		
	</body>
</html>
