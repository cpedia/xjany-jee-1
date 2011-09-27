<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<html>
<head>
<style>
.postTable{
border-collapse:collapse;
width:100%;

}
.title
{
background:#d3e0f1;
}
.text{
padding:2em;
height:2em;
}
.space{
margin:2em;
background:#dddddd;
height:3px;
}
</style>
<script>
var address=location.hostname;
var port=document.location.port;
if(port=="")port='80';
$(document).ready(function(){
			$('.link').attr("href",'http://'+address+':'+port+this.attr('href'));
	 });
</script>
</head>
<body>

<table cellspacint="0" class="postTable">

<tbody>
<s:iterator value="#request.posts" status="st">
	<tr class="space"><td></td><td></td></tr>
	<tr class="title">
		<td><img src="../images/icon_minipost_new.gif"/> Posted by <s:property value="userName"/>, <s:property value="time"/>, in <s:property value="topicTitle"/></td>
		<td></td>
	</tr>
	<tr class="text">
		<td><s:property value="text"/></td>
		<td><a class="link" href='/jforum/posts/list/<s:property value="Id"/>.page' target='_blank'>View more detail</a></td>
	</tr>
	
</s:iterator>
</tbody>
</table>
</body>
</html>