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
[#if title?? && title?length!=0]
[#else]
	[#if Parameters.title?? && Parameters.title?length!=0]
		[#assign title=Parameters.title]
	[#else]
		[#assign title="MyPlanIQ 401(k), IRA, Variable Annuity and Taxable Account Investment Solutions"]
	[/#if]
[/#if]
<title>${title!"MyPlanIQ 401(k), IRA, Variable Annuity and Taxable Account Investment Solutions"}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/style.css" rel="stylesheet" type="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/layout.css" rel="stylesheet" type="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/jquery_UI/${page.properties["meta.jqueryui"]!"moss"}/jquery-ui-1.8.custom.css" rel="stylesheet" type="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/tablesorter/tablesorter.css" rel="stylesheet" type="text/css"/>


<script src="${lti.baseUrl}/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/jquery_UI/jquery-ui-1.8.custom.min.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/tablesorter/jquery.tablesorter.js" type="text/javascript"></script>

<script src="${lti.baseUrl}/jsp/template/ed/js/maxheight.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/cufon-yui.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/cufon-replace.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/Franklin_Gothic_Medium_Cond_400.font.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/dd_roundies/dd_roundies.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/initUI_elements.js" type="text/javascript"></script>

<script>
	function setCookie(name, value, expires, path, domain, secure) {
				document.cookie = name + "=" + escape(value) +
				((expires) ? "; expires=" + expires.toGMTString() : "") +
				((path) ? "; path=" + path : "") +
				((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : ""); 
	}
	$(document).ready(function() {
	
		initButtons();
		Cufon.now();
		DD_roundies.addRule('.sidebar_box, .sidebar_box_noPadding', '8px', true);
		ieInputFix();
		$("#login_content").dialog({height:300,weight:400,title:"Login",position:"center",autoOpen:false});

		$("#loginEntry").click(function(){
			 $("#login_content").dialog("open");
			 var page = window.location;
			 setCookie("requestpage", page, null, "/", null, null);
		});
		
		//initSelects("styleSelect", 150);
		
	});
	
 </script>
 ${head}
</head>
<body id="page1">
<div id="main">
  <!-- HEADER -->
  <div id="header">
    <div class="top">
	 
		 
	 	<div class="searchbar">
	 		[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
				<div class="regSignin" id="loginEntry"><img src="${lti.baseUrl}/jsp/template/ed/images/regSignin.png" alt="" width="154" height="24" /></div>
			[/@authz.authorize]
			[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
				<div class="regSignin" >
				<a href="/LTISystem/j_acegi_logout">Logout &nbsp;</a>
				<a href="/LTISystem/jsp/register/ViewUserDetails.action">[@authz.authentication operation="username"/]</a>
				</div>
			[/@authz.authorize]
                        [#assign ind=request.requestURL?last_index_of("/")] 
			[#assign action_name=request.requestURL?substring(ind+1)]
			[#if action_name=="f401k__main.action"]
			<div style="margin-left:0px;float:left"><a href="http://myplaniq.com/jforum/posts/list/489.page"><img src="/LTISystem/jsp/template/ed/images/SAA_FREE1.gif"alt=""/></a></div>
	 		[/#if]
	 		<div class="searchFields">
	 					<table height='100%' align="left">
                	<tr>
                		<td>Search</td>
                    	<td align='right'>
                    		<form action='/LTISystem/jsp/main/Search.action' id='vf_search' name="vf_search" method='get'>
                        		<input id='n401k_search_text' name='q' 
								onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Keyword"' size="15" type="text" value='${Parameters.q!"Keyword"}'
								 size="15" type='text'/>
                            	<input alt='Search' class="submitSearchTop" type='submit' value=''/>
                            	<input name="cx" value="004465209614154372366:fpwmrrwztqy" type="hidden">
      							<input name="cof" value="FORID:9" type="hidden">
                            	<input name="ie" value="UTF-8" type="hidden">
                         	</form><!-- end of form -->
                     	</td><!-- end of search -->
                     	<td>Stock/Fund</td>
                     	
                     	<td align=right valign='middle'>
                     		<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
                           		<input id="n401k_search_text" name="symbol" size="15" type="text"
								onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Ticker"' size="15" type="text" value='${Parameters.symbol!"Ticker"}'
								>
                           		<input alt='Quote' class="submitSearchTop" type='submit' value=''/>
                         	</form><!-- end of form -->
                     	</td><!-- end of quote -->
                     	<td>Plan</td>
                     	<td align=right valign='left'>
		                		<form action='/LTISystem/f401k__search.action' id='vf_PlanSearch' name="vf_PlanSearch" method='post'>
		            	    	<input name="groupIDS" value="8" type="hidden"></input>
		            	        <input name="includeHeader" value="true" type="hidden"></input>
		            	        <input name="type" value="2" type="hidden"></input>
		            	        <input name="urlPrefix" value="/LTISystem/f401k_view.action?ID=" type="hidden"></input>
		            	        <input id="n401k_plansearch_text" name="keyword" 
		            	        onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Company"' size="15" type="text" value='${Parameters.keyword!"Company"}'
								size="15" type="text">
		            	        <input alt='Quote' class="submitSearchTop" type='submit' value=''/>
	            	        </form><!-- end of form -->
	                    </td><!-- end of PlanSearch -->
					</tr>
				</table>
 			</div>
			<div class="clearfloat"></div>
			</div>
      <div class="mast"><a href="${lti.baseUrl}/f401k__main.action"><img src="${lti.baseUrl}/jsp/template/ed/images/mast.gif" alt="" width="891" height="80" /></a></div>
        [#assign ind=request.requestURL?last_index_of("/")] 
		[#assign action_name=request.requestURL?substring(ind+1)]
		<div class="navbar" id="navbar">
		
				<a href="${lti.baseUrl}/f401k__main.action" [#if action_name=="f401k__main.action"]class="active"[/#if]>HOME</a>
				<a href="${lti.baseUrl}/f401k__plans.action" [#if action_name=="f401k__plans.action"]class="active"[/#if]>RETIREMENT/TAXABLE PLANS</a>
<!--				<a href="${lti.baseUrl}/f401k__iraplans.action" [#if action_name=="f401k__iraplans.action"]class="active"[/#if]>TAXABLE/SELF-DIRECTED</a>
-->
				<a href="${lti.baseUrl}/f401k__strategies.action" [#if action_name=="f401k__strategies.action"]class="active"[/#if]>STRATEGIES</a>
				<a href="${lti.baseUrl}/f401k__portfolios.action" [#if action_name=="f401k__portfolios.action"]class="active"[/#if]>MY PORTFOLIOS</a>
			        [@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
			        <a href="${lti.baseUrl}/jsp/register/ViewUserDetails.action" [#if action_name=="ViewUserDetails.action"]class="active"[/#if]>MY ACCOUNT</b></a>
				[/@authz.authorize]
				<a href="${lti.baseUrl}/f401k_blog.action" [#if action_name=="f401k_blog.action"]class="active"[/#if]>ARTICLES</a>
                                <a href="${lti.baseUrl}/newsletter__main.action" [#if action_name=="newsletter__main.action"]class="active"[/#if]>NEWSLETTERS</a>
				<a href="/jforum" [#if action_name=="/jforum"]class="active"[/#if]>COMMUNITIES</a>
				<a href="${lti.baseUrl}/f401k__faqs.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>SUPPORT</a>
				[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
					<a href="${lti.baseUrl}/f401k_admin.action" [#if action_name=="f401k_admin.action"]class="active"[/#if]>ADMIN</a>
					<a href="${lti.baseUrl}/jsp/admin/index.jsp" [#if action_name=="index.jsp"]class="active"[/#if]>VF ADMIN</a>
				[/@authz.authorize]

		</div>
                [@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
		[#if page.properties["meta.submenu"]??]
			     <div id="submenu">
			         <ul>
			         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="tools"]
			         	<li id='${page.properties["meta.raa"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/mutualfund/calculate.action' target='_blank'>Realtime Asset Allocation Analysis</a></li>
			         	<li id='${page.properties["meta.mvo"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/mvo/input.action' target='_blank'>Mean Variance Optimization</a></li>
			         	<li id='${page.properties["meta.blm"]!"vf_off"}'><a href='../blapp/BasicSetup.action?action=create' target='_blank'>Black-Litterman Method</a></li>
			         	<li id='${page.properties["meta.screening"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/security/ScreeningMain.action' target='_blank'>Securities Screening</a></li>
			         	[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
			         	<li id='${page.properties["meta.quotes"]!"vf_off"}'><a href='../security/Main.action'>Quotes</a></li>
			         	<li id='${page.properties["meta.rfa"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/RFA/Main.action' target='_blank'>RFA Analysis</a></li>
			         	<li id='${page.properties["meta.bga"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/betagain/Main.action' target='_blank'>Beta Gains Analysis</a></li>
			         	[/@authz.authorize]
			         	[/#if]
			         	
			            [#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="smoney"]
			            <li id='${page.properties["meta.smoney"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/fundcenter/SMoney.action'>Smart Money Watch</a></li>
			            <li id='${page.properties["meta.smoneyindicators"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/fundcenter/SMoneyIndicators.action'>Smart Money Indicators</a></li>
			            [/#if]
			                
			         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="individualstrategy"]
			         	<li id='${page.properties["meta.strategy"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/AdminView.action?ID=${page.properties["meta.id"]!"null"}'>Strategy Overview</a></li>
			         	[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
			         	<li id='${page.properties["meta.code"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/AdminView.action?viewCode=true&ID=${page.properties["meta.id"]!"null"}' >Strategy Code</a></li>
			         	<li id='${page.properties["meta.modelportfolios"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/ModelPortfolioMain.action?strategyID=${page.properties["meta.id"]!"null"}' >Model Portfolios</a></li>
			         	[/@authz.authorize]
			         	<li id='${page.properties["meta.news"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/news/labels/strategy_${page.properties["meta.id"]!"null"}.html' target='_blank'>News</a></li>
			         	[/#if]
			         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="strategymaintable"]
			         	<li id='${page.properties["meta.strategydesc"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/customizepage/StrategyMain.action' >Strategy Description</a></li>
			         	<li id='${page.properties["meta.publicstrategy"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/Main.action?group=public'>Public Strategies </a></li>
			         	[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
			         	<li id='${page.properties["meta.mystrategy"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/Main.action?group=my'>My Strategies</a></li>
			         	<li id='${page.properties["meta.premiumsstrategy"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/Main.action?group=premiums'>Premiums Strategies </a></li>
			         	[/@authz.authorize]
			         	[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
			         	<li id='${page.properties["meta.allstrategy"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/Main.action?group=all'>All Strategies </a></li>
			         	[/@authz.authorize]
			         	[/#if]
                                        [@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
			         	[#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="portfoliotable"]
			         	<li id='${page.properties["meta.portfoliodesc"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/portfolio/PortfolioMain.action'>Portfolio Description</a></li>
			                <li id='${page.properties["meta.modelPortfolios"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/customizepage/ModelPortfolios.action'>ValidFi Model Portfolios</a></li>
			         	<li id='${page.properties["meta.publicPortfolio"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/portfolio/Main.action?publicPortfolio=true'>Public Portfolios</a></li>
			         	[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
			         	<li id='${page.properties["meta.myPortfolio"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/portfolio/Main.action?myPortfolio=true'>My Portfolios</a></li>
			         	<li id='${page.properties["meta.premiumsPortfolio"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/portfolio/Main.action?premiumsPortfolio=true'>Premiums Portfolios</a></li>
			         	<li id='${page.properties["meta.allPortfolio"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/portfolio/Main.action?allPortfolio=true'>All Portfolios</a></li>
			         	[/@authz.authorize]
			         	[/#if]
                                        [/@authz.authorize]
			        <!--<li id="vf_off"><a href="index.html"><span>Guru Watch</span></a></li>
				   		<li id="vf_off"><a href="index.html"><span>Closed End Funds</span></a></li>
				    	<li id="vf_off"><a href="index.html"><span>Services</span></a></li>-->
			         </ul>
			     </div> <!-- end of submenu -->  
			  [/#if]
                          [/@authz.authorize]
    </div>
  </div>
  [#if action_name=="f401k__main.action" || action_name=="f401k__main_vf.action" || action_name=="f401k__main_new.action" || action_name?contains("newsletter")]
  	${body}
  [#else]
	  <div class="borderGreen">
	  	<div id="content_tier1" class="content_tier1">
	  		${body}
	  		<div class="clearfloat"></div>
	  		</div>
	  	</div><!--borderGreen -->
	  <!-- CONTENT -->
	  <div id="content">
	    <div class=" middlebar">
		  <div class="middleLinks" id="middleLinks">
				<!-- <a href="/LTISystem/f401k__why.action"> Why MyPlanIQ</a> -->
                                <a href="/LTISystem/UserFiles/Quickstart.pdf">Quick Start</a>
				<a href="/LTISystem/profile__getstarted.action"> Get Started</a>
				<!--<a href="#"> Button With Text</a> -->
				<div id="searchForPlan">
					<form action='/LTISystem/f401k__search.action' id='vf_PlanSearch' name="vf_PlanSearch" method='post'>
	    				<input name="groupIDS" value="8" type="hidden"></input>
	        			<input name="includeHeader" value="true" type="hidden"></input>
	        			<input name="type" value="2" type="hidden"></input>
	       			    <input name="urlPrefix" value="/LTISystem/f401k_view.action?ID=" type="hidden"></input>
	       			    <span>Search For A Plan and Model Portfolios</span>
	        			<input id="n401k_plansearch_text" name="keyword" 
 						onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Company"' size="15" type="text" value='${Parameters.keyword!"Company Plan Name"}'

					 size="15" type="text" >
	       				<input alt='Quote' id='submitSearchBottom' type='submit' value=''/>
	   				</form><!-- end of form -->
		 		</div><!--searchForPlan -->
		 
		 </div><!--middleLinks -->
	    
	      <div class="clearfloat"></div>
	  </div>  	<!--content -->
  [/#if]

  <!-- FOOTER -->
  <div id="footer">
        <div class="footerColumn">
                <a href="#" class="footerColumnHeading">@ 2010 LTI Systems, Inc.</a>
        </div> 
  	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Company Info</a>
		<a href="/LTISystem/f401k__about.action">About Us</a>
                <a href="/LTISystem/f401k__why.action">Why MyPlanIQ</a>
<!--		<a href="secondary.html">News and Press</a>
		<a href="secondary.html">Contact Us</a>
		<a href="secondary.html">TV and Radio Apearances</a>
		<a href="secondary.html">Our Services</a>
		<a href="secondary.html">Affiliate Program</a>
		<a href="secondary.html">Invite a Friend</a>
-->
  	</div>
	
	
  	<div class="footerColumn">
  		<a href="/LTISystem/f401k__faqs.action" class="footerColumnHeading">Support</a>
		<a href="/LTISystem/profile__getstarted.action">Get Started</a>
                <a href="/LTISystem/f401k__faqs.action">FAQs</a>
		<a href="/LTISystem/f401k__glossary.action">Glossary</a>
		<a href="/LTISystem/f401k__strategies.action">Asset Allocation Strategies</a>
	</div>	
	
	<div class="footerColumn">
  		<a href="#" class="footerColumnHeading">Legal</a>
		<a href="/LTISystem/jsp/register/privacy.action">Privacy Policy</a>
		<a href="/LTISystem/jsp/register/termuse2.action">Terms of Service</a>
		<a href="/LTISystem/jsp/register/disclaimer.action">Disclaimer</a>
<!--		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
-->
  	</div>	
	
	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Site Map</a>
<!--		<a href="secondary.html">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
-->
  	</div>	
	
	<div class="footerColumn">
  		<a href="/jforum/rss/forumTopics/61.page" class="footerColumnHeading">RSS Feed  <img border="0" alt="[XML]" src="/jforum/templates/default/images/xml_button.gif"></a>
<!--		<a href="secondary.html">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
-->
  	</div>	
	
<!--	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
  	</div>	
-->	
	<div class="clearfloat"></div>
     <div>
        <p></br><br><font color="#B4D377" size="1.9pt"><b>Disclaimer:</b> Any investment in securities including mutual funds, ETFs, closed end funds, stocks and any other securities could lose money over any period of time. All investments involve risk. Losses may exceed the principal invested. Past performance is not an indicator of future performance. There is no guarantee for future results in your investment and any other actions based on the information provided on the website including but not limited strategies, portfolios, articles, performance data and results of any tools.</font><br></p>

<p><font color="#B4D377" size="1.9pt">The website is not operated by a broker, a dealer, a registered financial planner or a registered investment adviser.</font><br></p>

<p><font color="#B4D377" size="1.9pt">Investment strategies, results and any other information presented on the website are for education and research purpose only. They do not represent financial planning and investment advice. MyPlanIQ does not provide tax or legal advice. They are generic in nature and do not take into account your detailed and complete personal financial facts and needs. You alone are responsible for evaluating the information provided and to decide which securities and strategies are suitable for your own financial risk profile and expectations.</font><br></p>

<p><font color="#B4D377" size="1.9pt">Information provided by the website could be time-sensitive and out of date. There is no guarantee for accuracy and completeness for the contents on the website. Contents are subject to change without notice.</font><br></p>

<p><font color="#B4D377" size="1.9pt">All rights are reserved and enforced. By accessing the website, you agree not to copy and redistribute the information provided herein without the explicit consent from MyPlanIQ.</font><br></p>
   </div>
  </div>
</div>
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
		    		<input id="username" name='j_username' type='text' maxlength="40">

				</p>
				<label id="Ntip"></label>
				</td>
			</tr>
			<tr> 
				<td align="left"><b>Password</b><font color="#FFFFFF"></font></td>
				<td>
				
				<p>
					<input id="password" type='password' name='j_password' maxlength="20" />

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
						<input id="login"  type="submit" name="Submit" value="Login"   class="uiButton"/>
		                &nbsp; 
		                <input type="button"  value="Register"  onclick="window.location.href='${lti.baseUrl}/jsp/register/openRegister.action'"  class="uiButton"/>
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
</div>
</body>
</html>
[/#if]
