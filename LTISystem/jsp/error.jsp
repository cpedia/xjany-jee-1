<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>The operation cannot be completed</title>
	</head>
	<body>
	<div style="height:570px;padding:20px;background: none repeat scroll 0 0 #FFFFFF;" >
			<span style=''>The operation cannot be completed , see the follow detail message: </span>
			<br>
			<s:actionmessage/>
			<s:actionerror/>
			<br>
			<br>
			<input type="button" class='uiButton'  value="Back" onclick="goFrontPage()"/>
			<input type="button" class='uiButton'  value="Home page" onclick="window.location.href='/LTISystem'"/>
			<s:url id="main" namespace="/jsp/main" action="Main"></s:url>
			<script>
			function goFrontPage(){
				var refUrl = document.referrer;
				if(refUrl==null || refUrl==""){
					window.location.href= '<s:property value="main"/>';
				}
				else
					window.location.href= refUrl;
			}
			</script>
			
			<pre>
				<s:property value="exception.message"/>
				
				<s:property value="exceptionStack"/>
			</pre>
	</div>
	</body>
	
</html>
