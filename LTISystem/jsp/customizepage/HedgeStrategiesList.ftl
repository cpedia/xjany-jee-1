<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Hedging Strategies</title>

<!--revised by fangzhenju-->
<!--<link rel="stylesheet" href="/LTISystem/UserFiles/Image/vf.css" type="text/css" />-->
</head>

<body>
<!-- wrap starts here -->
<div id="wrap">	
				
	<!-- content-wrap starts here -->
	<div id="content-wrap">		
	<p>&nbsp;</p>
<h1 style="text-align: center;">List of Hedge Strategies</h1>
<p>&nbsp;</p>		
<style type="text/css">
.link1 a:link{color:#096199}
</style>
<table align="center" bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" width="95%" style="margin-left: 15px;">
           <tr bgcolor="#ffffff">
              <td rowspan="2" align="center" width="20%">
                  <h4>strategy</h4>        
              </td>
            <td rowspan="2" align="center" width="20%">
                  <h4>portfolio</h4>
              </td>
               <td colspan="2" align="center" width="20%">
               <h4>1 year</s:text></h4>
            </td>
            <td colspan="2" align="center" width="20%">
               <h4>3 year</h4>
            </td>
            <td colspan="2" align="center" width="20%">
                  <h4>5 year</h4>
              </td>
        </tr>
           <tr bgcolor="#ffffff">
              <td align="center" width="10%">
                  Sharpe(%)
              </td>
              <td align="center" width="10%">
                  AR(%)
              </td>
              <td align="center" width="10%">
                  Sharpe(%)
              </td>
              <td align="center" width="10%">
                  AR(%)
              </td>
              <td align="center" width="10%">
                  Sharpe(%)
              </td>
              <td align="center" width="10%">
                  AR(%)
              </td>
           </tr>
<#if strategies?exists > 
       <#list strategies as strategy>
              <tr bgcolor="#57E964" style="font-size: 15px;">
                  <td align="center" width="40%" colspan="2">
                    <b> <span class="link1"><a href="../ajax/CustomizePage.action?pageName=StrategyClass${strategy.getClassID()}&includeHeader=true" target="_blank">${strategy.getName()}</a></span></b>
                  </td>         
                  <td align="center" width="10%">&nbsp;</td>
                  <td align="center" width="10%">&nbsp;</td>
                  <td align="center" width="10%">&nbsp;</td>
                  <td align="center" width="10%">&nbsp;</td>
                  <td align="center" width="10%">&nbsp;</td>
                  <td align="center" width="10%">&nbsp;</td>
              </tr>

<#list strategy.items as item>
              <tr bgcolor="#ffffff" style="font-size: 13px;">
                  <td align="center" width="20%">
                     <a href="../strategy/View.action?ID=${item.ID}&action=view" target="_blank"><div id='shortName' title="${item.name}">${item.showName}</div></a>
                     
                  </td>
                  <td align="center" width="20%">
                  
                     <#if item.portfolioName?exists >                     <a href="../portfolio/Edit.action?ID=${item.portfolioID}&action=view" target="_blank"><div id='shortPortfolioName' title="${item.portfolioName}">${item.portfolioShortName}</div></a></#if>
                     
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

              
</#list>
       </#if>
           </table>		
	</div>



<!--#include file="/LTISystem/UserFiles/Image/vf_footer.shtml" --> 
<!-- wrap ends here -->
</div>

</body>
</html>
