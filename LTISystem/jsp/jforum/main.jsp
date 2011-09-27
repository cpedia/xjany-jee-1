<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<head>
	<meta name="forums" content="vf_current" />
	<title>ValidFi Forum</title>
<!--自动调整高度，去除iframe滚动条-->
	<script> 

var FFextraHeight = 0;
 if(window.navigator.userAgent.indexOf("Firefox")>=1)
 {
  FFextraHeight = 200px;
  }
 function ReSizeiFrame(iframe)
 {
   if(iframe && !window.opera)
   {
     iframe.style.display = "block";
      if(iframe.contentDocument && iframe.contentDocument.body.offsetHeight)
      {
        iframe.height = iframe.contentDocument.body.offsetHeight + FFextraHeight;
      }
      else if (iframe.Document && iframe.Document.body.scrollHeight)
      {
        iframe.height = iframe.Document.body.scrollHeight;
        alert(iframe.Document.body.scrollHeight);
      }
   }
 }

</script>
</head>

<body>
	<div class="fbbl_center">
	<s:url id="center_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName">Commentary</s:param>
	</s:url>
	<iframe id="center" scrolling="no" width="100%" height="99%" src='../../../jforum/'></iframe>
	<script type="text/javascript">
		$(document).ready(function(){
			var iframelocation = $("#center").attr("src");
			//alert(iframelocation);
			if(iframelocation != "../../../jforum/"){
				//window.location.href = iframelocation;
				$("#center").attr({src:"../../../jforum/"});
			}
		})
	</script>
	</div>
</body>