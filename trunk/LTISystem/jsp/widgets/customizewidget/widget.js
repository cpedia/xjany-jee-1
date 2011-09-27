//demowidget

var mpiq_curTotalRiskyPercent="";
var mpiq_curTotalStablePercent="";
var mpiq_currentPlanName = "";
var mpiq_curriskyprofile="";
var mpiq_curTemplateName = "";
var mpiq_riskyFundTrNum = 0;
var mpiq_stableFundTrNum = 0;
var mpiq_customize_portfolioID=0;
function mpiq_widget_${widgetName}(mpiq_id,mpiq_params){
	var d = document.createElement("link");
	d.rel = "stylesheet";
	d.rev = "stylesheet";
	d.type = "text/css";
	d.media = "screen";
	d.href = "http://${serverName}:${port}/LTISystem/jsp/template/ed/css/jquery_UI/smoothness/jquery-ui-1.8.custom.css";
	document.getElementsByTagName("head")[0].appendChild(d);
	
   var params=mpiq_parse_params(mpiq_params);
   mpiq_customize_portfolioID = params.get("portfolioID");
	$("#"+mpiq_id).html("${ui}");
	
	mpiq_widget_${widgetName}_load_basic_inf_from_server(mpiq_id,mpiq_params);
		
}

String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

var mpiq_risk_cache = {};
var mpiq_stable_cache={};
var mpiq_fund_cache = {};
function mpiq_widget_${widgetName}_load_basic_inf_from_server(mpiq_id,mpiq_params){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/basic_inf.action?"+mpiq_params;
	mpiq_load_data_from_server(url,function(data){
		if(data.result==false){
			alert(data.message);
			return;
		}
		$("#mpiq_widget_${widgetName}_t_portfolioname").val(data.portfolioName);
		if(data.strategyID==771){
			$("#mpiq_widget_${widgetName}_t_strategyid option:first").attr('selected','selected');
			$("#mpiq_widget_${widgetName}_choosetemplate").show();
			}else{
				$("#mpiq_widget_${widgetName}_t_strategyid option:first").attr('selected','selected');
			}
	    $("#mpiq_widget_${widgetName}_t_planname").val(data.planName);
	    $("#mpiq_widget_${widgetName}_t_riskprofile").val(data.riskprofile);
	    $("#mpiq_widget_${widgetName}_profile_planid").val(data.planID);
	    
	    var templateHtml = "";
	    templateHtml += "<table><tr><td width='300px'><select class='ui-selectmenu ui-widget ui-state-default ui-selectmenu-dropdown ui-state-active ui-corner-top' style='font-size:0.8em;width:300px' ";
	    templateHtml += " id='mpiq_widget_${widgetName}_myselect' onchange='mpiq_changeTemplate()'>";
	    for(var key in data.templateList){
	    	if(data.templateList[key].ID!=undefined && data.templateList[key].ID!=null){
	    		if(key==0){
		    		templateHtml +="<option selected>"+data.templateList[key].templateName+"</option>";
		    	}else{
		    		templateHtml +="<option>"+data.templateList[key].templateName+"</option>";
		    	}
	    	}
	    	
	    }
	    templateHtml +="</select></td>";
	    templateHtml +="<td width='15px'><a id='mpiq_widget_${widgetName}_description' title=\""+data.defaultDescription+"\" href='javascript:void(0)' ><img border=0 alt=\""+data.defaultDescription+"\" height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a></td>";
	    templateHtml +="<td></td></tr></table>";
	    $("#mpiq_widget_${widgetName}_template").html(templateHtml);
	    
	    var riskHtml = "";
	    for(var key in data.defaultRiskyAssetClassList){
	    	if(data.defaultRiskyAssetClassList[key]!=undefined && key!= "mpiq_remove"){
	    		riskHtml +="<tr id='mpiq_widget_${widgetName}_riskyTr_"+key+"'><td>";
	    		riskHtml +="<input type='text' id='mpiq_widget_${widgetName}_riskyasset_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' value=\""+data.defaultRiskyAssetClassList[key]+"\" onclick='mpiq_showAllRisky("+key+")' ondblclick='mpiq_showRiskyTree("+key+")'></td>";
	    	    riskHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_riskyfundnum_"+key+"' style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true'   value=\""+data.defaultRiskyFundNumList[key]+"\"></td>";
	    	    riskHtml +="<td>";
	    	    if(data.defaultRiskyFundNumList[key]!=0){
	    	    	riskHtml +="<a id='mpiq_widget_${widgetName}_riskyButton_"+key+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\"riskyasset\","+key+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
	    	    }
	    	    riskHtml +="</td>";
	    	    riskHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_riskypercent_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' value=\""+data.defaultRiskyPercentageList[key]+"\" onblur='mpiq_changeRiskyPercent("+key+")'></td>";
	    	    riskHtml +="<td>";
	    	    if(data.defaultRiskyFundNumList[key]==0){
	    	    	riskHtml +="<a title='No Funds in Plan' href='javascript:void(0)'><img border=0 alt='No Funds in Plan' height=15px width=15px src='/LTISystem/jsp/template/ed/images/gth.png'></a>";	    	    	
	    	    }
	    	    riskHtml +="</td>";
	    	    riskHtml +="<td><input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdelete_"+key+"' value='Delete' onclick='mpiq_deleteRisky("+key+",1)'></td>";
	    	    riskHtml +="<td>";
	    	    if(data.defaultRiskyFundNumList[key]==0){
	    	    	riskHtml +="<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdivide_"+key+"' value='Scale to other' onclick='mpiq_divideRisky("+key+",1)'>";	    	    	
	    	    }
	    	    riskHtml +="</td>";
	    	    riskHtml +="</tr>";
	    	}
	    }
	    
	    var stableHtml = "";
	    for(var key in data.defaultStableAssetClassList){
	    	if(data.defaultStableAssetClassList[key]!=undefined && key!= "mpiq_remove"){
	    		stableHtml +="<tr id='mpiq_widget_${widgetName}_stableTr_"+key+"'><td>";
	    		stableHtml +="<input type='text' id='mpiq_widget_${widgetName}_stableasset_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' value=\""+data.defaultStableAssetClassList[key]+"\" onclick='mpiq_showAllStable("+key+")' ondblclick='mpiq_showStableTree("+key+")'></td>";
	    		stableHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_stablefundnum_"+key+"' style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true'   value=\""+data.defaultStableFundNumList[key]+"\"></td>";
	    		stableHtml +="<td>";
	    	    if(data.defaultStableFundNumList[key]!=0){
	    	    	stableHtml +="<a id='mpiq_widget_${widgetName}_stableButton_"+key+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\"stableasset\","+key+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
	    	    }
	    	    stableHtml +="</td>";
	    	    stableHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_stablepercent_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' value=\""+data.defaultStablePercentageList[key]+"\" onblur='mpiq_changeStablePercent("+key+")'></td>";
	    	    stableHtml +="<td>";
	    	    if(data.defaultStableFundNumList[key]==0){
	    	    	stableHtml +="<a title='No Funds in Plan' href='javascript:void(0)'><img border=0 alt='No Funds in Plan' height=15px width=15px src='/LTISystem/jsp/template/ed/images/gth.png'></a>";	    	    	
	    	    }
	    	    stableHtml +="</td>";
	    	    stableHtml +="<td><input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdelete_"+key+"' value='Delete' onclick='mpiq_deleteStable("+key+",1)'></td>";
	    	    stableHtml +="<td>";
	    	    if(data.defaultStableFundNumList[key]==0){
	    	    	stableHtml +="<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdivide_"+key+"' value='Scale to other' onclick='mpiq_divideStable("+key+",1)'>";	    	    	
	    	    }
	    	    stableHtml +="</td>";
	    	    stableHtml +="</tr>";
	    	}
	    }
	    $("#mpiq_widget_${widgetName}_risky_asset").html(riskHtml);
	    $("#mpiq_widget_${widgetName}_stable_asset").html(stableHtml);
	    $("#mpiq_widget_${widgetName}_totalrisky").val(data.defaultTotalRiskyPercentage);
	    $("#mpiq_widget_${widgetName}_totalstable").val(data.defaultTotalStablePercentage);
	    
	    for(var key in data.defaultRiskyAssetClassList){
	    	if(data.defaultRiskyAssetClassList[key]!=undefined && key!= "mpiq_remove"){
	    		$("#mpiq_widget_${widgetName}_riskyasset_"+key).autocomplete({
	    			minLength: 0,
	    			source: function( request, response ) {
	    				if(request.term == "")
	    			        request.term = "riskyspace";
	    				if ( request.term in mpiq_risk_cache) {
	    					response( $.map( mpiq_risk_cache[request.term].AssetClassName, function( item ) {
    							return {
    								label: item.name,
    								value: item.name.replace(/&nbsp;/ig,"")
    							}
    						}));
	    					return;
	    				}
	    				$.ajax({
	    					url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=0&"+mpiq_params,
	    					dataType: "jsonp",
	    					data: request,
	    					success: function( data ) {
	    						response( $.map( data.AssetClassName, function( item ) {
	    							return {
	    								label: item.name,
	    								value: item.name.replace(/&nbsp;/ig,"")
	    							}
	    						}));
	    						mpiq_risk_cache[ request.term ] = data;
	    					},
	    					error:function(){
	    						alert("error");
	    					}
	    				});
	    			},
	    			open: function (){
	    				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
	    				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
	    			},
	    			select:function(event,ui){
	    				$(this).val(ui.item.value);
	    				mpiq_changeFundNum(0);
	    			}
	    		}).data( "autocomplete" )._renderItem = function( ul, item ) {
	    			ul.addClass("mpiq_autocomplete");
	    				return $( "<li></li>" )
	    					.data( "item.autocomplete", item )
	    					.append( "<a>" + item.label + "</a>" )
	    					.appendTo( ul );
	    			
	    		};
	    	}
	    }
	    
	    for(var key in data.defaultStableAssetClassList){
	    	if(data.defaultStableAssetClassList[key]!=undefined && key!= "mpiq_remove"){
	    		$("#mpiq_widget_${widgetName}_stableasset_"+key).autocomplete({
	    			minLength: 0,
	    			source: function( request, response ) {
	    				if(request.term == "")
	    			        request.term = "stablespace";
	    				if ( request.term in mpiq_stable_cache) {
	    					response( $.map(mpiq_stable_cache[request.term].AssetClassName, function( item ) {
    							return {
    								label: item.name,
    								value: item.name.replace(/&nbsp;/ig,"")
    							}
    						}));
	    					return;
	    				}
	    				$.ajax({
	    					url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=1&"+mpiq_params,
	    					dataType: "jsonp",
	    					data: request,
	    					success: function( data ) {
	    						response( $.map( data.AssetClassName, function( item ) {
	    							return {
	    								label: item.name,
	    								value: item.name.replace(/&nbsp;/ig,"")
	    							}
	    						}));
	    						mpiq_stable_cache[ request.term ] = data;
	    					},
	    					error:function(){
	    						alert("error");
	    					}
	    				});
	    			},
	    			open: function (){
	    				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
	    				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
	    			},
	    			select:function(event,ui){
	    				$(this).val(ui.item.value);
	    				mpiq_changeFundNum(0);
	    			}
	    		}).data( "autocomplete" )._renderItem = function( ul, item ) {
	    			ul.addClass("mpiq_autocomplete");
	    				return $( "<li></li>" )
	    					.data( "item.autocomplete", item )
	    					.append( "<a>" + item.label + "</a>" )
	    					.appendTo( ul );
	    			
	    		};
	    	}
	    }
	    
	    $("#mpiq_widget_${widgetName}_t_planname").autocomplete({
			minLength: 2,
			source: function( request, response ) {
				$.ajax({
					url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/planName.action",
					dataType: "jsonp",
					data: {
						size: 6,
						planName: request.term
					},
					success: function( data ) {
						response( $.map( data.planNames, function( item ) {
							return {
								value: item.planName
							}
						}));

					},
					error:function(){
						alert("error");
					}
				});
			},
			open: function (){
				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
			},
			select:function(event,ui){
				$(this).val(ui.item.value);
				if(ui.item.value!=mpiq_currentPlanName){
				 	mpiq_currentPlanName = ui.item.value;
				 	mpiq_changePlan();
				 }
			}
		});
	    
	    
	    $("#mpiq_widget_${widgetName}_single_ticker").autocomplete({
			minLength: 1,
			source: function(request, response) {			    
				if ( request.term in mpiq_fund_cache) {
					response( $.map( data.funds, function( item ) {
						return {								
							value: item.fund
						}
					}));
					return;
				}
				$.ajax({
					url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchFund.action",
					dataType: "jsonp",
					data: request,
					success: function( data ) {
						response( $.map( data.funds, function( item ) {
							return {								
								value: item.fund
							}
						}));
						mpiq_fund_cache[ request.term ] = data;
					},
					error:function(){
						alert("error");
					}
				});
			},
			open: function (){
				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
			},
			select: function(event, ui) { 			 				 					 	
			 		$(this).val(ui.item.value);			 				 				 	
			}
						
		}).data( "autocomplete" )._renderItem = function( ul, item ) {
			ul.addClass("mpiq_autocomplete");
			return $( "<li></li>" )
				.data( "item.autocomplete", item )
				.append( "<a>" + item.value + "</a>" )
				.appendTo( ul );
		
	};
	    
	    var mpiq_widget_${widgetName}_planName = $("#mpiq_widget_${widgetName}_t_planname").val();
		mpiq_widget_${widgetName}_load_plan_fundtable(mpiq_widget_${widgetName}_planName,0);
		
		 mpiq_curTotalRiskyPercent = $("#mpiq_widget_${widgetName}_totalrisky").val();
		 mpiq_curTotalStablePercent = $("#mpiq_widget_${widgetName}_totalstable").val();
		 mpiq_curTotalRiskyPercent = mpiq_numFixed(mpiq_curTotalRiskyPercent);
		 mpiq_curTotalStablePercent = mpiq_numFixed(mpiq_curTotalStablePercent);
		 mpiq_currentPlanName = $("#mpiq_widget_${widgetName}_t_planname").val();
		 mpiq_curriskyprofile=mpiq_numFixed($("#mpiq_widget_${widgetName}_t_riskprofile").val());
		 mpiq_curTemplateName = $("#mpiq_widget_${widgetName}_template").val();
		 
		 mpiq_handleDefaultTemp();
		 var tempRiskyPercent = $("#mpiq_widget_${widgetName}_t_riskprofile").val();
		 var tempStablePercentNum = parseFloat(tempRiskyPercent);
		 var tempRiskyPercentNum = 100-tempRiskyPercent+0.001;
		 var totalRiskyPercent = mpiq_numFixed(tempRiskyPercentNum);
		 var totalStablePercent = mpiq_numFixed(tempStablePercentNum);
		 $("#mpiq_widget_${widgetName}_totalrisky").val(totalRiskyPercent);
		 $("#mpiq_widget_${widgetName}_totalstable").val(totalStablePercent);
		 mpiq_changeTotalRiskyPercent();
		 mpiq_changeTotalStablePercent();
			
		 rTrSize = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
		 sTrSize = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
		 for(var n=0;n<rTrSize;n++){
			 if(n!=0){mpiq_defaultAssetClassName+=",";}
			 var assetName = $("#mpiq_widget_${widgetName}_riskyasset_"+n).val();
			 mpiq_defaultAssetClassName+=assetName;
		  }
			
		 for(var m=0;m<sTrSize;m++){
			 var assetName = $("#mpiq_widget_${widgetName}_stableasset_"+m).val();
			 mpiq_defaultAssetClassName +=",";
			 mpiq_defaultAssetClassName +=assetName;
		   } 
	});
}

