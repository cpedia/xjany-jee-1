	function changeImg(){   
        var imgSrc = $("#imgObj");   
        var src = imgSrc.attr("src");   
        imgSrc.attr("src",chgUrl(src));  
    }   
    //timestamp
    //to make the picture difference every time, add the timestamp to prevent the browser buffer   
    function chgUrl(url){   
        var timestamp = (new Date()).valueOf();   
       
       if((url.indexOf("&")>=0)){   
           url = url + "×tamp=" + timestamp;   
       }else{   
           url = url + "?timestamp=" + timestamp;   
       }   
       return url;   
   }
   
   function changePassword_fb(){
        var newPassword=$('#i_newPassword').val();
   		var c_newPassword=$('#i_c_newPassword').val();
   		if(newPassword!=''){
   		   if(newPassword!=c_newPassword){
   				alert("Your confirmed password do not equal with your new password, please check it!");
   				return false;
   			}
   			$('#newPassword').val(MD5(newPassword));
   		}
   		return true;
   }  
   
   function checkPassword(){
   		var oldPassword=$('#i_oldPassword').val();
   		var newPassword=$('#i_newPassword').val();
   		var c_newPassword=$('#i_c_newPassword').val();
   		if(newPassword!=''){
   			if(newPassword!=c_newPassword){
   				alert("Your confirmed password do not equal with your new password, please check it!");
   				return false;
   			}
   			if(oldPassword==''){
   				alert("Please input your current password!");
   				return false;
   			}
   			$('#oldPassword').val(MD5(oldPassword));
   			$('#newPassword').val(MD5(newPassword));
   			
   		}
   		return true;
   		
   }
   
   function isRightCode(){   
       var code = $("#veryCode").attr("value");   
       code = "c=" + code;   
       $.ajax({   
           type:"POST",   
           url:"/LTISystem/jsp/verifycode/Verify.action",   
           data:code, 
           error: function(){
    	   $("#actionMessage").val("Fail to acess the server"); 
       },
           success: callback
       });   
  
   }   
     
   function callback(data){ 
	   $("#actionMessage").val(data);
	   if($("#actionMessage").val().substr(0,29)=="Verification Code is correct.")
	   {
	       if($('#cAction').val()=="openRegister"||$('#cAction').val()=="register")
	       {
	    	   $('#registerForm').attr("action","Register.action");
	    	   document.registerForm.submit();
	    	   
	       }
	    	   else if($('#cAction').val()=="viewUserDetails"||$('#cAction').val()=="updateUserDetails")
	    	   {
	    		   $('#registerForm').attr("action","UpdateUserDetails.action");
		    	   document.registerForm.submit();
		    	   $("#actionMessage").val("Update successfully.");
		    	   alert($("#actionMessage").val());
	    	   }
	 
	   }
	   else
	   {
	   alert($("#actionMessage").val());
       return;
	   }
   }  
   
