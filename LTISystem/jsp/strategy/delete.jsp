<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>

<html>
<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache,must-revalidate"/> 
<META HTTP-EQUIV="Expires" CONTENT="0"/> 

<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<title>Delete Strategy Page</title>
<style>
	.odd {
	background-color: white; /* pale yellow for odd rows */
	}
	.even {
	background-color: #FFFF99; /* pale blue for even rows */
	}
</style>
<script>
$(document).ready(function() {
	$('tr[@id^="item"]:odd').children('td').addClass('odd');
	$('tr[@id^="item"]:even').children('td').addClass('even');
});
</script>
</head>
<body>
	<s:form theme="simple" id="deleteForm" name="deleteForm">
		<s:hidden name="ID" theme="simple"></s:hidden>
	
		<s:if test="portfolioList != null && portfolioList.size() != 0">
			<div align="center"><s:text name="strategy.portfolios"></s:text></div>
			<table align="center" bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" width="70%">
				<tr bgcolor="#ffffff">
					<td rowspan="2" align="center" width="5%">&nbsp;</td>
					<td rowspan="2" align="center" width="18%">
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
				</tr>
			
			    <s:iterator value="portfolioList" status="st">
				<tr id='item<s:property value="#st.count"/>' bgcolor="#ffffff">
					<td align="center" width="5%"><input type="checkbox" name='portfolioList[<s:property value="#st.count -1"/>].deleteChoosed' value="true"></td>
					<td align="center" width="18%">
						<div id='shortName' title="<s:property value='name'/>">
							<s:property value="showName"/>
							<s:hidden name='portfolioList[%{#st.index}].Name'></s:hidden>
							<s:hidden name='portfolioList[%{#st.index}].ID'></s:hidden>
						</div>	
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
				</tr>    
				</s:iterator>      
			</table>
		
		</s:if>
	</s:form>
	
	<div id="confirm" align="center">
		<s:if test="%{isOwner}">
			<s:text name="strategy.delete.confirm"></s:text>
			<input type="button" onclick="del()" value='<s:text name="button.yes"></s:text>' />
			<input type="button" onclick="abandon()" value='<s:text name="button.no"></s:text>'>
		</s:if>
		<s:url action="View.action" id="url_str" includeParams="none">
			<s:param name="ID" value="ID"></s:param>	
			<s:param name="action">view</s:param>				
		</s:url>
		
		<script>
			function del()
			{
				  if(confirm("Do you want to delete this strategy and the selected portfolio?")){
			        document.deleteForm.action='../strategy/Delete.action?action=delete';
			        $('#deleteForm').submit();
	 			 }
				  else
				  {
				  	  $("#deleteForm").attr({onsubmit:"return false"});
				  }
					 
			}	
			function abandon()
			{
				window.location.href = '<s:property value="url_str" escape="false"/>';
			}
		</script>
	</div>
</body>
</html>