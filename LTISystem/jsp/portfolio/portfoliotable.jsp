<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
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
	<s:if test="#request.portfolios!=null && portfolios.size()>0">
	$("#portfolios<s:property value='type'/>")
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
	</s:if>
	<s:if test="#request.portfolios.size() > 30">
	.tablesorterPager({container: $("#pager<s:property value='type'/>")});
	</s:if>
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

<table class="tablesorter" id='portfolios<s:property value="type"/>'border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
<s:if test="#request.portfolios != null">
<thead>
<!-- 表头设计 -->
<tr bgcolor="#FFFFFF">
<!-- 这里将表格内设置为另一个颜色，是产生细线的重要条件 -->
      <th width="15%" align="center"><s:text name="name.portfolio"></s:text></th>
      <th width="12.5%" align="center"><s:text name="last.valid.date"></s:text></th>
      <th width="12.5%" align="center"><s:text name="last.valid.date"></s:text></th>
      <th width="10%" align="center">1 <s:text name="year"></s:text> <s:text name="sharpe"></s:text>(%)</th>
      <th width="10%" align="center">1 <s:text name="year"></s:text> <s:text name="AR"></s:text>(%)</th>
      <th width="10%" align="center">3 <s:text name="years"></s:text> <s:text name="sharpe"></s:text>(%)</th>
      <th width="10%" align="center">3 <s:text name="years"></s:text> <s:text name="AR"></s:text>(%)</th>
	  <th width="10%" align="center">5 <s:text name="years"></s:text> <s:text name="sharpe"></s:text>(%)</th>
      <th width="10%" align="center">5 <s:text name="years"></s:text> <s:text name="AR"></s:text>(%)</th>
</tr>
</thead>
<s:iterator value="#request.portfolios" id="portfolio" status="st">
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
	<td width="12.5%" align="center"><s:property value="lastValidDate"/></td>
	<td width="12.5%" align="center"><s:property value="lastTransactionDate"/></td>
	<!-- sharpe for one year -->
	<td width="10%" align="center">
		<s:property value="sharpeRatio1" default="NA"/>
	</td>
	<!-- annual return for one year -->
	<td width="10%" align="center">
		<s:property value="AR1" default="NA"/>
	</td>
	<!-- sharpe for three years -->
	<td width="10%" align="center">
		<s:property value="sharpeRatio3" default="NA"/>
	</td>
	<!-- annual return for three years -->
	<td width="10%" align="center">
		<s:property value="AR3" default="NA"/>
	</td>
	<!-- sharpe for five years -->
	<td width="10%" align="center">
		<s:property value="sharpeRatio5" default="NA"/>
	</td>
	<!-- annual return for five years -->
	<td width="10%" align="center">
		<s:property value="AR5" default="NA"/>
	</td>
</tr>
</s:iterator>
</s:if>
<s:else>
	<tr><td>
	No Portfolios!
	</td></tr>
</s:else>
</table>

<s:if test="portfolios.size() > 30">
<div style="posistion:static" id='pager<s:property value="type"/>' class="pager">
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
</s:if>
<div>
<br class="clear"/>
<br>
</div>
