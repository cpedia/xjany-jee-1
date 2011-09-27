<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security's Daily Data</title>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />		
	</head>
	<body>

	
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/security" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Security Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_view" namespace="/jsp/admin/security" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="ID" value="securityID"/>
				</s:url>
				<s:a href="%{url_view}">Security</s:a>				
			</td>
			<td width="15%">
				<s:url action="Main.action" id="url_dailydata" namespace="/jsp/admin/security/dailydata" includeParams="none">
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_dailydata}">Daily Datas</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/security/dailydata" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_create}">Create a Daily Data</s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Daily Datas</p>
		<table width="100%">
			<tr>
				<td>
					<s:url action="Search.action" id="url_s" includeParams="none">
					</s:url>					
					
					<form action='<s:property value="%{url_s}"/>' method="post">
						Date <s:textfield theme="simple" name="date" value="%{date}"></s:textfield>
						<s:hidden name="securityID" value="%{securityID}"></s:hidden>
						<input type="submit" value="Go"/>
						
					</form>
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
					Split
				</td>
				<td>
					Dividend
				</td>	
				<td>
					EPS
				</td>
				<td>
					MarketCap
				</td>
				<td>
					PE
				</td>
				<td>															
					Close
				</td>
				<td>
					Open
				</td>
				<td>
					High
				</td>	
				<td>
					low
				</td>
				<td>
					AdjClose
				</td>
				<td>
					Volume
				</td>	
				<td>
					Return Dividend
				</td>
				<td>
					Security ID
				</td>	
				<td>
					Turnover Rate
				</td>
				<td>
					NAV
				</td>						
				<td>
					AdjNAV
				</td>		
				<td>
					Remove
				</td>																	
			</tr>	
			<s:iterator value="#request.dailydatas.items">
				<tr>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/security/dailydata">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="ID"/></s:a>					
						
					</td>
					<td>
						<s:property value="date"/>
					</td>

					<td>
						<s:property value="split"/>
					</td>
					<td>
						<s:property value="dividend"/>
					</td>	
					<td>
						<s:property value="EPS"/>
					</td>
					<td>
						<s:property value="marketCap"/>
					</td>
					<td>
						<s:property value="PE"/>
					</td>		
					<td>													
						<s:property value="close"/>
					</td>
					<td>
						<s:property value="open"/>
					</td>
					<td>
						<s:property value="high"/>
					</td>	
					<td>
						<s:property value="low"/>
					</td>
					<td>
						<s:property value="adjClose"/>
					</td>
					<td>
						<s:property value="volume"/>
					</td>	
					<td>
						<s:property value="returnDividend"/>
					</td>
					<td>
						<s:property value="securityID"/>
					</td>	
					<td>
						<s:property value="turnoverRate"/>
					</td>
					<td>
						<s:property value="NAV"/>
					</td>						
					<td>
						<s:property value="adjNAV"/>
					</td>	
				
					
					
					
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/admin/security/dailydata">
							<s:param name="securityID" value="securityID"></s:param>
							<s:param name="dailydataID" value="ID"></s:param>	
							<s:param name="action">delete</s:param>						
						</s:url>
						<s:a href="%{urladdr}">Remove</s:a>
						
					</td>
				</tr>
			</s:iterator>

		</table>
					<center>
					Change to
						<s:url action="Main.action" id="url_first">
							<s:param name="startIndex" value="#request.dailydatas.firstParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_first}">
							First
						</s:a>	
										
						<s:iterator value="#request.dailydatas.parameters10" id="parameter" status="st">
							<s:url action="Main.action" id="pageurl">
								<s:param name="startIndex" value="#parameter.startIndex"></s:param>
							</s:url>
							<s:if test="#request.dailydatas.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
							<s:a href="%{pageurl}">
								<s:property value="#parameter.startIndex/#request.dailydatas.pageSize+1"/>
							</s:a>
							<s:if test="#request.dailydatas.startIndex==#parameter.startIndex"></class></s:if>	
						</s:iterator>
						
						<s:url action="Main.action" id="url_last">
							<s:param name="startIndex" value="#request.dailydatas.lastParameter.startIndex"></s:param>
						</s:url>
										
						<s:a href="%{url_last}">
							Last
						</s:a>	
					<s:url action="Main.action" id="url_m" includeParams="none">
					</s:url>					
					Page
					<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
						<input type="text" name="startIndex" value=""/>
						<input type="hidden" name="pageSize" value='<s:property value="#request.dailydatas.pageSize"/>'/>
						<input type="submit" value="Go"/>
					<form>
					</center>
		
	</body>
</html>
