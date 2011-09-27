<script>
	function copy(){
		[@authz.authorize ifAllGranted="ROLE_ANONYMOUS"]
			alert('You need to register for free to follow SAA portfolios or subscribe to follow the TAA portfolios. Please login or register first.');
			$("#loginEntry").trigger("click");
			return;	
		[/@authz.authorize]
		
		
		[@authz.authorize ifNotGranted="ROLE_ANONYMOUS"]
		[#if !pc.hasSimulateRole]
			alert('You need to subscribe as Basic User to follow the TAA portfolios. Please subscribe for one month free trial.');
			$("#loginEntry").trigger("click");
			return;	
		[#elseif plan??]
			$.ajax({
			    url: 'getstarted_followportfolio.action?includeHeader=false',
			    type: 'POST',
			    data: "portfolioID=${portfolioID?string.computer}&planID=${plan.ID?string.computer},
			    error: function(message){
			        alert($.trim(message.responseText));
			        return;
			    },
			    success: function(result){
			    	alert("This portfolio has been added to 'My Followed Public Portfolios' table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + result + " more portfolio(s)");
			    }
			});//end ajax
		[/#if]
		[/@authz.authorize]
	}

$(function(){
	   $("#btn_follow").toggle(
			   function(){
			   		$("#follow_pp").fadeIn();
			   		$("#btn_follow").css({"background-image": "url(images/grey-bg2.gif)"});
			   },
			   function(){
			    	$("#follow_pp").fadeOut();
			    	$("#btn_follow").css({"background-image": "url(images/grey-bg.gif)"});
			   }
		   );
		   
		   
		[#if pc.hasRealtimeRole()]
		[#else]
			$("#btn_customize2").toggle(
			   function(){
			   		$("#customize_message").fadeIn();
			   		$("#btn_customize2").css({"background-image": "url(images/grey-bg2.gif)"});
			   },
			   function(){
			    	$("#customize_message").fadeOut();
			    	$("#btn_customize2").css({"background-image": "url(images/grey-bg.gif)"});
			   }
			 );
		[/#if]
});
		
		
		
		
	
	 var isnameok=false;
	 var oldname=null
	function check(){
		var name=$.trim($("#portfolioName").val());
		if(name==""){
			isnameok=false;
			$("#checker").html("Please input a name");
			$("#checker").show();
				}
			if(oldname!=null&&oldname==name){
				return;
				}
						
			isnameok=checkname();
			if(isnameok){
				$("#checker").html("√");
				$("#checker").show();
				}else{
				$("#checker").html("Please input another name");
				$("#checker").show();
				}
						
				oldname=name;
						
			    }
					
				$(function(){
					check();
				});



</script>




<input type='button' id='btn_customize' class=btn style='font-weight: bold;display:none' onclick='window.location.href="/LTISystem/jsp/portfolio/EditHolding.action?operation=editbasic&ID=${portfolio.ID}"' value='Original Portfolio'>
<br>
<input title='chang the parameters of this portfolio, such as Risk Profile and Rebalance Frequancy' type='button' id='btn_modify' class=btn style='font-weight: bold;display:none' onclick='window.location.href="/LTISystem/select_modify.action?portfolioID=${portfolio.ID}"' value='Change Parameters'>






[#if portfolio.strategies.assetAllocationStrategy.parameter??]
	[#if portfolio.strategies.assetAllocationStrategy.parameter["PlanID"]??]
    <input title="" type='button' id='btn_follow' class=btn style='font-weight: bold' value='Follow This Portfolio'>
    [#if Parameters.follow?? && Parameters.follow=="true"]
    	<script>
    		$(function(){$("#btn_follow").trigger("click");});
    	</script>
    [/#if]
	
	[#if pc.hasRealtimeRole()]
		<input title="customize a new portfolio for the same plan" type='button' id='btn_customize2' class=btn style='font-weight: bold;display:none' onclick='window.location.href="/LTISystem/select_entry.action?portfolioID=${portfolio.ID}"' value='Customize'>
	[#else]
		<input title="customize a new portfolio for the same plan" type='button' id='btn_customize2' class=btn style='font-weight: bold;display:none'  value='Customize'>
	
		<div id="customize_message" style="display:none">
		[#if pc.anonymous]
			<br>
			<span style="font-size:1.2em;line-height:20px;font-weight:bold">
			You need to register for free to customize SAA portfolios or subscribe to customize the TAA portfolios. Please login or register first.  </span>
			<br>
			<br>
			<a class="uiButton" href="javascript:void(0)"  onclick='$("#loginEntry").trigger("click");' >Login</a>&nbsp;
			<a class="uiButton" href="/LTISystem/jsp/register/openRegister.action" target="_blank" >Register</a>&nbsp;
			
			<br>
			<br>
		[#else]
			<br>
			<span style="font-size:1.2em;line-height:20px;font-weight:bold">
				You need to subscribe as Basic User to customize the TAA portfolios. Please subscribe for one month free trial.</span>
			<br>
			<br>
			<a class="uiButton" href="/LTISystem/paypal_subscr.action" target="_blank">About Subscription</a>
		[/#if]
		</div>
	[/#if]
	
	
	[/#if]
[/#if]



<script>
[#if isAdvancedUser]
	[#if !portfolio.strategies.assetAllocationStrategy.parameter?? || !portfolio.strategies.assetAllocationStrategy.parameter["PlanID"]??]
		$('#btn_customize').show();
	[#else]
        $('#btn_customize2').show();
	[/#if]
[#elseif isAdmin]
	$('#btn_customize').show();
	[#if isPlanPortfolio]
		$('#btn_modify').show();
	[/#if]
	$('#btn_customize2').show();
[#else]
	if(window.location.host=="127.0.0.1"||window.location.host.indexOf("validfi.com")!=-1){
		$('#btn_customize').show();
	}else{
		[#if portfolio.strategies.assetAllocationStrategy.parameter??]
			[#if portfolio.strategies.assetAllocationStrategy.parameter["PlanID"]??]
				$('#btn_customize2').show();
			[/#if]
		[/#if]
	}
[/#if]



</script>


<input title='the news and articles on this portfolio or this plan' type='button' class=btn style='font-weight: bold;' onclick='window.open("/LTISystem/jsp/news/labels/portfolio_${ID?string.computer}.html")' value='News' >


[#-- 
	[@pu.EMailAlertButtonUnit portfolio plan pc.hasRealtimeRole() emailNotification]
	[/@pu.EMailAlertButtonUnit]
--]

	

[@ltiauthz.authorize ifAnyGranted="MPIQ_E,MPIQ_P,ROLE_SUPERVISOR"]
<input title="Use MyPlanIQ Portfolio Compare to compare multiple portfolios." type='button' class=btn style='font-weight: bold;' onclick='window.open("/LTISystem/jsp/portfolio/CompareMain.action?action=viewportfolio&ID=${portfolio.ID?string.computer}&pName=${portfolio.name}")' value='Portfolio Compare' >
[/@ltiauthz.authorize]
<br>
<br>

              <div id="follow_pp"style="display:none;margin:5px">
					[#if pc.hasRealtimeRole()]
					<br>
					We will build a private portfolio just for you. Please input a name for the portfolio.
					<br>
					<br>
					
					<input name="portfolioName" value="${pc.loginUser.userName}`s copy of ${portfolio.name}" id="portfolioName" style="border:1px solid #ccc;height:30px;width:500px" onblur="check()">&nbsp;<span id="checker" style="border:1px solid #ccc;background-color:#FFFB7B;display:none;padding-right:5px">√</span>
					<br>
					<br>
					<p>
						<button class="uiButton" style='font-weight: bold;' onclick="if(isnameok){$(this).attr('disabled',true);copy();return false;}else{alert('Please check your name.');return false;}">Follow</button>
					</p>
					<div id="process_bar" style="display:none">
					
					</div>
					[#else]
						[#if pc.anonymous]
							<br>
							<span style="font-size:1.2em;line-height:20px;font-weight:bold">You need to register for free to follow SAA portfolios or subscribe to follow the TAA portfolios. Please login or register first. </span>
							<br>
							<br>
							<a class="uiButton" href="javascript:void(0)"  onclick='$("#loginEntry").trigger("click");' >Login</a>&nbsp;
							<a class="uiButton" href="/LTISystem/jsp/register/openRegister.action" target="_blank" >Register</a>&nbsp;
							
							<br>
							<br>
						[#else]
							<br>
							<span style="font-size:1.2em;line-height:20px;font-weight:bold">You need to subscribe as Basic User to follow the TAA portfolios. Please subscribe for one month free trial.</span>
							<br>
							<br>
							<a class="uiButton" href="/LTISystem/paypal_subscr.action" target="_blank">About Subscription</a>
						[/#if]
					[/#if]
				</div>
				
				
[#if !realtime]
<span style="line-height:2.0em"><h1>For a non-subscriber, the portfolio information is delayed 30 days or more.</h1></span>
<br>
[/#if]

















[#macro BtnCustomize portfolio anonymous hasSimulateRole]
<input title="" type='button' id='btn_customize' class=btn style='font-weight: bold' value='Customize'>
<div id=customizediv style="margin-top:7px;margin-bottom:7px;display:none">
	[#if anonymous]
		<span style="font-size:1.2em;line-height:20px;font-weight:bold">
		You need to register for free to customize SAA portfolios or subscribe to customize the TAA portfolios. Please login or register first.  </span>
		<br>
		<br>
		<a class="uiButton" href="javascript:void(0)"  onclick='$("#loginEntry").trigger("click");' >Login</a>&nbsp;
		<a class="uiButton" href="/LTISystem/jsp/register/openRegister.action" target="_blank" >Register</a>&nbsp;
	[#else]
		[#-- 检查是否有customize的权限 --]
		[#if hasSimulateRole]
		[#else]
			<span style="font-size:1.2em;line-height:20px;font-weight:bold">You need to subscribe as Basic User to customize the TAA portfolios. Please subscribe for one month free trial.</span>
			<br>
			<br>
			<a class="uiButton" href="/LTISystem/paypal_subscr.action" target="_blank">About Subscription</a>
		[/#if]
	[/#if]
</div>
<script>
$(function(){
		[#if !hasSimulateRole || anonymous]
	   		$("#btn_customize").toggle(
	   	
			   function(){
			   		$("#btn_mess_div").html($("#customizediv").html());
			   		$("#btn_mess_div").fadeIn();
			   		$("#btn_customize").css({"background-image": "url(images/grey-bg2.gif)"});
			   },
			   function(){
			    	$("#btn_mess_div").fadeOut();
			    	$("#btn_customize").css({"background-image": "url(images/grey-bg.gif)"});
			   }
		     );
		[#else]
			$("#btn_customize").click(function(){
				window.location.href="/LTISystem/select_entry.action?portfolioID=${portfolio.ID}";
			});
    	[/#if]
});

</script>
[/#macro]
