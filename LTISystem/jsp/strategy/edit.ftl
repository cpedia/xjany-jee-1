[#ftl]
<html>
<head>
<meta name="submenu" content="individualstrategy"/>
<meta name="jqueryui" content="smoothness">
<script src="/LTISystem/jsp/template/gpl/editabletable.js"></script>
<meta name="id" content='${ID}'/>
<script type="text/javascript" src="/LTISystem/jsp/images/tiny_mce/tiny_mce.js"></script>
<script>
tinyMCE.init({
    mode : "none",
    theme : "advanced",
	plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
	theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
	theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
	theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
	theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	convert_urls : false,
	save_onsavecallback : "save_for_mce"
});
$(function() {
	tinyMCE.execCommand('mceAddControl', false, "description");
	tinyMCE.execCommand('mceAddControl', false, "reference");
	tinyMCE.execCommand('mceAddControl', false, "shortDescription");
	tinyMCE.execCommand('mceAddControl', false, "similarIssues");
	
	$('option').each(function(){
		if($(this).val()==${strategy.strategyClassID!"3"}){
			$(this).attr('selected','selected'); 
		}
	});
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
	//$("#accordion").accordion();
	$("#accordion a").each(function(){
		$(this).parent().next().hide();
		$(this).data('current',false);
	});
	$("#accordion a").button();
	$("#accordion a").click(function(){
		$(this).data('current',true);
		$("#accordion a").each(function(){
			if(!$(this).data('current')){
				$(this).parent().next().hide();
			}
		});
		$(this).parent().next().toggle();
		$(this).data('current',false);
	});
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
	if(confirm("Are you sure to delete this strategy? and please notice that all the portfolios which used this strategy won't be deleted.")){
		$('#operation').val('predelete');
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
		<td width="120">
			Read Groups
		</td>
		<td width="800">
			<input name="readGroup" value="${readGroup}" style="width:800px">
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


<form action="Save.action" method="post" id="_form">
<table width=100%>
		<input type='hidden'  name='operation' value='' id='operation'>
		<input type='hidden'  name='ID' value='${ID!""}'>
		<input type='hidden'  name='strategy.ID' value='${strategy.ID!""}'>
		<input type='hidden' readonly name='strategy.postID' value='${strategy.postID!""}'>
		<input type='hidden' readonly name='strategy.forumID' value='${strategy.forumID!""}'>
		<input type='hidden' name='strategy.mainStrategyID' value='${strategy.mainStrategyID!""}'>
		<tr>
			<td style="width:200px">
				<h2>Name</h2>
			</td>
			<td>
				<input type='text' id="name" name='strategy.name' value='${strategy.name!""}'  class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:550px">
				<input type='hidden' id="oldname" value='${strategy.name!""}'>
			</td>
		</tr>
		<tr>
			<td>
				<h2>StrategyClassID</h2>
			</td>
			<td>
				<select name='strategy.strategyClassID'  class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:550px">
					<option value='1'>--PORTFOLIO STRATEGY</option>
					<option value='3'>&nbsp;&nbsp;&nbsp;&nbsp;ASSET ALLOCATION STRATEGY</option>
					<option value='4'>&nbsp;&nbsp;&nbsp;&nbsp;REBALANCING STRATEGY</option>
					<option value='5'>&nbsp;&nbsp;&nbsp;&nbsp;++CASH FLOW STRATEGY</option>
					<option value='6'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DEPOSIT  STRATEGY</option>
					<option value='7'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WITHDRAW  STRATEGY</option>
					<option value='2'>--ASSET STRATEGY</option>
					<option value='9'>&nbsp;&nbsp;&nbsp;&nbsp;++EQUITY</option>
					<option value='16'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US EQUITY</option>
					<option value='40'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MIDDLE VALUE</option>
					<option value='41'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMALL VALUE</option>
					<option value='42'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE BLEND</option>
					<option value='43'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMALL BLEND</option>
					<option value='44'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MIDDLE BLEND</option>
					<option value='45'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE GROWTH</option>
					<option value='46'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMALL GROWTH</option>
					<option value='47'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MIDDLE GROWTH</option>
					<option value='52'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE VALUE</option>
					<option value='53'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Micro Cap</option>
					<option value='17'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INTERNATIONAL EQUITY</option>
					<option value='18'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EMERGING MARKET EQUITY</option>
					<option value='10'>&nbsp;&nbsp;&nbsp;&nbsp;++FIXED INCOME</option>
					<option value='19'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US GOVERNMENT BONDS</option>
					<option value='31'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SHORT TERM</option>
					<option value='32'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MIDDLE TERM</option>
					<option value='33'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LONG TERM</option>
					<option value='20'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US CORPORATE BONDS</option>
					<option value='34'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INVESTMENT GRADE</option>
					<option value='35'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;HIGH YIELD</option>
					<option value='21'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+INTERNATIONAL BONDS</option>
					<option value='36'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GOVERNMENT BONDS</option>
					<option value='37'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CORPORATE BONDS</option>
					<option value='38'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;HIGH YIELD BONDS</option>
					<option value='39'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EMERGING MARKET BONDS</option>
					<option value='22'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;HYBRID/MULTI-SECTOR BONDS</option>
					<option value='48'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US TREASURY BONDS</option>
					<option value='49'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SHORT TERM</option>
					<option value='50'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MIDDLE TERM</option>
					<option value='51'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LONG TERM</option>
					<option value='54'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Municipal Bonds</option>
					<option value='11'>&nbsp;&nbsp;&nbsp;&nbsp;++CASH</option>
					<option value='23'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CASH</option>
					<option value='24'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CD</option>
					<option value='12'>&nbsp;&nbsp;&nbsp;&nbsp;++HYBRID ASSETS</option>
					<option value='25'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+BALANCE FUND</option>
					<option value='55'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Moderate Allocation</option>
					<option value='56'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Conservative Allocation</option>
					<option value='26'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CONVERTIBLE BONDS</option>
					<option value='27'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PREFERRED SECURITIES</option>
					<option value='28'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CONVERTIBLE SECURITIES</option>
					<option value='13'>&nbsp;&nbsp;&nbsp;&nbsp;++REAL ESTATE</option>
					<option value='29'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;REITS</option>
					<option value='30'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NON-REITS</option>
					<option value='14'>&nbsp;&nbsp;&nbsp;&nbsp;++COMMODITIES</option>
					<option value='15'>&nbsp;&nbsp;&nbsp;&nbsp;++HEDGES</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<h2>Owner</h2>
			</td>
			<td>
				${strategy.userName!"-"}[${strategy.userID!"-"}]
			</td>
		</tr>
		<tr>
			<td>
				<h2>Type</h2>
			</td>
			<td>
				[#if strategy.normal]
					Strategy
				[#else]
					Other
				[/#if]
			</td>
		</tr>
		<tr>
			<td>
				<h2>Categories</h2>
			</td>
			<td>
				<input type='text'  name='strategy.categories' value='${strategy.categories!""}'  class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:550px">
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
				<script>
					var arr=new Array('pre','post');
					var cssesarr=new Array('','','','width:100%');
					$('#parametertable').editabletable({name:'attributes',data:arr,csses:cssesarr});
				</script>
			</td>
		</tr>
		<tr>
			<td colspan=2>
				<div id='accordion'>
					<h3><a>ShortDescription</a></h3>
					<div>
						<textarea name='strategy.shortDescription' id="shortDescription" style="width:100%;height:400px">[#if strategy.shortDescription??]${strategy.shortDescription?html}[/#if]</textarea>
					</div>
					<h3><a>Description</a></h3>
					<div>
						<textarea name='strategy.description' id="description" style="width:100%;height:400px">[#if strategy.description??]${strategy.description}[/#if]</textarea>
					</div>
					<h3><a>Reference</a></h3>
					<div>
						<textarea name='strategy.reference' id="reference" style="width:100%;height:400px">[#if strategy.reference??]${strategy.reference}[/#if]</textarea>
					</div>
					<h3><a>SimilarIssues</a></h3>
					<div>
						<textarea name='strategy.similarIssues' id="similarIssues" style="width:100%;height:400px">[#if strategy.similarIssues??]${strategy.similarIssues?html}[/#if]</textarea>
					</div>
				<div>
			</td>
		</tr>
</table>
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
