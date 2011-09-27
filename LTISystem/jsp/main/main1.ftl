[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<html>
<head>
<title>ValidFi: Strategies and Analytics</title>
<meta name="home" content="vf_current" />
</head>
<body>
<div id="vf_wrap">
	<div id="vf_content-wrap">
<!--          <p align="center"><font size="3"><em>Announcement: we are fixing a bug for password reset. If you encountered a problem and need immediate access, please send an email to support@validfi.com. Thank you for your patience!</em></font></p>
-->
<!--       <p align="center"><a title="Register an account"  href="/LTISystem/jsp/register/openRegister.action"><img width="880px" height="260px" src="/LTISystem/UserFiles/Image/HomePicHolidayP.jpg" border=none></a></p>
-->

            <div width="100%">
            <div class="vf_main" style="width:69%; height:360px;">
             <p align="left"><img width="350px" height="83px" src="/LTISystem/UserFiles/Image/AsSimpleAs.jpg" border=none></p>
            <p align="left" style="padding:1px; margin:1px;"><a title="Find Strategy"  href="/LTISystem/jsp/strategy/Main.action?group=public"><img width="600px" height="83px" src="/LTISystem/UserFiles/Image/StrategyButton.jpg" border=none></a></p>
            <p align="left" style="padding:1px; margin:1px;"><a title="Customize Portfolio"  href="/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=4406"><img width="600px" height="83px" src="/LTISystem/UserFiles/Image/PortfolioButton.jpg" border=none></a></p>
            <p align="left" style="padding:1px; margin:1px;"><a title="Email Alert"  href="/LTISystem/jsp/portfolio/Edit.action?ID=4407"><img width="600px" height="83px" src="/LTISystem/UserFiles/Image/EmailAlertButton.jpg" border=none></a></p>
            </div>
            <div class="vf_rightbar" style="width:29%; height:360px;">
             <p>&nbsp;</p>
             <p align="right"><img width="200px" height="280px" src="/LTISystem/UserFiles/Image/HappyHolidayPen.jpg" border=none></p>
            </div>
            </div> 

		<div id="vf_sidebar">
			<div class="vf_blockleft">
            	[@lti.Articlesfeed2 "article"]

		[/@lti.Articlesfeed2]
             </div> <!-- end of blockleft -->
             <div class="vf_blockleft">					
			 	<h1>Wise Words</h1>
                <p>Then He said to them, "Beware, and be on your guard against every form of greed; for not even when one has an abundance does his life consist of his possessions."</p>
				<p class="align-right">- Luke 12:15</p>					
                <p>&quot;You only find out who is swimming naked when the tide goes out.&quot;</p>
		    	<p class="align-right">- Warren Buffett</p>
                <p>&quot;I don't look to jump over 7-foot bars: I look around for 1-foot bars that I can step over.&quot;</p>
				<p class="align-right">- Warren Buffett</p>					
				<p>&quot;...if one tortures a dataset long enough, it will confess to anything!&quot;</p>
				<p class="align-right">- Andrew Lo</p>					
				<p>&quot;...a strategy is not a strategy if one keeps changing it.&quot;</p>
				<p class="align-right">- John P. Hussman</p>
               <p>&quot;Bulls make money, Bears make money, Pigs get slaughtered.&quot;</p>
				<p class="align-right">- Wall Street Saying</p>		
              </div> <!-- end of blockleft -->
              
		</div>
		<div id="vf_contentright"style="float:left;margin-left:3px;">
			<div id="vf_main">	
     	       <div class="vf_blockmain">
                	<a target="_top" href="../customizepage/StrategyMain.action"><h1><span>Research<span class="orange"> Strategies</span></span></h1></a>
					<ul>
    					<li><a target="_top" href="../strategy/TopStyles.action?topNum=25&sortColumn=10&title=Time Tested Top Strategies">Time Tested Top Strategies</a>
    						<ul>
    							<li><a target="_top" href="../strategy/View.action?ID=445&action=view">Goldman Sachs Tactical Asset Allocation</a></li>
    							<li><a target="_top" href="../strategy/View.action?ID=323&action=view">Sharpe Dynamics For Equity</a></li>
    							<li><a target="_top" href="../strategy/View.action?ID=187&action=view">High Yield Bond Timing</a></li>
    							<li> ... and many more ... </li>
    							</ul>
    					 </li>
    				  	 <li><a target="_top" href="../strategy/TopStyles.action?topNum=0&sortColumn=8&title=Diverse Styles">Diverse Styles</a>
          					 <ul>
            					<li>Momentum, Value, Timing, Sharpe/Alpha Dynamics, Hedge, ...</li>
            					<li>Fund Cloning</li>
        			  	     </ul>
   					   	 </li>
    					 <li><a target="_top" href="../strategy/Main.action">Over Diverse Assets</a>
       				     	<ul>
           						<li><a target="_top" href="../customizepage/StrategyClass3.action">Asset Allocation</a></li>
           						<li><a target="_top" href="../customizepage/StrategyClass9.action">Stocks</a>,
          						 <a target="_top" href="../customizepage/StrategyClass10.action">Bonds</a>,
        					     <a target="_top" href="../customizepage/StrategyClass14.action">Commodities</a>, ...</li>
          					</ul>
    					</li>
					</ul>
			<p>&nbsp;</p>
		</div>
        <div class="vf_blockmain">
	    	<a target="_top" href="../portfolio/Main.action"><h1>Manage      <span class="orange"> Portfolios</span></h1></a>
             <ul>
             	<li><a target="_top" href="../customizepage/ModelPortfolios.action">Easy to Follow Model Portfolios</a>
                 	<ul>
                 		<li>Conservative, Moderate and Growth</li>
                  	</ul>
                </li>
                  <li><a target="_top" href="../customizepage/PortfolioOverviewDemo.action?includeHeader=false"target="_blank">Versatile Portfolio Management</a>
                  	<ul>
                   	 <li>Historical Simulation and Live Monitoring </li>
                         <li>Transaction Email Notification</li>
                         <li>Returns/Risk Metrics and Flash Charts</li>
                         <li>Multiple Strategies with Parameters</li>
                         <li>Composite Portfolios</li>
                  	 </ul>
                   </li>
              </ul>
			<p>&nbsp;</p>
			
		</div>	<!-- end of vf_blockmain -->
            <div class="vf_blockmain">  
        	<a target="_top" href="../customizepage/ToolMain.action"><h1>Apply <span class="orange">Advanced Tools</span></h1></a>
            <ul>
         	   <li>Find Out Your Risk Profile
            	<li>Construct a Portfolio with Diversified Assets
            <ul>
           		 <li>
                 <li>Mean Variance Optimization (Efficient Frontier)</li>
                 <li>Black-Litterman</li>
         		   </ul>
          	    </li>
                <li>Construct a Portfolio with Multiple Strategies
             		<ul>
                		<li>Correlations of Strategies</li>
                      	<li>Mean Variance Optimization</li>
                    </ul>
                 </li>
             </ul>
			<p>&nbsp;</p>
	    </div> <!-- end of vf_blockmain -->
	</div>	<!-- end of vf_main -->
	<div id="vf_rightbar">
    	<div class="vf_blockright">
                <a target="_top" href="../fundcenter/SMoney.action"><h1>Follow <span class="orange">the Smart Money</span></h1></a>
                <p><a target="_top", href="../fundcenter/SMoneyIndicators.action">Smart Money Indicators</a></p>
                     <ul>
                      <li>Weekly Pro Money Asset Allocation Trends</li>
                      <li>Weekly Smart Money Asset Allocation Trends</li>
                     </ul>
		<p>&nbsp;</p>
                <p><a target="_top", href="../fundcenter/SMoney.action">Follow Gurus in Asset Allocation</a></p>
                <ul>
                      <li>Domestic US Equity and Fixed Income Asset Allocation Gurus:
                          <ul>
                              <li><h3>Hussman Strategic Growth (<a href="../fundcenter/View.action?symbol=HSGFX">HSGFX</a>)</h3></li>
                              <li><h3>Vanguard Asset Allocation (<a href="../fundcenter/View.action?symbol=VAAPX">VAAPX</a>)</h3></li>
                              <li><h3>FPA Crescent (<a href="../fundcenter/View.action?symbol=FPACX">FPACX</a>)</h3></li>
                              <li><h3>Greenspring Fund (<a href="../fundcenter/View.action?symbol=GRSPX">GRSPX</a>)</h3></li>
                              <li><h3>Leuthold Core Investment (<a href="../fundcenter/View.action?symbol=LCORX">LCORX</a>)</h3></li>
                              <li>... More ...</li>
                         </ul>
                      </li>
                     <li>US Equity, Foreign Equity, US Fixed Income and Foreign Fixed Income World Allocation Gurus:
                         <ul>
                             <li><h3>GMO Benchmark-Free Asset Allocation (<a href="../fundcenter/View.action?symbol=GBMFX">GBMFX</a>)</h3></li>
                             <li><h3>PIMCO All Asset (<a href="../fundcenter/View.action?symbol=PASDX">PASDX</a>)</h3></li>
                             <li><h3>PIMCO Global Multi-Asset (<a href="../fundcenter/View.action?symbol=PGMAX">PGMAX</a>)</h3></li>
                             <li><h3>Ivy Asset Strategy (<a href="../fundcenter/View.action?symbol=WASYX">WASYX</a>)</h3></li>
                             <li>... More ...</li>
                         </ul>
                     </li>
                     <li>Treasury, Corporate Investment Grade, High Yield Bond Allocation Gurus:
                         <ul>
                          <li><h3>PIMCO Total Return (<a href="../fundcenter/View.action?symbol=PTTRX">PTTRX</a>)</h3></li>
                          <li><h3>Loomis Sayles Bond Fund (<a href="../fundcenter/View.action?symbol=LSBDX">LSBDX</a>)</h3></li>
                         </ul>
                     </li>
                </ul>

	</div> <!-- end of vf_blockright -->
        <div class="vf_blockright">
                <a target="_top" href="../fundcenter/main.action?includeHeader=true"><h1>Study <span class="orange">Funds, ETFs and CEFs</span></h1></a>
		<ul>
         		<li>Total Returns, Risk Metrics Tables, Charts and Comparisons:
                	<form target="_top" action="/LTISystem/jsp/security/Quote.action">
	 					<input type="text" id="vf_quote-search-field" name="symbol">
	 					<input type="submit" value="Quote">
 					</form>
         		</li>
                        <li>Historical and Current Asset Allocation Trends and Ranking</li>
         		<li><a target="_top", href="../security/ScreeningMain.action">Fund Screening including Closed End Funds</a></li>
		</ul>
		<p>&nbsp;</p>
        </div> <!-- end of vf_blockright -->
	</div>			
	</div>
</div>
</body>
</html>
