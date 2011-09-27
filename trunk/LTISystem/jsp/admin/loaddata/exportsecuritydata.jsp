<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />	
		<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>	
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
		<p class="title">Export Holiday Data</p>
		<s:form action="ExportSecurityData.action" method="post" namespace="/jsp/admin/loaddata">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:textfield id="Filename" name="filename" label="File Name "></s:textfield>
	 			<s:submit></s:submit>
		</s:form>
		<br>
		<br>
		<br>
		<div>
		<s:form action="ExportPortfolioData.action" method="post" namespace="/jsp/admin/loaddata">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0  width="95%" align="left">
				Update Mode:<s:select id="portfolioSelect" list="#{'0':'ALL','1':'ALIVE','2':'UNALIVE','3':'DEVELOPMENT','4':'LOWLEVEL','5':'EMAILALERT'}" value = "portfolioMode"  name="updateMode"></s:select>
				<s:textfield id="PortfolioFilename" name="portfolioFilename" label="File Name "></s:textfield>
	 			<s:submit></s:submit>
		</s:form>
		</div>
   <script>		
		var defaultFile="allSecurity.csv";
		$('#Filename').val(defaultFile);
	</script>
	
	<script>
		var defaultFile="allPortfolio.csv";
		$('#PortfolioFilename').val(defaultFile);
    </script>
	</body>
</html>
