<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Portfolio Main Page</title>
		<link rel="stylesheet" href="images/style.css" type="text/css" />
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>
		<script type="text/javascript" src="../images/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="../images/jquery.tablesorter.pager.js"></script>
		<script type="text/javascript">
			function stopExecution(){
				$.ajax({type: "post",  
		             url:'ExecuteCommand.action?command=stopExecution',  
		             dataType: "html",  
		             //data: ,  
		             success: function(result){
		             	alert(result); 
		             },
		             error:function(){
		             	alert("Error");
		             }
		        });				
			}
			function stopCurrentExecution(id){
				$.ajax({type: "post",  
		             url:'ExecuteCommand.action?portfolioID='+id+'&command=stopCurrentExecution',  
		             dataType: "html",  
		             //data: ,  
		             success: function(result){
		             	alert(result); 
		             },
		             error:function(){
		             	alert("Error");
		             }
		        });
		        //window.location.reload();
			}
		</script>    
	</head>
	
	<body>
		<div>
		Daily Update Process<br>
		<table width="100%">
		<tr>
			<td>
				<table  id="processtable" class="tablesorter" border="0" cellpadding="0" cellspacing="1" width="50">
					<thead>
						<tr>
							<th class="header">
								Process
							</th>
						</tr>
					</thead>
					<tbody>
						<s:iterator  value="#request.stateList" status="st">
							<s:if test="#st.index%2==0">
							<tr class='odd'>
							</s:if>
							<s:if test="#st.index%2==1">
							<tr class='even'>
							</s:if>	
								<td><s:property/></td>
							</tr>
						</s:iterator>
					</tbody> 
				</table>
			</td>
			<td>
				<table width="50%" id="statetable" class="tablesorter" border="0" cellpadding="0" cellspacing="1" >
					<thead>
						<tr>
							<th class="header">
								State
							</th>
						</tr>
					</thead>
					<tbody>
						<s:iterator  value="#request.processState" status="st">
							<s:if test="#st.index%2==0">
							<tr class='odd'>
							</s:if>
							<s:if test="#st.index%2==1">
							<tr class='even'>
							</s:if>	
								<td><s:property/></td>
							</tr>
						</s:iterator>
					</tbody> 
				</table>
			</td>
		</tr>
		</table>
			
	
		
	
		Daily Update State<br>
		<table id="logtable" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
			<thead>
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
				<tr class="even">
					<td>Is updating</td>
					<td><s:property value="#request.isUpdating"/></td>
				</tr>
				<tr class="odd">
					<td>Start Date</td>
					<td><s:property value="#request.updateDBStartDate"/></td>
				</tr>
				<tr class="even">
					<td>Updating period</td>
					<td><s:property value="#request.updateDBPeriod"/> mins</td>
				</tr>
				<tr class="odd">
					<td>State</td>
					<td><s:property value="state"/></td>
				</tr>
				<tr class="even">
					<td>Total size</td>
					<td><s:property value="totalCount"/></td>
				</tr>
				<tr class="odd">
					<td>Current pos</td>
					<td><s:property value="currentCount"/></td>
				</tr>
			</tbody>
		</table>
		
		
		Running Portfolios<Br>
		<a href="javascript:stopExecution()">Stop Daily Execution</a><br>
		<table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1">
			<thead>
			<tr>
				<th class="header">Name</th>
				<th class="header">Progress</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator value="#request.executors" status="st">
					<s:if test="#st.index%2==0">
					<tr class='odd'>
					</s:if>
					<s:if test="#st.index%2==1">
					<tr class='even'>
					</s:if>
					<td>
						<s:property value="name"/>
						 <a href='javascript:stopCurrentExecution(<s:property value="ID"/>)'>Stop</a>
					</td>
					
					<td height="60">
						<iframe src='processing.jsp?portfolioID=<s:property value="ID"/>' name="detail" id="detail" scrolling="no" width="100%" height="60" frameborder="0" marginWidth="0" marginHeight="0" ></iframe>
					</td>
				</tr>
			</s:iterator>
			</tbody>
		</table>
		
		Logs of Daily Execution<Br>
		<table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1">
			<thead>
			<tr>
				<th class="header">Name</th>
				<th class="header">Download</th>
			</tr>
			</thead>
			<tbody>
			<s:iterator value="#request.logs" status="st">
					<s:if test="#st.index%2==0">
					<tr class='odd'>
					</s:if>
					<s:if test="#st.index%2==1">
					<tr class='even'>
					</s:if>
					<td>
						<s:property value="pre"/>
					</td>
					<td height="60">
						<s:url action="DownloadFile.action" id="urladdr" namespace="/jsp/ajax">
							<s:param name="name" value="pre"></s:param>
							<s:param name="isImageCache">false</s:param>		
						</s:url>
						<s:a href="%{urladdr}"><s:property value="pre"/></s:a>	
					</td>
				</tr>
			</s:iterator>
			</tbody>
		</table>
				
	</body>
</html>
