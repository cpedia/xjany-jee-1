[#ftl]
[#assign baseUrl="/LTISystem"/]
[#setting url_escaping_charset='utf8']
[#macro lti]
welcome to myplaniq.com.
[/#macro]

[#-- Please include jqueryUrl first. --]
[#macro strategy_table id keyword]
<div id="${id}">
Please wait for loading data...
</div>
<script>
$("#${id}").load("${baseUrl}/jsp/strategy/GetStrategyTable.action?keyword=${keyword}");
</script>
[/#macro]

[#macro htmlTag name]
<${name}>
[/#macro]

[#macro basejs]
<script type="text/javascript" src="${jqueryUrl}"></script>
[/#macro]

[#macro questionmark tooltip id]
<a title="${tooltip}" href="javascript:void(0)" id="${id}"><img alt="${tooltip}" height=20px width=20px src="/LTISystem/jsp/template/ed/images/qm.png"></a>
[/#macro]

[#macro rankingmessage scorenum id]
[#assign score=scorenum*100]
[#if score == 0]
	unrated
[#elseif score<10]
	poor
[#elseif score<35]
	below average
[#elseif score< 65]
	average
[#elseif score< 85]
	above average
[#else]
	great
[/#if]
[/#macro]

[#macro form action]
<form action="${baseUrl}${action}" method="post">
<table width="100%" height="100%" border=0>
[#nested]
</table>
</form>
[/#macro]

[#macro input name value] 
<tr><td>${name}</td><td><input type="text" name="${name}" value="${value}"/></td></tr>
[/#macro]


[#macro tablejson jsoncallback items]
${jsoncallback}({
	items:[
		[#list items as item]
			{
				length:${item?size},
				values:[
				[#list item as i]
					{value:'${i}'}
					[#if i_index!=item?size-1],[/#if]
				[/#list]
				]
			}
			[#if item_index!=items?size-1],[/#if]
		[/#list]
	]
});
[/#macro]


[#macro table name items includeJS]

[#if includeJS]
<script type="text/javascript" src="../images/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
[/#if]
<script>
	$(document).ready(function(){ 
	        	$("#${name}").tablesorter(); 
	        } 
	); 
</script>		    

<table id="${name}" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
	[#if items??]
	[#list items as item]
	[#if item_index==0]
	<thead>
		<tr>
			[#list item as i]
			<th class="header">
				${i!"NA"}
			</th>	
			[/#list]
					
		</tr>			
	</thead>
	[#else]
	[#if item_index==1]
	<tbody>
	[/#if]
	
	[#if item_index%2==0]
		<tr class='odd'>
	[/#if]
	[#if item_index%2==1]
		<tr class='even'>
	[/#if]
			[#list item as i]
			<td>
				${i!""}
			</td>	
			[/#list]			
		</tr>
	[/#if]
	[/#list]
	[/#if]
	</tbody>
</table>	
[/#macro]

<!--only title-->
[#macro Articlesfeed type]

<script src="http://feeds.feedburner.com/ValidfiNewsAndArticles?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/ValidfiNewsAndArticles"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

<!--title&content-->
[#macro Articlesfeed2 type]

<script src="http://feeds.feedburner.com/ValidfiNewsAndArticles2?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/ValidfiNewsAndArticles2"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]


[#macro Forumtopicsfeed type]

<script src="http://feeds.feedburner.com/ValidfiForum-LatestForumTopics?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/ValidfiForum-LatestForumTopics"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro ForumNewsfor401k type]
<script src="http://feeds.feedburner.com/evergreenplans/kLuU?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/kLuU"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro Investment type]
<script src="http://feeds.feedburner.com/evergreenplans/aaPu?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/aaPu"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro Retirement type]
<script src="http://feeds.feedburner.com/evergreenplans/pgYz?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/pgYz"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro Insurance type]
<script src="http://feeds.feedburner.com/evergreenplans/HlFV?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/HlFV"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro CollegeEducation type]
<script src="http://feeds.feedburner.com/evergreenplans/uGPR?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/uGPR"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro PersonalFinance type]
<script src="http://feeds.feedburner.com/evergreenplans/BuSW?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/BuSW"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro Brokage type]
<script src="http://feeds.feedburner.com/evergreenplans/FjyQ?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/FjyQ"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro Funds type]
<script src="http://feeds.feedburner.com/evergreenplans/mByc?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/mByc"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro Newsfor401kHomePage type]
<script src="http://feeds.feedburner.com/evergreenplans/iVUh?format=sigpro" type="text/javascript" ></script><noscript><p>Subscribe to RSS headline updates from: <a href="http://feeds.feedburner.com/evergreenplans/iVUh"></a><br/>Powered by FeedBurner</p> </noscript>
[/#macro]

[#macro chart name key]

[#assign url]/LTISystem/jsp/XML.action?key=${key}&includeHeader=false[/#assign ]
[#assign vars]url=${url?url}&chartName=${name?url}&lineNameArray=${name?url}&address=www.validfi.com&port=80&currentMode=portfolio[/#assign]
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			 width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="/LTISystem/jsp/flash/main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='${vars}'>
			<param name="wmode" value="transparent">
			<embed src="/LTISystem/jsp/flash/main.swf" FlashVars='${vars}' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">

			</embed>
</object>
[/#macro]


[#macro chart name key host]

[#assign url]/LTISystem/jsp/XML.action?key=${key}&includeHeader=false[/#assign ]
[#assign vars]url=${url?url}&chartName=${name?url}&lineNameArray=${name?url}&address=${host}&port=80&currentMode=portfolio[/#assign]
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			 width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="/LTISystem/jsp/flash/main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='${vars}'>
			<param name="wmode" value="transparent">
			<embed src="/LTISystem/jsp/flash/main.swf" FlashVars='${vars}' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">

			</embed>
</object>
[/#macro]

[#-- for --------------------global obejct --]
[#macro gchart name key host]

[#assign url]/LTISystem/jsp/GlobalObjectXML.action?key=${key}&includeHeader=false[/#assign ]
[#assign vars]url=${url?url}&chartName=${name?url}&lineNameArray=${name?url}&address=${host}&port=80&currentMode=portfolio[/#assign]
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			 width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="/LTISystem/jsp/flash/main.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="allowScriptAccess" value="sameDomain" />
			<param id="flashvar" name=FlashVars value='${vars}'>
			<param name="wmode" value="transparent">
			<embed src="/LTISystem/jsp/flash/main.swf" FlashVars='${vars}' quality="high" bgcolor="#ffffff"
				width="100%" height="630" name="main" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">

			</embed>
</object>
[/#macro]


[#macro pagination ps]
	[#if ps?? && ps.pages??]
		<a href="PagedPortfolio.action?pageSize=${pageSize}&startIndex=0">First</a>&nbsp;&nbsp;
		[#list ps.pages as page]
			[#assign index=page*pageSize]
			
			[#if index==startIndex]
				<a href="PagedPortfolio.action?pageSize=${pageSize}&startIndex=${index}"><b>${page+1}</b></a>&nbsp;&nbsp;
			[#else]
				<a href="PagedPortfolio.action?pageSize=${pageSize}&startIndex=${index}">${page+1}</a>&nbsp;&nbsp;
			[/#if]
		[/#list]
		<a href="PagedPortfolio.action?pageSize=${pageSize}&startIndex=${(ps.totalPage-1)*pageSize}">Last</a>&nbsp;&nbsp;
	[/#if]
[/#macro]

[#macro pagination ps link symbol1]
	[#if ps?? && ps.pages??]
	[#assign var=link]
	[#if var?contains("?")][#assign var=link + "&"][#else][#assign var=link+"?"][/#if]
		<a href="${var}pageSize=${pageSize}&startIndex=0&symbol=${symbol1}">First</a>&nbsp;&nbsp;
		[#list ps.pages as page]
			[#assign index=page*pageSize]
			
			[#if index==startIndex]
				<a href="${var}pageSize=${pageSize}&startIndex=${index}"><b>${page+1}</b></a>&nbsp;&nbsp;
			[#else]
				<a href="${var}pageSize=${pageSize}&startIndex=${index}">${page+1}</a>&nbsp;&nbsp;
			[/#if]
		[/#list]
		<a href="${var}pageSize=${pageSize}&startIndex=${(ps.totalPage-1)*pageSize}&symbol=${symbol1}">Last</a>&nbsp;&nbsp;
	[/#if]
[/#macro]

[#macro blogpagination ps link symbol1]
	[#if ps?? && ps.pages??]
	[#assign var=link]
	[#if var?contains("?")][#assign var=link + "&"][#else][#assign var=link+"?"][/#if]
		<a href="${var}pageSize=${setPageSize}&startIndex=0&symbol=${symbol1}">First</a>&nbsp;&nbsp;
		[#list ps.pages as page]
			[#assign index=page*setPageSize?number]
			
			[#if index==startIndex]
				<a href="${var}pageSize=${setPageSize}&startIndex=${index}"><b>${page+1}</b></a>&nbsp;&nbsp;
			[#else]
				<a href="${var}pageSize=${setPageSize}&startIndex=${index}">${page+1}</a>&nbsp;&nbsp;
			[/#if]
		[/#list]
		<a href="${var}pageSize=${setPageSize}&startIndex=${(ps.totalPage-1)*setPageSize?number}&symbol=${symbol1}">Last</a>&nbsp;&nbsp;
	[/#if]
[/#macro]

[#macro p page totalpage url params=''] 
    [#assign ipage=page?number] 
    
		   	[#-- First --] 
		    [#if ipage gt 1]
		        <a href="${url}&page=${ipage - 1}">pre</a> 
		    [#else]
		        <span class="disabled">First </span> 
		    [/#if] 
		    	<!-- <span style="font-color:red;">${(ipage*10)-9} to ${ipage*10}</span> -->
		    	<span style="font-color:red;">${ipage}</span>
		    [#-- Last --] 
		    [#if ipage lt totalpage] 
		        <a href="${url}&page=${ipage + 1}">next</a> 
		    [#else] 
		        <span class="disabled"> Last</span> 
		    [/#if]
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   Total Page: ${totalpage}
   
    
[/#macro]