var mpiq_defaultAssetClassName="";
function mpiq_handleDefaultTemp(){
	var rTrSize = 0;
	var sTrSize = 0;
	var rdivide = true;
	var sdivide = true;
	// alert("default template");
	while(rdivide){
	  rTrSize = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
	  for(var i=0;i<rTrSize;i++){
	   var fundNum = $("#mpiq_widget_${widgetName}_riskyfundnum_"+i).val();
	   if(fundNum =="0"){
	     mpiq_divideRisky(i,0);
	     break;
	   }
	   
	}
	if(i==rTrSize)
	 rdivide = false;
  }
	
	while(sdivide){
	  sTrSize = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
	  for(var j=0;j<sTrSize;j++){
	    var fundNum = $("#mpiq_widget_${widgetName}_stablefundnum_"+j).val();
	    if(fundNum =="0"){
	    	mpiq_divideStable(j,0);
	    	break;
	  		}
	 	}
	 	if(j==sTrSize)
	 	   sdivide = false;
	}
	
	mpiq_changeFundNum(0);
	
	
}

function mpiq_changeriskprofile(){
	   var riskyprofile = $("#mpiq_widget_${widgetName}_t_riskprofile").val();

	   riskyprofile = mpiq_numFixed(riskyprofile);
	   $("#mpiq_widget_${widgetName}_t_riskprofile").val(riskyprofile);
	   if(riskyprofile == mpiq_curriskyprofile)
	   		return;
	   var totalStableNum = parseFloat(riskyprofile);
	   var totalRiskyNum = 100-totalStableNum+0.001;
	   var totalStablePercent = mpiq_numFixed(totalStableNum);
	   var totalRiskyPercent = mpiq_numFixed(totalRiskyNum);
	   $("#mpiq_widget_${widgetName}_totalrisky").val(totalRiskyPercent);
	   $("#mpiq_widget_${widgetName}_totalstable").val(totalStablePercent);
	   mpiq_curriskyprofile = riskyprofile;
	   
	   mpiq_changeTotalRiskyPercent();
	   mpiq_changeTotalStablePercent();
	}

function mpiq_changePlan(){
	   var planName = $("#mpiq_widget_${widgetName}_t_planname").val();
	   if(planName=="")return;
	   mpiq_widget_${widgetName}_load_plan_fundtable(planName,1);
	} 

function mpiq_widget_${widgetName}_customize_portfolio(){
	$("#mpiq_widget_${widgetName}_profile_portfolioname").val($("#mpiq_widget_${widgetName}_t_portfolioname").val());
	$("#mpiq_widget_${widgetName}_strategyid").val($("#mpiq_widget_${widgetName}_t_strategyid").val());
	$("#mpiq_widget_${widgetName}_frequency").val($("#mpiq_widget_${widgetName}_t_frequency").val());
	$("#mpiq_widget_${widgetName}_maxOfRiskyAsset").val($("#mpiq_widget_${widgetName}_t_maxOfRiskyAsset").val());
	$("#mpiq_widget_${widgetName}_numberOfMainRiskyClass").val($("#mpiq_widget_${widgetName}_t_numberOfMainRiskyClass").val());
	$("#mpiq_widget_${widgetName}_profile_planname").val($("#mpiq_widget_${widgetName}_t_planname").val());
	$("#mpiq_widget_${widgetName}_profile_risknumber").val($("#mpiq_widget_${widgetName}_t_riskprofile").val());
	
	var portfolioName = $("#mpiq_widget_${widgetName}_profile_portfolioname").val();
	var strategyid = $("#mpiq_widget_${widgetName}_strategyid").val();
	var planName = $("#mpiq_widget_${widgetName}_profile_planname").val();
	var planID = $("#mpiq_widget_${widgetName}_profile_planid").val();
	var frequency = $("#mpiq_widget_${widgetName}_frequency").val();
	var risknumber = $("#mpiq_widget_${widgetName}_profile_risknumber").val();
	var maxOfRiskyAsset = $("#mpiq_widget_${widgetName}_maxOfRiskyAsset").val();
	var numberOfMainRiskyClass = $("#mpiq_widget_${widgetName}_numberOfMainRiskyClass").val();
	var MainAssetClass = $("#mpiq_widget_${widgetName}_MainAssetClass").val();
	var MainAssetClassWeight = $("#mpiq_widget_${widgetName}_MainAssetClassWeight").val();
	var AssetFundString = $("#mpiq_widget_${widgetName}_AssetFundString").val();
	var BuyThreshold = $("#mpiq_widget_${widgetName}_BuyThreshold").val();
	var SellThreshold = $("#mpiq_widget_${widgetName}_SellThreshold").val();
	var SpecifyAssetFund = $("#mpiq_widget_${widgetName}_SpecifyAssetFund").val();
	var UseDataObject = $("#mpiq_widget_${widgetName}_UseDataObject").val();
	var mpiq_widget_${widgetName}_params = "portfolioName="+portfolioName+"&strategyID="+strategyid+"&planName="+planName+"&planID="+planID;
	mpiq_widget_${widgetName}_params += "&frequency="+frequency+"&risknumber="+risknumber+"&maxOfRiskyAsset="+maxOfRiskyAsset+"&numberOfMainRiskyClass="+numberOfMainRiskyClass;
	mpiq_widget_${widgetName}_params += "&MainAssetClass="+MainAssetClass+"&MainAssetClassWeight="+MainAssetClassWeight+"&AssetFundString="+AssetFundString;
	mpiq_widget_${widgetName}_params += "&BuyThreshold="+BuyThreshold+"&SellThreshold="+SellThreshold+"&SpecifyAssetFund="+SpecifyAssetFund+"&UseDataObject="+UseDataObject;
	var url = "http://${serverName}:${port}/LTISystem/widgets/${widgetName}/generatePortfolio.action?"+mpiq_widget_${widgetName}_params;
	mpiq_load_data_from_server(url,function(data){
		if(data.portfolioID!=0){
			myjson = '{\"message\":\"'+data.message+'\",\"code\":\"'+data.code+'\",\"portfolioID\":\"'+data.portfolioID+'\"}';
			myjson = eval('(' + myjson + ')');
			$("#mpiq_widget_${widgetName}_progresstable").show();
			$("#mpiq_widget_${widgetName}_progressbar").progressbar({
				value: 0
			});
								    
		    $("#mpiq_widget_${widgetName}_progressbar").children('div').css('background','none repeat-x scroll 50% 50% #DFF1DF');
			$.timer(5000, function (timer) {
				if(++times>=4000){
					timer.stop();
				}
				
				var s = document.createElement("SCRIPT");
			    s.id="process_state"; 
			    document.getElementsByTagName("HEAD")[0].appendChild(s);
			    s.src='http://${serverName}:8081/state?portfolioID='+data.portfolioID+'&timestamp=-1&function=mpiq_setProcess';
			   
			});
			
		}else{
			$("#mpiq_widget_${widgetName}_message").html(data.message);			
		}
										
	});
	
}

var myjson = "";
var times =0;
function mpiq_setProcess(data){
	document.getElementsByTagName("HEAD")[0].removeChild(document.getElementById("process_state"));
	if(data.state!=-1){
		$("#mpiq_widget_${widgetName}_progressbar").progressbar({
			value: data.state
		});
		$("#mpiq_widget_${widgetName}_number").html(data.state+"%");
	}	
	if(data.state == 100){
		times = 4000;		
		if(typeof mpiq_widget_${widgetName}_customize_callback==undefined){
			$("#mpiq_widget_${widgetName}_message").html(myjson.message);
		}else{
			mpiq_widget_${widgetName}_customize_callback(myjson);
		}		
	}
	
}

var mpiq_widget_${widgetName}_SAAState = false;
function mpiq_widget_${widgetName}_showSAA(){
	if(mpiq_widget_${widgetName}_SAAState==false){
		$("#mpiq_widget_${widgetName}_SAA").show();
		mpiq_widget_${widgetName}_SAAState = true;
	}else{
		$("#mpiq_widget_${widgetName}_SAA").hide();
		mpiq_widget_${widgetName}_SAAState = false;
	}	
}

