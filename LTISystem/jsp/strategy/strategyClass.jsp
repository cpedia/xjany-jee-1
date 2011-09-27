<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib prefix="authz" uri="/WEB-INF/authz.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title><s:property value="sc.Name"/> Page</title>	
<script type="text/javascript" src="../strategy/images/strategyClass.js"></script>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>	
<link rel="stylesheet" href="../images/jquery.tooltip/jquery.tooltip.css" />
<SCRIPT src="../images/jquery.tooltip/PopDetails.js" type="text/javascript" ></SCRIPT>
<script src="../images/jquery.tooltip/chili-1.7.pack.js" type="text/javascript"></script>
<script src="../images/jquery.tooltip/jquery.tooltip.js" type="text/javascript"></script>

<!-- new theme -->
<link rel="stylesheet" href="../images/flora.all.css" type="text/css" media="screen" title="Flora (Default)">
<script type="text/javascript" src="../images/jquery.ui/ui.core.packed.js"></script>
<script type="text/javascript" src="../images/jquery.ui/ui.tabs.packed.js"></script>
<script type="text/javascript" src="../images/fbborderlayout/fbborderlayout.js"></script>
<script src="../images/jquery.ui.dialog/ui.dialog.packed.js" type="text/javascript"></script>
<script src="../images/jquery.ui.dialog/ui.draggable.packed.js" type="text/javascript"></script>
<script src="../images/jquery.ui.dialog/ui.droppable.packed.js" type="text/javascript"></script>
<script src="../images/jquery.ui.dialog/ui.resizable.packed.js" type="text/javascript"></script>
<script type="text/javascript" src="../images/jquery.dimensions.js"></script>
<script type="text/javascript" src="../images/jquery.bgiframe.js"></script>
<script type="text/javascript" src="../images/jquery.jdMenu/jquery.positionBy.js"></script>
<script type="text/javascript" src="../images/jquery.jdMenu/jquery.jdMenu.js"></script>
<link rel="stylesheet" href="../images/jquery.jdMenu/jquery.jdMenu.css" type="text/css" />

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

.strategyTable
{
width:98%;

border:1px solid #000;
}
.strategyTable td
{
border-left:1px solid #000;
border-top:1px solid #000;
}
.strategyTable .firstConner
{
border-left:none;
border-top:none;
}
.strategyTable .firstCol
{
border-left:none;
}
.strategyTable .firstRow
{
border-top:none;
}
.clear
{
clear:both;
}
#STUp_text
{
background:url(../jsp/images/go_left.gif) no-repeat left top;
}


