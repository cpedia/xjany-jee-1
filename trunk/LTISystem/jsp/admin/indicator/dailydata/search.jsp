<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Indicator's Daily Data</title>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />		
	</head>
	<body>

	
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/indicator" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Indicator Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_view" namespace="/jsp/admin/indicator" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="ID" value="indicatorID"/>
				</s:url>
				<s:a href="%{url_view}">Indicator</s:a>				
			</td>
			<td width="15%">
				<s:url action="Main.action" id="url_dailydata" namespace="/jsp/admin/indicator/dailydata" includeParams="none">
					<s:param name="indicatorID" value="indicatorID"/>
				</s:url>
				<s:a href="%{url_dailydata}">Daily Datas</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/indicator/dailydata" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="indicatorID" value="indicatorID"/>
				</s:url>
				<s:a href="%{url_create}">Create a Daily Data</s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Search Results</p>
		<table width="100%">
			<tr>
				<td>
					<s:form action="Search.action" namespace="/jsp/admin/indicator/dailydata" theme="simple">
						Date <s:textfield name="date" value="%{date}"></s:textfield>
						<s:hidden name="indicatorID" value="%{indicatorID}"></s:hidden>
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
					Date
				</td>	
				<td>
					Value
				</td>		
				<td>
					Remove
				</td>																	
			</tr>	
			<s:iterator value="#request.dailydatas.items">
				<tr>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/indicator/dailydata">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="ID"/></s:a>					
						
					</td>
					<td>
						<s:property value="date"/>
					</td>
					<td>
						<s:property value="value"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/indicator/dailydata">
							<s:param name="indicatorID" value="indicatorID"></s:param>
							<s:param name="dailydataID" value="ID"></s:param>	
							<s:param name="action">delete</s:param>						
						</s:url>
						<s:a href="%{urladdr}">Delete</s:a>
						
					</td>
				</tr>
			</s:iterator>

		</table>
					<s:date name="date" format="yyyy-MM-dd" id="date_string"/>
					
					<center>
					Change to
						<s:url action="Search.action" id="url_first">
							<s:param name="startIndex" value="#request.dailydatas.firstParameter.startIndex"></s:param>
							<s:param name="date" value="%{date_string}"></s:param>
							<s:param name="indicatorID" value="%{indicatorID}"></s:param>
						</s:url>
										
						<s:a href="%{url_first}">
							First
						</s:a>	
										
						<s:iterator value="#request.dailydatas.parameters10" id="parameter" status="st">
							<s:url action="Search.action" id="pageurl">
								<s:param name="startIndex" value="#parameter.startIndex"></s:param>
								<s:param name="date" value="%{date_string}"></s:param>
								<s:param name="indicatorID" value="%{indicatorID}"></s:param>
							</s:url>
							<s:if test="#request.dailydatas.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
							<s:a href="%{pageurl}">
								<s:property value="#parameter.startIndex/#request.dailydatas.pageSize+1"/>
							</s:a>
							<s:if test="#request.dailydatas.startIndex==#parameter.startIndex"></class></s:if>	
						</s:iterator>
						
						<s:url action="Search.action" id="url_last">
							<s:param name="startIndex" value="#request.dailydatas.lastParameter.startIndex"></s:param>
							<s:param name="date" value="%{date_string}"></s:param>
							<s:param name="indicatorID" value="%{indicatorID}"></s:param>
						</s:url>
										
						<s:a href="%{url_last}">
							Last
						</s:a>	
					<s:url action="Search.action" id="url_m" includeParams="none">
					</s:url>					
					Page
					<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						<input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.dailydatas.pageSize"/>'/>
						<s:hidden name="indicatorID" value="%{indicatorID}"></s:hidden>
						<s:hidden name="date" value="%{date}"></s:hidden>
						<input type="submit" value="Go"/>
					<form>
					</center>
		
	</body>
</html>
