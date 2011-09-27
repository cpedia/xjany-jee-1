function equalizeNav() {
	var totalwidth = $('#blueNavArea').innerWidth(true) + 10;
	var numitems = $('.blueNav_item','#blueNavArea').length;
	var padding = numitems *4;
	var newwidth = (Math.floor(totalwidth/numitems)-padding) + "px";
	$('.blueNav_item','#blueNavArea').each(function(){
		$(this).css("width",newwidth);
	});
	// tweak width of last item. Because of floating-point rounding
	// boxes may overflow. Last box shortened for visual compensation
	var lastitem_w = $('.blueNav_item:last','#blueNavArea').width();
	var tweakWidth = (lastitem_w - 26)+"px"
	$('.blueNav_item:last','#blueNavArea').css({"border":"none", "width":tweakWidth})
	
}

function initTabs() {


	 Cufon.now(); 
	 
	 
	var slide_1 = $('#slide_1').clone();
	$('#slide_1').remove();
	$('#slide1_tab').data("slide", slide_1);

	var slide_2 = $('#slide_2').clone();
	$('#slide_2').remove();
	$('#slide2_tab').data("slide", slide_2);

	var slide_3 = $('#slide_3').clone();
	$('#slide_3').remove();
	$('#slide3_tab').data("slide", slide_3);

	$('.tabBorderRight').click(function(){
		
		//$(this).addClass('tabRoll');
		$('.tab', '#kz_tabs').removeClass('activeTab');
		$(this).addClass('activeTab');
		
		
		//Cufon.replace("#"+$(this).attr("id"), { fontFamily: 'MyriadPro', fontSize:"18px", color:"#000000"});
		var clone = $(this).data('slide').clone();
		$('#slideArea').html(clone)
	}, function(){
		$(this).removeClass('tabRoll');
		Cufon.replace('.tab', { fontFamily: 'MyriadPro', fontSize:"18px", color:"#455395"});
	});
	firstTab('#slide1_tab');
	
}

function firstTab(target) {
	//$(target).addClass('tabRoll');
	
	Cufon.replace('.tab', { fontFamily: 'MyriadPro', fontSize:"18px", color:"#455395"});
	$('.tab', '#kz_tabs').removeClass('activeTab');
	$(target).addClass('activeTab');
	//Cufon.replace(target, { fontFamily: 'MyriadPro', fontSize:"18px", color:"#000000"});
	var clone = $(target).data('slide').clone();
	$('#slideArea').html(clone);
	
}


