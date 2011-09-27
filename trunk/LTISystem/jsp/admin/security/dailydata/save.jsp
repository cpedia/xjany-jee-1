<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security's Daily Data</title>
		<SCRIPT src="../../images/common.js" type=text/javascript></SCRIPT>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />		
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/security" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Security Manager</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_view" namespace="/jsp/admin/security" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_view}">Security</s:a>				
			</td>
			<td width="15%">
				<s:url action="Main.action" id="url_dailydata" namespace="/jsp/admin/security/dailydata" includeParams="none">
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_dailydata}">Daily Datas</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/security/dailydata" includeParams="none">
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create a Daily Data</s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Edit Daily Data</p>
	<s:actionmessage/>			
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield name="securityID" label="Security ID " disabled="true"/>
			<s:textfield name="ID" label="ID " disabled="true"/>
			<s:textfield name="date" label="Date "/>
			<s:textfield name="split" label="Split "/>
			<s:textfield name="dividend" label="Dividend "/>
			<s:textfield name="EPS" label="EPS "/>
			<s:textfield name="marketCap" label="MarketCap "/>
			<s:textfield name="PE" label="PE "/>
			<s:textfield name="close" label="Close "/>
			<s:textfield name="open" label="Open "/>
			<s:textfield name="high" label="High "/>
			<s:textfield name="low" label="Low "/>
			<s:textfield name="adjClose" label="AdjClose "/>
			<s:textfield name="volume" label="Volume "/>
			<s:textfield name="returnDividend" label="Return Dividend "/>
			<s:textfield name="securityID" label="Security ID "/>
			<s:textfield name="turnoverRate" label="Turnover Rate "/>
			<s:textfield name="NAV" label="NAV "/>
			<s:textfield name="adjNAV" label="AdjNAV "/>
 			<s:submit>Save</s:submit>

	</s:form>
   
	</body>
</html>
