<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="main" width="100%" height="30%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="PortfolioTable.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='url=<%=request.getParameter("url")%>&address=<%=request.getParameter("address")%>&port=<%=request.getParameter("port")%>&tableName=My Portfolio'/>
			
			<embed src="PortfolioTable.swf" id="flash" FlashVars='url=<%=request.getParameter("url")%>&address=<%=request.getParameter("address")%>&port=<%=request.getParameter("port")%>&tableName=My Portfolio' quality="high" bgcolor="#ffffff"
				width="100%" height="50%" name="Searching" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
		</object>