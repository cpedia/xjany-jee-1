[#ftl]

[#--EMail notification start --]
$(function(){
	setemail();
});
function setemail(){
	
	var notification = $("#notification").val();
	$.ajax({
		type: "get",
		url: "jsp/ajax/Email.action",
		data: "portfolioID=${Parameters.portfolioID}",
		datatype: "html",
		error:function(result){
			alert('Error!');
		},
		success: function(result){
			if(result.indexOf("fail") == -1){
				$('#emailstatus').html('on');
				$("#notification").val(true);
			}else{
				alert('Failed to set eamil alert!');
			}
		}
	});
}
[#--EMail notification end --]
 
[#-- get executing information  start --]
$(function(){
	$.timer(10000, function (timer) {
		if(++times>=4000){
			timer.stop();
		}
	    var s = document.createElement("SCRIPT");
	    s.id="process_state"; 
	    document.getElementsByTagName("HEAD")[0].appendChild(s);
	    d=new Date();
	    s.src='http://'+host+':8081/state?portfolioID=${Parameters.portfolioID}&timestamp=-1&function=setProcess';
	});
	
	$('#div_message').html('Performing Historical Simulation on the Portfolio ... Please Wait ...');
});
 
//times of trying to get executing information. max times to get information: 4000
var times=0;
//error times of getting information. max errors times to get information: 40
var errortimes=0;
//true: the information has been getted
var getted=false;
function setProcess(data){
	document.getElementsByTagName("HEAD")[0].removeChild(document.getElementById("process_state")); 
	var result=data.state;
	if(result<0){
		errortimes++;
		result='try to get simulation information['+errortimes+']';
	}
	if(errortimes>40){
		$('#div_message').html('the execution may be unsuccessful.<a href="/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${Parameters.portfolioID}">Click here the view the portfolio</a>');
		times=4000;
	}else if(result!=100){
		$('#div_message').html('Performing Historical Simulation: '+result+'');
	}
	
	if(result==100){
		if(getted){
			times=4000;
			return;
		}
		getted=true;
		window.location.href='profile_customizedresult.action?portfolioID=${Parameters.portfolioID}&planID=${Parameters.planID}';
	}
}
[#-- get executing information  end --]
 


var host=window.location.host;


