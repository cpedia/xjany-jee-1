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
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-18922257-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
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


[#if action_name=="f401k__mainv2.action" || action_name?starts_with("getstarted")|| action_name?starts_with("f401k_view")||action_name?starts_with("ViewPortfolio")]
	[#-- for main page --]
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
			$(".qButton").button({
				icons: {
	                primary: "ui-icon-help"
	            },
	            text: false

			});
			$(".tButton").css({"width":"20px","height":"20px"});
			$(".tButton").button({
				icons: {
	                primary: "ui-icon-help"
	            },
	            text: false

			});
			
			
			$(".tButton").click(function(e){
				var tipdiv= $(this).next();
				
				var top=$(this).offset().top; 
				var left=$(this).offset().left;
				
				var html="<div id='qtip' class='qtip' class='display:none'>";
				html+="<div style='text-align:right'><a style='text-decoration: none' href='javascript:void(0)' onclick=\"$('#qtip').fadeOut();$('#qtip').remove();\">X</a></div>";
				html+=tipdiv.html();
				html+="<img class='qtipArrow' src='/LTISystem/jsp/template/gpl/vtip_arrow2.png'></div>";
				$('body').append( html );
                        
				$('#qtip').children(".qtipArrow").css("top", $('#qtip').height()+20+"px").css("left", $('#qtip').width()-5+"px");
	            $('#qtip').css("top", top-$('#qtip').height()-40+"px").css("left", left-$('#qtip').width()+"px").fadeIn("slow");
			});
			
			
			

			Cufon.now();
			//DD_roundies.addRule('.sidebar_box, .sidebar_box_noPadding', '8px', true);
			ieInputFix();
			$("#login_content").dialog({height:280,weight:400,title:"Login",position:"center",autoOpen:false});
	
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
					<a href="${lti.baseUrl}/f401k__strategies.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Strategies</a>

					<a href="${lti.baseUrl}/f401k__faqs.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Support</a>
					<a href="/jforum" [#if action_name=="/jforum"]class="active"[/#if]>Communities</a>
					<a href="${lti.baseUrl}/f401k_blog.action" [#if action_name=="f401k_blog.action"]class="active"[/#if]>Articles</a>
					<a href="${lti.baseUrl}/newsletter__main.action" [#if action_name=="newsletter__main.action"]class="active"[/#if]>Newsletters</a>
					[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
						<a href="${lti.baseUrl}/f401k_admin.action" [#if action_name=="f401k_admin.action"]class="active"[/#if]>AD</a>
						<a href="${lti.baseUrl}/jsp/admin/index.jsp" [#if action_name=="index.jsp"]class="active"[/#if]>VA</a>
					[/@authz.authorize]
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					     <a href="/LTISystem/f401k__plancompare.action"><strong>Subscribe</strong> &nbsp;</a>
					[/@authz.authorize]
					<div class="clear"></div>
				</div>
				
				<div class="login_v2">
				
				
				[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]	
				    <div id="fb-root"></div>
				        <script src="http://connect.facebook.net/en_US/all.js"></script>
				        <script>
			               FB.init({ 
			                 appId:'173189549388799', cookie:false, 
			                 status:true, xfbml:true 
			               });
			           </script>
			
			  <div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;">
				<div id="facebook">
					<div id="login"><fb:login-button perms="email" show-faces="false" width="200" max-rows="1">Login</fb:login-button></div>
					<div id="me" style="display:none"></div>
				</div>
			</div>
				
				<script>
				var div = document.getElementById('me'),
				showMe = function(response) {
				    if (!response.session) {
				        div.innerHTML = '<em>Not Connected</em>';
				    } else {
				        FB.api('/me',
				        function(response) {
				            var html = '<table>';
				            var email = "";
				            email = response["email"]; 
				           <!-- alert(email);-->
				            $.ajax({
				                url: "/LTISystem/fb_facebook.action?includeHeader=false",
				                type: "post",
				                data: "fbEmail=" + email,
				                success: function(result) {
				                    window.location.reload();
				                    if (result.trim().length > 0) {
				                        alert(result);
				                    }
				                }
				            });
				
				            for (var key in response) {
				                html += ('<tr>' + '<th>' + key + '</th>' + '<td>' + response[key] + '</td>' + '</tr>');
				            }
				            div.innerHTML = html;
				        });
				    }
				};
				FB.getLoginStatus(function(response) {
				    $("#login").click(function() {
				        showMe(response);
				    });
				    FB.Event.subscribe('auth.sessionChange', showMe);
				});
				</script>
				[/@authz.authorize]
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					<!--<div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;"><div id="fb-root"></div><script src="http://connect.facebook.net/en_US/all.js#appId=173189549388799&amp;xfbml=1"></script><fb:login-button show-faces="false" width="200" max-rows="1"></fb:login-button></div> -->
			    
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
	      	<div id="myIQ_logo_v2"><a href="/LTISystem/f401k__main.action"><img src="${lti.baseUrl}/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a></div>
	      	<div class="headerSearchArea">
	      		<table height='100%' align="left">
	                	<tr>
	                		
	                     	
	                     	<td align=right valign='middle'>
	                     		<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
	                           		<input id="n401k_search_text" name="symbol" size="15" type="text"
									onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Ticker"' size="15" type="text" value='${Parameters.symbol!"Ticker"}'
									>
	                           		<input alt='Quote' class="submitSearchTop" type='submit' value=''/>
	                         	</form><!-- end of form -->
	                     	</td><!-- end of quote -->
	                     	<td align=right valign='left'>
			                		<form action='/LTISystem/f401k__search.action' id='vf_PlanSearch' name="vf_PlanSearch" method='get'>
	                            	<input name="cx" value="011203528935876500718:qtlemv-pl-8" type="hidden">
	      							<input name="cof" value="FORID:9" type="hidden">
	                            	<input name="ie" value="UTF-8" type="hidden">
			            	    	<input name="groupIDS" value="8" type="hidden"></input>
			            	        <input name="includeHeader" value="true" type="hidden"></input>
			            	        <input name="type" value="2" type="hidden"></input>
			            	        <input name="urlPrefix" value="/LTISystem/f401k_view.action?ID=" type="hidden"></input>
			            	        <input id="n401k_plansearch_text" name="q" 
			            	        onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Keywords/Plan"' size="15" type="text" value='${Parameters.keyword!"Keywords/Plan"}'
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
				<a id="blueNavTab_1" class="blueNav_item" href="/LTISystem/f401k__ira_brokerage_intro.action">Brokerage Investment <br />Plans</a>
				<a id="blueNavTab_2" class="blueNav_item" href="/LTISystem/f401k__401kplans.action">Retirement 401K,&nbsp; <br />403B, 457</a>
				<a id="blueNavTab_3" class="blueNav_item" href="/LTISystem/f401k__annuity_vul_colleges.action">Annuities, VULs, <br />529</a>
				<a class="blueNav_item" href="/LTISystem/f401k_showplanscorelist.action">Plan Rating &amp; <br />	401K/IRA Rollover</a>
				<a id="blueNavTab_4" class="blueNav_item" href="/LTISystem/advanced__strategies.action">Advanced<br /> Users</a>
				<div class="clear"></div>
			</div>
		</div>
			${body}
[#elseif action_name="f401k__main.action"||action_name="f401k__ma.action"||action_name="f401k__mb.action"||action_name="f401k__mc.action"||action_name="f401k__md.action"||action_name="f401k__me.action"||action_name="f401k__mf.action"||action_name="f401k__mg.action"]
	[#-- for main page --]
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
			$("#login_content").dialog({height:280,weight:400,title:"Login",position:"center",autoOpen:false});
	
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
	<style>
	.headerSearchArea {
		padding-top: 10px;
		width:555px;
	}.headerSearchArea input {
		height: 17px;
		font-size: 10px;
		margin: 0px 0px 0px 6px;
		padding-left: 4px;
		border: 1px solid #899AA0;
	}
	</style>
	</head>
	<body id="page1">
	<div id="main">
	  <!-- HEADER -->
	  <div id="header" style="border-bottom:1px solid;">
	    <div class="top">
	    	<div id="#topNavArea">
	    		<div id="topNav">
	    			<a href="/LTISystem" [#if action_name=="/LTISystem"]class="active"[/#if]>Home</a>
					[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
				        <a href="${lti.baseUrl}/jsp/register/ViewUserDetails.action" [#if action_name=="ViewUserDetails.action"]class="active"[/#if]>My Account</b></a>
				        <a href="${lti.baseUrl}/f401k__portfolios.action" [#if action_name=="f401k__portfolios.action"]class="active"[/#if]>My Portfolios</a>
					[/@authz.authorize]
					<a href="${lti.baseUrl}/f401k__strategies.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Strategies</a>

					<a href="${lti.baseUrl}/f401k__faqs.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Support</a>
					<a href="/jforum" [#if action_name=="/jforum"]class="active"[/#if]>Communities</a>
					<a href="${lti.baseUrl}/f401k_blog.action" [#if action_name=="f401k_blog.action"]class="active"[/#if]>Articles</a>

						
							<a href='/LTISystem/advanced__strategies.action'>Advanced</a>
					

					[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
					<a href="${lti.baseUrl}/newsletter__main.action" [#if action_name=="newsletter__main.action"]class="active"[/#if]>Newsletters</a>
					[/@authz.authorize]
					
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
						<a href='/LTISystem/jsp/register/FreeNewsletter.action?action=link'>Free Newsletters</a>
					[/@authz.authorize]



					[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
						<a href="${lti.baseUrl}/f401k_admin.action" [#if action_name=="f401k_admin.action"]class="active"[/#if]>AD</a>
						<a href="${lti.baseUrl}/jsp/admin/index.jsp" [#if action_name=="index.jsp"]class="active"[/#if]>VA</a>
					[/@authz.authorize]

					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					     <a href="/LTISystem/f401k__plancompare.action"><strong>Subscribe</strong> &nbsp;</a>
					[/@authz.authorize]
					<div class="clear"></div>
				</div>
				
				
				
				
				
			    





				
				<div class="login_v2">
				
				

				<div id="fb-root"></div>
				        <script src="http://connect.facebook.net/en_US/all.js"></script>
				        <script>
			               FB.init({ 
			                 appId:'173189549388799', cookie:false, 
			                 status:true, xfbml:true 
			               });
			           </script>
			[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]					
			  <div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;">
				<div id="facebook">
					<div id="login"><fb:login-button perms="email" show-faces="false" width="200" max-rows="1">Login</fb:login-button></div>
					<div id="me" style="display:none"></div>
				</div>
			</div>
				
				<script>
				var div = document.getElementById('me'),
				showMe = function(response) {
				    if (!response.session) {
				        div.innerHTML = '<em>Not Connected</em>';
				    } else {
				        FB.api('/me',
				        function(response) {
				            var html = '<table>';
				            var email = "";
				            email = response["email"]; 
				             <!-- alert(email);-->
				            $.ajax({
				                url: "/LTISystem/fb_facebook.action?includeHeader=false",
				                type: "post",
				                data: "fbEmail=" + email,
				                success: function(result) {
				                    window.location.reload();
				                    if (result.trim().length > 0) {
				                        alert(result);
				                    }
				                }
				            });
				
				            for (var key in response) {
				                html += ('<tr>' + '<th>' + key + '</th>' + '<td>' + response[key] + '</td>' + '</tr>');
				            }
				            div.innerHTML = html;
				        });
				    }
				};
				FB.getLoginStatus(function(response) {
				    $("#login").click(function() {
				        showMe(response);
				    });
				    FB.Event.subscribe('auth.sessionChange', showMe);
				});
				</script>
			[/@authz.authorize]	
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					<!--<div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;"><div id="fb-root"></div><script src="http://connect.facebook.net/en_US/all.js#appId=173189549388799&amp;xfbml=1"></script><fb:login-button show-faces="false" width="200" max-rows="1"></fb:login-button></div>-->
			    
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
	    	<div class="mast" >
	      	<div id="myIQ_logo_v2">
<!--	      		<a href="/LTISystem/f401k__main.action"><img src="${lti.baseUrl}/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a>
-->
                         <a href="/LTISystem/f401k__main.action"><img src="${lti.baseUrl}/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a>
	      	</div>
	      	
	      	<div class="headerSearchArea">
	      		<table border=0 width=100%>
	      		<tr><td>
	      		<table height='100%' align="right" >
	                	<tr>
	                		
	                     	
	                     	<td align=right valign='middle'>
	                     		<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
	                           		<input id="n401k_search_text" name="symbol" size="15" type="text"
									onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Ticker"' size="15" type="text" value='${Parameters.symbol!"Ticker"}'
									>
	                           		<input alt='Quote' class="submitSearchTop" type='submit' value=''/>
	                         	</form><!-- end of form -->
	                     	</td><!-- end of quote -->
	                     	<td align=right valign='left'>
			                		<form action='/LTISystem/f401k__search.action' id='vf_PlanSearch' name="vf_PlanSearch" method='get'>
			           
	                            	<input name="cx" value="011203528935876500718:qtlemv-pl-8" type="hidden">
	      							<input name="cof" value="FORID:9" type="hidden">
	                            	<input name="ie" value="UTF-8" type="hidden">
			            	    	<input name="groupIDS" value="8" type="hidden"></input>
			            	        <input name="includeHeader" value="true" type="hidden"></input>
			            	        <input name="type" value="2" type="hidden"></input>
			            	        <input name="urlPrefix" value="/LTISystem/f401k_view.action?ID=" type="hidden"></input>
			            	        <input id="n401k_plansearch_text" name="q" 
			            	        onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Keywords/Plan"' size="15" type="text" value='${Parameters.keyword!"Keywords/Plan"}'
									size="15" type="text">
			            	        <input alt='Quote' class="submitSearchTop" type='submit' value=''/>
		            	        </form><!-- end of form -->
		                    </td><!-- end of PlanSearch -->
		            	        
						</tr>
					</table>
					</td>
										<td><div style="border: medium none; height: 21px; margin-left: 8px; padding-top: 0px; overflow: hidden; width: 80px;"><fb:like layout="button_count" href="http://www.facebook.com/myplaniq"></fb:like></div></td>
					
					</tr><tr>
					<td>
<!--								<div class="sld_title" style="margin-top:15px;margin-left:-100px;position:relative; float: left;">Smart Strategies for My (Retirement) Investments</div>
-->
<div class="sld_title" style="margin-top:15px;margin-left:-100px;position:relative;">The Best Way to Manage My (Retirement) Investments</div>




					</td>
					</tr>
                                        
				</table>
					
			</div>
	      </div>
			</div>
			
	  </div>
	  
			${body}
[#elseif action_name="f401k__maintest.action"]
	[#-- for maintest page --]
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
	<script src="${lti.baseUrl}/jsp/template/ed/js/cufon/Arial.js" type="text/javascript"></script>
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
			$("#login_content").dialog({height:280,weight:400,title:"Login",position:"center",autoOpen:false});
	
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
	<style>
	.headerSearchArea {
		padding-top: 10px;
		width:555px;
	}.headerSearchArea input {
		height: 17px;
		font-size: 10px;
		margin: 0px 0px 0px 6px;
		padding-left: 4px;
		border: 1px solid #899AA0;
	}
	</style>
	</head>
	<body id="page1">
	<div id="main">
	  <!-- HEADER -->
	  <div id="header" style="border-bottom:1px solid;">
	    <div class="top">
	    	<div id="#topNavArea">
	    		<div id="topNav">
	    			<a href="/LTISystem" [#if action_name=="/LTISystem"]class="active"[/#if]>Home</a>
					[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
				        <a href="${lti.baseUrl}/jsp/register/ViewUserDetails.action" [#if action_name=="ViewUserDetails.action"]class="active"[/#if]>My Account</b></a>
				        <a href="${lti.baseUrl}/f401k__portfolios.action" [#if action_name=="f401k__portfolios.action"]class="active"[/#if]>My Portfolios</a>
					[/@authz.authorize]
					<a href="${lti.baseUrl}/f401k__strategies.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Strategies</a>

					<a href="${lti.baseUrl}/f401k__faqs.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Support</a>
					<a href="/jforum" [#if action_name=="/jforum"]class="active"[/#if]>Communities</a>
					<a href="${lti.baseUrl}/f401k_blog.action" [#if action_name=="f401k_blog.action"]class="active"[/#if]>Articles</a>

						
							<a href='/LTISystem/advanced__strategies.action'>Advanced</a>
					

					[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
					<a href="${lti.baseUrl}/newsletter__main.action" [#if action_name=="newsletter__main.action"]class="active"[/#if]>Newsletters</a>
					[/@authz.authorize]
					
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
						<a href='/LTISystem/jsp/register/FreeNewsletter.action?action=link'>FREE Newsletters</a>
					[/@authz.authorize]

					<table style="margin-top: -5px;" height='100%' align="left" >
	                	<tr>
	                		
	                     	
	                     	<td align=right valign='middle'>
	                     		<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
	                           		<input id="n401k_search_text" name="symbol" size="5" type="text"
									onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Ticker"' size="15" type="text" value='${Parameters.symbol!"Ticker"}'
									>
	                           		<input alt='Quote' class="submitSearchTop" type='submit' style="border: 1px none;" value=''/>
	                         	</form><!-- end of form -->
	                     	</td><!-- end of quote -->
	                     	
		            	        
						</tr>
					</table>
					
					
					[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
						<a href="${lti.baseUrl}/f401k_admin.action" [#if action_name=="f401k_admin.action"]class="active"[/#if]>AD</a>
						<a href="${lti.baseUrl}/jsp/admin/index.jsp" [#if action_name=="index.jsp"]class="active"[/#if]>VA</a>
					[/@authz.authorize]

					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					     <a href="/LTISystem/f401k__plancompare.action"><strong>Subscribe</strong> &nbsp;</a>
					[/@authz.authorize]
					<div class="clear"></div>
				</div>
				
				
				
				
				
			    





				
				<div class="login_v2">
				
				

				<div id="fb-root"></div>
				        <script src="http://connect.facebook.net/en_US/all.js"></script>
				        <script>
			               FB.init({ 
			                 appId:'173189549388799', cookie:false, 
			                 status:true, xfbml:true 
			               });
			           </script>
			[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]					
			  <div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;">
				<div id="facebook">
					<div id="login"><fb:login-button perms="email" show-faces="false" width="200" max-rows="1">Login</fb:login-button></div>
					<div id="me" style="display:none"></div>
				</div>
			</div>
				
				<script>
				var div = document.getElementById('me'),
				showMe = function(response) {
				    if (!response.session) {
				        div.innerHTML = '<em>Not Connected</em>';
				    } else {
				        FB.api('/me',
				        function(response) {
				            var html = '<table>';
				            var email = "";
				            email = response["email"]; 
				             <!-- alert(email);-->
				            $.ajax({
				                url: "/LTISystem/fb_facebook.action?includeHeader=false",
				                type: "post",
				                data: "fbEmail=" + email,
				                success: function(result) {
				                    window.location.reload();
				                    if (result.trim().length > 0) {
				                        alert(result);
				                    }
				                }
				            });
				
				            for (var key in response) {
				                html += ('<tr>' + '<th>' + key + '</th>' + '<td>' + response[key] + '</td>' + '</tr>');
				            }
				            div.innerHTML = html;
				        });
				    }
				};
				FB.getLoginStatus(function(response) {
				    $("#login").click(function() {
				        showMe(response);
				    });
				    FB.Event.subscribe('auth.sessionChange', showMe);
				});
				</script>
			[/@authz.authorize]	
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					<!--<div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;"><div id="fb-root"></div><script src="http://connect.facebook.net/en_US/all.js#appId=173189549388799&amp;xfbml=1"></script><fb:login-button show-faces="false" width="200" max-rows="1"></fb:login-button></div>-->
			    
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
	    	<div class="mast" >
	      	<div id="myIQ_logo_v2">
<!--	      		<a href="/LTISystem/f401k__main.action"><img src="${lti.baseUrl}/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a>
-->
                         <a href="/LTISystem/f401k__main.action"><img src="${lti.baseUrl}/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a>
	      	</div>
	      	
	      	<div class="headerSearchArea">
	      		<table border=0 width=100%>
	      		<tr><td>
	      		
					</td>
										<td><div style="border: medium none; height: 21px; margin-left: 8px; padding-top: 0px; overflow: hidden; width: 80px;"><fb:like layout="button_count" href="http://www.facebook.com/myplaniq"></fb:like></div></td>
					
					</tr><tr>
					<td>
<!--								<div class="sld_title" style="margin-top:15px;margin-left:-100px;position:relative; float: left;">Smart Strategies for My (Retirement) Investments</div>
-->
<div class="sld_title" style="margin-top:15px;margin-left:-100px;position:relative;">The Best Way to Manage My (Retirement) Investments</div>




					</td>
					</tr>
                                        
				</table>
					
			</div>
	      </div>
			</div>
			
	  </div>
	  
			${body}
[#else] 
[#-- for normal page --]
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
			$("#login_content").dialog({height:280,weight:400,title:"Login",position:"center",autoOpen:false});
	
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
		width: 350px;
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
		background-image: url(${lti.baseUrl}/jsp/template/ed//images/blueNavBkgd.gif);
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
		[#if !action_name?contains("getstarted")]
	.content_tier1 h3 {
	font-size: 14px;
	margin-bottom: 5px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
	padding: 0px;
}
	[/#if]
	
	</style>
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
					<a href="${lti.baseUrl}/f401k__strategies.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Strategies</a>
					<a href="${lti.baseUrl}/f401k__faqs.action" [#if action_name=="f401k__faqs.action"]class="active"[/#if]>Support</a>
					<a href="/jforum" [#if action_name=="/jforum"]class="active"[/#if]>Communities</a>
					<a href="${lti.baseUrl}/f401k_blog.action" [#if action_name=="f401k_blog.action"]class="active"[/#if]>Articles</a>



					[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
					<a href="${lti.baseUrl}/newsletter__main.action" [#if action_name=="newsletter__main.action"]class="active"[/#if]>Newsletters</a>
					[/@authz.authorize]
					
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
						<a href='/LTISystem/jsp/register/FreeNewsletter.action?action=link'>Free Newsletters</a>
					[/@authz.authorize]
					
					[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
						<a href="${lti.baseUrl}/f401k_admin.action" [#if action_name=="f401k_admin.action"]class="active"[/#if]>ADMIN</a>
						<a href="${lti.baseUrl}/jsp/admin/index.jsp" [#if action_name=="index.jsp"]class="active"[/#if]>VA</a>
					[/@authz.authorize]
					[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					     <a href="/LTISystem/f401k__plancompare.action"><strong>Subscribe</strong> &nbsp;</a>
					[/@authz.authorize]
					<div class="clear"></div>
				</div>
				
		<div class="login_v2">
				
              

					<div id="fb-root"></div>
				        <script src="http://connect.facebook.net/en_US/all.js"></script>
				        <script>
			               FB.init({ 
			                 appId:'173189549388799', cookie:false, 
			                 status:true, xfbml:true 
			               });
			           </script>
				[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]					
			  <div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;">
				<div id="facebook">
					<div id="login"><fb:login-button perms="email" show-faces="false" width="200" max-rows="1">Login</fb:login-button></div>
					<div id="me" style="display:none"></div>
				</div>
			</div>
				
				<script>
				var div = document.getElementById('me'),
				showMe = function(response) {
				    if (!response.session) {
				        div.innerHTML = '<em>Not Connected</em>';
				    } else {
				        FB.api('/me',
				        function(response) {
				            var html = '<table>';
				            var email = "";
				            email = response["email"]; 
				             <!-- alert(email);-->
				            $.ajax({
				                url: "/LTISystem/fb_facebook.action?includeHeader=false",
				                type: "post",
				                data: "fbEmail=" + email,
				                success: function(result) {
				                    window.location.reload();
				                    if (result.trim().length > 0) {
				                        alert(result);
				                    }
				                }
				            });
				
				            for (var key in response) {
				                html += ('<tr>' + '<th>' + key + '</th>' + '<td>' + response[key] + '</td>' + '</tr>');
				            }
				            div.innerHTML = html;
				        });
				    }
				};
				FB.getLoginStatus(function(response) {
				    $("#login").click(function() {
				        showMe(response);
				    });
				    FB.Event.subscribe('auth.sessionChange', showMe);
				});
				</script>
			[/@authz.authorize]	
				[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					<!--<div style="width: 66px; float: left; margin-top: 7px;margin-right:10px;"><div id="fb-root"></div><script src="http://connect.facebook.net/en_US/all.js#appId=173189549388799&amp;xfbml=1"></script><fb:login-button show-faces="false" width="200" max-rows="1"></fb:login-button></div>-->
			    
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
	      	<div id="myIQ_logo_v2"><a href="/LTISystem"><img src="${lti.baseUrl}/jsp/template/ed/images/logo_v2.gif" alt="" width="327" height="106" /></a></div>
	      	<div class="headerSearchArea">
	      		<table height='100%' align="left">
	                	<tr>
	                		
	                     	
	                     	<td align=right valign='middle'>
	                     		<form action='/LTISystem/jsp/security/Quote.action' id='vf_quote' name="vf_quote" method='get'>
	                           		<input id="n401k_search_text" name="symbol" size="15" type="text"
									onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Ticker"' size="15" type="text" value='${Parameters.symbol!"Ticker"}'
									>
	                           		<input alt='Quote' class="submitSearchTop" type='submit' value=''/>
	                         	</form><!-- end of form -->
	                     	</td><!-- end of quote -->
	                     	<td align=right valign='left'>
			                		<form action='/LTISystem/f401k__search.action' id='vf_PlanSearch' name="vf_PlanSearch" method='get'>
			           
	                            	<input name="cx" value="011203528935876500718:qtlemv-pl-8" type="hidden">
	      							<input name="cof" value="FORID:9" type="hidden">
	                            	<input name="ie" value="UTF-8" type="hidden">
			            	    	<input name="groupIDS" value="8" type="hidden"></input>
			            	        <input name="includeHeader" value="true" type="hidden"></input>
			            	        <input name="type" value="2" type="hidden"></input>
			            	        <input name="urlPrefix" value="/LTISystem/f401k_view.action?ID=" type="hidden"></input>
			            	        <input id="n401k_plansearch_text" name="q" 
			            	        onfocus='this.value=&quot;&quot;' onblur='if(this.value==&quot;&quot;)this.value="Keywords/Plan"' size="15" type="text" value='${Parameters.keyword!"Keywords/Plan"}'
									size="15" type="text">
			            	        <input alt='Quote' class="submitSearchTop" type='submit' value=''/>
		            	        </form><!-- end of form -->
		                    </td><!-- end of PlanSearch -->
		            	        
		            	     	<td><div style="border: medium none;height: 25px;margin-left: 8px;overflow: hidden;width: 80px;"><fb:like layout="button_count" href="http://www.facebook.com/myplaniq"></fb:like></div>
			</td>
						</tr>
					</table>
			</div>
			
			
			<!-- end of headerSearchArea -->
	      </div><!-- mast-->
			</div><!--end of top-->
	  </div><!--end of header-->
	  
		<div id="blueNavArea">
			<div class="overflow">
				<a id="blueNavTab_1" class="blueNav_item" href="/LTISystem/f401k__ira_brokerage_intro.action">Brokerage Investment<br />Plans</a>
				<a id="blueNavTab_2" class="blueNav_item" href="/LTISystem/f401k__401kplans.action">Retirement 401K,&nbsp; <br />403B, 457</a>
				<a id="blueNavTab_3" class="blueNav_item" href="/LTISystem/f401k__annuity_vul_colleges.action">Annuities, VULs, <br />529</a>
				<a class="blueNav_item" href="/LTISystem/f401k_showplanscorelist.action">Plan Rating &amp; <br />	401K/IRA Rollover</a>
				<a id="blueNavTab_4" class="blueNav_item" href="/LTISystem/advanced__strategies.action">Advanced<br /> Users</a>
				<div class="clear"></div>
			</div>
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
				            
				            [#if page.properties["meta.submenu"]?? && page.properties["meta.submenu"]=="individualportfolio"]
				            <li id='${page.properties["meta.edit"]!"vf_off"}'>
				            	<a href='${lti.baseUrl}/jsp/portfolio/Edit.action?operation=editbasic&ID=${page.properties["meta.id"]!"null"}'>
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
				         	<li id='${page.properties["meta.strategy"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/Edit.action?ID=${page.properties["meta.id"]!"null"}'>Strategy Overview</a></li>
				         	[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
				         	<li id='${page.properties["meta.code"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/EditCode.action?viewCode=true&ID=${page.properties["meta.id"]!"null"}' >Strategy Code</a></li>
				         	<li id='${page.properties["meta.modelportfolios"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/strategy/ModelPortfolioMain.action?strategyID=${page.properties["meta.id"]!"null"}' >Model Portfolios</a></li>
				         	[/@authz.authorize]
				         	<li id='${page.properties["meta.news"]!"vf_off"}'><a href='${lti.baseUrl}/jsp/news/labels/strategy_${page.properties["meta.id"]!"null"}.html' target='_blank'>News</a></li>
				         	[/#if]
				         	[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
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
	
	

	[#if action_name?contains("newsletter")]
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
[/#if][#-- end of main action judge --]


  <!-- FOOTER -->
  <div id="footer">
        <div class="footerColumn">
                <a href="#" class="footerColumnHeading">@ 2010 LTI Systems, Inc.</a>
        </div> 
  	<div class="footerColumn">
  		<a href="/LTISystem" class="footerColumnHeading">Company Info</a>
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
  		<a href="/LTISystem/f401k__ma.action" class="footerColumnHeading">Site Overview</a>
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
				<td headers="40" colspan="2" align="left">Log in to myplaniq.com </td>
			</tr> 
		    <tr> 
		    	<td align="left"><b>Name</b></td>
		    	<td>
	
		    		<input id="username" name='j_username' type='text' maxlength="40">

				<label id="Ntip"></label>
				</td>
			</tr>
			<tr> 
				<td align="left"><b>Password</b><font color="#FFFFFF"></font></td>
				<td>
				
					<input id="password" type='password' name='j_password' maxlength="20" />

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
					<div> 
						<input id="login"  type="submit" name="Submit" value="Login"   class="uiButton"/>
						<br>
						<br>
					</div>
				</td>
			</tr>
			<tr>
			     <td colspan="2" width="100%">
			     	Don't have an account?
			     	<div style="margin-top:2px">
				    <a class="uiButton" href="${lti.baseUrl}/jsp/register/openRegister.action" style="text-decoration: none">Sign up now!</a>
				    </div>
			     </td>
			</tr>
			<tr>
			     <td colspan="2" width="100%">
			     	Did you forget your password?
			     	<div style="margin-top:2px">
			     	
				    <a href="/LTISystem/jsp/register/ForgotPassword.action" style="color: #2d45a1;text-decoration: none">Recover it here.</a>
				    </div>
			     </td>
			</tr>
		</table>
		</form>
	</div>   <!--login_content -->
</div>
<!-- Start Quantcast tag -->
<script type="text/javascript">
_qoptions={
qacct:"p-1eBO57wiwyzVw"
};
</script>
<script type="text/javascript" src="http://edge.quantserve.com/quant.js"></script>
<noscript>
<img src="http://pixel.quantserve.com/pixel/p-1eBO57wiwyzVw.gif" style="display: none;" border="0" height="1" width="1" alt="Quantcast"/>
</noscript>
<!-- End Quantcast tag -->
</body>
</html>
[/#if]
