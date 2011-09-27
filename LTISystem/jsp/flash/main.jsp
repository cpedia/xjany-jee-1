<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<%@ page import="com.lti.action.flash.URLUTF8Encoder"%>
<%
String pn=request.getParameter("portfolioName");
pn=pn.replace('\'','`');
//String url="http://localhost:8080/LTISystem/jsp/flash/result/OutputXML.action?portfolioID="+request.getParameter("portfolioID");
String url="/LTISystem/jsp/flash/result/OutputXML.action?portfolioID="+request.getParameter("portfolioID");
URLUTF8Encoder uRLUTF8Encoder=new URLUTF8Encoder();
//url=uRLUTF8Encoder.encode(url);
pn=uRLUTF8Encoder.encode(pn);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title><s:property value="portfolioName" escape="false"/></title>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<script type="text/javascript">
var address=document.location.hostname;
var port=document.location.port;
if(port=="")port='80';

$(document).ready(function(){
	//alert('flash.jsp?pn=<%=pn%>&address='+address+'&port='+port+'&url=<%=url%>');
	//$('#flash').load('flash.jsp?pn=hi&address=localhost&port=8080&url=/LTISystem/jsp/flash/result/OutputXML.action?portfolioID=779');
	$('#flash').load('flash.jsp?pn=<%=pn%>&address='+address+'&port='+port+'&url=<%=url%>');
});


<!-- Initialize BorderLayout END-->
</script>


<script type="text/javascript" src="swfobject_source.js"></script>

</head>

<body>
<div class="fbbl_center" id="flashcontent">
	<span><s:property value="#portfolioName"/></span>
<!-- 
  	This text is replaced by the Flash movie.
	<script type="text/javascript">
	
		var so = new SWFObject("main.swf", "mymovie", "1050", "630", "6", "#FFFFFF");
		so.useExpressInstall('expressinstall.swf');
		so.addVariable("portfolioID", '<%=request.getParameter("portfolioID")%>');
		so.addVariable("portfolioName", '<%=request.getParameter("portfolioName")%>');
		so.addVariable("address",'202.116.76.163');
		so.write("flashcontent");
	</script> 
-->
	<div id="flash"></div>
</div> 
</body>
</html>
