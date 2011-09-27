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
	
<%--<script>
	function abc()
	{
	var date=prompt("please input the date",defaultDate);
	if(date==null) return false;
	var url="http://"+location.host+"/LTISystem/"+date+"downloadlog.html";
	document.getElementById("checkSplitLink").setAttribute("href",url);
	
	
	document.getElementById("checkSplitLink").setAttribute("target","_blank");
	return true;
	}
</script>--%>	
<%--<a href="#" onclick="handleClick()" id="checkSplitLink">Check Split Log</a>	--%>				
					
											
			<td>
			</td>
		</table>
		<p class="title">Load Security Data</p>
<table>
<tr>
<td>
		<div>
		<s:form action="LoadSecurityData.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:textfield name="seucrityNames" label="Seucrity Names "/>
				<s:select list="{'true','false'}" name="diversified" label="Diversified "></s:select>
				<s:textfield name="startDate1" label="Start Date "/>
				<s:textfield name="endDate" label="End Date "/>
				<s:file name="upFile" label="Upload File "></s:file>
	 			<s:submit></s:submit>
		</s:form>
   		</div>
</td>
</tr>
<tr>   		
<td>
   		<div>
   		<s:form action="DownloadLog.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:textfield id="Name" name="name" label="load date"/>
				<s:hidden name="logType" value="NORMAL"/>
				<s:submit></s:submit>
		</s:form>
		</div>
</td>
</tr>

<tr>   		
<td>
   		<div>
   		<s:form action="DownloadLog.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:textfield id="Name2" name="name" label="End of Date load date"/>
				<s:hidden name="logType" value="EOD"/>
				<s:submit></s:submit>
		</s:form>
		</div>
</td>
</tr>

<tr>   		
<td>
   		<div>
   		<s:form action="StopUpdateDailyData.action" method="post" namespace="/jsp/admin/loaddata" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
				<s:submit>Stop Updating Security Daily Data </s:submit>
		</s:form>
		</div>
</td>
</tr>

</table>
		<script>

			var today=new Date();
			today.setDate(today.getDate()-1);
			
			var month=(today.getMonth()+1).toString();
			if(month<10) month="0"+month;
			var day=today.getDate().toString();
			if(day<10) day="0"+day;
			
			var defaultDate=today.getFullYear().toString()+month+day;
			$('#Name').val(defaultDate);	
			$('#Name2').val(defaultDate);
	
		</script>
		
	</body>
</html>
