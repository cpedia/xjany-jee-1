//装载widget的主函数
//如果参数中包含simple则仅构造搜索结果
//否则将包含搜索框
//planwidget
var _mpiq_params;
function mpiq_widget_${widgetName}(mpiq_id,mpiq_params){
	var params=mpiq_parse_params(mpiq_params);
	_mpiq_params=mpiq_params;
	if(params.get("basic_inf")!=undefined||params.get("basic_inf")!=null){
		var tid="mpiq_table_${time}";
		mpiq_widget_${widgetName}_load_basic_inf_from_server(mpiq_id,tid,mpiq_params);
		
	}
	if(params.get("list_model_portfolios")!=undefined||params.get("list_model_portfolios")!=null){
		var tid="mpiq_table_${time}";
		mpiq_widget_${widgetName}_list_model_portfolios_from_server(function(table){$("#"+mpiq_id).html(table);},tid,mpiq_params);
	}
	
}



function mpiq_widget_${widgetName}_load_basic_inf_from_server(mpiq_id,tid,mpiq_params){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/basic_inf.action?"+mpiq_params;
	mpiq_load_data_from_server(url,function(data){
		
	
		//data.funds.titles=new Array();
		//data.funds.titles[0]="Asset Class";
		//data.funds.titles[1]="Ticker";
		//data.funds.titles[2]="Name";
		//data.funds.titles[3]="Redemption";
		//data.funds.titles[4]="Description";
		//data.funds.titles[5]="WaitingPeriod";
		//data.funds.titles[6]="Memo";
		//data.funds.titles[7]="RoundTrip";
		
		
		data.modelportfolios.titles=new Array();
		data.modelportfolios.titles[0]="ID";
		data.modelportfolios.titles[1]="Portfolio Name";
		data.modelportfolios.titles[2]="End Date";
		
		
		//var table=mpiq_load_table_from_json(data.funds,tid,function(item,header){
			//if(header=="ticker"){
				//return "<a href='http://${serverName}:${port}/LTISystem/jsp/fundcenter/View.action?symbol="+item.ticker+"&type=4' target='_blank'>"+item.ticker+"</a>";
			//}else{
				//return item[header];
			//}
		//});
		
		var html="";
		var a=data.Rating;
		var rating="";
		var shotDescription;
		var cutWords=150;
		var isWords=false;
		html+="<div id='mp_heardname'>"+data.name+"</div>";
		html+="<div id=\"mp_lista\"><a class='mp_accordion'><b>Plan Overview</b></a><div>";
		if(a==0){
			rating="This Plan is unrated."
		}else{
			for(var i=0;i<a;i++){
				rating += "<img border=0 src=\"/LTISystem/jsp/images/star.jpg\">";
			}
		}
		html+="<p><b>Rating</b>:"+rating+"</p>"
		//此段有bug,建议将descript分割为数组
		while(!isWords){
			shotDescription=delHtmlTag(data.description).substring(0,cutWords);
			if(shotDescription.length<=150||shotDescription==undefined||shotDescription==null) break;
			var a = shotDescription.charCodeAt(cutWords-1)
			 if (a < 65 || a > 122)
            {
				 isWords = true;
            }
            else
            {
            	cutWords += 1;
            }
		}

		html+="<b>Description</b><br>";
		if(data.similarIssue!=undefined&&data.similarIssue!=""){
			html+="<span>&nbsp;&nbsp;&nbsp;&nbsp;"+data.similarIssue+"</span>" ;
		}
		html+="<div id='morDesc2'>"+shotDescription+"...</div>";
		html+="<div id='morDesc' style='display:none;'>"+data.description+"</div>";
		if(shotDescription.length>100){
			html+="<button id='mp_showbutton' onclick='mp_showMore(this)' >more...</button>";
		}	
		
		html+="</div>";
		var table2=mpiq_load_table_from_json(data.modelportfolios,tid+"2",function(item,header){
			if(header=="name"){
				return "<a href='javascript:void(0)' onclick=\"mpiq_widget_portfoliowidget('mpiq_widget_getstartedwidget_display_div','portfolioID="+item.id+"',"+mpiq_params+")\">"+item.name+"</a>";
			}else{
				return item[header];
			}
		});
		
		html+="<a class='mp_accordion'><b>Model Portfolios</b></a><div><table>";
		//var reg=new RegExp("Tactical Asset Allocation Moderate");
		//var reg2=new RegExp("Strategic Asset Allocation Moderate");
		for(var p in data.modelportfolios.items){
			//if(data.modelportfolios.items[p]["id"]!=undefined||data.modelportfolios.items[p]["id"]!=null)
				//if(reg.exec(data.modelportfolios.items[p]["name"])!=null||reg2.exec(data.modelportfolios.items[p]["name"])!=null)
			if(data.modelportfolios.items[p]["name"]!=null&&data.modelportfolios.items[p]["name"]!="")
				html+="<tr><td style='font-size: 12px;font-weight: bold;'>Strategic Asset Allocation Moderate</td><td><button onclick=\"mpiq_widget_portfoliowidget('mpiq_widget_getstartedwidget_display_div','portfolioID="+data.modelportfolios.items[p]["id"]+"')\">select</button><td></tr>"
		}
		html+="</table></div>";

		if(data.fundtableMapd!=undefined||data.fundtableMapd!=null){
			html+="<a class='mp_accordion expArrow'><b>Investment Options</b></a><div style='display:none'><table cellspacing=10px>";
			html+="<thead>";
			html+="<tr>";
			html+="<th style=\"align:left\">Asset Class</th>";
			html+="<th style=\"align:left\">Ticker</th>";
			html+="<th style=\"align:left\">Rating</th>";
			html+="<th style=\"align:left\">Description</th>";
			html+="</tr>";
			html+="</thead>";
			html+="<tbody id=\"mp_plantable\">";
			for(var key in data.fundtableMapd.items){
	        	   html+="<tr class=\"mp_assetTitle\">";
		           html+="<td>"+key+"</td>";
		           html+="<td></td>";
		           html+="<td></td>";
		           html+="<td></td>";
		           html+="</tr>";
		           
		           for(var key1 in data.fundtableMapd.items[key]){
		        	   if(data.fundtableMapd.items[key][key1].ID!=undefined||data.fundtableMapd.items[key][key1].ID!=null){
		        		   if(key1%2==0){
		        			   html+="<tr class='odd'>";
		        		   }
		        		   if(key1%2==1){
		        			   html+="<tr class='even'>";
		        		   }
				           html+="<td>"+data.fundtableMapd.items[key][key1].assetClassName+"</td>";
				           html+="<td>"+data.fundtableMapd.items[key][key1].symbol+"</td>";
				           html+="<td>"+parseInt(data.fundtableMapd.items[key][key1].quality*100)+"%</td>";
				           html+="<td>"+data.fundtableMapd.items[key][key1].description+"</td>";
				           html+="</tr> ";
		        	   }
		           }
	           }
	            html+="</tbody>";
	            html+="</table></div>";
		}
		
		
		if(data.comments!=undefined || data.comments.size>0){
			
			data.comments.titles=new Array();
			data.comments.titles[0]="Comments";
			html+="<a class='mp_accordion expArrow'><b>Comments</b></a><div style='display:none'>";
			var table3=mpiq_load_table_from_json(data.comments,tid+"3");
			html+=table3;
			html+="<p><textarea style='width:97%;margin:1%;' id='mpiq_widget_${widgetName}_comment'></textarea></p><input type='button' onclick='mpiq_widget_${widgetName}_add_comment()' value='Add Comment'>";
			html+="</div>";
		}
		
		$("#"+mpiq_id).html(html);
		if(typeof window.mpiq_widget_${widgetName}_load_basic_inf_callback != 'undefined'){
			mpiq_widget_${widgetName}_load_basic_inf_callback(html );
		}
		$("#tablediv tr:even").addClass("even"); 
		$("#tablediv tr:odd").addClass("odd");
		$('.mp_accordion').collapser({
			target: 'next',
			targetOnly: 'div',
			changeText: 0,
			expandClass: 'expArrow',
			collapseClass: 'collArrow'
		});
	});
}