var mpiq_widget_${widgetName}_investment = false;
function mpiq_widget_${widgetName}_showAllFundtable(){
	if(mpiq_widget_${widgetName}_investment==false){
		$("#mpiq_widget_${widgetName}_investment").show();
		$("#mpiq_widget_${widgetName}_btn_showAllFundtable").val("- Investment Option");
		mpiq_widget_${widgetName}_investment = true;
	}else{
		$("#mpiq_widget_${widgetName}_investment").hide();
		$("#mpiq_widget_${widgetName}_btn_showAllFundtable").val("+ Investment Option");
		mpiq_widget_${widgetName}_investment = false;
	}	
	
}

function mpiq_widget_${widgetName}_load_plan_fundtable(planName,flag){
	var url="";
	if(flag==0){
		url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/planFundtable.action?planName="+planName+"&updateInfo=false";
	}else{
		url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/planFundtable.action?planName="+planName+"&updateInfo=true";
	}
	
	mpiq_load_data_from_server(url,function(data){
		var html="";
		if(data.result == false){
			html="The plan does not exist!";
			alert(html);
		}else{
			html +="<table cellspacing=10px><thead>";
			html+="<tr>";
			html+="<th style=\"align:left\">Asset Class</th>";
			html+="<th style=\"align:left\">Ticker</th>";
			html+="<th style=\"align:left\">Rateing</th>";
			html+="<th style=\"align:left\">Description</th>";
			html+="</tr>";
			html+="</thead>";
			html+="<tbody id=\"mp_plantable\">";
			for(var key in data.fundtable){
	        	   html+="<tr class=\"mp_assetTitle\">";
		           html+="<td>"+key+"</td>";
		           html+="<td></td>";
		           html+="<td></td>";
		           html+="<td></td>";
		           html+="</tr>";
		           
		           for(var key1 in data.fundtable[key]){
		        	   if(data.fundtable[key][key1].ID!=undefined||data.fundtable[key][key1].ID!=null){
		        		   if(key1%2==0){
		        			   html+="<tr class='odd'>";
		        		   }
		        		   if(key1%2==1){
		        			   html+="<tr class='even'>";
		        		   }
		        		   
				           html+="<td>"+data.fundtable[key][key1].assetClassName+"</td>";
				           html+="<td>"+data.fundtable[key][key1].symbol+"</td>";
				           html+="<td>"+parseInt(data.fundtable[key][key1].quality*100)+"%</td>";
				           html+="<td>"+data.fundtable[key][key1].description+"</td>";
				           html+="</tr> ";
		        	   }
		        	   
		           }
		     
	           }
	            html+="</tbody>";
	            html+="</table>";
		}
		
		$("#mpiq_widget_${widgetName}_investment").html(html);
		
		if(flag==1){
			  var fundName="";  
	          var count=0;
	          for(var i=0;i<mpiq_riskyFundTrNum;i++){
	            var fund = $("#mpiq_widget_${widgetName}_riskyfund_"+i).val();
	            if(fund==undefined)
	            continue
	            if(count!=0)
	             fundName+=",";
	            fundName+=fund;
	            count++;
	         }
	         
	         for(var j=0;j<mpiq_stableFundTrNum;j++){
	            var fund = $("#mpiq_widget_${widgetName}_stablefund_"+j).val();
	            if(fund==undefined)
	            continue
	            fundName+=",";
	            fundName+=fund;
	            count++;
	         }
	         if(fundName!=""){
	        	 var s_url = "http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getSingleFundInfo.action?fundName="+fundName;
	        	 mpiq_load_data_from_server(url,function(data){
	        		 var numItem = new Array();
	        		 for(var i=0;i<data.size;i++){
	        			 numItem[i]= data.number[i];
	        		 }
	                 var num=0;
	                 for(var i=0;i<mpiq_riskyFundTrNum;i++){
	            			var fund = $("#mpiq_widget_${widgetName}_riskyfund_"+i).val();
	            			if(fund==undefined)
	            			continue
	            			var item = numItem[num];
	            	         num++;
	            			if(item=="0"){
	            			  var percent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+i).val();
	            			  percent = parseFloat(percent);
	            			  var totalRiskyPercent = $("#mpiq_widget_${widgetName}_totalrisky").val();
	            			  totalRiskyPercent = parseFloat(totalRiskyPercent);
	            			  totalRiskyPercent = totalRiskyPercent - percent;
	            			  totalRiskyPercent = mpiq_numFixed(totalRiskyPercent);
	            			  $("#mpiq_widget_${widgetName}_totalrisky").val(totalRiskyPercent);
	            			  curTotalRiskyPercent = totalRiskyPercent;
	            			  $("#mpiq_widget_${widgetName}_riskyFundTr_"+i).remove();
	            			}
	         			}
	         
	        		 for(var j=0;j<mpiq_stableFundTrNum;j++){
	            		var fund = $("#mpiq_widget_${widgetName}_stablefund_"+j).val();
	            		if(fund==undefined)
	            		continue
	            		var item = numItem[num];
	            	    num++;
	            	    if(item=="0"){
	            			  var percent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val();
	            			  percent = parseFloat(percent);
	            			  var totalStablePercent = $("#mpiq_widget_${widgetName}_totalstable").val();
	            			  totalStablePercent = parseFloat(totalStablePercent);
	            			  totalStablePercent = totalStablePercent - percent;
	            			  totalStablePercent = mpiq_numFixed(totalStablePercent);
	            			  $("#mpiq_widget_${widgetName}_totalrisky").val(totalStablePercent);
	            			  mpiq_curTotalStablePercent = totalStablePercent;
	            			  $("#mpiq_widget_${widgetName}_stableFundTr_"+j).remove();
	            			}
	         			}
	         			 mpiq_changeFundNum(0);
	        	 });	           
	         }else{
	            mpiq_changeFundNum(0);
	         }
		}
	});
}

var mpiq_widget_${widgetName}_singlefund = false;
function mpiq_widget_${widgetName}_showSingleFund(){
	if(mpiq_widget_${widgetName}_singlefund==false){
		$("#mpiq_widget_${widgetName}_singleFund").show();
		$("#mpiq_widget_${widgetName}_btn_singleFund").val("- Add Single Fund");
		mpiq_widget_${widgetName}_singlefund = true;
	}else{
		$("#mpiq_widget_${widgetName}_singleFund").hide();
		$("#mpiq_widget_${widgetName}_btn_singleFund").val("+ Add Single Fund");
		mpiq_widget_${widgetName}_singlefund = false;
	}	
}

var mpiq_widget_${widgetName}_advance = false;
function mpiq_widget_${widgetName}_showAdvance(){
	if(mpiq_widget_${widgetName}_advance==false){
		$("#mpiq_widget_${widgetName}_Advance").show();
		$("#mpiq_widget_${widgetName}_btn_advance").val("- Advance Option");
		mpiq_widget_${widgetName}_advance = true;
	}else{
		$("#mpiq_widget_${widgetName}_Advance").hide();
		$("#mpiq_widget_${widgetName}_btn_advance").val("+ Advance Option");
		mpiq_widget_${widgetName}_advance = false;
	}	
}

function mpiq_showAllRisky(index){
    var text = $("#mpiq_widget_${widgetName}_riskyasset_"+index).val();
    if(text!="" || text.length!=0)
      return;
    $("#mpiq_widget_${widgetName}_riskyasset_"+index).autocomplete( "search", "" );
}

function mpiq_showAllStable(index){
	 var text = $("#mpiq_widget_${widgetName}_stableasset_"+index).val();
    if(text!="" || text.length!=0)
      return;
    $("#mpiq_widget_${widgetName}_stableasset_"+index).autocomplete( "search", "" );
}

function mpiq_showRiskyTree(index){
    var text = $("#mpiq_widget_${widgetName}_riskyasset_"+index).val();
    if(text =="" || text.length==0)
      return;
    $("#mpiq_widget_${widgetName}_riskyasset_"+index).autocomplete( "search", "" );
}


function mpiq_showStableTree(index){
	 var text = $("#mpiq_widget_${widgetName}_stableasset_"+index).val();
    if(text=="" || text.length==0)
      return;
    $("#mpiq_widget_${widgetName}_stableasset_"+index).autocomplete( "search", "" );
}

function mpiq_helpriskprofile(){
	 $("#mpiq_widget_${widgetName}_helpwindow").dialog({
			resizable: false,
			height:400,
			width:550,
			autoOpen:false,
			position:[10,100],
			modal: true,
			title:"More on Risk Profile"				
	});
	 if([,]==','){
		 $("#mpiq_widget_${widgetName}_helpimg").css("width","530px");
		}
	 $("#mpiq_widget_${widgetName}_helpwindow").dialog("open"); 
	 $("#ui-dialog-title-mpiq_widget_customizewidget_helpwindow").css("font-size",'0.7em')
}

