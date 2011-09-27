[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
[#if Parameters.includeHeader?? && Parameters.includeHeader=='false']
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>${title!"ValidFi: Strategies and Analytics"}</title>
[#if Parameters.includeJS?? && Parameters.includeJS=='false']
[#else]
${head}
[/#if]
</head>
<body>
${body}
</body>
</html>
[#else]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta name="Description" content="Information architecture, Web Design, Web Standards." />

<link rel="stylesheet" href="/LTISystem/jsp/images/vfNews.css" type="text/css" />
<link rel="icon" type="image/vnd.microsoft.icon" href="http://www.validfi.com/favicon.ico"/>
<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />


<script type="text/javascript" src="/LTISystem/jsp/images/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.core.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.dialog.js"></script>


<script type="text/javascript" src="/LTISystem/jsp/images/jquery.form/jquery.form.js"></script>

<title>${title!"ValidFi: Strategies and Analytics"}</title>
[#if Parameters.includeJS?? && Parameters.includeJS=='false']
[#else]
[#--]${head}[--]
[/#if]
</head>


<body>
<!-- vN_wrap starts here -->
<div id="vN_wrap">
	<div id="vN_header">
                <!-- Top Part -->
		<div id="vN_topmenu">
                  <table style="width:100%;">
                    <tr>
				<td style="align: left; width: 50px;" ><a href="/LTISystem/jsp/main/Main.action"><span>Home</span></a></td>
	            [@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
                     <td style="align: left; width: 50px;" ><a href="/jforum"><span>Forums</span></a></td>
                     [/@authz.authorize]
	             <td style="align: left; width: 50px;" ><a href="${lti.baseUrl}/jsp/customizepage/HelpTutorials.action"><span>Help</span></a></td>
	             <td style="align: left; width: 80px;" ><a href="/LTISystem/jsp/customizepage/AboutUs.action"><span>About Us</span></a></td>
	             <td width=auto class="hidden"> </td> 
	             <td nowrap align="right">
                       <table>
                       <tr>
                       <td align=right><form action='/LTISystem/jsp/main/Search.action' id='vf_search' name="vf_search" method='get'>
                             <input id='search-text' name='q' onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Site Search"' size="15" type='text' value='${Parameters.q!"Site Search"}'/>
                             <input name="cx" value="004465209614154372366:fpwmrrwztqy" type="hidden">
        					 <input name="cof" value="FORID:9" type="hidden">
                             <input name="ie" value="UTF-8" type="hidden">
                             <input alt='Search'  src='../images/search.gif' type='image' onclick="vf_search()"/>
                           </form>
                           <script>
                           function vf_search(){
                           	document.all.form.vf_search.submit();
                           }
                           </script>
                       </td>
                       <td align=right><form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
                             <input id="search-text" name="symbol" onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Stock/Fund"' size="15" type="text" value='${Parameters.symbol!"Stock/Fund"}'>
                             <input alt='Quote'  src='../images/search.gif' type='image' onclick="vf_quote()"/>
                           </form>
                           <script>
                           function vf_quote(){
                           	document.all.form.vf_quote.submit();
                           }
                           </script>
                       </td>
                        <td>
						[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
							<a title="log in the system" id="loginEntry" href="javascript:void(0)">&nbsp; Login &nbsp;</a>
						    <a title="Register an account"  href="/LTISystem/jsp/register/openRegister.action">Register</a>
						[/@authz.authorize]
						[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
							<a href="/LTISystem/j_acegi_logout">Logout &nbsp;</a>
							<a href="/LTISystem/jsp/register/ViewUserDetails.action">[@authz.authentication operation="username"/]</a>
						[/@authz.authorize]
					    </td>
                       </tr>
                       </table>
                     </td>

                   </tr>
                  </table>
                </div> <!-- vN_topmenu -->
                
         <div class="vN_clear" style="padding:10px;">
         <!-- Middle part -->
         <table style="width:100%;">
          <tr>
           <td valign="middle">
            <a href="../main/Main.action"><span class="logo"><img src="/LTISystem/jsp/images/vflogo.jpg" border=none></span></a>
          </td>
          <td valign="middle">
           <h2>Strategy Research, Smart Money Intelligence, Fund Analysis</h2>
          </td>
          <!-- <td>
          <img src="images/peace_of_mind.jpg" width="200" height="45" alt="Peace of Mind" class="no-border" />
          </td>
          -->
         </tr>
        </table>
        </div>
        <!-- Lower Part -->
        <div class="vN_clear">
          <table style="width:100%;">
           <tr style="valign:bottom; width:100%;">
            <td style="valign:bottom; width:100%;">
	     <!-- Lower part Menu Tabs -->
		<ul id="lowmenu">
			<li id='${page.properties["meta.home"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/main/Main.action"><span>Home</span></a></li>
			[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
			<li id='${page.properties["meta.admin"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/admin/index.jsp"><span>Admin</span></a></li>
		    <li id='${page.properties["meta.sadbox"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/customizepage/SandBox.action"><span>SandBox</span></a></li>
		    <li id='${page.properties["meta.clonecenter"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/clonecenter/Main.action"><span>Clone center</span></a></li>
			[/@authz.authorize]
			<li id='${page.properties["meta.strategies"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/customizepage/StrategyMain.action"><span>Strategies</span></a></li>
                        <li id='${page.properties["meta.smoney"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/fundcenter/SMoney.action"><span>Smart Money</span></a></li>
			<li id='${page.properties["meta.portfolios"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/portfolio/PortfolioMain.action"><span>Portfolios</span></a></li>
			<li id='${page.properties["meta.funds"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/fundcenter/main.action"><span>Funds</span></a></li>
			<li id='${page.properties["meta.markets"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/fundcenter/Market.action"><span>Markets</span></a></li>
			<li id='${page.properties["meta.tools"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/customizepage/ToolMain.action"><span>Tools</span></a></li>
			 <!-- <li id='${page.properties["meta.forums"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/jforum/Main.action"><span>Forums</span></a></li>  -->
			<li id='${page.properties["meta.articles"]!"current"}'><a href="${lti.baseUrl}/jsp/news/news_main.html"><span>Articles</span></a></li>
		
		</ul>	
            </td>
           </tr>
         </table>
        </div>													
      </div>	<!--- end of vN_header -->
     <!-- submenu starts here -->
     <div id="submenu">
         <ul>
        <!--<li id="off"><a href="index.html"><span>Guru Watch</span></a></li>
	   		<li id="off"><a href="index.html"><span>Closed End Funds</span></a></li>
	    	<li id="off"><a href="index.html"><span>Services</span></a></li>-->
         </ul>
     </div> <!-- end of submenu -->

<!-- login window -->
<div style="display:none;padding:0;margin:0">
	<div id="login_content" class="flora">
		<form id="form" action="/LTISystem/j_acegi_security_check" method="POST">

		<table width="100%" border="0" cellspacing="8" cellpadding="0" align="center">
			<tr>
				<td headers="40" colspan="2" align="left">Please register for free if you don't have an account. </td>
			</tr> 
		    <tr> 
		    	<td align="left"><b>Name</b></td>
		    	<td>
	
		    	<p>
		    		<input id="username" name='j_username' type='text' maxlength="20"
					style="width:70%;border-style:solid;border-width:1;padding-left:4;padding-right:4;padding-top:1;padding-bottom:1" 
					onMouseOver="this.style.background='#00CCFF'" 
					onMouseOut="this.style.background='#FFFFFF'" 
					title="Please enter your username (at least 3 characters)"
					class="{required:true,minLength:3}">

				</p>
				<label id="Ntip"></label>
				</td>
			</tr>
			<tr> 
				<td align="left"><b>Password</b><font color="#FFFFFF"></font></td>
				<td>
				
				<p>
					<input id="password" type='password' name='j_password' maxlength="20" 
					style="width:70%;border-style:solid;border-width:1;padding-left:4;padding-right:4;padding-top:1;padding-bottom:1" 
					onMouseOver="this.style.background='#00CCFF'" 
					onMouseOut="this.style.background='#FFFFFF'"
					class="{required:true,minLength:5}"/>

				</p>
				<label id="Ptip"></label>
				</td>
			</tr>
			<tr>
				<td align="center" width="10%">
					<input type="checkbox" name="_acegi_security_remember_me" >
	
				</td>
				<td width="90%"><div style="width:95%" align='left'>Keep me signed in for two weeks</div></td>

			</tr>
			<tr> 
				<td colspan="2" width="100%">
					<div align="center"> 
					    <a href="/LTISystem/jsp/register/openRegister.action">Register</a>
						&nbsp; 
						<input id="login"  type="submit" name="Submit" value="Login"  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; height: 70%; width: 60; 
						color: #000000; background-color:#00CCFF; border:1px solid #000000;"
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'"/>
		                &nbsp; 
		                <input name="reset" type="reset"  id="reset" value="Reset "  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; width: 60; 
						color: #000000; background-color: #00CCFF; border: 1px solid #000000;" 
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'"/>
						&nbsp;
						<br>
					</div>
				</td>
			</tr>
			<tr>
			     <td colspan="2" width="100%">
			       <s:url id="forgotpassword" action="ForgotPassword" namespace="/jsp/register"></s:url>
				    <a href="/LTISystem/jsp/register/ForgotPassword.action" >Forgot your password ?</a>
			     </td>
			</tr>
		</table>
		</form>
	</div>   
</div>

<!-- Please load ui.dialog.packed.js and other neccessary JS-->
<script type="text/javascript">
$(function() {

	function setCookie(name, value, expires, path, domain, secure) {
				document.cookie = name + "=" + escape(value) +
				((expires) ? "; expires=" + expires.toGMTString() : "") +
				((path) ? "; path=" + path : "") +
				((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : ""); 
	}
	
	$.ui.dialog.defaults.bgiframe = true;

 	$("#login_content").dialog({height:300,weight:400,title:"Login",autoOpen:false});

	$("#loginEntry").click(function(){
		 $("#login_content").dialog("open");
		 var page = window.location;
		 setCookie("requestpage", page, null, "/", null, null);
	});
	
});
</script>


<!-- login window end-->

	<!-- content-wrap starts here -->
	<div id="content-wrap">		
	${body}
	<!-- content-wrap ends here -->		
<div style="font: 0px/0px sans-serif;clear: both;display: block"> </div>
	</div>

<!-- footer starts here -->	
<div id="vN_footer">
	
	<div class="vN_footer-left">
		<p class="align-left">			
		&copy; 2009 <strong>LTI Systems, Inc.</strong> |
		<a href="http://www.validfi.com/">Home</a> |
		<a href="/LTISystem/jsp/customizepage/AboutUs.action">About Us</a> 
		</p>		
	</div>
	
	<div class="vN_footer-right">
		<p class="align-right">
		<a href="/LTISystem/jsp/customizepage/termsuse.action">Terms of Use</a>&nbsp;|&nbsp;
   	        <a href="/LTISystem/jsp/news/rss.xml">RSS Feed</a>
		</p>
	</div>
	
</div>
<!-- vN_footer ends here -->
	
<!-- vN_wrap ends here -->
<div style="font: 0px/0px sans-serif;clear: both;display: block"> </div>
</div>


</body>
</html>
[/#if]
