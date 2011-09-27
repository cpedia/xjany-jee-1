<%@ page import="com.lti.action.flash.URLUTF8Encoder"%>
<%

//String pn=request.getParameter("portfolioName");
//pn=pn.replace('\'','`');

String url="/LTISystem/jsp/flash/result/OutputMPT.action?portfolioID="+request.getParameter("portfolioID")+"&requiredMPT=beta";

URLUTF8Encoder uRLUTF8Encoder=new URLUTF8Encoder();
url=uRLUTF8Encoder.encode(url);

%>

<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			 width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="/LTISystem/jsp/flash/main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param name=FlashVars value='url=<%=url%>&chartName=Beta&lineNameArray=1year,3years,5years&address=<%= request.getParameter("address") %>&port=<%= request.getParameter("port") %>&currentMode=null'>
			
			<embed src="/LTISystem/jsp/flash/main.swf" FlashVars='url=<%=url%>&chartName=Beta&lineNameArray=1year,3years,5years&address=<%= request.getParameter("address") %>&port=<%= request.getParameter("port") %>&currentMode=null' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>