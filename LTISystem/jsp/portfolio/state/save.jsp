<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>State Page</title>
	</head>
	<body>
	
	<p class="title">Portfolio State</p>
	<s:actionmessage/>
	<s:form action="StateSave" method="post" >
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<s:hidden name="action" value="save"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:hidden name="portfolioID" value="%{portfolioID}"/>
			<s:textfield name="ID" label="ID " disabled="true"/>
			<s:textfield name="portfolioID" label="portfolioID" disabled="true"/>
			<s:textfield name="startDate" label="startDate"/>
			<s:textfield name="endDate" label="endDate"/>
			<s:textfield name="message" label="message"/>
			<s:select list="#{'-1':'INACTIVE', '0':'PREPARING','1':'EXECUTING', '2':'COMPUTMPTS','4':'FINISHED', '5':'FAILED'}" name="state" label="State "></s:select>
 			<s:submit>Save</s:submit>
 		</table>

	</s:form>
   
	</body>
</html>
