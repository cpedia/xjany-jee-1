<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>Strategy Main Page</title>	
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
	<script>
	function dataFormat(data){
		if(data!=null){
			data = Math.round(parseFloat(data)*1000)/1000;
			document.write(data);
		}
	}
	
	/**
	 * name: dataFormat2
	 * function: format the data to percentage (##.##%)*/
	function dataFormat2(data){
		data = data * 100;
		data = Math.round(parseFloat(data)*1000)/1000;
		if(data>=0)
			document.write(data + "%");
		else
			document.write(data);
	}

	</script>
	<div class="fbbl_north">
		<s:include value="../header.jsp"></s:include>
	</div>
	<div class="fbbl_center">
	<div id="searchdiv" align="left">
		<p align="left">
			<s:form namespace="/jsp/strategy">
				<label><s:text name="strategy.name"></s:text>:</label>
				<s:textfield name="name" theme="simple"></s:textfield>
				<s:submit value="Search" action="NameSearch" theme="simple"></s:submit>
			</s:form>
			<s:form>
				<label><s:text name="categories"></s:text>:</label>
				<s:textfield name="categories" theme="simple"></s:textfield>
				<s:submit value="Search" action="CategorySearch" theme="simple"></s:submit>
			</s:form>
		</p>
	</div>
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
		<tr bgcolor="#ffffff">
			<td bgcolor="#FFFF9" align="center" width="46%" colspan="4">
				<s:property value="keyword"/>
			</td>			
			<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
			<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
			<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
			<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
			<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
			<td bgcolor="#FFFF9" align="center" width="7%">&nbsp;</td>
		</tr>
		<!-- show the strategies in the strategy class -->
		<s:iterator value="strategyItems">
		<tr bgcolor="#ffffff">
			<td align="center" width="18%">
			
				<s:url action="View.action" id="url_str" includeParams="none">
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
			<td width="10%">
				<s:property value="lastValidDate"/>
			</td>		
			<td width="12%">
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
	</table>
	</div>
	
	<div class="fbbl_east">
	</div>
	<div class="fbbl_west">
	</div>
	<div class="fbbl_south">
	</div>	
	</body>
</html>
