<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="authz" uri="/WEB-INF/authz.tld"%>
<html>
	<head>
		<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<title>Security Page</title>
		<style type="text/css">
		
		#performanceTable{
		margin-top:1.5em;
		margin-bottom:1em;
		}
		#vf_content-wrap {
        padding: 10px;
         }
		
		fieldset{
		margin: 1em 0;
		padding: 1em;
		border: 1px solid #ccc;
		}
		
		legend{
		font-weight:bold;
		}
		
		</style>
		<script type="text/javascript">
		function fixNum(num)
		{
		document.write(num.toFixed(3));
		}
		</script>
	</head>
	<body>
	<div class="fbbl_center" id="infomation_div">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main" id="url_main" namespace="/jsp/security" includeParams="none">
				</s:url>
				<s:a href="%{url_main}"><s:text name="security.manager"></s:text></s:a>				
			</td>
			<td width="15%">
				<s:url action="Save" id="url_view" namespace="/jsp/security" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="ID" value="ID"/>
				</s:url>
				<s:a href="%{url_view}"><s:text name="daily.data.security"></s:text></s:a>				
			</td>
			<td width="15%">
				<s:url action="DailydataMain" id="url_dailydata" namespace="/jsp/security" includeParams="none">
					<s:param name="securityID" value="ID"/>
				</s:url>
				<s:a href="%{url_dailydata}"><s:text name="securiy.daily.data"></s:text></s:a>				
			</td>		
			<td width="15%">
				<s:url action="DailydataSave" id="url_create" namespace="/jsp/security" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="securityID" value="ID"/>
				</s:url>
				<s:a href="%{url_create}"><s:text name="security.create.daily.data"></s:text></s:a>			
			</td>				
		</table>
	<p class="title"><s:property value="%{title}"/> <a target="_blank"  href='http://finance.yahoo.com/q?s=<s:property value="symbol"/>'>details to yahoo</a></p>
	<p class="subtitle"><s:text name="security.edit"></s:text></p>
	<s:actionmessage/>	
	<div style="align:center;">
	<fieldset style="width:50%">	
	<legend>Security Details</legend>	
	
	<s:form action="Save" method="post">
			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield size="50" name="symbol" key="symbol"/>
			<s:textfield size="50" name="name" key="security.name"/>
			<s:select name="classID" list="%{acs}" listKey="ID" listValue="Name" label="ClassName" emptyOption="true"></s:select>
			<s:textfield size="30" name="currentPrice" key="security.current.price"/>
			<s:textfield size="30" name="diversified" key="diversified"/>
			<s:select name="securityType" list="%{sts}" listKey="ID" listValue="Name" label="SecurityTypeName" emptyOption="true" ></s:select>		
 			<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
 			<s:submit></s:submit>			
 			</authz:authorize>
	</s:form>
	</fieldset>
	<fieldset style="width:50%;">
	<legend>Security Performance</legend>
	<table id="performanceTable" cellspacing="0" border="solid" width="100%" align="center">
	<thead bgcolor="yellow">
	<th>Years</th>
	<s:iterator value="#request.smpt">
	<th>
	<s:if test="%{Year==-1}">
	Last 1 year
	</s:if>
	<s:elseif test="%{Year==-3}">
	Last 3 years
	</s:elseif>
	<s:elseif test="%{Year==-5}">
	Last 5 years
	</s:elseif>
	<s:else>
	<s:property value="Year"/>
	</s:else>
	</th>
	</s:iterator>
	</thead>
	<tbody>
	<tr>
	<th>Alpha</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="Alpha"/>)
	</script>
	</td>
	</s:iterator>
	</tr>
	<tr>
	<th>Beta</th>
	<s:iterator value="#request.smpt">
	<td width="100px">
	<script>
	fixNum(<s:property value="Beta"/>)
	</script>
	</td>
	</s:iterator>
	</tr>
	<tr>
	<th>AR</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="AR"/>)
	</script>
	</td>
	</s:iterator>
	</tr>
	<tr>
	<th>RSquared</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="RSquared"/>)
	</script>
	</td>
	</s:iterator>
	</tr>
	<tr>
	<th>SharpeRatio</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="SharpeRatio"/>)
	</script>
	</td>
	</s:iterator>
	</tr>
	<tr>
	<th>StandardDeviation</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="StandardDeviation"/>)
	</script>
	</td>
	</s:iterator>
	</tr>
	<tr>
	<th>TreynorRatio</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="TreynorRatio"/>)
	</script>
	</td>
	</s:iterator>
	</tr>	
	<tr>
	<th>DrawDown</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="DrawDown"/>)
	</script>
	</td>
	</s:iterator>
	</tr>		
	<tr>
	<th>TotalReturn</th>
	<s:iterator value="#request.smpt">
	<td>
	<script>
	fixNum(<s:property value="Return"/>)
	</script>
	</td>
	</s:iterator>
	</tr>			
	</tbody>
	</table>
	</fieldset>
	</div>		
	
	</div>
	</body>
</html>
