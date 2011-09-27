<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<head>
	<LINK href="/LTISystem/jsp/images/style.css" type=text/css rel=stylesheet>
	
	<script src="../../images/jquery-1.2.6.pack.js" type="text/javascript"></script>
	<script src="../../portfolio/images/jquery.progression.js" type="text/javascript"></script>
	<script src="../../portfolio/images/jquery.timer.js" type="text/javascript"></script>
	
	<s:url id="view_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
	</s:url>
	
	<script type="text/javascript"> 
		var times=11;
	    $(document).ready(function(){ 
			$('.progressbar').progression({ Current: 5 });
			
			$.timer(5000, function (timer) {
					$.ajax({type: "post",  
		             url:'../../ajax/GetPortfolioState.action?id=<%=request.getParameter("portfolioID")%>&runInJob=true',  
		             dataType: "html",  
		             //data: "userName="+userName+"&password="+password,  
		             success: function(result){  
		             				if(result==-1){
		             					times++;
		             					if(times>10){
		             						timer.stop();
			             					$(".progressbar").progression({ Current: 0 });
			             					$("#Message").html("Can not get portfolio's executing information or failed to execute all strategies from start date to end date!");
			             					//window.parent.location.reload();
		             					}else{
		             						$(".progressbar").progression({ Current: 0 });
		             						$("#Message").html("Please wait, try to get information, attempt times: "+times);
		             					}
		             					
		             				}else if(result==100){
		             					timer.stop();
		             					$(".progressbar").progression({ Current: 100 });
		             					$("#Message").html("Finish to Execute All Strategies!");
		             					//window.parent.location.href = '<s:property value="view_url"/>'+'?ID=<%=request.getParameter("portfolioID")%>&actionMessage="Execute The Portfolio Successfully!"';
		             				}else{
			                         	var res = String($.trim(result));  
			                         	$(".progressbar").progression({ Current: result });
		                            }
		                        	if(result==10){
		                        		$("#Message").html("Prepare to run strategies!");
		                        	}
		                        	if(result>20&&result<90)$("#Message").html("Executing strategies!");
		                        	if(result==90)$("#Message").html("Computing MPTs!");
		                        	//if(result==100)window.parent.location.reload();

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
	<iframe name="stopFrame" height="0" width="0"></iframe>
	<div id="p" class="progressbar">10 %</div>
	<div id="Message"></div>
</body>
</html>
