[#ftl]
[#if output.isUpdating]
	<h1>${output.title}[#if output.mode==0](daily)[#else](batch)[/#if]</h1>
	<br>
	<br>
	<font color="red">if only update, it will take some minutes to pick up portfolios at first.</font>
	<br>
	Start Date:${output.startDate?string("yyyy-MM-dd HH:mm:ss")} 
	<br>
	Running Period: ${output.runningPeriod}
	<br>
	Total Size:${output.updateSize}
	<br>
	Processing: ${output.processing}
	<br>
	Current Running Portfolio Name: [${output.portfolioID}]${output.portfolioName}
	<br>
	Current Running Portfolio Start Date:${output.currentStartDate?string("yyyy-MM-dd HH:mm:ss")} 
	<br>
	Current Running Portfolio Period: ${output.currentRunningPortfolioPeriod}
	<br>
	<a href='http://${output.ip}:8081/updatelist?portfolioID=0&mode=${output.mode}' target="_blank">Update portfolios list</a>
	<br>
	<br>
	<br>
	<br>
	<a href='http://${output.ip}:8081/dailystop?portfolioID=0&mode=${output.mode}'>Stop Daily Execution</a>
	<br>
	<br>
	
	
	[#list output.executors as pair]
		<br><a href='http://${output.ip}:8081/stop?portfolioID=${pair[0]}'>Stop '${pair[1]}'</a>
		<br>
	[/#list] 

[#else]
	<script>
		function startSpecialDate(){
			window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&mode=${output.mode}&filter=SpecailDatePortfoliosFilter&specialDate='+$('#specailDate').val();
		}
		function startSpecialStrategy(){
			window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&mode=${output.mode}&filter=SpecailStrategyPortfoliosFilter&strategyID='+$('#specailStrategy').val();
		}
		function startUpdateMode(){
			var interval = "0_0_0_0_0_1_1_1";
			window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&mode=${output.mode}&filter=UpdateModePortfolioFilter&interval='+interval+'&title=Daily Execution Use Update Mode';
		}
		function startBatchMonitor(){
		window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&mode=${output.mode}&filter=BatchMonitorFilter&updateMode='+$('#updateMode').val()+'&keyWords='+$('#keyWords').val()+'&title=BatchMonitorMode';
		}
	</script>
	<h2>[#if output.mode==0]Daily Execution is not started.[#else]Batch Execution is not started.[/#if]</h2>
	<br>
	<br>
	[#if output.mode==0]
		
		<a href='http://${output.ip}:8081/dailystart?portfolioID=0'>Start Daily Execution</a>
		<br>
		<br>
		
		<a href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=EmailAlertFilter&title=EmailAlert'>EMail Alert</a>
		<br>
		<br>
		[#--
		<a href='http://${output.ip}:8081/Advance'>Advance</a>
		<br><br>
		--]
	[#else]
		<table>
		<tr>
			<td>
				<form action="http://${output.ip}/LTISystem/jsp/admin/portfolio/UploadExecutionBatchPlanFile.action" namespace="" method ="post" enctype="multipart/form-data">
					<input type='file' name='uploadFile'>
					<input type="submit" value="Upload a new planID file">
				</form>
			</td>
			<td>
				<form action="http://${output.ip}/LTISystem/jsp/admin/portfolio/DownLoadExecutionBatchPlanFile.action" namespace="" method = "post">
					<input type="submit" value="Download current planID file">
				</form>
			</td>
		</tr>
		<tr>
			<td>
				<form action="http://${output.ip}/LTISystem/jsp/admin/portfolio/DownLoadUnSendPortfolioIDFile.action" namespace="" method = "post">
					<input type="text" name="endDate" value="">format:"2010-10-10"
					<input type="submit" value="Download Un Send Email PortfolioID file">
				</form>
			</td>
		</tr>
		</table>
		
		<form action="http://${output.ip}:8081/dailystart" method="get" >
			<input type="hidden" name="title" value="batch_monitor">
			<input type="hidden" name="portfolioID" value="0">
			IDs:<input type="text" name="ids">
			<input type="hidden" name="mode" value="1">
			<select style="width:200px;"  name="filter">
				<option value="PlanFilter">Plan IDs</option>
				<option value="PortfolioIDFilter">Portfolio IDs</option>
			</select>
			<select style="width:200px;"  name="dateMode">
				<option value="0">Before And On EndDate</option>
				<option value="1">On EndDate</option>
				<option value="2">On And After EndDate</option>
			</select>
			<input type="text" name="endDate" value="">format:"2010-10-10"
			<input type="submit" value="execute">
			<br>If Plan IDs is empty, we will user current planID file as default
			
		</form>
	[/#if]
[/#if]



