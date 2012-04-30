$(document).ready(function() {

//if close button is clicked
$('.window .webPollclose').click(function (e) {
	//Cancel the link behavior
	e.preventDefault();	
	$('#webPollmask').hide();
	$('.window').hide();
	set_cookie ();
});

//select all the a tag with name equal to modal
window.onload=function(){
	if (get_cookie('answered_on_poll') != null) {
		return;
	}
	//Cancel the link behavior
	//Get the screen height and width
	var webPollmaskHeight = $(document).height();
	var webPollmaskWidth = $(window).width();
	
	//Set heigth and width to webPollmask to fill up the whole screen
	$('#webPollmask').css({'width':webPollmaskWidth,'height':webPollmaskHeight});
	
	//transition effect
	$('#webPollmask').fadeIn(100);
	$('#webPollmask').fadeTo("slow",0.4);
	
	//Get the window height and width
	var winH = $(window).height();
	var winW = $(window).width();
	
	//Set the popup window to center
	var objPopup = $('#webPolldialog');
	//$(objPopup).css('top',  winH/2-$(objPopup).height()/5);
	$(objPopup).css('left', winW/2-$(objPopup).width()/2);
	
	//transition effect
	$(objPopup).fadeIn(100);
}


});


function sendAnswers() {
	var form = document.forms['answersForm'];
	if (validate(form)) {
		var data = "";
		for(i = 1; i<=4; i++) {
			data += '&answer_' + i + '=' + $('input[name=answer_' + i + ']').val();
		}
		$.ajax({
			type: "POST",
			url: "/poll_port?act=answer" + data,
			dataType: "html",
			async: false,
			success: function(html, stat) {
				$('#couponNumber').html('<b>' + html + '</b>');
				$('#invisible1').css('display', 'block');
				$('#invisible2').css('display', 'block');
				set_cookie ();
			},
			error: function(html, stat) {
				alert('Internal server error');
			}
		});
	}
}

function closePopup() {
	$('#webPollmask').hide();
	$('.window').hide();
	set_cookie ();
}

function sentVoucher() {
	var form = document.forms['answersForm'];
	if (validate(form)) {
		$.ajax({
			type: "POST",
			url: "/poll_port?act=send&email=" + $('#userEmail').val(),
			dataType: "html",
			async: false,
			success: function(html, stat) {
				closePopup();
			},
			error: function(html, stat) {
				alert('Internal server error');
			}
		});
	}
}

function get_cookie ( cookie_name ){
  var results = document.cookie.match ( '(^|;) ?' + cookie_name + '=([^;]*)(;|$)' );

  if ( results )
    return ( unescape ( results[2] ) );
  else
    return null;
}

function set_cookie () {
	var cookie_date=new Date();  // current date & time
	cookie_date.setTime ( cookie_date.getTime() + (1000*60*60*24*7) ); // +1 week
	document.cookie = "answered_on_poll=yes;expires=" + cookie_date.toGMTString();
}