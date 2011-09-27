[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
	<div id="basic-info" style="width:98%;margin-left:15px;min-height:40px">
		<span style="float:left;font-size:12pt"><strong>${security.name} (${security.symbol})</strong> 
		<span style="color:red;font-size:10pt"> start from ${startDate?string("MM/dd/yyyy")}</span></span> 		
	</div>
	<p class="clear"/>	

	<div id="main" style="width:60%;margin-top:2px;">
		<div class="border" id="compareDiv" style="width:500px;min-height:250px">
			<div "performance-compare">
				<h style="font-size:12pt;font-weight:bold">Performance Compare</h>
				<p style="width:100%">
					<label>Fund Symbol</label> 
					<input type="text" id="symbol"/>
					<input type="button" value="compare" id="compare" />
					<span id="comparetips" style="display:none;color:#3333ff">blue means fund ${compareSymbol!}</span>
					<script type="text/javascript">
						$("#compare").click(function(){
							var compareSymbol = $("#symbol").val();
							if(compareSymbol == ""){
								alert("no compare symbol");
							}
							else{
								$("#performancetable").html("");
								var paras = window.location.search;
								$("#performancetable").load("Performance.action" + paras + "&includeHeader=false&compareSymbol=" + compareSymbol,{}, function(){
									$("#more").html($("#history").html());
									$("#comparetips").show();
								});
								
							}
						})
					</script>
				</p>
			</div>
			<div id="calcualte_div" style="width:100%;margin-top:20px">
				<h style="font-size:12pt;font-weight:bold;padding:0;margin:0">Calculate Performance</h>
				<div id="calculate">
					<p>
						<label>start date</label> <input type="text" id="startDate"/>(MM/dd/yyyy) <input type="button" value="calculate" id="calculate" onclick="calculate()"/>
					</p>
					<p class="clear"/>
					<p>
						<label>end date</label> <input type="text" id="endDate"/>(MM/dd/yyyy)
					</p>
					<script type="text/javascript">
					$(document).ready(function(){
						var symbol = document.location.href;
						var re=new RegExp(/symbol=.+\&/);
						symbol = re.exec(symbol);
						//alert(symbol.replace(/symbol/,""));
						if(symbol == ""){
							alert("no symbol");
						}
						var url = "/LTISystem/f401k_listQuality.action?"+symbol+"includeHeader=false";
						$.ajax(
						{
							type:"get",
							url:url,
							success:function(data)
							{
								var list = new Array();
								list = data.split("|");
								for(var i = 0; i < list.length; i++){
									$("#quality").append("<tr><td>"+list[i]+"</td><br></tr>");
								}
							}
						});
					});
					
						function calculate(){
							var startDate = $("#startDate").val();
							var endDate = $("#endDate").val();
							if(startDate == null || startDate == ""){
								alert("No Start Date!");
							}
							else
							{
								$("#result-fieldset").show();
								$("#calculateresult").html("loading...");
								var paras = window.location.search;
								//startDate = escape();
								paras += "&startDate=" + startDate;
								if(endDate != null && endDate != "")
									paras+= "&endDate=" + endDate;
								paras+= "&chosenMPT=AR,standardDeviation,sharpeRatio"
								//paras = escape(paras);
								$("#calculateresult").load("CalculatePerformance.action" + paras+"&includeHeader=false");
							}
						}
					</script>
					<fieldset id="result-fieldset" style="display:none;">
					<legend>calculate result</legend>
					<div id="calculateresult" style="width:100%;font-size:10pt">
						
					</div>
					</fieldset>
				</div>
			</div>
		</div>
	</div>
	<p class="clear"/>
	
	
<div id="history" style="width:99%;margin:0 auto;height:auto;text-align:left;">
	<div class="border" style="margin-top:5px;width:auto">
		<h2 style="padding:0;margin:0;font-size:10pt">Performance History </h2>(As of [#if displayDate??]${displayDate?string("MM/dd/yyyy")}[#else]UNKNOWN (Calculation Failure)[/#if])
        <div id="performancetable" style="width:920px;overflow:auto">
		[@s.action name="Performance" namespace="/jsp/fundcenter" executeResult=true]
			[@s.param name="symbol"]${symbol}[/@s.param]
	    [/@s.action]
	    </div>
	</div>
</div>
<div>
	<table id="quality">
	</table>
</div>

