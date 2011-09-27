/*
 *	jQuery Editable Table plugin
 *	version: 0.01 (13 July 2009)
 *
 *	@Author: Michael Chua
 *
 *
 */

;(function( jQuery ) {
	$.widget("custom.catcomplete", $.ui.autocomplete, {
		_renderMenu: function( ul, items ) {
			var self = this;
			$.each( items, function( index, item ) {
				var str="Show more result for ";
				var index=item.value.indexOf(str);
				if(index!=-1){
					var tmpdata={"value":item.value.substr(str.length),"label":item.value};
					return $( "<li></li>" )
					.data( "item.autocomplete", tmpdata)
					.append( "<a style='cursor:pointer'>Show more result for <b>"+item.value.substr(str.length)+"</b></a>")
					.appendTo( ul );
					
				}else{
					self._renderItem( ul, item );
				}
				
			});
		}
	});

	
	jQuery.fn.jsuggest = function( args ) {
		args = jQuery.extend( {
			type:'plan',
			parameter:null,
			delay:0,
			url:"/LTISystem",
			planurl:"/LTISystem/select_ajaxplansuggest.action?includeHeader=false&size=10",
			strategyurl:"/LTISystem/select_ajaxstrategysuggest.action?includeHeader=false&size=10",
			portfoliourl:"/LTISystem/select_ajaxportfolioname.action?includeHeader=false&size=10",
			securityurl:"/LTISystem/select_ajaxsecuritysuggest.action?includeHeader=false&size=10",
			acurl:"/LTISystem/select_ajaxassetclass.action?includeHeader=false&size=10",
			scurl:"/LTISystem/select_ajaxstrategyclass.action?includeHeader=false&size=10",
			select:function(event,ui){
				
			}
		}, args || {} );
		
		var cache = {};
		

		var _jself=this;
		$(this).catcomplete({
				delay: args.delay,
				minLength: 2,
				source: function(request, response) {
					if ( request.term in cache ) {
						response( cache[ request.term ] );
						return;
					}
					var urlstr=null;
					
					if(args.type==null){
						urlstr=args.planurl;
					}else if(args.type=="plan"){
						urlstr=args.planurl;
					}else if(args.type=="strategy"){
						urlstr=args.strategyurl;
					}else if(args.type=="security"){
						urlstr=args.securityurl;
					}else if(args.type=="portfolio"){
						urlstr=args.portfoliourl;
					}else if(args.type=="assetclass"){
						urlstr=args.acurl;
					}else if(args.type=="strategyclass"){
						urlstr=args.scurl;
					}
					if(args.parmeter!=null){
						urlstr+=parameter;
					}
					$.ajax({
						url: urlstr,
						dataType: "json",
						data: request,
						success: function( data ) {
							cache[ request.term ] = data;
							response( data );
						}
					});
				},
				 select: function(event,ui){
					//ShowObjProperty(ui.item);
					if(ui.item.label.indexOf("Show more result")!=-1){
						var _url=args.url;
						if(args.url.indexOf("?")==-1){
							_url=args.url+"?keyword="+ui.item.value;
						}else{
							_url=args.url+"&keyword="+ui.item.value;
						}
				 		window.open(_url, "_blank");
				 	}else{
				 		$(this).val(ui.item.value);
				 		
				 		
				 	}
					args.select(event,ui);
				}
				
			});
		/*.data("autocomplete")._renderMenu=function( ul, items ) {
				var self = this;
				$.each( items, function( index, item ) {
					if(item.value.indexOf("Show more result")!=-1){
						var tmpdata={"value":$(_jself).val(),"label":"Show more result for "+$(_jself).val()};
						return $( "<li></li>" )
						.data( "item.autocomplete", tmpdata)
						.append( "<a style='cursor:pointer'>Show more result for <b>"+$(_jself).val()+"</b></a>")
						.appendTo( ul );
						
					}else{
						self._renderItem( ul, item );
					}
					
				});
			};*/

			
			
			
			
			
			
			
			
			
			/*
			_renderItem = function( ul, item ) {
				if(item.value.indexOf("Show more result")!=-1){
					return $( "<li></li>" )
					.data( "item.autocomplete", item )
					.append( "Show more result for <b>"+$(this).val()+"</b>")
					.appendTo( ul );
			 	}else{
			 		return $( "<li></li>" )
					.data( "item.autocomplete", item )
					.append( item.value )
					.appendTo( ul );
			 	}
				
			};*/
		
		return this;
	};
	
	

} )(jQuery);

function ShowObjProperty(Obj)
{
var PropertyList='';
var PropertyCount=0;
for(i in Obj){
if(Obj.i !=null)
PropertyList=PropertyList+i+' 属性：'+Obj.i+'\r\n';
else
PropertyList=PropertyList+i+'方法\r\n';
}
alert(PropertyList);
} 
