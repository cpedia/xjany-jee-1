/*
 *	jQuery Editable Table plugin
 *	version: 0.01 (13 July 2009)
 *
 *	@Author: Michael Chua
 *
 *
 */
;(function( jQuery ) {


	jQuery.fn.multipletable = function( args ) {
		args = jQuery.extend( {
			title:null,
			secondtitle:null,
			name: null,
			secondname:null,
			data: null,
			seconddata:null,
			csses:null,
			secondcsses:null,
			type:null,
			secondtype:null
		}, args || {} );

		if( ! args.name || ! args.data )return this;
		
		
		var divid=$(this).attr('id');
		
		if($(this).children('.control_bar').size()==0){
			$(this).append("<div class='control_bar'></div>");
		}
		
		$(this).children('.control_bar').eq(0).append('<a href="javascript:void(0)" class="first_level_add">Add</a>');
		
		$('#'+divid+' .item').each(function(){
			if($(this).children('.control_bar').size()==0){
				$(this).append("<div class='control_bar'></div>");
			}
			$(this).children('.control_bar').eq(0).append("<div><a href='javascript:void(0)' class='first_level_remove'>remove</a></div>");
		});

		$('#'+divid+" .control_bar .first_level_add").click(function(){
			var html="<div class='item'>";
			html+="<div class='control_bar'><a href='javascript:void(0)' class='first_level_remove'>remove</a></td>";
			html+="</div>";
			html+="<table width=100%><tr>";
			for(var i=0;i<args.title.length;i++){
				html+="<th>"+args.title[i]+"</th>";
			}
			html+="</tr><tr>";
			for(var i=0;i<args.data.length;i++){
				if(args.type!=null&&args.type[i]=="textaera"){
					html+="<td><textarea  style='"+args.csses[i]+"'></textarea></td>";
				}else{
					html+="<td><input type='text' style='"+args.csses[i]+"'></td>";
				}
			}
			html+="</tr></table>";
			
			html+="<table width=100% class='innertable' id='"+new Date().getTime()+"'><thead><tr>";
			for(var i=0;i<args.secondtitle.length;i++){
				html+="<th>"+args.secondtitle[i]+"</th>";
			}
			
			html+="</tr></thead><tbody></tbody></table>"
			
			
			
			$('#'+divid).append(html);
			
			$('#'+divid+' .item .control_bar .first_level_remove').click(function(){
				var trNode = $(this).parent().parent(); 
				trNode.remove();
			});
			
			$('#'+divid+' .item:last').children('.innertable').editabletable({
					name: args.secondname,
					data: args.seconddata,
					csses:args.secondcsses,
					type:args.secondtype
			});
			
//			var tr_count=0;
//			$('#'+tableid+' tbody tr').each(function(){
//				var td_count=0;
//				$(this).children('td').each(function(){
//					$(this).children('input').each(function(){
//						$(this).attr({name:args.name+'['+tr_count+'].'+args.data[td_count]});
//						td_count++;
//					});
//				});
//				tr_count++;
//			});
			
		});
		
		$('#'+divid+' .item .control_bar .first_level_remove').click(function(){
			var trNode = $(this).parent().parent(); 
			trNode.remove();
			
//			var tr_count=0;
//			$('#'+tableid+' tbody tr').each(function(){
//				var td_count=0;
//				$(this).children('td').each(function(){
//					$(this).children('input').each(function(){
//						$(this).attr({name:args.name+'['+tr_count+'].'+args.data[td_count]});
//						td_count++;
//					});
//				});
//				tr_count++;
//			});
		});
		
		return this;
	};
	


} )(jQuery);