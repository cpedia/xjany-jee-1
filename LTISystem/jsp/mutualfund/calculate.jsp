<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>
<html>
<s:set name="pageTitle" value="symbol"></s:set>

<style type="text/css">
fieldset{
	margin:1em 0;
	padding:1em;
	border:1px solid #ccc;
	background:#f8f8f8;
}
legend{
	font-weight:bold;
	color:black;
	FONT-FAMILY: Arial;
	font-size: 12pt;
}
input:focus{
	background:#ffc;
}
input[type="text"]{
	border-top:2px solid #999;
	border-left:2px solid #999;
	border-bottom:1px solid #ccc;
	border-right:1px solid #ccc;
	width:10em;
	padding:0;
	margin:0;
}
#basic-wrap label{
	float:left;
	width:8em;
	font-family: Arial;
	font-size: 12pt;
	padding:0;
	margin:0;
}
#basic-wrap span{
	width:55%;
	float:right;
	text-align:left;
	padding:0;
	margin:0;
}
#basic-wrap p{
	padding:0;
	margin-top:20px;
}
#save{
	float:left;
}
.clear{
	clear:both;
	padding:0;
	margin:0;
}
#indexParagraph input{
	padding:1px;
	margin-left:5px;
	margin-top:5px;
	width:10em;
}
#indexParagraph input[type="button"]{
	padding:1px;
	margin-left:5px;
	margin-top:5px;
	width:7em;
}
#index_table{
	text-align:left;
}
#index_table a:link,#index_table a:visited{
	display:block;
	padding:1px;
	margin-left:10px;
	width:8em;
	color:#000;
	text-decoration:none;
	background-color:#94b8e9;
	border:1px solid black;
	text-align:center;
	float:left;
	font-size: 10pt;
	font-family: Arial;
}


