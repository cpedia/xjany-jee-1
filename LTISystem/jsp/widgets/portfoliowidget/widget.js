//接收的参数portfolioID
function mpiq_widget_${widgetName}(mpiq_id,mpiq_params){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/view.action?"+mpiq_params;
	mpiq_load_data_from_server(url,function(data){
		
	
		data.holdings.titles=new Array();
		data.holdings.titles[0]="Asset";
		data.holdings.titles[1]="Ticker";
		data.holdings.titles[2]="Fundescription";
		data.holdings.titles[3]="Percentage";
		
		
		var table=mpiq_load_table_from_json(data.holdings,"holding${time}");
		var Performancetable=mpiq_load_table_from_json(data.perinf,"perinf${time}");
		var planname;
		var reg=new RegExp("Plan");
		var html="";
		var ID = mpiq_params.substring(mpiq_params.indexOf("portfolioID=")+12);
		if(data.hasRealtime=='true'){
			var perchart="<img border=\"0\" src='http://${serverName}:${port}/LTISystem/article_viewchart.action?portfolioID="+ID+"&securityID=1144,91&width=520&height=520&Type=0'/>";
			var holdchart="<img border=\"0\" src='http://${serverName}:${port}/LTISystem/jsp/ajax/DownloadImage.action?ID="+ID+"&isImageCache=true&imageType=2'/>";
		}else{
	    	var perchart="<img border=\"0\" src='http://${serverName}:${port}/LTISystem/article_viewchart.action?portfolioID="+ID+"&securityID=1144,91&width=520&height=520&Type=1'/>";
	    	var holdchart="<img border=\"0\" src='http://${serverName}:${port}/LTISystem/jsp/ajax/DownloadImage.action?ID="+ID+"&isImageCache=true&imageType=3'/>";
		}
		if(reg.exec(data.descriptions.planName)==null){
			planname=data.descriptions.planName+" Plan";
		}else planname = data.descriptions.planName;
		
		html+="<div id='mp_heardname'>"+data.name+"</div>";
		var delyinfo = '<font style=\"font-size:12px\">The portfolio information is delayed 30 days or more before you follow this portfolio.</font><br>';
		if(data.hasRealtime=='true'&&data.isAdmin=='false'){
			delyinfo = '';
			html+="<p><button onclick='unfollow("+mpiq_id+","+mpiq_params+","+data.isOwner+","+data.planID+")' id='btn_unfollow' class='uibutton'>Cancel Following</button>";
		}else if(data.isAdmin=='false'){
			html+="<p><button onclick='follow("+mpiq_params+")' id='btn_follow' class='uibutton'>Follow</button>";
		}
		
		html+="<button onclick='mpiq_widget_${widgetName}_customize("+mpiq_params+")' id='btn_follow' class='uibutton'>Customize</button>";
		html+="<div id=\"mp_lista\"><a class='mp_accordion'><b>Portfolio Overview</b></a><div>";

		html+="<strong>Description</strong><br>";
		if(data.descriptions.portfolioType=="Tactical Asset Allocation"){
			html+="<div id='mp_title'>This model portfolio uses Tactical Asset Allocation (TAA). It first derives trend scores of the "+data.descriptions.MajorAssetCount+" major asset classes: <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.listMajorAsset.replace("[","").replace("]","")+"</font> that are covered in <font> "+planname+"</font>.It then selects the top <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.NumberOfMainRiskyClass+" risk assets</font> and <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.NumberOfMainStableClass+" fixed income asset</font>.</div>";
			html+="<div id='mp_content'>&nbsp;&nbsp;&nbsp;1. <font style='font-weight: bold;'>Risk allocation</font>: Since this portfolio has risk profile <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.RiskProfile+"</font>, the total allocation of the "+data.descriptions.NumberOfMainStableClass+" fixed income asset should be at least <font>"+data.descriptions.RiskProfile+"%</font>.<br>";			
			html+="&nbsp;&nbsp;&nbsp;2. <font style='font-weight: bold;'>Asset weights</font>: risk assets selected are <font style='font-style: italic;font-weight: bold;'>equally weighted</font>.<br>"		
			html+="&nbsp;&nbsp;&nbsp;3. <font style='font-weight: bold;'>Fund selection</font>: about <font style='font-style: italic;font-weight: bold;'>2 top performing funds</font> among <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.availableFunds+" available funds</font> in the plan are chosen for each asset selected. They are usually equally weighted within the asset.<br>";       
			html+="&nbsp;&nbsp;&nbsp;4. <font style='font-weight: bold;'>Rebalance frequency</font>: the portfolio is reviewed by the strategy program <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.RebalanceFrequency+"</font> and the above asset and fund selection processes are repeated. Asset weights are rebalanced back to target allocation if it is necessary. During a market downturn, the asset exposure to risk assets might be reduced and is switched to fixed income instead to avoid big loss.<br>";
			if(data.descriptions.createDate!="null"){
				html+="&nbsp;&nbsp;&nbsp;5.<font style='font-weight: bold;'>Simulation</font>:Performance data before this portfolio was created on <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.createDate+"</font> are obtained from historical simulation. They are hypothetical.<br></div>";
			}else
				html+="&nbsp;&nbsp;&nbsp;5.<font style='font-weight: bold;'>Simulation</font>:Performance data before this portfolio was created are obtained from historical simulation. They are hypothetical.<br></div>";
		}else{
			html+="<div id='mp_title'>This model portfolio uses Strategic Asset Allocation (SAA). The allocations are diversified by investing in the "+data.descriptions.MajorAssetCount+" major asset classes: <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.listMajorAsset.replace("[","").replace("]","")+"</font> that are covered in "+planname+".  It then selects one or two funds for each of the "+data.descriptions.MajorAssetCount+" major asset classes.  </div>";
			
			html+="<div id='mp_content'>&nbsp;&nbsp;&nbsp;1.<font style='font-weight: bold;'>Risk allocation</font>: Since this portfolio has risk profile <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.RiskProfile+"</font>, the total allocation of the  fixed income assets should be at least <font>"+data.descriptions.RiskProfile+"%</font>.<br>";

			if(data.descriptions.SpecifyAssetFund=="true"){
				// alert(data.descriptions.MainAssetClass);
				// alert(data.descriptions.MainAssetClassWeight);
				var MainAssetClass= new Array();
				var MainAssetClassWeight = new Array();
				MainAssetClassWeight = data.descriptions.MainAssetClassWeight.split(",");
				MainAssetClass=data.descriptions.MainAssetClass.split(",");
// for (i=0;i<str.length ;i++ )
// {
// alert(str[i]);
// }
				html+="&nbsp;&nbsp;&nbsp;2.<font style='font-weight: bold;'>Asset weights</font>: This&nbspportfolio's&nbspasset&nbspweights&nbspare&nbspset&nbspby&nbspthe&nbspuser.";
				html+="<a href='javascript:void(0);' onclick='mp_detail()'><img width='15px' height='15px' src='http://${serverName}:${port}/LTISystem/jsp/template/ed/images/newhelp.png'></a><br>";
				html+="<div id='mp_detail' style='position:absolute;'><div><a onclick='cancelDetail()' class='close'></a><br><table id='mp_customers'><tr class='mp_alt'><th>Asset weights</th><th>Percentage</th></tr>";
				for(i=0;i<MainAssetClass.length ;i++ ){
					if(i % 2 == 0 )
					{
						html+="<tr class='mp_alt'><td>"+MainAssetClass[i]+"</td><td>"+parseFloat(MainAssetClassWeight[i])*100+"%</td></tr>";
					}else
						html+="<tr><td>"+MainAssetClass[i]+"</td><td>"+parseFloat(MainAssetClassWeight[i])*100+"%</td></tr>";
				}
				html+="</table></div></div>";
			}else{
				html+="&nbsp;&nbsp;&nbsp;2.<font style='font-weight: bold;'>Asset weights</font>: risk assets selected are equally weighted. <br>";
			}

			html+="&nbsp;&nbsp;&nbsp;3.<font style='font-weight: bold;'>Fund selection</font>: about <font style='font-style: italic;font-weight: bold;'>one or two top performing funds</font> among <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.availableFunds+" available funds</font> in the plan are chosen for each asset selected. They are usually equally weighted within the asset<br>";       
			
			html+="&nbsp;&nbsp;&nbsp;4.<font style='font-weight: bold;'>Rebalance frequency</font>: the portfolio is reviewed by the strategy program <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.RebalanceFrequency+"</font> and the above asset and fund selection processes are repeated. Asset weights are rebalanced back to target allocation if it is necessary.<br>";
			if(data.descriptions.createDate!="null"){
				html+="&nbsp;&nbsp;&nbsp;5.<font style='font-weight: bold;'>Simulation</font>:Performance data before this portfolio was created on <font style='font-style: italic;font-weight: bold;'>"+data.descriptions.createDate+"</font> are obtained from historical simulation. They are hypothetical.<br></div>";
			}else
				html+="&nbsp;&nbsp;&nbsp;5.<font style='font-weight: bold;'>Simulation</font>:Performance data before this portfolio was created are obtained from historical simulation. They are hypothetical.<br></div>";
		}
		html+="</div>";
		
		html+="<a class='mp_accordion'><b>Current Holdings on "+data.enddate+" </b></a><div>"+delyinfo+holdchart+"<button onclick='hold_showMore()'>More Descriptions on the Holdings</button>";
		html+="<div id='showmore'>"+table+"</div></div>"
		html+="<a class='mp_accordion expArrow'><b>Performance Table</b></a><div style='display:none'>"+Performancetable+"</div>";
		html+="<a class='mp_accordion expArrow'><b>Performance Chart</b></a><div style='display:none'>"+perchart+"</div>";
		$("#"+mpiq_id).html(html);
		if(typeof window.mpiq_widget_${widgetName}_callback != 'undefined'){
			mpiq_widget_${widgetName}_callback(html);
		}
		$('.mp_accordion').collapser({
			target: 'next',
			targetOnly: 'div',
			changeText: 0,
			expandClass: 'expArrow',
			collapseClass: 'collArrow'
		});
		$("#tablediv tr:even").addClass("even"); 
		$("#tablediv tr:odd").addClass("odd");
	});
}

