<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="adjHiehgt()">

		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/security">
				</s:url>
				<s:a href="%{url_main}">Security Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/security" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Security</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="title">Search Results</p>
		
		<table width="100%">
			<tr>
				<td>
					<s:form action="Search.action" namespace="/jsp/admin/security" theme="simple">
						Search <s:textfield name="key" value="%{key}"></s:textfield>
						<s:submit value="GO"></s:submit>
					</s:form>

				</td>
			</tr>
		</table>		
		<table width="100%">
		
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Symbol
				</td>	
				<td>
					Name
				</td>		
				<td>
					Security Type
				</td>	
				<td>
					Class ID
				</td>	
				<td>
					Diversified
				</td>	
<td>
					Start Date
				</td>	
				<td>
					End Date
				</td>	
				<td>
					Nav End Date
				</td>			
				<td>
					Daily Datas
				</td>	
				<td>
					Remove
				</td>																								
			</tr>		
			<s:iterator value="#request.securities.items">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/security">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="symbol"/></s:a>
						
					</td>
					<td>
						<s:property value="name"/>
					</td>
					<td>
						<s:property value="securitytype"/>
					</td>
					<td>
						<s:property value="classID"/>
					</td>
					<td>
						<s:property value="diversified"/>
					</td>															
					<td>
						<s:property value="startDate"/>
					</td>
					<td>
						<s:property value="endDate"/>
					</td>	
					<td>
						<s:property value="navLastDate"/>
					</td>	
					<td>
						<s:url action="Main.action" id="url_dailydata" namespace="/jsp/admin/security/dailydata">
							<s:param name="securityID" value="ID"></s:param>	
						</s:url>
						<s:a href="%{url_dailydata}">Daily Datas</s:a>
						
					</td>	
					<td>
						<s:url action="Save.action" id="url_del">
							<s:param name="ID" value="ID"></s:param>
							<s:param name="action">delete</s:param>
						</s:url>
						<s:a href="%{url_del}">Remove</s:a>
					</td>				
				</tr>
			</s:iterator>

		</table>
					<center>
					Change to
						<s:url action="Search.action" id="url_first">
							<s:param name="startIndex" value="#request.securities.firstParameter.startIndex"></s:param>
							<s:param name="key" value="#request.securities.firstParameter.key"></s:param>
						</s:url>
										
						<s:a href="%{url_first}">
							First
						</s:a>	
										
						<s:iterator value="#request.securities.parameters10" id="parameter" status="st">
							<s:url action="Search.action" id="pageurl">
								<s:param name="startIndex" value="#parameter.startIndex"></s:param>
								<s:param name="key" value="#parameter.key"></s:param>
							</s:url>
							<s:if test="#request.securities.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
							<s:a href="%{pageurl}">
								<s:property value="#parameter.startIndex/#request.securities.pageSize+1"/>
							</s:a>
							<s:if test="#request.securities.startIndex==#parameter.startIndex"></class></s:if>	
						</s:iterator>
						
						<s:url action="Search.action" id="url_last">
							<s:param name="startIndex" value="#request.securities.lastParameter.startIndex"></s:param>
							<s:param name="key" value="#request.securities.lastParameter.key"></s:param>
						</s:url>
										
						<s:a href="%{url_last}">
							Last
						</s:a>	
					<s:url action="Search.action" id="url_p" includeParams="none" namespace="/jsp/admin/security">
					</s:url>		<br>			
					Page
					<form action='<s:property value="%{url_p}"/>' method="get" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						<input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.securities.pageSize"/>'/>
						<input type="hidden" name="key" value='<s:property value="%{key}"/>'>
						<input type="submit" value="Go"/>
					<form>
					</center>	
					
		
	</body>
</html>
