<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>

<html>
<head>

	<meta name="screening" content="vf_current"/>
	<meta name="tools" content="vf_current" />
	<meta name="submenu" content="tools"/>
	<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
	<SCRIPT src="../images/jquery.tooltip/PopDetails.js" type="text/javascript" ></SCRIPT>
	<script src="../images/jquery.tooltip/jquery.bgiframe.js" type="text/javascript"></script>
	<script src="../images/jquery.tooltip/chili-1.7.pack.js" type="text/javascript"></script>
	<script src="../images/jquery.tooltip/jquery.tooltip.js" type="text/javascript"></script>
	<link rel="stylesheet" href="../images/jquery.tooltip/jquery.tooltip.css" />
	<!-- table sorter -->
	<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
	<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
	<link rel="stylesheet" href="../images/jquery.tablesorter/addons/pager/jquery.tablesorter.pager.css" type="text/css" />
	
	<!-- Initialize BorderLayout START-->
	<script type="text/javascript">
	</script>
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
		})
		$(document).ready(
			function(){
			<s:if test="#request.securityList!=null && securityList.size()>0">
			$("#result")
			.tablesorter({
				widthFixed: true, 
				widgets: ['zebra']
			})
			</s:if>
			<s:if test="#request.securityList.size() > 30">
			.tablesorterPager({container: $("#pager<s:property value='type'/>"),
							   size: 30});
			</s:if>
		}); 
	</script>
	<title>Screening Result</title>
</head>

<body>
<style>
	#create{
	float:left;
	}
	#search{
	float:right;
	}
	.clear{
	clear:both;
	}
	
	#vf_content-wrap {
    padding: 10px;
     }
</style>

<div class="fbbl_center" align="center" id="center_div">
<s:if test="securityList != null && securityList.size() != 0">
	<table id="result" class="tablesorter">
		<thead>
			<tr>
			<th width="20%">Name</th>
			<th>Symbol</th>
			<th>Type</th>
			<s:if test="isUseAssetClass==true">
				<th>Asset Type</th>
			</s:if>
			<s:if test="ExtraAttrs!=null&&ExtraAttrs.ExtraAttributes!=null">
				<s:iterator value="ExtraAttrs.ExtraAttributes">
					<th class="{sorter:'num'}"><s:property value="AttributeName"/></th>
				</s:iterator>
			</s:if>
			<s:if test="MPTList2!=null&&MPTList2.size()!=0">
				<s:iterator value="MPTList2" id="mpt">
					<s:if test="mpt.year > 0">
						<th class="{sorter:'num'}"><s:property value="year"/>-<s:property value="Name"/></th>
					</s:if>
					<s:else>
						<th class="{sorter:'num'}"><s:property value="year * -1"/>-<s:property value="Name"/></th>
					</s:else>
				</s:iterator>
			</s:if>
			<s:if test="showList != null && showList.size() != 0">
				<s:iterator value="showList" id="show">
					<s:if test="show.year > 0">
						<th class="{sorter:'num'}"><s:property value="year"/>-<s:property value="Name"/></th>
					</s:if>
					<s:else>
						<th class="{sorter:'num'}"><s:property value="year * -1"/>-<s:property value="Name"/></th>
					</s:else>
				</s:iterator>
			</s:if>
			</tr>
		</thead>
		<tbody>
		<s:iterator value="securityList" id="security">
		<tr>
			<s:if test="SecurityType != 6">
				<s:url id="security_url" namespace="/jsp/fundcenter" action="View">
					<s:param name="symbol" value="Symbol"></s:param>
					<s:param name="includeHeader">true</s:param>
					<s:param name="title" value="Symbol"></s:param>
				</s:url>
				
			</s:if>
			<s:else>
				<s:url id="security_url" namespace="/jsp/portfolio" action="Edit">
					<s:param name="ID" value="securityID"></s:param>
				</s:url>
			</s:else>
			<td><a href='<s:property value="security_url"/>'><s:property value="SecurityName"/></a></td>
			<td><s:property value="Symbol"/></td>
			<td><s:property value="SecurityTypeName"/></td>
			<s:if test="isUseAssetClass==true">
				<td><s:property value="assetClassName"/></td>
			</s:if>
			<s:iterator value="extras">
				<td><s:property/></td>
			</s:iterator>
			<s:iterator value="MPTStatistics">
				<td><s:property/></td>
			</s:iterator>
		</tr>
		</s:iterator>
		</tbody>
	</table>
	<br class="clear"/>
	<s:if test="securityList.size() > 30">
	<div style="posistion:static" id='pager<s:property value="type"/>' class="pager">
		<form>
			<img src="../images/jquery.tablesorter/addons/pager/icons/first.png" class="first"/>
			<img src="../images/jquery.tablesorter/addons/pager/icons/prev.png" class="prev"/>
			<input type="text" class="pagedisplay"/>
			<img src="../images/jquery.tablesorter/addons/pager/icons/next.png" class="next"/>
			<img src="../images/jquery.tablesorter/addons/pager/icons/last.png" class="last"/>
			<select class="pagesize">
				<option value="10">10</option>
				<option value="20">20</option>
				<option selected="selected" value="30">30</option>
				<option value="40">40</option>
			</select>
		</form>
	</div>
	</s:if>
</s:if>
<s:else>
	No Securities!
</s:else>
<div>
<br>
</div>
</div>
</body>
</html>
