[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<html>
	<head> 
		<title>Group's User email</title>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />		
	</head>
	<body>
		<p class="title">${title}</p>
		<p class="subtitle">User Email List</p>	
		<table width="100%" border="0" cellspacing="1" cellpadding="0">
		   <tr><td colspan=5><input type='button' value='Print Emails'  style='width:100px;' onclick="javaScript:print()" /></td></tr>
		</table>
		[#list emails as email]
			 ${email},
		[/#list]
	</body>
</html>