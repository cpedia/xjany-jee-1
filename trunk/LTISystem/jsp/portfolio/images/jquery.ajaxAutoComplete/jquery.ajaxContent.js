/**
 *ajaxContent - jQuery plugin for accessible, unobtrusive and easy ajax behaviour.
 * @Version 2.0
 * 
 * @requires jQuery v 1.0.1
 * 
 * http://www.andreacfm.com/jquery-plugins
 *
 * Copyright (c) 2007 Andrea Campolonghi (andreacfm.com)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 */
 //make $ surely available inside the PI as jQuery shortcut
(function($) {
	//call teh method with options arguments
	$.fn.ajaxContent = function(options) {
		//extend to defaults
		var defaults = $.extend({}, $.fn.ajaxContent.defaults, options);
			// debug if required		
			if(defaults.debug == 'true'){
				debug(this);
			};
			//Initilaize any instance looping on the match element
			return this.each(function(){
				//set local variables and extend to the metadata plugin if loaded
				var $obj = $(this);
				var o = $.meta ? $.extend({}, defaults, $obj.data()) : defaults;
				//make defaults local variables to prevent to be overwrite from next plugin calls
				var url = $obj.attr('href');
				var $target = $(o.target);

			//bind the event
			$obj.bind(o.event, function(){
				// add loader if required
				if(o.loader == 'true'){
					if(o.loaderType == 'img'){
							o.loadingMsg = '<img src=\"' + o.loadingMsg + '\"/>';
						}
					$target.html(o.loadingMsg);
				}	
					//remove add current class
					$('a.' + o.currentClass).removeClass(o.currentClass);								
					$obj.addClass(o.currentClass);
					// make the call
					url = $obj.attr('href');
					var ip=document.location.hostname;
					var port=document.location.port;
					if(port=="")port='80';
					$.ajax({ 
  						type: "post", 
  						url: UrlEncode(url),
  						dataType:"text/html; charset=UTF-8",
  						success: function(msg){ 
    						$target.html(msg);
    						//alert(msg);
    						//if a callback exist pass arguments ( object,target and receive message)
    						if(typeof o.success == 'function'){
    							o.success($obj,$target,msg);
    							}  						
    						},
						error: function(){
							$target.html("<p>" + o.errorMsg + "</p>");
							if(typeof o.error == 'function'){
    							o.error($target);
    							}  						
    					 
						}
					});
				return false;
			});
		});
	};	
  	function debug($obj) {
    if (window.console && window.console.log)
     window.console.log('selection count: ' + $obj.size() + '  with class:' + $obj.attr('class'));
  };
		
})(jQuery);

$.fn.ajaxContent.defaults = {
		target: '#ajaxContent',
		type:'get',
		event:'click',
		loader:'true',
		loaderType:'text',
		loadingMsg:'Loading...',
		errorMsg:'An error occured durign the page requesting process!',
		currentClass:'selected',
		success:'',
		error:'',
		debug:'false'
};


