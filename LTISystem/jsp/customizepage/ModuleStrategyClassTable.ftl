${javascript}

<link rel="stylesheet" href="../strategy/images/tablesorter/style.css" type="text/css" />

<script type="text/javascript" src="../strategy/images/tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="../strategy/images/tablesorter/jquery.tablesorter.pager.js"></script>

<table id = 'strategyClassTable${classid}'  width="100%"  class="tablesorter">
<thead>
<tr>
  <th class="header">
    Strategies
  </th>
  <th class="header">
    Portfolios
  </th>
  <th class="header">
    Last Valid Date
  </th>
  <th class="header">
    Last Transaction Date
  </th>
  <th class="header">
    1 Year Sharpe(%)
  </th>
  <th class="header">
    1 Year AR(%)
  </th>
  <th class="header">
    3 Years Sharpe(%)
  </th>
  <th class="header">
    3 Years AR(%)
  </th>
  <th class="header">
    5 Years Sharpe(%)
  </th>
  <th class="header">
    5 Years AR(%)
  </th>
</tr>
</thead>
<#if items?exists >
<#list items as item>
<tr bgcolor="#ffffff">

  <td align="center" width="18%">
    <a href="../strategy/View.action?ID=${item.ID}&action=view" target="_blank"><div id='shortName' title="${item.name}">${item.showName}</div></a>
  </td>

  <td align="center" width="18%">
<#if item.portfolioName?exists && item.portfolioName!="NA"><a href="../portfolio/Edit.action?ID=${item.portfolioID}&action=view" target="_blank"><div id='shortPortfolioName' title="${item.portfolioName}">${item.portfolioShortName}</div></a></#if>		
  </td>

  <td>
<#if item.lastValidDate?exists >${item.lastValidDate}</#if>
  </td>	

  <td>
<#if item.lastTransactionDate?exists >${item.lastTransactionDate}</#if>
  </td>	
		
  <td align="center" width="10%">
<#if item.sharpeRatio1?exists >${item.sharpeRatio1}</#if>
  </td>

  <td align="center" width="10%">
<#if item.AR1?exists >${item.AR1}</#if>
  </td>

  <td align="center" width="10%">
<#if item.sharpeRatio3?exists >${item.sharpeRatio3}</#if>
  </td>

  <td align="center" width="10%">
<#if item.AR3?exists >${item.AR3}</#if>
</td>

  <td align="center" width="10%">
<#if item.sharpeRatio5?exists >${item.sharpeRatio5}</#if>
  </td>

  <td align="center" width="10%">
<#if item.AR5?exists >${item.AR5}</#if>
</td>

</tr>
</#list>  
</#if>
</table>
<script>
$('#strategyClassTable${classid}').tablesorter(); 
</script>
