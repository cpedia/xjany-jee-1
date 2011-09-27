<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<LINK href="../images/style.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
	<SCRIPT src="../images/jquery.tooltip/PopDetails.js" type="text/javascript" ></SCRIPT>
	<script src="../images/jquery.tooltip/jquery.bgiframe.js" type="text/javascript"></script>
	<script src="../images/jquery.tooltip/chili-1.7.pack.js" type="text/javascript"></script>
	<script src="../images/jquery.tooltip/jquery.tooltip.js" type="text/javascript"></script>
	<link rel="stylesheet" href="../images/jquery.tooltip/jquery.tooltip.css" />
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
	<title>Portfolio Main Page</title>
</head>

<body>
<style>
	.odd {
	background-color:#FFFF99;  /* pale yellow for odd rows */
	}
	.even {
	background-color:white;  /* pale blue for even rows */
	}
</style>
<script>
$(document).ready(function() {
	$('tr[@id^="item"]:odd').children('td').addClass('odd');
	$('tr[@id^="item"]:even').children('td').addClass('even');
});

</script>
<script>
function dataFormat(data){
	if(data!=null)
	{
		data = Math.round(parseFloat(data)*1000)/1000
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
<div class="fbbl_center" align="center" id="center_div">
<!-- private portlolios list -->
<s:url namespace="/jsp/portfolio" action="Edit" id="create_url">
	<s:param name="ID" value="0"></s:param>
</s:url>
<div align="center"><s:a href="%{create_url}"><s:text name="create.portfolio"></s:text></s:a></div>
<p class="head">
 <s:text name="my.portfolio"></s:text>
</p>
<s:if test="#request.privatePortfolio.items != null">
<table width="80%" border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
<!-- 表头设计 -->
<tr bgcolor="#FFFFFF">
<!-- 这里将表格内设置为另一个颜色，是产生细线的重要条件 -->
      <td width="15%" rowspan="2" align="center"><h2><s:text name="name.portfolio"></s:text></h2></td>
      <td width="10%" rowspan="2" align="center"><h2><s:text name="last.valid.date"></s:text></h2></td>
      <td width="25%" colspan="2" align="center"><h3>1 <s:text name="year"></s:text></h3></td>
      <td width="25%" colspan="2" align="center"><h3>3 <s:text name="years"></s:text></h3></td>
      <td width="25%" colspan="2" align="center"><h3>5 <s:text name="years"></s:text></h3></td>
</tr>
<tr bgcolor="#FFFFFF">
  <td width="12.5%" align="center"><s:text name="sharpe"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="AR"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="sharpe"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="AR"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="sharpe"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="AR"></s:text>(%)</td>
</tr>

<s:iterator value="#request.privatePortfolio.items" id="privatePortfolio" status="st">
<tr id='item<s:property value="#st.count"/>' bgcolor="#FFFFFF">
<!-- 按照格式显示portfolio的名字，过长的部分用省略号代替，鼠标经过时可以看到全名 -->
	<td id='shortName<s:property value="#st.count"/>' width="15%" align="center" title="<s:property value='name'/>">
		<s:url id="portfolio_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
			<s:param name="ID" value="ID"></s:param>
		</s:url>
		<s:a href="%{portfolio_url}">
			<div id="shortName" title="<s:property value='name'/>">
				<s:property value="showName"/>
			</div>
		</s:a>
	</td>
	<td width="10%" align="center"><s:property value="lastValidDate"/></td>
<!-- 显示数据形式，如果当天的alpha和beta值不存在则显示NA -->
<!-- sharpe for one year -->
  <td width="12.5%" align="center">
  	<s:property value="sharpeRatio1" default="NA"/> 	
  </td>
<!-- annual return for one year -->
  <td width="12.5%" align="center">
  	<s:property value="AR1" default="NA"/>
  </td>
<!-- sharpe for three years -->
  <td width="12.5%" align="center">
  	<s:property value="sharpeRatio3" default="NA"/>
  </td>
<!-- annual return for three years -->
  <td width="12.5%" align="center">
  	<s:property value="AR3" default="NA"/>
  </td>
  
<!-- sharpe for five years -->
  <td width="12.5%" align="center">
  	<s:property value="sharpeRatio5" default="NA"/>
  </td>
<!-- annual return for five years -->
  <td width="12.5%" align="center">
  	<s:property value="AR5" default="NA"/>
  </td>
</tr>
</s:iterator>
</table>
<!-- The pages choice-->
<center>
<s:text name="change.to"></s:text>
<s:iterator value="#request.privatePortfolio.parameters" id="parameter" status="st">
	<s:url action="Main.action" id="pageurl">
		<s:param name="privateStartIndex" value="#parameter.startIndex"></s:param>
		<s:param name="modelStartIndex" value="#parameter.modelStartIndex"></s:param>
	</s:url>
					
	<s:a href="%{pageurl}">
		<s:property value="#st.count"/>
	</s:a>
</s:iterator>
<s:text name="page"></s:text>
</center>
</s:if>
<s:else>
<center> <font color="red"><s:text name="no.my.portfolio"></s:text></font></center>
</s:else>


<!-- model portfolios -->
<p class="head">
<s:text name="model.portfolio"></s:text>
</p>

<table width="80%" border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
<s:if test="#request.modelPortfolio.items != null">
<!-- 表头设计 -->
<tr bgcolor="#FFFFFF">
<!-- 这里将表格内设置为另一个颜色，是产生细线的重要条件 -->
      <td width="15%" rowspan="2" align="center"><h2><s:text name="name.portfolio"></s:text></h2></td>
      <td width="10%" rowspan="2" align="center"><h2><s:text name="last.valid.date"></s:text></h2></td>
      <td width="25%" colspan="2" align="center"><h3>1 <s:text name="year"></s:text></h3></td>
      <td width="25%" colspan="2" align="center"><h3>3 <s:text name="years"></s:text></h3></td>
      <td width="25%" colspan="2" align="center"><h3>5 <s:text name="years"></s:text></h3></td>
</tr>
<tr bgcolor="#FFFFFF">
  <td width="12.5%" align="center"><s:text name="sharpe"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="AR"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="sharpe"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="AR"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="sharpe"></s:text>(%)</td>
  <td width="12.5%" align="center"><s:text name="AR"></s:text>(%)</td>
</tr>

<s:iterator value="#request.modelPortfolio.items" id="privatePortfolio" status="st">
<s:set name="index" value="#st.count"></s:set>
<tr id='item<s:property value="#st.count"/>' bgcolor="#FFFFFF">
<!-- 按照格式显示portfolio的名字，过长的部分用省略号代替，鼠标经过时可以看到全名 -->
	<td id="shortName${index}" width="15%" align="center" title="<s:property value='name'/>">
		<s:url id="portfolio_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
			<s:param name="ID" value="ID"></s:param>
		</s:url>
		<s:a href="%{portfolio_url}">
			<s:property value="showName"/>
		</s:a>
	</td>
	<td width="10%" align="center"><s:property value="lastValidDate"/></td>
<!-- 显示数据形式，如果当天的alpha和beta值不存在则显示NA -->
<!-- sharpe for one year -->
  <td width="12.5%" align="center">
  	<s:property value="sharpeRatio1" default="NA"/>
  </td>
<!-- annual return for one year -->
  <td width="12.5%" align="center">
	<s:property value="AR1" default="NA"/>
  </td>
<!-- sharpe for three years -->
  <td width="12.5%" align="center">
	<s:property value="sharpeRatio3" default="NA"/>
  </td>
<!-- annual return for three years -->
  <td width="12.5%" align="center">
  	<s:property value="AR3" default="NA"/>
  </td>
  
<!-- sharpe for five years -->
  <td width="12.5%" align="center">
  	<s:property value="sharpeRatio5" default="NA"/>
  </td>
<!-- annual return for five years -->
  <td width="12.5%" align="center">
  	<s:property value="AR5" default="NA"/>
  </td>
</tr>
</s:iterator>
</s:if>
<s:else>
<tr bgcolor="#FFFFFF">
<td width="100%" align="center" colspan="10">
	<s:text name="no.model.portfolio"></s:text>
</td>
</tr>
</s:else>
</table>

<!-- The pages choice-->
<center>
<s:text name="change.to"></s:text>
<s:iterator value="#request.modelPortfolio.parameters" id="parameter" status="st">
	<s:url action="Main.action" id="pageurl">
		<s:param name="modelStartIndex" value="#parameter.startIndex"></s:param>
		<s:param name="privateStartIndex" value="#parameter.privateStartIndex"></s:param>
	</s:url>
					
	<s:a href="%{pageurl}">
		<s:property value="#st.count"/>
	</s:a>
</s:iterator>
<s:text name="page"></s:text>	
</center>	
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
