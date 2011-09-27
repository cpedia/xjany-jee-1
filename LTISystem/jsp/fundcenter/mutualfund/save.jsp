<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page  contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding = "UTF-8"%>
<html>
	<head xmlns="http://www.w3.org/1999/xhtml">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<LINK href="../../images/style.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<!-- the whole system layout -->
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
		<!-- table sorter -->
		<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
		<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
		<link rel="stylesheet" href="../images/jquery.tablesorter/addons/pager/jquery.tablesorter.pager.css" type="text/css" />
		
		<title>mutual fund</title>
		<!-- Initialize BorderLayout START-->
		<script type="text/javascript">
		$(function () {
			$.FBBorderLayout({
				spacing: 5,
				<s:if test="customizeRegion==null || customizeRegion.westRegionName==null">
				west_collapsable: false,
				west_collapsed:true,
				</s:if>
				<s:else> 
				west_width: "<s:property value='customizeRegion.westWidthStr'/>",
				west_collapsable: true,
				</s:else> 
				<s:if test="customizeRegion==null || customizeRegion.southRegionName==null">
				south_collapsable: false,
				south_collapsed:true,
				</s:if>
				<s:else>
				south_width: "<s:property value='customizeRegion.southHeightStr'/>",
				south_collapsable: true,
				</s:else> 
				<s:if test="customizeRegion==null || customizeRegion.eastRegionName==null">
				east_collapsable: false,
				east_collapsed:true,
				</s:if>
				<s:else>
				east_width: "<s:property value='customizeRegion.eastWidthStr'/>",
				east_collapsable: true,
				</s:else>
				north_collapsed:false,
				north_collapsable:false
			});		
			$("#performance").html("Loading Data...");
			//alert(window.location.search);
			var paraStr = window.location.search.substr(1).split("&");
			//alert(paraStr.length);
			var symbol="";
			for(i = 0; i < paraStr.length; i++){
				var pair = paraStr[i].split("=");
				if(pair[0].toLowerCase() == "symbol"){
					symbol = pair[1];
					break;
				}
			}
			$("#performance").load("Partial.action?symbol=" + symbol + "&chosenYear=-1,-3,-5");
			//alert("hello!");			
		});
		</script>
		<!-- Initialize BorderLayout END-->
		
		<style type="text/css">		
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
		<style>
		#create{
		float:left;
		}
		#search{
		float:right;
		}
		.clear{
		clear:both;
		}
		</style>
		<div class="fbbl_north">
			<s:include value="../../header.jsp"></s:include>
		</div>
		<div class="fbbl_center">
			<s:url id="content_url" namespace="/jsp/ajax" action="CustomizePage">
				<s:param name="pageName">FundIndividualPage</s:param>
			</s:url>
			<iframe id="content" scrolling="yes" width="100%" height="99%" src='<s:property value="content_url" escape="false"/>'></iframe>
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