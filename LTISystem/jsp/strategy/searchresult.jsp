<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.lti.action.flash.URLUTF8Encoder"%>
<%
Integer sort =(Integer) request.getAttribute("Sort");
//String url="http://202.116.76.163:8000/LTISystem/jsp/security/ScreeningOutput.action" ;
String url="/LTISystem/jsp/strategy/SearchResult.action" ;
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
$('#flash').load('flash.jsp?port='+port+'&address='+address+'&url=<%=url%>');
})
</script>

</head>
<body>

	<div class="fbbl_center">
		<s:if test="name != null">
			search key word is name: <s:property value="name"/>.
		</s:if>
		<s:elseif test="categories != null">
			search key word is categories: <s:property value="categories"/>
		</s:elseif>
		<div id="flash">
		</div>
	</div>
</body>
</html>
