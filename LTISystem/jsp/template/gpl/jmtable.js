/*
 *	jQuery Editable Table plugin
 *	version: 0.01 (13 July 2009)
 *
 *	@Author: Michael Chua
 *
 *
 */
;(function( jQuery ) {
	
//	var data={
//			"root": [
//			      { 
//			    	  "R1": "r1_1", "R2":"r2_1",
//			    	  "leaf":[
//			    	          {"L1":"l1_1","L2":"l2_1","L3":"l3_1"},
//			    	          {"L1":"l1_2","L2":"l2_2","L3":"l3_2"}
//			    	  ]
//			      },
//			      { 
//			    	  "R1": "r1_second", "R2":"r2_second",
//			    	  "leaf":[
//			    	          {"L1":"l1_second_1","L2":"l2_second_1","L3":"l3_second_1"},
//			    	          {"L1":"l1_second_2","L2":"l2_second_2","L3":"l3_second_2"}
//			    	  ]
//			      }
//	            ]
//	};
//	
//	var styles={
//			"title":"Asset Holding",
//			"name":"Asset",
//			"leafname":"SecurityItem",
//			"root": [
//				 {"name":"R1","title":"Root First","type":"text","css":"width:100%"},
//				 {"name":"R2","title":"Root Second","type":"text","css":"width:100%"}
//		     ],
//		     "leaf":[
//		         {"name":"L1","title":"Leaf First","type":"text","css":"width:100%"},
//		         {"name":"L2","title":"Leaf Second","type":"text","css":"width:100%"},
//		         {"name":"L3","title":"Leaf Third","type":"text","css":"width:100%"}
//		     ]
//		            
//	};
	
	jQuery.fn.jmtable = function( args ) {
		args = jQuery.extend( {
			data:null,
			styles:null,
			jm:null,
			afterAdd:null,
			afterRefresh:null,
			afterLoad:null
		}, args || {} );

		if( ! args.data || ! args.styles )return this;
		
		args.jm=$(this);
		
		var count=0;
		$(this).append("<div class='control_bar'>"+args.styles.title+" <a href='javascript:void(0)' class='first_level_add'>Add</a></div>");
		$(this).children(".control_bar").eq(0).children(".first_level_add").click(function(){
			var html="<div class='item'>";
			html+="<div class='control_bar'><b>Asset "+(++count)+"</b> <a href='javascript:void(0)' class='first_level_remove'>remove</a></div>";
			
			html+="<table width=100% class='itemtable'><tr>";
			for(var i=0;i<args.styles.root.length;i++){
				html+="<th width='"+args.styles.root[i].width+"'>"+args.styles.root[i].title+"</th>";
			}
			html+="</tr><tr>";
			for(var i=0;i<args.styles.root.length;i++){
				html+=jQuery.fn.gettd( args.styles.root[i].type, args.styles.root[i].css );
			}
			html+="</tr></table>";
			
			
			html+="<table width=100% class='innertable'><thead><tr>";
			for(var i=0;i<args.styles.leaf.length;i++){
				html+="<th width='"+args.styles.leaf[i].width+"'>"+args.styles.leaf[i].title+"</th>";
			}
			html+="<th width=50><a href='javascript:void(0)' class='second_level_add'>Add</a></th>";
			html+="</tr></thead><tbody></tbody></table></div>"
			
			
			$(this).parent().parent().append(html);
			var oitem=$(this).parent().parent().children(".item:last");
			var otr=oitem.children(".itemtable:first").children('tbody').children('tr:last');;
			
			$(this).parent().parent().children(".item:last").children('.control_bar').eq(0).children('.first_level_remove').click(function(){
				var fnode = $(this).parent().parent(); 
				fnode.remove();
				jQuery.fn.refresh( args);
			});
			
			$(this).parent().parent().children(".item:last").children('.innertable').children('thead').children('tr').children('th').children('.second_level_add').click(function(){
				var tr="<tr>";
				for(var i=0;i<args.styles.leaf.length;i++){
					tr+=jQuery.fn.gettd( args.styles.leaf[i].type, args.styles.leaf[i].css );
				}
				tr+="<td><a href='javascript:void(0)' class='second_level_remove'>Remove</a></td></tr>";
				var itbody=$(this).parent().parent().parent().parent().children('tbody');
				itbody.append(tr);
				
				$(this).parent().parent().parent().parent().children('tbody').children("tr:last").children('td').children('.second_level_remove').click(function(){
					var fnode = $(this).parent().parent(); 
					fnode.remove();
					jQuery.fn.refresh( args);
				});
				
				jQuery.fn.refresh( args);
				
				if(args.afterAdd!=null){
					args.afterAdd(itbody.children("tr:last"),$(this),oitem);
				}
			});
			
			jQuery.fn.refresh( args);
			
			if(args.afterAdd!=null){
				args.afterAdd(otr,$(this),oitem);
			}
		});
		
		jQuery.fn.load( args, $(this) );
		if(args.afterLoad!=null){
			args.afterLoad($(this),args);
		}
		jQuery.fn.refresh( args);
		return this;
	};
	
	jQuery.fn.gettd = function( type,css ) {
		var html="";
		if(type=="textaera"){
			html="<td><textarea class='"+css+"'></textarea></td>";
		}else if(type=="button"){
			html="<td><input type='button' class='"+css+"'></td>";
		}else if(type=="select"){
			html="<td><select class='"+css+"'></select></td>";
		}else if(type=="readonly"){
			html="<td><input type='text' class='"+css+"' readonly='true'></td>";
		}else{
			html="<td><input type='text' class='"+css+"'></td>";
		}
		return html;
	};
	
	
	jQuery.fn.load = function( args,node ) {
		if(args.data==undefined || args.data==null)return;
		for(var i=0;i<args.data.root.length;i++){
			node.children(".control_bar").eq(0).children(".first_level_add").trigger('click');
			for(var j=0;j<args.styles.root.length;j++){
				//$('#'+node.attr('id')+' input').eq(j).attr({value:args.data.root[i][args.styles.root[j].name]});
				if(args.styles.root[j].type=='select'){
					node.children(".item").eq(i).children(".itemtable").children("tbody").children("tr").eq(1).children("td").eq(j).children().children('option').each(function(){
						if($(this).val()==args.data.root[i][args.styles.root[j].name]){
							$(this).attr('selected','selected'); 
						}
					});
				}else{
					node.children(".item").eq(i).children(".itemtable").children("tbody").children("tr").eq(1).children("td").eq(j).children().attr({value:args.data.root[i][args.styles.root[j].name]});
				}
			}
			for(var j=0;j<args.data.root[i].leaf.length;j++){
				var innertable=node.children(".item").eq(i).children(".innertable");
				innertable.children("thead").children("tr").children("th").children(".second_level_add").trigger('click');
				for(var k=0;k<args.styles.leaf.length;k++){
					
					
					if(args.styles.leaf[k].type=='select'){
						innertable.children("tbody").children("tr").eq(j).children("td").eq(k)
						.children().children('option').each(function(){
							if($(this).val()==args.data.root[i].leaf[j][args.styles.leaf[k].name]){
								$(this).attr('selected','selected'); 
							}
						});
					}else{
						innertable.children("tbody").children("tr").eq(j).children("td").eq(k)
						.children().attr({value:args.data.root[i].leaf[j][args.styles.leaf[k].name]});
						
					}
					
				}
				
			}
		}
		
	};
	jQuery.fn.refresh = function( args ){
		var node=args.jm;
		var rlen=node.children(".item").size();
		var _data=new Object();
		_data.root=new Array();
		for(var i=0;i<rlen;i++){
			var item=node.children(".item").eq(i).children(".itemtable").children("tbody").children("tr").eq(1);
			for(var j=0;j<args.styles.root.length;j++){
				item.children("td").eq(j).children().attr({name:args.styles.name+'['+i+'].'+args.styles.root[j].name});
				_data.root[i]=new Object(); 
				_data.root[i][args.styles.root[j].name]=item.children("td").eq(j).children().val();
			}
			
			var llen=node.children(".item").eq(i).children(".innertable").children("tbody").children("tr").size();
			for(var j=0;j<llen;j++){
				var leaf=node.children(".item").eq(i).children(".innertable").children("tbody").children("tr").eq(j);
				for(var k=0;k<args.styles.leaf.length;k++){	
					var litem=leaf.children("td").eq(k).children().attr({name:args.styles.name+'['+i+'].'+args.styles.leafname+'['+j+'].'+args.styles.leaf[k].name});
					_data.root[i].leaf=new Array();
					_data.root[i].leaf[j]=new Object();
					_data.root[i].leaf[j][args.styles.leaf[k].name]=leaf.children("td").eq(k).children().val();
				}
			}
		}
		if(args.afterRefresh!=null){
			args.afterRefresh(_data);
		}
	};
	

} )(jQuery);