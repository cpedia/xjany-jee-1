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
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/security" includeParams="none">
				</s:url>
				<s:url action="Main.action" id="url_delay" namespace="/jsp/admin/security">
				<s:param name="action">delay</s:param>
				</s:url>
				<s:a href="%{url_main}">Security Manager</s:a>
			</td>
			<td width="15%">
				<!--<s:url action="Save.action" id="url_create" namespace="/jsp/admin/security" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A New Security</s:a>	-->			
			</td>
			<td>
			</td>
		</table>	
	<p class="title">Security List</p></body>
		<table width="100%">
			<tr>
			<td>Search:</td>
				<td>
					<s:url action="Search.action" id="url_s" includeParams="none">
					</s:url>					
					<form action='<s:property value="%{url_s}"/>' method="post">
						<input type="text" name="key"  value=""/>
						<input type="submit" value="Go"/>
					</form>
				</td>
				<td>
				<form action='<s:property value="%{url_delay}"/>' method="post">
				show securities before <input type="text" name="year" size="5"/>year,<input type="text" name="month" size="5"/>month,<input type="text" name="day" size="5"/>day 
				<input type="submit" value="Go">
				</form>
				</td>
			</tr>
			<tr>
				<s:url action="CheckSecurityPrice.action" id="url_checkprice" namespace="/jsp/admin/security/CheckSecurityPrice" includeParams="none">
				</s:url>
				<form action='<s:property value="%{url_checkprice}"/>' method="post">
						Set End Date: <input type="text" name="dateStr"  value=""/>
						Update:(y\n) <input type="text" name="update" id="update"/>
						<input type="submit" value="CheckPrice"/>
				</form>
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
						<s:property value="securityType"/>
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
							<s:param name="startIndex">0</s:param>	
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
						<s:url action="Main.action" id="url_first">
							<s:param name="startIndex" value="#request.securities.firstParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_first}">
							First
						</s:a>	
										
						<s:iterator value="#request.securities.parameters10" id="parameter" status="st">
							<s:url action="Main.action" id="pageurl">
								<s:param name="startIndex" value="#parameter.startIndex"></s:param>
							</s:url>
							<s:if test="#request.securities.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
							<s:a href="%{pageurl}">
								<s:property value="#parameter.startIndex/#request.securities.pageSize+1"/>
							</s:a>
							<s:if test="#request.securities.startIndex==#parameter.startIndex"></class></s:if>	
						</s:iterator>
						
						<s:url action="Main.action" id="url_last">
							<s:param name="startIndex" value="#request.securities.lastParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_last}">
							Last
						</s:a>	
					<s:url action="Main.action" id="url_m" includeParams="none">
					</s:url>	<br>				
					Page
					<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						<input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.securities.pageSize"/>'/>
						<input type="submit" value="Go"/>
					<form>
					
					</center>	
					
		
	</body>
</html>
