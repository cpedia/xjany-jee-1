<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<title>Log Main Page</title>
		
	</head>
	<body>
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="LogMain" id="url_main" namespace="/jsp/portfolio" includeParams="none">
					<s:param name="portfolioID" value="%{portfolioID}"/>
				</s:url>
				<s:a href="%{url_main}"><s:text name="all.logs"></s:text></s:a>				
			</td>	
			<td width="15%">
				<s:url action="LogMain.action" id="url_strategy" namespace="/jsp/portfolio" includeParams="none">
					<s:param name="portfolioID" value="%{portfolioID}"/>
					<s:param name="type">1</s:param>
				</s:url>
				<s:a href="%{url_strategy}"><s:text name="strategy.logs"></s:text></s:a>				
			</td>	
			<td width="15%">
				<s:url action="LogMain" id="url_system" namespace="/jsp/portfolio" includeParams="none">
					<s:param name="portfolioID" value="%{portfolioID}"/>
					<s:param name="type">0</s:param>
				</s:url>
				<s:a href="%{url_system}"><s:text name="system.logs"></s:text></s:a>				
			</td>								
			<s:iterator value="strategyIDs" id="id">
				<td width="15%">
						<s:url action="LogMain" id="url_1" namespace="/jsp/portfolio" includeParams="none">
							<s:param name="portfolioID" value="%{portfolioID}"/>
							<s:param name="strategyID" value="%{ID}"/>
						</s:url>
						<s:a href="%{url_1}"><s:property value="%{name}"/></s:a>	
				</td>
			</s:iterator>					
			<td>
			</td>
		</table>	
		<p class="title"><s:property value="portfolioName"/><s:text name="log.tips"></s:text></p>
		<s:if test="isHolding == true">
				<s:text name="holding.tips"></s:text><s:property value="holdingDate"/>
		</s:if>
		<table width="100%">
			<tr bgcolor="#FFEEEE">
				<td>
					<s:text name="date"></s:text>
				</td>	
				<td>
					<s:text name="message"></s:text>
				</td>		
																						
			</tr>		
			<s:iterator value="#request.logs">
				<tr>

					<td>
						<s:property value="logDateStr"/>
					</td>
					<td algin="left">
						<pre><s:property value="message" escape="false"/></pre>
						
					</td>
			
				</tr>
			</s:iterator>

		</table>

		<div id="button" align="center">
			<input type="button" value='<s:text name="back"></s:text>' onclick="backToPortfolio()">
		</div>
		<s:url id="portfolio_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
			<s:param name="ID" value="#request.portfolioID"></s:param>
		</s:url>	
		<script>
		/**
		 * backToPortfolio
		 * @param {}  
		 */
		 function backToPortfolio() {
		 	window.location.href = '<s:property value="portfolio_url"/>';
		 }
		</script>
		
	</body>
</html>
