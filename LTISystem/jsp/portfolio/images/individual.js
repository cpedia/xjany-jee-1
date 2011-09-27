$(document).ready(function(){
	var $window = jQuery(window);
	/*hide the security at the beginning*/
	//$('tr[@id^=security-]').hide().children('td');
	
	/*double click to change the content of the portfolio*/
	$("#portfolioName,#originalAmount, #startingDate, #benchmark, #cashFlow, #rebalancingStrategy, #assetAllocation, #endDate").toggleVal("active");
	
	/*benchmark suggest function*/
	
	jQuery("#benchmark").suggest("../ajax/GetSecuritySuggestTxt.action",
										 { 	haveSubTokens: true, 
										 	onSelect: function(){}});

	/**cash flow strategy parameters pop window
	*/
	/*$('#CFSPWindow').Draggable(
	{
		zIndex: 	20,
		ghosting:	false,
		opacity: 	0.7,
		handle:	'#CFSPWindow_handle'
	});	*/
	
	$('#CFSPWindow').css({
		position: 'absolute',
		width: 600,
		//top: 200,
		left: ($window.width()-600)/2
	});
	
	$('#CFSPWindow_close').click(function()
	{
		$("#CFSPWindow").hide();
	});	
	
	$('#CFSPWindow_confirm').click(function()
	{
		$("#CFSPWindow").hide();
	});	
	
	$("#csfp").click(function(){
		$("#CFSPWindow").show();
	})
	
	/**cashflow strategy suggest function
	*/
	$('.cashFlowStrategyParameter').ajaxContent({
		target:'#CFSPWindow_content',
		success: function(obj,target,msg){
			$(target).css({border:'1px solid #a9afd7'});
			if(msg.indexOf("No Parameter") == -1){
				$("#CFSPWindow").show();
			}
		},
		 error: function(target){
		 	$(target).css({color: 'red',fontSize:'18px'});
		 },
		 errorMsg:'Something went wrong'
	}); 
	
	jQuery("#cashFlow").suggest("../ajax/GetStrategySuggestTxt.action?type=CASH FLOW STRATEGY",
						  { onSelect: function() {
						  $('.cashFlowStrategyParameter').attr({ href: '../ajax/GetStrategyParameter.action?name=portfolio.cashFlowParameter&q='+this.value });
						  $('.cashFlowStrategyParameter').trigger("click");
						  changeCheck(this.value,"#cashFlowStrategyID","#cashFlowCheck")}});
	
	/**rebalancing strategy parameters pop window
	*/
	/*$('#RBSPWindow').Draggable(
	{
		zIndex: 	20,
		ghosting:	false,
		opacity: 	0.7,
		handle:	'#RBSPWindow_handle'
	});*/
	
	$('#RBSPWindow').css({
		position: 'absolute',
		width: 600,
		//top: 200,
		left: ($window.width()-600)/2
	});	
	
	$('#RBSPWindow_close').click(function()
	{
		$("#RBSPWindow").hide();
	});	
	
	$('#RBSPWindow_confirm').click(function()
	{
		$("#RBSPWindow").hide();
	});		
	
	$("#rbsp").click(function(){
		$("#RBSPWindow").show();
	});
	
	/**rebalancing strategy suggest function
	*/
	$('.rebalancingStrategyParameter').ajaxContent({
		target:'#RBSPWindow_content',
		success: function(obj,target,msg){
			$(target).css({border:'1px solid #a9afd7'});
			if(msg.indexOf("No Parameter") == -1){
				$("#RBSPWindow").show();
			}
		},
		error: function(target){
			$(target).css({color: 'red',fontSize:'18px',border:'1px solid #FF0000'});
		},
		errorMsg:'Something went wrong'
	});
	jQuery(function() {jQuery("#rebalancingStrategy").suggest("../ajax/GetStrategySuggestTxt.action?type=REBALANCING STRATEGY",
															{ onSelect: function() {
														  	$('.rebalancingStrategyParameter').attr({ href: '../ajax/GetStrategyParameter.action?name=portfolio.rebalancingParameter&q='+this.value });
														  	$('.rebalancingStrategyParameter').trigger("click"); 
														  	changeCheck(this.value, "#rebalancingStrategyID", "#rebalancingCheck")}});});
														  	
	/**asset allocation strategy parameters pop window
	*/
	/*$('#AASPWindow').Draggable(
	{
		zIndex: 	20,
		ghosting:	false,
		opacity: 	0.7,
		handle:	'#AASPWindow_handle'
	});	*/
	$('#AASPWindow').css({
		position: 'absolute',
		width: 600,
		//top: 200,
		left: ($window.width()-600)/2
	});
	
	$('#AASPWindow_close').click(function()
	{
		$("#AASPWindow").hide();
	});	
	
	$('#AASPWindow_confirm').click(function()
	{
		$("#AASPWindow").hide();
	});	

	$("#aasp").click(function(){
		$("#AASPWindow").show();
	});

	$('.assetAllocationStrategyParameter').ajaxContent({
		target:'#AASPWindow_content',
		success: function(obj,target,msg){
			$(target).css({border:'1px solid #a9afd7'});
			if(msg.indexOf("No Parameter") == -1){
				$("#AASPWindow").show();
			}
		},
		error: function(target){
			$(target).css({color: 'red',fontSize:'18px',border:'1px solid #FF0000'});
		},
		errorMsg:'Something went wrong'
	});
	jQuery(function() {  jQuery("#assetAllocation").suggest("../ajax/GetStrategySuggestTxt.action?type=ASSET ALLOCATION STRATEGY",
															{ onSelect: function() {
															$('.assetAllocationStrategyParameter').attr({ href: '../ajax/GetStrategyParameter.action?name=portfolio.assetAllocationParameter&q='+this.value });
															$('.assetAllocationStrategyParameter').trigger("click");
															changeCheck(this.value, "#assetAllocationID", "#assetAllocationCheck")}});});
		

})
function changeCheck(value, hiddenID, checkID){
	var $hidden = $(hiddenID);
	var $check = $(checkID);
	$.ajax({
		type: "post",
		url: "../ajax/GetStrategyID.action?strategyName=" + value,
		datatype: "html",
		success: function(result){
			if(result!=null){
				$hidden.val(result);
				$check.attr({href:"../strategy/View.action?ID="+result+"&action=view"});
			}
			else
			{
				$input.val('STATIC');
				$hidden.val(0);
				$("#check"+checkID).attr({href:"../strategy/View.action?ID=0&action=view"});
			}
		}
	})
}