function mpiq_addRiskyLine(){
	var size = $("#mpiq_widget_${widgetName}_tb_risky tr").length-1;
    var $pane = $("#mpiq_widget_${widgetName}_tb_risky");
    var type="riskyasset";
    var $tr,$td;
    $tr = $(document.createElement("tr"));
    $tr.attr({id:'mpiq_widget_${widgetName}_riskyTr_'+size});
    $pane.append($tr);
    $td = $(document.createElement("td"));
    $td.html("<input type='text' id='mpiq_widget_${widgetName}_riskyasset_"+size+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px'  onclick='mpiq_showAllRisky("+size+")' ondblclick='mpiq_showRiskyTree("+size+")'>");
    $tr.append($td);
    $td = $(document.createElement("td"));
    $td.html("<input type='text' id='mpiq_widget_${widgetName}_riskyfundnum_"+size+"' style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true'>");
    $tr.append($td);
    $td = $(document.createElement("td"));
    $tr.append($td);
    $td = $(document.createElement("td"));
    $td.html("<input type='text' id='mpiq_widget_${widgetName}_riskypercent_"+size+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px'  onblur='mpiq_changeRiskyPercent("+size+")'>");
    $tr.append($td);
    $td = $(document.createElement("td"));
    $tr.append($td);
    $td = $(document.createElement("td"));
    $td.html("<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdelete_"+size+"' value='Delete' onclick='mpiq_deleteRisky("+size+",1)'>");
    $tr.append($td);
    // $("#mpiq_widget_${widgetName}_rdelete_"+size).button();
    $td = $(document.createElement("td"));         
    $tr.append($td);
    
    $("#mpiq_widget_${widgetName}_riskyasset_"+size).autocomplete({
		minLength: 0,
		source: function( request, response ) {
			if(request.term == "")
		        request.term = "riskyspace";
			if ( request.term in mpiq_risk_cache) {
				response( $.map( mpiq_risk_cache[request.term].AssetClassName, function( item ) {
					return {
						label: item.name,
						value: item.name.replace(/&nbsp;/ig,"")
					}
				}));
				return;
			}
			$.ajax({
				url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=0&portfolioID="+mpiq_customize_portfolioID,
				dataType: "jsonp",
				data: request,
				success: function( data ) {
					response( $.map( data.AssetClassName, function( item ) {
						return {
							label: item.name,
							value: item.name.replace(/&nbsp;/ig,"")
						}
					}));
					mpiq_risk_cache[ request.term ] = data;
				},
				error:function(){
					alert("error");
				}
			});
		},
		open: function (){
			jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
			jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
		},
		select:function(event,ui){
			$(this).val(ui.item.value);
			mpiq_changeFundNum(0);
		}
	}).data( "autocomplete" )._renderItem = function( ul, item ) {
		ul.addClass("mpiq_autocomplete");
			return $( "<li></li>" )
				.data( "item.autocomplete", item )
				.append( "<a>" + item.label + "</a>" )
				.appendTo( ul );
		
	};
}

function mpiq_addStableLine(){
	var size = $("#mpiq_widget_${widgetName}_tb_stable tr").length-1;
    var $pane = $("#mpiq_widget_${widgetName}_tb_stable");
    var type="stableasset";
    var $tr,$td;
    $tr = $(document.createElement("tr"));
    $tr.attr({id:'mpiq_widget_${widgetName}_stableTr_'+size});
    $pane.append($tr);
    $td = $(document.createElement("td"));
    $td.html("<input type='text' id='mpiq_widget_${widgetName}_stableasset_"+size+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px'  onclick='mpiq_showAllStable("+size+")' ondblclick='mpiq_showStableTree("+size+")'>");
    $tr.append($td);
    $td = $(document.createElement("td"));
    $td.html("<input type='text' id='mpiq_widget_${widgetName}_stablefundnum_"+size+"' style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true'>");
    $tr.append($td);
    $td = $(document.createElement("td"));
    $tr.append($td);
    $td = $(document.createElement("td"));
    $td.html("<input type='text' id='mpiq_widget_${widgetName}_stablepercent_"+size+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px'  onblur='mpiq_changeStablePercent("+size+")'>");
    $tr.append($td);
    $td = $(document.createElement("td"));
    $tr.append($td);
    $td = $(document.createElement("td"));
    $td.html("<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdelete_"+size+"' value='Delete' onclick='mpiq_deleteStable("+size+",1)'>");
    $tr.append($td);
    // $("#mpiq_widget_${widgetName}_sdelete_"+size).button();
    $td = $(document.createElement("td"));         
    $tr.append($td);
    
    $("#mpiq_widget_${widgetName}_stableasset_"+size).autocomplete({
		minLength: 0,
		source: function( request, response ) {
			if(request.term == "")
		        request.term = "stablespace";
			if ( request.term in mpiq_stable_cache) {
				response( $.map( mpiq_stable_cache[request.term].AssetClassName, function( item ) {
					return {
						label: item.name,
						value: item.name.replace(/&nbsp;/ig,"")
					}
				}));
				return;
			}
			$.ajax({
				url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=1&portfolioID="+mpiq_customize_portfolioID,
				dataType: "jsonp",
				data: request,
				success: function( data ) {
					response( $.map( data.AssetClassName, function( item ) {
						return {
							label: item.name,
							value: item.name.replace(/&nbsp;/ig,"")
						}
					}));
					mpiq_stable_cache[ request.term ] = data;
				},
				error:function(){
					alert("error");
				}
			});
		},
		open: function (){
			jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
			jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
		},
		select:function(event,ui){
			$(this).val(ui.item.value);
			mpiq_changeFundNum(0);
		}
	}).data( "autocomplete" )._renderItem = function( ul, item ) {
		ul.addClass("mpiq_autocomplete");
			return $( "<li></li>" )
				.data( "item.autocomplete", item )
				.append( "<a>" + item.label + "</a>" )
				.appendTo( ul );
		
	};
}

function mpiq_changeFundNum(index){
	var assetClassName ="";
     var rTrSize = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
	 var sTrSize = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
	 var num =0;
	 for(var i=0;i<rTrSize;i++){
	     if($("#mpiq_widget_${widgetName}_riskyasset_"+i).val()=="")
	       continue;
	    if(num!=0)
	      assetClassName+=",";
	   assetClassName+=$("#mpiq_widget_${widgetName}_riskyasset_"+i).val(); 
	    num++;  
	 }
	 for(var j=0;j<sTrSize;j++){
	 if($("#mpiq_widget_${widgetName}_stableasset_"+j).val()=="")
	       continue;
	       if(num!=0)
	         assetClassName+=",";
	    assetClassName+= $("#mpiq_widget_${widgetName}_stableasset_"+j).val(); 
	    num++; 
	 }
	 var url = "http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getAssetFundNum.action?assetClassName="+assetClassName;
	 mpiq_load_data_from_server(url,function(data){
		 var numItem = new Array();
		 for(var i=0;i<data.size;i++){
			 numItem[i]= data.number[i];
		 }
         var riskynum=0;
         var stablenum=0;
         for(var i=0;i<rTrSize;i++){
            var type="riskyasset";
            if($("#mpiq_widget_${widgetName}_riskyasset_"+i).val()=="")
       			continue;
       			
            $("#mpiq_widget_${widgetName}_riskyfundnum_"+i).val(numItem[riskynum]);
            if(numItem[riskynum]!= "0"){
               var tdhtml = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(2)").html().trim();
               var td4html = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(4)").html().trim();
               var td6html = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(6)").html().trim();
               if(tdhtml==""){
                   var ht = "<a id='mpiq_widget_${widgetName}_riskyButton_"+i+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\""+type+"\","+i+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
                   $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(2)").html(ht);
               }
               if(td4html!=""){
                 $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(4)").html("");
               }
               if(td6html!=""){
               $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(6)").html("");
               }
               
            }else{
                var tdhtml = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(4)").html().trim();		                
                var td2html = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(2)").html().trim();
                var td6html = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(6)").html().trim();
                if(tdhtml==""){
                  var ht = "<a title='No Funds in Plan' href='javascript:void(0)'><img border=0 alt='No Funds in Plan' height=15px width=15px src='/LTISystem/jsp/template/ed/images/gth.png'></a>";
                  $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(4)").html(ht);
                }
                
                if(td2html!=""){
                  $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(2)").html("");
                }
                if(td6html==""){
                 var td6 = "<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdivide_"+i+"' value='Scale to other' onclick='mpiq_divideRisky("+i+",1)'>";
                 $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(6)").html(td6);
                 // $("#rdivide_"+i).button();
                }
            }
            riskynum++;
         }
         
         for(j=0;j<sTrSize;j++){ 
            var type="stableasset";
            if($("#mpiq_widget_${widgetName}_stableasset_"+j).val()=="")
       			continue;
       			
            $("#mpiq_widget_${widgetName}_stablefundnum_"+j).val(numItem[stablenum+riskynum]);
            if(numItem[j+rTrSize]!= "0"){
               var tdhtml = $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(2)").html().trim();
               var td4html = $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(4)").html().trim();
               var td6html = $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(6)").html().trim();
               if(tdhtml==""){
                   var ht = "<a id='mpiq_widget_${widgetName}_stableButton_"+j+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\""+type+"\","+j+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
                   $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(2)").html(ht);
               }
               if(td4html!=""){
                 $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(4)").html("");
               }
               if(td6html!=""){
                  $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(6)").html("");
               }
               
            }else{
                var tdhtml = $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(4)").html().trim();
                var td2html = $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(2)").html().trim();
                var td6html = $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(6)").html().trim();
                if(tdhtml==""){
                  var ht = "<a title='No Funds in Plan' href='javascript:void(0)'><img border=0 alt='No Funds in Plan' height=15px width=15px src='/LTISystem/jsp/template/ed/images/gth.png'></a>";
                  $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(4)").html(ht);
                }
                if(td2html!=""){
                   $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(2)").html("");
                }
                
                if(td6html==""){
                  var td6 = "<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdivide_"+j+"' value='Scale to other' onclick='mpiq_divideStable("+j+",1)'>";
                  $("#mpiq_widget_${widgetName}_stableTr_"+j+" td:eq(6)").html(td6);
                  // $("#sdivide_"+j).button();
                }
            }
            stablenum++;
         }
         
         // if(index == 1){
          // $("#changeplan_dialog").dialog("destroy");
         // }
	 });
	 	 
}


function mpiq_numFixed(curvalue){
    var curNum = new Number();
    curNum = parseFloat(curvalue);
    curNum = parseInt(curNum*100+0.1);
    curNum = curNum/100;
    var numString = curNum.toString();
    var stringItem = numString.split(".");
    if(stringItem[1]==undefined){
       var fixed = stringItem[0]+".00";
       return fixed;
    }else if(stringItem[1]!=undefined && stringItem[1].length<2){

        if(stringItem[1].length==0){
           stringItem[1]+="00";
        }else if(stringItem[1].length==1){
           stringItem[1]+="0";
        }
        var fixed = stringItem[0]+"."+stringItem[1];
         return fixed;
    }else if(stringItem[1]!=undefined && stringItem[1].length==2){

       var fixed = stringItem[0]+"."+stringItem[1];
        return fixed;
       
    }
 }


function mpiq_resultNumFixed(curvalue){
    var numString = curvalue.toString();
    var stringItem = numString.split(".");
    var pointString="";
    if(stringItem[1].length>4){
      for(var i=0;i<4;i++){
         pointString+=stringItem[1].charAt(i);
      }
      var fixed = stringItem[0]+"."+pointString;
      return fixed;
    }
    return numString;
 }

function mpiq_deleteRisky(index,flag){
    var rTrSize = $("#mpiq_widget_${widgetName}_risky_asset tr").length;	     
    var percentage = $("#mpiq_widget_${widgetName}_riskypercent_"+index).val();
    var asset = $("mpiq_widget_${widgetName}_riskyasset_"+index).val();
    var totalPercent = parseFloat($("#mpiq_widget_${widgetName}_totalrisky").val());
    var curtotalPercent = 0;
    if(percentage == "")
        curtotalPercent = totalPercent;
        else{
        percentage = parseFloat(percentage);
        curtotalPercent = totalPercent-percentage;
        curtotalPercent +=0.001;

        }
        curtotalPercent = mpiq_numFixed(curtotalPercent);
        mpiq_curTotalRiskyPercent = curtotalPercent;

    $("#mpiq_widget_${widgetName}_riskyTr_"+index).remove();
    $("#mpiq_widget_${widgetName}_riskyasset_"+index).autocomplete("destroy");
    $("#mpiq_widget_${widgetName}_totalrisky").val(curtotalPercent);
    for(var i=index+1;i<rTrSize;i++){
      var curindex = i-1;
      var type = "riskyasset";
      var assetName = $("#mpiq_widget_${widgetName}_riskyasset_"+i).val();
      var percent = $("#mpiq_widget_${widgetName}_riskypercent_"+i).val();
      var td2html = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(2)").html();
      var td6html = $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(6)").html();
      var ht0 = "<input type='text' id='mpiq_widget_${widgetName}_riskyasset_"+curindex+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' onclick='mpiq_showAllRisky("+curindex+")'  ondblclick='mpiq_showRiskyTree("+curindex+")' value=\""+assetName+"\">";
      $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(0)").html(ht0);
      
      $("#mpiq_widget_${widgetName}_riskyfundnum_"+i).attr("id","mpiq_widget_${widgetName}_riskyfundnum_"+curindex);
      
      if(td2html!=""){
        var ht2="<a id='mpiq_widget_${widgetName}_riskyButton_"+curindex+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\""+type+"\","+curindex+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
        $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(2)").html(ht2);
      }
      
      var ht3 = "<input type='text' id='mpiq_widget_${widgetName}_riskypercent_"+curindex+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' onblur='mpiq_changeRiskyPercent("+curindex+")' value=\""+percent+"\">";
      $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(3)").html(ht3);
      
      var ht5 = "<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdelete_"+curindex+"' value='Delete' onclick='mpiq_deleteRisky("+curindex+",1)'>"
      $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(5)").html(ht5);
      // $("#rdelete_"+curindex).button();
      
      if(td6html!=""){
        var ht6="<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdivide_"+curindex+"' value='Scale to other' onclick='mpiq_divideRisky("+curindex+",1)'>";
		  $("#mpiq_widget_${widgetName}_riskyTr_"+i+" td:eq(6)").html(ht6);
		  // $("#rdivide_"+curindex).button();
      }
         
      $("#mpiq_widget_${widgetName}_riskyTr_"+i).attr("id","mpiq_widget_${widgetName}_riskyTr_"+curindex);
      
      $("#mpiq_widget_${widgetName}_riskyasset_"+i).autocomplete("destroy");
      
      $("#mpiq_widget_${widgetName}_riskyasset_"+curindex).autocomplete({
		minLength: 0,
		source: function( request, response ) {
			if(request.term == "")
		        request.term = "riskyspace";
			if ( request.term in mpiq_risk_cache) {
				response( $.map( mpiq_risk_cache[request.term].AssetClassName, function( item ) {
					return {
						label: item.name,
						value: item.name.replace(/&nbsp;/ig,"")
					}
				}));
				return;
			}
			$.ajax({
				url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=0&portfolioID="+mpiq_customize_portfolioID,
				dataType: "jsonp",
				data: request,
				success: function( data ) {
					response( $.map( data.AssetClassName, function( item ) {
						return {
							label: item.name,
							value: item.name.replace(/&nbsp;/ig,"")
						}
					}));
					mpiq_risk_cache[ request.term ] = data;
				},
				error:function(){
					alert("error");
				}
			});
		},
		open: function (){
			jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
			jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
		},
		select:function(event,ui){
			$(this).val(ui.item.value);
			mpiq_changeFundNum(0);
		}
	}).data( "autocomplete" )._renderItem = function( ul, item ) {
		ul.addClass("mpiq_autocomplete");
			return $( "<li></li>" )
				.data( "item.autocomplete", item )
				.append( "<a>" + item.label + "</a>" )
				.appendTo( ul );
		
	  };
    }     
    if(asset == "") return;
    if(flag ==1)
     mpiq_changeFundNum(0);
  }

function mpiq_deleteStable(index,flag){
    var rTrSize = $("#mpiq_widget_${widgetName}_stable_asset tr").length;	     
    var percentage = $("#mpiq_widget_${widgetName}_stablepercent_"+index).val();
    var asset = $("mpiq_widget_${widgetName}_stableasset_"+index).val();
    var totalPercent = parseFloat($("#mpiq_widget_${widgetName}_totalstable").val());
    var curtotalPercent = 0;
    if(percentage == "")
        curtotalPercent = totalPercent;
        else{
        percentage = parseFloat(percentage);
        curtotalPercent = totalPercent-percentage;
        curtotalPercent +=0.001;

        }
        curtotalPercent = mpiq_numFixed(curtotalPercent);
        mpiq_curTotalStablePercent = curtotalPercent;

    $("#mpiq_widget_${widgetName}_stableTr_"+index).remove();
    $("#mpiq_widget_${widgetName}_stableasset_"+index).autocomplete("destroy");
    $("#mpiq_widget_${widgetName}_totalstable").val(curtotalPercent);
    for(var i=index+1;i<rTrSize;i++){
      var curindex = i-1;
      var type = "stableasset";
      var assetName = $("#mpiq_widget_${widgetName}_stableasset_"+i).val();
      var percent = $("#mpiq_widget_${widgetName}_stablepercent_"+i).val();
      var td2html = $("#mpiq_widget_${widgetName}_stableTr_"+i+" td:eq(2)").html();
      var td6html = $("#mpiq_widget_${widgetName}_stableTr_"+i+" td:eq(6)").html();
      var ht0 = "<input type='text' id='mpiq_widget_${widgetName}_stableasset_"+curindex+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' onclick='mpiq_showAllStable("+curindex+")'  ondblclick='mpiq_showStableTree("+curindex+")' value=\""+assetName+"\">";
      $("#mpiq_widget_${widgetName}_stableTr_"+i+" td:eq(0)").html(ht0);
      
      $("#mpiq_widget_${widgetName}_stablefundnum_"+i).attr("id","mpiq_widget_${widgetName}_stablefundnum_"+curindex);
      
      if(td2html!=""){
        var ht2="<a id='mpiq_widget_${widgetName}_stableButton_"+curindex+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\""+type+"\","+curindex+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
        $("#mpiq_widget_${widgetName}_stableTr_"+i+" td:eq(2)").html(ht2);
      }
      
      var ht3 = "<input type='text' id='mpiq_widget_${widgetName}_stablepercent_"+curindex+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' onblur='mpiq_changeStablePercent("+curindex+")' value=\""+percent+"\">";
      $("#mpiq_widget_${widgetName}_stableTr_"+i+" td:eq(3)").html(ht3);
      
      var ht5 = "<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdelete_"+curindex+"' value='Delete' onclick='mpiq_deleteStable("+curindex+",1)'>"
      $("#mpiq_widget_${widgetName}_stableTr_"+i+" td:eq(5)").html(ht5);
      // $("#rdelete_"+curindex).button();
      
      if(td6html!=""){
        var ht6="<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdivide_"+curindex+"' value='Scale to other' onclick='mpiq_divideStable("+curindex+",1)'>";
		  $("#mpiq_widget_${widgetName}_stableTr_"+i+" td:eq(6)").html(ht6);
		  // $("#rdivide_"+curindex).button();
      }
         
      $("#mpiq_widget_${widgetName}_stableTr_"+i).attr("id","mpiq_widget_${widgetName}_stableTr_"+curindex);
      
      $("#mpiq_widget_${widgetName}_stableasset_"+i).autocomplete("destroy");
      
      $("#mpiq_widget_${widgetName}_stableasset_"+curindex).autocomplete({
		minLength: 0,
		source: function( request, response ) {
			if(request.term == "")
		        request.term = "stablespace";
			if ( request.term in mpiq_stable_cache) {
				response( $.map( mpiq_stable_cache[request.term].AssetClassName, function( item ) {
					return {
						label: item.name,
						value: item.name.replace(/&nbsp;/ig,"")
					}
				}));
				return;
			}
			$.ajax({
				url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=1&portfolioID="+mpiq_customize_portfolioID,
				dataType: "jsonp",
				data: request,
				success: function( data ) {
					response( $.map( data.AssetClassName, function( item ) {
						return {
							label: item.name,
							value: item.name.replace(/&nbsp;/ig,"")
						}
					}));
					mpiq_stable_cache[ request.term ] = data;
				},
				error:function(){
					alert("error");
				}
			});
		},
		open: function (){
			jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
			jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
		},
		select:function(event,ui){
			$(this).val(ui.item.value);
			mpiq_changeFundNum(0);
		}
	}).data( "autocomplete" )._renderItem = function( ul, item ) {
		ul.addClass("mpiq_autocomplete");
			return $( "<li></li>" )
				.data( "item.autocomplete", item )
				.append( "<a>" + item.label + "</a>" )
				.appendTo( ul );
		
	  };
    }     
    if(asset == "") return;
    if(flag ==1)
     mpiq_changeFundNum(0);
  }

function mpiq_divideRisky(index,flag){
    
    if($("#mpiq_widget_${widgetName}_riskypercent_"+index).val()=="")
       return;
    var curDividePercent = $("#mpiq_widget_${widgetName}_riskypercent_"+index).val();              
    var curTotalPercent = $("#mpiq_widget_${widgetName}_totalrisky").val();
    if(flag ==1){
      mpiq_deleteRisky(index,1);
    }else if(flag==0){
     mpiq_deleteRisky(index,0);
    }
    
    
    mpiq_curTotalRiskyPercent = mpiq_numFixed(curTotalPercent);
    var size = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
    var fundTrSize = $("#mpiq_widget_${widgetName}_tb_riskyFund tr").length;
    var totalSize = size+fundTrSize;

    curDividePercent = parseFloat(curDividePercent);
    
    var addPercent = mpiq_numFixed(curDividePercent/totalSize);

    addPercent = parseFloat(addPercent);

    var totalChangePercent = 0;
    
    for(var j=0;j<mpiq_riskyFundTrNum;j++){
     var curfundpercent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+j).val();
     if(curfundpercent == undefined)
        continue;
     curfundpercent = parseFloat(curfundpercent);
     if(isNaN(curfundpercent))curfundpercent = 0;
     var changePercent = curfundpercent+addPercent;
     totalChangePercent +=changePercent;
     changePercent = mpiq_numFixed(changePercent);
     $("#mpiq_widget_${widgetName}_riskyFundPercent_"+j).val(changePercent);
   }
   
    for(var i=0;i<size-1;i++){
         var curPercent = parseFloat($("#mpiq_widget_${widgetName}_riskypercent_"+i).val());
         if(isNaN(curPercent)){curPercent = 0;}
         var changePercent = curPercent+addPercent;
         totalChangePercent +=changePercent;
         changePercent = mpiq_numFixed(changePercent);
         $("#mpiq_widget_${widgetName}_riskypercent_"+i).val(changePercent);
         
    }
    $("#mpiq_widget_${widgetName}_totalrisky").val(curTotalPercent);
    var lastNum = size-1;
    curTotalPercent = parseFloat(curTotalPercent);

    var lastCurPercent = curTotalPercent - totalChangePercent+0.001;
    
    lastCurPercent = mpiq_numFixed(lastCurPercent);
    $("#mpiq_widget_${widgetName}_riskypercent_"+lastNum).val(lastCurPercent);    
 }

function mpiq_divideStable(index,flag){
    
    if($("#mpiq_widget_${widgetName}_stablepercent_"+index).val()=="")
       return;
    var curDividePercent = $("#mpiq_widget_${widgetName}_stablepercent_"+index).val();              
    var curTotalPercent = $("#mpiq_widget_${widgetName}_totalstable").val();
    if(flag ==1){
      mpiq_deleteStable(index,1);
    }else if(flag==0){
     mpiq_deleteStable(index,0);
    }
    
    
    mpiq_curTotalStablePercent = mpiq_numFixed(curTotalPercent);
    var size = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
    var fundTrSize = $("#mpiq_widget_${widgetName}_tb_stableFund tr").length;
    var totalSize = size+fundTrSize;

    curDividePercent = parseFloat(curDividePercent);
    
    var addPercent = mpiq_numFixed(curDividePercent/totalSize);

    addPercent = parseFloat(addPercent);

    var totalChangePercent = 0;
    
    for(var j=0;j<mpiq_stableFundTrNum;j++){
     var curfundpercent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val();
     if(curfundpercent == undefined)
        continue;
     curfundpercent = parseFloat(curfundpercent);
     if(isNaN(curfundpercent))curfundpercent = 0;
     var changePercent = curfundpercent+addPercent;
     totalChangePercent +=changePercent;
     changePercent = mpiq_numFixed(changePercent);
     $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val(changePercent);
   }
   
    for(var i=0;i<size-1;i++){
         var curPercent = parseFloat($("#mpiq_widget_${widgetName}_stablepercent_"+i).val());
         if(isNaN(curPercent)){curPercent = 0;}
         var changePercent = curPercent+addPercent;
         totalChangePercent +=changePercent;
         changePercent = mpiq_numFixed(changePercent);
         $("#mpiq_widget_${widgetName}_stablepercent_"+i).val(changePercent);
         
    }
    $("#mpiq_widget_${widgetName}_totalstable").val(curTotalPercent);
    var lastNum = size-1;
    curTotalPercent = parseFloat(curTotalPercent);

    var lastCurPercent = curTotalPercent - totalChangePercent+0.001;
    
    lastCurPercent = mpiq_numFixed(lastCurPercent);
    $("#mpiq_widget_${widgetName}_stablepercent_"+lastNum).val(lastCurPercent);    
 }

function mpiq_changeRiskyPercent(index){
    var curvalue = $("#mpiq_widget_${widgetName}_riskypercent_"+index).val();
    if(curvalue=="")
        return;
    var fixed = mpiq_numFixed(curvalue);
     $("#mpiq_widget_${widgetName}_riskypercent_"+index).val(fixed);
     
    var size = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
    var totalPercent =new Number(0);
    for(var i=0;i<size;i++){
       var perc = new Number();
       perc = parseFloat($("#mpiq_widget_${widgetName}_riskypercent_"+i).val());
       if(perc !="" &&!isNaN(perc))
          totalPercent+=perc;
    }
    
    for(var j=0;j<mpiq_riskyFundTrNum;j++){
     var curfundpercent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+j).val();
     if(curfundpercent == undefined)
        continue;
     curfundpercent = parseFloat(curfundpercent);
     if(isNaN(curfundpercent))curfundpercent = 0;
     totalPercent +=curfundpercent;
   }
   
    totalPercent = mpiq_numFixed(totalPercent);
    $("#mpiq_widget_${widgetName}_totalrisky").val(totalPercent);

    mpiq_curTotalRiskyPercent = totalPercent;
 }

function mpiq_changeStablePercent(index){
    
    var curvalue = $("#mpiq_widget_${widgetName}_stablepercent_"+index).val();
    if(curvalue=="")
        return;
    var fixed = mpiq_numFixed(curvalue);
     $("#mpiq_widget_${widgetName}_stablepercent_"+index).val(fixed);
    var size = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
    var totalPercent =0;
    for(var i=0;i<size;i++){
       var perc = parseFloat($("#mpiq_widget_${widgetName}_stablepercent_"+i).val());
       if(perc !="" &&!isNaN(perc))
          totalPercent+=perc;
    }
    
    for(var j=0;j<mpiq_stableFundTrNum;j++){
     var curfundpercent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val();
     if(curfundpercent == undefined)
        continue;
     curfundpercent = parseFloat(curfundpercent);
     if(isNaN(curfundpercent))curfundpercent = 0;
     totalPercent +=curfundpercent;
   }
   
    totalPercent = mpiq_numFixed(totalPercent);
    $("#mpiq_widget_${widgetName}_totalstable").val(totalPercent);
     totalPercent = parseFloat(totalPercent);
     mpiq_curTotalStablePercent = mpiq_numFixed(totalPercent);
 }

function mpiq_changeTotalRiskyPercent(){
    var totalRiskyPercent = $("#mpiq_widget_${widgetName}_totalrisky").val();

    totalRiskyPercent = mpiq_numFixed(totalRiskyPercent);


    $("#mpiq_widget_${widgetName}_totalrisky").val(totalRiskyPercent);
    if(totalRiskyPercent == mpiq_curTotalRiskyPercent)
       return;

     totalRiskyPercent = parseFloat(totalRiskyPercent);  
    var curTRPercentNum = parseFloat(mpiq_curTotalRiskyPercent);
   var size = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
   var totalChangePercent = 0;
   
 for(var j=0;j<mpiq_riskyFundTrNum;j++){
   var curfundpercent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+j).val();
   if(curfundpercent == undefined)
      continue;
   curfundpercent = parseFloat(curfundpercent);
   if(isNaN(curfundpercent))curfundpercent = 0;
   var changePercent = totalRiskyPercent/curTRPercentNum*curfundpercent;

   changePercent = mpiq_numFixed(changePercent);
   $("#mpiq_widget_${widgetName}_riskyFundPercent_"+j).val(changePercent);
   changePercent = parseFloat(changePercent);
   totalChangePercent +=changePercent;

 }
   
   for(var i=0;i<size-1;i++){
      var curPercent = $("#mpiq_widget_${widgetName}_riskypercent_"+i).val();
      curPercent = parseFloat(curPercent);
      if(isNaN(curPercent)){curPercent = 0;}
      var changePercent = totalRiskyPercent/curTRPercentNum*curPercent;
      changePercent = mpiq_numFixed(changePercent);

      $("#mpiq_widget_${widgetName}_riskypercent_"+i).val(changePercent);
      
      changePercent = parseFloat(changePercent);
      totalChangePercent +=changePercent;
   }
   var lastChangePercent = totalRiskyPercent-totalChangePercent+0.001;
   lastChangePercent = mpiq_numFixed(lastChangePercent);
   var lastNum = size-1;
    $("#mpiq_widget_${widgetName}_riskypercent_"+lastNum).val(lastChangePercent);
    mpiq_curTotalRiskyPercent = mpiq_numFixed(totalRiskyPercent);
}

function mpiq_changeTotalStablePercent(){
    var totalStablePercent = $("#mpiq_widget_${widgetName}_totalstable").val();
    totalStablePercent = mpiq_numFixed(totalStablePercent);
    $("#mpiq_widget_${widgetName}_totalstable").val(totalStablePercent);

    if(totalStablePercent == mpiq_curTotalStablePercent)
       return;
      
     totalStablePercent = parseFloat(totalStablePercent);  
    var curSTPercentNum = parseFloat(mpiq_curTotalStablePercent);
   var size = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
   var totalChangePercent = 0;
   
  for(var j=0;j<mpiq_stableFundTrNum;j++){
   var curfundpercent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val();
   if(curfundpercent == undefined)
      continue;
   curfundpercent = parseFloat(curfundpercent);
   if(isNaN(curfundpercent))curfundpercent = 0;
   var changePercent = totalStablePercent/curSTPercentNum*curfundpercent;

   changePercent = mpiq_numFixed(changePercent);
   $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val(changePercent);
   changePercent = parseFloat(changePercent);
   totalChangePercent +=changePercent;

 }
 
   for(var i=0;i<size-1;i++){
      var curPercent = $("#mpiq_widget_${widgetName}_stablepercent_"+i).val();
      curPercent = parseFloat(curPercent);
      if(isNaN(curPercent)){curPercent = 0;}
      var changePercent = totalStablePercent/curSTPercentNum*curPercent;

      changePercent = mpiq_numFixed(changePercent);
     
      $("#mpiq_widget_${widgetName}_stablepercent_"+i).val(changePercent);
      changePercent = parseFloat(changePercent);
      totalChangePercent +=changePercent;
   }
   var lastChangePercent = totalStablePercent-totalChangePercent+0.001;
   lastChangePercent = mpiq_numFixed(lastChangePercent);
   var lastNum = size-1;
    $("#mpiq_widget_${widgetName}_stablepercent_"+lastNum).val(lastChangePercent);
    mpiq_curTotalStablePercent = mpiq_numFixed(totalStablePercent);
}

function mpiq_showFundtable(type,index){

    var assetClassName = $("#mpiq_widget_${widgetName}_"+type+"_"+index).val();
    var planName = $("#mpiq_widget_${widgetName}_t_planname").val();	    

	var theEvent = window.event || arguments.callee.caller.arguments[0];
	var top = theEvent.clientY;
	if(top >165)
	   top = top -165;
	 else
	    top =0;
	var left = theEvent.clientX;
    var url = "http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getFundtable.action?planName="+planName+"&assetClassName="+assetClassName;
    mpiq_load_data_from_server(url,function(data){
    	var html="";
    	if(data.result == false){
    		html="The plan doesn't not exist";
    	}else{
    		html = "<table cellspacing='5px' style='font-size:0.7em;width:280'><thead><tr><th>Ticker</th><th>Description</th></tr></thead><tbody>";
    		for(var i=0;i<data.items.length;i++){
    			html+="<tr><td>";
    			html+= data.items[i].symbol;
    			html+="</td><td>";
    			html+=data.items[i].description;
    			html+="</td></tr>";
    		}
    		html+="</tbody>";
    		html+="</table>";
    	}  	
    	$("#mpiq_widget_${widgetName}_fundtable_dialog").html(html);
        $("#mpiq_widget_${widgetName}_fundtable_dialog").dialog({
        autoOpen: false,
        resizable: true,
     	height:150,
     	width: 290,
     	position:[left,top],
     	modal: false
     	
        });
        $("#ui-dialog-title-mpiq_widget_customizewidget_fundtable_dialog").removeClass("ui-widget");
        $("#ui-dialog-title-mpiq_widget_customizewidget_fundtable_dialog").addClass("mpiq_customize_ui_widget");
        $("#mpiq_widget_${widgetName}_fundtable_dialog").dialog("open");
    });

 }


function mpiq_addSingleFund(){
    var fundName = $("#mpiq_widget_${widgetName}_single_ticker").val();
    var percent = $("#mpiq_widget_${widgetName}_single_percent").val();
    if(fundName==""){
      alert('The "ticker" is empty, please fill it');
      return;
    }
    if(percent==""){
     alert('The "Allocation Target" is empty, please fill it');
     return;
    }
    percent = mpiq_numFixed(percent);
    var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getFundType.action?fundName="+fundName;
    mpiq_load_data_from_server(url,function(data){
        if(data.type=="not fund"){
            alert("The ticker is not correct.")
            return;
   }else if(data.type == "Risky Asset"){
           var $pane = $("#mpiq_widget_${widgetName}_tb_riskyFund");
           var $tr,$td;
        	$tr = $(document.createElement("tr"));
        	$tr.attr({id:'mpiq_widget_${widgetName}_riskyFundTr_'+mpiq_riskyFundTrNum});
        	$pane.append($tr);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='text' id='mpiq_widget_${widgetName}_riskyfund_"+mpiq_riskyFundTrNum+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' value="+fundName+">");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='text'  style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true' value='1'>");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<a  title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showSingleFundtable(\"riskyfund\","+mpiq_riskyFundTrNum+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='text' id='mpiq_widget_${widgetName}_riskyFundPercent_"+mpiq_riskyFundTrNum+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' value="+percent+"  onblur='mpiq_changeRiskyFundPercent("+mpiq_riskyFundTrNum+")'>");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdeleteFund_"+mpiq_riskyFundTrNum+"' value='Delete' onclick='mpiq_deleteRiskyFund("+mpiq_riskyFundTrNum+")'>");
        	$tr.append($td);
        	mpiq_riskyFundTrNum++;
        	var totalRiskyPercent = $("#mpiq_widget_${widgetName}_totalrisky").val();
        	totalRiskyPercent = parseFloat(totalRiskyPercent);
        	percent = parseFloat(percent);
        	totalRiskyPercent = totalRiskyPercent + percent;
        	totalRiskyPercent = mpiq_numFixed(totalRiskyPercent);
        	$("#mpiq_widget_${widgetName}_totalrisky").val(totalRiskyPercent);
        	mpiq_curTotalRiskyPercent = totalRiskyPercent;
        }else if(data.type == "Stable Asset"){
           var $pane = $("#mpiq_widget_${widgetName}_tb_stableFund");
           var $tr,$td;
        	$tr = $(document.createElement("tr"));
        	$tr.attr({id:'mpiq_widget_${widgetName}_stableFundTr_'+mpiq_stableFundTrNum});
        	$pane.append($tr);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='text' id='mpiq_widget_${widgetName}_stablefund_"+mpiq_stableFundTrNum+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' value="+fundName+">");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='text'  style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true' value='1'>");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<a  title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showSingleFundtable(\"stablefund\","+mpiq_stableFundTrNum+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='text' id='mpiq_widget_${widgetName}_stableFundPercent_"+mpiq_stableFundTrNum+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' value="+percent+"  onblur='mpiq_changeStableFundPercent("+mpiq_stableFundTrNum+")'>");
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$tr.append($td);
        	$td = $(document.createElement("td"));
        	$td.html("<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdeleteFund_"+mpiq_stableFundTrNum+"' value='Delete' onclick='mpiq_deleteStableFund("+mpiq_stableFundTrNum+")'>");
        	$tr.append($td);
        	mpiq_stableFundTrNum++;
        	var totalStablePercent = $("#mpiq_widget_${widgetName}_totalstable").val();
        	totalStablePercent = parseFloat(totalStablePercent);
        	percent = parseFloat(percent);
        	totalStablePercent = totalStablePercent + percent;
        	totalStablePercent = mpiq_numFixed(totalStablePercent);
        	$("#mpiq_widget_${widgetName}_totalstable").val(totalStablePercent);
        	mpiq_curTotalStablePercent = totalStablePercent;
        }
         
        mpiq_changeFundNum(0);
        
        $("#mpiq_widget_${widgetName}_single_fund").html("<tr> <td><input type='text' style='font-size:0.8em' class='ui-widget ui-widget-content ui-corner-all' id='mpiq_widget_${widgetName}_single_ticker'></td><td><input type='text' style='font-size:0.8em' class='ui-widget ui-widget-content ui-corner-all' id='mpiq_widget_${widgetName}_single_percent'></td><td><input type='button' id='mpiq_widget_${widgetName}_btn_addfund' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' class='uiButton ui-button ui-state-default ui-corner-all' value='Add' onclick='mpiq_addSingleFund()'></td></tr>");
        $("#mpiq_widget_${widgetName}_single_ticker").autocomplete({
			minLength: 1,
			source: function(request, response) {			    
				if ( request.term in mpiq_fund_cache) {
					response( $.map( data.funds, function( item ) {
						return {								
							value: item.fund
						}
					}));
					return;
				}
				$.ajax({
					url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchFund.action",
					dataType: "jsonp",
					data: request,
					success: function( data ) {
						response( $.map( data.funds, function( item ) {
							return {								
								value: item.fund
							}
						}));
						mpiq_fund_cache[ request.term ] = data;
					},
					error:function(){
						alert("error");
					}
				});
			},
			open: function (){
				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
			},
			select: function(event, ui) { 			 				 					 	
			 		$(this).val(ui.item.value);			 				 				 	
			}
						
		}).data( "autocomplete" )._renderItem = function( ul, item ) {
			ul.addClass("mpiq_autocomplete");
			return $( "<li></li>" )
				.data( "item.autocomplete", item )
				.append( "<a>" + item.value + "</a>" )
				.appendTo( ul );
		
        };
    });

  }
  
