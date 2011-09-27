
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<s:if test="strategyItems != null && strategyItems.size()>0">
<script type="text/javascript">
	$.tablesorter.addParser({
		id: 'num',
		is: function(s) {
			// return false so this parser is not auto detected
			return false;
		},
		format: function(s) {// format your data for normalization
			var n;
			n = s.toLowerCase().replace(/na/, Number.NEGATIVE_INFINITY);
			return parseFloat(n);
		},         
		type: 'numeric' 
	});
	$(document).ready(function(){
		$("#strategies").tablesorter({
			widthFixed: true, 
			widgets: ['zebra']
		});
	})
</script>
<style>
.classStyle{
	display:block;
	width:100px;
	text-overflow:ellipsis; 	
	white-space:nowrap;
	overflow:hidden;
}

</style>

<table id = "strategies" class="tablesorter" width="99%">
	<thead>
	<tr>
		<th class="header" width="15%">
		<s:text name="strategy"></s:text>
		</th>
		<th class="header" width="15%">
		<s:text name="portfolio"></s:text>
		</th>
		<th class="header" width="7%">
		<s:text name="last.valid.date"></s:text>
		</th>
		<th class="header" width="7%">
		<s:text name="last.transaction.date"></s:text>
		</th>
		<th class="header" width="7%">
			Styles
		</th>
		<th class="{sorter:'num',defaultsort:desc}" width="7%">
		1y-<s:text name="sharpe"></s:text>(%)
		</th>
		<th class="{sorter:'num'}" width="7%">
		1y-<s:text name="AR"></s:text>(%)
		</th>
		<th class="{sorter:'num'}" width="7%">
		3y-<s:text name="sharpe"></s:text>(%)
		</th>
		<th class="{sorter:'num'}" width="7%">
		3y-<s:text name="AR"></s:text>(%)
		</th>
		<th class="{sorter:'num'}" width="7%">
		5y-<s:text name="sharpe"></s:text>(%)
		</th>
		<th class="{sorter:'num'}" width="7%">
		5y-<s:text name="AR"></s:text>(%)
		</th>
		
	</tr>
	</thead>
	<!-- show the strategies in the strategy class -->
	<s:iterator value="strategyItems" status="st">
	<tr bgcolor="#ffffff">
		<td align="center" width="15%">
			<s:url action="View" namespace="/jsp/strategy" id="url_str" includeParams="none">
				<s:param name="ID" value="ID"></s:param>	
				<s:param name="action">view</s:param>				
			</s:url>
			<s:a href="%{url_str}"><div id='shortName' title="<s:property value='name'/>"><s:property value="showName"/></div></s:a>
		</td>
		<td align="center" width="15%">
			<s:if test='portfolioShortName.equals("NA")'>
				NA
			</s:if>
			<s:else>
				<s:url action="Edit" id="url_port" namespace="/jsp/portfolio" includeParams="none">
					<s:param name="ID" value="portfolioID"></s:param>
					<s:param name="action">view</s:param>						
				</s:url>
				<s:a href="%{url_port}"><div id="shortName" title="<s:property value='portfolioName'/>"><s:property value="portfolioShortName"/></div></s:a>
			</s:else>
			
		</td>
		<td width="7%">
			<s:property value="lastValidDate"/>
		</td>		
		<td width="7%">
			<s:property value="lastTransactionDate"/>
		</td>	
		<td width="7%" title='<s:property value="categories"/>'><span class="classStyle"><s:property value="categories"/></span></td>
		<td width="7%" align="center">
			<s:property value="sharpeRatio1" default="NA"/>
		</td>
		<td width="7%" align="center">
			<s:property value="AR1" default="NA"/>
		</td>
		<td width="7%" align="center">
			<s:property value="sharpeRatio3" default="NA"/>
		</td>
		<td width="7%" align="center">
			<s:property value="AR3" default="NA"/>
		</td>
		<td width="7%" align="center">
			<s:property value="sharpeRatio5" default="NA"/>		
		</td>
		<td width="7%" align="center">
			<s:property value="AR5" default="NA"/>
		</td>
	</tr>
	</s:iterator>  
</table>	
</s:if>
<s:else>
No Strategies Fits The Conditions.
</s:else>