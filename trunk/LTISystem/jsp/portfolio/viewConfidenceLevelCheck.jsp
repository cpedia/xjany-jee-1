<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="fck" uri="/WEB-INF/FCKeditor.tld" %>
<html>
<head>

<title><s:property value="%{name}"/></title>

<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<link rel="stylesheet" href="../images/flora.all.css" type="text/css" media="screen" title="Flora (Default)">
<script type="text/javascript" src="../images/jquery.ui/ui.core.packed.js"></script>
<script type="text/javascript" src="../images/jquery.ui/ui.tabs.packed.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $(".fbbl_center > ul").tabs();
  });

</script>
</head>
<body>
	<div class="fbbl_center">
		<ul>
		<li><a href='#modelPortfolio'><span>Model Portfolio</span></a></li>
        <li><a href="#strategy"><span>Strategy</span></a></li>
        </ul>
    	
    	<div id="modelPortfolio">
    		<p>Confidence Check Interval: <a href="#" class="interval"><s:property value="confidence"/></a></p>
    		<table cellspacint="0" class="dataTable">
    		<thead>
    		<tr><th>Sample Size</th><th>Standard Deviation</th><th>Mean</th><th>Above Mean</th></tr>
    		</thead>
    		<s:iterator value="#request.ConfidenceBeans" status="st">
    		<s:if test="#request.ConfidenceBeans[#st.getIndex()].strategyName==null">
    		<tr>
    		<td>
                		<s:property value="sampleSize"/>
                	</td>
                	<td>
                		<s:property value="variance"/>
                	</td>
                	<td>
                		<s:property value="mean"/>%
                	</td>                	
                	<td>
                		<s:property value="AboveMeanPossibility"/>%
                	</td>
                	</tr>
                	</s:if>
                </s:iterator>
    		</table>
    		
    		<div class="spaceDiv"></div>
    		<p>Return Achievement in Sample Percentage</p>
    		<table cellspacint="0" class="dataTable">
    		<thead>
    		<tr><th>Sample Percentage</th><th>5%</th><th>10%</th><th>15%</th><th>20%</th><th>30%</th><th>40%</th><th>50%</th><th>60%</th></tr>
    		</thead>
    		<s:iterator value="#request.ConfidenceBeans" status="st">
    		<s:if test="#request.ConfidenceBeans[#st.getIndex()].strategyName==null">
    		<tr>
    		<td>Minimum Return in Sample Percentage</td>
    		<td><s:property value="maxReturnUnderSampleProportion5"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion10"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion15"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion20"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion30"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion40"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion50"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion60"/>%</td>
    		</tr>
    		</s:if>
    		</s:iterator>
    		</table>
    		<div class="spaceDiv"></div>
    		<s:url id="original_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
			<s:param name="ID" value="portfolioID"></s:param>
			</s:url>
    		<s:a href="%{original_url}">Go to Current Portfolio</s:a>
    		
    	</div>
    	
    	<div id="strategy" >
    		<table cellspacint="0" class="dataTable">
    		<thead>
			<tr><th>Strategy Name</th><th>Rule Name</th><th>Sample Size</th><th>Standard Deviation</th><th>Mean</th><th>Above Mean</th></tr>    		
    		</thead>
    		<s:iterator value="#request.ConfidenceBeans" status="st">
    		<s:if test="#request.ConfidenceBeans[#st.getIndex()].strategyName!=null">
    		<tr <s:if test="#st.odd">class="odd"</s:if>>
    		<td>
    			<s:property value="strategyName"/>
    		</td>
    		<td>
    			<s:property value="ruleName"/>
    		</td>
    		<td>
    			<s:property value="sampleSize"/>
    		</td>
    		<td>
        		<s:property value="variance"/>
        	</td>
        	<td>
        		<s:property value="mean"/>%
        	</td>
        	
        	<td>
        		<s:property value="AboveMeanPossibility"/>%
        	</td>
    		</tr>
    		</s:if>
    		</s:iterator>
    		</table>
    		<div class="spaceDiv"></div>
    		<p>Return Achievement in Sample Percentage</p>
    		<table cellspacint="0" class="dataTable">
    		<thead>
    		<tr><th colspan="2">Sample Percentage</th><th>5%</th><th>10%</th><th>15%</th><th>20%</th><th>30%</th><th>40%</th><th>50%</th><th>60%</th></tr>
    		</thead>
    		<s:iterator value="#request.ConfidenceBeans" status="st">
    		<s:if test="#request.ConfidenceBeans[#st.getIndex()].strategyName!=null">
    		<tr <s:if test="#st.odd">class="odd"</s:if>>
    		<td>Minimum Return in Sample Percentage</td>
    		<td><s:property value="ruleName"/></td>
    		<td><s:property value="maxReturnUnderSampleProportion5"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion10"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion15"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion20"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion30"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion40"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion50"/>%</td>
    		<td><s:property value="maxReturnUnderSampleProportion60"/>%</td>
    		</tr>
    		</s:if>
    		</s:iterator>
    		</table>
    		<div class="spaceDiv"></div>
    		<s:a href="%{original_url}">Go to Current Portfolio</s:a>
    	</div>	
		
	</div>	
	</body>
	
</html>
