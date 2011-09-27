<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="com.lti.system.*;" %>
<html>
<head>

<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache,must-revalidate"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 
<META HTTP-EQUIV="expires" CONTENT="wed,26feb199708:21:57gmt"> 

<script type="text/javascript" src="/LTISystem/jsp/images/tiny_mce/tiny_mce.js"></script>

<script type="text/javascript" src="/LTISystem/jsp/register/verificationCode.js"></script>  
<script type="text/javascript" src="/LTISystem/jsp/register/loadXMLdata.js?id=2"></script>
<script type="text/javascript" src="../images/md5.js"></script>



<%
String url= request.getRequestURL().toString();
boolean flag=false;
String[] strs=Configuration.get("f401kdomain").toString().split("\\|");
for(int i=0;i<strs.length;i++){
	if(url.toLowerCase().contains(strs[i].trim())){
		flag=true;
		break;
	}
}
if(flag==false){
%>
<link href="/LTISystem/jsp/template/ed/css/tablesorter/tablesorter.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />
<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.tabs.js"></script>
<%
}else{
%>
<link href="/LTISystem/jsp/template/ed/css/jquery_UI/moss/jquery-ui-1.8.custom.css" rel="stylesheet" type="text/css" />
<%
}		

%>







<!--<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />  -->
<!-- these JSes are necessary START-->
<!--<script type="text/javascript" src="../images/jquery.ui/1.7.1/ui.tabs.js"></script>  -->
<!--<SCRIPT src="../images/jquery.ToggleVal/jquery.toggleval.js" type="text/javascript" ></SCRIPT>  -->
<!--<link href="../images/jquery.ToggleVal/screen.css" rel="stylesheet" type="text/css" media="screen"/>  -->


<script type="text/javascript">
var _state;
var _country;
$(function () {
	var d = new Date();

	 $(".fbbl_center").tabs({ fx: { opacity: 'toggle' } });
	<s:if test="action=='viewUserDetails'||action=='updateUserDetails'">
	$("#emailsettings").html("Loading Data...");
	$("#emailsettings").load("ViewEmailList.action?includeHeader=false&time="+d.getTime());
	$("#user_myaccount").html("Loading Data...");
	$("#user_myaccount").load("userMyAccount.action?includeHeader=false&time="+d.getTime());
	$("#user_subscr").html("Loading Data...");
	$("#user_subscr").load("/LTISystem/paypal_center.action?includeHeader=false&time="+d.getTime());
	_state='<s:property value="user.addressState"/>';
	_country='<s:property value="user.addressCountry"/>';
	</s:if>
});
function onclk(){
		<s:if test="action=='openRegister'||action=='register'">
		var email=$('#u_email').val();
		var password=$('#i_password').val();
		var c_password=$('#c_password').val();
		var u_name=$('#u_username').val();
		var r_state=$('#select_state').val();
		var r_country=$('#select_country').val();
		var se_country=$('#write_country').val();
		var select = document.getElementById("select_state"); 
		var select1 = document.getElementById("select_country")
		var input = document.getElementById("write_state");
		var input1 = document.getElementById("write_country");
		if(email==''){
			alert("Please input your email address.");
			return;
		}
		if(u_name==''){
			alert("Please input your username.");
			return;
		}
		var rName=new RegExp("@","g");
		if(rName.test(u_name)){
			alert("The \"@\" shouldn't be in username.Please input your username.");
			return;
		}
		if(u_name.indexOf(" ")!=-1){
			alert("The white space shouldn't be in username.Please input your username.");
			return;
		}
		if(password==''||password.length<6){
			alert("Please input your new password with more than 6 characters.");
			return;
		}
		if(c_password==''||c_password.length<6){
			alert("Please input your confirm password correctly.");
			return;
		}
		if(c_password!=password){
			alert("Your confirm password is not the same as the password, please check!");
			return;
		}
		$('#t_password').val(MD5(password));
		//if(r_state!="" && r_country!="United States"){
		//	alert("The selection of the country is not the United States.\n You should not choose the state.");
		//	return;
		//}
		if(r_state==""){
			select.name="";
			input.name="user.addressState";
			input.readOnly=false;
		}
		if(se_country!=null && se_country!=""){
			select1.name="";
			input1.name="user.addressCountry";
		}
		
		isRightCode();
		</s:if>
		<s:elseif test="action=='viewUserDetails'||action=='updateUserDetails'">
		var  message="";
		var r_state=$('#select_state').val();
		var r_country=$('#select_country').val();
		//var ur_name=$('#ur_name').val();
		//var ur_email=$('#u_email').val();
		var se_state=$('#write_state').val();
		var se_country=$('#write_country').val();
		var select = document.getElementById("select_state"); 
		var select1 = document.getElementById("select_country")
		var input = document.getElementById("write_state");
		var input1 = document.getElementById("write_country");
		//if(ur_name==''){
		//	alert("The username should not be null.");
		//	return;
		//}
		//if(ur_email==''){
		//	alert("The email should not be null.");
		//	return;
		//}
		//var rName=new RegExp("@","g");
		//if(rName.test(ur_name)){
		//	alert("The \"@\" shouldn't be in username.Please input your username.");
		//	return;
		//}
		//if(r_state!="" && r_country!="United States"){
		//	message="The selection of the country is not the United States.\n You should not choose the state.";
		//	//alert(message);
		//	return message;
		//}
		if(r_state==""){
			select.name="";
			input.name="user.addressState";
			input.readOnly=false;
		}
		if(se_country!=null && se_country!=""){
			select1.name="";
			input1.name="user.addressCountry";
		}
		<s:if test="isfacebook">
		  if(changePassword_fb()){
			isRightCode();
		  }
		</s:if>
		<s:else>
		if(checkPassword()){
			isRightCode();
		}
		</s:else>
		</s:elseif> 
};


