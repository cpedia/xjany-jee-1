[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
<script type="text/javascript">
function showChart(){
	$('#history').hide();
	$('#chart').show();
	$("#pie-chart").html("");
}
function showHistory(){
	$('#history').show();
	$('#chart').hide();
	$("#pie-chart").html("");
}
function showPie(date){
	$('#history').hide();
	$('#chart').hide();
	var pie_url = "PlotPie.action?includeHeader=false&indexArray=${indexArray2}&symbol=${symbol}&date=" + date + "&createTime=${createTime?c}&IsRAA=[#if isRAA=true]true[#else]false[/#if]";
	$("#pie-chart").load(pie_url);
}
</script>
<div align="left">
${message!}
[#if mutualFundDailyBetas??]
<a href="javascript:void(0);" onclick="showChart()">Show Chart</a>
<a href="javascript:void(0);" onclick="showHistory()">Show History</a>
</div>

<div id="chart">
	[@s.url action="OutputBeta" namespace="/jsp/flash" id="url_flash_xml" escapeAmp="false"]
		[@s.param name="indexArray"]${indexArray2}[/@s.param]
		[@s.param name="symbol"]${symbol}[/@s.param]
		[@s.param name="createTime"]${createTime}[/@s.param]
		[@s.param name="IsRAA"][#if isRAA=true]true[#else]false[/#if][/@s.param]
		
	[/@s.url]

	[@s.action name="DisplayFlash" namespace="/jsp/flash" executeResult=true]
		[@s.param name="chartName"]${symbol}[/@s.param]
		[@s.param name="lineNameArray"]${indexArray2}[/@s.param]
		[@s.param name="address"]127.0.0.1[/@s.param]
		[@s.param name="port"]80[/@s.param]
		[@s.param name="url"]${url_flash_xml}[/@s.param]
		[@s.param name="currentMode"]null[/@s.param]
	[/@s.action]
</div>

<div id="history" style="display:none" class="border">
<table width="100%" id="history_table">
	<tr class="trHeader">
		<td>Date</td>
		<td>RSquare</td>	
		[#list index as item]
		<td>
			${item}
		</td>
		[/#list]
		<td>Pie Chart</td>
	</tr>
	[#list mutualFundDailyBetas as beta]
		<tr>
			<td>
				[#assign date]${beta.date?string("MM/dd/yyyy")}[/#assign]
				<a href="javascript:void(0)" onclick='showPie("${date}")'>
					${beta.date?string("MM/dd/yyyy")}
				</a>
			</td>
			<td>
				${beta.RSquareString}
			</td>
			[#list beta.betaList as item]
			<td>
				${item}
			</td>
			[/#list]
			<td>
				<a href="javascript:void(0)" onclick='showPie("${date}")'>
						Pie Chart
				</a>
			</td>
		</tr>
	[/#list]
</table>
</div>
<div id="pie-chart">
</div>
[/#if]