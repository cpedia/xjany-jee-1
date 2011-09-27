		$(document).ready(function() {
			if($('#Action').val()=="create")
			{
				//$('#des').hide();
				//$('#sdes').hide();
				//$('#ref').hide();
				//$('#simi').hide();
				//$('#des_text').hide();
				//$('#sdes_text').hide();
				//$('#ref_text').hide();
				//$('#simi_text').hide();
				//ShowDesEditor();
				//ShowSDesEditor();
				//ShowRefEditor();
				//ShowSimiEditor();
			}
			else
			{
				//$('#des').hide();
				//$('#sdes').hide();
				//$('#ref').hide();
				//$('#simi').hide();
				if($('#hideCode').val()=="false"){
					setRows("variable");
					setRows("function");
					setRows("init");
					setRows("commentAction");
					setRows("defaultAction");
					var i;
					for(i=0;i<$('#conditionCount').val();i++)
						setRows("postconditionAction"+i);					
				}

			}


			
		$("#strategyName").toggleVal("active");
		});
		
		

		
		function editmode(a){
			
		}
      function setRows(elementID) {
    	  var element=document.getElementById(elementID);
			var cols = element.cols;
			var str = element.value;
			// windows - replace \r\n
			// mac - replace just \r
			// linux - is just \n
			str = str.replace(/\r\n?/, "\n");
			var lines = 2;
			var chars = 0;
			for (i = 0; i < str.length; i++) {
			var c = str.charAt(i);
						chars++;
			             if (c == "\n" || chars == cols) {
			               lines ++;
			               chars = 0;
			             }
			           }
			element.setAttribute("rows", lines);
			}

		
		function del()
		{
			document.OverViewForm.action='../strategy/DeleteConfirm.action?action=delete';
			$('#OverViewForm').submit();
		}	
		
		function save()
		{
			
			if($('#Action').val()=="create" || $('#Action').val() == "save")
			{
				//$('#Action').val("save");
				document.OverViewForm.action='../strategy/Save.action?action=save';
				document.OverViewForm.submit();
			}
			else
			{
				//$('#Action').val("update");
				document.OverViewForm.action='../strategy/UpdateBase.action?action=update';
				document.OverViewForm.submit();
				
			}
			$("#OverViewForm").attr({onsubmit:"return true"});
		}
		
		
		function compile()
		{
			//$("#init").val(init_panel.getCode());
			$('#Action2').val("compile");
		}
		
		function savecode()
		{
			$('#Action2').val("updateCode");
			//$("#init").val(init_panel.getCode());
		}
		
		

	