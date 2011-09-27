	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="main" width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="${flashSrc}" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='url=${url}&lineNameArray=${nameArray}&chartName=${title}&address=${address}&port=${port}&currentMode=null'/>
			
			<embed src="${flashSrc}" id="flash" FlashVars='url=${url}&lineNameArray=${nameArray}&chartName=${title}&address=${address}&port=${port}&currentMode=null' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>

