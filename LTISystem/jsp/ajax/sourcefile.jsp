<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<html>
<head>
<link type="text/css" rel="stylesheet" href="images/SyntaxHighlighter.css"></link>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<script language="javascript" src="images/shCore.js"></script>
<script language="javascript" src="images/shBrushJava.js"></script>
<script language="javascript">
$(function () {
	dp.SyntaxHighlighter.ClipboardSwf = 'images/clipboard.swf';
	dp.SyntaxHighlighter.HighlightAll('code');
});

</script>
</head>
<body>
<textarea name="code" class="java">
<s:property value="%{resultString}" escape="false"/>
</textarea>

</body>
</html>


