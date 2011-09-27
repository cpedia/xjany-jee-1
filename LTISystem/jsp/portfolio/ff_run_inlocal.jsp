<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<script type="text/javascript">
try{
	//window.location = 'ltisystem://test';
	if('<%=request.getParameter("action")%>'=='execute'){
		window.parent.document.Edit.action="../portfolio/EvaluateInLocal.action?action=execute";
		window.parent.document.Edit.target="_self";
		window.parent.document.Edit.submit();
	}else{
		window.parent.location.href='Edit.action?action=<%=request.getParameter("action")%>&useThirdPartyAPI=<%=request.getParameter("useThirdPartyAPI")%>&local=true&ID=<%=request.getParameter("portfolioID")%>';
	}
	  
}catch(e){
	//if( e instanceof
	if(e.name=="NS_ERROR_UNKNOWN_PROTOCOL"){
		if(window.confirm("Download LTI Executor?\nAfter downloading LTI Executor, please install it.")){
				window.parent.location.href="../help/install.html";  
		}
	} 
	
}
</script>
</head>
<body>
</body>
</html>
