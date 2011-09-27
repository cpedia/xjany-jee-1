[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
<div id="selectDiv">
</div>
<div id="chartDiv">
[@s.url action="OutputPrice" namespace="/jsp/flash" id="url_flash_xml" escapeAmp="false"]
	[@s.param name="indexArray"]P_3110,P_717[/@s.param]
	[@s.param name="startDate"]2007-01-01[/@s.param]
	[@s.param name="endDate"]2008-01-01[/@s.param]
[/@s.url]
${url_flash_xml}<br />${url_flash_xml?url("utf8")}<br />${"&amp;"?url("utf8")}<br /> 

[@s.action name="DisplayFlash" namespace="/jsp/flash" executeResult=true]
	[@s.param name="chartName"]DisplayChartExample[/@s.param]
	[@s.param name="lineNameArray"]${Parameters.symbol}[/@s.param]
	[@s.param name="address"]127.0.0.1[/@s.param]
	[@s.param name="port"]80[/@s.param]
	[@s.param name="url"]${url_flash_xml}[/@s.param]
	[@s.param name="currentMode"]portfolio[/@s.param]
[/@s.action]
</div>