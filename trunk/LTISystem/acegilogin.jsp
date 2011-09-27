<%@ page contentType="text/html" language="java" %>
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
if(flag==false){
	request.getRequestDispatcher("acegilogin1.jsp").forward(request,response);         
}else{
	request.getRequestDispatcher("acegilogin2.jsp").forward(request,response);          
}		


%>
