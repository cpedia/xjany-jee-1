<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<!-- new theme -->
		<link rel="stylesheet" href="../images/flora.all.css" type="text/css" media="screen" title="Flora (Default)">
		<script type="text/javascript" src="../images/jquery.ui/ui.core.packed.js"></script>
		<script type="text/javascript" src="../images/jquery.ui/ui.tabs.packed.js"></script>
		
		<script src="../images/jquery.ui.dialog/ui.dialog.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.draggable.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.droppable.packed.js" type="text/javascript"></script>
		<script src="../images/jquery.ui.dialog/ui.resizable.packed.js" type="text/javascript"></script>
		<title>Security's Daily Data</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
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
		#vf_content-wrap {
         padding: 10px;
        }
		<!-- These CSS are necessary START -->
		body{
		background:#DFE8F6;
		}
		
		<!-- These CSS are necessary END -->
		</style>	
	</head>
	<body>
		<div class="fbbl_center">
		<table class="nav" width="100%">
			<td width="15%">
				<s:url action="Main" id="url_main" namespace="/jsp/security" includeParams="none">
				</s:url>
				<s:a href="%{url_main}"><s:text name="security.manager"></s:text></s:a>				
			</td>		
			<td width="15%">
				<s:url action="View.action" id="url_view" namespace="/jsp/fundcenter" includeParams="none">
					<s:param name="symbol" value="symbol"/>
				</s:url>
				<s:a href="%{url_view}"><s:property value="symbol"/></s:a>				
			</td>
			<td width="15%">
				<s:url action="DailydataMain" id="url_dailydata" namespace="/jsp/security" includeParams="none">
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_dailydata}"><s:text name="securiy.daily.data"></s:text></s:a>				
			</td>		
			<td width="15%">
				<s:url action="DailydataSave" id="url_create" namespace="/jsp/security" includeParams="none">
					<s:param name="action">create</s:param>
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_create}"><s:text name="security.create.daily.data"></s:text></s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p><s:text name="startingdate"></s:text>: <s:property value="securityStartingDate"/> <s:text name="enddate"></s:text>: <s:property value="securityEndDate"/></p>
	<p class="subtitle"><s:text name="securiy.daily.data"></s:text></p>
		<table width="100%">
			<tr>
				<td>
					<s:url action="DailydataSearch" id="url_s" includeParams="none">
					</s:url>					
					<s:form namespace="/jsp/security" theme="simple">
						<label><s:text name="date"></s:text></label> <s:textfield theme="simple" name="date"></s:textfield>
						<s:hidden name="securityID" value="%{securityID}"></s:hidden>
						<s:submit value="go" theme="simple" action="DailydataSearch"></s:submit>
						
					</s:form>
				</td>
			</tr>
		</table>		
		
		
		<table width="100%">
			<tr class="trHeader">
				<td>
					<s:text name="security.ID"></s:text>
				</td>
				<td>
					<s:text name="date"></s:text>
				</td>	
				<td>
					<s:text name="security.split"></s:text>
				</td>
				<td>
					<s:text name="dividend"></s:text>
				</td>	
				<td>
					<s:text name="security.eps"></s:text>
				</td>
				<td>
					<s:text name="security.market.cap"></s:text>
				</td>
				<td>
					<s:text name="security.pe"></s:text>
				</td>
				<td>															
					<s:text name="security.close"></s:text>
				</td>
				<td>
					<s:text name="security.open"></s:text>
				</td>
				<td>
					<s:text name="security.high"></s:text>
				</td>	
				<td>
					<s:text name="security.low"></s:text>
				</td>
				<td>
					<s:text name="security.adjClose"></s:text>
				</td>
				<td>
					<s:text name="security.volume"></s:text>
				</td>	
				<td>
					<s:text name="security.return.dividend"></s:text>
				</td>
				<td>
					<s:text name="security.ID"></s:text>
				</td>	
				<td>
					<s:text name="security.turnover.rate"></s:text>
				</td>
				<td>
					<s:text name="security.nav"></s:text>
				</td>						
				<td>
					<s:text name="security.adjNAV"></s:text>
				</td>		
				<td>
					<s:text name="operation"></s:text>
				</td>																	
			</tr>	
			<s:iterator value="#request.dailydatas.items">
				<tr>
					<td>
						<s:url action="DailydataSave" id="urladdr" namespace="/jsp/security">
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
						<s:url action="Save" id="urladdr" namespace="/jsp/security/dailydata">
							<s:param name="securityID" value="securityID"></s:param>
							<s:param name="dailydataID" value="ID"></s:param>	
							<s:param name="action">delete</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:text name="remove"></s:text></s:a>
						
					</td>
				</tr>
			</s:iterator>

		</table>
		<center>
		<s:text name="change.to"></s:text>
			<s:url action="DailydataMain" id="url_first" namespace="/jsp/security">
				<s:param name="startIndex" value="#request.dailydatas.firstParameter.startIndex"></s:param>
				<s:param name="securityID" value="#request.securityID"></s:param>
			</s:url>
							
			<s:a href="%{url_first}">
				<s:text name="first"></s:text>
			</s:a>	
							
			<s:iterator value="#request.dailydatas.parameters10" id="parameter" status="st">
				<s:url action="DailydataMain" id="pageurl" namespace="/jsp/security">
					<s:param name="startIndex" value="#parameter.startIndex"></s:param>
					<s:param name="securityID" value="#request.securityID"></s:param>
				</s:url>
				<s:if test="#request.dailydatas.startIndex==#parameter.startIndex">
					<s:a href="%{pageurl}" cssStyle="background-color:#2894FF;color:white;font-style:italic;font-variant:small-caps;font-weight:bold;">
						<s:property value="#parameter.startIndex/#request.dailydatas.pageSize+1"/>
					</s:a>
				</s:if>	
				<s:else>
					<s:a href="%{pageurl}">
						<s:property value="#parameter.startIndex/#request.dailydatas.pageSize+1"/>
					</s:a>
				</s:else>
			</s:iterator>
			
			<s:url action="DailydataMain" id="url_last" namespace="/jsp/security">
				<s:param name="startIndex" value="#request.dailydatas.lastParameter.startIndex"></s:param>
				<s:param name="securityID" value="#request.securityID"></s:param>
			</s:url>
							
			<s:a href="%{url_last}">
				<s:text name="last"></s:text>
			</s:a>	
		<s:url action="DailydataMain" id="url_m" includeParams="none" namespace="/jsp/security">
		</s:url>					
		<s:text name="page"></s:text>
		<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
			<input type="text" name="startIndex" value=""/>
			<input type="hidden" name="pageSize" value='<s:property value="#request.dailydatas.pageSize"/>'/>
			<s:hidden name="securityID" theme="simple"></s:hidden>
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
