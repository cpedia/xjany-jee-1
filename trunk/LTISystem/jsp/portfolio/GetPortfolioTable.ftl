[#ftl]
[#if portfolios?? && portfolios?size > 0]
<!-- ${lastUpdated?datetime} -->
<style type="text/css">
table,input,button,combox,select,textarea{
font-family: Arial, Helvetica, sans-serif;
font-size: 12px;
}
.clear{
	clear:both;
}
</style>

<script>
$.tablesorter.addParser({
	id: 'num',
	is: function(s) {
		// return false so this parser is not auto detected
		return false;
	},
	format: function(s) {// format your data for normalization
		var n;
		n = s.toLowerCase().replace(/na/, Number.NEGATIVE_INFINITY);
		return parseFloat(n);
	},         
	type: 'numeric' 
})
$(document).ready(
	function(){
	
	$('#portfolios${updateTime}')
	.tablesorter({
		widthFixed: true, 
		widgets: ['zebra'],
		headers: {
		 	3:{sorter:'num'},
		 	4:{sorter:'num'},
		 	5:{sorter:'num'},
		 	6:{sorter:'num'},
		 	7:{sorter:'num'},
		 	8:{sorter:'num'}
		},
		sortList: [[3,1]]
	})
	
	[#if portfolios?? && portfolios?size > 40 && paged]
	.tablesorterPager({container: $("#pager${updateTime}"),size:40});
	[/#if]
}); 

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
	document.write(data + "%");
}
</script>
[#if title??]<span style="font-size:1.7em;">${title}[/#if][#if hasMore?? && hasMore]</span> &nbsp;&nbsp;<a href='More
.action?paged=false&size=0&[#if title??]title=All%20${title}&[/#if]&groupIDs=${groupIDs}&admin=${admin?string}&owner=${owner?string}' target="_blank">More</a>[/#if]
<table class="tablesorter" id='portfolios${updateTime}'border="0" cellpadding="0" cellspacing="1" bgcolor="#000000" width=100%>
<thead>
<!-- 表头设计 -->
<tr bgcolor="#FFFFFF">
<!-- 这里将表格内设置为另一个颜色，是产生细线的重要条件 -->
      <th align="center">Name</s:text></th>
[#if !owner ||(owner && hasSubscred)]
      <th width="6%" align="center">Last Valid Date</s:text></th>
      <th width="6%" align="center">Last Transaction Date</th>
      <th width="6%" align="center">1 Year Sharpe(%)</th>
      <th width="6%" align="center">1 Year AR(%)</th>
       <th width="6%" align="center">3 Year Sharpe(%)</th>
      <th width="6%" align="center">3 Year AR(%)</th>
      <th width="6%" align="center">5 Year Sharpe(%)</th>
      <th width="6%" align="center">5 Year AR(%)</th>
[/#if]
</tr>
</thead>
<!-- 按照格式显示portfolio的名字，过长的部分用省略号代替，鼠标经过时可以看到全名 -->
[#list portfolios as portfolio]
<tr>
	<td id="shortName" title="${portfolio.name}">
		<a href="/LTISystem/jsp/portfolio/Edit.action?ID=${portfolio.ID}">${portfolio.showName}</a>
	</td>
[#if !owner ||(owner && hasSubscred)]
	<td>${portfolio.lastValidDate!"NA"}</td>
	<td>${portfolio.lastTransactionDate!"NA"}</td>
	<td>
		${portfolio.sharpeRatio1!"NA"}
	</td>
	<td>
		${portfolio.AR1!"NA"}
	</td>
	<td>
		${portfolio.sharpeRatio3!"NA"}
	</td>
	<td>
		${portfolio.AR3!"NA"}
	</td>
	<td>
		${portfolio.sharpeRatio5!"NA"}
	</td>
	<td>
		${portfolio.AR5!"NA"}
	</td>
[/#if]
</tr>
[/#list]
</table>

[#if portfolios?? && portfolios?size > 40 && paged]
<div style="posistion:static" id='pager${updateTime}' class="pager">
	<form>
		<img src="../images/jquery.tablesorter/addons/pager/icons/first.png" class="first"/>
		<img src="../images/jquery.tablesorter/addons/pager/icons/prev.png" class="prev"/>
		<input type="text" class="pagedisplay"/>
		<img src="../images/jquery.tablesorter/addons/pager/icons/next.png" class="next"/>
		<img src="../images/jquery.tablesorter/addons/pager/icons/last.png" class="last"/>
		<select class="pagesize">
			<option selected="selected"  value="10">10</option>
			<option value="20">20</option>
			<option value="30">30</option>
			<option value="40">40</option>
		</select>
	</form>
</div>
[/#if]
[/#if]