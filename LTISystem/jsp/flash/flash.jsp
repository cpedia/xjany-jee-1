<script type="text/javascript">
function showPic(img,width,height){
window.open("data:image/jpeg;base64,"+img,"","width="+width+",height="+height+",resizable=1");
}
</script>
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			 width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="/LTISystem/jsp/flash/main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='url=<%=request.getParameter("url")%>&chartName=portfolio&lineNameArray=<%=request.getParameter("pn")%>&address=<%=request.getParameter("address")%>&port=<%= request.getParameter("port") %>&currentMode=portfolio'>
			<param name="wmode" value="transparent">
			<embed src="/LTISystem/jsp/flash/main.swf" FlashVars='url=<%=request.getParameter("url")%>&chartName=portfolio&lineNameArray=<%=request.getParameter("pn")%>&address=<%=request.getParameter("address")%>&port=<%= request.getParameter("port") %>&currentMode=portfolio' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
</object>