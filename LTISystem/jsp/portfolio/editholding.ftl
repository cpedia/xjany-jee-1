[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
<html>
<head>
<meta name="submenu" content="individualportfolio"/>
<meta name="id" content='${ID}'/>
<meta name="jqueryui" content="smoothness">
<title>Edit holding of ${portfolio.name}</title>
<script type="text/javascript" src="/LTISystem/jsp/images/tiny_mce/tiny_mce.js"></script>
<script>

function saveas(){
	if($('#name').val()==""||$('#name').val()==$('#oldname').val()){
		alert('Please input another name the new one.');
		return;
	}
	$('#operation').val('saveas');
	$('#saveform').submit();
}

function remove(){
	if(confirm("Are you sure to delete this portfolio?")){
		$('#operation').val('delete');
		$('#saveform').submit();
	}
}

tinyMCE.init({
    mode : "none",
    theme : "advanced",
    plugins : "save",
	theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,|,fontselect,fontsizeselect",
	theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,blockquote,link,unlink,anchor,image,|,forecolor,backcolor",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	save_onsavecallback : "save_for_mce"
});
$(function() {
	tinyMCE.execCommand('mceAddControl', false, "description");
	
	
	var arr=new Array('pre','post');
	var cssesarr=new Array('','','','width:100%');
	$('#parametertable').editabletable({name:'attributes',data:arr,csses:cssesarr});
	
});  	
function accAdd(arg1,arg2){  
	var r1,r2,m;  
	try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}  
	try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}  
	m=Math.pow(10,Math.max(r1,r2))  
	return (arg1*m+arg2*m)/m  
}  
Number.prototype.add = function (arg){  
	return accAdd(arg,this);  
}  

