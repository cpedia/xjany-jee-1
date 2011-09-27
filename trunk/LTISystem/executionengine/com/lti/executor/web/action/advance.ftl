
<h1>Execute by file</h1>
<form action="http://${output.ip}:8081/dailystart?title=UploadByCSV" method="get" >
<input type="hidden" name="title" value="UploadByCSV">
<input type="hidden" name="portfolioID" value="0">
<input type="hidden" name="filter" value="FileFilter">
FilePath:<input type="text" name="filepath"><br>
<input type="submit" value="execute">
</form>

<script>
function startSpecialDate(){
	window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=SpecailDatePortfoliosFilter&specialDate='+$('#specailDate').val();
}
function startSpecialStrategy(){
	window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=SpecailStrategyPortfoliosFilter&strategyID='+$('#specailStrategy').val();
}
function startUpdateMode(){
	var interval = "0_0_0_0_0_1_1_1";
	window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=UpdateModePortfolioFilter&interval='+interval+'&title=Daily Execution Use Update Mode';
}
function startPriority(){
	var prioritys = "6_8_7_5_1_3_4_2";
	var interval = "0_0_0_0_0_1_1_1";
	window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=PriorityPortfolioFilter&priorityArray='+prioritys+'&interval='+interval+'&title=UpdateUsingPriority';
}
function startBatchMonitor(){
window.location.href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=BatchMonitorFilter&updateMode='+$('#updateMode').val()+'&keyWords='+$('#keyWords').val()+'&title=BatchMonitorMode';
}
</script>

<br>
<a href='http://${output.ip}:8081/Emailalertlist'>statistics</a>
<br>
<br>
<a href='http://${output.ip}:8081/dailystart?portfolioID=0&title=Normal'>Start Daily Execution</a>
<br>
<br>
<a href='javascript:startPriority()'>Start Daily Execution Using Priority</a>
<br>
<br>
<a href='javascript:startUpdateMode()'>Start Daily Execution Using Update Mode</a>
<br>
<br>
KeyWord:    <input type="text"  id=keyWords>
UpdateMode: <input type="text"  id=updateMode>
<a href='javascript:startBatchMonitor()'>Start Daily Execution Using batchMonitor</a>
<br>
<br>
<a href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=OnlyUpdatePortfoliosFilter&title=OnlyUpdate'>Start Daily Execution(Only update)</a>
<br>
<br>
<a href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=EmailAlertFilter&title=EmailAlert'>EMail alert</a>
<br>
<br>
<a href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=OnlyFinishedPortfoliosFilter&title=OnlyFinished'>Start Daily Execution(Only finished at previous execution)</a>
<br>
<br>
<a href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=DescPortfoliosFilter&title=Desc'>Start Daily Execution(Order by date desc)</a>
<br>
<br>
<a href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=IDDescPortfoliosFilter&title=IDDesc'>Start Daily Execution(Order by ID desc)</a>
<br>
<br>
<a href='http://${output.ip}:8081/dailystart?portfolioID=0&filter=AscPortfoliosFilter&title=ASC'>Start Daily Execution(Order by date asc)</a>
<br>
<br>
Date: <input type="text" value="yyyy-MM-dd" id=specailDate><a href='javascript:startSpecialDate()'>Start Daily Execution</a>
<br>
<br>
Strategy: <input type="text" value="0" id=specailStrategy><a href='javascript:startSpecialStrategy()'>Start Daily Execution</a>