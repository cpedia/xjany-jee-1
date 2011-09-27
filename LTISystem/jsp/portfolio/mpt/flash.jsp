

<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			 width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="../flash/main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param name=FlashVars value='url=<%=request.getParameter("url")%>&chartName=MPT&lineNameArray=Alpha1,Alpha3,Alpha5,Beta1,Beta3,Beta5,SharpeRatio1,SharpeRatio3,SharpeRatio5&address=<%= request.getParameter("address") %>&port=<%= request.getParameter("port") %>&currentMode=null'>
			
			<embed src="../flash/main.swf" FlashVars='url=<%=request.getParameter("url")%>&chartName=MPT&lineNameArray=Alpha1,Alpha3,Alpha5,Beta1,Beta3,Beta5,SharpeRatio1,SharpeRatio3,SharpeRatio5&address=<%= request.getParameter("address") %>&port=<%= request.getParameter("port") %>&currentMode=null' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>