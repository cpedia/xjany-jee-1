[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
	[#if securityRankingList??]
		<table id="historicalranking_${symbol}_${interval}" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
			<thead>
				<tr>
					<th class="header">
						EndDate
					</th>	
					<th class="header">
						Ranking
					</th>
				</tr>			
			</thead>
			<tbody>
			[#list securityRankingList as ranking]
				[#if ranking_index%2==0]
					<tr class='odd'>
				[/#if]
				[#if ranking_index%2==1]
					<tr class='even'>
				[/#if]
					<td>
						${ranking.getEndDate()?string("yyyy-MM")}
					</td>	
					<td>
						[#list 1..ranking.getRanking() as x]
							★
						[/#list]
					</td>
				</tr>
			[/#list]	
			</tbody>
		</table>
		<br class="clear"/>
	
		[#if securityRankingList.size() > 10]
		<div style="posistion:static" id="pager_${symbol}_${interval}" class="pager">
			<form>
				<img src="../images/jquery.tablesorter/addons/pager/icons/first.png" class="first"/>
				<img src="../images/jquery.tablesorter/addons/pager/icons/prev.png" class="prev"/>
				<input type="text" class="pagedisplay"/>
				<img src="../images/jquery.tablesorter/addons/pager/icons/next.png" class="next"/>
				<img src="../images/jquery.tablesorter/addons/pager/icons/last.png" class="last"/>
				<select class="pagesize">
					<option selected="selected" value="10">10</option>
					<option value="20">20</option>
					[#if securityRankingList.size()> 20 ]
					<option value="30">30</option>
					[/#if]
					[#if securityRankingList.size()> 30 ]
					<option value="40">40</option>
					[/#if]
				</select>
			</form>
		</div>
		[/#if]	
		<script type="text/javascript"> 
		$(document).ready(function(){
		$("#historicalranking_${symbol}_${interval}")
		.tablesorter({
			widthFixed: true, 
			widgets: ['zebra']
		})
			[#if securityRankingList.size() > 10]
			.tablesorterPager({container: $("#pager_${symbol}_${interval}")});
			[/#if]
		}); 
		</script>
[#else]
	[@s.actionerror/]
[/#if]









