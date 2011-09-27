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
[#assign ind=request.requestURL?last_index_of("/")] 
[#assign action_name=request.requestURL?substring(ind+1)]
<title>${title!"MyPlanIQ 401(k), IRA, Variable Annuity and Taxable Account Investment Solutions"}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Style-Type" content="text/css" />

<link href="${lti.baseUrl}/jsp/template/ed/css/style_v2.css" rel="stylesheet" type="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/layout_v2.css" rel="stylesheet" type="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/homepage_v2.css" rel="stylesheet" type="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/jquery_UI/${page.properties["meta.jqueryui"]!"moss"}/jquery-ui-1.8.custom.css" rel="stylesheet" type="text/css" />
<link href="${lti.baseUrl}/jsp/template/ed/css/tablesorter/tablesorter.css" rel="stylesheet" type="text/css"/>


<script src="${lti.baseUrl}/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/jquery_UI/jquery-ui-1.8.custom.min.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/maxheight.js" type="text/javascript"></script>

<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/cufon-yui.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/cufon-replace_v2.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/Franklin_Gothic_Medium_Cond_400.font.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/Myriad_Pro_400.font.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/MyriadProBold_700.font.js" type="text/javascript"></script>

<script src="${lti.baseUrl}/jsp/template/ed/js/dd_roundies/dd_roundies.js" type="text/javascript"></script>
<script src="${lti.baseUrl}/jsp/template/ed/js/initUI_elements.js" type="text/javascript"></script>

<script src="${lti.baseUrl}/jsp/template/ed/js/tablesorter/jquery.tablesorter.js" type="text/javascript"></script>
<script>
	function setCookie(name, value, expires, path, domain, secure) {
				document.cookie = name + "=" + escape(value) +
				((expires) ? "; expires=" + expires.toGMTString() : "") +
				((path) ? "; path=" + path : "") +
				((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : ""); 
	}
	$(document).ready(function() {
		alignHeight();
		equalizeNav();
		$(".uiButton").button();
		Cufon.now();
		//DD_roundies.addRule('.sidebar_box, .sidebar_box_noPadding', '8px', true);
		ieInputFix();
		$("#login_content").dialog({height:300,weight:400,title:"Login",position:"center",autoOpen:false});

		$("#loginEntry").click(function(){
			 $("#login_content").dialog("open");
			 var page = window.location;
			 setCookie("requestpage", page, null, "/", null, null);
		});
		
		if($.browser.msie){
		 	ieInputFix();
		}
		
	});
	
	function ieInputFix() {
		$('input').css("margin-top","-1px");
	}
</script>

<script src="${lti.baseUrl}/jsp/template/ed/js/homepage_v2.js" type="text/javascript"></script>
${head}
</head>
<body id="page1">
<div id="main">
  <!-- HEADER -->
  <div id="header">
    <div class="top">
    	<div id="#topNavArea">
    		<div id="topNav">
    			<a href="/LTISystem" [#if action_name=="/LTISystem"]class="active"[/#if]>Home</a>
				[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
			        <a href="${lti.baseUrl}/jsp/register/ViewUserDetails.action" [#if action_name=="ViewUserDetails.action"]class="active"[/#if]>My Account</b></a>
			        <a href="${lti.baseUrl}/f401k__portfolios.action" [#if action_name=="f401k__portfolios.action"]class="active"[/#if]>My Portfolios</a>
				[/@authz.authorize]
				<a href="${lti.baseUrl}/f401k__faqs.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Support</a>
				<a href="/jforum" [#if action_name=="/jforum"]class="active"[/#if]>Communities</a>
				<a href="${lti.baseUrl}/f401k_blog.action" [#if action_name=="f401k_blog.action"]class="active"[/#if]>Articles</a>
				<a href="${lti.baseUrl}/newsletter__main.action" [#if action_name=="newsletter__main.action"]class="active"[/#if]>News Letters</a>
				[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
					<a href="${lti.baseUrl}/f401k_admin.action" [#if action_name=="f401k_admin.action"]class="active"[/#if]>ADMIN</a>
					<a href="${lti.baseUrl}/jsp/admin/index.jsp" [#if action_name=="index.jsp"]class="active"[/#if]>VF ADMIN</a>
				[/@authz.authorize]
				<div class="clear"></div>
			</div>
			
			<div class="login_v2">
				[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					<div class="regSignin" id="loginEntry">
						<img src="${lti.baseUrl}/jsp/template/ed/images/regSignin.png" alt="" width="154" height="24" />
					</div>
				[/@authz.authorize]
				[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
					<div class="regSignin" >
                                                <a href="/LTISystem/paypal_subscr.action"><strong>Subscribe</strong> &nbsp;</a>

						<a href="/LTISystem/j_acegi_logout">Logout &nbsp;</a>
						<a href="/LTISystem/jsp/register/ViewUserDetails.action">[@authz.authentication operation="username"/]</a>
					</div>
				[/@authz.authorize]
			</div>
			<div class="clear"></div>
    	</div>
    	<div class="mast">
      	<div id="myIQ_logo_v2"><a href="index.html"><img src="${lti.baseUrl}/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a></div>
      	<div class="headerSearchArea">
      		<table height='100%' align="left">
                	<tr>
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
                     	
                     	<td align=right valign='middle'>
                     		<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
                           		<input id="n401k_search_text" name="symbol" size="15" type="text"
								onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Ticker"' size="15" type="text" value='${Parameters.symbol!"Ticker"}'
								>
                           		<input alt='Quote' class="submitSearchTop" type='submit' value=''/>
                         	</form><!-- end of form -->
                     	</td><!-- end of quote -->
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
      </div>
		</div>
  </div>
  
	<div id="blueNavArea">
		<div class="overflow">
			<a id="blueNavTab_1" class="blueNav_item" href="/LTISystem/f401k__ira_brokerage.action">IRA/Brokerage <br />	ETF/Fund Accounts</a>
			<a id="blueNavTab_2" class="blueNav_item" href="/LTISystem/f401k__401kplans.action">Retirement 401K,&nbsp; <br />403, 457</a>
			<a id="blueNavTab_3" class="blueNav_item" href="/LTISystem/f401k__annuity_vul_colleges.action">529/Annuities/VULs</a>
			<a class="blueNav_item" href="/LTISystem/f401k_showplanscorelist.action">Plan Rating &amp; <br />	401K/ IRA Rollover</a>
			<a id="blueNavTab_4" class="blueNav_item" href="/LTISystem/advanced__strategies.action">Advanced</a>
			<div class="clear"></div>
		</div>
	</div>
		${body}
  <!-- FOOTER -->
  <div id="footer">
  	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Company Info</a>
		<a href="secondary.html">About Us</a>
		<a href="secondary.html">News and Press</a>
		<a href="secondary.html">TV and Radio Apearances</a>
		<a href="secondary.html">Our Services</a>
		<a href="secondary.html">Affiliate Program</a>
		<a href="secondary.html">Invite a Friend</a>
		<a href="secondary.html">Contact Us</a>
  	</div>
	
	
  	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Asset Alocation</a>
		<a href="secondary.html">ETF</a>
		<a href="secondary.html">Index Funds</a>
		<a href="secondary.html">Vanguard Funds</a>
		<a href="secondary.html">DFA</a></div>	
	
	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
  	</div>	
	
	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
  	</div>	
	
	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
  	</div>	
	
	<div class="footerColumn">
  		<a href="secondary.html" class="footerColumnHeading">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum Dolor</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum Dolor Con</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
		<a href="secondary.html">Lorem Ipsum</a>
  	</div>	
	
	<div class="clearfloat"></div>
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