//=============================================================================
//一些辅助的javascript
//=============================================================================
function mpiq_load_table_from_json(json,tid,fn){
	var table="<table class='mpiq_table' border=0 cellspace=0 id='"+tid+"'>";
	if(json.caption!=undefined&&json.caption!=null){
		table+="<caption>"+json.caption+"</caption>";
	}
	if(json.headers!=undefined&&json.headers!=null){
		table+="<thead><tr>";
		for(i=0;i<json.headers.length;i++){
			table+="<th>";
			if(json.titles!=undefined){
				table+=json.titles[i];
			}else{
				table+=json.headers[i];
			}
			table+="</th>";
		}
		table+="</tr></thead>";
	}
	
	
	
	if(json.items!=undefined||json.items!=null){
		table+="<tbody>";
		for(t=0;t<json.items.length;t++){
			table+="<tr>";
			for(i=0;i<json.headers.length;i++){
				table+="<td>";
				if(fn!=undefined&&fn!=null){
					table+=fn(json.items[t],json.headers[i]);
				}else{
					table+=json.items[t][json.headers[i]];
				}
				
				table+="</td>";
			}
			table+="</tr>";
		}
		
		table+="</tbody>";
	}
	table+="</table>";
	return table;
}
//================================================================
//map数据类型
Array.prototype.mpiq_remove = function(s) {
	for (var i = 0; i < this.length; i++) {
		if (s == this[i])
			this.splice(i, 1);
	}
}

function mpiq_Map() {
	this.keys = new Array();
	this.data = new Object();
	
	this.put = function(key, value) {
		if(this.data[key] == null){
			this.keys.push(key);
		}
		this.data[key] = value;
	};
	
	this.get = function(key) {
		return this.data[key];
	};
	
	this.remove = function(key) {
		this.keys.mpiq_remove(key);
		this.data[key] = null;
	};
	
	this.each = function(fn){
		if(typeof fn != 'function'){
			return;
		}
		var len = this.keys.length;
		for(var i=0;i<len;i++){
			var k = this.keys[i];
			fn(k,this.data[k],i);
		}
	};
	
	this.entrys = function() {
		var len = this.keys.length;
		var entrys = new Array(len);
		for (var i = 0; i < len; i++) {
			entrys[i] = {
				key : this.keys[i],
				value : this.data[i]
			};
		}
		return entrys;
	};

	this.isEmpty = function() {
		return this.keys.length == 0;
	};
	
	this.size = function(){
		return this.keys.length;
	};
	
	this.toString = function(){
		var s = "";
		for(var i=0;i<this.keys.length;i++,s+='&'){
			var k = this.keys[i];
			s += k+"="+this.data[k];
		}
		return s;
	};
}

//================================================================
//================================================================
/*function mpiq_widget_init(){
	if(typeof window.jQuery == "undefined"){
		var _doc = document.getElementsByTagName('head')[0];
	    var js = document.createElement('script');
	    js.setAttribute('type', 'text/javascript');
	    js.setAttribute('src', "http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js");
	    var js2 = document.createElement('script');
	    js2.setAttribute('type', 'text/javascript');
	    js2.setAttribute('src', "http://localhost:8080/LTISystem/jsp/widgets/js/jquery.accordion.js");
	    _doc.appendChild(js);
	    _doc.appendChild(js2);
	    
	    if (!@cc_on!@0) { //if not IE
	        //Firefox2、Firefox3、Safari3.1+、Opera9.6+ support js.onload
	        js.onload = function () {
	            $(function(){
	            	mpiq_load_widgets();
	            });
	        }
	    } else {
	        //IE6、IE7 support js.onreadystatechange
	        js.onreadystatechange = function () {
	            if (js.readyState == 'loaded' || js.readyState == 'complete') {
	               $(function(){
		            	mpiq_load_widgets();
		            });
	            }
	        }
	    }
	}else{
		$(function(){
        	mpiq_load_widgets();
        });
	}
}*/


function mpiq_widget_init(ind){
	if(ind==mp_files.length){
		mpiq_load_widgets();
		return false;
	}
	var file=mp_files[ind];
	var flag=eval(mp_flags[ind]);
	if(flag){
	    var _doc = document.getElementsByTagName('head')[0];
	    var js = document.createElement('script');
	    js.setAttribute('type', 'text/javascript');
	    js.setAttribute('src', file);
	    _doc.appendChild(js);
	
	    if (!/*@cc_on!@*/0) { //if not IE
	        //Firefox2、Firefox3、Safari3.1+、Opera9.6+ support js.onload
	        js.onload = function () {
	        	mpiq_widget_init(++ind);
	        }
	    } else {
	        //IE6、IE7 support js.onreadystatechange
	        js.onreadystatechange = function () {
	            if (js.readyState == 'loaded' || js.readyState == 'complete') {
	            	mpiq_widget_init(++ind);
	            }
	        }
	    }
	}else{
		mpiq_widget_init(++ind);
	}

    return false;
}

var mp_files=new Array("http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js","http://${serverName}:${port}/LTISystem/jsp/widgets/js/jquery.collapser.js","http://${serverName}:${port}/LTISystem/jsp/widgets/js/jquery-ui-1.8.custom.min.js","http://${serverName}:${port}/LTISystem/jsp/widgets/js/jquery.timer.js");
var mp_flags=new Array("typeof window.jQuery == \"undefined\"","typeof jQuery.accordion == \"undefined\"","typeof jQuery.ui == \"undefined\"","typeof jQuery.timer == \"undefined\"");


function mpiq_parse_params(params){
	var map=new mpiq_Map();
	if(null!=params&&params.length>0){
		var pairs=params.split("&");
		for(var i=0;i<pairs.length;i++){
			var pair=pairs[i].split("=");
			if(pair!=null&&pair.length==2){
				map.put(pair[0], pair[1]);
			}
		}
	} 
	return map;
}

function mpiq_load_data_from_server(data_url,fn){
	$(".mp_container").css("opacity","0.5");
	$("#mp_loading").fadeIn();
	$("#mp_loading div").animate({width:"20px"}).text("loading...");
	$("#mp_loading div").animate({width:"60px"}).text("loading...");
	$.ajax({
       type : "GET",
       url : data_url,
       dataType : "jsonp",
       jsonp: 'jsoncallback',
        success : function(data){
        	$("#mp_loading div").animate({width:"100px"}).text("loading...");
        	$("#mp_loading").fadeOut();
        	$("#mp_loading").css("display","none");
        	$("#mp_createplan").fadeOut();
        	$(".mp_container").css("opacity","1");
        	$("#mp_loading div").animate({width:"0px"}).text("loading...");
        	fn(data);
        }
    });
}


/* *${"/"}
${generatedJS}
/${"*"} */

function mpiq_load_widgets(){
	//GetCookie();
	/* *${"/"}
	<#if widgets??>
	<#list widgets as w>
		<#if w.autoloading>
			mpiq_widget_${w.name}("${w.id}","${w.params}");
		</#if>
	</#list>
	</#if>
	/${"*"} */
}

function   GetCookie() 
{ 
    var   aCookie   =   document.cookie.split( "; "); 
    for   (var   i=0;   i   <   aCookie.length;   i++) 
    { 
        var   aCrumb   =   aCookie[i].split( "= "); 
        alert(aCrumb[0]   +   "   =   "   +   aCrumb[1]); 
    } 
}

//================================================================

mpiq_widget_init(0);
