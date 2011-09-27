<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<head>
	<LINK href="/LTISystem/jsp/images/style.css" type=text/css rel=stylesheet>
	
	<script src="../../images/jquery-1.2.6.pack.js" type="text/javascript"></script>
	<script src="../../portfolio/images/jquery.timer.js" type="text/javascript"></script>
	
	<script type="text/javascript"> 
		var times=0;
		var bool_init=0;
	    $(document).ready(function(){ 
			$.timer(2000, function (timer) {
					$.ajax({type: "post",  
		             url:'../../ajax/GetLoadSecurityDataMessage.action',  
		             dataType: "html",  
		             success: function(result){  
		             	 if(result!='No Message!'){
		             	 	$('#Message').append(result);
		             	 	times=0;
		             	 }else{
		             	 	times++;
		             	 }
		             	 if(times==1000){
		             	 	//timer.stop();
		             	 }
		             	 if(result=='Load Success'){
		             	 	timer.stop();
		             	 }			
		               }
		             });
			});
	    }); 
	</script>
</head>
<body>
	<pre id="Message"></pre>
</body>
</html>
