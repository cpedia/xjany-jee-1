$(document).ready(function(){
	//提交表单的时候检验是否
	$("#submit").click(function(){
		
	})
	
	function checkForm(){
		var flag= true;
		$userName = $("#j_username").value;
		$password = $("#j_password").value;
		if(userName == ""){
			alert("Please input the username");
			$("#j_username").focus();
			flag = false;	
		}
		if(password == ""){
			alert("Please input the password");
			$("#j_password").focus();
			flag = false;				
		}
	}
})