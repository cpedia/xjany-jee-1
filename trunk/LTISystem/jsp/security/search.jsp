<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="authz" uri="/WEB-INF/authz.tld"%>
<html>
	<head>
		<title>Search</title>
	</head>
	<body>
	<div class="fbbl_center" id="security_div">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main" id="url_main" namespace="/jsp/security">
				</s:url>
				<s:a href="%{url_main}"><s:text name="security.manager"></s:text></s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save" id="url_create" namespace="/jsp/security" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}"><s:text name="create.security"></s:text></s:a>				
			</td>
			<td>
			</td>
		</table>	
		<table width="100%">
			<tr>
				<td>
					<s:url action="Search" id="url_s" includeParams="none" namespace="/jsp/security">
					</s:url>					
					<s:text name="search"></s:text>
					<form action='<s:property value="%{url_s}"/>' method="post">
						<input type="text" name="key" value=""/>
						<input type="submit" value="Go"/>
					</form>
				</td>
			</tr>
		</table>
		<table width="100%">
			<thead class="trHeader">
				<th><s:text name="symbol"></s:text></th>	
				<th><s:text name="security.name"></s:text></th>		
				<th><s:text name="security.type"></s:text></th>	
				<th><s:text name="Class Name"></s:text></th>
				<th><s:text name="diversified"></s:text></th>	
				<th><s:text name="startingdate"></s:text></th>	
				<th><s:text name="enddate"></s:text></th>				
				<th><s:text name="dailydatas"></s:text></th>	
				<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
				<th><s:text name="operation"></s:text></th>		
				<th><s:text name="Details on Yahoo"></s:text></th>
				</authz:authorize>																						
			</thead>
			<tbody>		
			<s:iterator value="#request.securities.items" status="s">
				<tr>
					<s:hidden id="ID" value="%{ID}" name="ID"></s:hidden>
					<td>
						<s:url action="View" id="urladdr" namespace="/jsp/fundcenter">
							<s:param name="symbol" value="symbol"></s:param>	
							<s:param name="includeHeader">true</s:param>
							<s:param name="title" value="symbol"></s:param>									
						</s:url>
						<s:a href="%{urladdr}"><s:property value="symbol"/></s:a>
						
					</td>
					<td>
						<s:property value="name"/>
					</td>
					<td>
						<s:property value="Types.get(#s.count-1)"/>
					</td>
					<td>
						<s:property value="Classes.get(#s.count-1)"/>
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
						<s:url action="DailydataMain" id="url_dailydata" namespace="/jsp/security">
							<s:param name="securityID" value="ID"></s:param>	
						</s:url>
						<s:a href="%{url_dailydata}"><s:text name="check"></s:text></s:a>
						
					</td>
					<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">	
					<td>
						<s:url action="Save" id="url_del" namespace="/jsp/security">
							<s:param name="ID" value="ID"></s:param>
							<s:param name="action">delete</s:param>
						</s:url>
						<s:a href="%{url_del}"><s:text name="remove"></s:text></s:a>
					</td>
					</authz:authorize>		
					<td>
					<a target="_blank"  href='http://finance.yahoo.com/q?s=<s:property value="symbol"/>'>View details</a>
					</td>		
				</tr>
			</s:iterator>
		</tbody>
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
					</s:url>	<br>			
					Page
					<form action='<s:property value="%{url_p}"/>' method="get" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						<input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.securities.pageSize"/>'/>
						<input type="hidden" name="key" value='<s:property value="%{key}"/>'>
						<input type="submit" value="Go"/>
					<form>
					</center>
					
	</div>	
	</body>
</html>