function ReImgSize() {
    var await = document.getElementById("chart"); 
    var imgall = await.getElementsByTagName("img"); 
    for (i = 0; i < imgall.length; i++) {
        if (imgall[i].height > 180) 
        {
            var oWidth = imgall[i].width; 
            var oHeight = imgall[i].height;
            var num = oWidth/oHeight; 
            imgall[i].width = 180*num; 
            imgall[i].height = "180"; 
        }
    }
}


$(function(){ ReImgSize();});
</script>

<style>
p
{
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
}
label
{
float:left;
width:10em;
}

#userInfo input,#userInfo select{
	width:400px;
	border:1px solid #ccc;
	height:22px;
}
#userInfo textarea{
	width:400px;
	height: 120px;
	border:1px solid #ccc;
}

span.write_own {
color:#757575;
display:block;
font-size:1.2em;
margin:0.1em 0;
padding-left: 9em;
}

#verifyCode
{
text-align:bottom;
}
.ui-widget-content { border: 1px solid #72b42d; background: #ffffff url(images/ui-bg_inset-soft_20_566f57_1x100.png) 50% bottom repeat-x; color: #2A5333; }
.ui-widget-content a { color: #2A5333; }
</style>

<s:if test="action=='viewUserDetails'||action=='updateUserDetails'">
<title>My Account</title>
</s:if>
<s:else>
<title>Register Page</title>
</s:else>

</head>
<body>
<div class="fbbl_center">
	<ul>
	   <s:if test="action=='openRegister'||action=='register'">
		
		<li><a href="#userInfo"><span>Register</span></a></li>
		</s:if>
		<s:elseif test="action=='viewUserDetails'||action=='updateUserDetails'">
		    <li><a href="#userInfo"><span>My Account Profile</span></a></li>
		    <li><a href="#user_myaccount"><span>My Risk Profile</span></a></li>
			<li><a href="#emailsettings"><span>My Email Alert Setting</span></a></li>		
			<li><a href="#user_subscr"><span>My Subscription Center</span></a></li>
		</s:elseif> 
	</ul>
	<div id="userInfo">
		<s:property value="actionMessage"/>
		<s:form namespace="/jsp/register" id="registerForm" name="registerForm" theme="simple" enctype ="multipart/form-data">
		<s:if test="action=='openRegister'||action=='register'">
		<s:hidden id="id" name="user.ID" value="%{user.ID}"></s:hidden>
		<s:hidden id="cAction" value="%{action}"></s:hidden>
		
		
		
		<s:if test="regType!=null && regType=='subscr'">
		<p style="color:red; font-size:1.3em"><strong>Before you subscribe to MyPlanIQ, you need to register first. Afterward, you will be directed to the subscription page.</strong></p>
                <p>See <a href="/LTISystem/f401k__plancompare.action">Subscription Plan Comparison</a> for subscription benefits.</p>
		<s:hidden id="typeid" name="type" value="subscrlogin"></s:hidden>
		</s:if>
		<p>
		 	<label>Invitation Code:</label>
		 	<input type="text" name="inviteCodeId" value='<s:property value="inviteCodeId"/>'>
		 </p>
		<p style="color:red">Items with * are required!</p>
		<span><s:fielderror name="username" id="username" cssStyle="color:red"></s:fielderror></span><br>
		<s:if test="errString!=null">
		<p style="color:red"><s:property value="errString"/></p>
		</s:if>
		<p>
			 <label> <font color="red">* </font> Email:</label>
			 <span><input type="text" name="user.EMail" id="u_email" value='<s:property value="user.EMail"/>'></span>
			 <font color="red">You will use this Email to log in to our system.</font>
	 	</p>
	 	<p>
			<label><font color="red">* </font>UserName:</label>
			<span><input type="text" id="u_username" name="user.UserName" value='<s:property value="user.UserName"/>'> </span><font color="red">You can also use this UserName to log in to our system.</font>
		</p>
		<p>
			<label><font color="red">* </font> Password:</label>
			<input type="password" name="i_password" value="" id="i_password">
			<input type="hidden" name="user.Password" value="" id="t_password">
		</p>
		<p>
			<label><font color="red">* </font>Confirm Password:</label>
			<input type="password" name="c_user.Password" value="" id="c_password">
	    </p>
	    
	     <p>
			 <label>First Name:</label>
			 <input type="text" name="user.firstName" value='<s:property value="user.firstName"/>'>
		 </p>
		 <p>
			 <label>Last Name:</label>
			 <input type="text" name="user.lastName" value='<s:property value="user.lastName"/>'>
		 </p>
		 <p>
			 <label>Telephone:</label>
			 <input type="text" name="user.Telephone" value='<s:property value="user.Telephone"/>'>
		 </p>
		 <p>
			  <label>Detailed Address:</label>
			  <textarea  name="user.Address" ><s:property value="user.Address"/></textarea>
		 </p>
		 <p>
			 <label>City:</label>
			 <input type="text" name="user.addressCity" value='<s:property value="user.addressCity"/>'>
		 </p>
		 <p>
			 <label>State:</label>
			 <div id="div_select_state">
				<select id="select_state" name="user.addressState" onchange="getstateValue();"></select>
				
			</div>
			
		 </p>
		 <p style="display:none">
		 	<label>&nbsp;</label>
		 	<input id="write_state" type="text" name=""> Other?
		 </p>
		 <p></p>
		 <p>
			 <label>ZIP/Postal Code:</label>
			 <input type="text" name="user.addressZip" value='<s:property value="user.addressZip"/>'>
		 </p>
		 <p>
			 <label>Country:</label>
			 <div id="div_select_country">
					<select id="select_country" name="user.addressCountry"></select>
			</div>  	
		 </p>
		 <p style="display:none">
		 	<label>&nbsp;</label>
		 	<input id="write_country" type="text" name=""> Other?
		 </p>
		 <p></p>
		 <p>
		      <label id="verifyCode"> Verify Image:</label>
		      <s:url action="Generate.action" namespace="/jsp/verifycode" id="generateCode"></s:url>
			  <img id="imgObj" alt="" src="<s:property value='generateCode'/>"/> 
			   &nbsp;
			  <a href="javascript:void(0)" class="uiButton" onclick="changeImg()">change</a>  
		  </p>
          <p>
			  <label id="verifyCode"><font color="red">* </font> Verify Code:</label>
			  <input id="veryCode" name="veryCode" type="text"/>
		  </p>
		  
		  
		  
		  
		  
		  
		  
		  <p>
		      <font color="red">* </font><strong>Please read &nbsp;</strong>
			<%
				if(flag==false){
			%>
				<a target="_blank" href="http://www.validfi.com/LTISystem/register__termuse1.action">
		      		<strong>ValidFi.com's Terms of Use</strong>
			 	</a>
			<%
				}else{
			%>
				<a style="text-decoration: none;" target="_blank" href="http://www.myplaniq.com/LTISystem/register__termuse2.action">
		      		<strong>MyPlanIQ.com's Terms of Use</strong>
			 	</a>
			<%  
				}		
				
				
				%>
		  </p>
		  <p>
		      <td colspan="1" align="center">
              <input style="width:280px;height:26px" id="submitbutton" class="uiButton" name="submitbutton" type="button"  value="I agree the Terms of Use, create an account." onclick="onclk()"/>
              </td>
			  <input type="reset" class="uiButton" name="reset" value="Reset" style="width:80px;height:26px">
		  </p>
		 <p>
		       <div id="Message"><input type="hidden" size="55" readonly id="actionMessage"/></div> 
		 </p>
		</s:if>
		<s:elseif test="action=='viewUserDetails'||action=='updateUserDetails'">
		<s:hidden id="id" name="user.ID" value="%{user.ID}"></s:hidden>
		<s:hidden id="cAction" value="%{action}"></s:hidden>
		   <p>
		       <span><s:fielderror name="username" id="username" cssStyle="color:red"></s:fielderror></span>
			   <label><font color="red">* </font>UserName:</label>
			   <input type="text" id="ur_name"name="user.UserName" value='<s:property value="user.UserName"/>' readOnly>
		   </p>
		    <p>
			     <label> <font color="red">* </font> Email:</label>
			     <input type="text" id="ur_email" name="user.EMail" value='<s:property value="user.EMail"/>' readOnly>
		     </p>
		   <p style="display:none">
				<label>Current Password:</label>
				<input type="password" name="i_oldPassword" id="i_oldPassword"> 
				<input type="hidden" name="oldPassword" id="oldPassword">
			</p>
			<p>
				<label>New Password:</label>
				<input type="password" name="i_newPassword" id="i_newPassword">
				<input type="hidden" name="newPassword" id="newPassword"> 
			</p>
			<p>
				<label>Confirm Password:</label>
				<input type="password" name="i_c_newPassword" id="i_c_newPassword">
			</p>			
		
		<s:fielderror name="password" id="password" cssStyle="color:red"></s:fielderror>
		    
		    <p>
			 <label>First Name:</label>
			 <input type="text" name="user.firstName" value='<s:property value="user.firstName"/>'>
			 </p>
		 	<p>
			 <label>Last Name:</label>
			 <input type="text" name="user.lastName" value='<s:property value="user.lastName"/>'>
			 </p>
			<p>
				<label>Telephone:</label>
				<input type="text" name="user.Telephone" value='<s:property value="user.Telephone"/>'>
			</p>
			<p>
				<label>Address:</label>
				<textarea  name="user.Address"><s:property value="user.Address"/></textarea>
			</p>
			<p>
			 <label>City:</label>
			 <input type="text" name="user.addressCity" value='<s:property value="user.addressCity"/>'>
		 	</p>
		 	<p>
			 <label>State:</label>
			 <div id="div_select_state">
			 	<input type="hidden" value='<s:property value="user.addressState"/>' readOnly>
				<select id="select_state" name="user.addressState" onchange="getstateValue();"></select>
			</div>
		 	</p>
		 	<p style="display:none">
		 		<label>&nbsp;</label>
		 			<input id="write_state" type="text" name=""> Other?
		 	</p>		 		
		 	<p></p>
		 	<p>
			 <label>ZIP/Postal Code:</label>
			 <input type="text" name="user.addressZip" value='<s:property value="user.addressZip"/>'>
		 	</p>
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
		 	
			 <p>
			 <label>Country:</label>
			 <div id="div_select_country">
			 		<input type="hidden" value='<s:property value="user.addressCountry"/>'readOnly>
					<select id="select_country" name="user.addressCountry"></select>
			</div>
			
		 	</p>
		 	<p style="display:none">
		 	<label>&nbsp;</label>
				<input id="write_country" type="text" name=""> Other?
		 	
		 	</p>
		 	<p></p>
			<p>
			    <label id="verifyCode"> Verify Image:</label>
		      	<s:url action="Generate.action" namespace="/jsp/verifycode" id="generateCode"></s:url>
			  	<img id="imgObj" alt="" src="<s:property value='generateCode'/>"/> 
			   	&nbsp;
			  <a href="javascript:void(0)" class="uiButton" onclick="changeImg()">change</a> 
			 </p>
	         <p>
				<label id="verifyCode"><font color="red">* </font> Verify Code:</label>
				<input id="veryCode" name="veryCode" type="text"/>
			</p>
			
			
			
			
			<div <s:if test="advisor==false">style="display:none"</s:if>>
		 	<fieldset style="border:1px solid #ccc;padding-left:5px">
			    <legend style="margin-left:10px">For Advisor</legend>
		 	
		 	
		 	<p>
		 	<div id="chart">
		 	<img alt="" src="/LTISystem/<s:property value="user.logo"/>" border=0>
		 	</div>		 	 
			 <label>Logo:</label>
			 <input type="hidden" name="user.logo" value='<s:property value="user.logo"/>'>
			 <input type="file" name="logoFile">
		 	</p>
		 	
		 	<p>
			 <label>Company:</label>
			 <input type="text" name="user.company" value='<s:property value="user.company"/>'>
		 	</p>
		 	
		 	<p>
			 <label>License:</label>
			 <input type="text" name="user.license" value='<s:property value="user.license"/>'>
		 	</p>
		 	
		 	<p>
			 <label>Description:</label>
			 <textarea id="description" name="user.description" ><s:property value="user.description"/></textarea>
		 	</p>
		 	<script>

		 	tinyMCE.init({
			    mode : "none",
			    theme : "advanced",
			    plugins : "save",
				theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,fontselect,fontsizeselect",
				theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,blockquote,link,unlink,anchor,image,|,forecolor,backcolor",
				theme_advanced_buttons3 : "",
				theme_advanced_toolbar_location : "top",
				theme_advanced_toolbar_align : "left",
				theme_advanced_statusbar_location : "bottom",
				save_onsavecallback : "save_for_mce"
			});
			$(function() {
				tinyMCE.execCommand('mceAddControl', false, "description");
			});
		 	
		 	</script>
		 	
		 	</fieldset>
		 	
		 	</div>
		 	<s:if test="advisor==true"><br></s:if>
			
			
			<input type="hidden" name="user.createdDate" value='<s:property value="user.createdDate"/>'>
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			<div align="right" style="width:298px;text-align:left">
				<!--<input type="button" class="uiButton" name="Submit" value="Submit" onclick="onclk()">  -->
				<a href="javascript:void(0);" class="uiButton" id="pass_dialog"> Please Submit</a>
			</div>
		<s:if test="isfacebook">
		   <script type="text/javascript">
			$('#pass_dialog').click(function(){
			  onclk();
			});
			</script>
		</s:if>
		<s:else>
			<script type="text/javascript">
			$('#pass_dialog').click(function(){
				$("#dialog-confirm").dialog({
					autoOpen: false,
		    		//resizable: true,
					height:210,
					width: 420,
					modal: true,
					buttons: {
						Cancel: function() {
							$(this).dialog('close');
						},
						'Confirm': function() {
							currentpassword = $("#i_curPassword").val();
							$('#i_oldPassword').val(currentpassword);
							code = "action=permission&includeHeader=false&curPassword="+currentpassword;
							$("#password_message").html("Connecting to the server...");
							$.ajax({   
           						type:"POST",   
           						url:"PasswordReset.action",   
           						data:code, 
           						error: function(){
    	   							$("#password_message").val("Failed to access the server"); 
       							},
           						success: function(mesg){
         							$("#password_message").html(mesg);
         							sumesg = "You current password is right.";
         							if(mesg.indexOf(sumesg)>0){
         								$("#password_message").html(sumesg);
         								msg=onclk();
         								if(msg.toString()!="")
         									$("#password_message").html(sumesg+"<br/>"+msg);
         								$(this).dialog('close');		
         							}
         						}
       						});  
						}
					}
				});
				$("#dialog-confirm").dialog("open");
   			});
   			</script>
   		</s:else>
			<div id="password_dialog" height="250px" style="display:none">
				<div id="dialog-confirm" title="Please enter the current password" align='left' style="text-align: left">
				<br>
					<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>
						<b>Please enter the current password to continue the operation.</b>
					</p>
					<p>
						<label>Current Password:</label>
						<input type="password" name="i_curPassword" id="i_curPassword">
					</p>
					<div id="password_message"></div>
				</div>
			</div>
		<div id="Message"><input type="hidden" size="41" readonly id="actionMessage"/></div> 
		</s:elseif> 
		</s:form>
	</div>
	<s:if test="action=='viewUserDetails'||action=='updateUserDetails'">
		<div id="emailsettings">	
		</div>
		<div id="user_myaccount">
		</div>
		<div id="user_subscr"></div>
	</s:if>
</div>

</body>
</html>