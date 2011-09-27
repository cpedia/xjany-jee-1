<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Validate Example</title>
	</head>
	<body>
	<s:fielderror/>
	<br>
	<s:form action="/jsp/example/Test.action">
		<s:textfield name="ht.name"></s:textfield>
		<br>
		<s:textfield name="ht.passwd"></s:textfield>
		<br>
		<s:submit>Submit</s:submit>
	</s:form>
</body>	
</html>
