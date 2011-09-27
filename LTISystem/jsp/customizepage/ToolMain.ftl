[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="verify-v1" content="5p50UotfJnmCe0OpO/3NS0IcIE4Wi/ktqd6+Z/th2+Y=" />
<meta name="tools" content="vf_current" />
<meta name="submenu" content="tools"/>

<title>ValidFi Comprehensive Financial Strategies, Quantitative Platform, Strategy-Based Fund Analysis</title>
<link rel="stylesheet" href="/LTISystem/jsp/images/vf.css" type="text/css" />
</head>



<body>
<div id="vf_wrap">
	<div id="vf_content-wrap">
		<div id="vf_contentright">
			<h1 align="center"><span class="green">ValidFi<sup style="color:red"><i>Beta</i></sup>:</span> Apply Advanced Tools to Portfolio Construction</h1>        
			<p>&nbsp;</p>
			<hr/>
			<div class="clear">&nbsp;&nbsp;&nbsp;</div>
			<p>&nbsp;</p>
			<p>Carefully understanding your risk tolerance and investment needs is the foremost important step before you take the plunge into any investment actions. ValidFi strongly recommends performing at least the following basic planning steps. 
			</p>
			<h3><span style="color:#800517;"><strong><i>Consult your financial advisers for a complete assessment!</i></strong></span></h3>
			
			<div id ="vf_main"style="width:99%">
				<table border="0" width="100%" cellpadding="10">
	    		<tr>
	
	    		<td width="50%" valign="top">
	    		<h2>Basic Financial Planning Steps</h2>
	    		<ol>
	    		<li><strong>Risk profile:</strong> key factors include
	    		<ul>
	    		<li>Number of years to retirement: the closer you are to the retirement age, the more conservative you should be.</li>
	    		<li>Your employment situation.</li>
	    		<li>Your health ...</li>
	    		</ul>
	    		<p>For example, the following is a guideline to choose ValidFi's MVO risk aversion categories based on number of years to retirement.</p>
	    		<ul>
	    		  <li>Very Conservative: if you are in retirement</li>
	    		  <li>Conservative: if you are 5-10 years to retirement</li>
	    		  <li>Moderate: if you are 10-15 years to retirement</li>
	    		  <li>Aggressive: if you are 15-20 years to retirement</li>
	    		  <li>Very aggressive: if you are more than 20 years to retirement</li>
	    		</ul>
	    		</li>
	    		<li><strong>Your Investment Needs:</strong> 
	    		<ul>
	    		   <li>Cash Flow:
	    		    <ul>
	    		     <li>regular income: salary</li>
	    		     <li>irregular income: inheritance</li>
	    		     <li>regular expense: mortgage payment, health care cost, average household spending</li>
	    		     <li>irregular expense: college education expenses, big item spending (ex. house and car purchase)</li>
	    		    </ul>
	    		   <li>Estate planning</li>
	    		</ul>
	    		<p>A rule of thumb is to set aside cash or cash equivalent for at least 3 to 6 years expenses. </p>
	    		</ul>
	    		</td>
	
	    		<td width="50%" valign="top">
	    		<h2>Constructing Investment Portfolios</h2>
	    		<p>With the risk profile and investment needs understood, you could construct the investment portfolio accordingly in the following ways.</p>
	    		<ul>
	    		<li><strong>Manual Construction</strong>
	    		   <ol>
	    		    <li>Pick from ValidFi's asset allocation portfolios based on your risk profile. Compare the standard deviation of the portfolio with that of a spectrum of index funds including short term bond funds, general bond funds, long term bond funds, conservative asset allocation funds, moderate asset allocation (or balance) funds and general equity funds. That will give you a good feel on the risk. Also, pay special attention to maximum drawdown and Sharpe ratio. </li>
	    		    <li>Adjust allocation percentages to fit your need: you might not be able to find a perfect fit between your risk/return profile and the strategy model portfolios you select. You could further tweak the allocation percentages (if applicable) to reduce or increase the risk/return. </li>
	    		    <li>Further fine tune the portfolios by substituting individual asset index funds with some corresponding strategies on those assets. For example, if you pick <a target="_top" href="../portfolio/Edit.action?ID=3202">P Diversified Timing On Endowment Asset Allocation Model SMA 10 Months</a>, you could substitute US Equity index fund VFINX with a model portfolio from US Equity strategies such as <a target="_top" href="../strategy/View.action?ID=578&action=view">FundX Upgrading</a>.</li>
	    		    <li>Construct the modified portfolio in ValidFi and perform further historical simulation to study the portfolio's return and risk characteristics. </li>
	    		  </ol>
	    		</li>
	    		<li><strong>Multiple Asset Portfolios Using MVO:</strong> See below for detailed instructions. </li>
	    		<li><strong>Multiple Strategies Portfolios Using MVO:</strong> See below for the detailed instruction.</li> 
	    		</ul>
	    		</td>
	
	    		</tr>
	    		</table>
			</div>
			<div class="clear">&nbsp;&nbsp;&nbsp;</div>	
			<table border="0" width="100%" cellpadding="10">
			<div id="vf_main">
				<h1>Multiple Asset Portfolio Construction</h1>
				<hr width="80%">
				<p>ValidFi's MVO (Mean Variance Optimization) tool is based on modern portfolio theory and finds optimal allocation percentages among the selected underlying asset indices to achieve the maximum return under the constraint of a chosen risk aversion parameter. The key inputs to MVO are as follows</p>
				<ul>
					<li><strong>Risk aversion factor:</strong> this ranges from 0 to 10 with the bigger the factor, the more conservative (risk averse) it is. ValidFi has designated a set of values to represent risk profile as stated above.</li>
					<li><strong>Underlying asset securities:</strong> these are securities (such as index funds, ETFs or mutual funds) representing the underlying assets. The so called 'free lunch', diversification effect, critically relies on the (un)correlation among these securities. Ideally, one would like to choose asset securities which are as much uncorrelated as possible while retaining the desired expected returns. </li>
					<li><strong>Correlations among asset securities:</strong> these are <i>automatically</i> estimated based on the given period (Start, end Date and Time Unit parameters). Current MVO version does not allow users to manually alter or input the correlation coefficients.</li>
					<li><strong>Expected annualized returns for asset securities:</strong> these could be derived automatically based on the given past period or could be entered manually. </li>
					<li><strong>Lower and upper limits of asset percentages:</strong> often, it is desirable to set limits to a particular asset exposure. For example, the cash exposure lower limit could be set for the cash set aside for the near term usage, as mentioned in the planning steps. </li>
				</ul>
				<p>It should be noted that care should be taken for the correlation coefficients and the estimated expected returns. The great 2008 debacle reveals that during a severe market stress, nearly all of assets which are considered uncorrelated suddenly decline in a synchronous fashion. Fortunately, now that this period is part of history, using past estimation for the correlation, as long as this period is included, should yield more objective and conservative result. On the other hand, it also indicates just merely holding long securities, no matter how diversified they are, has very limited effect to reduce risks. This is the main reason why multiple strategies portfolios are advocated here. </p>
				<p>For expected returns in the future, again, merely using past performance yields poor result. A reasonable estimate for each asset class should be given. For example, on 7/15/2009, as S&P 500 was at 900 level, one could expect a reasonable 8-10% annual returns for the coming years. However, if past performance were used, a 5 or 10 year performance of the US equity would be a negative return, that would result in a none or very little equity exposure from MVO. </p>
				<p><strong>Black-Litterman Asset Allocation:</strong> the major benefit of this tool is to allow users to input expected returns in various form while reducing the sensitivity (or non intuitive, sudden dramatic change of allocation percentages) of the outputs. Using this tool requires advanced knowledge and is not recommended for average users. </p>
			</div>
			<div id="vf_rightbar">
				<h1>Multiple Strategy Portfolio Construction</h1>
	            <hr width="80%">
	            <p>The 2008 markets taught us a valuable lesson that just merely holding once considered 'diversified' assets in a portfolio is not sufficient or at least very difficult to reduce the systemic risk: during the 2008 market stress, almost all of assets (other than the US treasuries and CASH) were suddenly correlated and declined simultaneously. Looking ahead, it will take years and years for the (global) economic to muddle through. It is thus very important to diversify not only along the asset dimension, but also along the investment strategy dimension. A buy and hold long asset portfolio might not be the most effective way to reduce systemic risk. </p>
	            <p>In ValidFi, portfolios are just treated as securities: portfolios are represented by their symbols. For example, the symbol for <a target="_top" href="../portfolio/Edit.action?ID=3202">P Diversified Timing On Endowment Asset Allocation Model SMA 10 Months</a> could be found on its portfolio's Current Portfolio page, which is P_3202. Thus it is possible to apply MVO to derive optimal or good allocation among selected portfolios with various strategies. </p>
	            <p>To use MVO to construct a multiple strategy portfolio, users could input portfolios as some or all of the underlying asset securities. If the asset securities consist of some traditional mutual funds, ETFs and some portfolios, a mixed portfolio is derived.</p>
	            <p>The key issue in constructing such a portfolio is the portfolios (or strategies) selection. Intuitive understanding of how selected strategies behave with respect to each is extremely important. The following are some guidelines to select strategies.</p>
	            <ul>
	            	<li><strong>Underlying asset classes:</strong> Choose strategies in various diversified asset classes instead of choosing too many strategies in a single or duplicate asset class(es). For example, if you select a equity based strategy already, try to choose other strategies in fixed income and/or commodities or other asset classes. This makes intuitive sense as strategies in various asset classes tend to be less related. Pay special attention to the correlation between an asset allocation strategy and individual asset based strategy as during certain period, an asset allocation strategy might over weigh certain assets such as equities. Such a strategy, if mixed with an equity long only strategy, might result in unintended effect of too much equity exposure. </li>
	            	<li><strong>Various strategy styles:</strong> Choose different styles of strategies, avoid choosing similar styles. For example, a momentum based strategy tends to do well in some period of time but not well in other periods. In fact, an equity based momentum strategy did very poorly in year 2005 but did a super job in year 2008 (for a long short momentum strategy). If such a momentum strategy is chosen, it makes sense to mix it with other strategy styles such as a value based or a long short hedge strategy, which tend to behave differently from a momentum one.</li>
	           
	            </ul>
			</div>
				<div class="clear">&nbsp;&nbsp;&nbsp;</div>	
				</table>
	</div>
	</div>
</div>
</body>
</html>