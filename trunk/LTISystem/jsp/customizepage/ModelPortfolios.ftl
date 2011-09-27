<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>ValidFi Model Portfolios</title>
<meta name="portfolios" content="vf_current" />
<meta name="submenu" content="portfoliotable" />
<meta name="modelPortfolios" content="vf_current" />
<!--revised by fangzhenju-->
<!--<link rel="stylesheet" href="/LTISystem/UserFiles/Image/vf.css" type="text/css" />-->
<style type="text/css">
table{
	margin:0 auto;
}
</style>
</head>
<body>
<!-- wrap starts here -->
<div id="wrap">				
	<!-- content-wrap starts here -->
	<div id="content-wrap">	
		<div id="mainForm">
			<p>&nbsp;</p>
			<h1 style="text-align: center;">Multi-Strategy Model Portfolios</h1>
			<p>&nbsp;</p>
			        <div id="main">
			<p>&nbsp;</p>
			<h2 style="text-align: center;">Latest Update</h2>
			<hr>
			<p>&nbsp;</p>
			<p><b style="color: #461B7E;">Nov 2, 2009:</b> Since July, most asset classes (other than treasury and some commodities such as agriculture) have had steep run up in the belief that economics has started to recover. Indeed, various indicators including ISM, GDP and housing starts are pointing to the recovery. Some noticeable exceptions include elevated unemployment rate and the next wave of mortgage resets. Risky assets started to have a correction since late October. Cautions should be exercised. </p>
			<p>Year to date,the momentum driven strategies have gone through a whipsaw period and since July, they have shown some positive returns. The performance of <a target="_blank" href="../strategy/View.action?ID=432&action=view">S&P Diversified Trend Indicator (DTI)</a> has been a bit disappointing due to the lack of strong momentum of agricultural commodities. Again, this is expected. Moreover, users are reminded to look at these ETF based portfolios' corresponding mutual fund portfolios for a longer period (<a target="_blank" href="../strategy/View.action?ID=445&action=view">Goldman Sachs' global tactical asset allocation strategy</a> and <a target="_blank" href="../strategy/View.action?ID=478&action=view">Bond Funds Momentum  Based on Upgrading</a> have corresponding mutual fund portfolios while the longer periond's performance of <a target="_blank" href="../strategy/View.action?ID=432&action=view">S&P Diversified Trend Indicator (DTI)</a> could be found from S&P's original paper).</p>
			<p>The simple model portfolios also have had very good runs due to their equity exposure. They were out of treasury bonds at the end of May. </p>
			<p>The lazy model portfolios have rebounded strongly, again, due to their holdings of risky assets. From the tables below, lazy portfolios have outperformed simple portfolios in the trailing one year period. But users are reminded that if the whole 2008 is included, the lazy portfolios still under perform the more active simple portfolios.   </p>
			<p>&nbsp;</p>
			<h2 style="text-align: center;">The Model Portfolio Table</h2>
			<hr>
			<p>&nbsp;</p>
			<style type="text/css">
			.link1 a:link{color:#096199}
			</style>
			<p align="center" style="margin-bottom: 0cm;"><font face="Times New Roman, serif"><font face="Times New Roman, serif"><font size="3" style="font-size: 12pt;"><b>Lazy Model Portfolios</b></font></font></font></p>
			<table bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" width="95%" >
			           <tr bgcolor="#ffffff">
			            <td rowspan="2" align="center" width="20%">
			                  <h4>portfolio</h4>
			              </td>
			               <td colspan="2" align="center" width="20%">
			               <h4>1 year(%)</s:text></h4>
			            </td>
			            <td colspan="2" align="center" width="20%">
			               <h4>3 year(%)</h4>
			            </td>
			            <td colspan="2" align="center" width="20%">
			                  <h4>5 year(%)</h4>
			              </td>
			        </tr>
			           <tr bgcolor="#ffffff">
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			           </tr>
			<#if lazy_portfolios?exists > 
			       <#list lazy_portfolios as portfolio>
			
			              <tr bgcolor="#ffffff" style="font-size: 13px;">
			
			                  <td align="center" width="20%">
			                  
			                     <#if portfolio.name?exists ><a href="../portfolio/Edit.action?ID=${portfolio.ID}&action=view" target="_blank"><div id='shortPortfolioName' title="${portfolio.name}">${portfolio.name}</div></a></#if>
			                     
			                  </td>         
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio1?exists >${portfolio.sharpeRatio1}</#if>
			                     
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR1?exists >${portfolio.AR1}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio3?exists >${portfolio.sharpeRatio3}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR3?exists >${portfolio.AR3}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio5?exists >${portfolio.sharpeRatio5}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR5?exists >${portfolio.AR5}</#if>
			                  </td>
			              </tr>    
			              
			</#list>
			</#if>
			</table>
			<p>&nbsp;</p>
			<style type="text/css">
			.link1 a:link{color:#096199}
			</style>
			<p align="center" style="margin-bottom: 0cm;"><font face="Times New Roman, serif"><font face="Times New Roman, serif"><font size="3" style="font-size: 12pt;"><b>Simple Model Portfolios</b></font></font></font></p>
			<table  bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" width="95%">
			           <tr bgcolor="#ffffff">
			            <td rowspan="2" align="center" width="20%">
			                  <h4>portfolio</h4>
			              </td>
			               <td colspan="2" align="center" width="20%">
			               <h4>1 year(%)</s:text></h4>
			            </td>
			            <td colspan="2" align="center" width="20%">
			               <h4>3 year(%)</h4>
			            </td>
			            <td colspan="2" align="center" width="20%">
			                  <h4>5 year(%)</h4>
			              </td>
			        </tr>
			           <tr bgcolor="#ffffff">
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			           </tr>
			<#if simple_portfolios?exists > 
			       <#list simple_portfolios as portfolio>
			
			              <tr bgcolor="#ffffff" style="font-size: 13px;">
			
			                  <td align="center" width="20%">
			                  
			                     <#if portfolio.name?exists ><a href="../portfolio/Edit.action?ID=${portfolio.ID}&action=view" target="_blank"><div id='shortPortfolioName' title="${portfolio.name}">${portfolio.name}</div></a></#if>
			                     
			                  </td>         
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio1?exists >${portfolio.sharpeRatio1}</#if>
			                     
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR1?exists >${portfolio.AR1}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio3?exists >${portfolio.sharpeRatio3}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR3?exists >${portfolio.AR3}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio5?exists >${portfolio.sharpeRatio5}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR5?exists >${portfolio.AR5}</#if>
			                  </td>
			              </tr>    
			              
			</#list>
			</#if>
			</table>
			<p>&nbsp;</p>
			<style type="text/css">
			.link1 a:link{color:#096199}
			</style>
			<p align="center" style="margin-bottom: 0cm;"><font face="Times New Roman, serif"><font face="Times New Roman, serif"><font size="3" style="font-size: 12pt;"><b>Flexible Model Portfolios</b></font></font></font></p>
			<table bgcolor="#000000" border="0" cellpadding="0" cellspacing="1" width="95%">
			           <tr bgcolor="#ffffff">
			            <td rowspan="2" align="center" width="20%">
			                  <h4>portfolio</h4>
			              </td>
			               <td colspan="2" align="center" width="20%">
			               <h4>1 year(%)</s:text></h4>
			            </td>
			            <td colspan="2" align="center" width="20%">
			               <h4>3 year(%)</h4>
			            </td>
			            <td colspan="2" align="center" width="20%">
			                  <h4>5 year(%)</h4>
			              </td>
			        </tr>
			           <tr bgcolor="#ffffff">
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			              <td align="center" width="10%">
			                  Sharpe
			              </td>
			              <td align="center" width="10%">
			                  AR
			              </td>
			           </tr>
			<#if flexible_portfolios?exists > 
			       <#list flexible_portfolios as portfolio>
			
			              <tr bgcolor="#ffffff" style="font-size: 13px;">
			
			                  <td align="center" width="20%">
			                  
			                     <#if portfolio.name?exists ><a href="../portfolio/Edit.action?ID=${portfolio.ID}&action=view" target="_blank"><div id='shortPortfolioName' title="${portfolio.name}">${portfolio.name}</div></a></#if>
			                     
			                  </td>         
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio1?exists >${portfolio.sharpeRatio1}</#if>
			                     
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR1?exists >${portfolio.AR1}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio3?exists >${portfolio.sharpeRatio3}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR3?exists >${portfolio.AR3}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.sharpeRatio5?exists >${portfolio.sharpeRatio5}</#if>
			                  </td>
			                  <td align="center" width="10%">
			                     <#if portfolio.AR5?exists >${portfolio.AR5}</#if>
			                  </td>
			              </tr>    
			              
			</#list>
			</#if>
			</table>
			</div>  <!-- main end -->
			<div id="rightbar">
			<p>&nbsp;</p>
			<h2 style="text-align: center;">Model Portfolio Overview</h2>
			<hr>
			<p>&nbsp;</p>
			<p>The model portfolios are the showcases of how to utilize multiple strategies to construct a balanced portfolio to achieve superior risk-adjusted returns. Three types of portfolios, conservative, moderate and growth, are provided to accommodate different levels of risk tolerance. Conservative portfolios emphasize the capital preservation while moderate portfolios have a balance view on both capital preservation and capital growth. Growth portfolios are mainly for capital growth with certain degree of tolerance of the portfolio volatility. </p>
			<p>Users, however, could tailor portfolios based on their own risk profiles by adjusting the weights for assets (or strategies) and use ValidFi's platform to perform historical simulation and study the portfolio characteristics. ValidFi's MVO tool could be also used to help decide the weighting. See the MVO tutorial for more details. </p>
			<p>To accommodate different degrees of portfolio management activities, we provide three styles of portfolios: Lazy portfolios are those which simply buy and hold securities (such as mutual funds or ETFs) and do periodical rebalancing (such as annual rebalancing) across assets. These portfolios are really for comparison purpose. Simple portfolios are those which employ simple timing technique such as moving average for the asset classes. These portfolios have shown the benefit of flexibilities other than buy and hold. The third kind, flexible portfolios allow to employ some active portfolio management (meaning more trading activities) to achieve greater return while reducing risk. </p>
			<p>To facilitate trading activities, we construct the model portfolios based on Exchanged-Traded Funds (ETFs). For evaluation purpose, users could construct a corresponding portfolio using mutual funds or market indices instead of ETFs to examine how the portfolios perform in a longer history period.</p>
			<p><b>Lazy Model Portfolios:</b> These portfolios are designed for comparison purpose. They are based on Roger Gibson's <a target="_blank" href="../strategy/View.action?ID=1&action=view">diversified portfolios</a> and David Swensen's <a target="_blank" href="../strategy/View.action?ID=101&action=view">Yale individual portfolios</a>: their asset classes include US equity (US large cap and small cap), foreign equity (and emerging market equity), US real estate, commodities, fixed incomes including government long bond (for deflation hedging purpose), inflation protected bonds (TIPS, for inflation hedging purpose) and foreign bonds (for US dollar hedging purpose) and Cash.
			</p>
			
			<p><b>Simple Model Portfolios:</b> These portfolios are designed for those who prefer minimum trading activities. They are based on Mebane Faber's <a target="_blank" href="../strategy/View.action?ID=305&action=view">simple timing portfolios</a> and Roger Gibson's <a target="_blank" href="../strategy/View.action?ID=1&action=view">diversified portfolios</a>. For each asset class, a 10 month simple moving average timing method is used to guard against market downside volatility. For those who like to venture a bit, the single ETF (such as SPY) representing the asset class could be substituted with a model portfolio for that asset class. For example, SPY representing US equity could be substituted with <a target="_blank" href="../strategy/View.action?ID=161&action=view">Last Year's Best Fund</a> model portfolio to achieve better return. Users are encouraged to try out these using ValidFi's portfolio platform. 
			</p>
			
			<p><b>Flexible Model Portfolios:</b> These portfolios are designed for investors who would like to pursue maximum risk-adjusted returns utilizing various trading techniques. Main strategies employed here include: <a target="_blank" href="../strategy/View.action?ID=445&action=view">Goldman Sachs' global tactical asset allocation strategy</a>, <a target="_blank" href="../strategy/View.action?ID=432&action=view">S&P Diversified Trend Indicator (DTI)</a> and <a target="_blank" href="../strategy/View.action?ID=478&action=view">Bond Funds Momentum  Based on Upgrading</a>. The Goldman Sachs' strategy is basically a momentum strategy over diversified major asset classes. The S&P DTI is another momentum long short strategy over wide range of financial (bonds and currency) and commodities markets. Bond Funds Momentum  Based on Upgrading strategy is based on momentum ranking over various bond ETFs (representing government treasury, corporate bonds (investment grade or high yield junk bond) and foreign bonds. Users are encouraged to further explore other strategies such as the <a target="_blank" href="../strategy/View.action?ID=460&action=view">Global Tactical Asset Allocation (Value)</a> strategy. Even though this strategy did not perform well in the 2008 crash because it entered equity market too early, at the present time (January 2009), the equity market has attractive valuation. Adding some allocation to this strategy complements to the existing momentum based strategies, thus achieving better hedging.
			</p>
			</div> <!-- right bar end -->
		</div> <!-- content wrap end -->
    <div>

</div> <!-- wrap end -->
</body>
</html>
