[#ftl]
Total Size:${output.totalSize}
<br>
Update to No.${output.currentPointer+1}
<br>

<table border=1>
<tr>
	<td>order</td>
	<td>id</td>
	<td>name</td>
	<td>end date</td>
	<td>status</td>
</tr>
[#list output.portfolios as portfolio]
<tr>
<td>
${portfolio_index+1}
</td>
<td>
${portfolio.portfolioID}
</td>
<td>
<a href="http://www.validfi.com/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${portfolio.portfolioID}">
${portfolio.portfolioName} 
</a>
</td>
<td>
[#if portfolio.endDate??]${portfolio.endDate?string("yyyy-MM-dd")}[/#if]
</td>
<td>
[#if portfolio_index < output.currentPointer]
Finish
[#else]
Not Finish
[/#if]
</td>
</tr>
[/#list]
</table>

