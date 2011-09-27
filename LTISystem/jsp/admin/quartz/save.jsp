<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Quartz Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		
	<s:actionmessage/>
	<s:form action="Save" method="post">
		<br>If you want to disabele the End of Date schedule , please fill it with "0".</br>
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid; height:auto" cellSpacing=0 cellPadding=3 width="95%" align="left">

			<s:hidden name="action" value="save"/>
			<s:textfield name="updateDatabaseCornExpression" label="Update Database CornExpression "/>

			<s:textfield name="fastdailydataupdateOneCornExpression" label="End of Date DailyData Update CornExpression One"/>
			<s:textfield name="fastdailydataupdateTwoCornExpression" label="End of Date DailyData Update CornExpression Two"/>
			<s:textfield name="endofDateDailyExecutionOneCornExpression" label="End of Date Daily Execution CornExpression One"/>
			<s:textfield name="endofDateDailyExecutionTwoCornExpression" label="End of Date Daily Execution CornExpression Two"/>

			<s:textfield name="dailyExecutionCornExpression" label="Daily Execution CornExpression "/>
			<s:textfield name="clearPortfolioCornExpression" label="Daily Processor CornExpression "/>
			
 			<s:submit>Save</s:submit>
 		</table>

	</s:form>
   
	</body>
</html>
