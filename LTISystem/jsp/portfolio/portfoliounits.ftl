[#ftl]
[#assign baseUrl="/LTISystem"/]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#setting url_escaping_charset='utf8']

[#macro BtnOriginalPortfolio portfolio]
<input type='button' id='btn_orginal' class=btn style='font-weight: bold;' onclick='window.location.href="/LTISystem/jsp/portfolio/EditHolding.action?operation=editbasic&ID=${portfolio.ID}"' value='Original Portfolio'>
[/#macro]

[#macro BtnCustomizeADVPortfolio portfolio anonymous hasSimulateRole]
<input title="" type='button' id='btn_customize_adv' class=btn style='font-weight: bold;width:250px' value='Customize a New Portfolio'>
<div id="customizeadvdiv" style="margin-top:7px;margin-bottom:7px;display:none">
	[#if anonymous]
		<span style="font-size:1.2em;line-height:20px;font-weight:bold">
		 	You need to register for free to customize SAA portfolios or subscribe as Expert User to customize the advanced portfolios. Please login or register first.
		</span>
		<br>
		<br>
		<a class="uiButton" href="javascript:void(0)"  onclick='$("#loginEntry").trigger("click");' >Login</a>&nbsp;
		<a class="uiButton" href="/LTISystem/jsp/register/openRegister.action" target="_blank" >Register</a>&nbsp;
	[#else]
		[#-- 检查是否有customize adv的权�?--]
		[#if hasSimulateRole]
			
		[#else]
			<span style="font-size:1.2em;line-height:20px;font-weight:bold">
				You need to subscribe as Expert User to customize the advanced portfolios. Please subscribe for two weeks free trial.
			</span>
			<br>
			<br>
			<a class="uiButton" href="/LTISystem/paypal_subscr.action" target="_blank">About Subscription</a>
		[/#if]
	[/#if]
</div>
<script>
$(function(){
		[#if !hasSimulateRole || anonymous]
			$("#btn_customize_adv").toggle(
				function(){
					$("#btn_mess_div").html($("#customizeadvdiv").html());
			   		$("#btn_mess_div").fadeIn();
			   		$("#btn_customize_adv").css({"background-image": "url(images/grey-bg2.gif)"});
				},
				function(){
			    	$("#btn_mess_div").fadeOut();
			    	$("#btn_customize_adv").css({"background-image": "url(images/grey-bg.gif)"});
				}
			);
		[#else]
			$("#btn_customize_adv").click(function(){
				window.location.href="/LTISystem/jsp/portfolio/EditHolding.action?operation=editbasic&ID=${portfolio.ID}&compound=1";
			});
		[/#if]
});

</script>
[/#macro]

[#macro BtnChangeParameter portfolio]
<input title='chang the parameters of this portfolio, such as Risk Profile and Rebalance Frequancy' type='button' id='btn_modify' class=btn style='font-weight: bold;' onclick='window.location.href="/LTISystem/select_modify.action?portfolioID=${portfolio.ID}"' value='Change Parameters'>
[/#macro]


[#macro BtnFollow portfolio username open anonymous hasSimulateRole]
<input title="" type='button' id='btn_follow' class=btn style='font-weight: bold' value='Follow This Portfolio'>
<div id=followdiv style="margin-top:7px;margin-bottom:7px;display:none">
	[#if anonymous]
		<span style="font-size:1.2em;line-height:20px;font-weight:bold">
		[#if plan??]
			You need to register for free to follow SAA portfolios or subscribe to follow the TAA portfolios. Please login or register first.
		[#else]
			You need to subscribe to follow the advanced portfolios. Please login or register first.
		[/#if]
		</span>
		<br>
		<br>
		<a class="uiButton" href="javascript:void(0)"  onclick='$("#loginEntry").trigger("click");' >Login</a>&nbsp;
		<a class="uiButton" href="/LTISystem/jsp/register/openRegister.action" target="_blank" >Register</a>&nbsp;
	[#else]
		[#-- 检查是否有follow的权�?--]
		[#if hasSimulateRole]
			
		[#else]
			<span style="font-size:1.2em;line-height:20px;font-weight:bold">
				[#if plan??]
					You need to subscribe as Basic User to follow the TAA portfolios. Please subscribe for one month free trial.
				[#else]
					You need to subscribe as Expert User or Professional User to follow the advanced portfolios. Please subscribe for two weeks trial.
				[/#if]
			</span>
			<br>
			<br>
			<a class="uiButton" href="/LTISystem/paypal_subscr.action" target="_blank">About Subscription</a>
		[/#if]
	[/#if]
</div>
<script>
$(function(){
		[#if !hasSimulateRole || anonymous]
			$("#btn_follow").toggle(
				function(){
					$("#btn_mess_div").html($("#followdiv").html());
			   		$("#btn_mess_div").fadeIn();
			   		$("#btn_follow").css({"background-image": "url(images/grey-bg2.gif)"});
				},
				function(){
			    	$("#btn_mess_div").fadeOut();
			    	$("#btn_follow").css({"background-image": "url(images/grey-bg.gif)"});
				}
			);
		[#else]
			var ajaxurl = "/LTISystem/getstarted_followportfolio.action?includeHeader=false"
			var datastr = "portfolioID=${portfolio.ID?string.computer}";
			[#if plan??]
				datastr += "&planID=${plan.ID?string.computer}";
			[#else]
				ajaxurl = "/LTISystem/getstarted_followadvanceportfolio.action?includeHeader=false"
			[/#if]
			$("#btn_follow").click(function(){
				$.ajax({
						url: ajaxurl,
						type: 'POST',
						data: datastr,
						error: function(message){
							alert($.trim(message.responseText));
							return;
						},
						success: function(result){
							alert(result);
							location.reload();
						}
				});//end ajax
			});
		[/#if]
});

</script>
[/#macro]


[#macro BtnCustomize portfolio anonymous hasSimulateRole]
<input title="" type='button' id='btn_customize' class=btn style='font-weight: bold;width:200px' value='Customize a New Portfolio'>
<script>
$(function(){
			$("#btn_customize").click(function(){
				window.location.href="/LTISystem/select_entry.action?portfolioID=${portfolio.ID}";
			});
});

</script>
[/#macro]






[#macro BtnUnfollow portfolio isOwner]
<input type='button' id='btn_un_follow'  onclick="unfollow()" class=btn style='font-weight: bold;' value='Cancel Following'>
<script>
	function unfollow(){
		var url_s='/LTISystem/getstarted_unfollow.action?includeHeader=false&includeJS=false&portfolioID=${portfolio.ID}';
		[#if isOwner]
			if(!confirm('The customized portfolio will be deleted in the same time when you cancel following it. Are you sure to cancel following? ')){
				return;
			}
		[/#if]
		$.ajax({   
	           type:"POST",   
	           url:url_s,
	           success: function(mesg){
	          		alert($.trim(mesg));
	          		[#if isOwner]
	          			window.location.href = "/LTISystem/f401k__portfolios.action";
	          		[#else]//非用户自己的portfolio,刷新看到delay
	          			window.location.reload();
	          		[/#if]	 
	           },
	           error:function(){
	           		alert("Please try it again.");	
	           }
	    });  
	}
</script>
[/#macro]






[#macro PortfolioParametersUnit portfolio plan]
			<div class="sidebar_box_noPadding roundHeadingBlue" >
					<div class="sidebar_box_heading_white">Portfolio Parameters</div>
					<div class="sidebar_box_content">
						<table border=0 width=100%>
							<tr>
								<td><strong>Plan</strong></td>
								<td><a href='/LTISystem/f401k_view.action?ID=${plan.ID?string.computer}' target="_blank">${plan.name}</a></td>
							</tr>
							<tr>
								<td><strong>Strategy</strong></td>
								<td><a href='/LTISystem/jsp/strategy/View.action?ID=${portfolio.strategies.assetAllocationStrategy.ID}' target="_blank">${portfolio.strategies.assetAllocationStrategy.name}</a></td>
							</tr>
							<tr>
								<td width=150px><strong>Risk Profile</strong></td>
								<td width=200px>
									${portfolio.strategies.assetAllocationStrategy.parameter["RiskProfile"]}
								</td>
								
							</tr>
							<tr>
								<td width=150px><strong>Re-Balance Frequency</strong></td>
								<td width=200px>
									[#assign f="" /]
									[#if portfolio.strategies?? && portfolio.strategies.assetAllocationStrategy?? && portfolio.strategies.assetAllocationStrategy.parameter??]
										[#if portfolio.strategies.assetAllocationStrategy.parameter["Frequency"]??]
											[#assign f=portfolio.strategies.assetAllocationStrategy.parameter["Frequency"]?lower_case  /]
										[/#if]
										[#if f==""]
											[#if portfolio.strategies.assetAllocationStrategy.parameter["RebalanceFrequency"]??]
												[#assign f=portfolio.strategies.assetAllocationStrategy.parameter["RebalanceFrequency"]?lower_case  /]
											[/#if]
										[/#if]
										[#if f==""]
											[#if portfolio.strategies.assetAllocationStrategy.parameter["CheckFrequency"]??]
												[#assign f=portfolio.strategies.assetAllocationStrategy.parameter["CheckFrequency"]?lower_case  /]
											[/#if]
										[/#if]
									[/#if]
									
									
									[#if f=="monthly"]
										Month End 
									[#elseif f=="quarterly"]
										Quarter End
									[#elseif f=="yearly"]
										Year End
									[#else]
										${f}
									[/#if]
								</td>
							</tr>
						</table>
					</div>
				</div>
[/#macro]

[#macro StrategiesUnit portfolio]
				<div class="sidebar_box_noPadding roundHeadingBlue" >
					<div class="sidebar_box_heading_white">Strategies</div>
					<div class="sidebar_box_content">
						<table border=0 >
							[#if portfolio.strategies??]
							[#if portfolio.strategies.assetAllocationStrategy.ID?? && portfolio.strategies.assetAllocationStrategy.ID!=0]
							<tr>
								<td>
								Asset Allocation Strategy&nbsp;&nbsp;&nbsp; 
								</td>
								<td>
								<a href='/LTISystem/jsp/strategy/View.action?ID=${portfolio.strategies.assetAllocationStrategy.ID}'>${portfolio.strategies.assetAllocationStrategy.name!""}</a>
								</td>
							</tr>
							[/#if]
							[#if portfolio.strategies.cashFlowStrategy.ID?? && portfolio.strategies.cashFlowStrategy.ID!=0]
							<tr>
								<td>
								Cash Flow Strategy&nbsp;&nbsp;&nbsp; 
								</td>
								<td>
								<a href='/LTISystem/jsp/strategy/View.action?ID=${portfolio.strategies.cashFlowStrategy.ID}'>${portfolio.strategies.cashFlowStrategy.name!""}</a>
								</td>
							</tr>
							[/#if]
							[#if portfolio.strategies.rebalancingStrategy.ID?? && portfolio.strategies.rebalancingStrategy.ID!=0]
							<tr>
								<td>
								Rebalancing Strategy&nbsp;&nbsp;&nbsp;
								</td>
								<td>
								<a href='/LTISystem/jsp/strategy/View.action?ID=${portfolio.strategies.rebalancingStrategy.ID}'>${portfolio.strategies.rebalancingStrategy.name!""}</a>
								</td>
							</tr>	
							[/#if]
							[#if portfolio.strategies.assetStrategies??]
							[#list portfolio.strategies.assetStrategies as assetstrategy]
							[#if assetstrategy.ID?? && assetstrategy.ID!=0]
							<tr>
								<td>
								Asset Strategy&nbsp;&nbsp;&nbsp;
								</td>
								<td>
								<a href='/LTISystem/jsp/strategy/View.action?ID=${assetstrategy.ID}'>${assetstrategy.name!"STATIC"}</a>
								</td>
							</tr>	
							[/#if]
							[/#list]
							[/#if]
							[/#if]
						</table>
					</div>
				</div>
[/#macro]

[#macro HoldingsUnit portfolio aggregateFlag endDate realtime anonymous]

[#assign trSize =1 ]
[#if holdingInfs??]
  [#assign trSize=holdingInfs?size?string.computer]
[/#if]
<script src="/LTISystem/jsp/template/gpl/jsuggest.js"></script>
		<script>
		var show_more_holding = false;
		function showmore() {
			if(show_more_holding == false){
				show_more_holding = true;
				$('#show_holding_div').fadeIn();
			}else{
				show_more_holding = false;
				$('#show_holding_div').fadeOut();
			}
		}
		
		function transation(){
		var assetNames;
		var securityNames;
		var Amounts;
		var minSells;
		var minBuys;
		var availableToSell;
		 $("#transation_dialog").dialog({
		     autoOpen: false,
		     resizable: true,
			 height:560,
			 width: 860,
			 modal: true,
			 buttons: {
			 Cancel: function() {
					$(this).dialog('close');
					},
			 Back:function(){
			           $("#tableHolding").show();
			           $("#result").hide();
			 },
			 Submit:function(){
			         assetNames="";
		             securityNames="";
		 			 Amounts="";
		 			 minSells ="";
		 			 minBuys="";
		 			 availableToSell="";
			       var asset = document.getElementsByName("assetName");
			       var hasAsset = true;
			       for(var i=0;i<asset.length;i++){
			         if(i!=0)assetNames+=",";
			         assetNames+=asset[i].value;
			         if(asset[i].value=="Select an Asset Class"){
			             hasAsset = false;
			             break;
			         }
			       }
			       //alert(hasAsset);
			       //alert(assetNames);
			       if(hasAsset == false){
			         alert("Some tickers are not recognized. Please assign asset classes to them for transaction estimation.");
			       }else{
			          var security = document.getElementsByName("securityName");
			       for(var j=0;j<security.length;j++){
			         if(j!=0)securityNames+=",";			         
			         securityNames +=security[j].value;
			       }
			      // alert(securityNames);
			       
			       var percent = document.getElementsByName("amount");
			       for(var k =0;k<percent.length;k++){
			        if(k!=0)Amounts +=",";
			        Amounts +=percent[k].value;
			       }
			      //alert(Amounts);
			      
			      var avaToSell = document.getElementsByName("AvailableToSell");
			       for(var k =0;k<avaToSell.length;k++){
			        if(k!=0)availableToSell +=",";
			        availableToSell +=avaToSell[k].value;
			       }
			     // alert(availableToSell);
			      
			      
			      var minSell = document.getElementById("minSell");
			       minSells = minSell.value;
			     // alert(minSells);
			      
			      var minBuy = document.getElementById("minBuy");
			       minBuys = minBuy.value;
			      //alert(minBuys);
			      
			      
			      $.ajax({
			         type:"post",
			         url:"/LTISystem/jsp/portfolio/ViewPortfolio_transation.action?includeHeader=false&ID=${portfolio.ID?string.computer}&assetNames="+assetNames+"&securityNames="+securityNames+"&amount="+Amounts+"&avaliableToSell="+availableToSell+"&minSell="+minSells+"&minBuy="+minBuys,
			         success:function(message){
			           //alert(message);
			           var divItem = document.getElementById("result");
			           var results="";
			           var transationTable = "<h2>Transactions</h2><table cellspacing='10'><tr><td width='300'>Transaction</td><td width='200'>Fund</td><td width='100'>Amount</td><td width='100'>Percentage</td></tr>";
			           var holdingTable = "<h2>Holdings After Transaction</h2><table cellspacing='10'><tr><td width='300'>Asset</td><td width='200'>Fund</td><td width='100'>Amount</td><td width='100'>Percentage</td></tr>";
			           var resultItems = message.split("@");
			           //alert(resultItems[0]);
			          // alert(resultItems[1]);
			           if(resultItems[0].length==6){
			             transationTable = "<h2>Transactions</h2><br><p><b>There is no need for rebalancing. Your holdings are the same as the target holdings.</b></p>";
			           }else{
			               var transationItems = resultItems[0].split("$");			          
			           for(var m=0;m <transationItems.length;m++){
			              //alert(transationItems[m]);
			              transationTable = transationTable+"<tr>";
			              var tdItems = transationItems[m].split("#");
			              for(var i=0;i<tdItems.length;i++){
			              // alert(tdItems[i]);
			               transationTable = transationTable+"<td>";
			               transationTable = transationTable+tdItems[i];
			               transationTable = transationTable+"</td>";
			              }
			              transationTable = transationTable+"</tr>";
			           }
			           transationTable = transationTable+"</table>";
			           }
			          
			           //alert(transationTable);
			           
			           var holdingItems = resultItems[1].split("$");
			           for(var j=0; j<holdingItems.length;j++){
			              //alert(holdingItems[j]);
			              holdingTable = holdingTable+"<tr>";
			              var htdItems = holdingItems[j].split("#");
			              for(var k=0;k<htdItems.length;k++){
			               //alert(htdItems[k]);
			               holdingTable = holdingTable+"<td>";
			               holdingTable = holdingTable+htdItems[k];
			               holdingTable = holdingTable+"</td>";
			              }
			              holdingTable = holdingTable+"</tr>";
			           }
			           holdingTable = holdingTable+"</table>";
			           //alert(holdingTable);
			           results = transationTable+holdingTable;
			           divItem.innerHTML = results;
			           $("#tableHolding").hide();
			           $("#result").show();
			         },
			         error:function(message){
			            alert("error");
			         }
			      });
			    }//end else
			       			      			      
			 }//end submit
		  }       
		 });
		 $("#transation_dialog").dialog("open");
		}
		
		var showFlag = false;
		var cacheDetail = "";
		function detail(){
			if(showFlag==false){
				showFlag = true;
				$('#holdingPanel2').fadeOut();
				if(cacheDetail==""){
					$('#showDetailID').html("Please Wait!");
					$.ajax({type: "post",  
			        	 url:'../clonecenter/detailHolding.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&holdingDate='+$('#detailDateID').val(),  
			    	dataType: "html",
			     	success: function(result){
			     			cacheDetail = result;
			         		$('#showDetailID').html(result);
			        	}
			    	});
			    }else if(cacheDetail!=""){
			    	$('#showDetailID').html(cacheDetail);
			    }
				$('#showDetailID').fadeIn();
			}else if(showFlag == true){
				showFlag = false;
				$('#showDetailID').fadeOut();
				$('#holdingPanel2').fadeIn();
			}
			
		}	
		
		var trSize=${trSize};
		function addMoreAsset(){
		 trSize++;
		 var $pane = $("#currentHolding");
		 var $tr,$td;
		 $tr = $(document.createElement("tr"));
		 $tr.attr({id:'tr_'+trSize});
		 $pane.append($tr);
		 $td = $(document.createElement("td"));
		 $td.attr({width:100});
		 $td.html("<div id='assetTd_"+trSize+"'> <input type='text' name='assetName' id='asset_"+trSize+"' readOnly='true'></div>")
		 $tr.append($td);
		 $td = $(document.createElement("td"));
		 $td.attr({width:100});
		 $td.html("<input type='text' name='securityName' id='security_"+trSize+"' onblur='getAsset("+trSize+")'>")
		 $tr.append($td);
		 $td = $(document.createElement("td"));
		 $td.attr({width:100});
		 $td.html("<input type='text' name='amount' id='amount_"+trSize+"' onblur='copy("+trSize+")'>")
		 $tr.append($td);
		 $td = $(document.createElement("td"));
		 $td.attr({width:100});
		 $td.html("<input type='text' name='AvailableToSell' id='ava_"+trSize+"'>")
		 $tr.append($td);
		 $td = $(document.createElement("td"));
		 $td.attr({width:100});
		 $td.html("<input type='button' id='btn"+trSize+"' value='Delete' onclick='removeTr(" + trSize + ")'>")
		 //alert(trSize);
		 $tr.append($td);
		 $("#btn"+trSize).button();
		 $('#security_'+trSize).jsuggest({type:"security"});
		 
		}
		
	$(function() {
	      for(var i=1;i<=trSize;i++)
    		$('#security_'+i).jsuggest({type:"security"});
    });
		
		function removeTr(index){
        // alert(index);
       //  alert($("#tr_"+index).text());
         $("#tr_"+index).remove();		
		}
		
		function clearAll(){
		  //alert(trSize);
		  for(var i=1;i<=trSize;i++){
		    $("#tr_"+i).remove();
		    
		  }
		  addMoreAsset();
		}
						
		function copy(size){
		  var value = $("#amount_"+size).val();
		  $("#ava_"+size).val(value);
		}
		
		function getAsset(size){
		  var funds = $("#security_"+size).val();
		  $.ajax({
		   type:"post",
		   url:"/LTISystem/jsp/portfolio/ViewPortfolio_getFundAssetClass.action?includeHeader=false&fund="+funds,
		   success:function(message){
		     //alert(message.length);
		     if(message.length!=4){
		      $("#assetTd_"+size).html("<input type='text' name='assetName' id='asset_"+size+"' readOnly='true'>");
		       $("#asset_"+size).val(message.trim());
		     }else {
		      $("#assetTd_"+size).html("<select name='assetName'><option selected='selected'>Select an Asset Class</option><option>US EQUITY</option> <option>INTERNATIONAL EQUITY</option><option>FIXED INCOME</option><option>REAL ESTATE</option><option>COMMODITIES</option><option>Emerging Market</option><option>INTERNATIONAL BONDS</option><option>High Yield Bond</option><option>CASH</option><option>BALANCE FUND</option></select>");
		     }
		   },
		   error:function(message){
		    alert(message);
		   }
		  
		  });
		}
		
		var advance = false;
		function showAdvance(){
		  if(advance == false){
		     $("#btn_advance").val("- Advance Option");
		     $("#advance_option").show();
		     advance = true;
		  }else{
		     $("#btn_advance").val("+ Advance Option");
		     $("#advance_option").hide();
		     advance = false;
		  }
		}
		
		
		</script>
		<div class="sidebar_box_noPadding roundHeadingBlue" >
				<div class="sidebar_box_heading_white">Holdings on [#if endDate??]${endDate?string("MM/dd/yyyy")}[/#if]</div>
				<div class="sidebar_box_content">
				
					[#if !realtime]

						<div style="text-align:left">
						[#if anonymous]
							<span style="font-size:1.2em;line-height:20px;font-weight:bold">For an anonymous user, the portfolio information is delayed 30 days or more.</span>
						[#else]
							<span style="font-size:1.2em;line-height:20px;font-weight:bold">The portfolio information is delayed 30 days or more before you follow this portfolio.</span>
						[/#if]
						</div>
					[/#if]
					<div><!--${endDate?string("yyyy-MM-dd")}-->
					[#if realtime]
					<img src='/LTISystem/jsp/ajax/DownloadImage.action?ID=${portfolio.ID?string.computer}&isImageCache=true&imageType=2' border=0>
					[#else]
					<img src='/LTISystem/jsp/ajax/DownloadImage.action?ID=${portfolio.ID?string.computer}&isImageCache=true&imageType=3' border=0>
					[/#if]
					</div>
					<a id="btn_more_funding" style='margin:10px;' onclick="showmore()" class="uiButton">More Descriptions on the Holdings</a>
					
					<div id="show_holding_div" style='display:none;margin:10px;'>
						<input type="hidden" name="detailDate" id="detailDateID" value="${endDate?string("MM/dd/yyyy")}">
						<div id="holdingPanel2" style="width:100%">
							[@s.action name="ViewHolding" namespace="/jsp/clonecenter" executeResult=true]
								[@s.param name='includeHeader']false[/@s.param]
								[@s.param name='includeJS']false[/@s.param]
								[@s.param name='ID']${portfolio.ID?string.computer}[/@s.param]
								[@s.param name='holdingDate']${endDate?string("MM/dd/yyyy")}}[/@s.param]
							[/@s.action]
						</div>
						<div id="showDetailID">
							
						</div>
						<br>
					
					[@authz.authorize ifAnyGranted="MPIQ_E,MPIQ_P,ROLE_SUPERVISOR"]
					[#if !aggregateFlag]
					<input type="button" class="uiButton" style="margin-bottom:1px;float:left"  value="Transaction Estimator" onclick="transation()"><div style="color: red;font-weight: bold">Beta</div>
					[/#if]
					[#if aggregateFlag]
					<input type="button" class="uiButton" style="margin-bottom:1px"  value="Show Detail" onclick="detail()">
					[/#if]
					[/@authz.authorize]
					</div>
					<br>
				</div>
			</div>
	</div>
	<div id="dialog" height="250px" style="display:none">
	  <div id="transation_dialog" title="Transaction" align='left' style="text-align: left">	   
	   <div id ="tableHolding">
	   
	   <p>
		<b>"Transaction Estimator" helps you determine the trasactions needed to convert your actual holdings to those of the current model portfolio. 
		   Please input your actual holdings (tickers) and the amount available to sell immediately. Then click "Submit" to get the result.<br>
		   The Current holdings are listed below. You can modify to your actual holdings, or clear them and input by yourself.</b>
	   </p>
	   <p>	   
	   <table id="currentHolding" width="500">
	      <thead>
	        <tr>
	          <th width="100">Asset Class</th>
	          <th width="100">Ticker</th>
	          <th width="100">Amount($)</th>
	          <th width="100">AvailableToSell($)</th>
	          <th width="100"></th>
	        </tr>
	      </thead>
	      <tbody>
	      [#if holdingInfs??]
	         [#list holdingInfs as items]
	           <tr id="tr_${items_index+1}">	               
	                <td width="100" id="assetTd_${items_index+1}"><input type="text" name="assetName" id="asset_${items_index+1}"  readOnly="true" value='${items.get(0)}'></td>	               
	                <td width="100"><input type="text" name="securityName" id="security_${items_index+1}" value="${items.get(1)}" onblur="getAsset(${items_index+1})"></td>
	                <td width="100"><input type="text" name="amount" id="amount_${items_index+1}" value="${items.get(2)}" onblur="copy(${items_index+1})"></td>
	                <td width="100"><input type="text" name="AvailableToSell" id="ava_${items_index+1}" value="${items.get(3)}"></td>
	                <td width="100"><input type="button" class="uiButton" value='Delete' onclick="removeTr(${items_index+1})"></td>

	           </tr>
	         [/#list]	       
          [#else]
	       <tr id="tr_1">
	       <td width="100" id="assetTd_1"><input type="text" name="assetName" id="asset_1"  readOnly="true"></td>
	       <td width="100"><input type="text" name="securityName" id="security_1" onblur="getAsset(1)"></td>
	       <td width="100"><input type="text" name="amount" id="amount_1" onblur="copy(1)"></td>
	       <td width="100"><input type="text" name="AvailableToSell" id="ava_1"></td>
	       <td width="100"><input type="button" class="uiButton" value='Delete' onclick="removeTr(1)"></td>
	       </tr>
	     [/#if]
	      </tbody>
	   </table>
	   <input type="button" value="Add" onclick="addMoreAsset()" class="uiButton" >
	   <input type="button" value="Clear" class="uiButton" onclick="clearAll()">
	   </p>
	   <br><br>
	   	   <input type="button" id="btn_advance" value="+ Advance Option" onclick="showAdvance()" class="uiButton">
	   <div id="advance_option" style="display:none">
	   <br>
	   <p><b>We will not suggest transactions which amounts are below the trading thresholds.</b></p>
	       <p>Selling Threshold($)&nbsp;<input type="text" id="minSell" style="width:100px" value="0">&nbsp;&nbsp;&nbsp;  Buying Threshold($)&nbsp;<input type="text" id="minBuy" style="width:100px" value="0">
	   <!--&nbsp;&nbsp;&nbsp;Avalilable To Sell&nbsp;<input type="text" id="avaToSell" style="width:100px">
	      <br>Mirror Assets<input type="checkbox" id="mirAssets">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mirror Funds<input type="checkbox" id="mirFunds">-->
	   </p>
	   </div>
	 </div>
	   <div id="result" style="display:none">
	   </div>
	     </div>
	   </div>
	</div>
	<p> <a title="Instructions on How to Use CSV on FolioInvesting.com" href="/LTISystem/UserFiles/FolioHowto.pdf">Instructions On FolioInvesting.com</a></p>
	<p><a title="Download the holdings whose the date you selected into CSV file for Folio." href="javascript:void(0)" onclick="getCSV('folio')">Download Current Holdings to FolioInvesting CSV file</a></p>
[/#macro]


[#macro ScheduleHoldingsUnit portfolio realtime]
		[#if realtime]
		<div class="sidebar_box_noPadding roundHeadingBlue" id="e_st">
				<div class="sidebar_box_heading_white">Expected Holdings and Transactions</div>
				<div class="sidebar_box_content">
				<a title="Download the scheduled holdings into CSV file." href="/LTISystem/jsp/portfolio/OutputSchHolding.action?ID=${portfolio.ID}&folio=false">Download to CSV</a>&nbsp;
	            <a title="Download the scheduled holdings into CSV file for Folio." href="/LTISystem/jsp/portfolio/OutputSchHolding.action?ID=${portfolio.ID}&folio=true">Download to Folio CSV</a>&nbsp;
               
						<h3>Expected holdings</h3>
	                      <div id="scheduled_h">
	                      </div>
	                      <h3>Scheduled transactions</h3>
	                      <div id="scheduled_t">
	                      </div>
				</div>
			</div>
			<br>
		<script>
		$(function(){
			$.ajax({   
	           type:"POST",   
	           url:'/LTISystem/jsp/clonecenter/ViewTransaction.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&schedule=true',
	           success: function(mesg){
	           		if($.trim(mesg)==""){
	           			$('#e_st').hide();
	           			return;
	           		}
	           	
	         		$("#scheduled_info").show();
	         		$("#scheduled_t").html(mesg);
	         		$("#scheduled_h").load('/LTISystem/jsp/clonecenter/ViewHolding.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&schedule=true');
	         	},
	         	error:function(mesg){
	         	$('#e_st').hide();
	         	}
	      });  
		});
		</script>
		[/#if]
[/#macro]


[#macro PerformanceInfoUnit portfolio endDate]
		<div class="sidebar_box_noPadding roundHeadingBlue" >
				<div class="sidebar_box_heading_white">Performance Info [#if endDate??](As of ${endDate?string("MM/dd/yyyy")})[#else](As of ${portfolio.endDate?string("MM/dd/yyyy")})[/#if]</div>
				<div class="sidebar_box_content">
					[@s.action name="profile_getperformance" namespace="/" executeResult=true]
						[@s.param name='includeHeader']false[/@s.param]
						[@s.param name='portfolioID']${portfolio.ID?string.computer}[/@s.param]
					[/@s.action]
				</div>
			</div>
[/#macro]

[#macro HistoricalChartUnit portfolio realtime endDate]
		<div class="sidebar_box_noPadding roundHeadingBlue" >
				<div class="sidebar_box_heading_white">Historical Portfolio Chart [#if endDate??](As of ${endDate?string("MM/dd/yyyy")})[#else](As of ${portfolio.endDate?string("MM/dd/yyyy")})[/#if]</div>
				<div class="sidebar_box_content">
					[#if realtime]
					<img width=680 src='/LTISystem/article_viewchart.action?portfolioID=${portfolio.ID?string.computer}&securityID=1144,91&width=680&height=500&Type=0' border=0>
					[#else]
					<img width=680  src='/LTISystem/article_viewchart.action?portfolioID=${portfolio.ID?string.computer}&securityID=1144,91&width=680&height=500&Type=1' border=0>
					[/#if]
					<br>
					<br>
					<a href="FullChart.action?ID=${portfolio.ID}&name=${portfolio.name}&includeHeader=false" target="_blank" class="uiButton">Flash Chart</a>
				</div>
			</div>
			
			
[/#macro]


[#macro PerformanceAnalyticsUnit portfolio endDate]
<script>
		var show_more_per=false;
			function morePer(){
				if(show_more_per==false){
					show_more_per=true;
					$('#btn_more_per').val('- Performance Analytics');
					$('#more_per_div').show();
				}else{
					show_more_per=false;
					$('#btn_more_per').val('+ Performance Analytics');
					$('#more_per_div').hide();
				}
			}
		</script>
			<div>
			<input type='button' id='btn_more_per' class=btn onclick='javascript:morePer()' style='width:270px;text-align:left' value='+ Performance Analytics'>
			</div>
			<div class="sidebar_box_noPadding roundHeadingBlue" id='more_per_div' style='display:none'>
				<div class="sidebar_box_heading_white">Performance Analytics [#if endDate??](As of ${endDate?string("MM/dd/yyyy")})[#else](As of ${portfolio.endDate?string("MM/dd/yyyy")})[/#if]</div>
				<div class="sidebar_box_content" id="mptdiv">
				</div>
			</div>
			
			<script>
			$(function(){
				$.ajax({   
		           type:"POST",   
		           url:'../portfolio/MPTMain.action?includeHeader=false&includeJS=false&portfolioID=${portfolio.ID}&basicunit=true',
		           success: function(mesg){
		           		$("#mptdiv").html(mesg);
		         	},
		         	error:function(mesg){
		         	}
		      });  
			});
			</script>
		
[/#macro]

[#macro TaxPerformanceAnalyticsUnit portfolio advanceUser admin]
		<script>
		var show_tax_per=false;
			function moreTaxPer(){
				if(show_tax_per==false){
					show_tax_per=true;
					$('#btn_tax_per').val('- Performance Estimate');
					$('#more_tax_div').show();
				}else{
					show_tax_per=false;
					$('#btn_tax_per').val('+ Performance Estimate');
					$('#more_tax_div').hide();
				}
			}
		</script>
			[#if advanceUser || admin]
			<div>
			<input type='button' id='btn_tax_per' class=btn onclick='javascript:moreTaxPer()' style='float:left;width:270px;text-align:left' value='+ Performance Estimate'><div style="color: red;font-weight: bold">Beta</div>
			<div style="clear:both"></div>
			</div>
			<div class="sidebar_box_noPadding roundHeadingBlue" id='more_tax_div' style='display:none'>
				<div class="sidebar_box_heading_white">Tax Performance Analytics</div>
				<div>
					<form action="/jsp/portfolioTaxMPTMAIN" method="post" id="taxmptform">
						<table>
							<tr>
								<td>
									Commission Fee ($ per transaction) 
								</td>
								<td>
									<input type="text" name="commissionAmountEachTransaction" id="commissionAmountEachTransaction" value="${commissionAmountEachTransaction!7.0}">
								</td>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									Long Term Tax Rate (%)
								</td>
								<td>
									<input type="text" name="taxRateOnLongReturn" id="taxRateOnLongReturn" value="${taxRateOnLongReturn!10}">
								</td>
							</tr>
							<tr>
								<td>
									Short Term Tax Rate (%)
								</td>
								<td>
									 <input type="text" name="taxRateOnShortReturn" id="taxRateOnShortReturn" value="${taxRateOnShortReturn!30}">
								</td>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									Monthly Withdraw ($) 
								</td>
								<td>
									<input type="text" name="withDrawAmount" id="withDrawAmount" value="${withDrawAmount!100.0}">
								</td>
							</tr>
							<tr>
								<td>
									Monthly Deposit ($)
								</td>
								<td>
									 <input type="text" name="depositAmount" id="depositAmount" value="${depositAmount!0.0}">
								</td>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									Tax on Withdraw (%) 
								</td>
								<td>
									<input type="text" name="taxOnWithdraw" id="taxOnWithdraw" value="${taxOnWithdraw!0}">
								</td>
							</tr>
							<tr>
								<td>
									Initial Amount ($)
								</td>
								<td>
									 <input type="text" name="initialAmount" id="initialAmount" value="${initialAmount!10000}">
								</td>
							</tr>
						</table>
					</form>
					<a href="javascript:void(0)" onclick="taxmpt()" class="uiButton">Calculate</a>
				</div>
				<div class="sidebar_box_content" id="taxmptdiv">
				</div>
			</div>
			<script>
			function taxmpt(){
				alert("It will take a moment to estimate the performance data.");
				$.ajax({   
		           type:"POST",   
		           url:'../portfolio/TaxMPTMain.action?includeHeader=false&includeJS=false&portfolioID=${portfolio.ID}',
		           data: $('taxmptform').serialize(),
		           success: function(mesg){
		           		$("#taxmptdiv").html(mesg);
		         	},
		         	error:function(mesg){
		         	}
		      });  
			}
			</script>
			[/#if]
[/#macro]

[#macro EMailAlertButtonUnit portfolio plan realtime emailNotification]
[#if realtime==true]
<script type="text/javascript">
$(function() {
	$("#emailalert").click(function(){
		var $alertbutton = $(this);
		var notification = $("#notification").val();
		
		if(notification.indexOf(false) != -1){
			$.ajax({
				type: "get",
				url: "../ajax/Email.action",
				data: "portfolioID=${portfolio.ID?string.computer}",
				datatype: "html",
				success: function(result){
					if(result.indexOf("fail") == -1){
						$alertbutton.val("Remove Eamil Alert");
						$("#notification").val(true);
					}
				}
			})
		}
		else{
			$.ajax({
				type:	"get",
				url:	"../ajax/EmailRemove.action",
				data: "portfolioID=${portfolio.ID?string.computer}",
				datatype: "html",
				success: function(result){
					if(result.indexOf("fail") == -1){
						$alertbutton.val("Transaction Email Alert");
						$("#notification").val(false);
					}
				}
			})
		}
	});
		
});
</script>

	[@s.hidden id="notification" name="emailNotification"][/@s.hidden]

      [#if realtime && !plan??]
	    [#if emailNotification?exists && emailNotification == true]
		   <input title='stop recieving email alerts of this portfolio' id="emailalert" class=btn style='width:280px;font-weight: bold;' type="button" value="Remove Email Alert"/>
	    [#else]
		  <input title='Email alerts will be sent to your email address when there are scheduled transactions of this portfolio.' id="emailalert" class=btn style='width:280px;font-weight: bold;' type="button" value="Transaction Email Alert" />
	    [/#if]
	   [/#if]

		[/#if]
[/#macro]


[#macro AdvancedTabUnit portfolio isOwner read roleDelayed realtime roleRealtime operation admin]
<div>
<input type='button' id='btn_advanced' class=btn onclick='javascript:advanced()' style='width:270px;text-align:left' value='+ Advanced Options'>
</div>
		<div class="sidebar_box_noPadding roundHeadingBlue" style='display:none' id="advanced">
				<div class="sidebar_box_heading_white">Advanced Info</div>
				<div class="sidebar_box_content">
									[#if isOwner]
										<table border=0 cellspacing="1" bgcolor="#000000" width='100%'>
										<tr bgcolor="#ffffff">
									    	<td align="center" style="color:#CFCFCF">Groups Permission</td>
											<td align="left" width="15%"> delayed portfolio</td>
										 	[#if read==true||isOwner==true]
											<td align="left" width="15%">
												${roleDelayed}&nbsp;
											</td>
											[#else]
											<td align="left" width="15%">&nbsp;</td>
											[/#if]
											<td align="left" width="15%"> realtime portfolio</td>
											[#if realtime==true||isOwner==true]
											<td align="left">
											${roleRealtime}&nbsp;
											</td>
											[#else]
											<td align="left">&nbsp;</td>
											[/#if]
											<td align="left" width="15%">save operation</td>
											[#if operation==true||isOwner==true]
											<td align="left">
											${roleOperation}&nbsp;
											</td>
											[#else]
											<td align="left">&nbsp;</td>
											[/#if]
										</tr bgcolor="#ffffff">
										</table>
										[/#if]

										<br>
										<div width='100%' align="left">
											<!--
											<a href="/LTISystem/jsp/portfolio/EditHolding.action?operation=editbasic&ID=${portfolio.originalPortfolioID!portfolio.ID}"  target="_blank">Original Portfolio</a>&nbsp;&nbsp;&nbsp;&nbsp;-->
											<a href="/LTISystem/jsp/portfolio/DailyDataMain.action?portfolioID=${ID}"  target="_blank">Daily Data</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<!--<a href="/LTISystem/jsp/portfolio/confidenceLevelCheck.action?portfolioID=${ID}"  target="_blank">Confidence Check Result</a>&nbsp;&nbsp;&nbsp;&nbsp;-->
											<a href="/LTISystem/jsp/portfolio/TransactionMain.action?portfolioID=${ID}"  target="_blank">All Transactions</a>&nbsp;&nbsp;&nbsp;&nbsp;
											[#if admin]
											<a href="/LTISystem/jsp/portfolio/LogMain.action?portfolioID=${ID}" target="_blank">Logs</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<a href="/LTISystem/jsp/portfolio/AccountUserByPortfolioID.action?portfolioID=${ID}" target="_blank">FollowPortfolios</a>&nbsp;&nbsp;&nbsp;&nbsp;
											[/#if]
											[#if realtime]
												<a href="/LTISystem/jsp/portfolio/OutputPdf.action?ID=${ID}" target="_blank">Output PDF</a>&nbsp;&nbsp;&nbsp;&nbsp;
											[/#if]
										</div>
										<br>
										<div align="left" width='100%'>
														<input type='button' class=btn onclick='javascript:showAlpha()' style='text-align:left' value='+ Show Alpha Chart'>
														<input type='button' class=btn onclick='javascript:showBeta()' style='text-align:left' value='+ Show Beta Chart'>
														<input type='button' class=btn onclick='javascript:showSharpe()' style='text-align:left' value='+ Show Sharpe Chart'>
														<input type='button' class=btn onclick='javascript:HideChart()' style='text-align:left' value='- Hide'>
														<div id="flash">
														</div>
														<script type="text/javascript">
															var port=document.location.port;
															if(port=="")port="80";
															var address=document.location.hostname;
															function showAlpha(){
																$('#flash').load('./mpt/flashAlpha.jsp?address='+address+'&port='+port+'&portfolioID=${ID?string.computer}');
																$('#hideLink').show();
															}
															function showBeta(){
																$('#flash').load('./mpt/flashBeta.jsp?address='+address+'&port='+port+'&portfolioID=${ID?string.computer}');
																$('#hideLink').show();
															}
															function showSharpe(){
																$('#flash').load('./mpt/flashSharpe.jsp?address='+address+'&port='+port+'&portfolioID=${ID?string.computer}');
																$('#hideLink').show();
															}
															function HideChart(){
																$('#flash').html("");
																$('#hideLink').hide();
															}
															$('#hideLink').hide();
															
														</script>
										</div>
			</div>
<script>
var adv_hide=0;
function advanced(){
	if(adv_hide==0){
		$('#btn_advanced').val('- Advanced Options');
		$('#advanced').show();
		adv_hide=1;
	}else{
		$('#btn_advanced').val('+ Advanced Options');
		$('#advanced').hide();
		adv_hide=0;
	}
	
}
</script>

</div>
[/#macro]




[#macro HistoricalHoldingsUnit portfolio holdingDates aggregateFlag]
			<style type="text/css">

			.ui-slider .ui-slider-handle { position: absolute; z-index: 2; width: 1.2em; height: 1.2em; cursor: default; background: #ECECEC;border-color: black;}
			</style>
			
			
			<script>
			
			
			[#if holdingDates??]
			var dateArray=new Array(${holdingDates?size});
			var dateArray_js=new Array(${holdingDates?size});
			var cacheArray=new Array(${holdingDates?size});
			var detailCacheArray=new Array(${holdingDates?size});
			var transactionArray=new Array(${holdingDates?size});
			var detailTransactionArray=new Array(${holdingDates?size});
			[#list holdingDates as d]
			dateArray[${d_index}]='${d?string("yyyy-MM-dd")}';
			dateArray_js[${d_index}]=Date.parse('${d?string("MM/dd/yyyy")}');
			cacheArray[${d_index}]=0;
			detailCacheArray[${d_index}]=0;
			transactionArray[${d_index}]=0;
			detailTransactionArray[${d_index}]=0;
			[/#list]		
			[/#if]
			var flag = false;
			function getValue(dateText){
				for(var i=0;i<dateArray.length;i++){
					if(dateArray[i]==dateText)return i;
				}
				return -1;
			}
			function showDetail(){
				if(flag==false){
					flag = true;
					$("#result_holdingPanel").hide();
					$("#result_detailHoldingPanel").show();
					$("#result_transactionPanel").hide();
					$("#result_detailTransactionPanel").show();
				}else if(flag == true)
				{
					$("#result_holdingPanel").show();
					$("#result_detailHoldingPanel").hide();
					$("#result_transactionPanel").show();
					$("#result_detailTransactionPanel").hide();
					flag = false;
				}
				
				getContent(${holdingDates?size}-1);
			}
			function getContent(value){
				$("#holding_date").val(dateArray[value]);
				$("#holdingPanel").html('please wait');
				
				$("#detailHoldingPanel").html('pleanse wait');
				
				$("#transactionPanel").html('please wait');
				
				$("#detaiTransactionPanel").html('please wait');
				if(cacheArray[value]!=0&&flag==false){
					$("#holdingPanel").html(cacheArray[value]);
					$("#transactionPanel").html(transactionArray[value]);
					return;
				}
				if(detailCacheArray[value]!=0&&flag==true){
					$("#detailHoldingPanel").html(detailCacheArray[value]);
					$("#detaiTransactionPanel").html(detailTransactionArray[value]);
					return;
				}
				if(flag == false){
					$.ajax({type: "post",  
			        	 url:'../clonecenter/ViewHolding.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&holdingDate='+dateArray[value],  
			         	dataType: "html",  
			         	//data: "userName="+userName+"&password="+password,  
			         	success: function(result){
			         		cacheArray[value]=result;
			         		$("#holdingPanel").html(result);
			         	}
			     	});
			     	$.ajax({type: "post",  
			        	 url:'../clonecenter/ViewTransaction.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&holdingDate='+dateArray[value],  
			         	dataType: "html",  
			         	//data: "userName="+userName+"&password="+password,  
			         	success: function(result){
			         		transactionArray[value]=result;
			         		$("#transactionPanel").html(result);
			        	 }   
			    	 });
			     }
			     if(flag == true){
			    	 $.ajax({type: "post",  
			         	 url:'../clonecenter/detailHolding.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&holdingDate='+dateArray[value],  
			        	 dataType: "html",  
			        	 //data: "userName="+userName+"&password="+password,  
			        	 success: function(result){
			         		detailCacheArray[value]=result;
			         		//alert("detailHoding");
			         		$("#detailHoldingPanel").html(result);
			        	 }
			    	 });
			    	 $.ajax({type: "post",  
			        	 url:'../clonecenter/detailTransaction.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&holdingDate='+dateArray[value],  
			        	 dataType: "html",  
			        	 //data: "userName="+userName+"&password="+password,  
			        	 success: function(result){
			        	 	detailTransactionArray[value]=result;
			        	 	//alert("detailTransaction");
			         		$("#detaiTransactionPanel").html(result);
			        	 }   
			    	 });	
			     }	
			}
			function trace(){
				$('#btn_trace').attr({disabled:true});
				$.ajax({type: "post",  
			         url:'../ViewTransaction.action?includeHeader=false&includeJS=false&ID=${portfolio.ID}&holdingDate=2008-01-01&symbol='+$('#stockname').val(),  
			         dataType: "html",  
			         success: function(result){
			         	$("#result_trace").html(result);
			         	$('#btn_trace').attr({disabled:false});
			         }
			     });	
			}
			function setValue(val){
				//$('#slider').slider('value',val);
			}
			
			function pre(){
				var v=$("#slider").slider('value');
				if(v>0){
					$('#slider').slider('value',v-1);
					$("#holding_date").val(dateArray[v-1]);
					getContent(v-1);	
				}
			}
			
			function next(){
				var v=$("#slider").slider('value');
				if(v<${holdingDates?size}-1){
					$('#slider').slider('value',v+1);
					$("#holding_date").val(dateArray[v+1]);
					getContent(v+1);	
				}
			}
			$(function() {
			 	
			 	
				$("#slider").slider({
					min: 0,
					max: ${holdingDates?size}-1,
					value:${holdingDates?size}-1,
					step: 1,
					slide: function(event, ui) {
						$("#holding_date").val(dateArray[ui.value]);
					},
					stop: function(event, ui) {
						getContent(ui.value);	
					}
				});
				
				$('#newsPanel').load('/LTISystem/jsp/news/labels/portfolio_${ID?string.computer}.html?includeHeader=false');
				
				//$("#tabs").tabs();
				
				$("#holding_date").val(dateArray[$("#slider").slider("value")]);
				
				getContent(${holdingDates?size}-1);
				
			   
				
			      
			      $("#holding_date").datepicker({ dateFormat: 'yy-mm-dd',
			 		changeMonth:true,
			 		changeYear:true,
			 		highlight:dateArray_js,
			 		onSelect: function(dateText) {
			 			var val=getValue(dateText);
			 			if(val==-1)return;
			 			getContent(val);
			 			setValue(val);
					}
			 	});
			 	//$("#holding_date").datepicker('setHighlight',dateArray_js);
			    
			});
			
			
			
			
			$(function(){
				$("#btn_more_hist").toggle(
					function(){
						$("#div_hist").fadeIn();
						$(this).val("- Transactions and Historical Holdings");
					},
					function(){
						$("#div_hist").fadeOut();
						$(this).val("+ Transactions and Historical Holdings");
					}
				);
			});
			
			
			
			
			function getCSV(obj){
				var holdingDate = $("#holding_date").val();
				if(obj=="folio")
					url = "/LTISystem/jsp/portfolio/OutputActHolding.action?ID=${portfolio.ID}&holdingDate="+holdingDate+"&folio=true";
				else if(obj=="mpiq")
					url = "/LTISystem/jsp/portfolio/OutputActHolding.action?ID=${portfolio.ID}&holdingDate="+holdingDate+"&folio=false";
				location.href = url;
			}
			
			
			
			
			
			</script>
			<br>
			<div>
			<input type='button' id='btn_more_hist' class=btn  style='width:270px;text-align:left' value='+ Transactions and Historical Holdings'>
			</div>
			
<div class="sidebar_box_noPadding roundHeadingBlue" style='display:none' id="div_hist">
				<div class="sidebar_box_heading_white">Historical Info</div>
				<div class="sidebar_box_content">
				
Monthly Holding Date: <input type="text" id="holding_date" name="holdingDate" readonly="true" style="border:1px solid #b1c9d9; color:black; font-weight:bold;" />
		[#if aggregateFlag]
		<input type='button' id='btn_detail_info' class="uiButton" onclick='showDetail()' style='text-align:left;' value='Detail'>
		[/#if]
		<!--<div><span style='font-weight: bold;'><a title="Download the holdings whose the date you selected into CSV file." href="javascript:void(0)" onclick="getCSV('mpiq')">Download to CSV</a></span>
		<span style='font-weight: bold;'><a title="Download the holdings whose the date you selected into CSV file for Folio." href="javascript:void(0)" onclick="getCSV('folio')">Download to Folio CSV</a></span>
                <span style='font-weight: bold;'><a title="Instructions on How to Use CSV on FolioInvesting.com" href="/LTISystem/UserFiles/FolioHowto.pdf">Instructions On FolioInvesting.com</a></span>
		</div>-->
		<div style="width:100%;" >
			<table width="100%;">
				<tr style="font-size:12px">
					<td width=20% align="left" valign="middle">
						<a href="javascript:pre()" style="text-decoration:none"><img src="../images/jquery.tablesorter/addons/pager/icons/prev.png" border="0">Previous</a>
					</td>
					<td width=20% align="center">
					</td>
					<td width=20% align="center">
					</td>
					<td width=20% align="center">
					</td>
					<td width=20% align="right" valign="middle">
					<a href="javascript:next()" style="text-decoration:none">Next<img src="../images/jquery.tablesorter/addons/pager/icons/next.png" border="0"></a>
					</td>
				</tr>
				<tr>
					<td colspan="5">
						<div id="slider" style="margin-left:1px"></div>
					</td>
				</tr>
				<tr style="font-size:8px">
					<td width=20% align="left">|</td>
					<td width=20% align="center">|</td>
					<td width=20% align="center">|</td>
					<td width=20% align="center">|</td>
					<td width=20% align="right">|</td>
				</tr>
				<tr style="font-size:10px">
					<td width=20% align="left">
						<script>document.write(dateArray[0])</script>
					</td>
					<td width=20% align="center">
						[#assign l2=holdingDates?size*3/10]
						<script>document.write(dateArray[${l2?floor-1}])</script>
					</td>
					<td width=20% align="center">
						[#assign l3=holdingDates?size*5/10]
						<script>document.write(dateArray[${l3?floor-1}])</script>
					</td>
					<td width=20% align="center">
						[#assign l4=holdingDates?size*7/10]
						<script>document.write(dateArray[${l4?floor-1}])</script>
					</td>
					<td width=20% align="right">
						<script>document.write(dateArray[${holdingDates?size-1}])</script>
					</td>
				</tr>
				
						
			</table>
		</div>
<br>





	<div id="tabs"><!-- height:auto !important;height:400px;min-height:600px; -->
		<!-- <ul>
			<li><a href="#result_holdingPanel">Holdings</a></li>
			<li><a href="#result_transactionPanel">Transaction</a></li>
		</ul>-->
		
		<div id="result_holdingPanel">
			<span style='font-weight: bold;'>Holdings</span>
			<div id="holdingPanel" style="width:100%">
			</div>
		</div>
		
		<div id="result_detailHoldingPanel" style="display:none">
			<span style='font-weight: bold;'>DetailHoldings</span>
			<div id="detailHoldingPanel" style="width:100%">
			</div>
		</div>
		
		<div id="result_transactionPanel">
			<span style='font-weight: bold;'>Transactions</span>
			<div id="transactionPanel" style="width:100%">
			</div>
		</div>
		
		<div id="result_detailTransactionPanel" style="display:none">
			<span style='font-weight: bold;'>DetailTransactions</span>
			<div id="detaiTransactionPanel" style="width:100%">
			</div>
		</div>
	</div>
</div>

		</div>
		<br>
[/#macro]






