function createList(source,targetPane,selectName,hiddenObject,selectVal){
		$source=$('#'+source);
		$hiddenObject=$('#'+hiddenObject);
		$targetPane=$('#'+targetPane);
		$targetPane.html('');
		
		var currentClassID = $hiddenObject.val();
		//alert(currentClassID);
		
		for(var i=0;i<$('#'+source+' .treeDepth').val();i++){
			$targetPane.append("<select id='"+selectName+i+"'><option value='none'> -- Select -- </option></select>");
			
			$('#'+selectName+i).change( 
				function()
				{
					var $current=$(this);

					var currentNum= $current.attr("id").substring($current.attr("id").length-1,$current.attr("id").length);
					
					var $child=$('#'+selectName+eval(currentNum+'+'+'1'));
					$child.html('<option value="'+this.value+'"> -- Select -- </option>');
					$child.append($('#'+source+" ."+this.value).clone());
					

					$child.hide();

					if($child.html()!=null && eval($child.html().length)!=0 && $(this).html()!=$child.html())$child.show();
					
					for(var j=eval(currentNum+'+'+'2');j<$('#'+source+' .treeDepth').val();j++){
						$('#'+selectName+j).html('');
						$('#'+selectName+j).hide();
					}
					$hiddenObject.val(this.value);
					//alert($hiddenObject.val());
				}
			);
		}

		$('#'+selectName+'0').html($('#'+source+" .0").clone());
		$('#'+selectName+'0').trigger("change");
		//$hiddenObject.val($('#'+source+' .defaultValue').val());

		
		
		if(!(typeof selectVal == "undefined")){
			var stack=new Array();
			var parentVal,currentVal;
			if(currentClassID!=selectVal)
				currentVal = currentClassID;
			else
				currentVal = selectVal;
			var elements = $('#'+source+'>option');
			
			for(var num=0;num<10;num++){
				var flag=false;
				for(var i=0;i<elements.length;i++){
					$element=$(elements[i]);
					
					if(eval($element.val())==eval(currentVal)){
					
						stack.push(currentVal);
						currentVal=$element.attr('class');
						flag=true;
						break;
					}
				}
				if(flag==false)
					break;
			}//end while
			
			var triggertimes=stack.length;
			for(var i=0;i<$('#'+source+' .treeDepth').val()&&i<=triggertimes;i++){

				$('#'+selectName+i).val(stack.pop());
				$('#'+selectName+i).trigger("change");
			}
			$hiddenObject.val(selectVal);	
		}
}