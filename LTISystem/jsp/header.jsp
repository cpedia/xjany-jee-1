<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="authz" uri="/WEB-INF/authz.tld"%>

<link rel="icon" type="image/vnd.microsoft.icon" href="http://www.validfi.com/favicon.ico"/>
<style>
/* menu tabs */
#header ul.jd_menu {
	z-index: 999999;
/*	position: absolute; */
   position: float;
   margin:0; padding: 0;
   list-style:none;
	right: 0; 
/*	bottom: 6px !important; bottom: 5px; */
	/*font: bold 13px  Arial, 'Trebuchet MS', Tahoma, verdana,  sans-serif;	*/
}
#header li.foo {
   display:inline;
   margin:0; padding:0;
}
#header li.accessible {
   display:inline;
   margin:0; padding:0;
}
#header a.doo {
   float:left;
   background: url(/LTISystem/UserFiles/Image/tableft.gif) no-repeat left top;
   margin:0;
   padding:0 0 0 4px;
   text-decoration:none;
   font: bold;
}
#header a span {
   float:left;
   display:block;
   background: url(/LTISystem/UserFiles/Image/tabright.gif) no-repeat right top;
   padding:6px 15px 3px 8px;
   color: #FFF;
   font: bold 13px Arial, 'Trebuchet MS', Tahoma, verdana,  sans-serif;
}
/* Commented Backslash Hack hides rule from IE5-Mac \*/
#header a span {float:none;}
/* End IE5-Mac hack */
#header a:hover span {
	color:#FFF;
}
#header a:hover {
   background-position:0% -42px;
}
#header a:hover span {
   background-position:100% -42px;
}
#header #current a {
   background-position:0% -42px;
	color: #FFF;
}
#header #current a span {
   background-position:100% -42px;
	color: #FFF;
}
table{
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
}
/* end menu tabs */
</style>

<DIV id="top" style="WIDTH: 100%">
<table width="100%">
<tr valign="middle">
<td>
	<span class="logo"><a href="../main/Main.action"><img  style="cursor: hand"; src="/LTISystem/jsp/images/logo.png" border=0></a></span>
</td>
<td>
<div align="left">
<div id="header">
	<ul class="jd_menu">
		<li class="foo" ><a class="doo" href="/LTISystem/jsp/main/Main.action"><span><s:text name="home"></s:text></span></a></li>
		<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
		<li class="foo"><a class="doo" href="/LTISystem/jsp/admin/index.jsp"><span><s:text name="admin"></s:text></span></a></li>
		<!-- sandbox page for tryout --->
        <li class="foo"><a class="doo" href="/LTISystem/jsp/ajax/CustomizePage.action?pageName=SandBox"><span><s:text name="SandBox"></s:text></span></a></li>
        <li class="foo"><a class="doo" href="/LTISystem/jsp/clonecenter/Main.action?includeHeader=true&title=Clone Center"><span>Clone center</span></a></li>
		</authz:authorize>
		<li class="foo"><a class="doo" href="/LTISystem/jsp/ajax/CustomizePage.action?pageName=StrategyMain&includeHeader=true"><span><s:text name="Strategies"></s:text></span></a></li>

