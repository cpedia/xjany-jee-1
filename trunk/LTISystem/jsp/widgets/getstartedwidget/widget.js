//装载widget的主函数
//如果参数中包含simple则仅构造搜索结果
//否则将包含搜索框
function mpiq_widget_${widgetName}(mpiq_id,mpiq_params){
	var params=mpiq_parse_params(mpiq_params);
	$("#"+mpiq_id).html("${ui}");
	$("#mpiq_widget_${widgetName}_display_div").html($("#mpiq_widget_${widgetName}_welcome_div").html());
	mpiq_widget_${widgetName}_his_put($("#mpiq_widget_${widgetName}_welcome_div").html());
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/myportfolio.action";
	mpiq_load_data_from_server(url,function(data){
		show_myplan();
		if(data.UserName!="ANONYMOUS"&&data.UserName!=undefined){
			$("#mp_login").children().remove();
			$("#mp_login").append("<font color='FFFFFF'>Hello,"+data.UserName+"&nbsp&nbsp</font>");
			for(var i=0;i<data.myportfolio.length;i++){
				$(".appendthis").append("<tr><td><a onclick=\"mpiq_widget_portfoliowidget('mpiq_widget_getstartedwidget_display_div','portfolioID="+data.myportfolio[i].id+"')\">"+data.myportfolio[i].name+"</a></td><td>"+data.myportfolio[i].oyear+"</td><td>"+data.myportfolio[i].tyear+"</td><td>"+data.myportfolio[i].fyear+"</td></tr>");
			}
		}else{
			$(".appendthis").append("<tr><td>You are ANONYMOUS,Please login befor</td><td></td><td></td></tr>");
		}
	});
	/*jQuery('#mpiq_widget_getstartedwidget_display_div').children('#mp_lista').accordion({ 
	    active: 0,
	    alwaysOpen: false, 
	    autoheight: false
	});*/
	$('.mp_accordion').collapser({
		target: 'next',
		targetOnly: 'div',
		changeText: 0,
		expandClass: 'expArrow',
		collapseClass: 'collArrow'
	});
	$('.mp_accordion2').collapser({
		target: 'next',
		targetOnly: 'div',
		changeText: 0,
		expandClass: 'expIco',
		collapseClass: 'collIco'
	});
	$('.mp_accordion3').collapser({
		target: 'next',
		targetOnly: 'div',
		changeText: 0,
		expandClass: 'expIco',
		collapseClass: 'collIco'
	});
}

//装载数据
//mpiq_id：表格容器ID
//tid：生成的表格的id
//mpiq_params:请求数据的参数，包含keyword
function mpiq_widget_${widgetName}_search_from_server(mpiq_id,tid,mpiq_params){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/list.action?"+mpiq_params;
	mpiq_load_data_from_server(url,function(data){
		data.titles=new Array();
		data.titles[0]="Plan Name";
		if(data.size>0){
			var table=mpiq_load_table_from_json(data,tid,function(item,header){
				if(header=="name"){
					return "<a href='javascript:void(0)' onclick='mpiq_widget_${widgetName}_view_plan("+item.id+")'>"+item.name+"</a>";
				}else{
					return item[header];
				}
			});
			$("#"+mpiq_id).html(table);
			//构造历史
			mpiq_widget_${widgetName}_his_put(table);
		}else{
			$("#"+mpiq_id).html($("#mpiq_widget_${widgetName}_nofound_div").html());
			//构造历史
			mpiq_widget_${widgetName}_his_put($("#mpiq_widget_${widgetName}_nofound_div").html());
		}
		
	});
}


var mpiq_widget_${widgetName}_search_table_id;
//搜索函数
function mpiq_widget_${widgetName}_search(){
	var map=new mpiq_Map();
	map.put("keyword",$("#mpiq_widget_${widgetName}_keyword").val());
	mpiq_widget_${widgetName}_search_table_id="mpiq_widget_${widgetName}_search_table_id"+new Date().getTime();
	mpiq_widget_${widgetName}_search_from_server("mpiq_widget_${widgetName}_display_div",mpiq_widget_${widgetName}_search_table_id,map.toString());
}


