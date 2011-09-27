/**
 * 
 * */
 $(document).ready(function(){
 	//while inputing the username give tips.. 	
 	$("#username").keypress(function(){
 		var tips = "username contains at least 5 character!";
 		var minLenght = 5;	//username contains no less than 5 characters
 		var user = ("#username").value;	//获取已输入的内容
 		alert(user);
 		//alert(minLenght);
 		var s_user = String(user);	//转换成string对像
 		//少于最小长度时做提示
 		while(s_user.length < minLenght){
 			alert(s_user.length);
 			("#Ntip").value = tips;
 			$user = ("#username").value;	//获取已输入的内容
 			var s_user = String(user);	//转换成string对像
 		}
 	})
 })
