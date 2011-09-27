[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
[#if Parameters.includeHeader?? && Parameters.includeHeader=='false']
[#if Parameters.includeJS?? && Parameters.includeJS=='false'][#else]${head}[/#if]
${body}
[#else]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="Distribution" content="Global" />
<meta name="Robots" content="index,follow" />
<link rel="stylesheet" href="/LTISystem/jsp/images/vf.css" type="text/css" />
<link rel="icon" type="image/vnd.microsoft.icon" href="http://www.validfi.com/favicon.ico"/>

[#assign ind=request.requestURL?last_index_of("/")] 
[#assign action_name=request.requestURL?substring(ind+1)]
[#if action_name=="EditHolding.action"]
<script src="${lti.baseUrl}/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<link href="/LTISystem/jsp/template/ed/css/jquery_UI/smoothness/jquery-ui-1.8.custom.css" rel="stylesheet" type="text/css" />
<script src="/LTISystem/jsp/template/ed/js/jquery_UI/jquery-ui-1.8.custom.min.js" type="text/javascript"></script>
[#else]
<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />
<script type="text/javascript" src="/LTISystem/jsp/images/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.core.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.dialog.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.form/jquery.form.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.slider.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.datepicker_p.js"></script>
[/#if]



<title>
${title!"ValidFi: Strategies and Analytics"}[#if title?? && title=='']${Parameters.symbol!"ValidFi: Strategies and Analytics"}[/#if]
</title>
[#if Parameters.includeJS?? && Parameters.includeJS=='false']
[#else]
${head}
[/#if]
</head>


<body>
<!-- wrap starts here -->
<div id="vf_wrap">
	<div id="vf_header">
                <!-- Top Part -->
		<div id="vf_topmenu">
                  <table style="width:100%;">
                    <tr>
	             <td style="align: left; width: 50px;" ><a href="/LTISystem/jsp/main/Main.action"><span>Home</span></a></td>
	             [@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
	             <td style="align: left; width: 50px;" ><a href="/jforum"><span>Forums</span></a></td>
	             [/@authz.authorize]
	             <td style="align: left; width: 50px;" ><a href="${lti.baseUrl}/jsp/customizepage/HelpTutorials.action"><span>Help</span></a></td>
	             <td style="align: left; width: 80px;" ><a href="/LTISystem/jsp/customizepage/AboutUs.action"><span>About Us</span></a></td>
	             <td width=auto class="hidden"> </td> 
	             <td nowrap align="right">
                       <table>
                       <tr>
                       <td align=right><form action='/LTISystem/jsp/main/Search.action' id='vf_search' name="vf_search" method='get'>
                             <input id='vf_search-text' name='q' onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Site Search"' size="15" type='text' value='${Parameters.q!"Site Search"}'/>
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
                             <input id="vf_search-text" name="symbol" onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Stock/Fund"' size="15" type="text" value='${Parameters.symbol!"Stock/Fund"}'>
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
                </div> <!-- topmenu -->
                
         <div class="clear" style="padding:10px;">
         <!-- Middle part -->
         <table style="width:100%;">
          <tr>
           <td valign="middle">
            <a href="../main/Main.action"><img src="/LTISystem/jsp/images/vflogo.jpg" border=none></a>
          </td>
          <td valign="middle">
           <h2 align="center">Strategy Research, Smart Money Intelligence, Fund Analysis</h2>
           <h3 align="center"><a href="http://www.myplaniq.com">www.MyPlanIQ.com: Asset Allocation for 401(k), ETFs, Annuities, ...</a></h3>
          </td>
          <!-- <td>
          <img src="images/peace_of_mind.jpg" width="200" height="45" alt="Peace of Mind" class="no-border" />
          </td>
          -->
         </tr>
        </table>
        </div>
        <!-- Lower Part -->
        <div class="clear">
          <table style="width:100%;">
           <tr style="valign:bottom; width:100%;">
            <td style="valign:bottom; width:100%;">
	     <!-- Lower part Menu Tabs -->
		<ul id="vf_lowmenu">
			<li id='${page.properties["meta.home"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/main/Main.action"><span>Home</span></a></li>
			[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
			<li id='${page.properties["meta.admin"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/admin/index.jsp"><span>Admin</span></a></li>
<!--		    <li id='${page.properties["meta.sadbox"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/customizepage/SandBox.action"><span>SandBox</span></a></li>
-->
		    <li id='${page.properties["meta.clonecenter"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/clonecenter/Main.action"><span>Clone center</span></a></li>
			[/@authz.authorize]
			<li id='${page.properties["meta.strategies"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/customizepage/StrategyMain.action"><span>Strategies</span></a></li>
                        <li id='${page.properties["meta.smoney"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/fundcenter/SMoney.action"><span>Smart Money</span></a></li>
			<li id='${page.properties["meta.portfolios"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/portfolio/PortfolioMain.action"><span>Portfolios</span></a></li>
			<li id='${page.properties["meta.funds"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/fundcenter/main.action"><span>Funds</span></a></li>
			<li id='${page.properties["meta.markets"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/fundcenter/Market.action"><span>Markets</span></a></li>
			<li id='${page.properties["meta.tools"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/customizepage/ToolMain.action"><span>Tools</span></a></li>
			 <!-- <li id='${page.properties["meta.forums"]!"vf_off"}'><a href="${lti.baseUrl}/jsp/jforum/Main.action"><span>Forums</span></a></li>  -->
			<li id='${page.properties["meta.articles"]!"vf_off"}'><a href="http://validfi.blogspot.com"><span>Articles</span></a></li>
			
		</ul>	
            </td>
           </tr>
         </table>
        </div>													
      </div>	<!--- end of header -->
     <!-- submenu starts here -->
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
            
            [#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="individualportfolio"]
            <li id='${page.properties["meta.edit"]!"vf_off"}'>
            	<a href='${lti.baseUrl}/jsp/portfolio/Edit.action?ID=${page.properties["meta.id"]!"null"}'>
            		Edit
            	</a>
            </li>
            <li id='${page.properties["meta.holding"]!"vf_off"}'>
            	<a href='${lti.baseUrl}/jsp/portfolio/EditHolding.action?ID=${page.properties["meta.id"]!"null"}'>
            		Original Holding
            	</a>
            </li>
            <li id='${page.properties["meta.holding"]!"vf_off"}'>
            	<a href='${lti.baseUrl}/jsp/portfolio/ViewPortfolio.action?ID=${page.properties["meta.id"]!"null"}'>
            		Simulate Result
            	</a>
            </li>
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
<!-- login window end-->

	<!-- content-wrap starts here -->
	<div id="vf_content-wrap">		
	${body}
	<!-- content-wrap ends here -->	
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
	
<!-- wrap ends here -->
<div style="font: 0px/0px sans-serif;clear: both;display: block"> </div>
</div>

</body>
</html>
[/#if]
