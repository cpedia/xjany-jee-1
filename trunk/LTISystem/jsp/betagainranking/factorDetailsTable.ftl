[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
[#import "/jsp/lti_library_ftl.jsp" as lti]
<link rel="stylesheet" href="../images/jquery.tablesorter/style.css" type="text/css" />
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript" src="../images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
[#if factorBetaGainList??]
	<table id="factorDetailsTable_${symbol}" class="tablesorter" border="0" cellpadding="0" cellspacing="1">
		<thead>
			<tr>
				<th class="header">
					Factor
				</th>	
				<th class="header">
					Beta
				</th>
				<th class="header">
					Past 1 Month Gain
				</th>	
				<th class="header">
					Past 3 Months Gain
				</th>
				<th class="header">
					Past 6 Months Gain
				</th>	
				<th class="header">
					Past 1 year Gain
				</th>
			</tr>			
		</thead>
		<tbody>
		[#list factorBetaGainList as factorBetaGain]
			[#if factorBetaGain_index%2==0]
				<tr class='odd'>
			[/#if]
			[#if factorBetaGain_index%2==1]
				<tr class='even'>
			[/#if]
			<td>
				${factorBetaGain.getFactor()}
			</td>	
			<td>
				${factorBetaGain.getBeta()}
			</td>
			<td>
				${factorBetaGain.getOneMonth()}
			</td>
			<td>
				${factorBetaGain.getThreeMonth()}
			</td>
			<td>
				${factorBetaGain.getHalfYear()}
			</td>
			<td>
				${factorBetaGain.getOneYear()}
			</td>
		</tr>
		[/#list]
		</tbody>
	</table>
	<br class="clear"/>
	
	<script type="text/javascript"> 
	$(document).ready(function(){
		$("#factorDetailsTable_${symbol}")
		.tablesorter({
			widthFixed: true, 
			widgets: ['zebra']
		})
	}); 
	</script>	
[#else]
	[@s.actionerror/]
[/#if]