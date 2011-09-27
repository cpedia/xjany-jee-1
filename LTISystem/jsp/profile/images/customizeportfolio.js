[#ftl]
[#assign username="MPIQ"]
[#if Session["username"]??]
[#assign username=Session["username"]]
[/#if]
$(function() {
	// clear variables 
	$('#t_planid').val('-1');
	$('#t_portfolioid').val('-1');
	$('#t_planname').val('');
	$('#t_portfolioname').val('');
	
	//load portfolio table
	$('#div_modelportfolios').load('profile_list.action?includeHeader=false');
	
	//show/hide strategy table
	$('#btn_strategy').toggle(
			function(){
				$('#div_strategy').show();
				$('#btn_strategy').val('- Select a Strategy');
			},
			function(){
				$('#div_strategy').hide();
				$('#btn_strategy').val('+ Select a Strategy');
			}
	);
	
	//show/hide strategy table
	$('#btn_plan').toggle(
			function(){
				$('#div_plan').show();
				$('#btn_plan').val('- Select a Plan');
			},
			function(){
				$('#div_plan').hide();
				$('#btn_plan').val('+ Select a Plan');
			}
	);
	
	$('#keyword_plan').keypress(
			function(e){
				if(e.keyCode==13)searchplan();
			}
	);
});


//return plan list with specified keyword 
 function searchplan(){
 	var keyword=$("#keyword_plan").val();
 	
 	if(keyword!="" && keyword.length>2){
 	}else{
 		alert('Please input the keyword with at least two character.');
 		return;
 	}
 	
 	loadUrl('profilehelp_pickplan.action?includeHeader=false&planName='+keyword,'post','','div_plan_content');
 }


function selectplan(id,name){
	$('#t_planid').val(id);
	$('#t_planname').val(name);
	$('#t_portfolioid').val('-1');
	$('#t_portfolioname').val('');
	$('#a_portfolio').html('');
	$('#t_tempname').val('');
	$('#t_strategyid').val('-1');
	$('#t_strategyname').val('');
	$('#a_strategy').html('');
	$('#a_plan').html(name);
	$('#a_plan').attr({'href':'f401k_view.action?ID='+id});
	
	$('#btn_plan').trigger("click");
	
	loadUrl('profilehelp_pickstrategy.action?includeHeader=false&planID='+$('#t_planid').val(),'post','','div_strategy');
	$('#tr_strategy_1').show();
	$('#tr_strategy_2').show();
	
	$('#btn_generate').hide();
	$('#tr_parameter').hide();
	$('#tr_portfolio_1').hide();
	$('#tr_portfolio_2').hide();
	

}

var mode=0;
function selectstrategy(id,name){
	$('#t_strategyid').val(id);
	$('#t_strategyname').val(name);
	$('#t_portfolioname').val('${username}_'+$('#t_planname').val()+'_'+$('#t_strategyname').val()+'_RiskProfile'+'_'+$('#t_risknumber').val());
	$('#tr_parameter').show();
	$('#a_strategy').html(name);
	$('#a_strategy').attr({'href':'/LTISystem/jsp/strategy/View.action?ID='+id});
	mode=0;
	$('#btn_strategy').trigger("click");
	showportfolio(id,name);
	showbutton();
	
	
}

function selectpredefinedportfolio(sid,sname,pid,pname){
	$('#t_portfolioid').val(pid);
	$('#a_strategy').html(sname);
	$('#a_strategy').attr({'href':'/LTISystem/jsp/strategy/View.action?ID='+sid});
	$('#btn_strategy').trigger("click");
	$('#tr_parameter').hide();
	mode=1;
	$('#t_portfolioname').val('${username}_'+pname);
	showportfolio(pid,pname);
	showbutton();
	
}

function showportfolio(id,name){
	$('#tr_portfolio_1').show();
	if(mode==0){
		$('#tr_portfolio_2').hide();
	}else if(mode==1){
		$('#a_portfolio').html(name);
		$('#a_portfolio').attr({'href':'jsp/portfolio/ViewPortfolio.action?ID='+id});
		$('#tr_portfolio_2').show();
	}
}



function generate(){
	showStartMessage();
	if(!check())return;
	
	$('#profile_planid').val($('#t_planid').val());
	$('#strategyid').val($('#t_strategyid').val());
	$('#strategyname').val($('#t_strategyname').val());
	$('#profile_portfolioname').val($('#t_portfolioname').val());
	$('#profile_risknumber').val($('#t_risknumber').val());
	$('#frequency').val($('#t_frequency').val());
	$.ajax({
	    url: 'profile_generateportfolio.action?includeHeader=false',
	    type: 'POST',
	    data: $('#profile_form').serialize(),
	    error: function(message){
	        alert(message.responseText);
	        return;
	    },
	    success: function(result){
	    	if(result.replace(/(^\s*)|(\s*$)/g, '')==-1){
	    		alert('Error, please try again.');
	        	return;
	    	}else{
	    		$('#t_portfolioid').val(result.replace(/(^\s*)|(\s*$)/g, ''));
	    	}
	    	window.location.href='profile_customizing.action?planID='+$('#t_planid').val()+'&portfolioID='+$('#t_portfolioid').val();
	    }
	});
}


function copy(){
	showStartMessage();
	if(!check())return;
	
	$('#profile_planid').val($('#t_planid').val());
	$('#profile_portfolioname').val($('#t_portfolioname').val());
	$('#profile_portfolioid').val($('#t_portfolioid').val());
	$.ajax({
	    url: 'profile_copyportfolio.action?includeHeader=false',
	    type: 'POST',
	    data: $('#profile_form').serialize(),
	    error: function(message){
	        alert(message.responseText);
	        return;
	    },
	    success: function(result){
	    	if(result.replace(/(^\s*)|(\s*$)/g, '')==-1){
	    		alert('Error, please try again.');
	        	return;
	    	}else{
	    		$('#t_portfolioid').val(result.replace(/(^\s*)|(\s*$)/g, ''));
	    	}
	    	window.location.href='profile_customizing.action?planID='+$('#t_planid').val()+'&portfolioID='+$('#t_portfolioid').val();
	    }
	});
}

function go(){
	if(mode==1){
		copy();
	}else{
		generate();
	}
}


function check(){
	if(mode==1){
		return checkcopy();
	}else{
		return checkgenerate();
	}
}

function checkcopy(){
	if($('#t_planid').val()==-1||$('#t_portfolioid').val()==-1||$('#t_portfolioname').val()==-1){
		alert('You must pick a plan or a portfolio first.');
		$('div_message').val('You must pick a plan or a portfolio first.');
		return false;
	}
	var selected=false;
	$.ajax({
	    url: 'profile_checkname.action?includeHeader=false',
	    type: 'POST',
	    async: false,
	    data: 'profile.portfolioName='+$('#t_portfolioname').val(),
	    error: function(){
	        alert('Error loading XML document');
	    },
	    success: function(result){
	    	if(result.indexOf('true')!=-1){
	    		selected=true;
	    	}else{
	    		selected=false;
	    		$('#div_message').html('The name of the portfolio has been used.');
	    	}
	    }
	});
	return selected;
}

function checkgenerate(){
	if($('#t_planid').val()==-1||$('#t_risknumber').val()==''||$('#t_strategyid').val()=='-1'||$('#t_strategyname').val()==''||$('#t_portfolioname').val()==''){
		alert('Please enter the information needed for generating portfolio.');
		$('div_message').val('Please enter the information needed for generating portfolio.');
		return;
	}
	var selected=false;
	$.ajax({
	    url: 'profile_checkname.action?includeHeader=false',
	    type: 'POST',
	    async: false,
	    data: 'profile.portfolioName='+$('#t_portfolioname').val(),
	    error: function(){
	        alert('Error loading XML document');
	    },
	    success: function(result){
	    	if(result.indexOf('true')!=-1){
	    		selected=true;
	    	}else{
	    		selected=false;
	    		$('#div_message').html('The name of the portfolio has been used.');
	    	}
	    }
	});
	
	return selected;
}



function showbutton(){
	$('#div_message').html('');
	$('#btn_generate').show();
	if(mode==0){
		$('#btn_generate').val('Generate');
	}else{
		$('#btn_generate').val('Save As');
	}
}

function showStartMessage(){
	$('#div_message').html('Processing...');
}

function loadUrl(_url,_type,_data,_id){
	$.ajax({
	    url: _url,
	    type: _type,
	    data: _data,
	    error: function(){
	        alert('Error loading XML document');
	    },
	    success: function(result){
	    	if($.browser.msie){
				$('#'+_id)[0].innerHTML=result;
			}else{
				$('#'+_id).html(result);
			}

	    	
	    	
	    }
	});
}
