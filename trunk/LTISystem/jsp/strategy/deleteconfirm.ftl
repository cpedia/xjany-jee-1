[#ftl]
<div>
The following portfolios will be also deleted, are you sure to continue?
[#if portfolios?? && portfolios.size()>0]
	<table width="100%">
	[#list portfolios as p]
		<tr>
			<td>
				${p.ID}
			</td>
			<td>
				<a href="/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${p.ID}" target="_blank">${p.name}</a>
			</td>
		</tr>
	[/#list]
	</table>
[#else]
	<p>No portfolios!</p>
[/#if]
	<p><button onclick="window.location.href='Save.action?operation=delete&ID=${ID}'">Delete</button></p>
</div>
 