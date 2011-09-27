<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Indicator Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="adjHiehgt()">

		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/indicator">
				</s:url>
				<s:a href="%{url_main}">Indicator Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/indicator" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Indicator</s:a>				
			</td>
			<td>
			</td>
		</table>	
		<p class="title">Search Results</p>
		<table width="100%">
			<tr>
				<td>
					<s:form action="Search.action" namespace="/jsp/admin/indicator" theme="simple">
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
					Description
				</td>		
				<td>
					Daily Datas
				</td>	
				<td>
					Remove
				</td>																	
			</tr>		
			<s:iterator value="#request.indicators.items">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/indicator">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="symbol"/></s:a>
						
					</td>
					<td>
						<s:property value="description"/>
					</td>
					<td>
						<s:url action="Main.action" id="url_dailydata" namespace="/jsp/admin/indicator/dailydata">
							<s:param name="indicatorID" value="ID"></s:param>	
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
							<s:param name="startIndex" value="#request.indicators.firstParameter.startIndex"></s:param>
							<s:param name="key" value="#request.indicators.firstParameter.key"></s:param>
						</s:url>
										
						<s:a href="%{url_first}">
							First
						</s:a>	
										
						<s:iterator value="#request.indicators.parameters10" id="parameter" status="st">
							<s:url action="Search.action" id="pageurl">
								<s:param name="startIndex" value="#parameter.startIndex"></s:param>
								<s:param name="key" value="#parameter.key"></s:param>
							</s:url>
							<s:if test="#request.indicators.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
							<s:a href="%{pageurl}">
								<s:property value="#parameter.startIndex/#request.indicators.pageSize+1"/>
							</s:a>
							<s:if test="#request.indicators.startIndex==#parameter.startIndex"></class></s:if>	
						</s:iterator>
						
						<s:url action="Search.action" id="url_last">
							<s:param name="startIndex" value="#request.indicators.lastParameter.startIndex"></s:param>
							<s:param name="key" value="#request.indicators.lastParameter.key"></s:param>
						</s:url>
										
						<s:a href="%{url_last}">
							Last
						</s:a>	
					<s:url action="Search.action" id="url_p" includeParams="none" namespace="/jsp/admin/indicator">
					</s:url>		<br>			
					Page
					<form action='<s:property value="%{url_p}"/>' method="get" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						<input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.indicators.pageSize"/>'/>
						<input type="hidden" name="key" value='<s:property value="%{key}"/>'>
						<input type="submit" value="Go"/>
					<form>
					</center>	
					
		
	</body>
</html>
