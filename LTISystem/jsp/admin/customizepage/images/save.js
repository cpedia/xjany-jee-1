		$(document).ready(function(){
			$('#Template_FCK').hide();
		})
			
		function showTemplateFck(){
		$('#Template___Frame').attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName=Template&Toolbar=Default"});
		$('#Template_text').hide();
		$('#Template_FCK').show();
		$('#Template_edit').attr({disabled:"yes"});
		}
		