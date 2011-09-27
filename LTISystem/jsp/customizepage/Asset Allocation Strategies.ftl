<style type="text/css">
.link1 a:link{color:#096199}
</style>
<p align="center" style="margin-bottom: 0cm;"><font face="Times New Roman, serif"><font face="Times New Roman, serif"><font size="3" style="font-size: 12pt;"><b>Top Risk-Adjusted Performance Strategies</b></font></font></font></p>
<table bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" >
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
                    <b> <span class="link1"><a href="../strategyClass/Save.action?ID=${strategy.getClassID()}&action=view" target="_blank">${strategy.getName()}</a></span></b>
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
