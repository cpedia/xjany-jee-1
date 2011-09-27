[#ftl]
<html>
	<head>
		<title>Transactions</title>
	</head>
	<body>
		<p class="title">${portfolio.name }</p>
		[#if isHolding?? && isHolding]
			To Date: ${holdingDate}
		[/#if]
		<table width="100%">
			<tr class="trHeader">
			      
				<td>Operation</td>	
				<td>Date</td>	
				<td>Percentage</td>	
				<td>Symbol</td>	
				<td>Asset Name</td>	
			</tr>		
			[#list transactions as t]
				<tr>

				<td>
					${t.operation}
				</td>	
				<td>
					${t.dateStr}
				</td>		
				<td>
					${(t.percentage*100)?string("0.##")}%
				</td>					
				<td>
					${t.symbol!""}
				</td>	
				<td>
					${t.assetName!""}
				</td>		
			
				</tr>
			[/#list]

		</table>
		<div id="button" align="center">
		<input type="button" value='Back' onclick="backToPortfolio()">				
		<script>
		/**
		 * backToPortfolio
		 * @param {}  
		 */
		 function backToPortfolio() {
		 	window.location.href = '/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${portfolioID}';
		 }
		</script>
		</div>
	</body>
</html>
