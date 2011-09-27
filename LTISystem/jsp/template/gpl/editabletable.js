/*
 *	jQuery Editable Table plugin
 *	version: 0.01 (13 July 2009)
 *
 *	@Author: Michael Chua
 *
 *
 */
;(function( jQuery ) {


	jQuery.fn.editabletable = function( args ) {
		args = jQuery.extend( {
			name: null,
			data: null,
			csses:null,
			type:null
		}, args || {} );

		if( ! args.name || ! args.data )return this;
		
		var tableid=$(this).attr('id');
		$('#'+tableid+' thead tr').each(function(){
			$(this).append("<td><a href='javascript:void(0)' class='editabletable_add'>Add</a></td>");
		});
		$('#'+tableid+' tbody tr').each(function(){
			$(this).append("<td><a href='javascript:void(0)' class='editabletable_remove'>remove</a></td>");
		});

		$('#'+tableid+" .editabletable_add").click(function(){
			var html="<tr>";
			for(var i=0;i<args.data.length;i++){
				if(args.type!=null&&args.type[i]=="textaera"){
					html+="<td><textarea  style='"+args.csses[i]+"'></textarea></td>";
				}else{
					html+="<td><input type='text' style='"+args.csses[i]+"'></td>";
				}
				
				
			}
			html+="<td><a href='javascript:void(0)' class='editabletable_remove'>remove</a></td>";
			html+="</tr>";
			$('#'+tableid).append(html);
			
			$('#'+tableid+' .editabletable_remove').click(function(){
				var trNode = $(this).parent().parent(); 
				trNode.remove();
			});
			
			var tr_count=0;
			$('#'+tableid+' tbody tr').each(function(){
				var td_count=0;
				$(this).children('td').each(function(){
					$(this).children('input').each(function(){
						$(this).attr({name:args.name+'['+tr_count+'].'+args.data[td_count]});
						td_count++;
					});
				});
				tr_count++;
			});
			
		});
		
		$('#'+tableid+' .editabletable_remove').click(function(){
			var trNode = $(this).parent().parent(); 
			trNode.remove();
			
			var tr_count=0;
			$('#'+tableid+' tbody tr').each(function(){
				var td_count=0;
				$(this).children('td').each(function(){
					$(this).children('input').each(function(){
						$(this).attr({name:args.name+'['+tr_count+'].'+args.data[td_count]});
						td_count++;
					});
				});
				tr_count++;
			});
		});
		
		return this;
	};
	


} )(jQuery);