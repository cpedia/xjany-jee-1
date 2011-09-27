<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<!-- new theme -->
		<link rel="stylesheet" href="../images/flora.all.css" type="text/css" media="screen" title="Flora (Default)">
		<script type="text/javascript" src="../images/jquery.ui/ui.core.packed.js"></script>
		<script type="text/javascript" src="../images/jquery.ui/ui.tabs.packed.js"></script>
		<script type="text/javascript" src="../images/fbborderlayout/fbborderlayout.js"></script>
		<script type="text/javascript" src="../images/jquery.dimensions.js"></script>
		<script type="text/javascript" src="../images/jquery.bgiframe.js"></script>
		<script type="text/javascript" src="../images/jquery.jdMenu/jquery.positionBy.js"></script>
		<script type="text/javascript" src="../images/jquery.jdMenu/jquery.jdMenu.js"></script>
		<link rel="stylesheet" href="../images/jquery.jdMenu/jquery.jdMenu.css" type="text/css" />
		
		<script src="../images/jquery.ui.dialog/ui.dialog.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.draggable.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.droppable.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.resizable.packed.js" type="text/javascript"></script>
		<title>Security's Daily Data</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />	
		<script type="text/javascript">
			$(function () {
				$.FBBorderLayout({
					spacing: 5,
					<s:if test="customizeRegion==null || customizeRegion.westRegionName==null">
					west_collapsable: false,
					west_collapsed:true,
					</s:if>
					<s:else> 
					west_width: "<s:property value='customizeRegion.westWidthStr'/>",
					west_collapsable: true,
					</s:else> 
					<s:if test="customizeRegion==null || customizeRegion.southRegionName==null">
					south_collapsable: false,
					south_collapsed:true,
					</s:if>
					<s:else>
					south_width: "<s:property value='customizeRegion.southHeightStr'/>",
					south_collapsable: true,
					</s:else> 
					<s:if test="customizeRegion==null || customizeRegion.eastRegionName==null">
					east_collapsable: false,
					east_collapsed:true,
					</s:if>
					<s:else>
					east_width: "<s:property value='customizeRegion.eastWidthStr'/>",
					east_collapsable: true,
					</s:else>
					north_collapsed:false,
					north_collapsable:false
				});
				
			});
		
		<!-- Initialize BorderLayout END-->
		/*
		$(document).ready(function(){
		    $('.fbbl_north').load("../header.jsp");
		  });
		*/
		</script>
		<style type="text/css">
		table,input,button,combox,select,textarea{
		font-family: Arial, Helvetica, sans-serif;
		font-size: 12px;
		}
		
		<!-- These CSS are necessary START -->
		body{
		background:#DFE8F6;
		}
		.fbbl_north,
		.fbbl_south,
		.fbbl_east,
		.fbbl_west,
		.fbbl_center {
		 padding: 10px;
		 border: 1px solid #ccc;
		 background:#FffFff;
		}
		.fbbl_north{
			height:90px;
		}
		
		<!-- These CSS are necessary END -->
		</style>	
	</head>
	<body>
		<div class="fbbl_north">
			<s:include value="../../header.jsp"></s:include>
		</div>	

		<div class="fbbl_center">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main" id="url_main" namespace="/jsp/security" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Security Manager</s:a>				
			</td>		
			<td width="15%">
						<s:url action="View.action" id="url_view" namespace="/jsp/fundcenter" includeParams="none">
							<s:param name="symbol" value="symbol"/>
						</s:url>
						<s:a href="%{url_view}"><s:property value="symbol"/></s:a>				
			</td>
			<td width="15%">
				<s:url action="Main" id="url_dailydata" namespace="/jsp/security/dailydata" includeParams="none">
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_dailydata}">Daily Datas</s:a>				
			</td>		
			<td width="15%">
				<s:url action="Save" id="url_create" namespace="/jsp/security/dailydata" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_create}">Create a Daily Data</s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Search Results</p>
		<table width="100%">
			<tr>
				<td>
					<s:form action="Search.action" namespace="/jsp/security/dailydata" theme="simple">
						Date <s:textfield name="date" value="%{date}"></s:textfield>
						<s:hidden name="securityID" value="%{securityID}"></s:hidden>
						<s:submit value="GO"></s:submit>
					</s:form>
				</td>
			</tr>
		</table>		
		
		
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Date
				</td>	
				<td>
					Split
				</td>
				<td>
					Dividend
				</td>	
				<td>
					EPS
				</td>
				<td>
					MarketCap
				</td>
				<td>
					PE
				</td>	
				<td>														
					Close
				</td>
				<td>
					Open
				</td>
				<td>
					High
				</td>	
				<td>
					low
				</td>
				<td>
					AdjClose
				</td>
				<td>
					Volume
				</td>	

				<td>
					Return Dividend
				</td>
				<td>
					Security ID
				</td>	
				<td>
					Turnover Rate
				</td>
				<td>
					NAV
				</td>						
				<td>
					AdjNAV
				</td>		
				<td>
					Remove
				</td>																	
			</tr>		
			<s:iterator value="#request.dailydatas.items">
				<tr>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/security/dailydata">
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="ID"/></s:a>					
						
					</td>
					<td>
						<s:property value="date"/>
					</td>

					<td>
						<s:property value="split"/>
					</td>
					<td>
						<s:property value="dividend"/>
					</td>	
					<td>
						<s:property value="EPS"/>
					</td>
					<td>
						<s:property value="marketCap"/>
					</td>
					<td>
						<s:property value="PE"/>
					</td>		
					<td>													
						<s:property value="close"/>
					</td>
					<td>
						<s:property value="open"/>
					</td>
					<td>
						<s:property value="high"/>
					</td>	
					<td>
						<s:property value="low"/>
					</td>
					<td>
						<s:property value="adjClose"/>
					</td>
					<td>
						<s:property value="volume"/>
					</td>	
					<td>
						<s:property value="returnDividend"/>
					</td>
					<td>
						<s:property value="securityID"/>
					</td>	
					<td>
						<s:property value="turnoverRate"/>
					</td>
					<td>
						<s:property value="NAV"/>
					</td>						
					<td>
						<s:property value="adjNAV"/>
					</td>	
				
					
					
					
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/security/dailydata">
							<s:param name="securityID" value="securityID"></s:param>
							<s:param name="dailydataID" value="ID"></s:param>	
							<s:param name="action">delete</s:param>						
						</s:url>
						<s:a href="%{urladdr}">Remove</s:a>
						
					</td>
				</tr>
			</s:iterator>

		</table>
		<s:date name="date" format="MM/dd/yyyy" id="date_string"/>
		
		<center>
		Change to
			<s:url action="DailydataSearch" id="url_first" namespace="/jsp/security">
				<s:param name="startIndex" value="#request.dailydatas.firstParameter.startIndex"></s:param>
				<s:param name="date" value="%{date_string}"></s:param>
				<s:param name="securityID" value="%{securityID}"></s:param>
			</s:url>
							
			<s:a href="%{url_first}" theme="simple">
				First
			</s:a>	
							
			<s:iterator value="#request.dailydatas.parameters10" id="parameter" status="st">
				<s:url action="DailydataSearch" id="pageurl" namespace="/jsp/security">
					<s:param name="startIndex" value="#parameter.startIndex"></s:param>
					<s:param name="date" value="%{date_string}"></s:param>
					<s:param name="securityID" value="%{securityID}"></s:param>
				</s:url>
				<s:if test="#request.dailydatas.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
				<s:a href="%{pageurl}" theme="simple">
					<s:property value="#parameter.startIndex/#request.dailydatas.pageSize+1"/>
				</s:a>
				<s:if test="#request.dailydatas.startIndex==#parameter.startIndex"></class></s:if>	
			</s:iterator>
			
			<s:url action="DailydataSearch" id="url_last" namespace="/jsp/security">
				<s:param name="startIndex" value="#request.dailydatas.lastParameter.startIndex"></s:param>
				<s:param name="date" value="%{date_string}"></s:param>
				<s:param name="securityID" value="%{securityID}"></s:param>
			</s:url>
							
			<s:a href="%{url_last}" theme="simple">
				Last
			</s:a>	
		<s:url action="Search" id="url_m" includeParams="none" namespace="/jsp/security/dailydata">
		</s:url>					
		Page
		<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
			<input type="text" name="startIndex" value=""/>
			<input type="hidden" name="pageSize" value='<s:property value="#request.dailydatas.pageSize"/>'/>
			<s:hidden name="securityID" value="securityID"></s:hidden>
			<s:hidden name="date" value="%{date}"></s:hidden>
			<input type="submit" value="Go"/>
		<form>
		</center>
		</div>
	<div class="fbbl_east">
	<s:if test="customizeRegion!=null && customizeRegion.eastRegionName!=null">
		<s:url id="east_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.eastRegionName'/></s:param>
		</s:url>
		<iframe id="east" scrolling="yes" width="100%" height="99%" src='<s:property value="east_url"/>'></iframe>
	</s:if>
	</div>
	<div class="fbbl_west">
	<s:if test="customizeRegion!=null && customizeRegion.westRegionName!=null">
		<s:url id="west_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.westRegionName'/></s:param>
		</s:url>
		<iframe id="west" scrolling="yes" width="100%" height="99%" src='<s:property value="west_url"/>'></iframe>
	</s:if>
	</div>
	<div class="fbbl_south">
	<s:if test="customizeRegion!=null && customizeRegion.southRegionName!=null">
		<s:url id="south_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.southRegionName'/></s:param>
		</s:url>
		<iframe id="south" scrolling="yes" width="100%" height="99%" src='<s:property value="south_url"/>'></iframe>
	</s:if>	
	</div>	
	</body>
</html>
