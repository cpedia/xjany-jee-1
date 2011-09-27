//---------------------------------------------------------------------------------------------------
// PAGE INIT
//---------------------------------------------------------------------------------------------------
function initUI_elements(){
	initButtons();
	initTable();
	initRegWindow();
	initTabs()
	DD_roundies.addRule('.sidebar_box, .sidebar_box_noPadding', '8px', true);
	addPopuplink(".openDialog", 'Dialog', "This is a Dialog");
	ieInputFix();
	initSlider();
	initGuidePost();
	initSelects("styleSelect", 150);
}



//---------------------------------------------------------------------------------------------------
// GUIDEPOST
//---------------------------------------------------------------------------------------------------

function initSelects(className, width) {
	var selector = "."+className;
	$('select').filter(selector).selectmenu({
		maxHeight: 130,
		style: 'dropdown',
		width: width,
		menuWidth: width
	});
}


//---------------------------------------------------------------------------------------------------
// GUIDEPOST
//---------------------------------------------------------------------------------------------------


function initGuidePost() {
	DD_roundies.addRule('.guidepostItem', '8px', true);
	var items = $("#guidepost_decide, #guidepost_manage, #guidepost_customize");
	$("#guidepost_decide, #guidepost_manage, #guidepost_customize").each(function(){
		$(this).wrap('<div class="guidepostIEFIX"></div>').parent().data("active", false);
	});
	
}


function activeGuidebutton(choice) {
	var items = $("#guidepost_decide, #guidepost_manage, #guidepost_customize");
	switch(choice) {
		case "decide": 
			guidePost_item_active( $('#guidepost_decide'), items);
			break;
		case "manage": 
			guidePost_item_active( $('#guidepost_manage'), items);
			break;
		case "customize": 
			guidePost_item_active( $('#guidepost_customize'), items);
			break;
	}
}


function guidePost_item_active(guidepost, items) {
	$(guidepost).data("active", true).animate({
		"borderTopColor":"#54a136",
		"borderRightColor":"#54a136",
		"borderLeftColor":"#54a136",
		"borderBottomColor":"#54a136",
		opacity: 1
	  }, 200, function() {
	});
	$(items).not(guidepost).data("active", false).animate({
		"borderTopColor":"#cccccc",
		"borderRightColor":"#cccccc",
		"borderLeftColor":"#cccccc",
		"borderBottomColor":"#cccccc",
		opacity: 0.3
	  }, 200, function() {
	});
}


//---------------------------------------------------------------------------------------------------
// SLIDER
//---------------------------------------------------------------------------------------------------


function initSlider() {
	DD_roundies.addRule('.datesliderContainer', '8px', true);
	prepSlider_tick();
	window.startdate = new Date('01/01/2010');
	window.enddate = new Date('12/31/2010');
	$("body").append('<div id="datereadout1" class="datereadout"></div>');
	$("body").append('<div id="datereadout2" class="datereadout"></div>');
	var pos = $("#slider1").offset();
	var h =	 $("#slider1").outerWidth(true);
	var sliderposLeft = $("#slider1").offset();
	var sliderposRight = (sliderposLeft.left + h);
	$("#datereadout1").css({"left":sliderposLeft.left - 50, "top":pos.top-33});
	$("#datereadout2").css({"left":sliderposRight - 50, "top":pos.top-33});
	$("#startdate").datepicker({
			 onSelect: function(dateText, inst) {updateSliders();}
		}).datepicker( "setDate" , convertDate(startdate));
	$("#enddate").datepicker({
			onSelect: function(dateText, inst) {	updateSliders();}
		}).datepicker( "setDate" , convertDate(enddate));
	updateHandleText(convertDate(startdate), convertDate(enddate));
	$("#slider1").empty().slider({
		range: true,
		min: 0,
		max: 364,
		values: [0, 364],
		change: function(event, ui) {
			var newdateLeft = new Date(convertDate(window.startdate));
			var newdateRight = new Date(convertDate(window.startdate));
			newdateLeft.setDate(startdate.getDate() + ui.values[0]) ;
			newdateRight.setDate(startdate.getDate() + ui.values[1]) ;
			$("#startdate").datepicker( "setDate" , convertDate(newdateLeft));
			$("#enddate").datepicker( "setDate" , convertDate(newdateRight));
			updateSlideReadoutPos(convertDate(newdateLeft), convertDate(newdateRight));
		},
		slide: function(event, ui) {
			var newdateLeft = new Date(convertDate(window.startdate));
			var newdateRight = new Date(convertDate(window.startdate));
			newdateLeft.setDate(startdate.getDate() + ui.values[0]) ;
			newdateRight.setDate(startdate.getDate() + ui.values[1]) ;
			$("#startdate").datepicker( "setDate" , convertDate(newdateLeft));
			$("#enddate").datepicker( "setDate" , convertDate(newdateRight));
			updateSlideReadoutPos(convertDate(newdateLeft), convertDate(newdateRight));
		}
	});
	initializeNewSlideValues('04/05/2010', '11/19/2010');
}

