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
		
		<title>Security's Daily Data</title>
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
				<s:url action="Save" id="url_view" namespace="/jsp/security" includeParams="none">
					<s:param name="action">view</s:param>
					<s:param name="securityID" value="securityID"/>
				</s:url>
				<s:a href="%{url_view}"><s:text name="daily.data.security"></s:text></s:a>				
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
				</s:url>
				<s:a href="%{url_create}"><s:text name="security.create.daily.data"></s:text></s:a>			
			</td>				
			<td>
			</td>
		</table>
	<p class="title"><s:property value="%{title}"/></p>
	<p class="subtitle">Edit Daily Data</p>
	<s:actionmessage/>			
	<s:form action="Save" method="post">
		<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<s:hidden name="action" value="%{action}"/>
			<s:hidden name="ID" value="%{ID}"/>
			<s:textfield name="securityID" key="security.ID" disabled="true"/>
			<s:textfield name="ID" key="securiy.daily.data.ID" disabled="true"/>
			<s:textfield name="date" key="date"/>
			<s:textfield name="split" key="security.split"/>
			<s:textfield name="dividend" key="security.dividend"/>
			<s:textfield name="EPS" key="security.eps"/>
			<s:textfield name="marketCap" key="security.market.cap"/>
			<s:textfield name="PE" key="security.pe"/>
			<s:textfield name="close" key="security.close"/>
			<s:textfield name="open" key="security.open"/>
			<s:textfield name="high" key="security.high"/>
			<s:textfield name="low" key="security.low"/>
			<s:textfield name="adjClose" key="security.adjClose"/>
			<s:textfield name="volume" key="security.volume"/>
			<s:textfield name="returnDividend" key="security.return.dividend"/>
			<s:textfield name="securityID" key="security.ID"/>
			<s:textfield name="turnoverRate" key="security.turnover.rate"/>
			<s:textfield name="NAV" key="security.nav"/>
			<s:textfield name="adjNAV" key="security.adjNAV"/>
 			<s:submit key="save"></s:submit>

	</s:form>
   	</div>
   	<!--  
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
	 -->	   	
	</body>
</html>
