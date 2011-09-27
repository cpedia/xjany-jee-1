<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<LINK href="../../images/style.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<title>Daily Data Main Page</title>
		<!-- Initialize BorderLayout START-->
                <style type="text/css">
body{padding:0 0;}
                </style>
	</head>
	<body>
	<p class="title"><s:property value="portfolioName"/><s:text name="portfolio.daily.data"></s:text></p>
		<table width="100%">
			<tr class="trHeader">
                <td align="left" class="note"><s:text name="date"></s:text></td>
                <td ><s:text name="total.amount"></s:text></td>
                <td><s:text name="alpha"></s:text></td>
                <td><s:text name="beta"></s:text></td>                
                <td ><s:text name="AR"></s:text></td> 
                <td><s:text name="rsquare"></s:text></td> 
                <td><s:text name="sharpe"></s:text></td> 
                <td><s:text name="standard.deviation"></s:text></td>
                <td><s:text name="treynor"></s:text></td>
                <td><s:text name="draw.down"></s:text></td> 															
			</tr>		
			<s:iterator value="#request.dailydatas">
				<tr>

				<td align="left" class="note">
                   <s:property value="dateStr"  />
                </td>
                <td >
                  <s:property value="amount"  />
                </td>
                 <td>
                  <s:property value="alpha"  />
                </td>
                 <td>
                  <s:property value="beta"  />
                </td>                
                 <td >
                  <s:property value="AR"  />
                </td> 
                <td>
                  <s:property value="RSquared"  />
                </td> 
                <td>
                  <s:property value="sharpeRatio"  />
                </td> 
                <td>
                  <s:property value="standardDeviation"  />
                </td>
                <td>
                  <s:property value="treynorRatio"  />
                </td>
                <td>
                  <s:property value="drawDown"  />
                </td>   
			
				</tr>
			</s:iterator>

		</table>
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
	</body>
</html>
