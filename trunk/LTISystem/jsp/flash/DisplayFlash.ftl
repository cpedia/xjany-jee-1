[#ftl]
<script type="text/javascript">
function showPic(img,width,height){
window.open("data:image/jpeg;base64,"+img,"","width="+width+",height="+height+",resizable=1");
}
</script>
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			 width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="/LTISystem/jsp/${dir!"mutualfund"}/main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='url=${url?url("utf8")}&chartName=${chartName?url("utf8")}&lineNameArray=${lineNameArray?url("utf8")}&address=${address?url("utf8")}&port=${port?url("utf8")}&currentMode=${currentMode?url("utf8")}'>
			<param name="wmode" value="transparent">
			<embed src="/LTISystem/jsp/${dir!"mutualfund"}/main.swf" FlashVars='url=${url?url("utf8")}&chartName=${chartName?url("utf8")}&lineNameArray=${lineNameArray?url("utf8")}&address=${address?url("utf8")}&port=${port?url("utf8")}&currentMode=${currentMode?url("utf8")}' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
</object>