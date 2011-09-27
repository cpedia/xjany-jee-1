<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>


<html>
<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 

<style>
p
{
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
}
label
{
float:left;
width:7em;
}

</style>


<title>Password Page</title>
</head>
<body>

<div style="height:550px;">
 		
 	<h1>Reset Password</h1>
	
	<div class="blockmain">
	     <s:property value="actionMessage"/>
		 <s:form action="PasswordReset.action" namespace="/jsp/register" >
		    <s:fielderror name="username" id="username" cssStyle="color:red"></s:fielderror>
		    <p style="display:none">
				<label><font color="red">* </font>UserName:</label>
				<input type="hidden" name="UserName" value='<s:property value=""/>'>
		    </p>
		     <p style="display:none">
				<label><font color="red">* </font>VerifyCode:</label>
				<input type="hidden" name="CharCode" value='<s:property value="c"/>'>
		    </p>
			<p>
			
				<table border=0 width=100%>
					<tr>
						<td colspan=2><s:property value="message"/></td>
					</tr>
					<tr height=40>
						<td width=150>Enter New Password</td>
						<td>
							<input type="password" name="password" value="" id="password">
							<input type="hidden" name="user.Password" value="" id="t_password">
						</td>
					</tr>
					<tr height=40>
						<td>Confirm New Password</td>
						<td>
							<input type="password" name="cpassword" value="" id="cpassword">
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<s:submit value="Submit"  theme="simple" cssClass="uiButton"></s:submit>
												</td>
					
					</tr>
					
				</table>
			
			
		 	
	     </s:form>
      <br>
	</div>
</div>

</body>
</html>