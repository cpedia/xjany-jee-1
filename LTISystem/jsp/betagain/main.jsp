<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.InetAddress;" %>
<html>
	<head>
	   <meta name="bga" content="vf_current"/>
       <meta name="tools" content="vf_current" />
       <meta name="submenu" content="tools"/>
		<script type="text/javascript" src="../images/jquery-1.3.2.min.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			showHistory();
		});
		function showBetaChart(){
			$('#history_table').hide();
			$('#gain_chart').css({width:"0px",height:"0px"});
			$('#beta_chart').css({width:"100%",height:"100%"});
		}
		function showGainChart(){
			$('#history_table').hide();
			$('#beta_chart').css({width:"0px",height:"0px"});
			$('#gain_chart').css({width:"100%",height:"100%"});
		}
		function showHistory(){
			$('#history_table').show();
			$('#gain_chart').css({width:"0px",height:"0px"});
			$('#beta_chart').css({width:"0px",height:"0px"});
		}
		</script>
		
	</head>
	<body>

		<table class="nav" width="100%" style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid;BORDER-BOTTOM: #a6a398 1px solid;">
			<tr>
				<td width="10%">Symbol</td><td width="40%"><s:property value="symbol"/></td>
				<td width="10%">Interval</td><td width="40%"><s:property value="interval"/></td>
			</tr>
			<tr>
				<td>Index</td><td><s:property value="indexArray"/></td>
				<td><a href="javascript:void(0);" onclick="showBetaChart()">Show Beta Chart</a></td>
				<td><a href="javascript:void(0);" onclick="showGainChart()">Show Gain Chart</a></td>
				<td><a href="javascript:void(0);" onclick="showHistory()">Show Beta Gains</a></td>
				
			</tr>
			<tr>
				<td>Start Date</td><td><s:property value="startDate"/></td>
				<td>End Date</td><td><s:property value="endDate"/></td>
			</tr>			
		</table>
		
	
		<table width="100%" id="history_table">
			<tr class="trHeader">
				<td>Date</td>
				<s:iterator value="index" status="status">
				<td>
					<s:property/>
				</td>
				</s:iterator>	
			</tr>		
			<s:iterator value="#request.betagainDailyDatas">
				<tr>	
					<td>
						<s:property value="date"/>
					</td>				
					<s:iterator value="gainList" status="status">
					<td>
						<s:property/>
					</td>
					</s:iterator>
				</tr>
			</s:iterator>

		</table>
		<div id="beta_chart">
			<s:action name="DisplayFlash" namespace="/jsp/flash" executeResult="true">
				<s:param name="url">/LTISystem/jsp/flash/OutputFile.action?filename=<s:property value="filename_beta"/></s:param>
				<s:param name="chartName" ><s:property value="symbol"/>`s beta chart</s:param>
				<s:param name="lineNameArray" value="indexArray"></s:param>
				<s:param name="currentMode">portfolio</s:param>
			</s:action>
		</div>				
		<div id="gain_chart">
			<s:action name="DisplayFlash" namespace="/jsp/flash" executeResult="true">
				<s:param name="url">/LTISystem/jsp/flash/OutputFile.action?filename=<s:property value="filename_gain"/></s:param>
				<s:param name="chartName" ><s:property value="symbol"/>`s gain chart</s:param>
				<s:param name="lineNameArray" value="indexArray"></s:param>
				<s:param name="currentMode">portfolio</s:param>
			</s:action>
		</div>			
			
	</body>
</html>