//实现前进后退
var mpiq_widget_${widgetName}_his=new Array();
var mpiq_widget_${widgetName}_his_current=-1;
var mpiq_widget_${widgetName}_his_size=0;
//后退
function mpiq_widget_${widgetName}_his_back(){
	if(mpiq_widget_${widgetName}_his_current == 0 || 
		mpiq_widget_${widgetName}_his_size == 0
		)return;
	mpiq_widget_${widgetName}_his_current--;
	$("#mpiq_widget_${widgetName}_display_div").html(mpiq_widget_${widgetName}_his[mpiq_widget_${widgetName}_his_current]);
	//判断是否显示前进/后退按钮
	
	$('.mp_accordion').collapser({
		target: 'next',
		targetOnly: 'div',
		changeText: 0,
		expandClass: 'expArrow',
		collapseClass: 'collArrow'
	});
	
	mpiq_widget_${widgetName}_his_set_button();
}
//前进
function mpiq_widget_${widgetName}_his_forward(){
	if(mpiq_widget_${widgetName}_his_current == mpiq_widget_${widgetName}_his_size-1 || 
		mpiq_widget_${widgetName}_his_size == 0
		)return;
	mpiq_widget_${widgetName}_his_current++;
	$("#mpiq_widget_${widgetName}_display_div").html(mpiq_widget_${widgetName}_his[mpiq_widget_${widgetName}_his_current]);
	$('.mp_accordion').collapser({
		target: 'next',
		targetOnly: 'div',
		changeText: 0,
		expandClass: 'expArrow',
		collapseClass: 'collArrow'
	});
	mpiq_widget_${widgetName}_his_set_button();
}
//增加历史记录
function mpiq_widget_${widgetName}_his_put(html){
	mpiq_widget_${widgetName}_his_current++;
	mpiq_widget_${widgetName}_his_size=mpiq_widget_${widgetName}_his_current+1;
	mpiq_widget_${widgetName}_his[mpiq_widget_${widgetName}_his_current]=html;
	mpiq_widget_${widgetName}_his_set_button();
}
//重设前进后退的状态
function mpiq_widget_${widgetName}_his_set_button(){
	if(mpiq_widget_${widgetName}_his_size<=1){
		$("#mpiq_widget_${widgetName}_back_button").attr("disabled","true");
		$("#mpiq_widget_${widgetName}_forward_button").attr("disabled","true");
	}else
	if(mpiq_widget_${widgetName}_his_current >= mpiq_widget_${widgetName}_his_size-1){
		$("#mpiq_widget_${widgetName}_back_button").removeAttr("disabled");
		$("#mpiq_widget_${widgetName}_forward_button").attr("disabled","true");
	}else
	if(mpiq_widget_${widgetName}_his_current <= 0){
		$("#mpiq_widget_${widgetName}_back_button").attr("disabled","true");
		$("#mpiq_widget_${widgetName}_forward_button").removeAttr("disabled");
	}else{
		$("#mpiq_widget_${widgetName}_back_button").removeAttr("disabled");
		$("#mpiq_widget_${widgetName}_forward_button").removeAttr("disabled");
	}
	
}
//查看plan的相关的信息
function mpiq_widget_${widgetName}_view_plan(planid){
	mpiq_widget_planwidget("mpiq_widget_${widgetName}_display_div","planID="+planid+"&basic_inf=1");
	//mpiq_widget_${widgetName}_his_put($("#mpiq_widget_${widgetName}_welcome_div").html());
}
//查看plan的回调函数
function mpiq_widget_planwidget_load_basic_inf_callback(html){
	//mpiq_widget_planwidget("mpiq_widget_${widgetName}_display_div","planID="+planid+"&list_model_portfolios=1");
	mpiq_widget_${widgetName}_his_put(html);
}
//查看portfolio的回调函数
function mpiq_widget_portfoliowidget_callback(html){
	mpiq_widget_${widgetName}_his_put(html);
}


//创建plan的回调函数
//返回id
function mpiq_widget_createplanwidget_save_callback(data,id){
	if(data.code!=undefined&&data.code==400){
		if(id==undefined)
			alert("Created successfully");
		else
			alert("Update successfully");
		mpiq_widget_${widgetName}_view_plan(data.id);
		show_myplan();
	}else{
		alert(data.msg);
	}
}
//创建plan前的回调函数
function mpiq_widget_createplanwidget_callback(text){
	mpiq_widget_${widgetName}_his_put(text);
}