function follow(mpiq_params){
	ajaxurl="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/followportfolio.action?portfolioID="+mpiq_params;
	mpiq_load_data_from_server(ajaxurl,function(data){
		alert(data.message);
		if(data.success=="true"){
			var url="http://${serverName}:${port}/LTISystem/widgets/getstartedwidget/myportfolio.action";
			var mpiq_id = "mpiq_widget_getstartedwidget_display_div";
			mpiq_params ="portfolioID="+mpiq_params;
			mpiq_widget_${widgetName}(mpiq_id,mpiq_params);
			mpiq_load_data_from_server(url,function(data){
				$(".appendthis").children().remove(); 
				for(var i=0;i<data.myportfolio.length;i++){
					$(".appendthis").append("<tr><td><a onclick=\"mpiq_widget_portfoliowidget('mpiq_widget_getstartedwidget_display_div','portfolioID="+data.myportfolio[i].id+"')\">"+data.myportfolio[i].name+"</a></td><td>"+data.myportfolio[i].oyear+"</td><td>"+data.myportfolio[i].tyear+"</td><td>"+data.myportfolio[i].fyear+"</td></tr>");
				}
			});
		}
	})
}

function unfollow(mpiq_id,mpiq_params,mpiq_isOwner,mpiq_planId){
	if(mpiq_isOwner==true){
		if(!confirm('The customized portfolio will be deleted in the same time when you cancel following it. Are you sure to cancel following? ')){
			return;
		}
	}
	ajaxurl="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/unfollow.action?portfolioID="+mpiq_params;
	mpiq_load_data_from_server(ajaxurl,function(data){
		alert(data.message);
		if(data.success=="true"){
			
//			var mpiq_id = "mpiq_widget_getstartedwidget_display_div";
//			mpiq_params ="planID="+mpiq_planId;
		  //mpiq_widget_planwidget(mpiq_id,mpiq_params);
			
			
			mpiq_widget_${widgetName}_portfolio_callback(mpiq_planId);
			
		}
	})
}
var isShow=false;
function hold_showMore(){
	if(!isShow){
		$("#showmore").fadeIn("slow"); 
		isShow=true;
	}else{
		$("#showmore").fadeOut("slow"); 
		isShow=false;
	}
}