function check(){
	var targetpercentage=0.0;
	if($('.targetpercentage').length!=0){
		$('.targetpercentage').each(function(){
			targetpercentage=accAdd(targetpercentage,$(this).val());
		});
		if(targetpercentage-1.0>0.01||1.0-targetpercentage>0.01){
			return "The total of the target percentage should be 1.0.";
		}
	}
	
	
	var assetname=false;
	$('.assetname').each(function(){
		if($(this).val()==""){
			assetname=true;
		}
	});
	if(assetname)return "Please input a name for all assets.";
	
	
	var securityname=false;
	$('.suggestsecurity').each(function(){
		if($(this).val()==""){
			securityname=true;
		}
	});
	if(securityname)return "Please input a name for all securities.";
	
	var shares=false;
	$('.share').each(function(){
		if($(this).val()==""||$(this).val()=="Infinity"||isNaN($(this).val())){
			shares=true;
		}
	});
	if(shares)return "The share is NaN, please check the amount field or the symbol field.";
	
	
	return "";
}
function save(){
	var str=check();
	if(str!=""){
		alert(str);
		return;
	}
	var pattern =/^((\d{1,2})\/(\d{1,2})\/(\d{4}))$/;  
	var startingdate=$('#startingdate').val();
	if(!pattern.test(startingdate)){
		alert( "Please input the starting date with format 'dd/MM/yyyy'.");
		return;
	}
	if($('#name').val()=="New Portfolio"){
		alert("Please input a name for the new portfolio.");
	}
	$('#description').val(tinyMCE.getInstanceById('description').getBody().innerHTML);
	$.ajax({
	   type: "Post",
	   url: "SaveHolding.action?includeHeader=false[#if ID==0]&strategyID=${strategyID!""}[/#if]",
	   data:$("#saveform").serialize(),
	   success: function(msg){
	     
	   [#if ID!=0]
	     window.location.reload();
	    [#else]
	     $('body').html(msg);
	    [/#if]
	   },
	  error: function(message){
        alert($.trim(message.responseText));
        return;
      }
	 });	
}
[#if ID!=0 && !personal]
$(function(){
				$("#benchmark").jsuggest({type:"security"});
				
				$('#parameterwindow').jparameter({strategyname:$('#assetAllocationStrategy').val(),storeddata:${holding.assetAllocationStrategyParameterJSON!"null"},name:'holding.assetAllocationStrategyParameter'});
					$('#parameter').click(function(){
						$('#parameterwindow').toggle();
					});
					$("#assetAllocationStrategy").jsuggest({
						type:"strategy",
						select:function(event,ui){
							$("#parameterwindow").children().remove();
							$('#parameterwindow').jparameter({strategyname:$('#assetAllocationStrategy').val(),name:'holding.assetAllocationStrategyParameter'});
						}
					});
					
				$('#parameterwindow_r').jparameter({strategyname:$('#rebalancingStrategy').val(),storeddata:${holding.rebalancingStrategyParameterJSON!"null"},name:'holding.rebalancingStrategyParameter'});
					$('#parameter_r').click(function(){
						$('#parameterwindow_r').toggle();
					});
					$("#rebalancingStrategy").jsuggest({
						type:"strategy",
						select:function(event,ui){
							$("#parameterwindow_r").children().remove();
							$('#parameterwindow_r').jparameter({strategyname:$('#rebalancingStrategy').val(),name:'holding.rebalancingStrategyParameter'});
						}
					});

				$('#parameterwindow_c').jparameter({strategyname:$('#cashFlowStrategy').val(),storeddata:${holding.cashFlowStrategyParameterJSON!"null"},name:'holding.cashFlowStrategyParameter'});
					$('#parameter_c').click(function(){
						$('#parameterwindow_c').toggle();
					});
					$("#cashFlowStrategy").jsuggest({
						type:"strategy",
						select:function(event,ui){
							$("#parameterwindow_c").children().remove();
							$('#parameterwindow_c').jparameter({strategyname:$('#cashFlowStrategy').val(),name:'holding.cashFlowStrategyParameter'});
						}
					});
					
					var data=${holdingJSON};
	
		var styles={
			"title":"Holdings",
			"name":"holding.assets",
			"leafname":"holdingItems",
			"root": [
				 {"name":"name","title":"Name","type":"text","css":"assetname","width":"200"},
				 {"name":"targetPercentage","title":"TA*","type":"text","css":"targetpercentage","width":"30"},
				 {"name":"assetClassName","title":"Asset Class","type":"select","css":"acselect"},
				 {"name":"assetStrategy","title":"Strategy","type":"text","css":"suggeststrategy","width":"350"},
				 {"name":"assetStrategyParameter","title":"PA*","type":"button","css":"jparameter","width":"26"},
				 {"name":"check","title":"CH*","type":"button","css":"strategycheck","width":"26"}
		     ],
		     "leaf":[
		         {"name":"reInvest","title":"DR*","type":"select","css":"riselect","width":"50"},
		         {"name":"share","title":"Shares","type":"readonly","css":"share","width":"90"},
		         {"name":"amount","title":"Amount","type":"text","css":"amount","width":"90"},
		         {"name":"price","title":"Price","type":"readonly","css":"price","width":"50"},
		         {"name":"symbol","title":"Symbol","type":"text","css":"suggestsecurity","width":"550"}
		     ]
		            
	};
	
	[#if holding.assets??]
		var parameters={
		[#list holding.assets as asset]
			"${asset.name}":[#if asset.assetStrategyParameterJSON?? && asset.assetStrategyParameterJSON!=""]${asset.assetStrategyParameterJSON}[#else]{}[/#if]
			[#if asset_index!=(holding.assets?size-1)],[/#if]
		[/#list]
		};
	[/#if]
	$('#holdings').jmtable({
		title:"Edit Holding",
		data:data,
		styles:styles,
		afterAdd:function(tr,root,item){
			tr.children().children("input").focus(function(){
				$(this).css({"border":"solid 1px #9FC78F"});
			});
			tr.children().children("input").blur(function(){
				$(this).css({"border":"solid 1px #FFFFFF"});
			});
			tr.children().children(".suggestsecurity").jsuggest({
				type:"security",
				select:function(event,ui){
					$.ajax({
						url: "/LTISystem/select_ajaxprice.action?includeHeader=false",
						dataType: "html",
						data: "term="+ui.item.value+"&date="+$('#startingdate').val(),
						success: function( data ) {
							tr.children().children(".price").val(data);
							tr.children().children(".share").val(tr.children().children(".amount").val()/data);
						},
						error:function(x, textStatus, errorThrown){
							alert(textStatus);
						}
					});
				}
			});
			
			tr.children().children(".riselect").append("<option value='true'>true</option><option value='false'>false</option>");
			
			tr.children().children(".amount").blur(function(){
				tr.children().children(".share").val(tr.children().children(".amount").val()/tr.children().children(".price").val());
			});
			
			//tr.children().children(".suggestassetclass").jsuggest({type:"assetclass"});
			tr.children().children(".acselect").append("<option value='1. EQUITY'>--EQUITY</option><option value='8. EQUITY -> US EQUITY'>&nbsp;&nbsp;&nbsp;&nbsp;++US EQUITY</option><option value='48. EQUITY -> US EQUITY -> US Large Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US Large Cap</option><option value='129. EQUITY -> US EQUITY -> US Large Cap -> LARGE VALUE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE VALUE</option><option value='197. EQUITY -> US EQUITY -> US Large Cap -> LARGE BLEND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE BLEND</option><option value='202. EQUITY -> US EQUITY -> US Large Cap -> LARGE GROWTH'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE GROWTH</option><option value='49. EQUITY -> US EQUITY -> US Mid Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US Mid Cap</option><option value='204. EQUITY -> US EQUITY -> US Mid Cap -> MID-CAP VALUE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MID-CAP VALUE</option><option value='205. EQUITY -> US EQUITY -> US Mid Cap -> MID-CAP BLEND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MID-CAP BLEND</option><option value='226. EQUITY -> US EQUITY -> US Mid Cap -> Mid-Cap Growth'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mid-Cap Growth</option><option value='52. EQUITY -> US EQUITY -> US Small Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US Small Cap</option><option value='207. EQUITY -> US EQUITY -> US Small Cap -> SMALL VALUE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMALL VALUE</option><option value='208. EQUITY -> US EQUITY -> US Small Cap -> SMALL BLEND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMALL BLEND</option><option value='231. EQUITY -> US EQUITY -> US Small Cap -> Small Growth'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Small Growth</option><option value='57. EQUITY -> US EQUITY -> Micro Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Micro Cap</option><option value='172. EQUITY -> US EQUITY -> US Equity Moderate Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;US Equity Moderate Allocation</option><option value='9. EQUITY -> INTERNATIONAL EQUITY'>&nbsp;&nbsp;&nbsp;&nbsp;++INTERNATIONAL EQUITY</option><option value='10. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Foreign Small Cap</option><option value='63. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small Cap -> Foreign Small Value'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Small Value</option><option value='58. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Developed Countries</option><option value='201. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries -> EUROPE STOCK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EUROPE STOCK</option><option value='232. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries -> Greece Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Greece Equity</option><option value='233. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries -> Europe Large-Cap Blend Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Europe Large-Cap Blend Equity</option><option value='243. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries -> JAPAN STOCK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JAPAN STOCK</option><option value='61. EQUITY -> INTERNATIONAL EQUITY -> Emerging Market'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Emerging Market</option><option value='62. EQUITY -> INTERNATIONAL EQUITY -> Emerging Market -> China Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;China Equity</option><option value='214. EQUITY -> INTERNATIONAL EQUITY -> Emerging Market -> DIVERSIFIED EMERGING MKTS'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DIVERSIFIED EMERGING MKTS</option><option value='229. EQUITY -> INTERNATIONAL EQUITY -> Emerging Market -> Latin America Stock'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Latin America Stock</option><option value='192. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small/Mid Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Foreign Small/Mid Cap</option><option value='130. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small/Mid Cap -> Foreign Small/Mid Value'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Small/Mid Value</option><option value='206. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small/Mid Cap -> FOREIGN SMALL/MID GROWTH'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FOREIGN SMALL/MID GROWTH</option><option value='193. EQUITY -> INTERNATIONAL EQUITY -> Pacific/Asia Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Pacific/Asia Equity</option><option value='239. EQUITY -> INTERNATIONAL EQUITY -> Pacific/Asia Equity -> DIVERSIFIED PACIFIC/ASIA'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DIVERSIFIED PACIFIC/ASIA</option><option value='240. EQUITY -> INTERNATIONAL EQUITY -> Pacific/Asia Equity -> PACIFIC/ASIA EX-JAPAN STK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PACIFIC/ASIA EX-JAPAN STK</option><option value='194. EQUITY -> INTERNATIONAL EQUITY -> Foreign Large Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Foreign Large Cap</option><option value='118. EQUITY -> INTERNATIONAL EQUITY -> Foreign Large Cap -> Foreign Large Growth'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Large Growth</option><option value='127. EQUITY -> INTERNATIONAL EQUITY -> Foreign Large Cap -> Foreign Large Blend'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Large Blend</option><option value='132. EQUITY -> INTERNATIONAL EQUITY -> Foreign Large Cap -> Foreign Large Value'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Large Value</option><option value='198. EQUITY -> INTERNATIONAL EQUITY -> WORLD STOCK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WORLD STOCK</option><option value='2. FIXED INCOME'>--FIXED INCOME</option><option value='11. FIXED INCOME -> US MUNICIPAL BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++US MUNICIPAL BONDS</option><option value='84. FIXED INCOME -> US MUNICIPAL BONDS -> Muni National Interm'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni National Interm</option><option value='86. FIXED INCOME -> US MUNICIPAL BONDS -> Muni National Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni National Short</option><option value='87. FIXED INCOME -> US MUNICIPAL BONDS -> Muni Single State Interm'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Single State Interm</option><option value='88. FIXED INCOME -> US MUNICIPAL BONDS -> Muni California Interm/Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni California Interm/Short</option><option value='89. FIXED INCOME -> US MUNICIPAL BONDS -> Muni Pennsylvania'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Pennsylvania</option><option value='90. FIXED INCOME -> US MUNICIPAL BONDS -> Muni Ohio'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Ohio</option><option value='92. FIXED INCOME -> US MUNICIPAL BONDS -> Muni New Jersey'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni New Jersey</option><option value='94. FIXED INCOME -> US MUNICIPAL BONDS -> Muni New York Interm/Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni New York Interm/Short</option><option value='96. FIXED INCOME -> US MUNICIPAL BONDS -> Muni Single State Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Single State Short</option><option value='101. FIXED INCOME -> US MUNICIPAL BONDS -> Muni Massachusetts'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Massachusetts</option><option value='103. FIXED INCOME -> US MUNICIPAL BONDS -> Muni New York Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni New York Long</option><option value='104. FIXED INCOME -> US MUNICIPAL BONDS -> Muni Single State Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Single State Long</option><option value='105. FIXED INCOME -> US MUNICIPAL BONDS -> Muni National Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni National Long</option><option value='106. FIXED INCOME -> US MUNICIPAL BONDS -> Muni Minnesota'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Minnesota</option><option value='114. FIXED INCOME -> US MUNICIPAL BONDS -> Muni California Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni California Long</option><option value='139. FIXED INCOME -> US MUNICIPAL BONDS -> High Yield Muni'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;High Yield Muni</option><option value='12. FIXED INCOME -> US CORPORATE BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++US CORPORATE BONDS</option><option value='26. FIXED INCOME -> US CORPORATE BONDS -> INVESTMENT GRADE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INVESTMENT GRADE</option><option value='27. FIXED INCOME -> US CORPORATE BONDS -> High Yield Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;High Yield Bond</option><option value='83. FIXED INCOME -> US CORPORATE BONDS -> Intermediate-Term Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate-Term Bond</option><option value='215. FIXED INCOME -> US CORPORATE BONDS -> Long-Term Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Long-Term Bond</option><option value='221. FIXED INCOME -> US CORPORATE BONDS -> Short-Term Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Short-Term Bond</option><option value='247. FIXED INCOME -> US CORPORATE BONDS -> ULTRASHORT BOND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ULTRASHORT BOND</option><option value='13. FIXED INCOME -> INTERNATIONAL BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++INTERNATIONAL BONDS</option><option value='28. FIXED INCOME -> INTERNATIONAL BONDS -> Foreign Goverment Bonds'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Goverment Bonds</option><option value='29. FIXED INCOME -> INTERNATIONAL BONDS -> Foreign Corporate Bonds'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Corporate Bonds</option><option value='30. FIXED INCOME -> INTERNATIONAL BONDS -> Foreign High Yield Bonds'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign High Yield Bonds</option><option value='195. FIXED INCOME -> INTERNATIONAL BONDS -> WORLD BOND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WORLD BOND</option><option value='220. FIXED INCOME -> INTERNATIONAL BONDS -> Emerging Markets Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Emerging Markets Bond</option><option value='41. FIXED INCOME -> US TREASURY BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++US TREASURY BONDS</option><option value='199. FIXED INCOME -> US TREASURY BONDS -> SHORT GOVERNMENT'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SHORT GOVERNMENT</option><option value='200. FIXED INCOME -> US TREASURY BONDS -> LONG GOVERNMENT'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LONG GOVERNMENT</option><option value='224. FIXED INCOME -> US TREASURY BONDS -> Intermediate Government'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate Government</option><option value='64. FIXED INCOME -> Money Market'>&nbsp;&nbsp;&nbsp;&nbsp;Money Market</option><option value='136. FIXED INCOME -> Bank Loan'>&nbsp;&nbsp;&nbsp;&nbsp;Bank Loan</option><option value='218. FIXED INCOME -> Multisector Bond'>&nbsp;&nbsp;&nbsp;&nbsp;Multisector Bond</option><option value='234. FIXED INCOME -> Inflation-Protected Bond'>&nbsp;&nbsp;&nbsp;&nbsp;Inflation-Protected Bond</option><option value='3. CASH ASSET'>--CASH ASSET</option><option value='15. CASH ASSET -> CASH'>&nbsp;&nbsp;&nbsp;&nbsp;CASH</option><option value='16. CASH ASSET -> CD'>&nbsp;&nbsp;&nbsp;&nbsp;CD</option><option value='4. HYBRID ASSETS'>--HYBRID ASSETS</option><option value='17. HYBRID ASSETS -> BALANCE FUND'>&nbsp;&nbsp;&nbsp;&nbsp;++BALANCE FUND</option><option value='65. HYBRID ASSETS -> BALANCE FUND -> Moderate Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Moderate Allocation</option><option value='66. HYBRID ASSETS -> BALANCE FUND -> Conservative Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Conservative Allocation</option><option value='99. HYBRID ASSETS -> BALANCE FUND -> World Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;World Allocation</option><option value='133. HYBRID ASSETS -> BALANCE FUND -> Retirement Income'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Retirement Income</option><option value='138. HYBRID ASSETS -> BALANCE FUND -> Target Date 2000-2010'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2000-2010</option><option value='141. HYBRID ASSETS -> BALANCE FUND -> Target Date 2011-2015'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2011-2015</option><option value='142. HYBRID ASSETS -> BALANCE FUND -> Target Date 2016-2020'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2016-2020</option><option value='143. HYBRID ASSETS -> BALANCE FUND -> Target Date 2021-2025'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2021-2025</option><option value='144. HYBRID ASSETS -> BALANCE FUND -> Target Date 2031-2035'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2031-2035</option><option value='145. HYBRID ASSETS -> BALANCE FUND -> Target Date 2041-2045'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2041-2045</option><option value='146. HYBRID ASSETS -> BALANCE FUND -> Target Date 2026-2030'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2026-2030</option><option value='147. HYBRID ASSETS -> BALANCE FUND -> Target Date 2036-2040'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2036-2040</option><option value='149. HYBRID ASSETS -> BALANCE FUND -> Target Date 2050+'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2050+</option><option value='19. HYBRID ASSETS -> PREFERRED SECURITIES'>&nbsp;&nbsp;&nbsp;&nbsp;PREFERRED SECURITIES</option><option value='20. HYBRID ASSETS -> CONVERTIBLE SECURITIES'>&nbsp;&nbsp;&nbsp;&nbsp;CONVERTIBLE SECURITIES</option><option value='209. HYBRID ASSETS -> Convertibles'>&nbsp;&nbsp;&nbsp;&nbsp;Convertibles</option><option value='5. REAL ESTATE'>--REAL ESTATE</option><option value='21. REAL ESTATE -> US Real Estate'>&nbsp;&nbsp;&nbsp;&nbsp;US Real Estate</option><option value='22. REAL ESTATE -> Non-US Real Estate'>&nbsp;&nbsp;&nbsp;&nbsp;Non-US Real Estate</option><option value='140. REAL ESTATE -> Global Real Estate'>&nbsp;&nbsp;&nbsp;&nbsp;Global Real Estate</option><option value='6. COMMODITIES'>--COMMODITIES</option><option value='246. COMMODITIES -> COMMODITIES BROAD BASKET'>&nbsp;&nbsp;&nbsp;&nbsp;COMMODITIES BROAD BASKET</option><option value='7. HEDGES'>--HEDGES</option><option value='78. Long-Short'>--Long-Short</option><option value='159. Alternative'>--Alternative</option><option value='79. Alternative -> Bear Market'>&nbsp;&nbsp;&nbsp;&nbsp;Bear Market</option><option value='97. Alternative -> Currency'>&nbsp;&nbsp;&nbsp;&nbsp;Currency</option><option value='160. Alternative -> Market Neutral'>&nbsp;&nbsp;&nbsp;&nbsp;Market Neutral</option><option value='162. SECTOR EQUITY'>--SECTOR EQUITY</option><option value='91. SECTOR EQUITY -> Precious Metals'>&nbsp;&nbsp;&nbsp;&nbsp;Precious Metals</option><option value='107. SECTOR EQUITY -> Miscellaneous'>&nbsp;&nbsp;&nbsp;&nbsp;Miscellaneous</option><option value='108. SECTOR EQUITY -> Utilities'>&nbsp;&nbsp;&nbsp;&nbsp;Utilities</option><option value='112. SECTOR EQUITY -> Health'>&nbsp;&nbsp;&nbsp;&nbsp;Health</option><option value='120. SECTOR EQUITY -> Communications'>&nbsp;&nbsp;&nbsp;&nbsp;Communications</option><option value='121. SECTOR EQUITY -> Natural Resources'>&nbsp;&nbsp;&nbsp;&nbsp;Natural Resources</option><option value='126. SECTOR EQUITY -> Technology'>&nbsp;&nbsp;&nbsp;&nbsp;Technology</option><option value='137. SECTOR EQUITY -> Financial'>&nbsp;&nbsp;&nbsp;&nbsp;Financial</option><option value='187. SECTOR EQUITY -> Consumer Staples'>&nbsp;&nbsp;&nbsp;&nbsp;Consumer Staples</option><option value='188. SECTOR EQUITY -> Consumer Discretionary'>&nbsp;&nbsp;&nbsp;&nbsp;Consumer Discretionary</option><option value='189. SECTOR EQUITY -> Industrials'>&nbsp;&nbsp;&nbsp;&nbsp;Industrials</option><option value='190. SECTOR EQUITY -> Energy'>&nbsp;&nbsp;&nbsp;&nbsp;Energy</option><option value='196. SECTOR EQUITY -> SPECIALTY-REAL ESTATE'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-REAL ESTATE</option><option value='222. SECTOR EQUITY -> Equity Energy'>&nbsp;&nbsp;&nbsp;&nbsp;Equity Energy</option><option value='235. SECTOR EQUITY -> SPECIALTY-UTILITIES'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-UTILITIES</option><option value='236. SECTOR EQUITY -> SPECIALTY-FINANCIAL'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-FINANCIAL</option><option value='237. SECTOR EQUITY -> SPECIALTY-HEALTH'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-HEALTH</option><option value='238. SECTOR EQUITY -> EQUITY PRECIOUS METALS'>&nbsp;&nbsp;&nbsp;&nbsp;EQUITY PRECIOUS METALS</option><option value='241. SECTOR EQUITY -> SPECIALTY-TECHNOLOGY'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-TECHNOLOGY</option><option value='242. SECTOR EQUITY -> SPECIALTY-NATURAL RES'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-NATURAL RES</option><option value='244. SECTOR EQUITY -> SPECIALTY-COMMUNICATIONS'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-COMMUNICATIONS</option><option value='245. SECTOR EQUITY -> SPECIALTY-PRECIOUS METALS'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-PRECIOUS METALS</option><option value='248. SECTOR EQUITY -> MISCELLANEOUS SECTOR'>&nbsp;&nbsp;&nbsp;&nbsp;MISCELLANEOUS SECTOR</option><option value='225. Capital Protected'>--Capital Protected</option>");
			
			tr.children().children(".suggeststrategy").jsuggest({
				type:"strategy",
				select:function(event,ui){
					item.children(".parameterdiv:first").children().remove();
					item.children(".parameterdiv:first").html("");
					item.children(".parameterdiv:first").jparameter({strategyname:ui.item.value,name:tr.children().children(".jparameter").attr('name')});
				}
			});
			
			tr.children().children(".jparameter").each(function(){
				item.append("<div class='parameterdiv' style='display:none'></div>");
			});
			
			tr.children().children(".jparameter").click(function(){
				var pdiv=item.children(".parameterdiv:first");
				pdiv.toggle();
			});
			
			
			tr.children().children(".strategycheck").click(function(){
				if(tr.children().children(".suggeststrategy").val()==''){
					alert("Please input the name of strategy.");
					return;
				}
				window.open("/LTISystem/jsp/strategy/View.action?name="+tr.children().children(".suggeststrategy").val(), "_blank");
			});
			
		},
		afterLoad:function(root,args){
			$(".jparameter").each(function(){
				
				var itemroot=$(this).parent().parent().parent().parent().parent();
				var strinput=$(this).parent().parent().children().children(".suggeststrategy:first");
				var asset=$(this).parent().parent().children().children(".assetname:first");
				itemroot.children(".parameterdiv:first").jparameter({strategyname:strinput.val(),storeddata:parameters[asset.val()],name:$(this).attr('name')});
				
			});
		}
	});
						
});
[/#if]
$(function(){
	$.ajax({   
           type:"POST",   
           url:'/LTISystem/jsp/portfolio/PermissionState_checkPublicState.action?ID=${portfolio.ID}&includeHeader=false',
           success: function(mesg){
           		var button1 = document.getElementById('btn_Public');
           		var button2 = document.getElementById('btn_Private');
           		if(mesg.indexOf('private')==-1){
           			button1.disabled=true;
           			button2.disabled=false;
           		}
           		else if(mesg.indexOf('public')==-1){
           			button2.disabled=true;
           			button1.disabled=false;
           		}
         	}
      }); 		
});
</script>

<script src="/LTISystem/jsp/template/gpl/editabletable.js"></script>
<script src="/LTISystem/jsp/template/gpl/multipletable.js"></script>
<script src="/LTISystem/jsp/template/gpl/jmtable.js"></script>
<script src="/LTISystem/jsp/template/gpl/jsuggest.js"></script>
<script src="/LTISystem/jsp/template/gpl/jparameter.js"></script>
<script src="/LTISystem/jsp/portfolio/images/jquery.timer.js" type="text/javascript"></script>
<script src="/LTISystem/jsp/template/gpl/jprocess.js"></script>
<style>
#holdings .item{
	border: 1.5px solid #9e9e9e;
	margin-top: 15px;
}
#holdings .item .control_bar{
	padding-top : 10px;
	padding-bottom : 10px;
}
#holdings input{
	border:solid 1px #FFFFFF;
	width:98%;
}
#holdings innertable,#holdings th,#holdings td{
	border: 1px solid #E0E0E0;
}
#holdings th{
	background-color : #f0f0f0;
	font-weight:normal;
}
.input2{
	height:30px;width:500px;
}

.jparameter{
	cursor:pointer;
	background-image:url(/LTISystem/jsp/portfolio/images/pa.png);
	background-repeat:no-repeat;
	height:25px;
	width:26px;
	margin-top:-3px;
	margin-left:-1px;
	border:0px;
	
}
.strategycheck{
	cursor:pointer;
	background-image:url(/LTISystem/jsp/portfolio/images/ch.png);
	background-repeat:no-repeat;
	height:25px;
	width:26px;
	margin-top:-3px;
	margin-left:-1px;
}

#pbi input{
	border: 1px solid #9FC78F;
	height: 15px;
	width:760px;
	margin: 0px;
	padding: 3px;
}
#pai2 input{
	border: 1px solid #9FC78F;
	height: 15px;
	width:400px;
	margin: 0px;
	padding: 3px;
}

#parameterdiv input{
	border: 1px solid #9FC78F;
	height: 15px;
	width:150px;
	margin: 0px;
	padding: 3px;
}
#pbi select{
	width:765px;
}
#pbi textarea{
	width:763px;
}
#pbi tr{
	height:30px;
}
</style>
</head>
<body>


