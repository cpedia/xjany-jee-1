$(document).ready(function(){
	if(pfConfigurator)runPf('AnimatedPictureFrame', pfConfigurator);
	//debugger;
	var winW = $(document).width();
	
	$('div.tickertape').css('width', winW);
	$('div.footer').css('width', winW);
});

/*$(window).resize(function() {
  $('body').prepend('<div>' + $(window).width() + '</div>');
});*/


function runPf(name, conf){
	try{
		if (document.getElementsByName(name).length>1){
			/* Give  params to embed for FF */
			document.getElementsByName(name)[1].init(conf);
		}else{
			/* Give  params to object for IE */
			document.getElementsByName(name)[0].init(conf);
		}
	}
	catch(e){
		setTimeout(function(){runPf(name, conf)}, 200);
	}
}


function openDialog(params)
{
	params = params||{};

	
	var dialog = window.showModalDialog('/dialogs/commonPopupWindow.html',
		params, 'center:yes; dialogHeight:550px;resizable:yes;dialogWidth:550px ')

	if(params.answer.resCode == 'OK')
	{
		return params.answer;
	}	
}

// Lang images pre-load
var lang_en_selected=new Image();
var lang_nl_selected=new Image();
lang_en_selected.src="/site/core/images/flag-enr.gif";
lang_nl_selected.src="/site/core/images/flag-nlr.gif";
	// Menu images pre-load
var menu_bg_1 = new Image();
menu_bg_1.src = "/site/core/images/menu_bg.gif";
var menu_bg_2 = new Image();
menu_bg_2.src = "/site/core/images/bg-td-menu.gif";
var menu_bg_3 = new Image();
menu_bg_3.src = "/site/core/images/menu_bgr.gif";
