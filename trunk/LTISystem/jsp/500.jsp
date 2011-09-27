<%@ page import="com.lti.system.*;" %>
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
String domainname="validfi.com";
if(flag==false){
	
}else{
	domainname="myplaniq.com";
}		

%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=gb2312">
<TITLE>www.<%=domainname%> 500 Error</TITLE>
</HEAD>
<BODY background=/LTISystem/jsp/images/error-bg.gif>
<script type="text/javascript">
//<!--
function isIFrameSelf(){try{if(window.top ==window){return false;}else{return true;}}catch(e){return true;}}
function toHome(){ if(!isIFrameSelf()){ window.location.href="http://www.<%=domainname%>";}}
window.setTimeout("toHome()",30000);
//-->
</script>

<table border=0 cellpadding=0 cellspacing=0 >
 <tr><td height=134></td></tr>
</table>
<table width=544 height=157 border=0 cellpadding=0 cellspacing=0 align=center>
  <tr valign=middle align=middle>

	<td background=/LTISystem/jsp/images/error-block.gif>
	    <table border=0 cellpadding=0 cellspacing=0 >
		 <tr>
		    <td  style=padding-left:10px;padding-right:10px;padding-top:10px>
			Sorry, there are some errors in our system, if you have any problems please contact us with email <a href='mailto:support@<%=domainname%>'>support@<%=domainname%></a>.
		<br>
		Or go back to <a href=http://www.<%=domainname%>>www.<%=domainname%></a>.				
			</td>
                 </tr>
            </table>
	</td>
  </tr>

</table>
<br>
</BODY>
</HTML>
