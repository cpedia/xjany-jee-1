[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
[#if Parameters.includeHeader?? && Parameters.includeHeader=='false']
[#if Parameters.includeJS?? && Parameters.includeJS=='false'][#else]${head}[/#if]
${body}
[#else]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	[#if title??]
		[#if title=="" && Parameters.title??]
			[#assign title=Parameters.title]
		[/#if]
	[#else]
		[#assign title=Parameters.title]
	[/#if]
	<title>${title!"Ever green plans"}</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="Distribution" content="Global" />
	<meta name="Robots" content="index,follow" />
	
	<link rel="stylesheet" href="/LTISystem/jsp/images/n401k.css" type="text/css" />
	<link rel="icon" type="image/vnd.microsoft.icon" href="http://www.validfi.com/favicon.ico"/>
	<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />

	<script type="text/javascript" src="/LTISystem/jsp/images/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.core.js"></script>
	<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.dialog.js"></script>
	<script type="text/javascript" src="/LTISystem/jsp/images/jquery.form/jquery.form.js"></script>
	${head}
</head>
<body>
<div id="n401k_container">
  <div id="n401k_top_header">
	<table width='100%' height='100%' valign='middle'>
		<tr>
	    	<td align='left' width='100'><form><a href="../jforum">Communities</a></form></td>
	        <td align='left' width='50'><form><a href="jsp/jforum/Main.action">Blogs</a></form></td>
	        <td width=auto class="hidden"> </td> 
            <td align="right">
	            <table height='100%'>
                	<tr>
                    	<td align=right>
                    		<form action='/LTISystem/jsp/main/Search.action' id='vf_search' name="vf_search" method='get'>
                        		<input id='n401k_search_text' name='q' onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Site Search"' size="15" type='text' value='${Parameters.q!"Site Search"}'/>
                            	<input alt='Search'  class='search_button' type='submit' value=''/>
                            	<input name="cx" value="004465209614154372366:fpwmrrwztqy" type="hidden">
      							<input name="cof" value="FORID:9" type="hidden">
                            	<input name="ie" value="UTF-8" type="hidden">
                         	</form><!-- end of form -->
                     	</td><!-- end of search -->
                     	<td align=right valign='middle'>
                     		<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
                           		<input id="n401k_search_text" name="symbol" onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Stock/Fund"' size="15" type="text" value='${Parameters.symbol!"Stock/Fund"}'>
                           		<input alt='Quote' class='search_button' type='submit' value=''/>
                         	</form><!-- end of form -->
                     	</td><!-- end of quote -->
                     	<td align=right valign='middle'>
		                		<form action='/LTISystem/f401k__search.action' id='vf_PlanSearch' name="vf_PlanSearch" method='post'>
		            	    	<input name="groupIDS" value="8" type="hidden"></input>
		            	        <input name="includeHeader" value="true" type="hidden"></input>
		            	        <input name="type" value="2" type="hidden"></input>
		            	        <input name="urlPrefix" value="/LTISystem/f401k_view.action?ID=" type="hidden"></input>
		            	        <input id="n401k_plansearch_text" name="keyword" onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Plan Search"' size="15" type="text" value='${Parameters.symbolPlan!"Plan Search"}'>
		            	        <input alt='Quote' class='search_button' type='submit' value=''/>
	            	        </form><!-- end of form -->
	                    </td><!-- end of PlanSearch -->
                      	<td><form>
							[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
								&nbsp; <a title="log in the system" id="loginEntry" href="javascript:void(0)">Login</a> &nbsp;
						   	 	<a title="Register an account"  href="/LTISystem/jsp/register/openRegister.action">Register</a>
							[/@authz.authorize]
							[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
								<a href="/LTISystem/j_acegi_logout">Logout &nbsp;</a>
								<a href="jsp/register/ViewUserDetails.action">[@authz.authentication operation="username"/]</a>
							[/@authz.authorize]
							</form>
					    </td><!-- end of login/logout -->
					</tr>
				</table><!-- end of inner table -->
			</td>
		</tr>
	</table>
  </div>
  <div id="n401k_header">
    <table width="100%" border="0" height="100%" cellspacing="0" cellpadding="0">
      <tr>
        <td><div id="n401k_logotip">
            <h1 id="n401k_logo"><a href="${lti.baseUrl}/f401k__main.action" title="Company Title - return to the homepage"></a></h1>
          </div></td>
        <td><div id="n401k_header_text">401K, IRA, Self-Directed Plan Investment Solutions</div></td>
      </tr>
    </table>
  </div>
  [#assign ind=request.requestURL?last_index_of("/")] 
  [#assign action_name=request.requestURL?substring(ind+1)]
  <div id="n401k_menu">
	<a href="${lti.baseUrl}/f401k__main.action" [#if action_name=="f401k__main.action"]class="current"[/#if]><b>Home</b></a>
	<a href="${lti.baseUrl}/f401k__list.action" [#if action_name=="f401k__list.action"]class="current"[/#if]><b>Plans</b></a>
	[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
		<a href="${lti.baseUrl}/f401k_admin.action" [#if action_name=="f401k_admin.action"]class="current"[/#if]><b>Admin</b></a>
		<a href="${lti.baseUrl}/jsp/admin/index.jsp" [#if action_name=="index.jsp"]class="current"[/#if]><b>Admin of ValidFi</b></a>
	[/@authz.authorize]
	<a href="${lti.baseUrl}/f401k__iraplans.action" [#if action_name=="f401k__iraplans.action"]class="current"[/#if]><b>IRA</b></a>
	<a href="${lti.baseUrl}/f401k__portfolios.action" [#if action_name=="f401k__portfolios.action"]class="current"[/#if]><b>Portfolios</b></a>
	<a href="${lti.baseUrl}/f401k__strategies.action" [#if action_name=="f401k__strategies.action"]class="current"[/#if]><b>Strategies</b></a>
	<a href="${lti.baseUrl}/f401k__communities.action" [#if action_name=="f401k__communities.action"]class="current"[/#if]><b>Communities</b></a>
    <a href="${lti.baseUrl}/f401k__blogs.action" [#if action_name=="f401k__blogs.action"]class="current"[/#if]><b>Blogs</b></a>
    [@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
    <a href="${lti.baseUrl}/f401k_myaccount.action" [#if action_name=="f401k_myaccount.action"]class="current"[/#if]><b>My Account</b></a>
	[/@authz.authorize]
  	<div class="clear"></div>
  </div>
  <span class='clear'></span>
  [#if page.properties["meta.submenu"]??]
     <div id="vf_submenu">
         <ul>
         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="tools"]
         	<li id='${page.properties["meta.raa"]!"vf_off"}'><a href='/LTISystem/jsp/mutualfund/calculate.action' target='_blank'>Realtime Asset Allocation Analysis</a></li>
         	<li id='${page.properties["meta.mvo"]!"vf_off"}'><a href='/LTISystem/jsp/mvo/input.action' target='_blank'>Mean Variance Optimization</a></li>
         	<li id='${page.properties["meta.blm"]!"vf_off"}'><a href='../blapp/BasicSetup.action?action=create' target='_blank'>Black-Litterman Method</a></li>
         	<li id='${page.properties["meta.screening"]!"vf_off"}'><a href='/LTISystem/jsp/security/ScreeningMain.action' target='_blank'>Securities Screening</a></li>
         	[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
         	<li id='${page.properties["meta.quotes"]!"vf_off"}'><a href='../security/Main.action'>Quotes</a></li>
         	<li id='${page.properties["meta.rfa"]!"vf_off"}'><a href='/LTISystem/jsp/RFA/Main.action' target='_blank'>RFA Analysis</a></li>
         	<li id='${page.properties["meta.bga"]!"vf_off"}'><a href='/LTISystem/jsp/betagain/Main.action' target='_blank'>Beta Gains Analysis</a></li>
         	[/@authz.authorize]
         	[/#if]
         	
            [#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="smoney"]
            <li id='${page.properties["meta.smoney"]!"vf_off"}'><a href='/LTISystem/jsp/fundcenter/SMoney.action'>Smart Money Watch</a></li>
            <li id='${page.properties["meta.smoneyindicators"]!"vf_off"}'><a href='/LTISystem/jsp/fundcenter/SMoneyIndicators.action'>Smart Money Indicators</a></li>
            [/#if]
                
         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="individualstrategy"]
         	<li id='${page.properties["meta.strategy"]!"vf_off"}'><a href='/LTISystem/jsp/strategy/Edit.action?ID=${page.properties["meta.id"]!"null"}'>Strategy Overview</a></li>
         	[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
         	<li id='${page.properties["meta.code"]!"vf_off"}'><a href='/LTISystem/jsp/strategy/EditCode.action?viewCode=true&ID=${page.properties["meta.id"]!"null"}' >Strategy Code</a></li>
         	<li id='${page.properties["meta.modelportfolios"]!"vf_off"}'><a href='/LTISystem/jsp/strategy/ModelPortfolioMain.action?strategyID=${page.properties["meta.id"]!"null"}' >Model Portfolios</a></li>
         	[/@authz.authorize]
         	<li id='${page.properties["meta.news"]!"vf_off"}'><a href='/LTISystem/jsp/news/labels/strategy_${page.properties["meta.id"]!"null"}.html' target='_blank'>News</a></li>
         	[/#if]
         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="strategymaintable"]
         	<li id='${page.properties["meta.strategydesc"]!"vf_off"}'><a href='/LTISystem/jsp/customizepage/StrategyMain.action' >Strategy Description</a></li>
         	<li id='${page.properties["meta.publicstrategy"]!"vf_off"}'><a href='/LTISystem/jsp/strategy/Main.action?group=public'>Public Strategies </a></li>
         	[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
         	<li id='${page.properties["meta.mystrategy"]!"vf_off"}'><a href='/LTISystem/jsp/strategy/Main.action?group=my'>My Strategies</a></li>
         	<li id='${page.properties["meta.premiumsstrategy"]!"vf_off"}'><a href='/LTISystem/jsp/strategy/Main.action?group=premiums'>Premiums Strategies </a></li>
         	[/@authz.authorize]
         	[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
         	<li id='${page.properties["meta.allstrategy"]!"vf_off"}'><a href='/LTISystem/jsp/strategy/Main.action?group=all'>All Strategies </a></li>
         	[/@authz.authorize]
         	[/#if]
         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="portfoliotable"]
         	<li id='${page.properties["meta.portfoliodesc"]!"vf_off"}'><a href='/LTISystem/jsp/portfolio/PortfolioMain.action'>Portfolio Description</a></li>
                <li id='${page.properties["meta.modelPortfolios"]!"vf_off"}'><a href='/LTISystem/jsp/customizepage/ModelPortfolios.action'>ValidFi Model Portfolios</a></li>
         	<li id='${page.properties["meta.publicPortfolio"]!"vf_off"}'><a href='/LTISystem/jsp/portfolio/Main.action?publicPortfolio=true'>Public Portfolios</a></li>
         	[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
         	<li id='${page.properties["meta.myPortfolio"]!"vf_off"}'><a href='/LTISystem/jsp/portfolio/Main.action?myPortfolio=true'>My Portfolios</a></li>
         	<li id='${page.properties["meta.premiumsPortfolio"]!"vf_off"}'><a href='/LTISystem/jsp/portfolio/Main.action?premiumsPortfolio=true'>Premiums Portfolios</a></li>
         	<li id='${page.properties["meta.allPortfolio"]!"vf_off"}'><a href='/LTISystem/jsp/portfolio/Main.action?allPortfolio=true'>All Portfolios</a></li>
         	[/@authz.authorize]
         	[/#if]
        <!--<li id="vf_off"><a href="index.html"><span>Guru Watch</span></a></li>
	   		<li id="vf_off"><a href="index.html"><span>Closed End Funds</span></a></li>
	    	<li id="vf_off"><a href="index.html"><span>Services</span></a></li>-->
         </ul>
     </div> <!-- end of submenu -->  
  [/#if]
  
  
  <div id="n401k_body_div">
   	${body}
  </div>
  <div id="n401k_footer" >
  		<div style='width:48%;float:left'>
			<p class="align-left">			
			© 2009 <strong>LTI Systems, Inc.</strong> &nbsp;|&nbsp;
			<a href="http://www.validfi.com/">Home</a> &nbsp;|&nbsp;
	
			<a href="/LTISystem/jsp/customizepage/AboutUs.action">About Us</a> 
			</p>		
		</div>
	
		<div style='width:48%;float:left'>
			<p class="align-right">
			<a href="/LTISystem/jsp/customizepage/termsuse.action">Terms of Use</a>&nbsp;|&nbsp;
	   	        <a href="/LTISystem/jsp/news/rss.xml">RSS Feed</a>
			</p>
	
		</div>
  </div>
</div>





[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
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
	</div>   <!--login_content -->

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
</div>
[/@authz.authorize]

</body>
</HTML>
[/#if]