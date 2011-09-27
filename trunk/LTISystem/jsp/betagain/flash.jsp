<%@ page import="com.lti.action.flash.URLUTF8Encoder"%>
<%
String url="/LTISystem/jsp/flash/result/OutputBetaGain.action?indexArray="+request.getParameter("indexArray")+"&values="+request.getParameter("values");
URLUTF8Encoder uRLUTF8Encoder=new URLUTF8Encoder();
url=uRLUTF8Encoder.encode(url);
%>

	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="main" width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='url=<%=url%>&lineNameArray=<%=request.getParameter("indexArray")%>&chartName=<%=request.getParameter("symbol")%>&address=<%=request.getParameter("address")%>&port=<%=request.getParameter("port")%>&currentMode=null'/>
			
			<embed src="main.swf" id="flash" FlashVars='url=<%=url%>&lineNameArray=<%=request.getParameter("indexArray")%>&chartName=<%=request.getParameter("symbol")%>&address=<%=request.getParameter("address")%>&port=<%=request.getParameter("port")%>&currentMode=null' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>