<!--                <li class="foo"><a class="doo" href="/LTISystem/jsp/strategy/Main.action"><span><s:text name="Strategies"> </s:text></span></a></li>
-->
		<li class="foo"><a class="doo" href="/LTISystem/jsp/portfolio/Main.action"><span><s:text name="Portfolios"></s:text></span></a></li>
                <li class="foo"><a class="doo" href="/LTISystem/jsp/fundcenter/main.action?includeHeader=true"><span><s:text name="Funds"></s:text></span></a></li>

		<li class="accessible"><a class="doo" href="/LTISystem/jsp/ajax/CustomizePage.action?pageName=ToolMain&includeHeader=true"><span><s:text name="Tools"></s:text></span></a>
			<ul>
				<li><a href="/LTISystem/jsp/mutualfund/calculate.action" target="_blank"><s:text name="Realtime Asset Allocation Analysis"></s:text></a></li>
				<li><a href="/LTISystem/jsp/MVO/main.jsp" target="_blank"><s:text name="Asset Allocation: Mean Variance Optimization"></s:text></a></li>
                <li><a href="../blapp/BasicSetup.action?action=create" target="_blank"><s:text name="Asset Allocation: Black-Litterman Method"></s:text></a></li>
				<li><a href="/LTISystem/jsp/security/ScreeningMain.action" target="_blank"><s:text name="Securities Screening"></s:text></a></li>
                <li><a href="../security/Main.action"><s:text name="Individual Securities Quotes"></s:text></a></li>

                 <authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
				<li><a href="/LTISystem/jsp/RFA/calculate.jsp" target="_blank"><s:text name="RFA Analysis"></s:text></a></li>
                		<li><a href="/LTISystem/jsp/betagain/calculate.action" target="_blank"><s:text name="Beta Gains Analysis"></s:text></a></li>
                 </authz:authorize>

			</ul>
		</li>
                <authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
		<li class="foo"><a class="doo" href="/LTISystem/jsp/jforum/Main.action"><span><s:text name="Forums"></s:text></span></a></li>
                 </authz:authorize>
<!--	<li class="foo"><a class="doo" href="../LTI System_API_Document.doc"><span><s:text name="document"></s:text></span></a></li> -->
<!--		<li class="foo"><a class="doo" href="/LTISystem/jsp/ajax/CustomizePage.action?pageName=Update&includeHeader=true"><span><s:text name="Update"></s:text></span></a></li> -->

                
                <li class="foo"><a class="doo" href="/LTISystem/jsp/fundcenter/Market.action?includeHeader=true"><span><s:text name="Markets"></s:text></span></a></li>
                

                <li class="foo"><a class="doo" href="/LTISystem/jsp/news/news_main.html"><span><s:text name="Articles"></s:text></span></a></li>
		<li class="foo"><a class="doo" href="/LTISystem/jsp/ajax/CustomizePage.action?pageName=HelpTutorials&includeHeader=true"><span><s:text name="Help"></s:text></span></a></li>
		<li class="foo"><a class="doo" href="/LTISystem/jsp/ajax/CustomizePage.action?pageName=AboutUs&includeHeader=true"><span><s:text name="About Us"></s:text></span></a></li>
	</ul>
</div> <!-- end of header -->
</div> 
</td>
<td align="right">
<div id="right" align="right">
	<ul class="jd_menu">
	<authz:authorize ifAllGranted="ROLE_ANONYMOUS">
		<li ><a href="/LTISystem/jsp/register/openRegister.action"><s:text name="register"></s:text></a></li>
		<li><a title="log in the system" id="loginEntry" href="javascript:void(0)"><s:text name="login"></s:text></a></li>
		</authz:authorize>
		<authz:authorize ifNotGranted="ROLE_ANONYMOUS">
		<li><a href="/LTISystem/jsp/register/ViewUserDetails.action"><authz:authentication operation="username"></authz:authentication></a></li>
		<li><a href="/LTISystem/j_acegi_logout"><s:text name="Logout"></s:text></a></li>
		</authz:authorize>
	</ul>
</div>
</td>
</tr>
</table>
</DIV>
<div style="float:left;">
<table>
<tr>
<td style="vertical-align: middle;">
<p style="font-size: 15px; font-family: Arial, Helvetica, sans-serif; color:#6C2DC7;"><b><i>Financial Strategies and Analytics</b></i></p>
</td>
</tr>
</table>
</div>
<div style="clear:both;width:100%;background:#ffffff;padding:0;margin:0;height:30%;">
<p align="right">
<div class="cse-branding-right" style='float:right; background-color:#FFFFFF;color:#000000;width:42%;align:right'>
 	<table>
 	<tr>
 	  <td>
 		<form action="/LTISystem/jsp/security/Quote.action">
	 		<input type="text" id="quote-search-field" name="symbol" size=8>
	 		<input type="submit" value="Quote">
 		</form>
      </td>
      <td>
      <form action="http://www.validfi.com/LTISystem/jsp/main/search.jsp" id="cse-search-box">
       <div>
        <input type="hidden" name="cx" value="004465209614154372366:fpwmrrwztqy" />
        <input type="hidden" name="cof" value="FORID:9" />
        <input type="hidden" name="ie" value="UTF-8" />
        <input type="text" name="q" size="25" />
        <input type="submit" name="sa" value="Search" />
        </div>
       </form>
      </td>
     </tr>
  </table>
