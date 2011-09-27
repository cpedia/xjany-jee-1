<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Security Page</title>
		<link rel="stylesheet" href="images/style.css" type="text/css" />		
	</head>
	<!--  <input type="submit" name="Submit" value="GO BACK" onclick="window.history.back()" >-->
		<table class="tablesorter" border="0" cellpadding="0" cellspacing="1">
		<tr  class="even">
			<td>
			Portfolio Name
			</td>
			<td>
			<s:property value="%{portfolioName}"/>
			</td>
		</tr>
		<!-- <tr  class="odd">
			<td>
			Portfolio Structure
			</td>
			<td>
			<pre><s:property value="%{portfolioInformation}"/></pre>
			</td>
		</tr> -->
		<tr  class="odd">
			<td>
			Portfolio ID
			</td>
			<td>
			<s:property value="%{portfolioState.PortfolioID}"/>
			</td>
		</tr>
		<tr  class="even">
			<td>
			Start Date
			</td>
			<td>
			<s:property value="%{portfolioState.StartDate}"/>
			</td>
		</tr>
		<tr  class="odd">
			<td>
			End Date
			</td>
			<td>
			<s:property value="%{portfolioState.EndDate}"/>
			</td>
		</tr>
		<tr  class="even">
			<td>
			Executed to Date
			</td>
			<td>
			<s:property value="%{portfolioState.CurrentDate}"/>
			</td>
		</tr>	
		<tr class="odd">
			<td>
			State
			</td>
			<td>
			<s:property value="%{portfolioState.State}"/>
			</td>
		</tr>	
		<tr class="even">
			<td>
			Message
			</td>
			<td>
			<pre><s:property value="%{portfolioState.Message}"/></pre>
			</td>
		</tr>	
		</table>			

	</body>
</html>
