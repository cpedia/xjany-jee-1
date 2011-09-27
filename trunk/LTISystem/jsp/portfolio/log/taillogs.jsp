<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<link rel="stylesheet" href="../admin/portfolio/images/style.css" type="text/css" />
	</head>
	<body>
		<s:url id="log_url" namespace="/jsp/portfolio" action="LogMain" includeParams="none">
			<s:param name="portfolioID" value="#request.portfolioID"></s:param>
		</s:url>
	  	<s:a href="%{log_url}">All logs</s:a>
		<table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1" >
			<thead>
			<tr>
				<th class="header">Date</th>
				<th class="header">Message</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator value="#request.logs" status="st">
				<s:if test="#st.index%2==0">
				<tr class='odd'>
				</s:if>
				<s:if test="#st.index%2==1">
				<tr class='even'>
				</s:if>
					<td>
						<s:property value="logDate"/>
					</td>
					<td algin="left">
						<pre><s:property value="message" escape="false"/></pre>
					</td>
				</tr>
			</s:iterator>
			</tbody>
		</table>
	</body>
</html>
