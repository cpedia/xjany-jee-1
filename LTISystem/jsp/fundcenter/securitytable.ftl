[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
[#if securityList??]
	<table id="securityTable_${securityList.get(0).getSymbol()}" class="tablesorter" style="height:99%;padding:0;margin:0;margin-bottom:0">
		<thead>
			<tr>
			<th>Name</th>
			<th>Symbol</th>
			<th>Type</th>
			[#if isUseAssetClass==true]
				<th>Asset Type</th>
			[/#if]
			[#if MPTList?? && MPTList.size()!=0]
				[#list MPTList as mpt]
					<th style="width: 14px" class="{sorter:'num'}">
						[#if mpt.year < 1000 && mpt.year !=0]
                                                [#if mpt.name == "sharpeRatio"]
                                                ${mpt.year * -1} Year Sharpe (%)
                                                [#else]
						${mpt.year * -1} Year ${mpt.name}(%)
                                                [/#if]
						[#elseif mpt.year ==0]
						up-to-date ${mpt.name}
						[#else]
                                                [#if mpt.name == "sharpeRatio"]
                                                ${mpt.year * -1} Year Sharpe (%)
                                                [#else]
						${mpt.year * -1} Year ${mpt.name}(%)
                                                [/#if]
						[/#if]
					</th>
				[/#list]
			[/#if]
			[#if extras?? && extras.size()!=0]
				[#list extras as extra]
					[#if extra.sort??]
					<th class="{sorter:'num',defaultsort:${extra.sort}}">
						${extra.time}-${extra.attributeName}(%)
					</th>
					[#else]
					<th class="{sorter:'num'}">
						${extra.time}-${extra.attributeName}(%)
					</th>
					[/#if]
				[/#list]
			[/#if]
			</tr>
		</thead>
		<tbody>
		[#list securityList as security]
			[#if security_index%2==0]
				<tr class='odd'>
			[/#if]
			[#if security_index%2==1]
				<tr class='even'>
			[/#if]
			[@s.url id="security_url" namespace="/jsp/fundcenter" action="View"]
				[@s.param name="symbol"]${security.symbol}[/@s.param]
				[@s.param name="title"]${security.symbol}[/@s.param]
			[/@s.url]
			<td><a target="_top" href='${security_url}'>${security.securityName}</a></td>
			<td>${security.symbol}</td>
			<td>${security.securityTypeName!}</td>
			[#if isUseAssetClass==true]
				<td>${assetClass}</td>
			[/#if]
			[#if security.MPTStatistics??]
				[#list security.MPTStatistics as mpt]
					<td>${mpt}</td>
				[/#list]
			[/#if]

			[#list security.extras as extra]
				<td>${extra}</td>
			[/#list]
		</tr>
		[/#list]
		</tbody>
	</table>
	[#if securityList.size() > 30]
	<br class="clear"/>
	<div style="posistion:static" id='pager' class="pager">
		<form>
			<img src="${lti.baseUrl}/jsp/images/jquery.tablesorter/addons/pager/icons/first.png" class="first"/>
			<img src="${lti.baseUrl}/jsp/images/jquery.tablesorter/addons/pager/icons/prev.png" class="prev"/>
			<input type="text" class="pagedisplay"/>
			<img src="${lti.baseUrl}/jsp/images/jquery.tablesorter/addons/pager/icons/next.png" class="next"/>
			<img src="${lti.baseUrl}/jsp/images/jquery.tablesorter/addons/pager/icons/last.png" class="last"/>
			<select class="pagesize">
				<option value="10">10</option>
				<option value="20">20</option>
				<option selected="selected" value="30">30</option>
				<option value="40">40</option>
			</select>
		</form>
	</div>
	
	[/#if]
	<script type="text/javascript">
	$.tablesorter.addParser({
		id: 'num',
		is: function(s) {
			// return false so this parser is not auto detected
			return false;
		},
		format: function(s) {// format your data for normalization
			var n;
			n = s.toLowerCase().replace(/na/, Number.NEGATIVE_INFINITY);
			return formatFloat(n);
		},         
		type: 'numeric' 
	});
	$(document).ready(function(){
		$("#securityTable_${securityList.get(0).getSymbol()}")
		.tablesorter({
			sorter:'num', 
			sortList:[[${sortColumn},${isDesc}]]
		})
		[#if securityList.size() > 30]
		.tablesorterPager({container: $("#pager"),size: 30});
		[/#if]
	}); 
	</script>
[#else]
	[@s.actionerror/]
[/#if]
