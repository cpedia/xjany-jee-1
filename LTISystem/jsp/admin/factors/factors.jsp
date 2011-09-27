<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />		
		<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>
	</head>
	<body>

<p class="title">Fators Manager</p>
		
		<s:url value="Factors_sample.csv" id="s">
		</s:url>
		<s:a href="%{s}">Factors Samples</s:a>
<table width="100%">
<tr>
<td width="100%">
		<div>
		<s:form action="UploadFactorsFile.action" method="post" namespace="/jsp/admin/factors" enctype="multipart/form-data">
				Upload new RAA-Factors File
				<p>
				<s:file name="uploadFile" theme="simple"></s:file>
				<s:hidden name="type" value="RAA" theme="simple"></s:hidden>
	 			<s:submit theme="simple"></s:submit>
	 			</p>
		</s:form>
   		</div>
</td>
</tr>

<tr>   		
<td width="100%">
   		<div>
   		<s:form action="DownloadFactorsFile.action" method="post" namespace="/jsp/admin/factors" enctype="multipart/form-data">
				Download current RAA-Factors File
			<p>
			<s:hidden name="type" value="RAA" theme="simple"></s:hidden>
			<s:submit theme="simple"></s:submit>
			</p>
		</s:form>
		</div>
</td>
</tr>


<tr>
<td width="100%">
		<div>
		<s:form action="UploadFactorsFile.action" method="post" namespace="/jsp/admin/factors" enctype="multipart/form-data">
				Upload new Style-Factors File
				<p>
				<s:file name="uploadFile" theme="simple"></s:file>
				<s:hidden name="type" value="Style" theme="simple"></s:hidden>
	 			<s:submit theme="simple"></s:submit>
	 			</p>
		</s:form>
   		</div>
</td>
</tr>

<tr>   		
<td width="100%">
   		<div>
   		<s:form action="DownloadFactorsFile.action" method="post" namespace="/jsp/admin/factors" enctype="multipart/form-data">
				Download current Style-Factors File
			<p>
			<s:hidden name="type" value="Style" theme="simple"></s:hidden>
			<s:submit theme="simple"></s:submit>
			</p>
		</s:form>
		</div>
</td>
</tr>

</table>

		
	</body>
</html>
