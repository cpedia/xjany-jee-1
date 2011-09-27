<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.acegisecurity.AuthenticationException" %>
<html>
<head>
<link rel="stylesheet" href="/LTISystem/jsp/images/vf.css" type="text/css" />
<title>Logging in the LTI System</title>

<style type="text/css">
.btn {
	font-family: Tahoma;
	font-size: .89em;
	color: #000000;
	background-image: url(images/grey-bg.gif);
	border: 1px solid #999999;
	cursor: pointer;
	background-repeat: repeat;
	padding: 2px;
	margin-top: 6px;
	margin-right: 6px;
	margin-bottom: 6px;
	margin-left: 2px;
	width:130px;
}
</style>
</head>


<body>
<div id="vf_wrap">
	<div id="vf_header">
                <!-- Top Part -->
		<div id="vf_topmenu">
            <table style="width:100%">
               <tr>
	           <td style="align: left; width: 50px;" ><a href="/LTISystem/jsp/main/Main.action"><span>Home</span></a></td>
	           <td style="align: left; width: 50px;" ><a href="/LTISystem/jsp/customizepage/HelpTutorials.action"><span>Help</span></a></td>
	           <td style="align: left; width: 80px;" ><a href="/LTISystem/jsp/customizepage/AboutUs.action"><span>About Us</span></a></td>
	           <td width=auto class="hidden"> </td> 
	           <td nowrap align="right">
                  <table>
                     <tr>
                     <td align=right>
                     	<form action='/LTISystem/jsp/main/Search.action' id='vf_search' name="vf_search" method='get'>
                     		<input id='vf_search-text' name='q' onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Site Search"' size="15" type='text' value='Site Search'/>
                     		<input name="cx" value="004465209614154372366:fpwmrrwztqy" type="hidden">
        		     		<input name="cof" value="FORID:9" type="hidden">
                     		<input name="ie" value="UTF-8" type="hidden">
                     		<input alt='Search'  src='jsp/images/search.gif' type='image' onclick="vf_search()"/>
                     	</form>
                        <script>
                           function vf_search(){
                           	document.all.form.vf_search.submit();
                           }
                       </script>
                     </td>
                     <td align=right>
                     	<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
                        	<input id="vf_search-text" name="symbol" onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Stock/Fund"' size="15" type="text" value='Stock/Fund'>
                            <input alt='Quote'  src='jsp/images/search.gif' type='image' onclick="vf_quote()"/>
                        </form>
                        <script>
                           function vf_quote(){
                           	document.all.form.vf_quote.submit();
                           }
                        </script>
                    </td>
                    <td></td>
                    </tr>
                 </table>
                    </td></tr>
                 </table>
     	</div> <!-- topmenu -->     
     	<div class="clear" style="padding:10px;">
         	<!-- Middle part -->
         	<table style="width:100%;">
         		<tr>
           		<td valign="middle">
            		<a href="../main/Main.action"><img src="/LTISystem/jsp/images/vflogo.jpg" border=none></a>
          		</td>
          		<td valign="middle">
           			<h2>Strategy Research, Smart Money Intelligence, Fund Analysis</h2>
         		</td>
            	</tr>
        	</table>
    	</div> 												
   </div>	
   <!--- end of header -->
<div style="margin:5px;border:dotted 1px #CFCFCF; background:#E9EFFF;height:500px;">
<c:if test="${not empty param.login_error}">
  <font color="red">
  <br>
  <p>
  <strong>Sorry, login failed. Please enter your correct user name and password again!</strong><BR><BR>
  </P>
   <!-- Reason: <%= ((AuthenticationException) session.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>-->
  </font>
</c:if>
<p>
<strong>The information you request requires log into the system. Please register for free if you don't have an account. </strong><br>
</p>
<form action="<c:url value='j_acegi_security_check'/>" method="POST" >

	<table width="650" border="0">
	
		<tr> 
			<td align="right">
				User name:
			</td>
			<td>
				<input type='text' name='j_username' <c:if test="${not empty param.login_error}"> value="<c:out value="${ACEGI_SECURITY_LAST_USERNAME}"/>" </c:if> />
			</td>
			
			<td>
			   <s:url id="register" action="openRegister" namespace="/jsp/register"></s:url>
			   <s:a href="%{register}" value="Register">Register an account</s:a>
	 	    </td>
		</tr>
		
		<tr> 
			<td align="right">
				User password:
			</td>
			<td>
				<input type='password' name='j_password' maxlength="20" />
			</td>
			<td>
			    <s:url id="forgotpassword" action="ForgotPassword" namespace="/jsp/register"></s:url>
			    <a href="/LTISystem/jsp/register/ForgotPassword.action" >Forgot password ?</a>
		    </td>
		</tr>
		
		<tr>
			<td align="right">
				<input type="checkbox" name="_acegi_security_remember_me" />
			</td>
			<td>
				Don't ask for my password for two weeks
			</td>
		</tr>
		
		<tr> 
			<td colspan="2">
				<div align="center"> 
					<s:url id="main" namespace="/jsp/main" action="Main"></s:url>
					<!--<s:url id="register" action="openRegister" namespace="/jsp/register"></s:url>
					<s:a href="%{register}" value="Register">Register</s:a>-->
					<input name="Submit" type="submit"  class=btn style='font-weight: bold;' value="Comfirm" />
					<input name="reset"  type="reset"  class=btn style='font-weight: bold;' id="reset" value="Reset "/>
					<input type="button" value="Go back" class=btn style='font-weight: bold;' onclick="goFrontPage()"/>
					
					<script>
					function goFrontPage(){
						var refUrl = document.referrer;
						if(refUrl==null || refUrl==""){
							window.location.href= '/LTISystem/jsp/main/Main.action';
						}
						else
							window.location.href= refUrl;
					}
					</script>
				
				</div>
	 		</td>
		</tr>
		
	</table>
</form>
	<div style="font: 0px/0px sans-serif;clear: both;display: block"> </div>
</div>
<!-- footer starts here -->	
<div id="vf_footer">
	
	<div class="vf_footer-left">

		<p class="align-left">			
		&copy; 2009 <strong>LTI Systems, Inc.</strong> &nbsp;|&nbsp;
		<a href="http://www.validfi.com/">Home</a> &nbsp;|&nbsp;
		<a href="/LTISystem/jsp/customizepage/AboutUs.action">About Us</a> 
		</p>		
	</div>

	
	<div class="vf_footer-right">
		<p class="align-right">
		<a href="/LTISystem/jsp/customizepage/termsuse.action">Terms of Use</a>&nbsp;|&nbsp;
   	        <a href="/LTISystem/jsp/news/rss.xml">RSS Feed</a>
		</p>
	</div>
	
</div>

<!-- footer ends here -->
</div>
</body>
</html>

