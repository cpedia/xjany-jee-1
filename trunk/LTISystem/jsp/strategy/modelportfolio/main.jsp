<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<s:if test="includeHeader!=false">
<html>
<head>

<title><s:property value="strategy.name"/></title>
<meta name="submenu" content="individualstrategy"/>
<meta name="strategies" content="vf_current"/>
<meta name="modelportfolios" content="vf_current"/>
<meta name="id" content='<s:property value="strategy.ID"/>'/>

<META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache,must-revalidate"/> 
<META HTTP-EQUIV="Expires" CONTENT="0"/> 

<s:if test="includeJS!=false">
<link rel="stylesheet" href="../../images/jquery.tooltip/jquery.tooltip.css" />
<SCRIPT src="../../images/jquery.tooltip/PopDetails.js" type="text/javascript" ></SCRIPT>
<script src="../../images/jquery.tooltip/chili-1.7.pack.js" type="text/javascript"></script>
<script src="../../images/jquery.tooltip/jquery.tooltip.js" type="text/javascript"></script>
</s:if>


</head>
<body>
</s:if>
<style>
	.vf_odd {
	background-color: white; /* pale yellow for odd rows */
	}
	.vf_even {
	background-color: #ECECEC; /* pale blue for even rows */
	}
</style>
<s:if test="portfolioList.size>0">
<div id="mainForm">
<div align="center"><font color="red"><s:property value="actionMessage"/></font></div>
	<s:if test="isHolding == true">
	<p>
		<s:text name="holding.tips.pre"></s:text> <s:property value="holdingDate"/> <s:text name="holding.tips.tail"></s:text>
	</p>
	</s:if>
	<s:if test="P_operation == true">
		<s:url action="EditHolding" id="url_create" namespace="/jsp/portfolio" includeParams="none">
			<s:param name="strategyID" value="%{strategyID}"></s:param>			
			<s:param name="operation">create</s:param>
			<s:param name="ID">0</s:param>						
		</s:url>
	
	<s:a href="%{url_create}"><s:text name="strategy.create.portfolio"></s:text></s:a>
	</s:if>	

		<table bgcolor="#999999" border="0" cellpadding="0" cellspacing="1" width="100%">
			<tr bgcolor="#ffffff">
    			<td rowspan="2" align="center" width="23%">
					<h3><s:text name="name.portfolio"></s:text></h3>
				</td>
				<td rowspan="2" align="center" width="10%">
					<h3><s:text name="last.valid.date"></s:text></h3>
				</td>
				<td rowspan="2" align="center" width="12%">
					<h3><s:text name="last.transaction.date"></s:text></h3>
				</td>
  				<td colspan="2" align="center" width="14%">
    				<h3>1 <s:text name="year"></s:text></h3>
    			</td>
    			<td colspan="2" align="center" width="14%">
    				<h3>3 <s:text name="years"></s:text></h3>
    			</td>
    			<td colspan="2" align="center" width="14%">
					<h3>5 <s:text name="years"></s:text></h3>
				</td>
				<td>&nbsp;</td>
    		</tr>
			<tr bgcolor="#ffffff">
				<td align="center" width="7%">
					<s:text name="sharpe"></s:text>(%)
				</td>
				<td align="center" width="7%">
					<s:text name="AR"></s:text>(%)
				</td>
				<td align="center" width="7%">
					<s:text name="sharpe"></s:text>(%)
				</td>
				<td align="center" width="7%">
					<s:text name="AR"></s:text>(%)
				</td>
				<td align="center" width="7%">
					<s:text name="sharpe"></s:text>(%)
				</td>
				<td align="center" width="7%">
					<s:text name="AR"></s:text>(%)
				</td>
				<td>&nbsp;</td>
			</tr>

            <s:iterator value="portfolioList" status="st">
            	<s:if test="#st.count%2==0">
				<tr class="vf_odd">
				</s:if>
				<s:else>
				<tr class="vf_even" >
				</s:else>
				
					<td align="center" width="23%">
					
						<s:url action="Edit.action" id="url_port" namespace="/jsp/portfolio" includeParams="none">
							<s:param name="ID" value="ID"></s:param>			
						</s:url>
						<s:a href="%{url_port}"><div id='shortName' title='<s:property value="name"/>'><s:property value="showName"/></div></s:a>
						
					</td>			
					<td align="center" width="10%">
						<s:property value="lastValidDate"/>
					</td>
					<td align="center" width="12%">
						<s:property value="lastTransactionDate"/>
					</td>
					<td align="center" width="7%" >
						<s:property value="sharpeRatio1"/>
					</td>
					<td align="center" width="7%">
						<s:property value="AR1"/>
					</td>
					<td align="center" width="7%">
						<s:property value="sharpeRatio3"/>
					</td>
					<td align="center" width="7%">
						<s:property value="AR3"/>
					</td>
					<td align="center" width="7%">
						<s:property value="sharpeRatio5"/>
					</td>
					<td align="center" width="7%">
						<s:property value="AR5"/>
					</td>
					<td align="center" width="6%">
					<s:url id="delete_url" namespace="/jsp/portfolio" action="DeleteByID" includeParams="none">
						<s:param name="strategyID" value="%{strategyID}"></s:param>
						<s:param name="ID" value="ID"></s:param>	
					</s:url>
					<s:a href="%{delete_url}"><s:text name="delete"></s:text></s:a>
					</td>
				</tr>    
			</s:iterator>      
		</table>
</div>
</s:if>
<s:else>
There is no portfolios!
<s:if test="P_operation == true">
		<s:url action="EditHolding" id="url_create" namespace="/jsp/portfolio" includeParams="none">
			<s:param name="strategyID" value="%{strategyID}"></s:param>			
			<s:param name="operation">create</s:param>
			<s:param name="ID">0</s:param>				
		</s:url>
	
	<s:a href="%{url_create}"><s:text name="strategy.create.portfolio"></s:text></s:a>
</s:if>	
</s:else>
<s:if test="includeHeader!=false">
</body>
</html>
</s:if>