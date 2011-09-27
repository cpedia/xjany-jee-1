[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<html>
	<head>
		<title>Beta Gain Ranking [#if Parameters.name??] - ${Parameters.name}[/#if]</title>
		<script type="text/javascript" src="../images/jquery-1.3.2.min.js"></script>
		<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
		<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
	</head>
	<body>
		
		<h2 align="center">${name}</h2>
		<table id="rankingofassetclass" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
			<thead>
				<tr>
					<th class="header">
						Symbol
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
						<a href="../betagainranking/GetDetails.action?symbol=${ranking.getSymbol()}&includeHeader=true" target="_blank">${ranking.getSymbol()}</a>
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
		<script>
		$("#rankingofassetclass").tablesorter(); 
		</script>
	</body>
</html>









