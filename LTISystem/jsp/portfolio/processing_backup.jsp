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
		var times=0;
		var bool_init=0;
	    $(document).ready(function(){ 
			$('.progressbar').progression({ Current: 5 }); 
				
			$.timer(2000, function (timer) {
				   			    
					$.ajax({type: "post",  
		             url:'../ajax/GetPortfolioState.action?id=<%=request.getParameter("portfolioID")%>',  
		             dataType: "html",  
		             //data: "userName="+userName+"&password="+password,  
		             success: function(result){  
		             				if(result==-1){
		             					bool_init++;
		             					if(bool_init==100){
						  					$('#exceptionFrame').attr({src:'../ajax/GetPortfolioMessage.action?id=<%=request.getParameter("portfolioID")%>',height:400});
						 					window.parent.document.getElementById('detail').height   =   document.body.scrollHeight+10;
						 					window.parent.document.getElementById('processingpane').style.height   =   document.body.scrollHeight+10;
						 					bool_init=0;
						 					timer.stop();				
 										}
		             					
		             				}else if(result==100){
		             					timer.stop();
		             					$(".progressbar").progression({ Current: 100 });
		             					$("#Message").html("Finish to Execute All Strategies!");
		             					window.parent.location.href = '<s:property value="view_url"/>'+'?ID=<%=request.getParameter("portfolioID")%>&actionMessage="Execute The Portfolio Successfully!"';
		             				}
		             				else{
			                         var res = String($.trim(result));  
			                         $(".progressbar").progression({ Current: result });
		                            }
		                        if(result==10){
		                        	$("#Message").html("Prepare to run strategies!");
		                        }
		                        if(result>20&&result<90)$("#Message").html("Executing strategies!");
		                        if(result==90)$("#Message").html("Computing MPTs!");
		               }
		             });
			});
	    }); 
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
	<div id="p" class="progressbar">10 %</div><form action='../ajax/StopExecution.action' target="stopFrame"><input type="hidden" name="portfolioID" value='<%=request.getParameter("portfolioID")%>'><input type="submit" value="Stop"></form>
	<iframe name="stopFrame" height="0" width="0" frameborder="0" marginWidth="0" marginHeight="0" scrolling="no"></iframe>
	<div id="Message"></div>
	<iframe id="exceptionFrame" name="exceptionFrame" height="0" width="800" height="0" frameborder="0" marginWidth="0" marginHeight="0" scrolling="no"></iframe>
	
</body>
</html>
