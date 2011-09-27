
<style type="text/css">

/*security table*/
#securities table{
	width:100%;
	padding:none;
	margin:0;
}
#securities .compare{
	color:#3333ff;
}
/*end security table*/

/*security compare*/
#compareDiv label{
	font-size:10pt;
	padding:0;
	margin:0;
	width:10em;
	float:left;
}
/*end security compare*/
/*calculate result*/
</style>

<#if securityMPTList??>
	<table id="securities" class="tablesorter" style="background: none repeat scroll 0 0 #FFFFFF;" width="98%" sytle="height:99%">
		<thead>
			<tr>
			<#if securityMPTList.size()< 7>
			<th width="25%">&nbsp;</th>
			<#else>
			<th width="6%">&nbsp;</th>
			</#if>
			<#list securityMPTList as security>
				<#if security.year == -1>
					<th class="{sorter:'percent'}">Last 1 Years</th>
				<#elseif security.year == -3>
					<th class="{sorter:'percent'}">Last 3 Years</th>
				<#elseif security.year == -5>
					<th class="{sorter:'percent'}">Last 5 Years</th>
				<#elseif security.year == 0>
					<th class="{sorter:'percent'}">Up To Date</th>
				<#else>
					<th class="{sorter:'percent'}">${security.year?c}</th>
				</#if>
			</#list>
			<tr>
		</thead>
		<tbody>
		<#if alpha == true>
			<tr id="Alpha">
				<#if compare==false><td>Alpha(%)</td><#else><td rowspan="2">Alpha(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.alpha??>${(security.alpha*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="Alpha_compare" >
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.alpha??>${(comparesecurity.alpha*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if beta == true>
			<tr id="Beta">
				<#if compare==false><td>Beta(%)</td><#else><td rowspan="2">Beta(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.beta??>${(security.beta*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="Beta_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.beta??>${(comparesecurity.beta*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if AR == true>
			<tr id="AR">
				<#if compare==false><td>AR(%)</td><#else><td rowspan="2">AR(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.AR??>${(security.AR*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="AR_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.AR??>${(comparesecurity.AR*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if RSquared == true>
			<tr id="RSquared">
				<#if compare==false><td>RSquared(%)</td><#else><td rowspan="2">RSquared(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.RSquared??>${(security.RSquared*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="RSquared_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.RSquared??>${(comparesecurity.RSquared*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if sharpeRatio == true>
			<tr id="SharpeRatio">
				<#if compare==false><td>Sharpe Ratio(%)</td><#else><td rowspan="2">Sharpe Ratio(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.sharpeRatio??>${(security.sharpeRatio*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="SharpeRatio_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.sharpeRatio??>${(comparesecurity.sharpeRatio*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if standardDeviation == true>
			<tr id="StandardDeviation">
				<#if compare==false><td>Standard Deviation(%)</td><#else><td rowspan="2">Standard Deviation(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.standardDeviation??>${(security.standardDeviation*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="StandardDeviation_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.standardDeviation??>${(comparesecurity.standardDeviation*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if treynorRatio == true>
			<tr id="TreynorRatio">
				<#if compare==false><td>Treynor Ratio(%)</td><#else><td rowspan="2">Treynor Ratio(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.treynorRatio??>${(security.treynorRatio*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="StandardDeviation_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.treynorRatio??>${(comparesecurity.treynorRatio*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if drawDown == true>
			<tr id="DrawDown">
				<#if compare==false><td>Draw Down(%)</td><#else><td rowspan="2">Draw Down(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.drawDown??>${(security.drawDown*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="DrawDown_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.drawDown??>${(comparesecurity.drawDown*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		<#if totalReturn == true>
			<tr id="TotalReturn">
				<#if compare==false><td>Return(%)</td><#else><td rowspan="2">Return(%)</td></#if>
				<#list securityMPTList as security>
				<td><#if security.return??>${(security.return*100)?string}<#else>NA</#if></td>
				</#list>
			</tr>
			<#if compare==true>
			<tr id="TotalReturn_compare">
				<#if compareSecurityMPTList??>
				<#list compareSecurityMPTList as comparesecurity>
				<td class="compare"><#if comparesecurity.return??>${(comparesecurity.return*100)?string}<#else>NA</#if></td>
				</#list>
				</#if>
			</tr>
			</#if>
		</#if>
		</tbody>
	</table>
</#if>
