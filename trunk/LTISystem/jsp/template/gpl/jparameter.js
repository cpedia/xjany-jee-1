/*
 *	jQuery Editable Table plugin
 *	version: 0.01 (13 July 2009)
 *
 *	@Author: Michael Chua
 *
 *
 */
;(function( jQuery ) {
	
//	var styles={
//			"map": [
//				 {"name":"tab","title":"int tab","type":"text","css":"","default":"10"}
//		     ]       
//	};
	
//	var storeddata={"test":"text","userSecurity":"SPY","mapsize":2};
	jQuery.fn.jparameter = function( args ) {
		args = jQuery.extend( {
			structure:null,
			storeddata:null,
			strategyname:null,
			name:null
		}, args || {} );
		
		if(args.strategyname!=undefined&&args.strategyname!=null){
			if(args.strategyname=="STATIC"){
				var html="<table width=100% class='parametertable'><tr><td>No parameters.</td></tr></table>";
				$(this).append(html);
				return this;
			}
		}
		
		if(args.strategyname!=null){
			var urlstr="/LTISystem/select_ajaxparametersuggest.action?includeHeader=false";
			$.ajax({
				url: urlstr,
				dataType: "json",
				data: "strategyName="+args.strategyname,
				async:false,
				success: function( data ) {
					args.structure=data;
				},
				error:function(x, textStatus, errorThrown){
					alert(textStatus);
				}
			});
		}
		
		
		if(args.structure==null||args.structure.map==undefined||args.structure.map.length==0){
			var html="<table width=100% class='parametertable'><tr><td>No parameters.</td></tr></table>";
			$(this).append(html);
		}else{
			var html="<table width=100% class='parametertable'>";
			for(var i=0;i<args.structure.map.length;i++){
				html+="<tr><td>"+args.structure.map[i].title+" "+args.structure.map[i].name+"</td>";
				html+=jQuery.fn.gettd( args.structure.map[i].type, args.structure.map[i].css, args.structure.map[i].name );
				html+="</tr>";
			}
			html+="</table>";
			$(this).append(html);
			jQuery.fn.refreshmap(args,$(this), args.name );
		}
		
		
		return this;
	};
	
	
	jQuery.fn.refreshmap = function(args,node, name ) {
		var size=node.children(".parametertable").children('tbody').children('tr').size();
		for(var i=0;i<size;i++){
			var newname=name+"['"+args.structure.map[i].name+"']";
			var _input=node.children(".parametertable").children('tbody').children('tr').eq(i).children('td').eq(1).children();
			_input.attr({'name':newname});
			if(_input.val()==""){
				if(args.storeddata!=null&&args.storeddata[args.structure.map[i].name]!=null){
					_input.val(args.storeddata[args.structure.map[i].name]);
				}else if(args.structure.map[i].default2!=null){
					_input.val(args.structure.map[i].default2);
				}
			}
			
		}
	};

} )(jQuery);