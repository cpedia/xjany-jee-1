<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Help</title>
<link rel="stylesheet" href="/LTISystem/jsp/images/vf.css" type="text/css" />
</head>

<body>
<!-- wrap starts here -->
<div id="wrap">
<div id="content-wrap">				
	<!-- content-wrap starts here -->
	<div id="content-wrap">	
<p>&nbsp;</p>
<h1 style="text-align: center;">Frequently Asked Questions (FAQs)</h1>
<p>&nbsp;</p>

<ul>
<li><a href="#missions">What are the missions of ValidFi.com?</a></li>
<li><a href="#differ">How does ValidFi differ from other trading system development tools and investment advisories? </a></li>
<li><a href="#services">What are the services? Is ValidFi free of charge?</a></li>
<li><a href="#strategypolicy">Does ValidFi recommend the strategies collected? Does ValidFi only collect 'good' strategies?</a></li>
<li><a href="#strategy">How does a strategy look like?</a></li>

<li><a href="#index">Is a strategy similar to an index?</a></li>

<li><a href="#portfolio">How does a portfolio look like?</a></li>
<li><a href="#modelportfolios">How to use the model portfolios?</a></li>
<li><a href="#superportfolio">How to combine several portfolios to create a new portfolio or a strategy?</a></li>
<li><a href="#strategycreate">Can I create my own strategy?</a></li>
<li><a href="#MVO">How do I use MVO (Mean Variance Optimization) for (multiple strategy) portfolio optimization?</a></li>
<li><a href="#RAA">How do I use RAA (Real time Asset Allocation Analyzer)?</a></li>
<li><a href="#screening">How do I use screening tool?</a></li>
</ul>
<p>&nbsp;</p>
<p><h3><a name="missions">What are the missions of ValidFi.com?</a></h3></p>

<p>ValidFi's missions are to provide
   <ul>
   <li>a comprehensive knowledge base for financial planning and investment strategies published by books, magazines and academic papers. ValidFi also provides proprietary strategies. Through live monitoring and historical performance data of strategies provided by ValidFi.com, investors are able to navigate through tumultuous market conditions and noisy hearsay with clear, objective and up to date information.</li>
   <li>investment strategies to follow great Guru investors on their Beta exposure (asset allocation) and Alpha (security selection) through FundAlpha and FundBeta Clones.</li>
   <li>precise guidelines and easy to use tools to allow users to evaluate and construct suitable portfolios with diverse time tested strategies in a wide risk spectrum. </li>
   <li>fund analysis based on strategy centric information such as effective beta and style gains to gain better insights into mutual funds, ETFs, closed end funds.</li>
   <li>an easy to use platform for users to monitor and study strategies and research their effects in a portfolio. Advanced portfolio construction and management tools are offered.</li>
   </ul>
</p>
<p>For more information, please read the <a target="_parents" href="${HomeURL}Guide&&includeHeader=true">guide</a>. </p>

<p><h3><a name="differ">How does Validfi differ from other trading system development tools and investment advisories? </a></h3></p>

<p>ValidFi differs from other stock, ETF and other financial instrument trading tools in the following:
    <ul>
    <li>ValidFi emphasizes on overall financial planning (such as cash flow strategies) and portfolio management (such as asset allocation) strategies. It offers well known planning, risk management and portfolio management (or so called money/position management) techniques employed by institutional investors.</li>
    <li>ValidFi offers both technical trading and fundamental based investment strategies. It has and will continue to enrich many economic indicators such as CPIs, PMIs, GDPs etc.</li>
    </ul>
</p>
<p>Compared with investment advisories, ValidFi offers the following services and features:
    <ol>
    <li>Easy to follow Model Portfolios as the first stepping stone. </li>
    <li>A platform to create users' own portfolios consisting of portfolios/securities  from the strategies/portfolios provided by ValidFi and other users. Advanced portfolio statistics are provided to study these portfolios/strategies.</li>
    <li>Advanced analytical tools such as RAA (Realtime Asset Allocation Analyzer), MVO (Mean Variance Optimization) and Black-Litterman's asset allocation tools and many others. </li> 
    <li>Mutual funds and other securities risk and performance analytics. </li>
    </ol>
</p>

<p><h3><a name="services">What are the services? Is ValidFi free of charge?</a></h3></p>