function mpiq_deleteRiskyFund(index){
    var percent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+index).val();
    var fund = $("#mpiq_widget_${widgetName}_riskyfund_"+index).val();
    percent = parseFloat(percent);
    var url="http://${serverName}:${port}/LTISystem/widgets/customizewidget/deleteFund.action?fundName="+fund;
    mpiq_load_data_from_server(url,function(data){
    	$("#mpiq_widget_${widgetName}_riskyFundTr_"+index).remove();
		var totalRiskyPercent = $("#mpiq_widget_${widgetName}_totalrisky").val();
		totalRiskyPercent = parseFloat(totalRiskyPercent);
		totalRiskyPercent = totalRiskyPercent -percent+0.001;
		totalRiskyPercent = mpiq_numFixed(totalRiskyPercent);
		$("#mpiq_widget_${widgetName}_totalrisky").val(totalRiskyPercent);
		mpiq_curTotalRiskyPercent = totalRiskyPercent; 
		mpiq_changeFundNum(0); 
    });      
  }

function mpiq_deleteStableFund(index){
	 	var percent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+index).val();
	    var fund = $("#mpiq_widget_${widgetName}_stablefund_"+index).val();
	    percent = parseFloat(percent);
	    var url="http://${serverName}:${port}/LTISystem/widgets/customizewidget/deleteFund.action?fundName="+fund;
	    mpiq_load_data_from_server(url,function(data){
	    	$("#mpiq_widget_${widgetName}_stableFundTr_"+index).remove();
			var totalStablePercent = $("#mpiq_widget_${widgetName}_totalstable").val();
			totalStablePercent = parseFloat(totalStablePercent);
			totalStablePercent = totalStablePercent -percent+0.001;
			totalStablePercent = mpiq_numFixed(totalStablePercent);
			$("#mpiq_widget_${widgetName}_totalstable").val(totalStablePercent);
			mpiq_curTotalStablePercent = totalStablePercent; 
			mpiq_changeFundNum(0); 
	    });	
}

