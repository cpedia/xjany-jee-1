<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
<head>
<title>Register Page</title>
</head>
<body>
<div align="left">
<pre>
Example:
String[] ses={"spy","vfinx"};
Date d=securityManager.getValidStartDate(ses);
print("valid start date :"+d);

print("spy start date:"+securityManager.getStartDate(securityManager.get("spy").getID()));
print("vfinx start date:"+securityManager.getStartDate(securityManager.get("vfinx").getID()));
</pre>
</div>
<s:form  action="CodeExecutor" namespace="/jsp/ajax">
<table>
<tr>
<td>Code</td>
<td><textarea name="code" cols="200" rows="30"></textarea></td>
</tr>
<tr>
<td colspan="2">
<center>
<input type="submit" name="submit" value="Submit">
<input type="reset" name="reset" value="Reset">
</center>
</tr>
</table>
</s:form>
</body>
</html>