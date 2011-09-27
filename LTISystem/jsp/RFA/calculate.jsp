<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.InetAddress;" %>
<html>
	<head>
		<meta name="rfa" content="vf_current"/>
		<meta name="tools" content="vf_current"/>
		<meta name="submenu" content="tools"/>
		<title>RFA Analysis -<s:property value="symbol"/></title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />

		<script type="text/javascript">
		
		
		<!-- Initialize BorderLayout END-->
		var ip=document.location.hostname;
		var port=document.location.port;
		if(port=="")port='80';
		var address=location.hostname;
		$('#link').attr({href:'flash.jsp?port='+port+'&address='+address+'&indexArray=<s:property value="indexArray2"/>&createTime=<s:property value="createTime"/>&IsRAA=<s:property value="IsRAA"/>&symbol=<%=request.getParameter("symbol")%>'});				
		
		$(document).ready(function(){
		    $('#history_table').hide();
		    $('#chart').load('flash.jsp?port='+port+'&address='+address+'&indexArray=<s:property value="indexArray2"/>&createTime=<s:property value="createTime"/>&IsRAA=<s:property value="IsRAA"/>&symbol=<%=request.getParameter("symbol")%>');
		     

		  });
		function showChart(){
			$('#history_table').hide();
			$('#chart_table').show();
		}
		function showHistory(){
			$('#history_table').show();
			$('#chart_table').hide();
		}
		</script>
	
		<!-- These CSS are necessary START -->

		
		<!-- These CSS are necessary END -->
	</head>
	<body>
		<table class="nav" width="100%" style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid;BORDER-BOTTOM: #a6a398 1px solid;">
			<tr>
				<td width="10%">Symbol</td><td width="40%"><s:property value="symbol"/></td>
				<td width="10%">Interval</td><td width="40%"><s:property value="interval"/></td>
			</tr>
			<tr>
				<td>Index</td><td><s:property value="indexArray"/></td>
				<td><a href="javascript:void(0);" onclick="showChart()">Show Chart</a></td>
				<td><a href="javascript:void(0);" onclick="showHistory()">Show History</a></td>

			</tr>
			<tr>
				<td>Start Date</td><td><s:property value="startDate"/></td>
				<td>End Date</td><td><s:property value="endDate"/></td>
			</tr>

			
		</table>	
		<table width="100%" id="chart_table">
			<tr>
				<td height="600"><div id="chart"></div></td>
			</tr>
		</table>
		<table width="100%" id="history_table">
			<tr class="trHeader">
				<td>Date</td>
				<td>RSquare</td>	
				<s:iterator value="index" status="status">
				<td>
					<s:property/>
				</td>
				</s:iterator>	
				<td>Pie Chart</td>
			</tr>		
			<s:iterator value="#request.mutualFundDailyBetas">
				<tr>
					<td>
						<a href='PlotPie.action?indexArray=<s:property value="indexArray"/>&symbol=<%=request.getParameter("symbol")%>&date=<s:property value="Date"/>&createTime=<s:property value="createTime"/>&IsRAA=<s:property value="IsRAA"/>'>
							<s:property value="Date"/>
						</a>
					</td>
					<td>
						<s:property value="RSquareString"/>
					</td>
					
					<s:iterator value="betaList" status="status">
					<td>
						<s:property/>
					</td>
					</s:iterator>
					<td>
					<a href='PlotPie.action?indexArray=<s:property value="indexArray"/>&symbol=<%=request.getParameter("symbol")%>&date=<s:property value="Date"/>&createTime=<s:property value="createTime"/>&IsRAA=<s:property value="IsRAA"/>'>
							Pie Chart
					</a>
					</td>
				</tr>
			</s:iterator>

		</table>
					
	
	
	
	</body>
</html>