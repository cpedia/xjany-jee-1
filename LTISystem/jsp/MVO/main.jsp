<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
	<head>
	   <meta name="mvo" content="vf_current"/>
       <meta name="tools" content="vf_current" />
       <meta name="submenu" content="tools"/>
		<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
		<!-- new theme -->
		<title>MVO Main Page</title>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<SCRIPT src="../portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.js" type="text/javascript"></SCRIPT>
		<link href="../portfolio/images/jquery.ajaxAutoComplete/jquery.suggest.css" rel="stylesheet" type="text/css" />
		
		
		<!-- These CSS are necessary START -->
		
		
		<!-- These CSS are necessary END -->

		<script type="text/javascript">
			
		$(document).ready(function(){

			addNewAssetBanchmark('VFINX');
			addNewAssetBanchmark('VGTSX');
			addNewAssetBanchmark('VGSIX');
			addNewAssetBanchmark('QRAAX');
			addNewAssetBanchmark('BEGBX');
			addNewAssetBanchmark('VBMFX');
			addNewAssetBanchmark('CASH');
			//06/01/2008
			var date_1=new Date();
			var s_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear()-1);
			var e_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear());
			$('#startDate').val(s_date);
			$('#endDate').val(e_date);
			
			getStartDate();
		});
		
		
		function caculate(){
			$('#result').html("Please wait...");
			$.ajax({
			   type: 'POST',
			   url: '../mvo/Main.action?includeHeader=false',
			   dataType: "html", 
			   data: $('#mainForm').serialize(),
			   success: function(msg){
			   	 //alert(msg);
			     $('#result').html(msg);
			   },
			   error: function(msg){
			   	 $('#result').html("Error, please try it again.");
			   }
			 });		
		}
		
		var banchmarkCount=0;
		function addNewAssetBanchmark(name){
			banchmarkCount++;
			
			var $pline=$(document.createElement("p"));
			$pline.attr({id:"pline"+banchmarkCount});
			
			var $input=$(document.createElement("input"));
			$input.attr({type:"text"});
			$input.attr({name:"banchmarkArray"});
			$input.attr({id:"banchmark"+(banchmarkCount)});
			if(name!="null") $input.attr({value:name});
			$pline.append($input);
			
			var $lower=$(document.createElement("input"));
			$lower.attr({type:"text"});
			$lower.attr({name:"lower"});
			$lower.attr({id:"lower"+banchmarkCount});
			$lower.attr({value:"0"});
			$pline.append($lower);
			
			var $upper=$(document.createElement("input"));
			$upper.attr({type:"text"});
			$upper.attr({name:"upper"});
			$upper.attr({id:"upper"+banchmarkCount});
			$upper.attr({value:"1"});
			$pline.append($upper);
			
			var $expected=$(document.createElement("input"));
			$expected.attr({type:"text"});
			$expected.attr({name:"expected"});
			$expected.attr({id:"expected"+banchmarkCount});
			$expected.attr({value:" "});
			$pline.append($expected);
			
			var $removebutton=$(document.createElement("input"));
			$removebutton.attr({type:"button"});
			$removebutton.attr({id:"button"+banchmarkCount});
			$removebutton.attr({value:"Remove"});
			
			$pline.append($removebutton);
			//alert(banchmarkCount);
			$("#banchmarkParagraph").append($pline);
			
			$("#button"+banchmarkCount).click(function(){
				$("#"+$pline.attr("id")).remove();
				getStartDate();
			})
			
			jQuery("#banchmark"+(banchmarkCount)).suggest("../ajax/GetSecuritySuggestTxt.action",
										 { 	haveSubTokens: true, 
										 	onSelect: function(){getStartDate();}});
										 	
		}

		
		
		
		<!-- Initialize BorderLayout END-->

		function fixNum(num)
		{
		num.toFixed(3);
		}
		function getElementsByName(tag,eltname){
			  var elts=document.getElementsByTagName(tag);
			  var count=0;
			  var elements=[];
			  for(var i=0;i<elts.length;i++){
			     if(elts[i].getAttribute("name")==eltname){
			        elements[count++]=elts[i];
			     }
			  }
			  return elements;
		} 
		function getStartDate(){
			var benchmarkArray="";
			var elements=getElementsByName("input","banchmarkArray");
			for(i=0;i<elements.length;i++){
				benchmarkArray+=elements[i].value;
				if(i!=elements.length-1)benchmarkArray+=",";
			}
			$("#tips").load("../ajax/GetValidStartDate.action?benchmarkArray="+benchmarkArray+"&&includeHeader=false");
		}
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
		}
		input:focus{
		background:#ffc;
		}
		input[type="text"]{
		border-top:2px solid #999;
		border-left:2px solid #999;
		border-bottom:1px solid #ccc;
		border-right:1px solid #ccc;
		width:150px;
		}
		label{
		float:left;
		width:10em;
		}
		#save{
		float:left;
		}
		.clear{
		clear:both;
		}
		#banchmarkParagraph input{
		padding:1px;
		margin:10px;
		}

		#banchmark_table a:link,#banchmark_table a:visited,#caculate a:link,#caculate a:visited{
		display:block;
		width:8em;
		margin:10px;
		color:#000;
		text-decoration:none;
		background-color:#94b8e9;
		border:1px solid black;
		text-align:center;
		float:left;
		}
		#banchmark_table a:hover,#caculate a:hover{
		background-color:#369;
		color:#fff;
		}
		
		</style>
	</head>
	<body>
	
		<s:property value="message"/>
		<s:form action="Main" method="post" id="mainForm" theme="simple" namespace="/jsp/mvo">

			
			<fieldset>
				<legend><s:text name="Parameters"/></legend>	
				<p><label><s:text name="startingdate"/></label><input type="text" name="startDate" id="startDate" value="06/01/2008"/><span id="tips"></span></p>
				<p><label><s:text name="enddate"/></label><input type="text" name="endDate" id="endDate" value="07/01/2008"/></p>
				<p><label><s:text name="Time Unit"/></label><s:select theme="simple" name="unit" list="{'weekly','monthly','yearly'}"></s:select></p>
				<p>
					<label><s:text name="Risk aversion factor"/></label>
					<s:select id="RAF_selector" value="0.5"  name="RAF" theme="simple" list="#{0.5:'very aggressive', 1:'aggressive', 2:'moderate', 4:'conservative', 8:'very conservative', -1:'others'}" onchange="otherSelected()"></s:select>
					<s:textfield id="RAF_textfield" value="0.5" name="RAF" theme="simple" disabled="true" ></s:textfield>
					<script type="text/javascript">
						function otherSelected(){
							//alert("hello!");
							value = $("#RAF_selector").val();
							if(value == -1){
								$("#RAF_textfield").attr({"disabled": false});
								$("#RAF_selector").attr({"name": ''});
							}
							if(value != -1){
								$("#RAF_textfield").attr({"disabled": true});
								$("#RAF_textfield").attr({"value":value});
								$("#RAF_selector").attr({"name": 'RAF'});
							}
						}
					</script>				
				</p>
			</fieldset>	


			<fieldset id="banchmark_table">
				 <legend><s:text name="Asset Securities"/></legend>
				<p><a href="#" onclick=addNewAssetBanchmark('null')>Add Security</a><label class="header">Lower limit</label><label class="header">Upper limit</label><label class="header">Expected Excessive Returns</label></p>
				<br class="clear"/>
				<p id="banchmarkParagraph"></p>
			</fieldset>			
				
		 		<p id='caculate'><a href="#" onclick=caculate()>Calculate</a></p>
		 	<br class="clear"/>
		</s:form>
		<div id="result"></div>
	
	
	</body>
</html>
