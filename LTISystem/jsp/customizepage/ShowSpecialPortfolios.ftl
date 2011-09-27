[#ftl]
<table class="tablesorter" id='portfolios' border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
<thead>
<!-- &#34920;&#22836;&#35774;&#35745; -->
<tr bgcolor="#FFFFFF">
<!-- &#36825;&#37324;&#23558;&#34920;&#26684;&#20869;&#35774;&#32622;&#20026;&#21478;&#19968;&#20010;&#39068;&#33394;&#65292;&#26159;&#20135;&#29983;&#32454;&#32447;&#30340;&#37325;&#35201;&#26465;&#20214; -->
      <th align="center">Name</s:text></th>

      <th width="6%" align="center">Last Valid Date</s:text></th>
      <th width="6%" align="center">Last Transaction Date</th>
      <th width="6%" align="center">1 Year Sharpe(%)</th>
      <th width="6%" align="center">1 Year AR(%)</th>
       <th width="6%" align="center">3 Year Sharpe(%)</th>
      <th width="6%" align="center">3 Year AR(%)</th>
      <th width="6%" align="center">5 Year Sharpe(%)</th>
      <th width="6%" align="center">5 Year AR(%)</th>

</tr>
</thead>
<!-- &#25353;&#29031;&#26684;&#24335;&#26174;&#31034;portfolio&#30340;&#21517;&#23383;&#65292;&#36807;&#38271;&#30340;&#37096;&#20998;&#29992;&#30465;&#30053;&#21495;&#20195;&#26367;&#65292;&#40736;&#26631;&#32463;&#36807;&#26102;&#21487;&#20197;&#30475;&#21040;&#20840;&#21517; -->
[#list portfolios as portfolio]
<tr>
	<td id="shortName" title="${portfolio.portfolioName}">
		<a href="/LTISystem/jsp/portfolio/Edit.action?ID=${portfolio.portfolioID}">${portfolio.portfolioName}</a>
	</td>

	<td>
		[#if portfolio.lastValidDate??]
			${portfolio.lastValidDate?string("MM/dd/yyyy")}
		[/#if]
	</td>
	<td>
		[#if portfolio.lastTransactionDate??]
			${portfolio.lastTransactionDate?string("MM/dd/yyyy")}
		[/#if]
	</td>
	<td>
		[#if portfolio.sharpeRatio1??]
			${(portfolio.sharpeRatio1*100)?string("0.##")}%
		[#else]
			NA
		[/#if]
	</td>
	<td>
		[#if portfolio.AR1??]
			${(portfolio.AR1*100)?string("0.##")}%
		[#else]
			NA
		[/#if]
	</td>
	<td>
		[#if portfolio.sharpeRatio3??]
			${(portfolio.sharpeRatio3*100)?string("0.##")}%
		[#else]
			NA
		[/#if]
	</td>
	<td>
		[#if portfolio.AR3??]
			${(portfolio.AR3*100)?string("0.##")}%
		[#else]
			NA
		[/#if]
	</td>
	<td>
		[#if portfolio.sharpeRatio5??]
			${(portfolio.sharpeRatio5*100)?string("0.##")}%
		[#else]
			NA
		[/#if]
	</td>
	<td>
		[#if portfolio.AR5??]
			${(portfolio.AR5*100)?string("0.##")}%
		[#else]
			NA
		[/#if]
	</td>

</tr>
[/#list]
</table>
<script>
$('#portfolios')
	.tablesorter();
</script>
${133.1?string("0.##")}