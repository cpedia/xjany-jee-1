<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Validate Example</title>
	</head>
	<body>
	<s:fielderror/>
	<br>
	<s:property value="a"/>
	<s:form action="/jsp/example/TestValidate.action">
		<s:textfield name="name"></s:textfield>
		<br>
		<s:textfield name="date"></s:textfield>
		<br>
		
		<s:submit>Submit</s:submit>
	</s:form>
</body>	
</html>
