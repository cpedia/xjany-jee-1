<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Portfolio Main Page</title>
	</head>
	<body>
		<s:form action="ScreeningMain.action" namespace="/jsp/portfolio">
		<s:select label="Year" list="#{'-1':'LAST_ONE_YEAR','-3':'LAST_THREE_YEAR','-5':'LAST_FIVE_YEAR','0':'FROM_STARTDATE_TO_ENDDATE','2008':'2008','2007':'2007','2006':'2006','2005':'2005'}" name="year"></s:select>
		<s:select label="Sort Method" list="#{'0':'SORT_BY_AR','1':'SORT_BY_ALPHA','2':'SORT_BY_BETA','3':'SORT_BY_RSQUARED','4':'SORT_BY_SHARPERATIO','5':'SORT_BY_STANDARDDEVIATION','6':'SORT_BY_TREYNORRATIO','7':'SORT_BY_DRAWDOWN'}" name="sortMethod"></s:select>
		<s:select label="is Model Portfolio" list="#{'null':'all','true':'true','false':'false'}" name="isModelPortfolio"></s:select>
		<s:textfield label="Class ID" name="classID"></s:textfield>
		<s:submit value="GO"></s:submit>
		</s:form>

		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Name
				</td>	
				<td>
					year
				</td>
				<td>
					Class ID
				</td>							
				<td>
					Strategy ID
				</td>
				<td>
                  Alpha
                </td>
                 <td>
                  Beta
                </td>                
                 <td >
                  AR
                </td> 
                <td>
                  RSquared
                </td> 
                <td>
                  Sharpe Ratio
                </td> 
                <td>
                  Standard Deviation
                </td>
                <td>
                  Treynor Ratio
                </td>
                <td>
                  Draw Down
                </td> 																																								
			</tr>			
		
			<s:iterator value="#request.portfolios">
				<tr>
					<td>
						<s:property value="portfolioID"/>
					</td>
					<td>
						<s:url action="Edit.action" id="urladdr" namespace="/jsp/portfolio">
							<s:param name="ID" value="portfolioID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="name"/></s:a>
					</td>
					<td>
						<s:property value="yearString"  />
					</td>
					<td>
						<s:property value="classID"  />
					</td>							
					<td>
						<s:property value="strategyID"  />
					</td>					
	 				<td>
	                  <s:property value="alpha"  />
	                </td>
	                 <td>
	                  <s:property value="beta"  />
	                </td>                
	                 <td >
	                  <s:property value="AR"  />
	                </td> 
	                <td>
	                  <s:property value="RSquared"  />
	                </td> 
	                <td>
	                  <s:property value="sharpeRatio"  />
	                </td> 
	                <td>
	                  <s:property value="standardDeviation"  />
	                </td>
	                <td>
	                  <s:property value="treynorRatio"  />
	                </td>
	                <td>
	                  <s:property value="drawDown"  />
	                </td>  
																	
				</tr>
			</s:iterator>

		</table>
	</body>
</html>
