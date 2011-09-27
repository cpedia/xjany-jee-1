<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<head>
	<LINK href="/LTISystem/jsp/images/style.css" type=text/css rel=stylesheet>
	
	<script src="../images/jquery-1.2.6.pack.js" type="text/javascript"></script>
	<script src="images/jquery.progression.js" type="text/javascript"></script>
	<script src="images/jquery.timer.js" type="text/javascript"></script>
	
	<s:url id="view_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
	</s:url>
	
	<script type="text/javascript">
		function stopCurrentExecution(id){
			$('#stopFrame').attr({src:'http://127.0.0.1:52201/stop?portfolioID='+id});
			stop=1;
			$("#Message2").html("User interrupted this execution!");
		}	
	
		var stop=0;
	    $(document).ready(function(){ 
			$('.progressbar').progression({ Current: 5 }); 
			$.timer(1000, function (timer) {
				if(stop==1)timer.stop();
			    var s = document.createElement("SCRIPT");
			    s.id="process_state"; 
			    document.getElementsByTagName("HEAD")[0].appendChild(s);
			    d=new Date();
			    s.src='http://127.0.0.1:52201/state2?portfolioID=<%=request.getParameter("portfolioID")%>&version=1.2.1&time='+d.getTime()+'&timestamp=<%=request.getParameter("timestamp")%>';

			});
	    }); 
	    
	    var counter=0;
	    function setProcess(data){
	    	document.getElementsByTagName("HEAD")[0].removeChild(document.getElementById("process_state")); 
	    	
	    	var result=data.state;
	    	
	    	if(result=='-2'){
	    		counter++;
	    		if(counter==50){
					$("#Message").html("The execution may fail, please check the validfi console to see more detail information.");
	    		}
	    	}else if(result==-1){
	  				$('#exceptionFrame').attr({src:'http://127.0.0.1:52201/message?portfolioID=<%=request.getParameter("portfolioID")%>',height:400});
	 				window.parent.document.getElementById('detail').height   =   document.body.scrollHeight+10;
	 				window.parent.document.getElementById('processingpane').style.height   =   document.body.scrollHeight+10;
					stop=1;					
 			}else if(result==100){
 					stop=1;
 					$(".progressbar").progression({ Current: 100 });
 					$("#Message").html("Finish to Execute All Strategies!");
 					window.parent.location.href = '<s:property value="view_url"/>'+'?ID=<%=request.getParameter("portfolioID")%>&actionMessage="Execute The Portfolio Successfully!"';
 			}else if(result==10){
 				$(".progressbar").progression({ Current: 10 });
            	$("#Message").html("Prepare to run strategies!");
            }else if(result==90){
            	$(".progressbar").progression({ Current: 90 });
            	$("#Message").html("Computing MPTs!");
            }else {
				$(".progressbar").progression({ Current: result });
				$("#Message").html("Executing strategies!");
            }	    
	    }
	</script>
	
	<style type="text/css">
	.progressbar { 
			border:1px solid black;
			width: 200px;
			height: 20px;
			line-height: 20px;
			text-align: center;
	    
	}
	</style>
</head>
<body>
	<div id="p" class="progressbar">10 %</div><a href='javascript:stopCurrentExecution(<%=request.getParameter("portfolioID")%>)'>Stop</a>
	<iframe name="stopFrame" id="stopFrame" height="0" width="0" height="0" frameborder="0" marginWidth="0" marginHeight="0"></iframe>
	<div id="Message"></div><span id="Message2"></span>
	<iframe id="exceptionFrame" name="exceptionFrame" height="0" width="800" height="0" frameborder="0" marginWidth="0" marginHeight="0" scrolling="no"></iframe>
</body>
</html>
