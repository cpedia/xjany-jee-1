			function ShowEditor(id,div_id)
			{
				$("#"+id+"___Frame").attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName="+id+"&Toolbar=Default"});
				$('#'+div_id+'_text').hide();
				$('#'+div_id+'_edit').show();
				$('#'+div_id+'EditNoFck').attr({disabled:"yes"});
				$('#'+div_id+'Edit').attr({disabled:"yes"});
			}
			
			function ShowEditorNoFck(id,div_id)
			{
				//$("#"+id+"___Frame").attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName="+id+"&Toolbar=Default"});
				$('#'+div_id+'_text').hide();
				
				
				var $inp=$(document.createElement("textarea"));
				$inp.attr({name:$('#'+id).attr('name'),rows:20,cols:150});
				$inp.val($('#'+div_id+'_text').html());
				$('#'+id).remove();
				$('#'+div_id+'_editnofck').append($inp);
				$('#'+div_id+'EditNoFck').attr({disabled:"yes"});
				$('#'+div_id+'Edit').attr({disabled:"yes"});
			}			