<!-- These CSS are necessary END -->
</style>
</head>
<body>
	<script>
	$(document).ready(function(){
    $(".fbbl_center > ul").tabs();
    $('#overview_edit').hide();
   	$('#STUp_edit').hide();
   	$('#STDown_edit').hide();
   	$('#LU_edit').hide();
  });

	</script>
	<div class="fbbl_north">
		<s:include value="../header.jsp"></s:include>
	</div>
	
	<div class="fbbl_center">
		<ul>
		<li><a href='#OverViewTab'><span>Overview</span></a></li>
        <li><a href="#relativeStrategyTable"><span>List of strategies</span></a></li>
        <li><a href="#latestUpdate"><span>Latest Update</span></a></li>
        </ul>
        

        <div id="OverViewTab">
        <form action="Save.action?ID=<s:property value='ID'/>&action=updateOverView" method="post">
		<div id="overview_text" ><s:property value="sc.OverView" escape="false"/></div>
		<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
		<div id="overview_edit">
						<table width="100%">
						<td>
						<div><input type="hidden" id="OverView" name="sc.OverView" value='<s:property value="sc.OverView" escape="false"/>'><input type="hidden" id="OverView___Config" value=""><iframe id="OverView___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no"></iframe></div>
						</td>
						</table>
		</div>
		<div id="overview_editnofck"></div>
		<input type="button" id="overviewEdit" onclick="ShowEditor('OverView','overview')" value='<s:text name="edit"></s:text>' />
		<input type="button" id="overviewEditNoFck" onclick="ShowEditorNoFck('OverView','overview')" value='EditNoFck' />
		
		<input type="submit" value="Submit" id="submitOverView">

		</authz:authorize>
		</form>
	</div>
		
		<div id="relativeStrategyTable">
		<form action="Save.action?ID=<s:property value='ID'/>&action=updateStrategyTable" method="post">
		<div id="STUp_text"><s:property value="sc.StrategyTableUp" escape="false"/></div>
		<div class="clear"></div>
		<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
		<div id="STUp_edit">
						<table width="100%">
						<td>
						<div><input type="hidden" id="StrategyTableUp" name="sc.StrategyTableUp" value='<s:property value="sc.StrategyTableUp" escape="false"/>'><input type="hidden" id="StrategyTableUp___Config" value=""><iframe id="StrategyTableUp___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no"></iframe></div>
						</td>
						</table>
		</div>
		<div id="STUp_editnofck"></div>
		 <input type="button" id="STUpEdit" onclick="ShowEditor('StrategyTableUp','STUp')" value='<s:text name="edit"></s:text>' />
		 <input type="button" id="STUpEditNoFck" onclick="ShowEditorNoFck('StrategyTableUp','STUp')" value='EditNoFck' />
		
		</authz:authorize>
	<table id = "strategies" bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" width="100%">
		<tr bgcolor="#ffffff">
			<td rowspan="2" align="center" width="18%">
				<h3><s:text name="strategy"></s:text></h3>        
			</td>
			<td rowspan="2" align="center" width="18%">
				<h3><s:text name="portfolio"></s:text></h3>
			</td>
			<td rowspan="2" align="center" width="10%">
				<h3><s:text name="last.valid.date"></s:text></h3>
			</td>
			<td rowspan="2" align="center" width="10%">
				<h3><s:text name="last.transaction.date"></s:text></h3>
			</td>
  			<td colspan="2" align="center" width="14%">
				<h3>1 <s:text name="year"></s:text></h3>
			</td>
			<td colspan="2" align="center" width="14%">
				<h3>3 <s:text name="years"></s:text></h3>
			</td>
			<td colspan="2" align="center" width="14%">
				<h3>5 <s:text name="years"></s:text></h3>
			</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td align="center" width="7%">
				<s:text name="sharpe"></s:text>(%)
			</td>
			<td align="center" width="7%">
				<s:text name="AR"></s:text>(%)
			</td>
			<td align="center" width="7%">
				<s:text name="sharpe"></s:text>(%)
			</td>
			<td align="center" width="7%">
				<s:text name="AR"></s:text>(%)
			</td>
			<td align="center" width="7%">
				<s:text name="sharpe"></s:text>(%)
			</td>
			<td align="center" width="7%">
				<s:text name="AR"></s:text>(%)
			</td>
		</tr>
		<!-- show the strategy class -->
        <s:iterator value="sis" status="st">
			<tr bgcolor="#ffffff">
				<td bgcolor="#FFFF9" align="center" width="46%" colspan="4">
				<s:url action="Save.action" namespace="/jsp/strategyClass" id="url_strategyClass" includeParams="none">
				<s:param name="ID" value="ClassID"></s:param>
				<s:param name="action">view</s:param>
				</s:url>
					<s:a href="%{url_strategyClass}"><s:property value="Name"/></s:a>
				</td>			
				<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
				<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
				<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
				<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
				<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
				<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
			</tr>
			<!-- show the strategies in the strategy class -->
			<s:iterator value="items">
			<tr bgcolor="#ffffff">
				<td align="center" width="18%">
				
					<s:url action="View.action" namespace="/jsp/strategy" id="url_str" includeParams="none">
						<s:param name="ID" value="ID"></s:param>	
						<s:param name="action">view</s:param>				
					</s:url>
					<s:a href="%{url_str}"><div id='shortName' title="<s:property value='name'/>"><s:property value="showName"/></div></s:a>
					
				</td>
				<td align="center" width="18%">
				
					<s:url action="Edit.action" id="url_port" namespace="/jsp/portfolio" includeParams="none">
						<s:param name="ID" value="portfolioID"></s:param>
						<s:param name="action">view</s:param>						
					</s:url>
					<s:a href="%{url_port}"><div id="shortName" title="<s:property value='portfolioName'/>"><s:property value="portfolioShortName"/></div></s:a>
					
				</td>
				<td>
					<s:property value="lastValidDate"/>
				</td>	
				<td>
					<s:property value="lastTransactionDate"/>
				</td>		
				<td align="center" width="7%">
					<s:property value="sharpeRatio1"/>
				</td>
				<td align="center" width="7%">
					<s:property value="AR1"/>
				</td>
				<td align="center" width="7%">
					<s:property value="sharpeRatio3"/>
				</td>
				<td align="center" width="7%">
					<s:property value="AR3"/>
				</td>
				<td align="center" width="7%">
					<s:property value="sharpeRatio5"/>		
				</td>
				<td align="center" width="7%">
					<s:property value="AR5"/>
				</td>
			</tr>
			</s:iterator>  
		</s:iterator>      
	</table>
			<div id="STDown_text" ><s:property value="sc.StrategyTableDown" escape="false"/></div>
		<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
		<div id="STDown_edit">
						<table width="100%">
						<td>
						<div><input type="hidden" id="StrategyTableDown" name="sc.StrategyTableDown" value='<s:property value="sc.StrategyTableDown" escape="false"/>'><input type="hidden" id="StrategyTableDown___Config" value=""><iframe id="StrategyTableDown___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no"></iframe></div>
						</td>
						</table>
		</div>
		<div id="STDown_editnofck"></div>
		 <input type="button" id="STDownEdit" onclick="ShowEditor('StrategyTableDown','STDown')" value='<s:text name="edit"></s:text>' />
		 <input type="button" id="STDownEditNoFck" onclick="ShowEditorNoFck('StrategyTableDown','STDown')" value='EditNoFck' />
		<input type="submit" value="Submit" id="submitStrategyTable"/>
		</authz:authorize>
		</form>
		</div>
		
		
		<div id="latestUpdate">
		<form action="Save.action?ID=<s:property value='ID'/>&action=updateLatestCommons" method="post">
		<div id="LU_text" ><s:property value="sc.LatestCommons" escape="false"/></div>
		<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
		<div id="LU_edit">
						<table width="100%">
						<td>
						<div><input type="hidden" id="LatestCommons" name="sc.LatestCommons" value='<s:property value="sc.LatestCommons" escape="false"/>'><input type="hidden" id="LatestCommons___Config" value=""><iframe id="LatestCommons___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no"></iframe></div>
						</td>
						</table>
		</div>
		<div id="LU_editnofck"></div>
		 <input type="button" id="LUEdit" onclick="ShowEditor('LatestCommons','LU')" value='<s:text name="edit"></s:text>' />
		  <input type="button" id="LUEditNoFck" onclick="ShowEditorNoFck('LatestCommons','LU')" value='EditNoFck' />
		  
		  
				<input type="submit" value="Submit" id="submitLatestCommons"/>
		</authz:authorize>
		</form>
		</div>
	
	</form>
	</div>
	
	
	<div class="fbbl_east">
	<s:if test="customizeRegion!=null && customizeRegion.eastRegionName!=null">
		<s:url id="east_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.eastRegionName'/></s:param>
		</s:url>
		
		<iframe id="east" scrolling="yes" width="100%" height="99%" src='<s:property value="east_url"/>'></iframe>
		
	</s:if>
	</div>
	<div class="fbbl_west">
	<s:if test="customizeRegion!=null && customizeRegion.westRegionName!=null">
		<s:url id="west_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.westRegionName'/></s:param>
		</s:url>
		<iframe id="west" scrolling="yes" width="100%" height="99%" src='<s:property value="west_url"/>'></iframe>
	</s:if>
	</div>
	<div class="fbbl_south">
	<s:if test="customizeRegion!=null && customizeRegion.southRegionName!=null">
		<s:url id="south_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.southRegionName'/></s:param>
		</s:url>
		<iframe id="south" scrolling="yes" width="100%" height="99%" src='<s:property value="south_url"/>'></iframe>
	</s:if>
	</div>	
	</body>
</html>