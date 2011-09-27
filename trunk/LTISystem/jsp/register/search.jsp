<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title><s:property value="%{pageName}" escape="false"/></title>
<LINK href="../../images/style.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<!-- new theme -->
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
<script type="text/javascript">
var address=document.location.hostname;
var port=document.location.port;
if(port=="")port='80';


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
	
});

<!-- Initialize BorderLayout END-->
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

<div class="fbbl_center" id="flashcontent">
<div id="cse-search-results"></div>
<script type="text/javascript">
  var googleSearchIframeName = "cse-search-results";
  var googleSearchFormName = "cse-search-box";
  var googleSearchFrameWidth = 900;
  var googleSearchDomain = "www.google.com";
  var googleSearchPath = "/cse";
</script>
<script type="text/javascript" src="http://www.google.com/afsonline/show_afs_search.js"></script>



</div> 

<div class="fbbl_east">
</div>
<div class="fbbl_west">
</div>
<div class="fbbl_south">
</div>
</body>
</html>
