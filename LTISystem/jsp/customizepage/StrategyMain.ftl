[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="submenu" content="strategymaintable"/>
<meta name="strategies" content="vf_current" />
<meta name='strategydesc' content="vf_current"/>
<meta name="verify-v1" content="5p50UotfJnmCe0OpO/3NS0IcIE4Wi/ktqd6+Z/th2+Y=" />
<title>ValidFi Strategies</title>
<link rel="stylesheet" href="/LTISystem/jsp/images/vf.css" type="text/css" />
</head>


<body>
<div id="vf_wrap">
	<div id="vf_content-wrap">
		<div id="vf_sidebar">
<!--		[@lti.Forumtopicsfeed "Articles"]
                
		[/@lti.Forumtopicsfeed]
-->
                [@lti.Articlesfeed2 "article"]
                [/@lti.Articlesfeed2]
		</div>
		<div id="vf_contentright"style="float:left;">
			
			<div id ="vf_main">
				<h1><span>Top Strategy <span class="orange">Styles</span></span></h1>
	    		<hr width="80%">
	    		<ul>
	    			<li><a target="_top" href="../strategy/TopStyles.action?topNum=0&sortColumn=6&includeHeader=true&title=Top strategy styles table">Top strategy styles table</a></li>
	    			<li><a target="_top" href="../strategy/TopStyles.action?topNum=25&sortColumn=6&includeHeader=true&title=Short Term (Past 1 Year)">Short Term (Past 1 Year)</a>
	    				<ul>
	    					<li>Asset Allocation: Cross-Asset Momentum, Long-Short Trend Following, Value. </li>
	    					<li>Equity: Momentum Hedge, Sharpe Dynamics, Timing </li>
	    					<li>Bonds: High Yield Bond Timing, Upgrading. </li>
	    				</ul>
	    			</li>
	    			<li><a target="_top" href="../strategy/TopStyles.action?topNum=25&sortColumn=10&includeHeader=true&title=Intermediate or Long Term (3 Years or Longer)">Intermediate or Long Term (3 Years or Longer)</a>
	    				<ul> 
	    					<li>Asset Allocation: Cross-Asset Momentum, Diversified Timing, Guru Allocation Clone. </li>
	    					<li>Equity: Sharpe Dynamics, Momentum Hedging,Country Rotation</li>
	    					<li>Bonds: High Yield Bond Timing, Upgrading. </li>
	    				</ul>
	    			</li>
	    		</ul>
			</div>
			<div id="vf_rightbar">
	    		<h1>Top Strategy <span class="orange">Classes</span></h1>
	            <hr width="80%">
	            <ul>
	            	<li><span><a target="_top" href="../ajax/${HomeURL}HomePageTable&includeHeader=true">Top strategies in each strategy class</a></span></li>  
	            	<li><span><a target="_top" href="../strategy/Main.action">All strategies in each strategy class</a></span></li>  
	            	<li><span>Strategy Classes</span>
	            		<ul>
	            			<li><span><a target="_top" href="../ajax/${HomeURL}StrategyClass3&includeHeader=true">Asset allocation strategies:</a> diversification and risk control.<span></li>
	            			<li><span><a target="_top" href="../ajax/${HomeURL}StrategyClass4&includeHeader=true">Rebalancing strategies:</a> get back on track while taking opportunities.</span></li>
	            			<li><span><a target="_top" href="../ajax/${HomeURL}StrategyClass5&includeHeader=true">Cash flow strategies:</a> retirement withdrawal methods to cope with market's up and down.</span></li>
	            			<li><span>Major asset strategies: <a target="_top" href="../ajax/${HomeURL}StrategyClass9&includeHeader=true">Equity (stock)</a> and <a target="_top" href="../ajax/${HomeURL}StrategyClass10&includeHeader=true">Fixed income(bond)</a>.</span></li>   
	            			<li><span>Alternative asset strategies: <a target="_top" href="../ajax/${HomeURL}StrategyClass13&includeHeader=true">Real Estate</a>, <a target="_top" href="../ajax/${HomeURL}StrategyClass14&includeHeader=true">Commodities</a> and <a target="_top" href="../ajax/${HomeURL}StrategyClass15&includeHeader=true">Hedges</a>.</span></li>   
	            		</ul>
	            	</li>                   
	            	<li><span><span>Closed end funds strategies</span></li>
	            </ul>
			</div>
			<div class="clear">&nbsp;&nbsp;&nbsp;</div>
			<p>&nbsp;&nbsp;</p>
	</div>
	</div>
</div>
</body>
</html>
