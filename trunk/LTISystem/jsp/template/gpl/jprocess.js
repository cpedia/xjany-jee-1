/*
 *	jQuery Editable Table plugin
 *	version: 0.01 (13 July 2009)
 *
 *	@Author: Michael Chua
 *
 *
 */
;(function( jQuery ) {
	
	
	jQuery.fn.jprocess = function( args ) {
		args = jQuery.extend( {
			id:null,
			interval:5000,
			times:4000,
			onChange:null
		}, args || {} );
		
		try{
			_jprocess_timer.stop();
		}catch(error){
		}
		
		var host=window.location.host;
		
		_jprocess_max_times=args.times;
		
		_jprocess_timer=$.timer(args.interval, function (timer) {
			if(++_jprocess_request_times>=args.times){
				timer.stop();
			}
		    var s = document.createElement("SCRIPT");
		    s.id="process_state"; 
		    document.getElementsByTagName("HEAD")[0].appendChild(s);
		    d=new Date();
		    var host=window.location.host;
			if($('#run_in_local_flag').attr('checked')){
				host='localhost';
			}
		    s.src='http://'+host+':8081/state?portfolioID='+args.id+'&timestamp=-1&function=_jprocess_setProcess';
		});
		
		_jprocess_error_times=0;
		_jprocess_request_times=0;
		$(this).progressbar({
			value: 0
		});
		
		_jprocess=this;
		
		_jprocess_onchange=args.onChange;
		
		return this;
	};
	

} )(jQuery);

var _jprocess_timer;

var _jprocess_error_times=0;

var _jprocess_request_times=0;

var _jprocess_max_times;

var _jprocess;

var _jprocess_onchange;

function _jprocess_setProcess(data){
	document.getElementsByTagName("HEAD")[0].removeChild(document.getElementById("process_state")); 
	var result=data.state;
	if(result<0){
		_jprocess_error_times++;
	}
	if(_jprocess_error_times>40){
		_jprocess_request_times=_jprocess_max_times;
	}else if(result!=100){
		$(_jprocess).progressbar({
			value: result
		});
		$("#number").html(result+"%");
	}
	
	if(result==100){
		$(_jprocess).progressbar({
			value: result
		});
		
		_jprocess_request_times=_jprocess_max_times;
	}
	if(_jprocess_onchange!=null){
		_jprocess_onchange(data.state);
	}
};