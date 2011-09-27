<html>
<head>
	<#if nofck==false && (name?ends_with("html") || name?ends_with("htm") ||name?ends_with("uftl"))>
	<!-- TinyMCE -->
	<script type="text/javascript" src="jscripts/tiny_mce/tiny_mce.js"></script>
	<script type="text/javascript">
		tinyMCE.init({
			// General options
			mode : "textareas",
			theme : "advanced",
			plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
	
			// Theme options
			theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
			theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
			theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
			theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "bottom",
			theme_advanced_resizing : true,
			//auto_resize : true,
			//force_br_newlines:true,
			//force_p_newlines: false,
			forced_root_block : false,
   			force_br_newlines : false,
   			force_p_newlines : false,
	
			// Example content CSS (should be your site CSS)
			content_css : "css/content.css",
	
			// Drop lists for link/image/media/template dialogs
			template_external_list_url : "lists/template_list.js",
			external_link_list_url : "lists/link_list.js",
			external_image_list_url : "lists/image_list.js",
			media_external_list_url : "lists/media_list.js",
	
			// Replace values for the template plugin
			template_replace_values : {
				username : "Some User",
				staffid : "991234",
				keyword : "Parameters.keyword",
				name : "Please input unique id here",
				value : "Parameters.value"
				
			}
		});
	</script>
	<!-- /TinyMCE -->
	</#if>
	<script>
	function load(){
		window.location.href="Read.action?name="+$("#name").val();		
	}
	function loadFCK(nofck){
		window.location.href="Read.action?nofck=" + nofck +"&name=${name}";	
	}
	</script>
	<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>
	<script>
	function delete2(){
		if(confirm("Are you sure to delete '"+$('#name').val()+"'?")){
			$('#form_update').attr({action:'Delete.action'});
			$('#form_update').submit();
		} 
	}
	</script>
</head>
<body>
	<table border="0" style="height:95%;width:95%;border:0px;margin:10px">
		<form action='Update.action' id="form_update" method="post">
		<tr>
			<td width="15%">
			Name:
			</td>
			<td align="left" width="30%">
				<input type="text" value="${name}" name="name"  id="name" style="width:100%;border:1px solid grey;font-size:12px"/>
			</td>
			<td align="left">
			<a href="javascript:load()">go</a>
			</td>
		</tr>
		<tr>
			<td width="15%">
			Content:
			</td>
			<td align="left" colspan="2">
				<#if nofck==false>
				<a href="javascript:loadFCK(true)">View Without FCK</a>
				<#else>
				<a href="javascript:loadFCK(false)">View With FCK</a>
				</#if>
			</td>
		</tr>
		<tr>
			<td height="80%" colspan="3">
			<#if nofck==false && (name?ends_with("html") || name?ends_with("htm") ||name?ends_with("uftl"))>
				<!-- Gets replaced with TinyMCE, remember HTML in a textarea should be encoded -->
				<textarea id='content' name='content' rows="25" style="width:100%;">
				${content?html}
				</textarea>
			<#else>
				<textarea name='content' style="overflow:auto; height:98%;width:100%;border:1px solid grey;font-size:13px">${content?html}</textarea>
			</#if>
			</td>
		</tr>
		<tr>
			<td height="10%" colspan="3">
				<input type="submit" value="Save/Save as"/>
				<input type="button" value="Delete" onclick="delete2()"/>
			</td>
		</tr>
	
	</table>
</body>
</html>