<p>ValidFi is in beta stage right now. So everything is free. Some of the services will become premium services in the near future. However, we will offer substantial saving for the early registered users for the future premium services, so do take advantage of this opportunity and register. </p>

<p>ValidFi provides the following services:
<ol>
<li>Basic financial planning and investment strategies, fund screening tools and discussion forums.</li>
<li>Multi-strategy model portfolios, ValidFi portfolio construction and simulation platform and real time (end of day) strategy/portfolio monitoring with trading signals. </li>
<li>Tools for portfolio optimization, real time asset allocation analysis for mutual funds and advanced screening tools for mutual funds and closed end funds.</li>
<li>Innovative strategy-based fund analytics. </li>
<li>Strategy code development. </li>
</ol>
</p>

<p><h3><a name="strategypolicy">Does ValidFi recommend the strategies collected? Does ValidFi only collect 'good' strategies?</a></h3></p>
<p>ValidFi does not recommend any strategy it collects. ValidFi collects both 'good' and 'bad' strategies. ValidFi's goal is to collect many popular strategies, monitor those strategies and provide novel tools to pinpoint their potential risks and inherent behavior. We view it is extremely valuable to enable investors to validate the 'bad' performance or risk of a strategy suggested by some 'experts' or media so that they could avoid such a strategy. </p>

<p><h3><a name="strategy">How does a strategy look like?  </a></h3></p>

<p>In ValidFi, a strategy consists of an overview, a list of model portfolios and strategy code. The strategy is essentially a timed Java program. A STATIC strategy is just a no op program. The strategy overview describes the strategy and lists relevant references A strategy may have parameters. For example, a moving average based trading strategy has a length parameter to allow input various intervals of a moving average. It might have another parameter which describes the underlying security used for the moving average calculation. Multiple model portfolios for a strategy could be created to allow various combinations of parameters and different starting dates (inception dates) of a portfolio study etc. Users are encourage to browse strategies in the strategy page. Strategy code section allows developers to create, modify and compile the code used in the strategy. Strategy code development will be offered as a premium feature in the future. </p>

<p><h3><a name="portfolio">How does a portfolio look like?</a></h3></p>

<p>ValidFi's portfolio consists of two parts: the initial portfolio setup on an initial portfolio page. The setup consists of initial portfolio positions in various asset classes and the strategies assigned at the portfolio level and asset level. See <a target="_parents" href="${HomeURL}PortfolioOverviewDemo"> portfolio demo.</a></p>

<p><h3><a name="modelportfolios">How to use the model portfolios?</a></h3></p>

<p>See <a target="_parents" href="${HomeURL}ModelPortfolios&includeHeader=true">model portfolio page</a> for an explanation. </p>

<p><h3><a name="superportfolio">How to combine several portfolios to create a new portfolio or a strategy?</a></h3></p>

<p>This could be best explained by looking at the model portfolio ValidFi Flexible Growth ETF. In this portfolio, three model portfolios <a target="_blank" href="../portfolio/Edit.action?ID=3234&action=view">P GS PURE FundX ETF</a>, <a target="_blank" href="../portfolio/Edit.action?ID=3212&action=view">P S and P Diversified Trend Indicators</a> and <a target="_blank" href="../portfolio/Edit.action?ID=3684&action=view">P Bond ETF Momentum</a> are used as "securities". The <a target="_blank" href="${HomeURL}SuperPortfolioDemo"> demo here</a> best describes the process of creating and working on this so called super portfolio. </p>

<p><h3><a name="strategycreate">Can I create my own strategy?</a></h3></p>

<p>For now, users can create a strategy without strategy code. The strategy could have several model portfolios which use various portfolio level or asset level strategies provided by ValidFi. We are planning to offer strategy code development in the near future. </p>

<p><h3><a name="MVO">How do I use MVO (Mean Variance Optimization) for (multiple strategy) portfolio optimization? </a></h3></p>
<p>This demo shows how to use MVO to allocate weights to "assets" (which are really strategies' model portfolios). </p>

<p><h3><a name="RAA">How do I use RAA (Real time Asset Allocation Analyzer)?</a></h3></p>

<p><h3><a name="screening">How do I use screening tool? </a></h3></p>

<!-- content wrap -->
</div>


<!-- wrap ends here -->
</div>

</body>
</html>