<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
	<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript">
		function showAlpha(){
			var port=document.location.port;
			if(port=="")port="80";
			var address=document.location.hostname;
			$('#flash').load('./mpt/flashAlpha.jsp?address='+address+'&port='+port+'&portfolioID=<s:property value="portfolioID"/>');
			
		}
		function showBeta(){
		var port=document.location.port;
			var address=document.location.hostname;
			$('#flash').load('./mpt/flashBeta.jsp?address='+address+'&port='+port+'&portfolioID=<s:property value="portfolioID"/>');
		}
		function showSharpe(){
		var port=document.location.port;
			var address=document.location.hostname;
			$('#flash').load('./mpt/flashSharpe.jsp?address='+address+'&port='+port+'&portfolioID=<s:property value="portfolioID"/>');
		}
		</script>
	</head>
	<body>
	
	<p class="title"><s:property value="#request.portfolioName"/><s:text name="mpt.yearly"></s:text></p>
		<s:if test="isHolding == true">
			<s:text name="holding.tips"></s:text><s:property value="holdingDate"/>
		</s:if>
		<s:include value="main_basicunit.jsp"></s:include>
	<div id="button" align="center">
		<input type="button" value='<s:text name="back"></s:text>' onclick="backToPortfolio()">
	</div>
	<s:url id="portfolio_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
		<s:param name="ID" value="#request.portfolioID"></s:param>
	</s:url>	
	<script>
	/**
	 * backToPortfolio
	 * @param {}  
	 */
	 function backToPortfolio() {
	 	window.location.href = '<s:property value="portfolio_url"/>';
	 }
	</script>
	<a href="#" onclick=showAlpha()>Show Alpha Chart</a>
	<a href="#" onclick=showBeta()>Show Beta Chart</a>
	<a href="#" onclick=showSharpe()>Show Sharpe Chart</a>
	<div id="flash">
	
	</div>
	
	
	</body>
	
</html>
