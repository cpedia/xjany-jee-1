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
	<body>
	
	
	
	
		 
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
						   <s:a href="%{register}">Register an account</s:a>
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
							Invitiation Code:
						</td>
						<td>
							<input type='text' name='inviteCode' value='<s:property value="inviteCodeId"/>' <c:if test="${not empty param.login_error}"> value="<c:out value="${ACEGI_SECURITY_LAST_USERNAME}"/>" </c:if> />
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



		 
</body>
</html>
