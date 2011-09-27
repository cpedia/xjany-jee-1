<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />		
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="15%">
				<s:url value="loadsecuritydata.jsp" id="url_1" namespace="/jsp/admin/loaddata" includeParams="none">
				</s:url>
				<s:a href="%{url_1}">Load Security Data</s:a>				
			</td>		
			<td width="15%">
				<s:url value="importsecuritydata.jsp" id="url_2" namespace="/jsp/admin/loaddata" includeParams="none">
				</s:url>
				<s:a href="%{url_2}">Import Security Data</s:a>			
			</td>
			<td width="15%">
				<s:url value="exportsecuritydata.jsp" id="url_3" namespace="/jsp/admin/loaddata" includeParams="none">
				</s:url>
				<s:a href="%{url_3}">Export Security Data</s:a>							
			</td>		
			<td width="15%">
				<s:url value="importecnomicdata.jsp" id="url_4" namespace="/jsp/admin/loaddata" includeParams="none">
				</s:url>
				<s:a href="%{url_4}">Import Ecnomic Data</s:a>			
			</td>
			<td width="15%">
				<s:url value="importecnomicdatas.jsp" id="url_5" namespace="/jsp/admin/loaddata" includeParams="none">
				</s:url>
				<s:a href="%{url_5}">Import Ecnomic Datas</s:a>			
			</td>
			<td width="15%">
				<s:url value="exportecnomicdata.jsp" id="url_6" namespace="/jsp/admin/loaddata" includeParams="none">
				</s:url>
				<s:a href="%{url_6}">Export Ecnomic Data</s:a>			
			</td>
			<td width="15%">
				<s:url action="UpdateSecurityDailyData" id="url_7" namespace="/jsp/admin/loaddata" includeParams="none">
				</s:url>
				<s:a href="%{url_7}" onclick="return confirm('Are you sure?');">Update Daily Data</s:a>				
			</td>										
			<td>
			</td>
		</table>
		<p class="title">Import Security Data</p>
		<s:url value="ltisamples.csv" id="s"></s:url>
		<s:a href="%{s}">Samples</s:a>
		<s:form action="ImportSecurityData.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:actionerror label="ExcuteMessage"/>
				<s:file name="uploadFile" label="Upload File "></s:file>
				<s:select list="{'true','false'}" name="flash" label="Flash UpDate?"></s:select>
				<s:select list="{'true','false'}" name="overWrite" label="Over Write?"></s:select>
				<s:hidden name="action" value="OLD_VERSION"> </s:hidden> 
	 			<s:submit></s:submit>
	 		</table>
		</s:form>
		
		<s:url value="new_ltisamples.csv" id="s"></s:url>
		<s:a href="%{s}">New_Samples</s:a>
		<s:form action="ImportSecurityData.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:actionerror label="ExcuteMessage"/>
				<s:file name="uploadFile" label="Upload File "></s:file>
				<s:select list="{'true','false'}" name="flash" label="Flash UpDate?"></s:select>
				<s:select list="{'true','false'}" name="overWrite" label="Over Write?"></s:select>
				<s:hidden name="action" value="NEW_VERSION"> </s:hidden>
	 			<s:submit></s:submit>
	 		</table>
		</s:form>
		
		<s:url value="portfolio.csv" id="s"></s:url>
		<s:a href="%{s}">Update_Portfolio_Samples</s:a>
		<s:form action="UpdatePortfolioData.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:file name="uploadFile" label="Upload File "></s:file>
	 			<s:submit></s:submit>
	 		</table>
		</s:form>
		<br>
		<s:label value="Update_Adjust_Close(For Security)"></s:label>
		<form action="UpdateAdjustClose.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<tr>
					<td>Upload File(One Column of SecurityID)</td>
					<td><input type="file" name="uploadFile"></td>
				</tr>
				<tr>
					<td>Start Date(From Which Date to adjust) </td>
					<td><input type="text" name="dateStr"><br></td>
				</tr>
				<tr>
					<td><input type="submit" value="submit"></td>
				</tr>
	 		</table>
		</form>
	</body>
</html>
