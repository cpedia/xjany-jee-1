<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Customize Page</title>
<style>
/********************************************
   HTML ELEMENTS
********************************************/ 

/* top elements */
* { padding: 0; margin: 0; }

body {
	margin: 0;
	padding: 0;
	font: .8em/1.5em  Verdana, Tahoma, Helvetica, sans-serif;
	color: #666666; 
	background: #A9BAC3 url(tableft.gif) repeat-x;
	text-align: center;
}
/* box with rounded curve */
.bl {background:  0 100% no-repeat #B5EAAA;}
.bl2 {background:  0 100% no-repeat #ADDFFF;}
.br {background:  100% 100% no-repeat}
.tl {background:  0 0 no-repeat}
.tr {background:  100% 0 no-repeat; padding:10px}
.clear {font-size: 1px; height: 1px} 

/* links */
a { 
/*	color: #4284B0; */
        color: #2B60DE;
	background-color: inherit;
	text-decoration: none;
}
a:hover {
	color: #9EC068;
	background-color: inherit;
}

/* headers */
h1, h2, h3 {
/*	font: bold 1em 'Trebuchet MS', Arial, Sans-serif; */
       font: bold 1em Arial, Helvetica, sans-serif; 
	color: #333;	
}
h1_box { font: bold; font-size: 1.8em; color: #6297BC; } 
box1 {height: 100%; background: #B5EAAA repeat-x;}
h1 { font: bold; font-size: 1.7em; color: #461B7E;}
h2 { font-size: 1.4em; text-transform:uppercase;}
h3 { font-size: 1.3em; }

p, h1, h2, h3 {
	margin: 10px 15px;
}
ul, ol {
	margin: 10px 30px;
	padding: 0 15px;
	color: #4284B0; 
        color: #000000;
}
ul span, ol span {
	color: #666666; 
}

/* images */
img {
	border: 2px solid #CCC;
}
img.no-border {
	border: none;
}
img.float-right {
  margin: 5px 0px 5px 15px;  
}
img.float-left {
  margin: 5px 15px 5px 0px;
}
a img {  
  border: 2px solid #568EB6;
}
a:hover img {  
  border: 2px solid #CCC !important; /* IE fix*/
  border: 2px solid #568EB6;
}

code {
  margin: 5px 0;
  padding: 10px;
  text-align: left;
  display: block;
  overflow: auto;  
  font: 500 1em/1.5em 'Lucida Console', 'courier new', monospace;
  /* white-space: pre; */
  background: #FAFAFA;
  border: 1px solid #f2f2f2;  
  border-left: 4px solid #4284B0; 
}
acronym {
  cursor: help;
  border-bottom: 1px solid #777;
}
blockquote {
	margin: 15px;
 	padding: 0 0 0 20px;  	
  	background: #FAFAFA;
	border: 1px solid #f2f2f2; 
	border-left: 4px solid #4284B0;   
	color: #4284B0;
	font: bold 1.2em/1.5em Georgia, 'Bookman Old Style', Serif; 
}

/* form elements */
form {
	margin:10px; padding: 0;
	border: 1px solid #f2f2f2; 
	background-color: #FAFAFA; 
}
label {
	display:block;
	font-weight:bold;
	margin:5px 0;
}
input {
	padding: 2px;
	border:1px solid #eee;
	font: normal 1em Verdana, sans-serif;
	color:#777;
}
textarea {
	width:300px;
	padding:2px;
	font: normal 1em Verdana, sans-serif;
	border:1px solid #eee;
	height:100px;
	display:block;
	color:#777;
}
input.button { 
	margin: 0; 
	font: bold 1em Arial, Sans-serif; 
	border: 1px solid #CCC;
	background: #FFF; 
	padding: 2px 3px; 
	color: #4284B0;	
}

/* search form */
form.searchform {
	background: transparent;
	border: none;
	margin: 0; padding: 0;
}
form.searchform input.textbox { 
	margin: 0; 
	width: 120px;
	border: 1px solid #9EC630; 
	background: #FFF;
	color: #333; 
	height: 14px;
	vertical-align: top;
}
form.searchform input.button { 
	margin: 0; 
	padding: 2px 3px; 
	font: bold 12px Arial, Sans-serif; 
	background: #FAFAFA;
	border: 1px solid #f2f2f2;
	color: #777;	
	width: 60px;
	vertical-align: top;
}

/***********************
	  LAYOUT
************************/
#wrap {
	background: #FFF;
	width: 900px; height: 100%;
	margin: 0 auto;	
	text-align: left;
}
#content-wrap {
	clear: both;
	margin: 0; padding: 0;	
	background: #FFF;
}

/* header */
#header {
	position: relative;
	height: 85px;	
	background: #000 url(headerbg.gif) repeat-x 0% 100%;	
}
#header h1#logo {
	position: absolute;
	margin: 0; padding: 0;
	font: bolder 4.1em 'Trebuchet MS', Arial, Sans-serif;
	letter-spacing: -2px;
	text-transform: lowercase;
	top: 0; left: 5px;	
}
#header h2#slogan {
	position: absolute;	 
	top:37px; left: 95px;
	color: #666666;
	text-indent: 0px;
	font: bold 11px Tahoma, 'trebuchet MS', Sans-serif; 
	text-transform: none;	
}
#header form.searchform {
	position: absolute;
	top: 0; right: -12px;	
}