function mpiq_widget_${widgetName}_add_comment(){
	var comm=$("#mpiq_widget_${widgetName}_comment").val();
	if(comm.length<6){
		alert("Comment text should be more than 6 characters.");
		return;
	}
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/add_comment.action?"+_mpiq_params+"&postText="+comm;
	mpiq_load_data_from_server(url,function(data){
		if(data.code==400){
			$("#mpiq_table_${time}3 tbody").append("<tr><td>"+comm+"</td></tr>");
			$("#mpiq_widget_${widgetName}_comment").val("");
		}
	});
	
}

function mpiq_widget_${widgetName}_list_model_portfolios_from_server(fn,tid,mpiq_params){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/list_model_portfolios.action?"+mpiq_params;
	mpiq_load_data_from_server(url,function(data){
		data.titles=new Array();
		data.titles[0]="ID";
		data.titles[1]="Portfolio Name";
		data.titles[2]="End Date";
		var table=mpiq_load_table_from_json(data,tid);
		fn(table);
	});
}
var isshowMore=false;
function mp_showMore(){
	if(!isshowMore){
		$("#morDesc").fadeIn("slow");
		$("#morDesc2").fadeOut("slow");
		$("#mp_showbutton").html("hide");
		isshowMore=true;
	}else{
		$("#morDesc2").fadeIn("slow");
		$("#morDesc").fadeOut("slow");
		$("#mp_showbutton").html("more...");
		isshowMore=false;
	}
}

function mpiq_widget_portfoliowidget_portfolio_callback(data){
	var mpiq_id = "mpiq_widget_getstartedwidget_display_div";
		mpiq_widget_${widgetName}(mpiq_id,"planID="+data+"&basic_inf=1");
		
		var url="http://${serverName}:${port}/LTISystem/widgets/getstartedwidget/myportfolio.action";
		mpiq_load_data_from_server(url,function(data){
			$(".appendthis").children().remove(); 
			for(var i=0;i<data.myportfolio.length;i++){
				$(".appendthis").append("<tr><td><a onclick=\"mpiq_widget_portfoliowidget('mpiq_widget_getstartedwidget_display_div','portfolioID="+data.myportfolio[i].id+"')\">"+data.myportfolio[i].name+"</a></td><td>"+data.myportfolio[i].oyear+"</td><td>"+data.myportfolio[i].tyear+"</td><td>"+data.myportfolio[i].fyear+"</td></tr>");
			}
		});
}

//去掉所有的html标记
function delHtmlTag(str)
{
	return str.replace(/<[^>]+>/g,"");
}
