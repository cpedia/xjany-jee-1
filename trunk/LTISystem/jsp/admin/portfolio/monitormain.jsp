<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Monitor Main Page</title>
		<link rel="stylesheet" href="images/style.css" type="text/css" />
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>
		<script type="text/javascript" src="../images/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="../images/jquery.tablesorter.pager.js"></script>    
		<link href="images/facebox/facebox.css" media="screen" rel="stylesheet" type="text/css"/>
		<script src="images/facebox/facebox.js" type="text/javascript"></script> 
	</head>
	
	<body onload="adjHiehgt()">
		<script>
			$(document).ready(function() 
			    { 
			        $("#logtable").tablesorter(); 
			        $('a[rel*=facebox]').facebox()
			    } 

			); 
			    
		</script>
		<table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1">
			<thead > 
			<tr>
				<th class="header">
				Name
				</th>
				<th class="header">
				Value
				</th>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td>
				Total Counts
				</th>
				<td>
				<s:property value="%{totalNumber}"/>
				</th>
			</tr>			
			<tr class='even'>
				<td>
				Failed Counts
				</td>
				<td>
				<s:property value="%{failedNumber}"/>
				</td>
			</tr>
			<tr class='odd'>
				<td>
				Finished Counts
				</td>
				<td>
				<s:property value="%{finishedNumber}"/>
				</td>
			</tr>
			<tr class='even'>
				<td>
				Live Counts
				</td>
				<td>
				<s:property value="%{othersNumber}"/>
				</td>
			</tr>
		</table>
		Clicking the column header to sort<br>
		Tips! Sort multiple columns simultaneously by holding down the shift key and clicking a second, third or even fourth column header! 
		<table id="logtable" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
			<thead>
			<tr>
				<th class="header">
					ID
				</th>
				<th class="header">
					Name
				</th>	
				<th class="header">
					Last Valid Date
				</th>
				<th class="header">
					State
				</th>	
				<th class="header">
					Is Live
				</th>				
				<th class="header" colspan="2">
					Message
				</th>						
																																				
			</tr>			
			</thead>
			<tbody>
			<s:iterator value="#request.portfolioStates" status="st">
			<s:if test="#st.index%2==0">
			<tr class='odd'>
			</s:if>
			<s:if test="#st.index%2==1">
			<tr class='even'>
			</s:if>
				
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Edit.action" id="urladdr" namespace="/jsp/portfolio">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="name"/></s:a>
					</td>
					<td>
						<s:property value="lastValidDate"/>
					</td>					
					<td>
						<s:property value="state"/>
					</td>
					<td>
						<s:property value="isLive"/>
					</td>					
					<td>
					<s:url action="ViewState.action" id="url_view" namespace="/jsp/admin/portfolio" includeParams="none">
						<s:param name="portfolioID" value="ID"></s:param>
					</s:url>
					<pre><s:property value="message"/></pre>
					
					</td>
					<td>
					<a href='<s:property value="url_view"/>' >more</a>
				</tr>
			</s:iterator>
			</tbody>
		</table>

	</body>
</html>
