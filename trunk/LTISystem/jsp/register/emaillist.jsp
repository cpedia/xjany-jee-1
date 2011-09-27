<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 
<s:form theme="simple" namespace="/jsp/register">
    <s:param name="portfolioID" value='<s:property value="portfolioID"/>'/>
    <s:param name="portfolioName" value='<s:property value="portfolioName"/>'/>
	<s:if test="emailList != null || emailList.size()<1" >
	<table id="emails" width="100%" border="0">
		<thead>
			<th width="14%">Portfolio ID</th>
			<th width="40%">Portfolio Name</th>
			<th width="19%">Last Transaction Date</th>
			<th width="19%">Last Email Alert</th>
		</thead>
		<s:iterator value="emailList" status="st">
		<tr>
			<td align="center"><s:property value="portfolioID"/><input type="hidden" name='emailList[<s:property value="#st.count-1"/>].portfolioID' value='<s:property value="portfolioID"/>'></td>
			<td align="left">
			<a href="http://www.myplaniq.com/LTISystem/jsp/portfolio/Edit.action?ID=${portfolioID}">${portfolioName} </a> 
			</td>
			<td align="center">${lastTransactionDate}</td>
			<td align="center">${lastSentDate}</td>
		</tr>
		</s:iterator>
		
	</table>
	<br></br>
	If you want to cancel email alerts, please click "remove this portfolio" on the "My Portfolio" page.
	</s:if>
	<s:else>
		You didn't choose any portfolios.
	</s:else>
	
</s:form>