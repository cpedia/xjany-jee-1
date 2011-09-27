<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.lti.system.*;" %>
<%

String domainname="validfi.com";

	domainname="myplaniq.com";
	

%>
<html>
	<head>
		<title>Exception</title>
	</head>
	<body>
	<div align="left">
		<span style=''>The operation cannot be completed , see the following detail message: </span>
		<br>
		<br>
		<s:property value="exception.message"/><br>
		<br>
		<br>
		<br>
		Go back to <a href='http://www.<%=domainname%>' id='link'>www.<%=domainname%></a>.
		<div style='display:none'>		
		<pre>
			<s:property value="exception.message"/>
			
			<s:property value="exceptionStack"/>
		</pre>
		</div>
	</div>
	</body>
</html>
