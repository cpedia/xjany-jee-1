[#ftl]
<html>
<head>
<meta name="submenu" content="individualstrategy"/>
<meta name="id" content='${ID}'/>
<title>Edit Code of ${strategy.name}</title>
<script src="/LTISystem/jsp/template/gpl/codemirror/js/codemirror.js" type="text/javascript"></script>
<script src="/LTISystem/jsp/template/gpl/editabletable.js"></script>
 <link rel="stylesheet" type="text/css" href="/LTISystem/jsp/template/gpl/codemirror/docs.css"/>
<script>
$(function() {
	var editor = CodeMirror.fromTextArea('initcode', {
	    height: "450px",
	    parserfile: ["../contrib/csharp/js/tokenizecsharp.js", "../contrib/csharp/js/parsecsharp.js"],
	    stylesheet: "../template/gpl/codemirror/contrib/csharp/css/csharpcolors.css",
	    path: "../template/gpl/codemirror/js/",
	    textWrapping: false
	  });
	var editor2 = CodeMirror.fromTextArea('functioncode', {
	    height: "450px",
	    parserfile: ["../contrib/csharp/js/tokenizecsharp.js", "../contrib/csharp/js/parsecsharp.js"],
	    stylesheet: "../template/gpl/codemirror/contrib/csharp/css/csharpcolors.css",
	    path: "../template/gpl/codemirror/js/",
	    textWrapping: false
	  });
	var editor2 = CodeMirror.fromTextArea('reinitcode', {
	    height: "450px",
	    parserfile: ["../contrib/csharp/js/tokenizecsharp.js", "../contrib/csharp/js/parsecsharp.js"],
	    stylesheet: "../template/gpl/codemirror/contrib/csharp/css/csharpcolors.css",
	    path: "../template/gpl/codemirror/js/",
	    textWrapping: false
	  });
	  
	  
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
	
	var arr1=new Array('pre','post');
	var cssesarr1=new Array('','width:100%;height:400px');
	var typearr1=new Array('','textarea');
	$('#actiontable').editabletable({name:'codeInf.conditionAction',data:arr1,csses:cssesarr1,type:typearr1});
});  	

function compile(){
	$.ajax({
			url: "Compile.action?includeHeader=false",
			dataType: "html",
			data: "ID=${ID}",
			success: function( data ) {
				alert(data);
			}
		});
}
function save(){
	$('#_form').submit();
}
</script>
<style>
textarea{
	width:100%;height:450px;
}
</style>
</head>
<body>
<a href='/LTISystem/jsp/ajax/ViewSourceFile.action?strategyID=${ID}' target='_blank' class="uiButton">View Source</a>
<form action="SaveCode.action" method="post" id="_form">
<table width=100%>
		<input type='hidden'  name='codeInf.ID' value='${codeInf.ID!""}'>
		<input type='hidden'  name='ID' value='${ID}'>
		<input type='hidden'  name='codeInf.name' value='${codeInf.name!""}' style="width:100%">
		<tr>
			<td colspan="2">
				<h2>Parameters</h2>
				<table width=100% id="parametertable">
					
					<thead>
					<tr>
						<th>
							<h3>Type</h3>
						</th>
						<th>
							<h3>Name</h3>
						</th>
						<th>
							<h3>Value</h3>
						</th>
						<th style="width:50%">
							<h3>Description</h3>
						</th>
					</tr>
					</thead>
					<tbody>
					[#if codeInf.parameter??]
					[#list codeInf.parameter as p]
					<tr>
						<td>
							<input type='text' name='codeInf.parameter[${p_index}].first' value='${p.first!""}'>
						</td>
						<td>
							<input type='text' name='codeInf.parameter[${p_index}].second' value='${p.second!""}'>
						</td>
						<td>
							<input type='text' name='codeInf.parameter[${p_index}].third' value='${p.third!""}'>
						</td>
						<td>
							<input type='text' name='codeInf.parameter[${p_index}].fourth' value='${p.fourth!""}' style="width:100%">
						</td>
					</tr>
					[/#list]
					[/#if]
					
					</tbody>
				</table>
				<script>
					var arr=new Array('first','second','third','fourth');
					var cssesarr=new Array('','','','width:100%');
					$('#parametertable').editabletable({name:'codeInf.parameter',data:arr,csses:cssesarr});
				</script>
			</td>
		</tr>
			<td colspan=2>
				<div id='accordion'>
					<h3><a href="javascript:void(0)">Variable</a></h3>
					<div>
						<textarea name='codeInf.variable'>[#if codeInf.variable??]${codeInf.variable?html}[/#if]</textarea>
					</div>
					<h3><a href="javascript:void(0)">Function</a></h3>
					<div style="border:1px solid #000000;">
						<textarea name='codeInf.function' id="functioncode">[#if codeInf.function??]${codeInf.function?html}[/#if]</textarea>
					</div>
					<h3><a href="javascript:void(0)">Init</a></h3>
					<div style="border:1px solid #000000;">
						<textarea name='codeInf.init' id="initcode" >[#if codeInf.init??]${codeInf.init?html}[/#if]</textarea>
					</div>
					<h3><a href="javascript:void(0)">Default Action</a></h3>
					<div>
						<textarea name='codeInf.defaultAction'>[#if codeInf.defaultAction??]${codeInf.defaultAction?html}[/#if]</textarea>
					</div>
					<h3><a href="javascript:void(0)">Re Init</a></h3>
					<div style="border:1px solid #000000;">
						<textarea name='codeInf.reInit' id="reinitcode">[#if codeInf.reInit??]${codeInf.reInit?html}[/#if]</textarea>
					</div>
					<h3><a href="javascript:void(0)">Common Action</a></h3>
					<div>
						<textarea name='codeInf.commentAction'>[#if codeInf.commentAction??]${codeInf.commentAction?html}[/#if]</textarea>
					</div>
					<h3><a href="javascript:void(0)">Condiction Actions</a></h3>
					<div>
						<table width=100% id="actiontable">
							<thead>
							<tr>
								<th>
									Condition
								</th>
								<th width="80%">
									Action
								</th>
							</tr>
							</thead>
							<tbody>
							[#if codeInf.conditionAction??]
							[#list codeInf.conditionAction as ca]
							<tr>
								<td>
									<input type='text' name='codeInf.conditionAction[${ca_index}].pre' value='${ca.pre!""}'>
								</td>
								<td>
									<textarea name='codeInf.conditionAction[${ca_index}].post' >${ca.post!""}</textarea>
								</td>
							</tr>
							[/#list]
							[/#if]
							</tbody>
						</table>
					</div>
				</div>
			</td>
		</tr>
		
		
</table>


<input type="button" class="uiButton" value="Save" onclick="save()">
<input type="button" class="uiButton" value="Compile" onclick="compile()">
</form>


</body>
</html>
