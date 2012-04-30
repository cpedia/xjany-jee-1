$(document).ready(function() {

//if close button is clicked
$('.window .close').click(function (e) {
	//Cancel the link behavior
	e.preventDefault();	
	$('#mask').hide();
	$('.window').hide();
});

//if mask is clicked
$('#mask').click(function () {
	$(this).hide();
	$('.window').hide();
});

 $('input.searchDomain').focus();
});
	  
function isDomain(domain){
	var domainRegexp =  /^[a-zA-Z0-9][a-zA-Z0-9_\-]{0,61}([a-zA-Z0-9]+\.?){0,2}[a-zA-Z0-9]$/;
	if(!new RegExp(domainRegexp).test(domain)){		
		alert('Bad domain format');
		return;
	}
}
	  
function checkDomain() {	
	var domain = $('input.searchDomain').val();	
	isDomain(domain);
	var check_url = "/portal/?command=check-domains&domain=" + domain;	
	$('.searchPanel .wait').show();
	$.ajax({
		  url: check_url,
		  type: 'get',
		  dataType: "html",
		  success: function(date){			  
			  $('.top-block').find('.dBlank').html(date);
			  showCheckDomain();
			  checkDomains();
			}	
		});
}

function checkDomainNew(){	
	var domain = $('input.searchDomainNew').val();
	isDomain(domain);
	var check_url = "/portal/?command=check-domains&domain=" + domain;	
	$('.searchPanel .wait').show();
	$.ajax({
		  url: check_url,
		  type: 'get',
		  dataType: "html",
		  success: function(date){			  
			  $('.top-block').find('.dBlank').html(date);
			  showCheckDomain();
			  checkDomains();
			}	
		});
}

function checkDomains() {
	$('img.wait').show();
	$('.searchPanel .wait').hide();
	$('div.available').hide();
	$('div.notavailable').hide();
	
	$('tr.domainRow').each(function(i){
			var domain = $('td.domain',this).text();
			var tld = $('td.tld',this).text();
			chackDomain(domain,tld);
	});
}

function chackDomain(domain,tld){
		var check_url = "/portal/?command=check-domain&domain=" + domain + "&tld=" + tld;
		$.ajax({
		  url: check_url,
		  dataType: "xml",
		  success: function(xml){
			  	var node = find_node(xml,'negeso:domainChekingResult');
				if ($(node).attr('status') == "FREE"){
					setStatus(domain,tld,true); 
				}else {
					setStatus(domain,tld,false); 
				}
				
			}
		});
}

function setStatus(domain,tld,isFree){
	$('tr.domainRow').each(function(i){
			if($('td.domain',this).text() == domain && $('td.tld',this).text() == tld){
				if(isFree){
					$('div.available',this).show();
				}else{
					$('div.notavailable',this).show();
					$('input[type=checkbox]',this).attr('disabled', true);
				}
				$('img.wait',this).hide();
			}
	});
}

function sendDomain() { 	 
	 var params= "act=add_domains";
	 $('tr.domainRow').each(function (i){
									
		if($('input[type=checkbox]',this).is(':checked') ){
				var domain = $('td.domain',this).text();
				var tld = $('td.tld',this).text();
				
				params += '&domain'+ (i+1) + "=" + domain + '&tld' + (i+1) + '=' + tld;
			}
	
	
		});
	if (params == "act=add_domains"){
	 	alert('Please select a domain!');
	return;
	 }
	 
	 $('input.buyBtn').attr("disabled", "true");
	 $('img.wait_buy').show();	
	 
	 	window.location="http://portal.sitementrix.com/invoice.html?" + params;
 
	  /* $.ajax({           
				url:'/portal/invoice_nl.html',
				data:params,
				type: 'POST',           
				success: function(data){window.location="http://portal.sitementrix.com/invoice.html"}
		});*/
};


function showCheckDomain() {
		//Cancel the link behavior
		//Get the screen height and width
		var maskHeight = $(document).height();
		var maskWidth = $(window).width();
		
		//Set heigth and width to mask to fill up the whole screen
		$('#mask').css({'width':maskWidth,'height':maskHeight});
		
		//transition effect
		$('#mask').fadeIn(100);
		$('#mask').fadeTo("slow",0.4);
		
		//Get the window height and width
		var winH = $(window).height();
		var winW = $(window).width();
		
		//Set the popup window to center
		var objPopup = $('#dialog');
		//$(objPopup).css('top',  winH/2-$(objPopup).height()/5);
		$(objPopup).css('left', winW/2-$(objPopup).width()/2);
		
		//transition effect
		$(objPopup).fadeIn(100);
	}




function find_node(node,name) {
	var res=null;
	if (node.nodeType==1 && node.nodeName.toLowerCase()==name.toLowerCase()) {
		res=node;
	} else {
		for (var i=0; i<node.childNodes.length; i++) {
			res=find_node(node.childNodes[i],name);
			if (res) break;
		}
	}
	return res;
}




