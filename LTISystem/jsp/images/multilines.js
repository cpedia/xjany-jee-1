   
    var n=0;
    var m=0;
    var port=0;
    
    function addConditionAction() {
    	if(m>0)
    	{
		    var k=m-1;
		    var actionDel="deleteAction"+k;
    	    var parameterAction = document.getElementById(actionDel);
		    var $pre=$('#preconditionAction'+k+'');
    		var $post=$('#postconditionAction'+k+''); 
    		if(((parameterAction.style.display != 'none')&&($pre.val()==''))||((parameterAction.style.display != 'none')&&($post.val()=='')))
    		{
    			 window.alert("please finish the condition action textbox");   
    		     return 0;
    		}
    	}
   
    		 var $pane=$('#addConditionItem');
		      var $tr,$td;
		      $tr=$(document.createElement("tr"));
		       $tr.attr({id:'deleteCondition'+m});
		      $pane.append($tr);
		      
		      $td=$(document.createElement("td"));
		      $td.html("Condition**");
		      $td.width="20";
		      $tr.append($td);
		      
		      $td=$(document.createElement("td"));
		      $td.width="100";
		     // $td.html("<textarea id=postconditionAction" + m + " name=conditionAction[" + m + "].post cols=110  rows=3/> <input type=button  onclick=deleteConditionAction("+m+") value='delete' id=actionsButton" + m + " size=10 name=action" + m + ">");
		     $td.html("<textarea id=postconditionAction" + m + "  name=conditionAction[" + m + "].pre cols=110  rows=5/>");
		      $tr.append($td);
		      
		      $tr=$(document.createElement("tr"));
		       $tr.attr({id:'deleteAction'+m});
		      $pane.append($tr);
		      
		      $td=$(document.createElement("td"));
		      $td.html("Action :: ");
		      $tr.append($td);
		      
		      $td=$(document.createElement("td"));
		      $td.html("<textarea  id=postconditionAction" + m + "  name=conditionAction[" + m + "].post cols=120 value='' rows=15  ></textarea>");
		      $tr.append($td);
		      m++;
    		 return 1;
    }
    
    function deleteConditionAction(p)
    {
    	var condtionDel="deleteCondition"+p;
    	var actionDel="deleteAction"+p;
    	 var parameterCondtion = document.getElementById(condtionDel);
    	 var parameterAction = document.getElementById(actionDel);
    	 var preconditionAction = document.getElementById("preconditionAction"+p+"");
    	 var postconditionAction = document.getElementById("postconditionAction"+p+"");
    	  preconditionAction.value='';
    	  postconditionAction.value='';
    	  parameterCondtion.style.display = 'none' ;
    	  parameterAction.style.display = 'none' ;
    	 
    }
    
    function addParameter() {
      if(n>0)
    	{
		    var k=n-1;
		    var parameter="parameter"+k;
		    var parameter1 = document.getElementById(parameter);
		    var $first=$('#parameterfirst'+k+'');
    		var $second=$('#parametersecond'+k+''); 
    		if(((parameter1.style.display != 'none')&&($first.val()==''))&&((parameter1.style.display != 'none')&&($second.val()=='')))
    		{    
    			window.alert("please finish the Parameter textbox"); 
    		    return 0;
    		}
    	}
      
      var $pane=$('#addParameterItem');
      var $tr,$td;
      $tr=$(document.createElement("tr"));
      //$tr.attr("id")="parameter"+n;
      //$tr="<tr id=parameter"+ n +"></tr>";
      $tr.attr({id:'parameter'+n});
      $pane.append($tr);
      
      $td=$(document.createElement("td"));
      $td.html("<input type=text  value='' id=parameterfirst" + n + " name=parameter[" + n + "].first>");
      $tr.append($td);
      
      $td=$(document.createElement("td"));
      $td.html("<input type=text  value='' id=parametersecond" + n + " name=parameter[" + n + "].second>");
      $tr.append($td);
      
      $td=$(document.createElement("td"));
      $td.html("<input type=text  value='' id=parameterthird" + n + "  name=parameter[" + n + "].third>");
      $tr.append($td);
      
      $td=$(document.createElement("td"));
      $td.html("<input type=text value='' id=parameterfourth" + n + "  name=parameter[" + n + "].fourth size=64 >");
      $tr.append($td);
      
      $td=$(document.createElement("td"));
      $td.html("<input type=button  onclick=deleteParameter("+n+") value='delete' id=parameterbutton" + n + "  name=parameter" + n + ">");
      
      $tr.append($td);
      n++;
      return 0;
    }
    
    function deleteParameter(p)
    {
    	var kk;
    	kk=p;
    	var parameter="parameter"+p;
    	 var parameter1 = document.getElementById(parameter);
    	 var parameterfirst1 = document.getElementById("parameterfirst"+p+"");
    	 var parametersecond1 = document.getElementById("parametersecond"+p+"");
    	 var parameterthird1 = document.getElementById("parameterthird"+p+"");
    	 var parameterfourth1 = document.getElementById("parameterfourth"+p+"");
    	  parameterfirst1.value='';
    	  parametersecond1.value='';
    	  parameterthird1.value='';
    	  parameterfourth1.value='';
    	  parameter1.style.display = 'none' ;   	 
    }
    


    