function mpiq_changeRiskyFundPercent(index){
    var percent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+index).val();
    if(percent=="")
      return;
    percent = parseFloat(percent);
    if(isNaN(percent))
      return;
   var rTrSize = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
   var totalRiskyPercent = 0;
   for(var i=0;i<rTrSize;i++){
      var curpercent = $("#mpiq_widget_${widgetName}_riskypercent_"+i).val();
      curpercent = parseFloat(curpercent);
      if(isNaN(curpercent)){curpercent=0;}
      totalRiskyPercent +=curpercent;
   }
   for(var j=0;j<mpiq_riskyFundTrNum;j++){
     var curfundpercent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+j).val();
     if(curfundpercent == undefined)
        continue;
     curfundpercent = parseFloat(curfundpercent);
     if(isNaN(curfundpercent))curfundpercent = 0;
     totalRiskyPercent +=curfundpercent;
   }
   totalRiskyPercent = mpiq_numFixed(totalRiskyPercent);
   $("#mpiq_widget_${widgetName}_totalrisky").val(totalRiskyPercent);
   mpiq_curTotalRiskyPercent = totalRiskyPercent;
}


function mpiq_changeStableFundPercent(index){
    var percent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+index).val();
    if(percent=="")
      return;
    percent = parseFloat(percent);
    if(isNaN(percent))
      return;
   var sTrSize = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
   var totalStablePercent = 0;
   for(var i=0;i<sTrSize;i++){
      var curpercent = $("#mpiq_widget_${widgetName}_stablepercent_"+i).val();
      curpercent = parseFloat(curpercent);
      if(isNaN(curpercent)){curpercent=0;}
      totalStablePercent +=curpercent;
   }
   for(var j=0;j<mpiq_stableFundTrNum;j++){
     var curfundpercent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val();
     if(curfundpercent == undefined)
        continue;
     curfundpercent = parseFloat(curfundpercent);
     if(isNaN(curfundpercent))curfundpercent = 0;
     totalStablePercent +=curfundpercent;
   }
   totalStablePercent = mpiq_numFixed(totalStablePercent);
   $("#mpiq_widget_${widgetName}_totalstable").val(totalStablePercent);
   mpiq_curTotalStablePercent = totalStablePercent;
}