<p>
	[#if owner || admin]<button id="btnAdvance">Advanced</button>&nbsp;[/#if]
	[#if admin]<button id="btnPermission">Permissions</button>&nbsp;[/#if]
	[#if personal]
	     [#if owner]
	       <button id="btn_Public" onclick="makePubOrPri('public');">Make Public</button>&nbsp;
		   <button id="btn_Private" onclick="makePubOrPri('private');">Make Private</button>&nbsp;
	     [/#if]
	[#else]
	   [#if admin]
		 <button id="btn_Public" onclick="makePubOrPri('public');">Make Public</button>&nbsp;
		 <button id="btn_Private" onclick="makePubOrPri('private');">Make Private</button>&nbsp;
	   [/#if]
	[/#if]
	
</p>

[#if admin]
 <div id="permissionpanel" style="display:none">
		<form id="permissionform">
		<table width=100%>
			<input type="hidden" name="ID" value="${ID}">
			<tr>
				<td>
					Delay Groups
				</td>
				<td width="400">
					<input name="delayGroup" value="${delayGroup!""}" style="width:100%">
				</td>
			</tr>
			<tr>
				<td>
					Realtime Groups

				</td>
				<td>
					<input name="realtimeGroup" value="${realtimeGroup!""}" style="width:100%">
				</td>
			</tr>
			<tr>
				<td colspan=2>
					<a href="javascript:void(0)" class="uiButton" id="btnChange">Change</a>
				</td>
			</tr>
			<tr>
				<td>Group Name</td>
				<td>
				MPIQ,MPIQ_B,ANONYMOUS,<br>
						IS_AUTHENTICATED_ANONYMOUSLY,<br>
						IS_AUTHENTICATED_REMEMBERED,VF_B<br>
				</td>
			</tr>
		</table>
		</form>
</div>
[/#if]

[#if owner || admin]


<div id="advancepanel" style="display:none;margin-top:5px;padding:5px;border:1px solid #E0E0E0">
	<table border=0 width=100%>
		<tr>
			<td width="80px">
				<h2>Stop Date</h2> 
			</td>
			<td>
				<input id="enddate" class="ui-widget ui-widget-content ui-corner-all" style="height:30px;width:250px">
			</td>
		</tr>
		<tr>
			<td colspan="2">
			<button class="uiButton" id="showMessage">Show Error Message</button>
			<button class="uiButton" id="btnMonitor">Monitor</button>
			<button class="uiButton" id="btnUpdate">Update</button>
			<button class="uiButton" id="btnRC">Re Construct</button>
			<button class="uiButton" id="btnStop">Stop</button>
			<a href="/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${ID}" target="_blank">Check</a>
			[#if isPlanPortfolio]
				<a href="/LTISystem/select_modify.action?portfolioID=${ID}" target="_blank">Modify</a>
			[/#if]
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type='checkbox' id='run_in_local_flag'>run on local
			</td>
		</tr>
	</table>
	<br>
	<table border=0 width=100%>
		<tr>
			<td width=50%>
				<div id="process_bar" style="">
				</div>
			</td>
			<td>
				<div id="process_message" style="">
				</div>
			</td>
		</tr>
	</table>
	
	<div id="error_message" style="display:none;width:100%;height:300px" >
	<iframe src='' id='message_iframe' style="width:100%;height:100%"></iframe>
	</div>
	
</div>
<br>
<iframe src='' style="width:0px;height:0px" id='execframe'></iframe>
[/#if]
<script>
	
$(function() {
	    $('#enddate').datepicker();
	    
	    [#if ID!=0]
		    $('#btnAdvance').button({
	            icons: {
	                primary: 'ui-icon-gear'
	            }
	        });
        [/#if]
        [#if owner || admin]
		    $('#btn_Public').button({
	            icons: {
	                primary: 'ui-icon-gear'
	            }
	        });
	        $('#btn_Private').button({
	            icons: {
	                primary: 'ui-icon-gear'
	            }
	        });
        [/#if]
        
        [#if admin]
        	$('#btnPermission').button({
	            icons: {
	                primary: 'ui-icon-gear'
	            }
	        });
        	$('#permissionpanel').dialog({title:"Permission",autoOpen:false,width:600,height:250});
        	$('#btnPermission').click(function(){
				$('#permissionpanel').dialog('open');
			});
			$('#btnChange').click(function(){
				 $.ajax({
					   type: "Post",
					   url: "SaveGroups.action?includeHeader=false",
					   data:$("#permissionform").serialize(),
					   success: function(msg){
					     alert(msg );
					   }
				 });
			});
        [/#if]
        
        
		$('#btnAdvance').click(function(){
			$('#advancepanel').toggle();
		});
	

	$('#showMessage').toggle(function(){
								var host=window.location.host;
								if($('#run_in_local_flag').attr('checked')){
									host='localhost';
								}
								//$('#error_message').load('http://'+host+':8081/Error?portfolioID=${ID?string.computer}&timestamp=-1&portfolioUpdate=1&endDate='+$('#enddate').val());
								$('#error_message').show();
								//$('#message_iframe').attr({'src':'http://localhost:8081/Error?portfolioID=${ID?string.computer}&timestamp=-1&portfolioUpdate=1&endDate='+$('#enddate').val()});
								$('#message_iframe').attr({'src':'http://'+host+':8081/Error?portfolioID=${ID?string.computer}&timestamp=-1&portfolioUpdate=1&endDate='+$('#enddate').val()});
							},
							function(){
								$('#error_message').hide();
							}
	);
	$('#btnMonitor').click(function(){  
		//if(!confirm('Are you sure to monitor this portfolio?')){
			//return;
		//}
		var host=window.location.host;
		if($('#run_in_local_flag').attr('checked')){
			host='localhost';
		}
		$('#process_message').html('Start to monitor...');
		var link='http://'+host+':8081/Execute?portfolioID=${ID?string.computer}&timestamp=-1&portfolioUpdate=1&endDate='+$('#enddate').val();
		sendRequest(link);
	});
	
	$('#btnUpdate').click(function(){
		if(!confirm('Are you sure to update this portfolio?')){
			return;
		}
		var host=window.location.host;
		if($('#run_in_local_flag').attr('checked')){
			host='localhost';
		}
		$('#process_message').html('Start to update...');
		var link='http://'+host+':8081/Execute?portfolioID=${ID?string.computer}&timestamp=-1&endDate='+$('#enddate').val();
		sendRequest(link);
	});
	
	$('#btnRC').click(function(){
		if(!confirm('Are you sure to re-construct this portfolio?')){
			return;
		}
		$('#process_message').html('Start to re-construct...');
		var host=window.location.host;
		if($('#run_in_local_flag').attr('checked')){
			host='localhost';
		}
		var link='http://'+host+':8081/Execute?portfolioID=${ID?string.computer}&portfolioUpdate=4&timestamp=-1&endDate='+$('#enddate').val();
		sendRequest(link);
	});
	
	function sendRequest(link){
		var s = document.createElement("SCRIPT");
	    s.id="request_node"; 
	    document.getElementsByTagName("HEAD")[0].appendChild(s);
		s.src=link+"&function=startProcess";
	}
	function sendRequest2(link){
		var s = document.createElement("SCRIPT");
	    s.id="request_node"; 
	    document.getElementsByTagName("HEAD")[0].appendChild(s);
		s.src=link+"&function=stopProcess";
	}
	
	
	
	$('#btnStop').click(function(){
		if(!confirm('Are you sure to stop this portfolio?')){
			return;
		}
		
		var host=window.location.host;
		if($('#run_in_local_flag').attr('checked')){
			host='localhost';
		}
		var link='http://'+host+':8081/Stop?portfolioID=${ID?string.computer}&timestamp=-1';
		sendRequest2(link);
	});
	[#-- --]
	$('#startingdate').datepicker();
	
	$('#acheck').click(function(){
		if($('#assetAllocationStrategy').val()=='')alert("Please input the name of strategy.");
		window.open("/LTISystem/jsp/strategy/View.action?name="+$('#assetAllocationStrategy').val(), "_blank");
	});
	
	$('#rcheck').click(function(){
		if($('#rebalancingStrategy').val()=='')alert("Please input the name of strategy.");
		window.open("/LTISystem/jsp/strategy/View.action?name="+$('#rebalancingStrategy').val(), "_blank");
	});
	
	$('#ccheck').click(function(){
		if($('#cashFlowStrategy').val()=='')alert("Please input the name of strategy.");
		window.open("/LTISystem/jsp/strategy/View.action?name="+$('#cashFlowStrategy').val(), "_blank");
	});
	
});  	
	function stopProcess(mess){
		document.getElementsByTagName("HEAD")[0].removeChild(document.getElementById("request_node")); 
		if(mess=="ok"){
			alert("The simulation has been stopped.");
			return;
		}
	}
	function startProcess(mess){
		document.getElementsByTagName("HEAD")[0].removeChild(document.getElementById("request_node")); 
		if(mess!="ok"){
			$('#process_message').html('the execution may be unsuccessful.<a href="/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${ID?string.computer}">Click here the view the portfolio</a>');
			return;
		}
		$('#process_bar').jprocess({
			id:${ID?string.computer},
			onChange:function(result){
				if(result<0){
					$('#process_message').html('try to get simulation information['+_jprocess_error_times+']');
				}
				if(_jprocess_error_times>40){
					$('#process_message').html('the execution may be unsuccessful.<a href="/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${ID?string.computer}">Click here the view the portfolio</a>');
				}else if(result!=100){
					$('#process_message').html('Performing Historical Simulation on the Portfolio ... '+result+'%');
					
				}
				if(result==100){
					$('#process_message').html('Simulation Finish.');
					window.location.href='/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${ID?string.computer}';
				}
			}
		});
	}
	[#if owner || admin]
	function makepub(obj){
		var button1 = document.getElementById('btn_Public');
        var button2 = document.getElementById('btn_Private');
		if(obj=='public'){
			$.ajax({
				type: "post",
				url: "/LTISystem/jsp/portfolio/MakePublicPortfolio.action?ID=${ID?string.computer}&action=public&includeHeader=false",
				datatype: "html",
				success: function(result){
					if(result.indexOf("fail")==-1){
						alert("You have made this portfolio public.");
						button1.disabled=true;
						button2.disabled=false;
					    window.location.reload();
					}
			  		else alert("Your operation is fail.Please try again!");
				}
			})
		}
		if(obj=='private'){
			$.ajax({
				type: "post",
				url: "/LTISystem/jsp/portfolio/MakePublicPortfolio.action?ID=${ID?string.computer}&action=private&includeHeader=false",
				datatype: "html",
				success: function(result){
					if(result.indexOf("fail")==-1){
						alert("You have made this portfolio private.");
						button2.disabled=true;
						button1.disabled=false;
					    window.location.reload();
					}
			  		else alert("Your operation is fail.Please try again!");
				}
			})
		}
	}
	
	function makePubOrPri(obj){
	    var button1 = document.getElementById('btn_Public');
        var button2 = document.getElementById('btn_Private');
        $.ajax({
            type:"post",
            url:"/LTISystem/jsp/portfolio/edit_makepublic.action?includeHeader=false&ID=${ID?string.computer}&type="+obj,
            datatype:"html",
            success:function(result){
                if(result.indexOf("fail")==-1){
						alert("You have made this portfolio "+obj+".");
						if(obj=="public"){
						  button1.disabled=true;
						  button2.disabled=false;
						}else{
						  button2.disabled=true;
						  button1.disabled=false;
						}
						
					    window.location.reload();
					}
			  		else alert("Your operation is fail.Please try again!");
            }
        });
	}
	[/#if]
	//$('#advancepanel input').addClass("ui-widget ui-widget-content ui-corner-all");
</script>

<form action="SaveHolding.action" method="post" id="saveform">
 <div class="sidebar_box_noPadding roundHeadingGreen" id="pbi">
	<div class="sidebar_box_heading_white">Portfolio Basic Information</div>
	<table width="100%" style="margin:10px">
			<input type='hidden'  name='ID' value='${ID?string.computer}'>
			<input type='hidden' id='operation' name='operation' value=''>
			<tr>
				<td width="100px">
					<b>Name</b>
				</td>
				<td>
					<input type='text' id="name" name='portfolio.name' value='${portfolio.name!""}' >
					<input type='hidden' id="oldname"  value='${portfolio.name!""}'>
				</td>
			</tr>
			<tr>
				<td>
					<b>Categories</b>
				</td>
				<td>
					<input type='text'  name='portfolio.categories' value='${portfolio.categories!""}'>
				</td>
			</tr>
			
			<tr>
				<td>
					<b>Production</b>
				</td>
				<td>
					<select name='isProduction'  >
						<option value="true" [#if isProduction]selected[/#if]>true</option>
						<option value="false" [#if !isProduction]selected[/#if]>false</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<b>Starting Date</b>
				</td>
				<td>
					<input type='text' id='startingdate'  name='portfolio.startingDate' value='[#if portfolio.startingDate??]${portfolio.startingDate?string("MM/dd/yyyy")}[/#if]' >
				</td>
			</tr>
			[#if admin]
			<tr>
				<td>
					<b>Owner</b>
				</td>
				<td>
					${portfolio.userName!"-"}[${portfolio.userID!"-"}]	
				</td>
			</tr>
			[/#if]
			[#if (admin|| owner) && portfolio.mainStrategyID??]
			<tr>
				<td>
					<b>Belong to</b>
				</td>
				<td>
					<a href="/LTISystem/jsp/strategy/View.action?ID=${portfolio.mainStrategyID}">${portfolio.mainStrategyName!"check"}</a>	
				</td>
			</tr>
			[/#if]
			<tr>
				<td valign="top"><b>Description</b></td>
				<td>
					<textarea name='portfolio.description' id='description' style="height:300px">[#if portfolio.description??]${portfolio.description?html}[/#if]</textarea>
				</td>
			</tr>
	
	</table>
 </div>
<input type="hidden" name="personal" value="[#if personal??]${personal?string}[/#if]">
[#if ID!=0 && personal]
<div class="sidebar_box_noPadding roundHeadingGreen" id="pai">
	<div class="sidebar_box_heading_white">Transactions</div>
		<div style="padding:15px;font-family: 'Courier New', Courier, monospace;">
<pre>
Example 1:
buy         spy       01/02/2003  4000      88.23     352920    
sell        spy       07/01/2003  3000      98.53
buy         vfinx     07/01/2003  4000

Example 2:
buy         spy       01/02/2003  share4000      price88.23     352920    
sell        spy       07/01/2003  share3000      price98.53
buy         vfinx     07/01/2003  share4000

Example 3:
buy         spy       01/02/2003  shr4000      price88.23     amnt352920    
sell        spy       07/01/2003  share 3000      pr98.53     amount 295590    
buy         vfinx     07/01/2003  pr90.75     amt363000    


</pre>
<br>
<br>
<table border=0>
	<tr height=30>
		<td style="font-weight:900;font-family:arial;"><span style="font-weight:900;">Initial amount</span>&nbsp;&nbsp;&nbsp;</td>
		<td><input name="holding.cash" type="text" value="[#if holding??]${holding.cash?string.computer}[#else]10000.0[/#if]"></td>
	</tr>
</table>


			<textarea name="personalTransactions" id="personalTransactions" style="border:1px #ccc solid;height:300px;width:100%;font-family: 'Lucida Console', Monaco, monospace;">[#if personalTransactions??]${personalTransactions?html}[/#if]</textarea>
		</div>
	</div>
<div>
[/#if]
[#if ID!=0 && !personal]
<div class="sidebar_box_noPadding roundHeadingGreen" id="pai">
	<div class="sidebar_box_heading_white">Portfolio Strategy Information</div>
	<table width=100% style="margin:10px" id="pai2">
		<tr>
			<td>
				<b>Cash</b>
			</td>
			<td>
				<input type='text'  name='holding.cash' value='${holding.cash?string.computer}'>
			</td>
		</tr>
		[#-- Asset allocation strategy 注意很多javascript --]
		<tr>
			<td>
				<b>Asset Allocation Strategy</b>
			</td>
			<td>
				<input type='text'  name='holding.assetAllocationStrategy' id='assetAllocationStrategy' value="${holding.assetAllocationStrategy}" >
				<a href="javascript:void(0)" id="parameter" >Parameter</a>
				<a href="javascript:void(0)" id="acheck" >Check</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="parameterwindow" style="display:none">
				</div>
			</td>
		</tr>
		
		[#-- Rebalancing strategy 注意很多javascript --]
		<tr>
			<td>
				<b>Rebalancing Strategy</b>
			</td>
			<td>
				<input type='text'  name='holding.rebalancingStrategy' id='rebalancingStrategy' value='${holding.rebalancingStrategy!""}' >
				<a href="javascript:void(0)" id="parameter_r" >Parameter</a>
				<a href="javascript:void(0)" id="rcheck" >Check</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="parameterwindow_r" style="display:none">
				</div>
			</td>
		</tr>
		
		[#-- Cash Flow strategy 注意很多javascript --]
		<tr>
			<td>
				<b>Cash Flow Strategy</b>
			</td>
			<td>
				<input type='text'  name='holding.cashFlowStrategy' id='cashFlowStrategy' value='${holding.cashFlowStrategy!""}'>
				<a href="javascript:void(0)" id="parameter_c" >Parameter</a>
				<a href="javascript:void(0)" id="ccheck" >Check</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="parameterwindow_c" style="display:none">
				</div>
			</td>
		</tr>
		
		<tr>
			<td>
				<b>BenchmarkSymbol</b>
			</td>
			<td>
				<input type='text'  name='holding.benchmarkSymbol' id='benchmark' value='${holding.benchmarkSymbol!""}' >
			</td>
		</tr>
	</table>
	
	<div id="parameterdiv" style="margin:10px">
		<b>Attributes</b>
		<table width=400px id="parametertable" style="border:solid #F0F8FF 1px;margin:5px">
			<thead>
			<tr>
				<th width="30%">
					Name
				</th>
				<th>
					Value
				</th>
			</tr>
			</thead>
			<tbody>
			[#if attributes??]
			[#list attributes as p]
			<tr>
				<td>
					<input type='text' name='attributes[${p_index}].pre' value='${p.pre!""}'>
				</td>
				<td>
					<input type='text' name='attributes[${p_index}].post' value='${p.post!""}'>
				</td>
			</tr>
			[/#list]
			[/#if]
			
			</tbody>
		</table>
	</div>
</div>

<div class="sidebar_box_noPadding roundHeadingGreen" >
	<div class="sidebar_box_heading_white">Holdings</div>
	<div id="holdings" style="margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:10px"></div>
	
</div>
<p>
	<b>TA*</b>: Target Allocation
	<b>PA*</b>: Parameter
	<b>CH*</b>: Check
	<b>DR*</b>: Dividend Re-invest
</p>
[#if admin]<p><a href="formatholding.action?ID=${ID?string.computer}">Batch Input</a></p>[/#if]
[/#if]


<br>
[#if admin || owner || ID==0]
<input type="button" class="uiButton" value="Save" onclick="save()">
[/#if]
[#if admin || owner]
[#if ID!=0]
<input type="button" class="uiButton" value="Delete" onclick="remove()">
[/#if]
[/#if]
[#if hasSimulateRole || admin]
<input type="button" class="uiButton" value="Save as" onclick="saveas()">
[/#if]
<a  class="uiButton" href="/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=${ID}" target="_blank">Go Back</a>
</form>



<script>
[#if compound??&&compound=='0']
$(function(){
	$('#advancepanel').toggle();   		
	$('#btnMonitor').trigger('click'); 
})
    	
[/#if]
</script>
</body>
</html>
