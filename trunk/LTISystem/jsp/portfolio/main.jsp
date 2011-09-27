<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<%@ page import="com.lti.action.flash.URLUTF8Encoder"%>
<%
Integer sort =(Integer) request.getAttribute("Sort");
//String url="http://202.116.76.163:8000/LTISystem/jsp/security/ScreeningOutput.action" ;
String url1="/LTISystem/jsp/portfolio/ModelPortfolioOutput.action" ;
URLUTF8Encoder uRLUTF8Encoder=new URLUTF8Encoder();
url1=uRLUTF8Encoder.encode(url1);
String url2="/LTISystem/jsp/portfolio/PrivatePortfolioOutput.action" ;
url2=uRLUTF8Encoder.encode(url2);
%>
<html>
<head>
    <title>Portfolio Main Page</title> 
    <meta name="submenu" content="portfoliotable"/>
	<meta name="portfolios" content="vf_current" />
    <meta name='<s:property value="menuItem"/>' content="vf_current"/>
	<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
	<SCRIPT src="../images/jquery.tooltip/PopDetails.js" type="text/javascript" ></SCRIPT>
	<script src="../images/jquery.tooltip/jquery.bgiframe.js" type="text/javascript"></script>
	<script src="../images/jquery.tooltip/chili-1.7.pack.js" type="text/javascript"></script>
	<script src="../images/jquery.tooltip/jquery.tooltip.js" type="text/javascript"></script>
	<!--<link rel="stylesheet" href="../images/jquery.tooltip/jquery.tooltip.css" />-->
	<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />
	<script type="text/javascript" src="/LTISystem/jsp/images/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.core.js"></script>
	<script type="text/javascript" src="/LTISystem/jsp/images/jquery.ui/1.7.1/ui.dialog.js"></script>
	<script type="text/javascript" src="/LTISystem/jsp/images/jquery.form/jquery.form.js"></script>
	<!-- table sorter -->
	<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
	<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
	<link rel="stylesheet" href="../images/jquery.tablesorter/addons/pager/jquery.tablesorter.pager.css" type="text/css" />
	
		
	<!-- Initialize BorderLayout START-->
	<script type="text/javascript">
	$(function () {
			
		
		<s:if test="menuItem=='publicPortfolio'">
		$('#<s:property value="menuItem"/>').html("Loading Data...");
		$('#<s:property value="menuItem"/>').load('GetPortfolioTable.action?includeHeader=false&title=Public%20Portfolios');
		</s:if>
		<s:elseif test="menuItem=='premiumsPortfolio'">
		$('#<s:property value="menuItem"/>1').html("Loading Data...");
		$('#<s:property value="menuItem"/>2').html("Loading Data...");
		$('#<s:property value="menuItem"/>1').load('GetPortfolioTable.action?groupIDs=4&includeHeader=false&title=LEVEL_1%20Portfolios');
		$('#<s:property value="menuItem"/>2').load('GetPortfolioTable.action?groupIDs=5&includeHeader=false&title=LEVEL_2%20Portfolios');
		</s:elseif>
		<s:elseif test="menuItem=='allPortfolio'">
		$('#<s:property value="menuItem"/>').html("Loading Data...");
		$('#<s:property value="menuItem"/>').load('GetPortfolioTable.action?includeHeader=false&admin=true&titile=All%20Portfolios');
		</s:elseif>
		<s:elseif test="menuItem=='myPortfolio'">
		$('#<s:property value="menuItem"/>').html("Loading Data...");
		$('#<s:property value="menuItem"/>').load('GetPortfolioTable.action?owner=true&includeHeader=false&title=My%20Portfolios');
		</s:elseif>
		
		
	});
	</script>
	<!-- $('#<s:property value="menuItem"/>').html("Loading Data...");
		$('#<s:property value="menuItem"/>').load('PortfolioTable.action?type=<s:property value="menuItem"/>&includeHeader=false');
	 -->
	
	
</head>

<body>