function mpiq_showSingleFundtable(type,index){
   var fundName = $("#mpiq_widget_${widgetName}_"+type+"_"+index).val();
   var planName = $("#mpiq_widget_${widgetName}_t_planname").val();
   if(fundName=="") return;
   var theEvent = window.event || arguments.callee.caller.arguments[0];
    var top = theEvent.clientY;
   if(top >165)
   	top = top -165;
   else
    	 top =0;
   var left = theEvent.clientX;
   var url = "http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getFundInfo.action?fundName="+fundName+"&planName="+planName;
   mpiq_load_data_from_server(url,function(data){
   	var html="";
   	if(data.result == false){
   		html="The plan doesn't not exist";
   	}else{
   		html = "<table cellspacing='5px' style='font-size:0.7em;width:280'><thead><tr><th>Ticker</th><th>Description</th></tr></thead><tbody>";
   		html+="<tr><td>";
   		html+= data.symbol;
   		html+="</td><td>";
   		html+=data.description;
   		html+="</td></tr>";  		
   		html+="</tbody>";
   		html+="</table>";
   	}  	
   	$("#mpiq_widget_${widgetName}_fundtable_dialog").html(html);
       $("#mpiq_widget_${widgetName}_fundtable_dialog").dialog({
    	autoOpen: false,
       	resizable: true,
    	height:150,
    	width: 290,
    	position:[left,top],
    	modal: false
    	
       });
       $("#ui-dialog-title-mpiq_widget_customizewidget_fundtable_dialog").removeClass("ui-widget");
       $("#ui-dialog-title-mpiq_widget_customizewidget_fundtable_dialog").addClass("mpiq_customize_ui_widget");
       $("#mpiq_widget_${widgetName}_fundtable_dialog").dialog("open");
   });
}

function mpiq_changeTemplate(){
    var templateName = $("#mpiq_widget_${widgetName}_myselect").val();
    if(templateName == "")
    return;
    if(templateName == mpiq_curTemplateName)
       return;
    mpiq_curTemplateName = templateName;
    var fundName = "";        
    var count=0;
    for(var i=0;i<mpiq_riskyFundTrNum;i++){
       var fund = $("#mpiq_widget_${widgetName}_riskyfund_"+i).val();
       if(fund==undefined)
       continue
       if(count!=0)
        fundName+=",";
       fundName+=fund;
       count++;
    }
    
    for(var j=0;j<mpiq_stableFundTrNum;j++){
       var fund = $("#mpiq_widget_${widgetName}_stablefund_"+j).val();
       if(fund==undefined)
       continue
       fundName+=",";
       fundName+=fund;
       count++;
    }
    
    var rTrSize = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
	var sTrSize = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
	for(var i=0;i<rTrSize;i++){
	    $("#mpiq_widget_${widgetName}_riskyasset_"+i).autocomplete("destroy");
	  }
	for(var j=0;j<sTrSize;j++){
	  $("#mpiq_widget_${widgetName}_stableasset_"+j).autocomplete("destroy");
	  }
	
    var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getTemplateInfo.action?templateName="+templateName+"&fundName="+fundName;
    mpiq_load_data_from_server(url,function(data){
    	$("#mpiq_widget_${widgetName}_description").attr("title",data.description);
        $("#mpiq_widget_${widgetName}_description").children("img").attr("alt",data.description);
                       
		var riskHtml = "";
	    for(var key in data.riskyAssetClassList){
	    	if(data.riskyAssetClassList[key]!=undefined && key!= "mpiq_remove"){
	    		riskHtml +="<tr id='mpiq_widget_${widgetName}_riskyTr_"+key+"'><td>";
	    		riskHtml +="<input type='text' id='mpiq_widget_${widgetName}_riskyasset_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' value=\""+data.riskyAssetClassList[key]+"\" onclick='mpiq_showAllRisky("+key+")' ondblclick='mpiq_showRiskyTree("+key+")'></td>";
	    	    riskHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_riskyfundnum_"+key+"' style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true'   value=\""+data.riskyFundNumList[key]+"\"></td>";
	    	    riskHtml +="<td>";
	    	    if(data.riskyFundNumList[key]!=0){
	    	    	riskHtml +="<a id='mpiq_widget_${widgetName}_riskyButton_"+key+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\"riskyasset\","+key+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
	    	    }
	    	    riskHtml +="</td>";
	    	    riskHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_riskypercent_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' value=\""+data.riskyPercentageList[key]+"\" onblur='mpiq_changeRiskyPercent("+key+")'></td>";
	    	    riskHtml +="<td>";
	    	    if(data.riskyFundNumList[key]==0){
	    	    	riskHtml +="<a title='No Funds in Plan' href='javascript:void(0)'><img border=0 alt='No Funds in Plan' height=15px width=15px src='/LTISystem/jsp/template/ed/images/gth.png'></a>";	    	    	
	    	    }
	    	    riskHtml +="</td>";
	    	    riskHtml +="<td><input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdelete_"+key+"' value='Delete' onclick='mpiq_deleteRisky("+key+",1)'></td>";
	    	    riskHtml +="<td>";
	    	    if(data.riskyFundNumList[key]==0){
	    	    	riskHtml +="<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_rdivide_"+key+"' value='Scale to other' onclick='mpiq_divideRisky("+key+",1)'>";	    	    	
	    	    }
	    	    riskHtml +="</td>";
	    	    riskHtml +="</tr>";
	    	}
	    }
	    
	    var stableHtml = "";
	    for(var key in data.stableAssetClassList){
	    	if(data.stableAssetClassList[key]!=undefined && key!= "mpiq_remove"){
	    		stableHtml +="<tr id='mpiq_widget_${widgetName}_stableTr_"+key+"'><td>";
	    		stableHtml +="<input type='text' id='mpiq_widget_${widgetName}_stableasset_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:200px' value=\""+data.stableAssetClassList[key]+"\" onclick='mpiq_showAllStable("+key+")' ondblclick='mpiq_showStableTree("+key+")'></td>";
	    		stableHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_stablefundnum_"+key+"' style='font-size:0.8em;width:100px' class='mpiq_readtext' readOnly='true'   value=\""+data.stableFundNumList[key]+"\"></td>";
	    		stableHtml +="<td>";
	    	    if(data.stableFundNumList[key]!=0){
	    	    	stableHtml +="<a id='mpiq_widget_${widgetName}_stableButton_"+key+"' title='Click here to view funds' href='javascript:void(0)' onclick='mpiq_showFundtable(\"stableasset\","+key+")'><img border=0 alt='Click here to view funds' height=15px width=15px src='/LTISystem/jsp/template/ed/images/info.png'></a>";
	    	    }
	    	    stableHtml +="</td>";
	    	    stableHtml +="<td><input type='text' id='mpiq_widget_${widgetName}_stablepercent_"+key+"' class='ui-widget ui-widget-content ui-corner-all' style='font-size:0.8em;width:100px' value=\""+data.stablePercentageList[key]+"\" onblur='mpiq_changeStablePercent("+key+")'></td>";
	    	    stableHtml +="<td>";
	    	    if(data.stableFundNumList[key]==0){
	    	    	stableHtml +="<a title='No Funds in Plan' href='javascript:void(0)'><img border=0 alt='No Funds in Plan' height=15px width=15px src='/LTISystem/jsp/template/ed/images/gth.png'></a>";	    	    	
	    	    }
	    	    stableHtml +="</td>";
	    	    stableHtml +="<td><input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdelete_"+key+"' value='Delete' onclick='mpiq_deleteStable("+key+",1)'></td>";
	    	    stableHtml +="<td>";
	    	    if(data.stableFundNumList[key]==0){
	    	    	stableHtml +="<input type='button' class='uiButton ui-button ui-state-default ui-corner-all' style='font-weight: bold;font-size:0.8em;font-family: Verdana,Arial,sans-serif;padding:0.2em 0.4em' id='mpiq_widget_${widgetName}_sdivide_"+key+"' value='Scale to other' onclick='mpiq_divideStable("+key+",1)'>";	    	    	
	    	    }
	    	    stableHtml +="</td>";
	    	    stableHtml +="</tr>";
	    	}
	    }
	    $("#mpiq_widget_${widgetName}_risky_asset").html(riskHtml);
	    $("#mpiq_widget_${widgetName}_stable_asset").html(stableHtml); 
	    mpiq_changeRiskyPercent(0);
        mpiq_changeStablePercent(0);
        if(templateName == "Default Equal Weight"){
            mpiq_handleDefaultTemp();
      }
        for(var key in data.riskyAssetClassList){
        	if(data.riskyAssetClassList[key]!=undefined && key!= "mpiq_remove"){
        		$("#mpiq_widget_${widgetName}_riskyasset_"+key).autocomplete({
        			minLength: 0,
        			source: function( request, response ) {
        				if(request.term == "")
        			        request.term = "riskyspace";
        				if ( request.term in mpiq_risk_cache) {
        					response( $.map( mpiq_risk_cache[request.term].AssetClassName, function( item ) {
    							return {
    								label: item.name,
    								value: item.name.replace(/&nbsp;/ig,"")
    							}
    						}));
        					return;
        				}
        				$.ajax({
        					url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=0&"+mpiq_params,
        					dataType: "jsonp",
        					data: request,
        					success: function( data ) {
        						response( $.map( data.AssetClassName, function( item ) {
        							return {
        								label: item.name,
        								value: item.name.replace(/&nbsp;/ig,"")
        							}
        						}));
        						mpiq_risk_cache[ request.term ] = data;
        					},
        					error:function(){
        						alert("error");
        					}
        				});
        			},
        			open: function (){
        				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
        				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
        			},
        			select:function(event,ui){
        				$(this).val(ui.item.value);
        				mpiq_changeFundNum(0);
        			}
        		}).data( "autocomplete" )._renderItem = function( ul, item ) {
        			ul.addClass("mpiq_autocomplete");
        				return $( "<li></li>" )
        					.data( "item.autocomplete", item )
        					.append( "<a>" + item.label + "</a>" )
        					.appendTo( ul );
        			
        		};
        	}
        }
        
        for(var key in data.stableAssetClassList){
        	if(data.stableAssetClassList[key]!=undefined && key!= "mpiq_remove"){
        		$("#mpiq_widget_${widgetName}_stableasset_"+key).autocomplete({
        			minLength: 0,
        			source: function( request, response ) {
        				if(request.term == "")
        			        request.term = "stablespace";
        				if ( request.term in mpiq_stable_cache) {
        					response( $.map(mpiq_stable_cache[request.term].AssetClassName, function( item ) {
    							return {
    								label: item.name,
    								value: item.name.replace(/&nbsp;/ig,"")
    							}
    						}));
        					return;
        				}
        				$.ajax({
        					url: "http://${serverName}:${port}/LTISystem/widgets/customizewidget/searchAssetClass.action?assetType=1&"+mpiq_params,
        					dataType: "jsonp",
        					data: request,
        					success: function( data ) {
        						response( $.map( data.AssetClassName, function( item ) {
        							return {
        								label: item.name,
        								value: item.name.replace(/&nbsp;/ig,"")
        							}
        						}));
        						mpiq_stable_cache[ request.term ] = data;
        					},
        					error:function(){
        						alert("error");
        					}
        				});
        			},
        			open: function (){
        				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
        				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
        			},
        			select:function(event,ui){
        				$(this).val(ui.item.value);
        				mpiq_changeFundNum(0);
        			}
        		}).data( "autocomplete" )._renderItem = function( ul, item ) {
        			ul.addClass("mpiq_autocomplete");
        				return $( "<li></li>" )
        					.data( "item.autocomplete", item )
        					.append( "<a>" + item.label + "</a>" )
        					.appendTo( ul );
        			
        		};
        	}
        }
        $("#mpiq_widget_${widgetName}_tb_riskyFund").html("");
        $("#mpiq_widget_${widgetName}_tb_stableFund").html("");
        mpiq_riskyFundTrNum = 0;
     	mpiq_stableFundTrNum = 0;  
        
    });
           	
}

