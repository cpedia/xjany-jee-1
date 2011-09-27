<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="decorator" uri="/WEB-INF/sitemesh-decorator.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/sitemesh-page.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
     response.setHeader("Pragma", "no-cache");
     response.setHeader("Cache-Control", "no-cache");
     response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<decorator:head />
	</head>
	<body>
	<h3>This Page is filtered by SiteMesh!</h3>
	<decorator:body />
	</body>
</html>