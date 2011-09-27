[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]

<html>
<head>
[#assign strategyMenuItem="ValidFi Strategies"]
[#if Parameters.group?? && Parameters.group=="my"]
	[#assign strategyMenuItem="mystrategy"]
	<title>My Strategies</title>	
[/#if]
[#if Parameters.group?? && Parameters.group=="premiums"]
	[#assign strategyMenuItem="premiumsstrategy"]
	<title>Premiums Strategies</title>
[/#if]
[#if Parameters.group?? && Parameters.group=="public"]
	[#assign strategyMenuItem="publicstrategy"]
	<title>Public Strategies</title>
[/#if]
[#if Parameters.group?? && Parameters.group=="all"]
	[#assign strategyMenuItem="allstrategy"]
	<title>All Strategies</title>
[/#if]

<meta name="submenu" content="strategymaintable"/>
<meta name="strategies" content="vf_current" />
<meta name='${strategyMenuItem}' content="vf_current"/>
<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
<link rel="stylesheet" href="../images/jquery.tablesorter/addons/pager/jquery.tablesorter.pager.css" type="text/css" />

</head>
<body>

	<div id="searchdiv" align="left" style="margin:10px">
		<table>
			<tr>
				<td align="left">
				[@s.form namespace="/jsp/strategy"]
					<label>[@s.text name="strategy.name"][/@s.text]:</label>
					[@s.textfield id="namefield" name="name" theme="simple"][/@s.textfield]
					<input id="searchName" type="button" value="Search" onclick="searchByName()" />
				[/@s.form]
				</td>
				<td>
				[@s.form]
					<label>[@s.text name="categories"][/@s.text]:</label>
					[@s.textfield id="catefield" name="categories" theme="simple"][/@s.textfield]
					<input id="searchCategory" type="button" value="Search" onclick="searchByCategory()" />
				[/@s.form]					
				</td>
			</tr>
		</table>
		<script type="text/javascript">
			$("#namefield").keydown(function(ev){
				//var e = event.srcElement;
				if(ev.which==13)
				{
					$("#searchName").click();
					return false;
				}
			})
			$("#catefield").keydown(function(ev){
				//var e = event.srcElement;
				if(ev.which==13)
				{
					$("#searchCategory").click();
					return false;
				}
			})
		</script>
		<script type="text/javascript">
		function searchByName(){
				var name = $("#namefield").val();
				name = escape(name);
				if(name.length<3){
					alert("keyword is too small to search.");
					return;
				}
				$('#strategyClassTable').html('please wait for a moment.');
				[#if Parameters.group?? && Parameters.group=="all"]
				var url = "GetStrategyTable.action?includeHeader=false&admin=true&q=";
				[#elseif Parameters.group?? && Parameters.group=="my"]
				var url = "GetStrategyTable.action?includeHeader=false&owner=true&q=";
				[#elseif Parameters.group?? && Parameters.group=="premiums"]
				var url = "GetStrategyTable.action?includeHeader=false&groupIDs=4,5&q=";
				[#else]
				var url = "GetStrategyTable.action?includeHeader=false&q=";
				[/#if]
				url=url + name;
				$('#strategyClassTable').load(url);
			}
			function searchByCategory(){
				var cate = $("#catefield").val();
				cate = escape(cate);
				if(cate.length<3){
					alert("keyword is too small to search.");
					return;
				}
				$('#strategyClassTable').html('please wait for a moment.');
				[#if Parameters.group?? && Parameters.group=="all"]
				var url = "GetStrategyTable.action?includeHeader=false&admin=true&categories=";
				[#elseif Parameters.group?? && Parameters.group=="my"]
				var url = "GetStrategyTable.action?includeHeader=false&owner=true&categories=";
				[#elseif Parameters.group?? && Parameters.group=="premiums"]
				var url = "GetStrategyTable.action?includeHeader=false&groupIDs=4,5&categories=";
				[#else]
				var url = "GetStrategyTable.action?includeHeader=false&categories=";
				[/#if]
				url=url + cate+"&includeCategory=true";
				$('#strategyClassTable').load(url);
			}
		</script>
	</div>

[#if realtime==false]
    &nbsp; The following table contains portfolios with delayed information. To see the real time portfolio performance, please register and login.
<br>
[/#if]
	
<div id='${strategyMenuItem}s'>
	<!-- show the strategy class -->
	<div id="strategyClassTable">
	[#assign groupIDs=""]
	[#assign owner="false"]
	[#assign admin="false"]

	[#if Parameters.group?? && Parameters.group=="all"]
		[#assign admin="true"]
	[/#if]

	[#if Parameters.group?? && Parameters.group=="my"]
		[#assign owner="true"]
		<br class="clear">
		<span style="font-size:1.5em;margin:10px">My Strategies</span><a id="create" href="Edit.action?ID=0&action=create">[@s.text name="create.strategy"][/@s.text]</a>
		<div id='mystrategies'>
			[@s.action name="GetStrategyTable" namespace="/jsp/strategy" executeResult=true]
			    [@s.param name="includeCategory"]false[/@s.param]
                [@s.param name="includeLastValidDate"]true[/@s.param]
                [@s.param name="includeLastTransactionDate"]true[/@s.param]
			    [@s.param name="includeClassName"]false[/@s.param]
			    [@s.param name="includePortfolio"]true[/@s.param]
			    [@s.param name="year"]-1,-3,-5[/@s.param]
			    [@s.param name="mpt"]ar,sharperatio[/@s.param]
			    [@s.param name="sortColumn"]6[/@s.param]
			    [@s.param name="size"]0[/@s.param]
			    [@s.param name="owner"]true[/@s.param]
			[/@s.action]
		</div>
	[#elseif Parameters.group?? && Parameters.group=="premiums"]
		
			<h3 id="group4title"><a href='#'>Level_1</a></h3>
			<div id='group4'>
				[@s.action name="GetStrategyTable" namespace="/jsp/strategy" executeResult=true]
				    [@s.param name="includeCategory"]false[/@s.param]
				    [@s.param name="includeClass"]true[/@s.param]
				    [@s.param name="includePortfolio"]true[/@s.param]
				    [@s.param name="year"]-1,-3,-5[/@s.param]
                    [@s.param name="includeLastValidDate"]true[/@s.param]
                    [@s.param name="includeLastTransactionDate"]true[/@s.param]
				    [@s.param name="mpt"]ar,sharperatio[/@s.param]
				    [@s.param name="size"]0[/@s.param]
				    [@s.param name="groupIDs"]4[/@s.param]
				[/@s.action]
			</div>
			<script>
				if($("#group4").html().length<200&&$("#group4").html().replace(/(^\s*)|(\s*$)/g, "").length==0){
					$("#group4title").hide();
				}
			</script>
			<h3 id="group5title"><a href='#'>Level_2</a></h3>
			<div id='group5'>
				[@s.action name="GetStrategyTable" namespace="/jsp/strategy" executeResult=true]
				    [@s.param name="includeCategory"]false[/@s.param]
				    [@s.param name="includeClass"]true[/@s.param]
                    [@s.param name="includeLastValidDate"]true[/@s.param]
                    [@s.param name="includeLastTransactionDate"]true[/@s.param]
				    [@s.param name="includePortfolio"]true[/@s.param]
				    [@s.param name="year"]-1,-3,-5[/@s.param]
				    [@s.param name="mpt"]ar,sharperatio[/@s.param]
				    [@s.param name="size"]0[/@s.param]
				    [@s.param name="groupIDs"]5[/@s.param]
				[/@s.action]
			</div>
			<script>
				if($("#group5").html().length<200&&$("#group5").html().replace(/(^\s*)|(\s*$)/g, "").length==0){
					$("#group5title").hide();
				}
			</script>
	[#else]
		[#list strategyClassBeans as bean]
			<h3><a href='../customizepage/StrategyClass${bean.classID}.action'>${bean.name}</a></h3>
			<div id='strategyClassTableContainer${bean.classID}'>
				[@s.action name="GetStrategyTable" namespace="/jsp/strategy" executeResult=true]
				    [@s.param name="includeCategory"]false[/@s.param]
				    [@s.param name="includeClassName"]false[/@s.param]
				    [@s.param name="includeLastValidDate"]true[/@s.param]
				    [@s.param name="includeLastTransactionDate"]true[/@s.param]
				    [@s.param name="includePortfolio"]true[/@s.param]
				    [@s.param name="year"]-1,-3,-5[/@s.param]
				    [@s.param name="mpt"]ar,sharperatio[/@s.param]
				    [@s.param name="size"]0[/@s.param]
				    [@s.param name="strategyClassID"]${bean.classID}[/@s.param]
				    [@s.param name="admin"]${admin}[/@s.param]
				[/@s.action]
			</div>
		[/#list]  
	[/#if]
	
	</div>

</div>	
</body>
</html>
