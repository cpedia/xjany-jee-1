$(document).ready(function(){	
	/* Function: Show the full name of the portfolio, the full name is stored in the title of the cell. 
	 * Trigger: When the mouse pass over the cell with the id beginning with "shortname"
	 * P.S: ToopTip is a plugin 
	 */
	$('td [id^="portShortName"]').Tooltip({
		track: true,
		delay: 20,
		showURL: false,
		showBody: " - ",
		extraClass: "portfolio"
	});
	
	$('td [id^="straShortName"]').Tooltip({
		track: true,
		delay: 20,
		showURL: false,
		showBody: " - ",
		extraClass: "portfolio"
	});
	
	$('td [id^="shortName"]').Tooltip({
		track: true,
		delay: 20,
		showURL: false,
		showBody: " - ",
		extraClass: "portfolio"
	});
	
	$('td [id^="strategyclass"]').Tooltip({
		track: true,
		delay: 20,
		showURL: false,
		showBody: " - ",
		extraClass: "portfolio"
	});
	$('td [id^="strategystyles"]').Tooltip({
		track: true,
		delay: 20,
		showURL: false,
		showBody: " - ",
		extraClass: "portfolio"
	});
});