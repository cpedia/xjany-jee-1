[#ftl]
<h1 style="text-align:center">MyPlanIQ Model Portfolios for FolioInvesting.com</h1><br>
<p>MyPlanIQ is pleased to offer the following model portfolios on folioinvesting.com. These portfolios are listed on folioinvesting.com as what they call folios. To mirror these portfolios, you need to have an account on folioinvesting.com. FolioInvesting offers a one-click rebalancing mechanism that with proper setup, you will be able to rebalance your account based on the target allocations in one of the model portfolios you choose. </p>
<p>MyPlanIQ offers two types of model portfolios: 
<ul>
<li><strong>Strategic Allocation Model Portfolios</strong> using MyPlanIQ <a href="http://www.myplaniq.com/LTISystem/jsp/strategy/View.action?ID=771">SAA (Strategic Asset Allocation) strategy</a>. Any MyPlanIQ registered user can access these portfolios real time free of charge. </li>
<li><strong>Tactical Allocation Model Portfolios</strong> using MyPlanIQ <a href="http://www.myplaniq.com/LTISystem/jsp/strategy/View.action?ID=1003">TAA (Tactical Asset Allocation) strategy</a>. To access these portfolios real time, you need to be a MyPlanIQ <a href="http://www.myplaniq.com/LTISystem/paypal_subscr.action">subscriber</a>. </li>
</ul>
</p>
<p>These portfolios are customized model portfolios using ETFs in <a href="http://www.myplaniq.com/LTISystem/f401k_view.action?ID=1,429">MyPlanIQ Allocation Folio Plan</a>. Please visit the plan page for more information on ETFs used. The portfolios have the following five different <a href="http://www.myplaniq.com/LTISystem/profile_edit.action?planID=0&portfolioID=0">risk profiles</a>:</p>
<ul>
<li><strong>Very Conservative:</strong> this portfolio has risk profile 80. Its target allocations are: 20% risk assets and 80% fixed income.</li>
<li><strong>Conservative:</strong> this portfolio has risk profile 60. Its target allocations are: 40% risk assets and 60% fixed income.</li>
<li><strong>Balanced:</strong> this portfolio has risk profile 50. Its target allocations are: 50% risk assets and 50% fixed income.</li>
<li><strong>Moderate:</strong> this portfolio has risk profile 40. Its target allocations are: 60% risk assets and 40% fixed income.</li>
<li><strong>Growth:</strong> this portfolio has risk profile 20. Its target allocations are: 80% risk assets and 20% fixed income.</li>
</ul><br>
<p>Investors should pay close attention to choosing an appropriate risk profile before deciding what model portfolios to follow.</p>
<h2 style="text-align:center">MyPlanIQ Strategic Allocation Model Portfolios on FolioInvesting.com</h2><br>

<table class="tablesorter" id='portfolios' border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
<thead>
<!-- &#34920;&#22836;&#35774;&#35745; -->
<tr bgcolor="#FFFFFF">
<!-- &#36825;&#37324;&#23558;&#34920;&#26684;&#20869;&#35774;&#32622;&#20026;&#21478;&#19968;&#20010;&#39068;&#33394;&#65292;&#26159;&#20135;&#29983;&#32454;&#32447;&#30340;&#37325;&#35201;&#26465;&#20214; -->
      <th align="center">Name</s:text></th>

      <th width="6%" align="center">Access FolioInvesting</s:text></th>
      <th width="6%" align="center">Last Rebalance Date</th>
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

	<td><a href="https://www.folioinvesting.com/rtg/index.jsp">Goto FolioInvesting</a></td>
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

</tr>
[/#list]
</table>

<h2 style="text-align:center">MyPlanIQ Tactical Allocation Model Portfolios on FolioInvesting.com</h2>
<p style="text-align:center; color:green"><i>Need to be a subscriber to see real time info</i></p><br>

<table class="tablesorter" id='portfolios1' border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
<thead>
<!-- &#34920;&#22836;&#35774;&#35745; -->
<tr bgcolor="#FFFFFF">
<!-- &#36825;&#37324;&#23558;&#34920;&#26684;&#20869;&#35774;&#32622;&#20026;&#21478;&#19968;&#20010;&#39068;&#33394;&#65292;&#26159;&#20135;&#29983;&#32454;&#32447;&#30340;&#37325;&#35201;&#26465;&#20214; -->
      <th align="center">Name</s:text></th>

      <th width="6%" align="center">Access FolioInvesting</s:text></th>
      <th width="6%" align="center">Last Rebalance Date</th>
      <th width="6%" align="center">1 Year Sharpe(%)</th>
      <th width="6%" align="center">1 Year AR(%)</th>
       <th width="6%" align="center">3 Year Sharpe(%)</th>
      <th width="6%" align="center">3 Year AR(%)</th>
      <th width="6%" align="center">5 Year Sharpe(%)</th>
      <th width="6%" align="center">5 Year AR(%)</th>

</tr>
</thead>
<!-- &#25353;&#29031;&#26684;&#24335;&#26174;&#31034;portfolio&#30340;&#21517;&#23383;&#65292;&#36807;&#38271;&#30340;&#37096;&#20998;&#29992;&#30465;&#30053;&#21495;&#20195;&#26367;&#65292;&#40736;&#26631;&#32463;&#36807;&#26102;&#21487;&#20197;&#30475;&#21040;&#20840;&#21517; -->
[#list portfolios1 as portfolio]
<tr>
	<td id="shortName" title="${portfolio.portfolioName}">
		<a href="/LTISystem/jsp/portfolio/Edit.action?ID=${portfolio.portfolioID}">${portfolio.portfolioName}</a>
	</td>

	<td><a href="https://www.folioinvesting.com/rtg/index.jsp">Goto FolioInvesting</a></td>
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

</tr>
[/#list]
</table>
<script>
$('#portfolios')
	.tablesorter();
</script>
<script>
$('#portfolios1')
	.tablesorter();
</script>
