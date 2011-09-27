[#ftl]
<table width="100%" border=0>
<tr>
<td>
<span style="font-size:18pt;line-height:1.5em;"><strong>${security.name!} (${security.symbol!})</strong>${security.securityTypeName!}:${security.className!}</span>
<span style="margin-left:20px;color:red;white-space:nowrap">start from [#if startDate??]${startDate?string("yyyy-MM-dd")}[#else]${"-"}[/#if]</span>
</td>
<td align="right">
<a target="_blank" href="http://finance.yahoo.com/q?s=${Parameters.symbol}&ql=1">Yahoo Quote</a>
<td align="right">
<a target="_blank" href="/LTISystem/jsp/portfolio/ComparePortfolio.action?portfolioString=${Parameters.symbol}">Compare With Other Funds/Portfolios</a>
</td>
</tr>
</table>


