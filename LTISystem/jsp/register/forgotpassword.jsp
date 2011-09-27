<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<html>
<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 


<title>Password Page</title>
</head>
<body>


	<h1>Reset Password</h1>
		<s:form action="ForgotPassword.action" namespace="/jsp/register" theme="simple">
		<s:fielderror name="email" id="email" cssStyle="color:red"></s:fielderror>
		<p>Please fill in your registered e-mail address.</p>
		<p>We will send you an e-mail about password information within 24 hours.</p> 
		<p>If you haven't received our e-mail after 24 hours , please try it again or send email to support@myplaniq.com!</p>
		<h4>&nbsp; &nbsp; <s:text name="E-mail:"></s:text>
		<s:textfield name="email" theme="simple"></s:textfield>
		&nbsp; <s:submit value="Submit" theme="simple"></s:submit>
		</h4>
		</s:form>
		<br>
	

</body>
</html>