<p align="center" style="margin-bottom: 0cm;"><font face="Times New Roman, serif"><font face="Times New Roman, serif"><font size="6" style="font-size: 26pt;"><b>Top Risk-Adjusted Performance Strategies</b></font></font></font></p>
<table bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" width="100%">
           <tr bgcolor="#ffffff">
              <td rowspan="2" align="center" width="23%">
                  <h3>strategy</h3>        
              </td>
            <td rowspan="2" align="center" width="23%">
                  <h3>portfolio</h3>
              </td>
               <td colspan="3" align="center" width="18%">
               <h3>1 year</s:text></h3>
            </td>
            <td colspan="3" align="center" width="18%">
               <h3>3 year</h3>
            </td>
            <td colspan="3" align="center" width="18%">
                  <h3>5 year</h3>
              </td>
        </tr>
           <tr bgcolor="#ffffff">
              <td align="center" width="6%">
                  sharpe(%)
              </td>
              <td align="center" width="6%">
                  beta(%)
              </td>
              <td align="center" width="6%">
                  AR(%)
              </td>
              <td align="center" width="6%">
                  sharpe(%)
              </td>
              <td align="center" width="6%">
                  beta(%)
              </td>
              <td align="center" width="6%">
                  AR(%)
              </td>
              <td align="center" width="6%">
                  sharpe(%)
              </td>
              <td align="center" width="6%">
                  beta(%)
              </td>
              <td align="center" width="6%">
                  AR(%)
              </td>
           </tr>
<#if strategies?exists > 
       <#list strategies as strategy>
              <tr bgcolor="#ffffff">
                  <td align="center" width="46%" colspan="2">
                     <a href="../strategyClass/Save.action?ID=${strategy.getClassID()}&action=view" target="_blank">${strategy.getName()}</a>
                  </td>         
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
                  <td align="center" width="6%">&nbsp;</td>
              </tr>

<#list strategy.items as item>
              <tr bgcolor="#ffffff">
                  <td align="center" width="23%">           
                     <a href="../strategy/Save.action?ID=${item.ID}&action=view" target="_blank">${item.name}</a>
                     
                  </td>
                  <td align="center" width="23%">
                  
                     <#if item.portfolioName?exists > ${item.portfolioName}</#if>
                     
                  </td>         
                  <td align="center" width="6%">
                     <#if item.sharpeRatio1?exists >${item.sharpeRatio1}</#if>
                     
                  </td>
                  <td align="center" width="6%">
                     <#if item.beta1?exists >${item.beta1}</#if>
                  </td>
                  <td align="center" width="6%">
                     <#if item.AR1?exists >${item.AR1}</#if>
                  </td>
                  <td align="center" width="6%">
                     <#if item.sharpeRatio3?exists >${item.sharpeRatio3}</#if>
                  </td>
                  <td align="center" width="6%">
                     <#if item.beta3?exists >${item.beta3}</#if>
                  </td>
                  <td align="center" width="6%">
                     <#if item.AR3?exists >${item.AR3}</#if>
                  </td>
                  <td align="center" width="6%">
                     <#if item.sharpeRatio5?exists >${item.sharpeRatio5}</#if>
                  </td>
                  <td align="center" width="6%">
                     <#if item.beta5?exists >${item.beta5}</#if>
                  </td>
                  <td align="center" width="6%">
                     <#if item.AR5?exists >${item.AR5}</#if>
                  </td>
              </tr>    
              </#list>     

              
</#list>
       </#if>
           </table>
