[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
<html>
<head>
<meta name="bga" content="vf_current"/>
<meta name="tools" content="vf_current" />
<meta name="submenu" content="tools"/>
<title>Result of Beta Gain Analysis_${symbol}</title>
<style>
.hidden{
	display:none;
}
</style>
<script type="text/javascript">
function showBetaChart(){
	$('#history').hide();
	$('#gainchart').css({width:"0px",height:"0px"});
	$('#betachart').css({width:"100%",height:"100%"});
}
function showHistory(){
	$('#history').show();
	$('#betachart').css({width:"0px",height:"0px"});
	$('#gainchart').css({width:"0px",height:"0px"});
}
function showGainChart(){
	$('#history').hide();
	$('#betachart').css({width:"0px",height:"0px"});
	$('#gainchart').css({width:"100%",height:"100%"});
}
$(document).ready(function(){
	showBetaChart();
});
</script>

</head>
<body>

<div style="font-weight:bold;text-align:left;font-size:12pt">
	Gain Interval:
	[#if gainInterval==22]
		1 month
	[#elseif gainInterval==66]
		3 months
	[#elseif gainInterval==132]
		6 months
	[#elseif gainInterval==252]
		1 year
	[#elseif gainInterval==756]
		3 years
	[/#if]
</div>
<div align="left">
${message!}
[#if betagainDailyDatas??]
<span class="border"><a href="javascript:void(0);" onclick="showBetaChart()">Show Beta Chart</a></span>
<span class="border"><a href="javascript:void(0);" onclick="showGainChart()">Show Gain Chart</a></span>
<span class="border"><a href="javascript:void(0);" onclick="showHistory()">Show History</a></span>
</div>

<div id="betachart">
	[@s.url action="OutputFile" namespace="/jsp/flash" id="beta_xml" escapeAmp="false"]
		[@s.param name="filename"]${filename_beta}[/@s.param]		
	[/@s.url]

	[@s.action name="DisplayFlash" namespace="/jsp/flash" executeResult=true]
		[@s.param name="chartName"]${symbol} Beta Chart[/@s.param]
		[@s.param name="lineNameArray"]${indexArray2}[/@s.param]
		[@s.param name="address"]127.0.0.1[/@s.param]
		[@s.param name="port"]80[/@s.param]
		[@s.param name="url"]${beta_xml}[/@s.param]
		[@s.param name="currentMode"]portfolio[/@s.param]
	[/@s.action]
</div>
<div id="gainchart">
	[@s.url action="OutputFile" namespace="/jsp/flash" id="gain_xml" escapeAmp="false"]
		[@s.param name="filename"]${filename_gain}[/@s.param]		
	[/@s.url]

	[@s.action name="DisplayFlash" namespace="/jsp/flash" executeResult=true]
		[@s.param name="chartName"]${symbol} Gain Chart[/@s.param]
		[@s.param name="lineNameArray"]${indexArray2}[/@s.param]
		[@s.param name="address"]127.0.0.1[/@s.param]
		[@s.param name="port"]80[/@s.param]
		[@s.param name="url"]${gain_xml}[/@s.param]
		[@s.param name="currentMode"]portfolio[/@s.param]
	[/@s.action]
</div>
<div id="history" style="border:1px solid">
<table width="100%" id="history_table">
	<tr class="trHeader">
		<td>Date</td>
		[#list index as item]
		<td>
			${item}
		</td>
		[/#list]
	</tr>
	[#list betagainDailyDatas as betagain]
		<tr>
			<td>
				${betagain.date?string("yyyy/MM/dd")}
			</td>
			[#list betagain.gainList as gain]
			<td>
				${gain}
			</td>
			[/#list]
		</tr>
	[/#list]
</table>
</div>
[/#if]

</body>
</html>

