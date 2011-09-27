[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<html>
<head>
<title>${chartName!"Chart Comparison Tool"}</title>
<style type="text/css">

#chartDiv{
	z-index:1;
}
#selectDiv{
	z-index:99999;
}
</style>
[#if Parameters.includeHeader?? && Parameters.includeHeader=="false"]
<script src="${lti.baseUrl}/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
[/#if]
<script style="text/javascript">
$(document).ready(function(){
	var indexCount = 0;
	while(indexCount <= 4){
		$("#index" + indexCount).suggest("${lti.baseUrl}/jsp/ajax/GetSecuritySuggestTxt.action",
									 	{ 	haveSubTokens: true, 
									 		onSelect: function(){}});
		indexCount++;
	}
	//$('#full_chart').attr({"href":window.location.href+"&includeHeader=false"});
	//alert("");
})
</script>
<SCRIPT src="${lti.baseUrl}/jsp/portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.js" type="text/javascript"></SCRIPT>
<link href="${lti.baseUrl}/jsp/portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.css" rel="stylesheet" type="text/css" />
<title>${Parameters.sybmol!"Security"}'s MPT Chart</title>
</head>
<body>
<div id="mainForm">
<div id="selectDiv" style="text-align:left;">
	<p style="font-size:12pt;padding:0;width:100%"><strong>Compare Securities</strong><span style="font-size:9pt">(symbols)</span></p>
	<p style="padding-top:0;width:100%">
		<form action="SecurityMPTChart.action?includeHeader=false" method="Get" style="padding:0;margin:0">
			<input type="hidden" name="includeHeader" value="false"></input>
			<input type="hidden" name="symbol" value="${symbol}"></input>
			[#if chartName??]
				<input type="hidden" name="chartName" value="${chartName}" ></input>
			[/#if]
			[@s.textfield name="symbolList[0]" id="index0"/]
			[@s.textfield name="symbolList[1]" id="index1"/]
			[@s.textfield name="symbolList[2]" id="index2"/]
			[@s.textfield name="symbolList[3]" id="index3"/]
			[@s.textfield name="symbolList[4]" id="index4"/]
			<input type="submit" value="submit"/>
		</form>
	</p>
</div>

[#if nameList?? && symbolList?? && nameList?size==symbolList?size]
<table border=0 width=100% style="font-size:10px">
	[#list symbolList as symbol]
		<tr>
			<td>
				<b>${symbol!""}</b>
			</td>
			<td>
				${nameList[symbol_index]}
			</td>
		<tr>
	[/#list]
</table>
[/#if]

<div id="chartDiv" style="padding-top:-5px;margin:0;">
[@s.url action="OutputSecuirtyMPT" namespace="/jsp/flash" id="url_flash_xml" escapeAmp="false"]
	[@s.param name="symbols"]${symbols}[/@s.param]
[/@s.url]
<!--${url_flash_xml} -->
[@s.action name="DisplayFlash" namespace="/jsp/flash" executeResult=true]
	[#if chartName??]
		[@s.param name="chartName"]${chartName}[/@s.param]
	[#else]
		[@s.param name="chartName"]${symbol}[/@s.param]
	[/#if]
	[@s.param name="lineNameArray"]${symbols}[/@s.param]
	[@s.param name="address"]127.0.0.1[/@s.param]
	[@s.param name="port"]80[/@s.param]
	[@s.param name="url"]${url_flash_xml}[/@s.param]
	[@s.param name="currentMode"]portfolio[/@s.param]
[/@s.action]
</div>
</div>
</body>
</html>