function mpiq_widget_${widgetName}_customize(mpiq_params){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/customize.action";
	mpiq_load_data_from_server(url,function(data){
		if(data.result==false){
			alert("you should login before customize a portfolio");
			return;
		}else{
			var mpiq_id = "mpiq_widget_getstartedwidget_display_div";
			mpiq_params ="portfolioID="+mpiq_params;
			mpiq_widget_customizewidget(mpiq_id,mpiq_params);
		}
	});
	
}

function mpiq_widget_customizewidget_customize_callback(data){

	if(data.portfolioID==0){
		alert(data.message);
		return;
	}else{
		var mpiq_id = "mpiq_widget_getstartedwidget_display_div";
		var mpiq_params ="portfolioID="+data.portfolioID;
		mpiq_widget_${widgetName}(mpiq_id,mpiq_params);
	}
	
	var url="http://${serverName}:${port}/LTISystem/widgets/getstartedwidget/myportfolio.action";
	var mpiq_id = "mpiq_widget_getstartedwidget_display_div";
	mpiq_params ="portfolioID="+data.portfolioID;
	mpiq_widget_${widgetName}(mpiq_id,mpiq_params);
	mpiq_load_data_from_server(url,function(data){
		$(".appendthis").children().remove(); 
		for(var i=0;i<data.myportfolio.length;i++){
			$(".appendthis").append("<tr><td><a onclick=\"mpiq_widget_portfoliowidget('mpiq_widget_getstartedwidget_display_div','portfolioID="+data.myportfolio[i].id+"')\">"+data.myportfolio[i].name+"</a></td><td>"+data.myportfolio[i].oyear+"</td><td>"+data.myportfolio[i].tyear+"</td><td>"+data.myportfolio[i].fyear+"</td></tr>");
		}
	});
	
}

function mp_detail(){
	$("#mp_detail").css("display","block");
}
function cancelDetail(){
	$("#mp_detail").css("display","none");
}