function mpiq_saveTemplate(){
    var rTrSize = $("#mpiq_widget_${widgetName}_risky_asset tr").length;
	 var sTrSize = $("#mpiq_widget_${widgetName}_stable_asset tr").length;
	 var useDataObject = true;
	 
	  for(var n=0;n<rTrSize;n++){
	  var curassetName = $("#mpiq_widget_${widgetName}_riskyasset_"+n).val();
	  
	  
	  if(mpiq_defaultAssetClassName.indexOf(curassetName)==-1){
	    useDataObject = false;
	  }
	  
	  
	   for(var i=0;i<rTrSize;i++){
	   if( i==n){
	       continue;	     
	   }
	   if($("#mpiq_widget_${widgetName}_riskyasset_"+i).val()==curassetName){
	     alert("Two assetClass are same, please remove one of them.");
	     return;
	   }
	   
	 }
	 
	 for(var j=0;j<sTrSize;j++){
	   if($("#mpiq_widget_${widgetName}_stableasset_"+j).val()==curassetName){
	      alert("Two assetClass are same, please remove one of them.");
	     return;
	   }		   
	 }
}

for(var m=0;m<sTrSize;m++){
	 var curassetName = $("#mpiq_widget_${widgetName}_stableasset_"+m).val();
	 
	 
	 if(mpiq_defaultAssetClassName.indexOf(curassetName)==-1){
	    useDataObject = false;
	  }
	 
	   for(var i=0;i<rTrSize;i++){		   
	     if($("#mpiq_widget_${widgetName}_riskyasset_"+i).val()==curassetName){
	       alert("Two assetClass are same, please remove one of them.");
	       return;
	   }
	   
	 }
	 
	 for(var j=0;j<sTrSize;j++){
	    if(j==m){
	       continue;	     
	       }
	   if($("#mpiq_widget_${widgetName}_stableasset_"+j).val()==curassetName){
	      alert("Two assetClass are same, please remove one of them.");
	     return;
	   }		   
	 }
}
	 var assetClassName ="";
	 var assetClassWeight="";
	 var totalPercent = 0;
	 for(var i=0;i<rTrSize;i++){
	   var rfundNum = $("#mpiq_widget_${widgetName}_riskyfundnum_"+i).val();
	   var rfundName = $("#mpiq_widget_${widgetName}_riskyasset_"+i).val();
	   if(rfundNum == "0" ||rfundNum ==""){		     
	     alert("The AssetClass of "+rfundName+" is not available for this plan, please delete it.");
	     return;
	   }
	   var rpercent = $("#mpiq_widget_${widgetName}_riskypercent_"+i).val();
	   if(rpercent==""){
	      alert("The target allocation of "+rfundName+" has not been filled, please fill it.");
	      return
	   }else{
	      var rpercentNum = parseFloat(rpercent);
	      if(!isNaN(rpercentNum))
	           totalPercent+=rpercentNum;
	   }
	   if(i!=0){
	     assetClassName+=",";
	     assetClassWeight+=",";		     
	   }
	    var rpointpercent = rpercent/100+0.00001;
	    rpointpercent = mpiq_resultNumFixed(rpointpercent);
	    assetClassName+=rfundName; 
	    assetClassWeight+=rpointpercent; 
	 }
	 
	 for(var j=0;j<sTrSize;j++){
	   var sfundNum = $("#mpiq_widget_${widgetName}_stablefundnum_"+j).val();
	   var sfundName = $("#mpiq_widget_${widgetName}_stableasset_"+j).val();
	   if(sfundNum == "0" || sfundNum ==""){		     
	     alert("The AssetClass of "+sfundName+" is not available for this plan, please delete it.");
	     return;
	   }
	   var spercent = $("#mpiq_widget_${widgetName}_stablepercent_"+j).val();
	   if(spercent==""){
	      alert("The target allocation of "+sfundName+" has not been filled, please fill it.");
	      return
	   }else{
	      var spercentNum = parseFloat(spercent);
	      if(!isNaN(spercentNum))
	           totalPercent+=spercentNum;
	   }
	   
	   var spointpercent = spercent/100+0.00001;
	   spointpercent = mpiq_resultNumFixed(spointpercent);
	   assetClassName+=",";
	   assetClassWeight+=",";	
	   assetClassName+=sfundName;
	   assetClassWeight+=spointpercent;
	 }
	 
	 for(var i=0;i<mpiq_riskyFundTrNum;i++){
        var fund = $("#mpiq_widget_${widgetName}_riskyfund_"+i).val();
        if(fund==undefined)
        	continue
        if(mpiq_defaultAssetClassName.indexOf(fund)==-1){
	     		useDataObject = false;
	      }
        var percent = $("#mpiq_widget_${widgetName}_riskyFundPercent_"+i).val();
        percent = parseFloat(percent);
        if(isNaN(percent))percent=0;
        totalPercent+=percent;
        var percentpoint = percent/100+0.00001;
        percentpoint = mpiq_resultNumFixed(percentpoint);
        assetClassName+=",";
	   	 assetClassWeight+=",";	
	     assetClassName+=fund.toUpperCase();
	     assetClassWeight+=percentpoint;	
     	}
     
    	for(var j=0;j<mpiq_stableFundTrNum;j++){
        	var fund = $("#mpiq_widget_${widgetName}_stablefund_"+j).val();
        	if(fund==undefined)
        		continue
        	if(mpiq_defaultAssetClassName.indexOf(fund)==-1){
	     		useDataObject = false;
	     	 }
        	var percent = $("#mpiq_widget_${widgetName}_stableFundPercent_"+j).val();
        	percent = parseFloat(percent);
           if(isNaN(percent))percent=0;
           totalPercent+=percent;
       	var percentpoint = percent/100+0.00001;
       	percentpoint = mpiq_resultNumFixed(percentpoint);
        	assetClassName+=",";
	   	 	assetClassWeight+=",";	
	     	assetClassName+=fund.toUpperCase();
	     	assetClassWeight+=percentpoint;	
     	}
     	
     	if(mpiq_defaultAssetClassName.length!=assetClassName.length){
      				useDataObject = false;
     	}
	 
	 if(totalPercent != 100){
	   alert("The total target allocation is not 100%, please modify and save again.");
	   return;
	 }
	
	if(useDataObject == false){
	 $("#mpiq_widget_${widgetName}_UseDataObject").val("false");
	}else{
		$("#mpiq_widget_${widgetName}_UseDataObject").val("true");
	}
	
	$("#mpiq_widget_${widgetName}_MainAssetClass").val(assetClassName);
	$("#mpiq_widget_${widgetName}_MainAssetClassWeight").val(assetClassWeight);
	$("#mpiq_widget_${widgetName}_BuyThreshold").val($("#mpiq_widget_${widgetName}_t_BuyThreshold").val());
	$("#mpiq_widget_${widgetName}_SellThreshold").val($("#mpiq_widget_${widgetName}_t_SellThreshold").val());
	$("#mpiq_widget_${widgetName}_SpecifyAssetFund").val("true");
	$("#mpiq_widget_${widgetName}_t_riskprofile").val($("#mpiq_widget_${widgetName}_totalstable").val());
	$("#mpiq_widget_${widgetName}_t_riskprofile").attr("readOnly","true");
	$("#mpiq_widget_${widgetName}_t_riskprofile").css('background','none repeat-x scroll 50% 50% #D9D9D9');
		
		
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getAllAssetFund.action?assetClassName="+assetClassName;
	 mpiq_load_data_from_server(url,function(data){
	     $("#mpiq_widget_${widgetName}_AssetFundString").val(data.message);
	     alert("Asset allocation has been specified.");
	 });

 }