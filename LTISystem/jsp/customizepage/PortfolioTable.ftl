<style type="text/css">
.link1 a:link{color:#096199}
</style>
<p align="center" style="margin-bottom: 0cm;"><font face="Times New Roman, serif"><font face="Times New Roman, serif"><font size="3" style="font-size: 12pt;"><b>Lazy Model Portfolios</b></font></font></font></p>
<table bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" >
           <tr bgcolor="#ffffff">
            <td rowspan="2" align="center" width="19%">
                  <h4>portfolio</h4>
              </td>
               <td colspan="3" align="center" width="27%">
               <h4>1 year</s:text></h4>
            </td>
            <td colspan="3" align="center" width="27%">
               <h4>3 year</h4>
            </td>
            <td colspan="3" align="center" width="27%">
                  <h4>5 year</h4>
              </td>
        </tr>
           <tr bgcolor="#ffffff">
              <td align="center" width="9%">
                  Sharpe(%)
              </td>
              <td align="center" width="9%">
                  AR(%)
              </td>
              <td align="center" width="9%">
                  Beta(%)
              </td>
              <td align="center" width="9%">
                  Sharpe(%)
              </td>
              <td align="center" width="9%">
                  AR(%)
              </td>
              <td align="center" width="9%">
                  Beta(%)
              </td>
              <td align="center" width="9%">
                  Sharpe(%)
              </td>
              <td align="center" width="9%">
                  AR(%)
              </td>
              <td align="center" width="9%">
                  Beta(%)
              </td>
           </tr>
<#if lazy_portfolios?exists > 
       <#list lazy_portfolios as portfolio>

              <tr bgcolor="#ffffff" style="font-size: 13px;">

                  <td align="center" width="19%">
                  
                     <#if portfolio.name?exists ><a href="../portfolio/Edit.action?ID=${portfolio.ID}&action=view" target="_blank"><div id='shortPortfolioName' title="${portfolio.name}">${portfolio.showName}</div></a></#if>
                     
                  </td>         
                  <td align="center" width="9%">
                     <#if portfolio.sharpeRatio1?exists >${portfolio.sharpeRatio1}</#if>
                     
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.AR1?exists >${portfolio.AR1}</#if>
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.beta1?exists >${portfolio.beta1}</#if>
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.sharpeRatio3?exists >${portfolio.sharpeRatio3}</#if>
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.AR3?exists >${portfolio.AR3}</#if>
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.beta3?exists >${portfolio.beta3}</#if>
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.sharpeRatio5?exists >${portfolio.sharpeRatio5}</#if>
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.AR5?exists >${portfolio.AR5}</#if>
                  </td>
                  <td align="center" width="9%">
                     <#if portfolio.beta5?exists >${portfolio.beta5}</#if>
                  </td>
              </tr>    
              
</#list>
</#if>
</table>
