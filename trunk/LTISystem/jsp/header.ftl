[#ftl]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${pageTitle?default("ValidFi")?html}</title>

<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.ui/1.7.1/ui.core.js"></script>
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.ui/1.7.1/ui.dialog.js"></script>


<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.dimensions.js"></script>
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.bgiframe.js"></script>
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.jdMenu/jquery.positionBy.js"></script>
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.jdMenu/jquery.jdMenu.js"></script>

<link rel="stylesheet" href="${lti.baseUrl}/UserFiles/Image/vf.css" type="text/css" media="screen">
<link rel="stylesheet" href="${lti.baseUrl}/jsp/images/jquery.jdMenu/jquery.jdMenu.css" type="text/css" />
<link rel="stylesheet" href="${lti.baseUrl}/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />

<link rel="stylesheet" href="${lti.baseUrl}/jsp/images/jquery.tablesorter/style.css" type="text/css" />
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
<script type="text/javascript" src="${lti.baseUrl}/jsp/images/jquery.form/jquery.form.js"></script>

<SCRIPT src="${lti.baseUrl}/jsp/portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.js" type="text/javascript"></SCRIPT>
<link href="${lti.baseUrl}/jsp/portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.css" rel="stylesheet" type="text/css" />
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
</head>
<body>
<div id="menu-wrap">
	<div id="header">
		<div id="logo"><a href="${lti.baseUrl}/jsp/main/Main.action"><img style="cursor:hand;border:0"; src="${lti.baseUrl}/jsp/images/logo.png"></a></div> 
		
		<div id="header-content">
			<ul id="head_menu" class="jd_menu">
				<li class="foo" ><a class="doo" href="${lti.baseUrl}/jsp/main/Main.action"><span>Home</span></a></li>
				[@authz.authorize ifAnyGranted="ROLE_SUPERVISOR"]
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/admin/index.jsp"><span>Admin</span></a></li>
				<!-- sandbox page for tryout --->
		        <li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/ajax/CustomizePage.action?pageName=SandBox"><span>SandBox</span></a></li>
		        <li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/clonecenter/Main.action?includeHeader=true&title=Clone Center"><span>Clone center</span></a></li>
				[/@authz.authorize]
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/strategy/Main.action"><span>Strategies</span></a></li>
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/portfolio/Main.action"><span>Portfolios</span></a></li>
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/fundcenter/main.action?includeHeader=true"><span>Funds</span></a></li>
				<li class="accessible"><a class="doo" href="${lti.baseUrl}/jsp/main/Main.action"><span>Tools</span></a>
					<ul>
						<li><a href="${lti.baseUrl}/jsp/mutualfund/calculate.action" target="_blank">Realtime Asset Allocation Analysis</a></li>
						<li><a href="${lti.baseUrl}/jsp/RFA/calculate.jsp" target="_blank">RFA Analysis</a></li>
						<li><a href="${lti.baseUrl}/jsp/MVO/main.jsp" target="_blank">Asset Allocation: Mean Variance Optimization</a></li>
		                <li><a href="${lti.baseUrl}/jsp/blapp/BasicSetup.action?action=create" target="_blank">Asset Allocation: Black-Litterman Method</a></li>
						<li><a href="${lti.baseUrl}/jsp/security/ScreeningMain.action" target="_blank">Securities Screening</a></li>
		                <li><a href="${lti.baseUrl}/jsp/security/Main.action">Individual Securities Quotes</a></li>
		                <li><a href="/LTISystem/jsp/betagain/calculate.action" target="_blank">Beta Gains Analysis</a></li>
					</ul>
				</li>
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/jforum/Main.action"><span>Forums</span></a></li>
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/ajax/CustomizePage.action?pageName=Update&includeHeader=true"><span>Update</span></a></li>
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/ajax/CustomizePage.action?pageName=HelpTutorials&includeHeader=true"><span>Help</span></a></li>
				<li class="foo"><a class="doo" href="${lti.baseUrl}/jsp/ajax/CustomizePage.action?pageName=AboutUs&includeHeader=true"><span>About Us</span></a></li>
			</ul>
		
			<div id="rightside">
				<ul class="jd_menu">
				[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
					<li><a href="${lti.baseUrl}/jsp/register/openRegister.action">Register</a></li>
					<li><a title="log in the system" id="loginEntry" href="javascript:void(0)">Login</a></li>
				[/@authz.authorize]
				[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
					<li><a href="${lti.baseUrl}/jsp/register/ViewUserDetails.action">[@authz.authentication operation="username"/]</a></li>
					<li><a href="${lti.baseUrl}/j_acegi_logout">Logout</a></li>
				[/@authz.authorize]
					
					
				</ul>
			</div>
		</div>
	</div>
	<!-- end of header -->
	<span class="clear"/>

	<!-- google search -->
	<div style="clear:both;width:100%;background:#ffffff;padding:0;margin:0;height:30%;" >	
		<div class="cse-branding-right" style="float:right; background-color:#FFFFFF;color:#000000;width:42%;">
			<div id="security-quote" style="padding:0 4px auto;float:left">
		 		<form action="${lti.baseUrl}/jsp/security/Quote.action">
			 		<input type="text" id="quote-search-field" name="symbol">
			 		<input type="submit" value="Quote">
		 		</form>
		 	</div>
		  <div class="cse-branding-form">
		    <form action="${lti.baseUrl}/jsp/main/search.jsp" id="cse-search-box">
		
		      <div>
		        <input type="hidden" name="cx" value="004465209614154372366:fpwmrrwztqy" />
		        <input type="hidden" name="cof" value="FORID:9" />
		        <input type="hidden" name="ie" value="UTF-8" />
		        <input type="text" name="q" size="31" />
		        <input type="submit" name="sa" value="Search" />
		      </div>
		    </form>
		  </div>
		</div>
	</div>
	<!-- end google search -->
	<span class="clear"/>
	<div id="bar">
		<p class="bar" />
	</div>
</div>
<!-- menu end -->
<span class="clear"/>
<!-- login window -->
<div style="display:none;padding:0;margin:0">
	<div id="login_content" class="flora">
		<form id="form" action="${lti.baseUrl}/j_acegi_security_check" method="POST">

		<table width="100%" border="0" cellspacing="8" cellpadding="0" align="center">
			<tr>
				<td headers="40" colspan="2" align="left">The information you request requires log into the system. Please register for free if you don't have an account. </td>
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
				<td align="center" width="30%">
					<input type="checkbox" name="_acegi_security_remember_me" >
	
				</td>
				<td width="70%"><div style="width:95%" align='left'>Keep me signed in for two weeks</div></td>

			</tr>
			<tr> 
				<td colspan="2" width="100%">
					<div align="center"> 
						<input id="login"  type="submit" name="Submit" value="login"  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; height: 70%; width: 60; 
						color: #000000; background-color:#00CCFF; border:1px solid #000000;"
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'"/>
		                &nbsp; 
		                <input name="reset" type="reset"  id="reset" value="reset "  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; width: 60; 
						color: #000000; background-color: #00CCFF; border: 1px solid #000000;" 
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'"/>
						&nbsp;
						
						<a href="${lti.baseUrl}/jsp/register/openRegister.action">Register</a>
						<br>

					</div>
				</td>
			</tr>
		</table>
		</form>
	</div>   
</div>
<style>
<link rel="stylesheet" href="${lti.baseUrl}/UserFiles/Image/vf.css" type="text/css" media="screen">
</style>
