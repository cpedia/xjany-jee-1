[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#assign pageTitle="${symbol}"]
[#import "/jsp/lti_library_ftl.jsp" as lti]

<style type="text/css">
body{
	background:#FFFFFF;
}
#chartDiv{
	z-index:1;
}
</style>

<div id="chartDiv" style="padding:0;margin:0;">
[@s.url action="OutputHistoricalBetaGain" namespace="/jsp/flash" id="url_flash_xml" escapeAmp="false"]
	[@s.param name="symbol"]${symbol}[/@s.param]
	
[/@s.url]

[@s.action name="DisplayFlash" namespace="/jsp/flash" executeResult=true]
	[@s.param name="chartName"]${symbol}[/@s.param]
	[@s.param name="lineNameArray"]${symbol}[/@s.param]
	[@s.param name="address"]127.0.0.1[/@s.param]
	[@s.param name="port"]8080[/@s.param]
	[@s.param name="url"]${url_flash_xml}[/@s.param]
	[@s.param name="currentMode"]portfolio[/@s.param]
[/@s.action]
</div>
