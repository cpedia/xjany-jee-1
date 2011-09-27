<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<style>
	.vf_odd {
	background-color: white; /* pale yellow for odd rows */
	}
	.vf_even {
	background-color: white; /* pale blue for even rows */
	}
</style>

<s:text name="from"></s:text> <s:date name="startDate" format="MM/dd/yyyy" /> <s:text name="to"></s:text> <s:date name="endDate" format="MM/dd/yyyy" />
<table bgcolor="#999999" border="0" cellpadding="0" cellspacing="1" width="100%" >
	<tr class=vf_even style="color:#3A5FCD">
        <td align="left" >
        </td>
        <s:iterator value="#request.MPTBeans"  status="st">
        	<td>
        		<s:property value="yearString"/>
        	</td>
        </s:iterator>
	</tr>
	
	<tr class="vf_odd">
        <td align="left" style="color:#3A5FCD">
          Annualized Return (%)
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="AR"/>
        	</td>
        </s:iterator>
	</tr>		
	<tr class="vf_even">
        <td align="left" style="color:#3A5FCD">
           Sharpe Ratio (%)
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="sharpeRatio"/>
        	</td>
        </s:iterator>
	</tr>		
<s:if test="#request.type!='simple'">
	
	<tr class="vf_odd">
        <td align="left" style="color:#3A5FCD">
           <s:text name="alpha"></s:text>(%)
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="alpha"/>
        	</td>
        </s:iterator>
																				
	</tr>	
	<tr class="vf_even">
        <td align="left" style="color:#3A5FCD">
           <s:text name="beta"></s:text>
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="beta"/>
        	</td>
        </s:iterator>
	</tr>	
								
	<tr class="vf_odd">
        <td align="left" style="color:#3A5FCD">
           <s:text name="rsquare"></s:text>
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="RSquared"/>
        	</td>
        </s:iterator>
	</tr>	
	
	<tr class="vf_even">
        <td align="left" style="color:#3A5FCD">
          <s:text name="standard.deviation"></s:text>
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="standardDeviation"/>
        	</td>
        </s:iterator>
	</tr>	
	<tr class="vf_odd">
        <td align="left" style="color:#3A5FCD">
          <s:text name="treynor"></s:text>
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="treynorRatio"/>
        	</td>
        </s:iterator>
	</tr>	
	<tr class="vf_even">
        <td align="left" style="color:#3A5FCD">
          <s:text name="draw.down"></s:text>
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="drawDown"/>
        	</td>
        </s:iterator>
	</tr>		

	<tr class="vf_odd">
        <td align="left" style="color:#3A5FCD">
          <s:text name="sortino.ratio"></s:text>
        </td>
        <s:iterator value="#request.MPTBeans">
        	<td >
        		<s:property value="sortinoRatio"/>
        	</td>
        </s:iterator>
	</tr>							                  
</s:if>
</table>
<p align="right"><a href="/LTISystem/jsp/portfolio/MPTDownload.action?portfolioID=<s:property value="portfolioID"/>">Download</a></p>	
