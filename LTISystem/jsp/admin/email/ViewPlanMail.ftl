<html>
<head>
	<title>Edit Email of Plan ${plan.name}</title>
	<!-- TinyMCE -->
	<script type="text/javascript" src="../filebrowser/jscripts/tiny_mce/tiny_mce.js"></script>
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
	<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>
</head>
<body>
<form action='SendPlanMail.action' id="form_update" method="post">
<input type="hidden" name="planID" value="${planID}">
<table width="800" height="400" border=0>
<tr>
	<td>
		<input type="text" value="EMails of ${plan.name}" style="width:100%">
	</td>
</tr>
<tr>
	<td>
		<textarea name='content' style="overflow:auto; height:98%;width:100%;border:1px solid grey;font-size:13px">Dear #username#:&lt;br&gt;&lt;br&gt;The plan link is: &lt;a href='http://www.myplaniq.com/LTISystem/f401k_view.action?ID=${plan.ID?string.computer}'&gt;${plan.name}&lt;/a&gt;.&lt;br&gt;&lt;br&gt;From &lt;a href='www.myplaniq.com'&gt;MyPlanIQ.com&lt;/a&gt;</textarea>
	</td>
</tr>
<tr>
	<td>
		<input type="submit" value="Send Mail"/>
	</td>
</tr>
</table>
</form>
</body>
</html>


