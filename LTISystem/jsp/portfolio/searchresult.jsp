<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.lti.action.flash.URLUTF8Encoder"%>
<%
Integer sort =(Integer) request.getAttribute("Sort");
String url="/LTISystem/jsp/portfolio/SearchResult.action" ;
URLUTF8Encoder uRLUTF8Encoder=new URLUTF8Encoder();
url=uRLUTF8Encoder.encode(url);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>Search Results</title>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<script type="text/javascript">

$(document).ready(function(){
var port=document.location.port;
if(port=="")port='80';
var address=location.hostname;
//alert(port);
$('#flash').load('flash.jsp?port='+port+'&address='+address+'&url=<%=url%>');
})
</script>


</head>
<body>
		<s:if test="name != null">
			search key word is name: <s:property value="name"/>.
		</s:if>
		<s:elseif test="categories != null">
			search key word is categories: <s:property value="categories"/>
		</s:elseif>
		<s:if test="isHolding == true">
		<p>
			<s:text name="holding.tips"></s:text> <s:property value="holdingDate"/>
		</p>
		</s:if>
		<div id="flash">
		</div>
</body>
</html>
