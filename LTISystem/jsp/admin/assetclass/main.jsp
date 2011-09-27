<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Reorder TreePanel</title>
<link rel="stylesheet" type="text/css" href="images/ext/ext-all.css" />

    <!-- GC -->
 	<!-- LIBS -->
 	<script type="text/javascript" src="images/ext/ext-base.js"></script>
 	<!-- ENDLIBS -->

    <script type="text/javascript" src="images/ext/ext-all.js"></script>
    
<script type="text/javascript" src="images/reorder.js"></script>

<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
<link href="../images/style.css" rel="stylesheet" type="text/css" />
</head onload="adjHiehgt()">
<body>


<table class="nav" width="100%">
			<td width="20%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/assetclass" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Asset Class Manager</s:a>				
			</td>		
			<td width="20%">
				<s:url action="Save.action" id="url_create" namespace="/jsp/admin/assetclass" includeParams="none">
					<s:param name="parentID" value="ID"/>
			
					<s:param name="action">create</s:param>
				</s:url>
				<s:a href="%{url_create}">Create A Asset Class</s:a>				
			</td>
			<td width="20%">
				<s:url action="ModifyAssetClass.action" id="url_merge" namespace="/jsp/admin/assetclass" includeParams="none">
				</s:url>
				<s:a href="%{url_merge}">Modify Asset Class</s:a>
			</td>
			<td width="40%">
			</td>
</table>
<div id="tree-div" style="overflow:auto; height:90%;width:95%;border:1px solid #c3daf9;"></div>
</body>
</html>