<!--<script>
$(document).ready(function() {
	var port=document.location.port;
	if(port=="")port='80';
	var address=location.hostname;
	$('#myPortfolioTable').load('myPortfolioFlash.jsp?port='+port+'&address='+address+'&url=<%=url2%>');
	$('#publicPortfolioTable').load('publicPortfolioFlash.jsp?port='+port+'&address='+address+'&url=<%=url1%>');
});
</script>-->

	<s:url namespace="/jsp/portfolio" action="EditHolding" id="create_url">
		<s:param name="ID" value="0"></s:param>
		<s:param name="operation" value="0"></s:param>
	</s:url>
	<p align="left">
	<div id="searchDiv" align="left" style="margin:5px">
		<script type="text/javascript">
		function searchByName(){
			var name = $("#namefield").val();
			name = escape(name);
			if(name.length<3){
				alert("the keyword is too small to search.");
				return;
			}
			$("#publicPortfolios").hide();
			$("#myPortfolios").hide();
			$("#premiumsPortfolios").hide();
			$("#searchPortfolios").html("Loading data...");
			<s:if test="menuItem=='publicPortfolio'">
			$("#searchPortfolios").load('GetPortfolioTable.action?includeHeader=false&title=Search%20Public%20Portfolios&keyword='+name);
			</s:if>
			<s:elseif test="menuItem=='premiumsPortfolio'">
			$("#searchPortfolios").load('GetPortfolioTable.action?groupIDs=4,5&includeHeader=false&title=Search%20Premiums%20Portfolios&keyword='+name);
			</s:elseif>
			<s:elseif test="menuItem=='allPortfolio'">
			$("#searchPortfolios").load('GetPortfolioTable.action?includeHeader=false&admin=true&titile=Search%20All%20Portfolios&keyword='+name);
			</s:elseif>
			<s:elseif test="menuItem=='myPortfolio'">
			$("#searchPortfolios").load('GetPortfolioTable.action?owner=true&includeHeader=false&title=Search%20My%20Portfolios&keyword='+name);
			</s:elseif>
		}
		function searchByCategory(){
			var cate = $("#catefield").val();
			cate = escape(cate)
			$("#publicPortfolios").hide();
			$("#myPortfolios").hide();
			$("#premiumsPortfolios").hide();
			$("#searchPortfolios").html("Loading data...");
			$("#searchPortfolios").load('CategorySearch.action?includeHeader=false&categories=' + cate);
		}
		</script>
		<table>
			<s:form namespace="/jsp/portfolio" theme="simple">
				<tr>
					<s:if test="menuItem=='myPortfolio'">
				    <td><s:a id="create" theme="simple" href="%{create_url}"><font size="2"><s:text name="create.portfolio"></s:text></font></s:a></td>
					<td>  </td>
					</s:if>
					<td><label><s:text name="portfolio.name"></s:text>:</label></td>
					<td><s:textfield id="namefield" name="name" theme="simple"></s:textfield></td>
					<td><input id="searchName" type="button" name="submit" value="Search" onclick="searchByName()"/></td>
				<!--  <td><label><s:text name="categories"></s:text>:</label></td>
					<td><s:textfield id="catefield" name="categories" theme="simple"></s:textfield></td>
					<td><input id="searchCategory" type="button" name="submit" value="Search" onclick="searchByCategory()" /></td>-->	
				</tr>
			</s:form>

			<script type="text/javascript">
			$("#namefield").keydown(function(ev){
				//var e = event.srcElement;
				if(ev.which==13)
				{
					$("#searchName").click();
					return false;
				}
			})
			$("#catefield").keydown(function(ev){
				//var e = event.srcElement;
				if(ev.which==13)
				{
					$("#searchCategory").click();
					return false;
				}
			})
			</script>
		</table>
	</div>
	</p>

	<s:if test="#request.isHolding == true">
		<s:text name="holding.tips.pre"></s:text> <s:property value="holdingDate"/>. <s:text name="holding.tips.tail"></s:text>
	</s:if>
	<s:if test="#request.realtime==false">
  &nbsp; The following table contains portfolios with delayed information. To see the real time portfolio performance,please register and login.
	</s:if>
	<div id="searchPortfolios">
	
	</div>
	<p align="left">
		<div id='<s:property value="menuItem"/>s' style="margin:5px">
			<br>
			<s:if  test="menuItem=='premiumsPortfolio'">
			<div id='<s:property value="menuItem"/>1' style="margin:5px">
			</div>
			<div id='<s:property value="menuItem"/>2' style="margin:5px">
			</div>
			</s:if>
			<s:else>
			<div id='<s:property value="menuItem"/>' style="margin:5px">
			</div>
			</s:else>
			
		</div>
	</p>
	<br><br>
</body>
</html>
