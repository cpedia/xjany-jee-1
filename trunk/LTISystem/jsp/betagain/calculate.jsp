<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<LINK href="../images/style.css" type=text/css rel=stylesheet>
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
		<title>Beta Gains Analysis</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<SCRIPT src="../portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.js" type="text/javascript"></SCRIPT>
		<link href="../portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		table,input,button,combox,select,textarea{
		font-family: Arial, Helvetica, sans-serif;
		font-size: 12px;
		}
		
		<!-- These CSS are necessary START -->

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
		$(document).ready(function(){
					
			addNewIndex('VFINX');
			addNewIndex('VGTSX');
			addNewIndex('VGSIX');
			addNewIndex('QRAAX');
			addNewIndex('BEGBX');
			addNewIndex('VBMFX');			
			
			jQuery("#symbol").suggest("../ajax/GetSecuritySuggestTxt.action",
										 { 	haveSubTokens: true, 
										 	onSelect: function(){}});
			
			var date_1=new Date();
			var s_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear()-1);
			var e_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear());
			$('#startDate').val(s_date);
			$('#endDate').val(e_date);
			
			
			var $address=$(document.createElement("input"));
			$address.attr({type:"hidden"});
			$address.attr({name:"address"});
			$address.attr({value:address});
			$("#indexParagraph").append($address);
			
			var $port=$(document.createElement("input"));
			$port.attr({type:"hidden"});
			$port.attr({name:"port"});
			$port.attr({value:port});
			$("#indexParagraph").append($port);
			
		});
		var port=document.location.port;
		if(port=="")port='80';
		var address=location.hostname;
		
		
		var indexCount=0;
		function addNewIndex(name){
			indexCount++;			
			
			var $pline=$(document.createElement("p"));
			$pline.attr({id:"pline"+indexCount});
			
			var $input=$(document.createElement("input"));
			$input.attr({type:"text"});
			$input.attr({name:"indexArray"});
			$input.attr({id:"index"+(indexCount)});
			if(name!="null") $input.attr({value:name});
			//$p.append($input);			
			$("#indexParagraph").append($input);
			$pline.append($input);
			
			var $type=$(document.createElement("input"));
			$type.attr({type:"text"});
			$type.attr({name:"type"});
			$type.attr({id:"type"+indexCount});
			$type.attr({value:"1"});
			$("#indexParagraph").append($type);
			$pline.append($type);
						
			var $removebutton=$(document.createElement("input"));
			$removebutton.attr({type:"button"});
			$removebutton.attr({id:"button"+indexCount});
			$removebutton.attr({value:"Remove"});
			$pline.append($removebutton);
			
			$("#indexParagraph").append($pline);
						
			$("#button"+indexCount).click(function(){
				$("#"+$pline.attr("id")).remove();
			})
			
			
			jQuery("#index"+(indexCount)).suggest("../ajax/GetSecuritySuggestTxt.action",
										 { 	haveSubTokens: true, 
										 	onSelect: function(){}});
		}	
		
		
		<!-- Initialize BorderLayout END-->
		/*
		$(document).ready(function(){
		    $('.fbbl_north').load("../header.jsp");
		  });
		*/
		</script>
		<style>
		fieldset{
			margin:1em 0;
			padding:1em;
			border:1px solid #ccc;
			background:#f8f8f8;
		}
		legend{
			font-weight:bold;
			font-color:black;
			FONT-FAMILY: Arial;
			font-size: 14px;
		}
		input:focus{
			background:#ffc;
		}
		input[type="text"]{
			border-top:2px solid #999;
			border-left:2px solid #999;
			border-bottom:1px solid #ccc;
			border-right:1px solid #ccc;
		}
		label{
			float:left;
			width:10em;
			font-family: Arial;
			font-size: 14px;
		}
		
		#checkHistory{
			float:left;
			width:2em;
		}
		#save{
			float:left;
		}
		.clear{
			clear:both;
		}
		#indexParagraph input{
			padding:1px;
			margin:10px;
		}
		#index_table a:link,#index_table a:visited{
			display:block;
			width:8em;
			margin:10px;
			color:#000;
			text-decoration:none;
			background-color:#94b8e9;
			border:1px solid black;
			text-align:center;
			float:left;
			font-size: 14px;
			font-family: Arial;
		}
		#index_table a:hover{
			background-color:#369;
			color:#fff;
		}
		
		</style>
	</head>
	<body>
	<div class="fbbl_north">
		<s:include value="../header.jsp"></s:include>
	</div>
	<div class="fbbl_center" id="security_div">
		<s:property value="message"/>
		<s:form action="Main" method="post" id="mainForm" theme="simple">
			<fieldset>
				<legend><s:text name="Fund to Analysis"/></legend>
				<p><label for="symbol" value="symbol"><s:text name="symbol"/></label><input type="text" id="symbol" name="symbol" value="DODBX"/></p>
				<p><label><s:text name="interval(One Quarter)"/></label><input type="text" name="interval" value="64" readonly/></p>
				<p><label><s:text name="Gain Interval"/></label><input type="text" name="gainInterval" value="15"/></p> 
			</fieldset>
				
			<fieldset id="index_table">
				<legend><s:text name="Factors"/></legend>
				<p><a href="#" onclick=addNewIndex('null')>Add one Factor</a><label class="header"><a>Return(0) or Price(1)</a></label>
				<br class="clear"/>
				<p id="indexParagraph"></p>
			</fieldset>
			
			
			<fieldset>
				<legend><s:text name="chooseDate"/></legend>	
				<p><label><s:text name="startingdate"/></label><input type="text" name="startDate" id="startDate" value="06/01/2008"/></p>
				<p><label><s:text name="enddate"/></label><input type="text" name="endDate" id="endDate" value="07/01/2008"/></p>
				<p><s:select list="{'WLS','OLS'}" name="LSType" theme="simple">WLS or OLS?   </s:select></p>
				<p><s:select list="{false,true}" name="isSigmaOne" theme="simple">Sigma Is One?   </s:select></p>
				<br>
				
				
			</fieldset>	
				
		 		<p><s:submit key="submit" id="save" ></s:submit></p>
		 	<br class="clear"/>
		</s:form>
	</div>	
	<div class="fbbl_east">
	<s:if test='customizeRegion!=null && customizeRegion.eastRegionName!=null && !customizeRegion.eastRegionName.equals("Fixed")'>
		<s:url id="east_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.eastRegionName'/></s:param>
		</s:url>
		<iframe id="east" scrolling="yes" width="100%" height="99%" src='<s:property value="east_url"/>'></iframe>
	</s:if>
	</div>
	<div class="fbbl_west">
	<s:if test='customizeRegion!=null && customizeRegion.westRegionName!=null && !customizeRegion.westRegionName.equals("Fixed")'>
		<s:url id="west_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.westRegionName'/></s:param>
		</s:url>
		<iframe id="west" scrolling="yes" width="100%" height="99%" src='<s:property value="west_url"/>'></iframe>
	</s:if>
	</div>
	<div class="fbbl_south">
	<s:if test='customizeRegion!=null && customizeRegion.southRegionName!=null && !customizeRegion.southRegionName.equals("Fixed")'>
		<s:url id="south_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
			<s:param name="pageName"><s:property value='customizeRegion.southRegionName'/></s:param>
		</s:url>
		<iframe id="south" scrolling="yes" width="100%" height="99%" src='<s:property value="south_url"/>'></iframe>
	</s:if>	
	</div>	
	</body>
</html>
