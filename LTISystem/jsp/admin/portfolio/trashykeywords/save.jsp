<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Page</title>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />
		<SCRIPT src="../../images/jquery.latest.js" type="text/javascript" ></SCRIPT>
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="10%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/portfolio" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Portfolio Manager</s:a>		
			</td>
		</table>	
	<p class="title"><s:property value="title"/></p>
	<p class="subtitle">Edit Trash Keywords</p>
	<s:actionmessage/>
	<s:form id="keywordForm" method="post" namespace="/jsp/admin/portfolio/trashykeyword" theme="simple">
		<s:hidden name="portfolioID"></s:hidden>
		<table id="keywordList" style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<tr>
				<td>
					Name
				</td>
				<td>
					Is Deleted?
				</td>
			</tr>
			<s:iterator value="trashyKeywords">
			<tr>
				<td>
					<s:property value="key"/>
				</td>
				<td>
					<select name='trashyKeywords.<s:property value="key"/>'>
						<option value="true" <s:if test="value==true">selected</s:if> >true</option>
						<option value="false" <s:if test="value==false">selected</s:if> >false</option>
					</select>
				</td>
			</tr>
			</s:iterator>
 		</table>
 		<div align="left" style="clear:both">
	 		<s:submit action="Save" value="Save">
			</s:submit>
 		</div>
	 </s:form>
	</body>
</html>