<!-- <div class="cse-branding-text">
    Search
  </div>
-->
</p>
</div>
</div>
<div style="display:none">
	<div id="login_content" class="flora">
		<form id="form" action="/LTISystem/j_acegi_security_check" method="POST">
		<table width="100%" border="0" cellspacing="8" cellpadding="0" align="center">
			<tr>
				<td headers="40" colspan="2">The information you request requires log into the system. Please register for free if you don't have an account. </td>
			</tr> 
		    <tr> 
		    	<p>
		    	<td align="left"><b>Name</b></td>
		    	<td>
		    		<input id="username" name='j_username' type='text' maxlength="20"
					style="width:70%;border-style:solid;border-width:1;padding-left:4;padding-right:4;padding-top:1;padding-bottom:1" 
					onMouseOver="this.style.background='#00CCFF'" 
					onMouseOut="this.style.background='#FFFFFF'" 
					title="Please enter your username (at least 3 characters)"
					class="{required:true,minLength:3}">
				
					<s:url id="register" action="openRegister" namespace="/jsp/register"></s:url>
					<s:a href="%{register}" value="Register">Register</s:a>
				    <label id="Ntip"></label>
				</td>
				</p>
			</tr>
			<tr> 
			  <p>
				<td align="left"><b>Password</b><font color="#FFFFFF"></font></td>
				<td>
					<input id="password" type='password' name='j_password' maxlength="20" 
					style="width:70%;border-style:solid;border-width:1;padding-left:4;padding-right:4;padding-top:1;padding-bottom:1" 
					onMouseOver="this.style.background='#00CCFF'" 
					onMouseOut="this.style.background='#FFFFFF'"
					class="{required:true,minLength:5}"/>
					
					<s:url id="forgotpassword" action="ForgotPassword" namespace="/jsp/register"></s:url>
				    <a href="../register/forgotpassword.jsp" >Forgot ?</a>
				    <label id="Ptip"></label>
				</td>
			  </p>
			</tr>
			<tr>
				<td align="center" width="30%">
					<input type="checkbox" name="_acegi_security_remember_me" >	
				</td>
				<td width="70%"><div style="width:95%">Keep me signed in for two weeks</div></td>
			</tr>
			<br>
			<tr> 
				<td colspan="2" width="100%">
					<div align="center"> 
						<input id="login"   type="submit" name="Submit" value="login"  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; height: 70%; width: 60; 
						color: #000000; background-color:#00CCFF; border:1px solid #000000;"
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'"/>
		                &nbsp; 
		                &nbsp; 
		                &nbsp; 
		                &nbsp;
		                &nbsp;
		                <input name="reset" type="reset"  id="reset" value="reset "  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; width: 60; 
						color: #000000; background-color: #00CCFF; border: 1px solid #000000;" 
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'"/>						
					</div>
				</td>
				<script type="text/javascript">
				$("#login").click(function(){
					var page = window.location;
					setCookie("requestpage", page, null, "/", null, null);
				})
				
				function setCookie(name, value, expires, path, domain, secure) {
					document.cookie = name + "=" + escape(value) +
					((expires) ? "; expires=" + expires.toGMTString() : "") +
					((path) ? "; path=" + path : "") +
					((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : ""); 
				}
				</script>
			</tr>
		</table>
		</form>
	</div>   
</div>
<!-- Please load ui.dialog.packed.js and other neccessary JS-->
<script>
$("#loginEntry").click(function(){
	 $("#login_content").dialog({height:270,weight:350,title:"Login"});
});

</script>
