window.logos = 
	[
		{
		"title":"Comprehensive Plan Coverage",
		"url":"/LTISystem/jsp/template/ed/images/logos/plans.jpg",
		"link":"/LTISystem/f401k__plans.action"
		},

		{
		"title":"401(k), 403(b) Investments",
		"url":"/LTISystem/jsp/template/ed/images/logos/logos_1.gif",
		"link":"/LTISystem/f401k__plans.action"
		},
		
		{
		"title":"IRA, Taxable ETF Portfolios",
		"url":"/LTISystem/jsp/template/ed/images/logos/logos_2.gif",
		"link":"/LTISystem/f401k__plans.action"
		},	
		
		{
		"title":"Variable Annuities, VUL Investments",
		"url":"/LTISystem/jsp/template/ed/images/logos/logos_3.gif",
		"link":"/LTISystem/f401k__plans.action"
		}		
	]




function logoRotator() {


	

	if (window.loopindex == "undefined" || window.loopindex == null) {
		window.loopindex = 0;
		
	} else {
		if (window.loopindex == window.logos.length-1) {
			window.loopindex = 0;
		} else {
			window.loopindex++
		}
	}
        

 	$('#logoRotation').animate({ opacity: 0 }, 10, function() {
  	  		$('#logoRotation .logoRotator')
			.empty().append('<img>').find('img').attr("src",window.logos[window.loopindex].url)
			.wrap('<a>').parent().attr('href', window.logos[window.loopindex].link)
			.parent().parent().find('h3').text(window.logos[window.loopindex].title);
			
									Cufon.replace ('#logoTitleText', { fontFamily: 'Franklin Gothic Medium Cond' , fontSize:"20px"});

			$('#logoRotation').animate({opacity: 1}, 300, function() {
						setTimeout('logoRotator()', 7500);
			});	
 	 });	
}

