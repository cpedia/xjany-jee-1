<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/authz.tld" prefix="authz" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.acegisecurity.AuthenticationException" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>


	<title>MyPlanIQ 401(k), IRA, Variable Annuity and Taxable Account Investment Solutions</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Content-Style-Type" content="text/css" />
	<link href="/LTISystem/jsp/template/ed/css/style.css" rel="stylesheet" type="text/css" />
	<link href="/LTISystem/jsp/template/ed/css/layout.css" rel="stylesheet" type="text/css" />
	<link href="/LTISystem/jsp/template/ed/css/jquery_UI/moss/jquery-ui-1.8.custom.css" rel="stylesheet" type="text/css" />

	<link href="/LTISystem/jsp/template/ed/css/tablesorter/tablesorter.css" rel="stylesheet" type="text/css"/>
	
	
	<script src="/LTISystem/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/jquery_UI/jquery-ui-1.8.custom.min.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/tablesorter/jquery.tablesorter.js" type="text/javascript"></script>
	
	<script src="/LTISystem/jsp/template/ed/js/maxheight.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/cufon/cufon-yui.js" type="text/javascript"></script>

	<script src="/LTISystem/jsp/template/ed/js/cufon/cufon-replace.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/cufon/Franklin_Gothic_Medium_Cond_400.font.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/dd_roundies/dd_roundies.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/initUI_elements.js" type="text/javascript"></script>
	
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
			$("#login_content").dialog({height:270,weight:400,title:"Login",position:"center",autoOpen:false});
	
			$("#loginEntry").click(function(){
				 $("#login_content").dialog("open");
				 var page = window.location;
				 setCookie("requestpage", page, null, "/", null, null);
			});
			
			//initSelects("styleSelect", 150);
			alignHeight();
			equalizeNav();
			
		});
		function equalizeNav() {
			var totalwidth = $('#blueNavArea').innerWidth(true) + 10;
			var numitems = $('.blueNav_item','#blueNavArea').length;
			var padding = numitems *4;
			var newwidth = (Math.floor(totalwidth/numitems)-padding) + "px";
			$('.blueNav_item','#blueNavArea').each(function(){
				$(this).css("width",newwidth);
			});
			// tweak width of last item. Because of floating-point rounding
			// boxes may overflow. Last box shortened for visual compensation
			var lastitem_w = $('.blueNav_item:last','#blueNavArea').width();
			var tweakWidth = (lastitem_w - 26)+"px"
			$('.blueNav_item:last','#blueNavArea').css({"border":"none", "width":tweakWidth})
			
		}
	 </script>
	 <style>
	#topNavArea_v2 {
		padding-top: 20px;
		padding-bottom: 20px;
	}
	#myIQ_logo_v2 {
		float: left;
		height: 106px;
		width: 400px;
	}
	.login_v2 {
		float: right;
		padding-top: 2px;
	}
	.login_v2 input, .headerSearchArea input {
		height: 17px;
		font-size: 10px;
		margin: 0px 0px 0px 6px;
		padding-left: 4px;
		border: 1px solid #899AA0;
	}
	#blueNavArea {
		background-image: url(/LTISystem/jsp/template/ed//images/blueNavBkgd.gif);
		background-repeat: repeat-x;
		border-right-width: 1px;
		border-left-width: 1px;
		border-right-style: solid;
		border-left-style: solid;
		border-right-color: #19411b;
		border-left-color: #19411b;
		border-top-width: 1px;
		border-bottom-width: 1px;
		border-top-style: solid;
		border-bottom-style: solid;
		border-top-color: #B2C1A6;
		border-bottom-color: #B2C1A6;
		height: 52px;
		overflow: hidden;
		width:980px;
	}
	.blueNav_item {
		float: left;
		font-size: 13px;
		color: #2F4D69;
		padding-top: 10px;
		border-right-color: #BCC8D4;
		height: 42px;
		border-right-width: 1px;
		border-right-style: solid;
		text-align: center;
		padding-right: 10px;
		padding-left: 10px;
		line-height: 1.3em;
		cursor: pointer;
		display: block;
	}
	.blueNav_item:hover {
		color: #FFFFFF;
		background-color: #476076;
	}
	.overflow {
		width: 300%;
		
	}
	.overflow a{
		text-decoration:none
	}
	
	
	#topNavArea_v2 {
		padding-top: 20px;
		padding-bottom: 20px;
	}
	#topNav {
		font-size: 13px;
		padding-top: 15px;
		padding-left: 20px;
		padding-bottom: 10px;
		float: left;
	}
	#topNav a {
		color: #FDF3C6;
		float: left;
		padding-right: 20px;
		text-decoration: none;
	}
	#page1 .mast {
		background-image: url(../images/headerV5.gif);
		background-repeat: no-repeat;
		height: 106px;
		border-top-width: 4px;
		border-top-style: solid;
		border-top-color: #A4B2B0;
		margin-bottom: 0px;
		border-bottom-style: none;
	}
	#page1  #main  #header  {
		border-bottom-style: none;
	}
	.headerSearchArea {
		float: right;
		padding-top: 74px;
		margin-right: 20px;
	}
	.regSignin {
		float: right;
		margin-right: 30px;
		margin-top: 5px;
		cursor: pointer;
	}
	</style>

	 
	</head>
	<body id="page1">
	<div id="main">
	  <!-- HEADER -->
		<div id="header">
	    <div class="top">
	    	<div id="#topNavArea">
	    		<div id="topNav">
	    			<a href="/LTISystem" >Home</a>

					<a href="/LTISystem/f401k__strategies.action" >Strategies</a>
					<a href="/LTISystem/f401k__faqs.action" >Support</a>
					<a href="/jforum" >Communities</a>
					<a href="/LTISystem/f401k_blog.action" >Articles</a>
					<a href="/LTISystem/newsletter__main.action" >News Letters</a>
					<div class="clear"></div>

				</div>
				
				<div class="login_v2">
					<authz:authorize ifAllGranted="ROLE_ANONYMOUS">
						<div class="regSignin" id="loginEntry">
							<img src="/LTISystem/jsp/template/ed/images/regSignin.png" alt="" width="154" height="24" />
						</div>
					</authz:authorize>
					<authz:authorize ifNotGranted="ROLE_ANONYMOUS">
						<div class="regSignin" >
							<a href="/LTISystem/j_acegi_logout">Logout &nbsp;</a>
							<a href="/LTISystem/jsp/register/ViewUserDetails.action"><authz:authentication operation="username"/></a>
						</div>
					</authz:authorize>
				</div>
				<div class="clear"></div>
	    	</div>
	    	<div class="mast">

	      	<div id="myIQ_logo_v2"><a href="http://www.myplaniq.com"><img src="/LTISystem/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a></div>
	      	<div class="headerSearchArea">
	      		<table height='100%' align="left">
	                	<tr>
	                    	<td align='right'>
	                    		<form action='/LTISystem/jsp/main/Search.action' id='vf_search' name="vf_search" method='get'>
	                        		<input id='n401k_search_text' name='q' 
									onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Keyword"' size="15" type="text" value='Keyword'
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
									onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Ticker"' size="15" type="text" value='Ticker'
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
			            	        onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Company"' size="15" type="text" value='Company'
									size="15" type="text">
			            	        <input alt='Quote' class="submitSearchTop" type='submit' value=''/>

		            	        </form><!-- end of form -->
		                    </td><!-- end of PlanSearch -->
						</tr>
					</table>
			</div><!-- end of headerSearchArea -->
	      </div><!-- mast-->
			</div><!--end of top-->
	  </div><!--end of header-->
	  
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
		
	
	
	
	
		  <div class="borderGreen">
		  	<div id="content_tier1" class="content_tier1">
		  	
		  	
		  	
		  	
		  	
  		<div class="featured-box">
  		<div >
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
			<authz:authorize ifAnyGranted="ROLE_ANONYMOUS">
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
					
					<tr style="display:none">
						<td align="right">
							<input type="checkbox" name="_acegi_security_remember_me" checked/>
						</td>
						<td>
							Don't ask for my password for two weeks
						</td>
					</tr>
					
					<tr> 
						<td colspan="2">
							<div align="center"> 
								<s:url id="main" namespace="/jsp/main" action="Main"></s:url>
								<input name="Submit" type="submit"  class=uiButton  value="Comfirm" />
								<input name="reset"  type="reset"  class=uiButton  id="reset" value="Reset "/>
								<input type="button" value="Go back" class=uiButton  onclick="goFrontPage()"/>
								
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
			</authz:authorize>
			<authz:authorize ifNotGranted="ROLE_ANONYMOUS">
			<p>You are already logged in.</p>
			</authz:authorize>
		</div>
  		</div>
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
	 						onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Company"' size="15" type="text" value='Company Plan Name'
	
						 size="15" type="text" >
		       				<input alt='Quote' id='submitSearchBottom' type='submit' value=''/>
		   				</form><!-- end of form -->
			 		</div><!--searchForPlan -->
			 
			 </div><!--middleLinks -->
		    
		      <div class="clearfloat"></div>
		  </div>  	<!--content -->


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

		<table width="100%" border="0" cellspacing="8" cellpadding="0" align="center" height="225">
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
			<tr style="display:none">
				<td align="center" width="10%">
					<input type="checkbox" name="_acegi_security_remember_me" checked>
	
				</td>
				<td width="90%"><div style="width:95%" align='left'>Keep me signed in for two weeks</div></td>

			</tr>
			<tr>
			     <td colspan="2" width="100%">
			       <s:url id="forgotpassword" action="ForgotPassword" namespace="/jsp/register"></s:url>
				    <a href="/LTISystem/jsp/register/ForgotPassword.action" >Forgot your password ?</a>
			     </td>
			</tr>
			<tr> 
				<td colspan="2" width="100%">
					<div align="center"> 
						<input id="login"  type="submit" name="Submit" value="Login"   class="uiButton"/>
		                &nbsp; 
		                <input type="button"  value="Register"  onclick="window.location.href='/LTISystem/jsp/register/openRegister.action'"  class="uiButton"/>
						&nbsp; 
						<br>
					</div>
				</td>
			</tr>

		</table>
		</form>
	</div>   <!--login_content -->
</div>
</body>
</html>