function keyPress(e){
	var k = window.event?window.event.keyCode:e.which;
	if(k==13){
		mpiq_widget_${widgetName}_search();
	}
}
function mp_register()
{
	var Email = $("#Email").val();
	var RegisterUserName = $("#RegisterUserName").val();
	var RegisterPassWord = $("#RegisterPassWord").val();
	var ConfirmPassWord = $("#ConfirmPassWord").val();
	var Address = $("#Address").val();
	if(RegisterUserName=="" || RegisterPassWord=="" || Email=="")
	{
		alert("Can't be empty" +RegisterUserName);
		return;
	}
	else if(RegisterPassWord != ConfirmPassWord)
	{
		alert("Password is not consistent");
		return;
	}
	$("#mp_register").css("display","none");
	var url = "http://${serverName}:${port}/LTISystem/widgets/${widgetName}/register.action?RegisterUserName="+RegisterUserName+"&RegisterPassWord="+RegisterPassWord+"&Email="+Email+"&Address="+Address;
	mpiq_load_data_from_server(url,function(data){
		alert(data.message);
	});
}

function mp_login(){
	var UserName = $("#UserName").val();
	var PassWord = $("#PassWord").val();
	if(UserName==""||PassWord=="") {
		alert("can't be empty");
		return;
		}
	$("#mp_loading2").css("display","none");
	var url = "http://${serverName}:${port}/LTISystem/j_acegi_security_check?j_username="+UserName+"&j_password="+PassWord+"&ajax=ajax&jsoncallback=?";
	mpiq_load_data_from_server(url,function(data)
	{
		if(data.success==true){
			$(".appendthis").children().remove(); 
			$("#mp_login").children().remove();
			$("#mp_login").append("<font color='white'>Hello,"+data.name+"&nbsp&nbsp</font>");
			var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/myportfolio.action";
			mpiq_load_data_from_server(url,function(data){
				for(var i=0;i<data.myportfolio.length;i++){
					$(".appendthis").append("<tr><td><a onclick=\"mpiq_widget_portfoliowidget('mpiq_widget_getstartedwidget_display_div','portfolioID="+data.myportfolio[i].id+"')\">"+data.myportfolio[i].name+"</a></td><td>"+data.myportfolio[i].oyear+"</td><td>"+data.myportfolio[i].tyear+"</td><td>"+data.myportfolio[i].fyear+"</td></tr>");
				}
			});
			
			show_myplan();
		}else{
			alert("Sorry, login failed. Please enter your correct user name and password again!");
		}
	})
}

function show_myplan(){
	var planurl="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/myPlan.action";
	mpiq_load_data_from_server(planurl,function(data){
		for(var i=0;i<data.myplan.length;i++){
			$(".planappendhere").append("<tr><td><a onclick=\"mpiq_widget_createplanwidget('mpiq_widget_${widgetName}_display_div','noparams','"+data.myplan[i].id+"')\">"+data.myplan[i].name+"</td><td><button onclick='mpiq_widget_${widgetName}_view_plan("+data.myplan[i].id+");'>View</button></td><td><button onclick=\"mpiq_widget_createplanwidget('mpiq_widget_${widgetName}_display_div','noparams','"+data.myplan[i].id+"')\">Modify</button></td></tr>");
		}
	});
}

function cancel(id){
	$(".mp_container").css("opacity","1");
	$("#"+id).css("display","none");
}

function open_register(){
	$("#mp_loading2").css("display","none");
	$("#mp_register").css("display","block");
}
function open_login(){
	$(".mp_container").css("opacity","0.2");
	$("#mp_loading2").css("display","block");
}
function return_login(){
	$("#mp_register").css("display","none");
	$("#mp_loading2").css("display","block");
}

function keyLogin(e){
	var k = window.event?window.event.keyCode:e.which;
	if(k==13){
		mp_login();
	}
}

function keyNext(event){
	if (window.event.keyCode==13) 
	{
		window.event.keyCode=9;
	}
}