function initializeNewSlideValues(datestring1, datestring2){
	$("#startdate").datepicker( "setDate" , datestring1);
	$("#enddate").datepicker( "setDate" , datestring2);
	updateSliders();
	updateSlideReadoutPos(datestring1, datestring2);
}

function updateSliders() {
		var n_startDate = ($("#startdate").datepicker('getDate'));
		var n_endDate = ($("#enddate").datepicker('getDate'));
		var slideVal1 = days_between(window.startdate, n_startDate);
		var slideVal2 = days_between(window.startdate, n_endDate);
		$("#slider1").slider("values", 0 , slideVal1);
		$("#slider1").slider("values", 1 , slideVal2);
		updateHandleText(convertDate(n_startDate), convertDate(n_endDate));
}

function prepSlider_tick() {
	var cellwidth = Math.floor(($(".slider_tick").outerWidth(true))/12);
	$(".slider_tick td").css({"width": cellwidth});
}

function updateSlideReadoutPos(datestring1, datestring2) {
		var offset = 40;
		sliderposLeft = $("#slider1 .ui-slider-handle:first").offset();
		sliderposRight = $("#slider1 .ui-slider-handle:last").offset();
		$("#datereadout1").css({"left":sliderposLeft.left - offset});
		$("#datereadout2").css({"left":sliderposRight.left - offset});
		updateHandleText(datestring1, datestring2);
}

function updateHandleText(datestring1, datestring2) {
		$("#datereadout1").text(datestring1);
		$("#datereadout2").text(datestring2);
}

function convertDate(thedate) {
	var YY = thedate.getFullYear();
	var MM = thedate.getMonth()+1;
	var DD = thedate.getDate();
	var datestring = padDigit(MM) +'/'+ padDigit(DD) +'/'+ YY;
	return datestring;
}

function days_between(date1, date2) {
    var ONE_DAY = 1000 * 60 * 60 * 24
    var date1_ms = date1.getTime()
    var date2_ms = date2.getTime()
    var difference_ms = Math.abs(date1_ms - date2_ms)
    return Math.round(difference_ms/ONE_DAY)
}


//---------------------------------------------------------------------------------------------------
// OTHER FUNCTIONS
//---------------------------------------------------------------------------------------------------

function ieInputFix() {
		$('input').css("margin-top","-1px");
}
	
	
	
function initTabs() {
	$("#tabs").tabs();
}


function initButtons () {
	$(".uiButton").button();
	$("#submitButton2").button();
}

function initTable () {
		$.tablesorter.defaults.widgets = ['zebra']; 
		$("#exampleTable").tablesorter({
			sortClassAsc: 'headerSortUp',	
			sortClassDesc: 'headerSortDown',	
			headerClass: 'header'	
		}); 
}


function addPopuplink(target, title, message) {
	$(target).click(function(e){
		$('#popup').dialog("destroy").remove();
		var x = e.pageX;
   		var y = e.pageY - $(document).scrollTop()-100;
		$('body').append('<div id="popup">'+message+'</div>');
		$('#popup').dialog({
			title: title,
			width: 280,
			resizable: false,
			modal: false,
			position: [x, y]
		});	
		return false;
	});
}



function initRegWindow () {
		var target = ['#regSignin', '.uiButton', '#searchPlanInput2', '.submitSearch', '#submitButton2'];
		var title = "Signup or Register";
		var regContent = '<table id="regTable" class="regTable" width="200" height="62" border="0" cellpadding="0" cellspacing="0"> <tr> <td>&nbsp;</td> <td>&nbsp;</td> <td>&nbsp;</td> </tr> <tr> <td width="24">User</td> <td width="16">&nbsp;</td> <td width="147"><input type="text" name="textfield3" id="textfield3" /></td> </tr> <tr> <td>Pass</td> <td>&nbsp;</td> <td><input type="text" name="textfield4" id="textfield4" /></td> </tr> <tr> <td>&nbsp;</td> <td>&nbsp;</td> <td>&nbsp;</td> </tr> </table> </div>';
	$.each(target, function(i, val){
		$(val).click(function(){
			$('#popup2').dialog("destroy").remove();
			$('body').append('<div id="popup2"></div>');
			$('#popup2').html(regContent);
				$('#popup2').dialog({
					title: title,
					width: 280,
					resizable: false,
					modal: true,
					position: "center",
					buttons: { 
					"Submit": function(){
						$(this).dialog("close");
						},
					"Cancel": function(){
						$(this).dialog("close");
						}
					}
			});	
			return false;		
		});
	});
}



function padDigit(digit) {
	var newDigit = String(digit)
	if (newDigit.length<2) {
		newDigit = "0"+newDigit
	}
	return(newDigit) ;
}