/* main */
#main {
	float: left;
	margin-left: 15px;
	padding: 0;
	width: 48%;		
}

.post-footer {
	background-color: #FAFAFA;
	padding: 5px; margin: 20px 15px 0 15px;
	border: 1px solid #f2f2f2;
	font-size: 95%;	
}
.post-footer .date {
	background: url(clock.gif) no-repeat left center;
	padding-left: 20px; margin: 0 10px 0 5px;
}
.post-footer .comments {
	background: url(comment.gif) no-repeat left center;
	padding-left: 20px; margin: 0 10px 0 5px;
}
.post-footer .readmore {
	background: url(page.gif) no-repeat left center;
	padding-left: 20px; margin: 0 10px 0 5px;
}

/* sidebar */
#sidebar {
	float: left;
	width: 23%;
	margin: 0;	padding: 0; 
	display: inline;		
}
#sidebar ul.sidemenu {
	list-style:none;
	margin:10px 0 10px 15px;
	padding:0;		
}
#sidebar ul.sidemenu li {
	margin-bottom:1px;
	border: 1px solid #f2f2f2;
}
#sidebar ul.sidemenu a {
	display:block;
	font-weight:bold;
	color: #333;	
	text-decoration:none;	
	padding:2px 5px 2px 10px;
	background: #f2f2f2;
	border-left:4px solid #CCC;	
	
	min-height:18px;
}

/* * html body #sidebar ul.sidemenu a { height: 18px; } */

#sidebar ul.sidemenu a:hover {
	padding:2px 5px 2px 10px;
	background: #f2f2f2;
	color: #339900;
	border-left:4px solid #9EC630;
}

/* rightbar */
#rightbar {
	float: right;
	width: 48%;
	padding: 0; margin:0;		
}

/* Footer */
#footer { 
	clear: both; 
	color: #FFF; 
	background: #A9BAC3; 
	border-top: 5px solid #568EB6;
	margin: 0; padding: 0; 
	height: 50px;	  
	font-size: 95%;		
}
#footer a { 
	text-decoration: none; 
	font-weight: bold;	
	color: #FFF;
}
#footer .footer-left{
	float: left;
	width: 65%;
}
#footer .footer-right{
	float: right;
	width: 30%;
}

/* menu tabs */
#header ul {
	z-index: 999999;
	position: absolute;
   margin:0; padding: 0;
   list-style:none;
	right: 0; 
	bottom: 6px !important; bottom: 5px;
	font: bold 13px  Arial, 'Trebuchet MS', Tahoma, verdana,  sans-serif;	
}
#header li {
   display:inline;
   margin:0; padding:0;
}
#header a {
   float:left;
   background: url(tableft.gif) no-repeat left top;
   margin:0;
   padding:0 0 0 4px;
   text-decoration:none;
}
#header a span {
   float:left;
   display:block;
   background: url(tabright.gif) no-repeat right top;
   padding:6px 15px 3px 8px;
   color: #FFF;
}
/* Commented Backslash Hack hides rule from IE5-Mac \*/
#header a span {float:none;}
/* End IE5-Mac hack */
#header a:hover span {
	color:#FFF;
}
#header a:hover {
   background-position:0% -42px;
}
#header a:hover span {
   background-position:100% -42px;
}
#header #current a {
   background-position:0% -42px;
	color: #FFF;
}
#header #current a span {
   background-position:100% -42px;
	color: #FFF;
}
/* end menu tabs */

/* alignment classes */
.float-left  { float: left; }
.float-right {	float: right; }
.align-left  {	text-align: left; }
.align-right {	text-align: right; }

