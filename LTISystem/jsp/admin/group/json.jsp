<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
[
<s:iterator value="#request.names" id="name">
	["<s:property />"],	
</s:iterator>
]