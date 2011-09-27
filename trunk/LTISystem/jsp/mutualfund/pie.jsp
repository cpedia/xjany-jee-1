<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<LINK href="../../images/style.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<!-- new theme -->
		<link rel="stylesheet" href="../images/flora.all.css" type="text/css" media="screen" title="Flora (Default)">
		<script type="text/javascript" src="../images/jquery.ui/ui.core.packed.js"></script>
		<script type="text/javascript" src="../images/jquery.ui/ui.tabs.packed.js"></script>
		<script type="text/javascript" src="../images/fbborderlayout/fbborderlayout.js"></script>
		<script type="text/javascript" src="../images/jquery.dimensions.js"></script>
		<script type="text/javascript" src="../images/jquery.bgiframe.js"></script>
		<script type="text/javascript" src="../images/jquery.jdMenu/jquery.positionBy.js"></script>
		<script type="text/javascript" src="../images/jquery.jdMenu/jquery.jdMenu.js"></script>
		<link rel="stylesheet" href="../images/jquery.jdMenu/jquery.jdMenu.css" type="text/css" />
		
		<script src="../images/jquery.ui.dialog/ui.dialog.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.draggable.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.droppable.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.resizable.packed.js" type="text/javascript"></script>
		<title>Security Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />

		<script type="text/javascript">
		$(function () {
			$.FBBorderLayout({
				spacing: 5,
				<s:if test="customizeRegion==null || customizeRegion.westRegionName==null">
				west_collapsable: false,
				west_collapsed:true,
				</s:if>
				<s:else> 
				west_width: 300,
				west_collapsable: true,
				</s:else> 
				<s:if test="customizeRegion==null || customizeRegion.southRegionName==null">
				south_collapsable: false,
				south_collapsed:true,
				</s:if>
				<s:else>
				south_width: 300,
				south_collapsable: true,
				</s:else> 
				<s:if test="customizeRegion==null || customizeRegion.eastRegionName==null">
				east_collapsable: false,
				east_collapsed:true,
				</s:if>
				<s:else>
				east_width: 300,
				east_collapsable: true,
				</s:else>
				north_collapsed:false
			});
			
		});
		
		<!-- Initialize BorderLayout END-->
		/*
		$(document).ready(function(){
		    $('.fbbl_north').load("../header.jsp");
		  });
		*/
		</script>
		<style type="text/css">
		table,input,button,combox,select,textarea{
		font-family: Arial, Helvetica, sans-serif;
		font-size: 12px;
		}
		
		<!-- These CSS are necessary START -->
		body{
		background:#DFE8F6;
		}
		.fbbl_north,
		.fbbl_south,
		.fbbl_east,
		.fbbl_west,
		.fbbl_center {
		 padding: 10px;
		 border: 1px solid #ccc;
		 background:#FffFff;
		}
		.fbbl_north{
			height:90px;
		}
		
		<!-- These CSS are necessary END -->
		</style>
	</head>
	<body>
	<div class="fbbl_north">
		<s:include value="../header.jsp"></s:include>
	</div>
	<div class="fbbl_center" id="security_div">
		<img src='../../<s:property value="filePath"/>'>
	</div>	
	<div class="fbbl_east">
	<s:if test='customizeRegion!=null && customizeRegion.eastRegionName!=null && !customizeRegion.eastRegionName.equals("Fixed")'>
		<s:url id="east_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.eastRegionName'/></s:param>
		</s:url>
		<iframe id="east" scrolling="yes" width="100%" height="99%" src='<s:property value="east_url"/>'></iframe>
	</s:if>
	</div>
	<div class="fbbl_west">
	<s:if test='customizeRegion!=null && customizeRegion.westRegionName!=null && !customizeRegion.westRegionName.equals("Fixed")'>
		<s:url id="west_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.westRegionName'/></s:param>
		</s:url>
		<iframe id="west" scrolling="yes" width="100%" height="99%" src='<s:property value="west_url"/>'></iframe>
	</s:if>
	</div>
	<div class="fbbl_south">
	<s:if test='customizeRegion!=null && customizeRegion.southRegionName!=null && !customizeRegion.southRegionName.equals("Fixed")'>
		<s:url id="south_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.southRegionName'/></s:param>
		</s:url>
		<iframe id="south" scrolling="yes" width="100%" height="99%" src='<s:property value="south_url"/>'></iframe>
	</s:if>	
	</div>	
	</body>
</html>