#index_table a:hover{
	background-color:#369;
	color:#fff;
}
#input-info{
	font-size:11pt;
	width:100%;
	text-align:center;
	padding:0;
	margin:0;
}
#basic-wrap{
	width:40%;
	float:left;
	padding:0;
	margin:0;
}
#index-wrap{
	width:58%;
	float:right;
	padding:0;
	margin:0;
}
#indexParagraph{
	padding:0;
	margin:0;
	text-align:left;
	display:inline;
}
</style>	
<script type="text/javascript">
$(document).ready(function(){
	var url = window.location.href;
	if(url.indexOf(".action") == -1)
		initialCalculate();
	else
	{
		<s:if test="indexes != null">
			<s:iterator value="indexes" status="st">
				var index_name = '<s:property/>';
				var lower = <s:property value="%{lowers[#st.index]}"/>;
				var upper = <s:property value="%{uppers[#st.index]}"/>;
				addNewIndex(index_name, lower, upper);
			</s:iterator>
		</s:if>
		
	}
});
var indexCount=0;
function addNewIndex(name, lower, upper ){
	indexCount++;			
	
	var $pline=$(document.createElement("p"));
	$pline.attr({id:"pline"+indexCount});
	$pline.addClass('index');
	var $input=$(document.createElement("input"));
	$input.attr({type:"text"});
	$input.attr({name:"indexArray"});
	$input.attr({id:"index"+(indexCount)});
	if(name!="null") $input.attr({value:name});
	//$p.append($input);			
	$("#indexParagraph").append($input);
	$pline.append($input);
	
	var $lower=$(document.createElement("input"));
	$lower.attr({type:"text"});
	$lower.attr({name:"lowerArray"});
	$lower.attr({id:"lower"+indexCount});
	$lower.attr({value:lower});
	$("#indexParagraph").append($lower);
	$pline.append($lower);
	
	var $upper=$(document.createElement("input"));
	$upper.attr({type:"text"});
	$upper.attr({name:"upperArray"});
	$upper.attr({id:"upper"+indexCount});
	$upper.attr({value:upper});
	$("#indexParagraph").append($upper);
	$pline.append($upper);
	
	var $removebutton=$(document.createElement("input"));
	$removebutton.attr({type:"button"});
	$removebutton.attr({id:"button"+indexCount});
	$removebutton.attr({value:"Remove"});
	$pline.append($removebutton);
	
	$("#indexParagraph").append($pline);
				
	$("#button"+indexCount).click(function(){
		$("#"+$pline.attr("id")).remove();
	})
	
	var index_height = $("#index_array").height();
	var basic_height = $("#basic_table").height();
	if(basic_height != index_height){
		//alert(index_height);
		$("#basic_table").css("height",index_height);
		//alert($("#basic_table").height());
	}
	
	jQuery("#index"+(indexCount)).suggest("../ajax/GetSecuritySuggestTxt.action",
								 { 	haveSubTokens: true, 
								 	onSelect: function(){}});
	
	//alert(height);
}	
function initialCalculate(){
	$('#checkHistory').click(function(){
		
		if($('#checkHistory').attr('checked')==true){
			$('#startDate').attr({'disabled':true});
			$('#endDate').attr({'disabled':true});
		}else{
			$('#startDate').attr({'disabled':false});
			$('#endDate').attr({'disabled':false});
		}
		

	});
	
	addNewIndex('VFINX', 0, 1);
	addNewIndex('VGTSX', 0, 1);
	addNewIndex('VGSIX', 0, 1);
	addNewIndex('QRAAX', 0, 1);
	addNewIndex('BEGBX', 0, 1);
	addNewIndex('VBMFX', 0, 1);
	addNewIndex('CASH', 0, 1);
	
	var index_height = $("#index_array").height();
	var basic_height = $("#basic_table").height();
	if(basic_height < index_height){
		alert(index_height);
		$("#basic_table").css("height",index_height);
		alert($("#basic_table").height());
	}
	
	jQuery("#symbol").suggest("../ajax/GetSecuritySuggestTxt.action",
								 { 	haveSubTokens: true, 
								 	onSelect: function(){}});
	
	var date_1=new Date();
	var s_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear()-1);
	var e_date=(date_1.getMonth()+1)+"/"+date_1.getDate()+"/"+(date_1.getFullYear());
	$('#startDate').val(s_date);
	$('#endDate').val(e_date);
	$("#symbol").val("DODBX");
	$("#interval").val("12");
}
</script>
<s:property value="message"/>
<div id="input-info">
	<s:form action="Result" method="post" id="mainForm" theme="simple" namespace="/jsp/mutualfund">
		<div id="basic-wrap">
			<fieldset>
				<legend><s:text name="basicInformation"/></legend>
				<div id="basic_table">
					<p><label for="symbol" value="symbol"><s:text name="symbol"/></label><span><input type="text" id="symbol" name="symbol" value='<s:property value="symbol"/>'/></span></p>
					<br class="clear"/>
					<p><label><s:text name="interval"/></label><span><input type="text" id="interval" name="interval" value='<s:property value="interval"/>'/></span></p> 
					<br class="clear"/>
					<p><label><s:text name="startingdate"/></label><span><input type="text" name="startDate" id="startDate" value='<s:property value="startDate"/>'/></span></p>
					<br class="clear"/>
					<p><label><s:text name="enddate"/></label><span><input type="text" name="endDate" id="endDate" value='<s:property value="endDate"/>'/></span></p>							
					<br class="clear"/>
					<p><label><s:text name="check.history"/></label><span><input type="checkbox" name="checkHistory" id="checkHistory" style="padding:0;margin:0" <s:if test="checkHistory==true">checked</s:if>/></span></p>
					<br class="clear"/>
					<p><label><s:text name="allow.negative.value"></s:text></label><span><s:select list="{false,true}" name="allowNegative" theme="simple" cssStyle="padding:0;margin:0"></s:select></span></p>
				</div>
			</fieldset>
		</div>	
		<div id="index-wrap">
			<fieldset id="index_table">
				<legend><s:text name="index"/></legend>
				<div id="index_array">
					<p style="padding:1px;margin-top:2px;margin-bottom:2px"><a href="javascript:void(0)" onclick=addNewIndex('null')>Add one Index</a><label style="width:10em;margin-left:5px">Lower Limit</label><label style="width:10em;margin-left:5px">Upper Limit</label></p>
					<br class="clear"/>
					<p id="indexParagraph">
					</p>
				</div>
			</fieldset>
		</div>
		<br class="clear"/>	
	 	<div style="margin-top:10px"><s:submit cssStyle="padding:0;margin:0" key="submit" id="save" theme="simple"></s:submit></div>
	</s:form>
</div>
<br class="clear" />
<div id="result-wrap" style="width:100%">
<s:if test='action.equals("result")'>
	<s:action name="Main" namespace="/jsp/mutualfund" executeResult="true">
		<s:param name="symbol" value="symbol"></s:param>
		<s:param name="interval" value="interval"></s:param>
		<s:param name="indexArray" value="indexArray"></s:param>
		<s:param name="startDate" value="startDate"></s:param>
		<s:param name="endDate" value="endDate"></s:param>
		<s:param name="checkHistory" value="checkHistory"></s:param>
		<s:param name="allowNegative" value="allowNegative"></s:param>
	</s:action>
</s:if>
</div>	
	</body>
</html>