/* additional classes */
.clear { clear: both; }
.green {	color: #9EC630; }
.gray  {	color: #BFBFBF; }
.orange {       color: #CC3300; }

</style>
</head>
<body>
<!-- wrap starts here -->
<div id="wrap">
	
	
				
	<!-- content-wrap starts here -->
	<div id="content-wrap">		
	<div class="bl"><div class="br"><div class="tl"><div class="tr">									
        <h1><span class="green">ValidFi:</span> Comprehensive Financial Strategies, <span class="orange">Advanced Quantitative Platform,</span> Fund Analytics. <a href="../ajax/${HomeURL}Guide">Read the primer ... </a></h1>
        </div></div></div></div>
        <div class="clear">&nbsp;</div>
			
		<div id="main">	

                        <h1><span>News and Announcements</span></h1>
			<a name="TemplateInfo"></a>
<ul>
    <li> <span>The S&P Diversified Trend Indicator (DTI): it withstood the great crash of 2008, 
         reminding us the benefits of diversification over multiple assets with flexible strategies (long and short), 
         not just buy and hold. See <a target="_blank" href="../strategy/View.action?ID=432&action=view">here</a>.</span></li> 
    <li> <span>The Fed model: it stumbled in the 2008 crash. Does it really work? 
         see <a target="_blank" href="../strategy/View.action?ID=479&action=view">here</a>. </span></li>
    <li> <span>Global tactical asset allocation <a target="_blank" href="../strategy/View.action?ID=460&amp;action=view">(value)</a> 
         and global tactical asset allocation 
         <a target="_blank" href="../strategy/View.action?ID=452&amp;action=view">(momentum</a>) 
         strategies added. They complement with each other, a good hedge. 
         See <a target="_blank" href="../strategy/View.action?ID=462&amp;action=view">their combination</a>.</span></li>
</ul>	
                        <h1>Multi-Strategy Model Portfolios: Another Diversification Dimension</h1>
<ul>
        <li><span>Three types of model portfolios (Conservative, Moderate and Growth) in three styles (Lazy, Simple and Flexible). 
            See <a target="_blank" href="../ajax/${HomeURL}ModelPortfolios&includeHeader=true">here</a> for more details.  </span></li>
        <li><span>Use ValidFi's asset allocation tool <a href="../MVO/main.jsp" target="_blank">MVO</a> to construct 
             a multiple strategy portfolio. See here for the tutorial.</span></li>
</ul>
                       <h1>Portfolio Management, Asset Allocation and Multiple Strategy Selection</h1>
<ul>
    <li><span>Construct your own portfolios and perform historical simulation, study portfolio analytics and monitor them. 
         See demo <a target="_blank" href="../ajax/CustomizePage.action?pageName=PortfolioOverviewDemo">here</a>.</span></li>
    <li><span>Super portfolios --- Choose and mix portfolios provided by ValidFi and/or your own to construct a super portfolio. 
        A good example is the <a target="_blank" href="../portfolio/Edit.action?ID=3112">core satellite portfolio</a> concept.
        See demo here.</span></li>
    <li><span>Asset allocation --- <a href="../MVO/main.jsp" target="_blank">MVO</a> (Mean Variance Optimization). 
        An advanced Black-Litterman's asset allocation method incorporates investors' market outlook and make the outcome 
        more stable and sensible. See <a href="../blapp/BasicSetup.action?action=create" target="_blank">here</a> for Black-Litterman's tool. </span</li>
</ul>                       			
					
		</div>	
			
		<div id="rightbar">
			<box1>
			<h1>Strategy Watch</h1>
                        <ul>
                         <li><span>Top strategies in each category: <a target="_blank" href="../ajax/${HomeURL}HomePageTable&includeHeader=true">asset allocation, rebalance, cash flow, equity, fixed income, commodities and more</a></span>
                           <ul>
                             <li> <span>Retirement withdrawal strategies: how do they do in this turbulent time? </span></li>
                             <li> <span>Portfolio rebalancing strategies: reducing risks or taking advantage of the recent market deficiency?</span></li>
                           </ul>
                         </li>
                         <li><span>Portfolio and strategies screening here</span></li>
                         <li><span>Strategies employing other instrutments:</span>
                           <ul>
                            <li><span>Closed end funds</span></li>
                           </ul>
                         </li>
                       </ul>
                       </box1>
                       <h1>Fund Watch</h1>
                       <ul>
                        <li><span>What are the great asset allocators doing? John Hussman, GMO's Jeremy Grantham, Steve Leuthold, Ivy Asset Management, PIMCO's (Research Affiliated) Rob Arnott, etc. <a target="_blank" href="../ajax/${HomeURL}mutualfund_raa&includeHeader=true">See here</a></span></li>
                        <li><span>How about stock sectors? CGM's Ken Heebner, FPA's Steve Romick, etc. <a target="_blank" href="../ajax/${HomeURL}mutualfund_raa&includeHeader=true">See here</a></span></li>
                        <li><span>How about my funds: use Real time Asset Allocation Analyzer here. </span></li>
                        <li><span>Fund Alert: are funds skidding out of their trajectories? Red alerts!</span></li>
                       </ul>
                       <h1>Asset Classes On a Page</h1>
                       <h1>Communitiy Forum Watch</h1>
			
		</div>			
			
	<!-- content-wrap ends here -->		
	</div>

	
<!-- wrap ends here -->
</div>

</body>
</html>