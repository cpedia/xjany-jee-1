<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
	<meta name="rfa" content="vf_current"/>
    <meta name="tools" content="vf_current" />
    <meta name="submenu" content="tools"/>
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
		<title>RFA Analysis</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<SCRIPT src="../portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.js" type="text/javascript"></SCRIPT>
		<link href="../portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.css" rel="stylesheet" type="text/css" />
		
		
		<!-- These CSS are necessary START -->
		
		
		<!-- These CSS are necessary END -->
		
		<script type="text/javascript">
			
		$(document).ready(function(){
			$('#checkHistory').click(function(){
				
				if($('#checkHistory').attr('checked')==true){
					$('#startDate').attr({'disabled':true});
					$('#endDate').attr({'disabled':true});
				}else{
					$('#startDate').attr({'disabled':false});
					$('#endDate').attr({'disabled':false});
				}
				

			});
			
			addNewIndex('VFINX');
			addNewIndex('VGTSX');
			addNewIndex('VGSIX');
			addNewIndex('QRAAX');
			addNewIndex('BEGBX');
			addNewIndex('VBMFX');
			addNewIndex('CASH');
			
			
			jQuery("#symbol").suggest("../ajax/GetSecuritySuggestTxt.action",
										 { 	haveSubTokens: true, 
										 	onSelect: function(){}});
			
			var date_1=new Date();
			var s_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear()-1);
			var e_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear());
			$('#startDate').val(s_date);
			$('#endDate').val(e_date);
			
		});
		var indexCount=0;
		function addNewIndex(name){
			indexCount++;
			var $input=$(document.createElement("input"));
			$input.attr({type:"text"});
			$input.attr({name:"indexArray"});
			$input.attr({id:"index"+(indexCount)});
			if(name!="null") $input.attr({value:name});
			//$p.append($input);
			
			$("#indexParagraph").append($input);
			
			
			jQuery("#index"+(indexCount)).suggest("../ajax/GetSecuritySuggestTxt.action",
										 { 	haveSubTokens: true, 
										 	onSelect: function(){}});
		}
		
		function delIndex(){
		$("#index"+(indexCount)).remove();
		indexCount--;
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

		<s:property value="message"/>
		<s:form action="Main" method="post" id="mainForm" theme="simple">
			<fieldset>
				<legend><s:text name="basicInformation"/></legend>
				<p><label for="symbol" value="symbol"><s:text name="symbol"/></label><input type="text" id="symbol" name="symbol" value="DODBX"/></p>
				<p><label><s:text name="interval"/></label><input type="text" name="interval" value="12"/></p> 
			</fieldset>
				
			<fieldset id="index_table">
				<legend><s:text name="index"/></legend>
				<p><a href="#" onclick=addNewIndex('null')><s:text name="add.index"/></a><a href="#" onclick=delIndex()>Delete an Index</a></p>
				<br class="clear"/>
				<p id="indexParagraph"></p>
			</fieldset>
			
			
			<fieldset>
				<legend><s:text name="chooseDate"/></legend>	
				<p><label><s:text name="startingdate"/></label><input type="text" name="startDate" id="startDate" value="06/01/2008"/></p>
				<p><label><s:text name="enddate"/></label><input type="text" name="endDate" id="endDate" value="07/01/2008"/></p>
				
				<p><input type="checkbox" name="checkHistory" id="checkHistory"/><label><s:text name="check.history"/></label></p>
				<br>
							
			</fieldset>	
				
		 		<p><s:submit key="submit" id="save" ></s:submit></p>
		 	<br class="clear"/>
		</s:form>
	</body>
</html>
