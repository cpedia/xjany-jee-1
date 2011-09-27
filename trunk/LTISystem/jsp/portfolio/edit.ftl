[#ftl]
<html>
<head>
<meta name="submenu" content="individualportfolio"/>
<meta name="id" content='${ID}'/>
<meta name="jqueryui" content="smoothness">
<script src="/LTISystem/jsp/template/gpl/editabletable.js"></script>
<script type="text/javascript" src="/LTISystem/jsp/images/tiny_mce/tiny_mce.js"></script>
<script>
tinyMCE.init({
    mode : "none",
    theme : "advanced",
    plugins : "save",
	theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,|,fontselect,fontsizeselect",
	theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,blockquote,link,unlink,anchor,image,|,forecolor,backcolor",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	save_onsavecallback : "save_for_mce"
});
$(function() {
	tinyMCE.execCommand('mceAddControl', false, "description");
	$('#btnPermission').click(function(){
		$('#permissionpanel').toggle();
	});
	$('#btnChange').click(function(){
		 $.ajax({
			   type: "Post",
			   url: "SaveGroups.action?includeHeader=false",
			   data:$("#permissionform").serialize(),
			   success: function(msg){
			     alert(msg );
			   }
		 });
	});
	
	$('#startingdate').datepicker();
	var arr=new Array('pre','post');
	var cssesarr=new Array('','','','width:100%');
	$('#parametertable').editabletable({name:'attributes',data:arr,csses:cssesarr});
	
});  	
function saveas(){
	if($('#name').val()==""||$('#name').val()==$('#oldname').val()){
		alert('Please input another name the new one.');
		return;
	}
	$('#operation').val('saveas');
	$('#_form').submit();
}

function remove(){
	if(confirm("Are you sure to delete this portfolio?")){
		$('#operation').val('delete');
		$('#_form').submit();
	}
}

function save(){
	$('#operation').val('');
	$('#_form').submit();
}
</script>
<style>
#parameterdiv,#permissionpanel {border:1px #cccccc solid;text-align:left}

#parameterdiv input,#permissionpanel input{border:1px #cccccc solid;width:95%}
</style>
</head>
<body>
[#if admin]
<a href="javascript:void(0)" class="uiButton" id="btnPermission">Permissions</a>
<div id="permissionpanel" style="display:none">
<form id="permissionform">
<table width=100%>
	<input type="hidden" name="ID" value="${ID}">
	<tr>
		<td>
			Delay Groups
		</td>
		<td width="800">
			<input name="delayGroup" value="${delayGroup}" style="width:100%">
		</td>
	</tr>
	<tr>
		<td>
			Realtime Groups
		</td>
		<td>
			<input name="realtimeGroup" value="${realtimeGroup}" style="width:100%">
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<a href="javascript:void(0)" class="uiButton" id="btnChange">Change</a>
			Group Name: MPIQ,MPIQ_B,ANONYMOUS,IS_AUTHENTICATED_ANONYMOUSLY,IS_AUTHENTICATED_REMEMBERED,VF_B
		</td>
	</tr>
</table>
</form>
</div>
[/#if]



<!--<a class="uiButton" href="EditHolding.action?ID=${ID}">Edit Holding</a>-->
<form action="Save.action" method="post" id="_form">
<table width=100%>
		<input type='hidden' name='portfolio.ID' value='${portfolio.ID!""}'>
		<input type='hidden' name='ID' value='${ID}'>
		<input type='hidden' name='strategyID' value='${strategyID!""}'>
		<input type='hidden' name='portfolio.userID' value='${portfolio.userID!""}'>
		<input type='hidden' name='portfolio.mainStrategyID' value='${portfolio.mainStrategyID!""}'>
		<input type='hidden' name='portfolio.originalPortfolioID' value='${portfolio.originalPortfolioID!""}'>
		<input type='hidden' id='operation' name='operation' value=''>
		<tr>
			<td>
				<h2>Name</h2>
			</td>
			<td style="width:70%">
				<input type='text' id="name" name='portfolio.name' value='${portfolio.name!""}' class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:550px">
				<input type='hidden' id="oldname"  value='${portfolio.name!""}'>
			</td>
		</tr>
		
		<tr>
			<td>
				<h2>EndDate</h2>
			</td>
			<td>
				[#if portfolio.endDate??]${portfolio.endDate?string("MM/dd/yyyy")}[/#if]
				<input type='hidden'  name='portfolio.endDate' value='[#if portfolio.endDate??]${portfolio.endDate?string("MM/dd/yyyy")}[/#if]' >
			</td>
		</tr>
		<tr>
			<td>
				<h2>StartingDate</h2>
			</td>
			<td>
				<input type='text' id="startingdate"  name='portfolio.startingDate' value='[#if portfolio.startingDate??]${portfolio.startingDate?string("MM/dd/yyyy")}[/#if]'  class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:550px">
			</td>
		</tr>
		<tr>
			<td>
				<h2>Categories</h2>
			</td>
			<td>
				<input type='text'  name='portfolio.categories' value='${portfolio.categories!""}'  class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:550px">
			</td>
		</tr>
		[#if portfolio.MainStrategyName??]
		<tr>
			<td>
				<h2>Belong to</h2>
			</td>
			<td>
				${portfolio.MainStrategyName}
			</td>
		</tr>
		[/#if]
		<tr>
			<td>
				<h2>LastTransactionDate</h2>
			</td>
			<td>
				[#if portfolio.lastTransactionDate??]${portfolio.lastTransactionDate?string("MM/dd/yyyy")}[/#if]
				<input type='hidden' name='portfolio.lastTransactionDate' value='[#if portfolio.lastTransactionDate??]${portfolio.lastTransactionDate?string("MM/dd/yyyy")}[/#if]' style="width:100%">
			</td>
		</tr>
		<tr>
			<td>
				<h2>DelayLastTransactionDate</h2>
			</td>
			<td>
				[#if portfolio.delayLastTransactionDate??]${portfolio.delayLastTransactionDate?string("MM/dd/yyyy")}[/#if]
				<input type='hidden' name='portfolio.delayLastTransactionDate' value='[#if portfolio.delayLastTransactionDate??]${portfolio.delayLastTransactionDate?string("MM/dd/yyyy")}[/#if]'>
				
			</td>
		</tr>
		<tr>
			<td>
				<h2>Production</h2>
			</td>
			<td>
				<select name='isProduction'  class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:550px">
					<option value="true" [#if isProduction]selected[/#if]>true</option>
					<option value="false" [#if !isProduction]selected[/#if]>false</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<h2>Attributes</h2>
				<div id="parameterdiv">
				<table width=100% id="parametertable">
					
					<thead>
					<tr>
						<th width="30%">
							Name
						</th>
						<th>
							Value
						</th>
					</tr>
					</thead>
					<tbody>
					[#if attributes??]
					[#list attributes as p]
					<tr>
						<td>
							<input type='text' name='attributes[${p_index}].pre' value='${p.pre!""}'>
						</td>
						<td>
							<input type='text' name='attributes[${p_index}].post' value='${p.post!""}'>
						</td>
					</tr>
					[/#list]
					[/#if]
					
					</tbody>
				</table>
				</div>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<div><h2>Description</h2></div>
				<div>
					<textarea name='portfolio.description' id='description' style="width:100%;height:300px">[#if portfolio.description??]${portfolio.description?html}[/#if]</textarea>
				</div>
			</td>
		</tr>
</table>
<br>
[#if admin || owner || ID==0]
<input type="button" class="uiButton" value="Save" onclick="save()">
[/#if]
[#if admin || owner]
<input type="button" class="uiButton" value="Delete" onclick="remove()">
[/#if]
[#if !anonymous]
<input type="button" class="uiButton" value="Save as" onclick="saveas()">
[/#if]
</form>
</body>
</html>