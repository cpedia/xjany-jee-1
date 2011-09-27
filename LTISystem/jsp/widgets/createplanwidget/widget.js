//装载widget的主函数
//如果参数中包含simple则仅构造搜索结果
//否则将包含搜索框
//planwidget
var notFandSymbols="";
function mpiq_widget_${widgetName}(mpiq_id,mpiq_params,id,isCopy){
	if(typeof jQuery.autocomplete == "undefined"){
		var _doc = document.getElementsByTagName('head')[0];
	    var js = document.createElement('script');
	    js.setAttribute('type', 'text/javascript');
	    js.setAttribute('src', "http://${serverName}:${port}/LTISystem/jsp/widgets/js/jquery-ui-1.8.custom.min.js");
	    _doc.appendChild(js);
	    if (!/*@cc_on!@*/0) { //if not IE
	        //Firefox2、Firefox3、Safari3.1+、Opera9.6+ support js.onload
	        js.onload = function () {
	            $(function(){
	            	mpiq_widget_${widgetName}_init(mpiq_id,mpiq_params,id,isCopy);
	            });
	        }
	    } else {
	        //IE6、IE7 support js.onreadystatechange
	        js.onreadystatechange = function () {
	            if (js.readyState == 'loaded' || js.readyState == 'complete') {
	               $(function(){
		            	mpiq_widget_${widgetName}_init(mpiq_id,mpiq_params,id,isCopy);
		            	
		            });
	            }
	        }
	    }
	    
	    var d = document.createElement("link");
		d.rel = "stylesheet";
		d.rev = "stylesheet";
		d.type = "text/css";
		d.media = "screen";
		//d.href = "http://${serverName}:${port}/LTISystem/jsp/template/ed/css/jquery_UI/smoothness/jquery-ui-1.8.custom.css";
		document.getElementsByTagName("head")[0].appendChild(d);
	}else{
		$(function(){
        	mpiq_widget_${widgetName}_init(mpiq_id,mpiq_params,id,isCopy);
        });
	}
}

function mpiq_widget_${widgetName}_init(mpiq_id,mpiq_params,id,isCopy){
	isMore = false;
	$("#"+mpiq_id).html("${ui}");
	mpiq_widget_${widgetName}_rework();
	if(typeof window.mpiq_widget_${widgetName}_callback != 'undefined'){
		mpiq_widget_${widgetName}_callback($("#"+mpiq_id).html());
	}
	if(id!=null||id!=undefined){
		if(isCopy)
			mpiq_widget_${widgetName}_update_plan(id,true);
		else
			mpiq_widget_${widgetName}_update_plan(id,false);
	}
	else
		$("#createplan_mp_heardname").css("display","")
}

function mpiq_widget_${widgetName}_load_basic_inf_from_server(mpiq_id,tid,mpiq_params){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/ticker.action?"+mpiq_params;
	mpiq_load_data_from_server(url,function(data){
		
		
	});
}

function mpiq_widget_${widgetName}_edit_text(element){
	$(element).removeClass('mpiq_show_text');
	$(element).addClass('mpiq_edit_text');
}

function mpiq_widget_${widgetName}_add_column(){
	var th;
	th = "<th id='mpiq_widget_${widgetName}_overhere' class='mpiq_header' key='description' width='30'><b>Description</b></th>"
	$("#mpiq_widget_${widgetName}_overhere").after(th);
}

function mpiq_widget_${widgetName}_add_item(self,id,vals,modifi){
	//$("#mpiq_widget_${widgetName}_fund_table${time}").append($(self).parent().next().clone());
	var headers=$(self).parent().parent().children();
	if(vals!=null&&vals[headers.eq(0).attr("key")]==null){
		if(notFandSymbols==""){
			notFandSymbols = vals[headers.eq(4).attr("key")];
		}else
			notFandSymbols = notFandSymbols+"|"+vals[headers.eq(4).attr("key")];
//		alert(notFandSymbols);
		if(modifi==null)
			return;
	}
	if(modifi!=null){
		//var tr="<tr>";
		var tr="";
		for(i=0;i<headers.length;i++){
			var key=headers.eq(i).attr("key");
			if(!isMore && i>4 && i!=9){
				tr+="<td style='display: none;'><textarea  id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)'>"+vals[key]+"</textarea></td>";
			}else if(i==9)
				tr+="<td><input type='button' value='delete' onclick='mpiq_widget_${widgetName}_remove_item(this)' class='button_remove'></td>";
			else if(i==1)
				tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' onblur='mpiq_widget_${widgetName}_out_text(this)'>"+vals[key]+"</textarea></td>";
			else
				tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)'>"+vals[key]+"</textarea></td>";
		}
		//tr+="</tr>";
		$(id).parent().parent().html(tr); 
	}else{
		var tr="<tr>";
		for(i=0;i<headers.length;i++){
			if(!isMore && i>4 && i!=9){
				tr+="<td style='display: none;'><textarea  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' onblur='mpiq_widget_${widgetName}_out_text(this)'></textarea></td>";
			}else if(i==9)
				tr+="<td><input type='button' value='delete' onclick='mpiq_widget_${widgetName}_remove_item(this)' class='button_remove'></td>";
			else if(i==1)
				tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' onblur='mpiq_widget_${widgetName}_out_text(this)'></textarea></td>";
			else
				tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' ></textarea></td>";
		}
		tr+="</tr>";
		$("#mpiq_widget_${widgetName}_fund_table${time}").append(tr);
		//alert($("#mpiq_widget_${widgetName}_fund_table${time} tr").eq(1).children().eq(6).css("display","none"));
		for(i=0;i<headers.length-1;i++){
			var key=headers.eq(i).attr("key");
//			alert(vals[headers.eq(4).attr("key")]);
			if(vals!=undefined&&vals!=null){
				$("#mpiq_widget_${widgetName}_fund_table${time} tr:last").children().eq(i).children().val(vals[key]);
			}
			if(key!=undefined&&key!=null&&key=="ticker"){
				var ticker=$("#mpiq_widget_${widgetName}_fund_table${time} tr:last").children().eq(i).children();
			}
		}
	}
	
	
	
	ticker.autocomplete({
			minLength: 2,
			source: function( request, response ) {
				$.ajax({
					url: "http://${serverName}:${port}/LTISystem/widgets/createplanwidget/ticker.action",
					dataType: "jsonp",
					data: {
						size: 6,
						ticker: request.term
					},
					success: function( data ) {
						response( $.map( data.tickers, function( item ) {
							return {
								label: item.description,
								value: item.ticker
							}
						}));
					},
					error:function(){
						alert("error");
					}
				});
			},
			open: function (){
				//alert(jQuery(".mpiq_autocomplete").html());
				jQuery(".mpiq_autocomplete").removeClass( "ui-corner-all" );
				jQuery(".mpiq_autocomplete li a").removeClass('ui-corner-all');
			}
			
	}).data( "autocomplete" )._renderItem = function( ul, item ) {
			ul.addClass("mpiq_autocomplete");
			var regS = new RegExp("("+ticker.val()+")","i");
			return $( "<li></li>" )
				.data( "item.autocomplete", item )
				.append( "<a><b>" + item.value.replace(regS, "<span style='color:red'>$1</span>") + "</b><br>" + item.label.replace(regS, "<span style='color:red'>$1</span>") + "</a>" )
				.appendTo( ul );
	};
	if(vals!=undefined&&vals!=null){
	}else{
		mpiq_widget_${widgetName}_rework();
	}
	
}

function mpiq_widget_${widgetName}_remove_item(self){
	//$("#mpiq_widget_${widgetName}_fund_table${time}").remove();
	$(self).parent().parent().remove();
	
	mpiq_widget_${widgetName}_rework();
}

function mpiq_widget_${widgetName}_rework(){
	$("#mpiq_widget_${widgetName}_fund_table${time} tr:odd").removeClass("even");
	$("#mpiq_widget_${widgetName}_fund_table${time} tr:odd").addClass("odd");
	
	$("#mpiq_widget_${widgetName}_fund_table${time} tr:even").removeClass("odd");
	$("#mpiq_widget_${widgetName}_fund_table${time} tr:even").addClass("even");
}
function mpiq_widget_${widgetName}_clear(){
	var trs=$("#mpiq_widget_${widgetName}_fund_table${time} tr");
	for(trs_i=1;trs_i<trs.length;trs_i++){
		trs.eq(trs_i).remove();
	}
}

function mpiq_widget_${widgetName}_show(){
	$(".mp_container").css("opacity","0.3");
	$('#mp_createplan').css("display","block");
}
function mpiq_widget_${widgetName}_close(){
	$(".mp_container").css("opacity","1");
	$('#mp_createplan').css("display","none");
}
/*
 * id planID
 * isInner 是否页面调用widget
 * isModerpor 是否需要moderPortfolio
 * isCopy 是否从页面拷贝的plan
 */
function mpiq_widget_${widgetName}_save(id,isInner,isModerpor,isCopy){
	var comm=$("#mpiq_widget_${widgetName}_comment").val();
	if(comm.length>1&&comm.length<6){
		alert("Comment text should be more than 6 characters.");
		return;
	}
	var planname=$("#mpiq_widget_${widgetName}_planname").val();
	
	if(id==undefined && (planname==""||planname=="Please input the plan name here")){
		alert("Please input the plan name!");
		return;
	}

	var headers=$("#mpiq_widget_${widgetName}_fund_table${time} th");
	var trs=$("#mpiq_widget_${widgetName}_fund_table${time} tr");
	var data="..";
	for(i=0;i<headers.length-1;i++){
		data+=headers.eq(i).attr("key");
		data+=",,";
	}
	data+="..";
	if(trs.length<2){
		alert("Please input the funds.");
		return ;
	}
	//str.replace(/Microsoft/g, "W3School");
	for(i=1;i<trs.length;i++){
		var tds=trs.eq(i).children();
		for(j=0;j<tds.length-1;j++){
			data+=tds.children().eq(j).val();
			//alert(data);
			data+=",,";
		}
		data+="..";
	}
	data=data.replace(/%/g,"%25").replace(/&/g, "%26");
	//if($("#mp_checkbox").attr("checked")==true){
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/createplan.action?planname="+planname+"&help='help me'"+"&data="+data+"&postText="+comm+"&description="+$("#mpiq_widget_${widgetName}_description${time}").val()+"&id="+id+ "&isCopy="+ isCopy+ "&isModer="+ isModerpor;
	//}else
	//	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/createplan.action?planname="+planname+"&data="+data+"&postText="+comm+"&description="+$("#mpiq_widget_${widgetName}_description${time}").val()+"&id="+id+ "&isCopy="+ isCopy+ "&isModer="+ isModerpor;
	mpiq_load_data_from_server(url,function(data1){
		if(typeof window.mpiq_widget_${widgetName}_save_callback != 'undefined'){
			if(isInner==undefined||isInner==false)
				mpiq_widget_${widgetName}_save_callback(data1,id);
			else if(isModerpor){
				alert("successfully");
				window.location="/LTISystem/f401k_view.action?ID="+data1.id;
			}else
				alert("successfully");
		}else{
			if(dat1a.code!=undefined&&data1.code==400){
				if(isModerpor){
					alert("successfully");
					window.location="/LTISystem/f401k_view.action?ID="+data1.id;
				}else
					alert("successfully");
			}else{
				alert(data1.msg);
			}
		}
				
	});	
}

function mpiq_widget_${widgetName}_out_text(element){
	$(element).removeClass('mpiq_edit_text');
	$(element).addClass('mpiq_show_text');
	var symbol=$(element).val();
	mpiq_widget_${widgetName}_auto_add(symbol,$(element));
}

function mpiq_widget_${widgetName}_auto_add(addtickers,id){
	mpiq_widget_${widgetName}_b_process(addtickers,function(data){
		for(ii=0;ii<data.size;ii++){
			//$("#mpiq_widget_${widgetName}_btnadd").trigger("click");
			mpiq_widget_${widgetName}_add_item(document.getElementById("mpiq_widget_${widgetName}_btnadd"),id,data.items[ii],"dd");
		}
		mpiq_widget_${widgetName}_rework();
	});
}

//批量处理并添加
function mpiq_widget_${widgetName}_b_add(addtickers){
	mpiq_widget_${widgetName}_b_process(addtickers,function(data){
		for(ii=0;ii<data.size;ii++){
			//$("#mpiq_widget_${widgetName}_btnadd").trigger("click");
			mpiq_widget_${widgetName}_add_item(document.getElementById("mpiq_widget_${widgetName}_btnadd"),"",data.items[ii]);
		}
		mpiq_widget_${widgetName}_rework();
		//alert(data.size);
		mpiq_widget_${widgetName}_getBenchMarks(document.getElementById("mpiq_widget_${widgetName}_bench"));
	});
}

//查找后没有结果的处理
function mpiq_widget_${widgetName}_getBenchMarks(self){
	if(notFandSymbols=="") return;
	$("#mp_getBenchMarks").css("display","block");
	var headers=$(self);
	headers.html("");
	var lines = notFandSymbols.split('|');
	var tr = "";
	for(var i=0;i<lines.length;i++){
		tr +="<tr><td><select id='i"+i+"_0' onChange='getMessage("+i+")'>";
		tr +="<option value='-1.NotFound'>--Asset Class</option>";
		tr +="<option value='1.EQUITY'>--EQUITY</option><option value='8. EQUITY ->US EQUITY'>&nbsp;&nbsp;&nbsp;&nbsp;++US EQUITY</option><option value='48. EQUITY -> US EQUITY ->US Large Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US Large Cap</option><option value='129. EQUITY -> US EQUITY -> US Large Cap ->LARGE VALUE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE VALUE</option><option value='197. EQUITY -> US EQUITY -> US Large Cap ->LARGE BLEND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE BLEND</option><option value='202. EQUITY -> US EQUITY -> US Large Cap ->LARGE GROWTH'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LARGE GROWTH</option><option value='49. EQUITY -> US EQUITY ->US Mid Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US Mid Cap</option><option value='204. EQUITY -> US EQUITY -> US Mid Cap ->MID-CAP VALUE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MID-CAP VALUE</option><option value='205. EQUITY -> US EQUITY -> US Mid Cap ->MID-CAP BLEND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MID-CAP BLEND</option><option value='226. EQUITY -> US EQUITY -> US Mid Cap ->Mid-Cap Growth'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mid-Cap Growth</option><option value='52. EQUITY -> US EQUITY ->US Small Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+US Small Cap</option><option value='207. EQUITY -> US EQUITY -> US Small Cap ->SMALL VALUE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMALL VALUE</option><option value='208. EQUITY -> US EQUITY -> US Small Cap ->SMALL BLEND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SMALL BLEND</option><option value='231. EQUITY -> US EQUITY -> US Small Cap ->Small Growth'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Small Growth</option><option value='57. EQUITY -> US EQUITY ->Micro Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Micro Cap</option><option value='172. EQUITY -> US EQUITY ->US Equity Moderate Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;US Equity Moderate Allocation</option><option value='9. EQUITY ->INTERNATIONAL EQUITY'>&nbsp;&nbsp;&nbsp;&nbsp;++INTERNATIONAL EQUITY</option><option value='10. EQUITY -> INTERNATIONAL EQUITY ->Foreign Small Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Foreign Small Cap</option><option value='63. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small Cap ->Foreign Small Value'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Small Value</option><option value='58. EQUITY -> INTERNATIONAL EQUITY ->Developed Countries'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Developed Countries</option><option value='201. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries ->EUROPE STOCK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EUROPE STOCK</option><option value='232. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries ->Greece Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Greece Equity</option><option value='233. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries ->Europe Large-Cap Blend Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Europe Large-Cap Blend Equity</option><option value='243. EQUITY -> INTERNATIONAL EQUITY -> Developed Countries ->JAPAN STOCK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JAPAN STOCK</option><option value='61. EQUITY -> INTERNATIONAL EQUITY ->Emerging Market'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Emerging Market</option><option value='62. EQUITY -> INTERNATIONAL EQUITY -> Emerging Market ->China Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;China Equity</option><option value='214. EQUITY -> INTERNATIONAL EQUITY -> Emerging Market ->DIVERSIFIED EMERGING MKTS'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DIVERSIFIED EMERGING MKTS</option><option value='229. EQUITY -> INTERNATIONAL EQUITY -> Emerging Market ->Latin America Stock'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Latin America Stock</option><option value='192. EQUITY -> INTERNATIONAL EQUITY ->Foreign Small/Mid Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Foreign Small/Mid Cap</option><option value='130. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small/Mid Cap ->Foreign Small/Mid Value'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Small/Mid Value</option><option value='206. EQUITY -> INTERNATIONAL EQUITY -> Foreign Small/Mid Cap ->FOREIGN SMALL/MID GROWTH'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FOREIGN SMALL/MID GROWTH</option><option value='193. EQUITY -> INTERNATIONAL EQUITY ->Pacific/Asia Equity'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Pacific/Asia Equity</option><option value='239. EQUITY -> INTERNATIONAL EQUITY -> Pacific/Asia Equity ->DIVERSIFIED PACIFIC/ASIA'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DIVERSIFIED PACIFIC/ASIA</option><option value='240. EQUITY -> INTERNATIONAL EQUITY -> Pacific/Asia Equity ->PACIFIC/ASIA EX-JAPAN STK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PACIFIC/ASIA EX-JAPAN STK</option><option value='194. EQUITY -> INTERNATIONAL EQUITY ->Foreign Large Cap'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+Foreign Large Cap</option><option value='118. EQUITY -> INTERNATIONAL EQUITY -> Foreign Large Cap ->Foreign Large Growth'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Large Growth</option><option value='127. EQUITY -> INTERNATIONAL EQUITY -> Foreign Large Cap ->Foreign Large Blend'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Large Blend</option><option value='132. EQUITY -> INTERNATIONAL EQUITY -> Foreign Large Cap ->Foreign Large Value'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Large Value</option><option value='198. EQUITY -> INTERNATIONAL EQUITY ->WORLD STOCK'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WORLD STOCK</option><option value='2. FIXED INCOME'>--FIXED INCOME</option><option value='11. FIXED INCOME ->US MUNICIPAL BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++US MUNICIPAL BONDS</option><option value='84. FIXED INCOME -> US MUNICIPAL BONDS ->Muni National Interm'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni National Interm</option><option value='86. FIXED INCOME -> US MUNICIPAL BONDS ->Muni National Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni National Short</option><option value='87. FIXED INCOME -> US MUNICIPAL BONDS ->Muni Single State Interm'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Single State Interm</option><option value='88. FIXED INCOME -> US MUNICIPAL BONDS ->Muni California Interm/Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni California Interm/Short</option><option value='89. FIXED INCOME -> US MUNICIPAL BONDS ->Muni Pennsylvania'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Pennsylvania</option><option value='90. FIXED INCOME -> US MUNICIPAL BONDS ->Muni Ohio'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Ohio</option><option value='92. FIXED INCOME -> US MUNICIPAL BONDS ->Muni New Jersey'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni New Jersey</option><option value='94. FIXED INCOME -> US MUNICIPAL BONDS ->Muni New York Interm/Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni New York Interm/Short</option><option value='96. FIXED INCOME -> US MUNICIPAL BONDS ->Muni Single State Short'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Single State Short</option><option value='101. FIXED INCOME -> US MUNICIPAL BONDS ->Muni Massachusetts'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Massachusetts</option><option value='103. FIXED INCOME -> US MUNICIPAL BONDS ->Muni New York Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni New York Long</option><option value='104. FIXED INCOME -> US MUNICIPAL BONDS ->Muni Single State Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Single State Long</option><option value='105. FIXED INCOME -> US MUNICIPAL BONDS ->Muni National Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni National Long</option><option value='106. FIXED INCOME -> US MUNICIPAL BONDS ->Muni Minnesota'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni Minnesota</option><option value='114. FIXED INCOME -> US MUNICIPAL BONDS ->Muni California Long'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Muni California Long</option><option value='139. FIXED INCOME -> US MUNICIPAL BONDS ->High Yield Muni'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;High Yield Muni</option><option value='12. FIXED INCOME ->US CORPORATE BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++US CORPORATE BONDS</option><option value='26. FIXED INCOME -> US CORPORATE BONDS ->INVESTMENT GRADE'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INVESTMENT GRADE</option><option value='27. FIXED INCOME -> US CORPORATE BONDS ->High Yield Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;High Yield Bond</option><option value='83. FIXED INCOME -> US CORPORATE BONDS ->Intermediate-Term Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate-Term Bond</option><option value='215. FIXED INCOME -> US CORPORATE BONDS ->Long-Term Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Long-Term Bond</option><option value='221. FIXED INCOME -> US CORPORATE BONDS ->Short-Term Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Short-Term Bond</option><option value='247. FIXED INCOME -> US CORPORATE BONDS ->ULTRASHORT BOND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ULTRASHORT BOND</option><option value='13. FIXED INCOME ->INTERNATIONAL BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++INTERNATIONAL BONDS</option><option value='28. FIXED INCOME -> INTERNATIONAL BONDS ->Foreign Goverment Bonds'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Goverment Bonds</option><option value='29. FIXED INCOME -> INTERNATIONAL BONDS ->Foreign Corporate Bonds'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign Corporate Bonds</option><option value='30. FIXED INCOME -> INTERNATIONAL BONDS ->Foreign High Yield Bonds'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Foreign High Yield Bonds</option><option value='195. FIXED INCOME -> INTERNATIONAL BONDS ->WORLD BOND'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;WORLD BOND</option><option value='220. FIXED INCOME -> INTERNATIONAL BONDS ->Emerging Markets Bond'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Emerging Markets Bond</option><option value='41. FIXED INCOME ->US TREASURY BONDS'>&nbsp;&nbsp;&nbsp;&nbsp;++US TREASURY BONDS</option><option value='199. FIXED INCOME -> US TREASURY BONDS ->SHORT GOVERNMENT'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SHORT GOVERNMENT</option><option value='200. FIXED INCOME -> US TREASURY BONDS ->LONG GOVERNMENT'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LONG GOVERNMENT</option><option value='224. FIXED INCOME -> US TREASURY BONDS ->Intermediate Government'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Intermediate Government</option><option value='64. FIXED INCOME ->Money Market'>&nbsp;&nbsp;&nbsp;&nbsp;Money Market</option><option value='136. FIXED INCOME ->Bank Loan'>&nbsp;&nbsp;&nbsp;&nbsp;Bank Loan</option><option value='218. FIXED INCOME ->Multisector Bond'>&nbsp;&nbsp;&nbsp;&nbsp;Multisector Bond</option><option value='234. FIXED INCOME ->Inflation-Protected Bond'>&nbsp;&nbsp;&nbsp;&nbsp;Inflation-Protected Bond</option><option value='3.CASH ASSET'>--CASH ASSET</option><option value='15. CASH ASSET ->CASH'>&nbsp;&nbsp;&nbsp;&nbsp;CASH</option><option value='16. CASH ASSET ->CD'>&nbsp;&nbsp;&nbsp;&nbsp;CD</option><option value='4.HYBRID ASSETS'>--HYBRID ASSETS</option><option value='17. HYBRID ASSETS ->BALANCE FUND'>&nbsp;&nbsp;&nbsp;&nbsp;++BALANCE FUND</option><option value='65. HYBRID ASSETS -> BALANCE FUND ->Moderate Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Moderate Allocation</option><option value='66. HYBRID ASSETS -> BALANCE FUND ->Conservative Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Conservative Allocation</option><option value='99. HYBRID ASSETS -> BALANCE FUND ->World Allocation'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;World Allocation</option><option value='133. HYBRID ASSETS -> BALANCE FUND ->Retirement Income'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Retirement Income</option><option value='138. HYBRID ASSETS -> BALANCE FUND ->Target Date 2000-2010'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2000-2010</option><option value='141. HYBRID ASSETS -> BALANCE FUND ->Target Date 2011-2015'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2011-2015</option><option value='142. HYBRID ASSETS -> BALANCE FUND ->Target Date 2016-2020'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2016-2020</option><option value='143. HYBRID ASSETS -> BALANCE FUND ->Target Date 2021-2025'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2021-2025</option><option value='144. HYBRID ASSETS -> BALANCE FUND ->Target Date 2031-2035'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2031-2035</option><option value='145. HYBRID ASSETS -> BALANCE FUND ->Target Date 2041-2045'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2041-2045</option><option value='146. HYBRID ASSETS -> BALANCE FUND ->Target Date 2026-2030'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2026-2030</option><option value='147. HYBRID ASSETS -> BALANCE FUND ->Target Date 2036-2040'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2036-2040</option><option value='149. HYBRID ASSETS -> BALANCE FUND ->Target Date 2050+'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Target Date 2050+</option><option value='19. HYBRID ASSETS ->PREFERRED SECURITIES'>&nbsp;&nbsp;&nbsp;&nbsp;PREFERRED SECURITIES</option><option value='20. HYBRID ASSETS ->CONVERTIBLE SECURITIES'>&nbsp;&nbsp;&nbsp;&nbsp;CONVERTIBLE SECURITIES</option><option value='209. HYBRID ASSETS ->Convertibles'>&nbsp;&nbsp;&nbsp;&nbsp;Convertibles</option><option value='5.REAL ESTATE'>--REAL ESTATE</option><option value='21. REAL ESTATE ->US Real Estate'>&nbsp;&nbsp;&nbsp;&nbsp;US Real Estate</option><option value='22. REAL ESTATE ->Non-US Real Estate'>&nbsp;&nbsp;&nbsp;&nbsp;Non-US Real Estate</option><option value='140. REAL ESTATE ->Global Real Estate'>&nbsp;&nbsp;&nbsp;&nbsp;Global Real Estate</option><option value='6.COMMODITIES'>--COMMODITIES</option><option value='246. COMMODITIES ->COMMODITIES BROAD BASKET'>&nbsp;&nbsp;&nbsp;&nbsp;COMMODITIES BROAD BASKET</option><option value='7.HEDGES'>--HEDGES</option><option value='78.Long-Short'>--Long-Short</option><option value='159.Alternative'>--Alternative</option><option value='79. Alternative ->Bear Market'>&nbsp;&nbsp;&nbsp;&nbsp;Bear Market</option><option value='97. Alternative ->Currency'>&nbsp;&nbsp;&nbsp;&nbsp;Currency</option><option value='160. Alternative ->Market Neutral'>&nbsp;&nbsp;&nbsp;&nbsp;Market Neutral</option><option value='162.SECTOR EQUITY'>--SECTOR EQUITY</option><option value='91. SECTOR EQUITY ->Precious Metals'>&nbsp;&nbsp;&nbsp;&nbsp;Precious Metals</option><option value='107. SECTOR EQUITY ->Miscellaneous'>&nbsp;&nbsp;&nbsp;&nbsp;Miscellaneous</option><option value='108. SECTOR EQUITY ->Utilities'>&nbsp;&nbsp;&nbsp;&nbsp;Utilities</option><option value='112. SECTOR EQUITY ->Health'>&nbsp;&nbsp;&nbsp;&nbsp;Health</option><option value='120. SECTOR EQUITY ->Communications'>&nbsp;&nbsp;&nbsp;&nbsp;Communications</option><option value='121. SECTOR EQUITY ->Natural Resources'>&nbsp;&nbsp;&nbsp;&nbsp;Natural Resources</option><option value='126. SECTOR EQUITY ->Technology'>&nbsp;&nbsp;&nbsp;&nbsp;Technology</option><option value='137. SECTOR EQUITY ->Financial'>&nbsp;&nbsp;&nbsp;&nbsp;Financial</option><option value='187. SECTOR EQUITY ->Consumer Staples'>&nbsp;&nbsp;&nbsp;&nbsp;Consumer Staples</option><option value='188. SECTOR EQUITY ->Consumer Discretionary'>&nbsp;&nbsp;&nbsp;&nbsp;Consumer Discretionary</option><option value='189. SECTOR EQUITY ->Industrials'>&nbsp;&nbsp;&nbsp;&nbsp;Industrials</option><option value='190. SECTOR EQUITY ->Energy'>&nbsp;&nbsp;&nbsp;&nbsp;Energy</option><option value='196. SECTOR EQUITY ->SPECIALTY-REAL ESTATE'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-REAL ESTATE</option><option value='222. SECTOR EQUITY ->Equity Energy'>&nbsp;&nbsp;&nbsp;&nbsp;Equity Energy</option><option value='235. SECTOR EQUITY ->SPECIALTY-UTILITIES'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-UTILITIES</option><option value='236. SECTOR EQUITY ->SPECIALTY-FINANCIAL'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-FINANCIAL</option><option value='237. SECTOR EQUITY ->SPECIALTY-HEALTH'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-HEALTH</option><option value='238. SECTOR EQUITY ->EQUITY PRECIOUS METALS'>&nbsp;&nbsp;&nbsp;&nbsp;EQUITY PRECIOUS METALS</option><option value='241. SECTOR EQUITY ->SPECIALTY-TECHNOLOGY'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-TECHNOLOGY</option><option value='242. SECTOR EQUITY ->SPECIALTY-NATURAL RES'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-NATURAL RES</option><option value='244. SECTOR EQUITY ->SPECIALTY-COMMUNICATIONS'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-COMMUNICATIONS</option><option value='245. SECTOR EQUITY ->SPECIALTY-PRECIOUS METALS'>&nbsp;&nbsp;&nbsp;&nbsp;SPECIALTY-PRECIOUS METALS</option><option value='248. SECTOR EQUITY ->MISCELLANEOUS SECTOR'>&nbsp;&nbsp;&nbsp;&nbsp;MISCELLANEOUS SECTOR</option><option value='225.Capital Protected'>--Capital Protected</option>";
		tr +="</select></td>";
		tr +="<td><input id=i"+i+"_1 value='NotFound'></td>";
		tr +="<td><input id=i"+i+"_2 value='NotFound'></td>";
		tr +="<td><input id=i"+i+"_3 value='NotFound'></td>";
		tr +="<td>"+lines[i]+"</td>";
		tr +="</tr>";
	}
	headers.append(tr);
}

function getMessage(symbol_index){
	id = "i"+symbol_index+"_0";
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/getBenchMarksByAssertClass.action?symbolsStr="+$('#'+id+' option:selected').val();
	mpiq_load_data_from_server(url,function(result){
		var lines = result.msg.split('|');
		for(var i=1;i<lines.length;i++){
			if(i==3){
				continue;
			}
			else{
				$('#i'+symbol_index+'_'+i).val(lines[i]);
			}
		}
		$("#mp_getBenchMarks").css("display","block");
	});
}

function mpiq_widget_${widgetName}_pasteTicker(){
	var tickers = "";
	var lines = notFandSymbols.split('|');
	for(var i=0;i<lines.length;i++){
		if($('#i'+i+'_1').val()=="NotFound")
			tickers = tickers + lines[i]+"$$";
		else
			tickers = tickers + $('#i'+i+'_1').val()+"$$";
	}
	mpiq_widget_${widgetName}_b_add(tickers);
	$(".mp_container").css("opacity: 0.5","1");
	$("#mp_getBenchMarks").css("display","none");
	notFandSymbols="";
}
//批量处理并替换
function mpiq_widget_${widgetName}_b_replace(){
	mpiq_widget_${widgetName}_clear();
	mpiq_widget_${widgetName}_b_process(function(data){
		for(ii=0;ii<data.size;ii++){
			//$("#mpiq_widget_${widgetName}_btnadd").trigger("click");
			mpiq_widget_${widgetName}_add_item(document.getElementById("mpiq_widget_${widgetName}_btnadd"),data.items[ii]);
		}
		mpiq_widget_${widgetName}_rework();
	});
}
//批量处理
function mpiq_widget_${widgetName}_b_process(addtickers,fn){
	//var tickers=$("#mpiq_widget_${widgetName}_tickers").val().replace(/\n/g, "..");
	var tickers=$("#mpiq_widget_${widgetName}_tickers").val().replace(/%/g,"%25").replace(/&/g, "%26");;
	var arrText = new Array();
	arrText = tickers.replace(/\r/g,'').split('\n');
	if(addtickers!=null) tickers = addtickers;
	else{
		tickers = "";
		for(var i=0;i<arrText.length;i++){
			tickers = tickers+arrText[i]+"$$";
		}
	}
	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/process.action?tickers="+tickers;
	mpiq_load_data_from_server(url,function(data1){
		fn(data1);
	});
}


//重设某列下面的控件值
function mpiq_widget_${widgetName}_reset(keyt,val){
	
	var headers=$("#mpiq_widget_${widgetName}_fund_table${time} tr").eq(0).children();
	var index=-1;
	for(i=0;i<headers.length-1;i++){
		var key=headers.eq(i).attr("key");
		
		if(key==keyt){
			index=i;			
		}
	}
	for(i=1;i<$("#mpiq_widget_${widgetName}_fund_table${time} tr").length;i++){
		$("#mpiq_widget_${widgetName}_fund_table${time} tr").eq(i).children().eq(index).children().val(val);
	}
	
}

//显示其它列
var isMore = false;
function mpiq_widget_${widgetName}_setHiddenCol(iCol,num){   
//	var oTable = $("#mpiq_widget_${widgetName}_fund_table${time}");
	isMore = !isMore;
	headers = $("#mpiq_widget_${widgetName}_btnadd").parent().parent().children();
	if(isMore){
		$("#mp_fundtablehide").fadeIn("slow");
		for(var i=0;i<4;i++){
			headers.eq(i).attr("width","1")
			headers.eq(i).css("font-size","0px");
		}
	}else{
		$("#mp_fundtablehide").fadeOut("slow"); 
		for(var i=0;i<5;i++){
			headers.eq(i).attr("width","300")
			headers.eq(i).css("font-size","8pt");
		}
	}
	var oTable = document.getElementById("mpiq_widget_${widgetName}_fund_table${time}");
    
		for(j=5;j<9;j++){
			for (i=0;i < oTable.rows.length ; i++)   
		    {   
		    	oTable.rows[i].cells[j].style.display = oTable.rows[i].cells[j].style.display=="none"?"":"none";
		    }
		}
		

	
	
}   
//重设redemption
function mpiq_widget_${widgetName}_resetredemption(){
	var str = window.prompt("Please input redemption: ","");
	if(str==undefined||str==null||str=="")return;
	mpiq_widget_${widgetName}_reset("redemption",str);

}
//重设roundtrip
function mpiq_widget_${widgetName}_resetroundtrip(){
	var str = window.prompt("Please input roundtrip: ","");
	if(str==undefined||str==null||str=="")return;
	mpiq_widget_${widgetName}_reset("roundtrip",str);
}
//重设waiting period
function mpiq_widget_${widgetName}_resetwaitingperiod(){
	var str = window.prompt("Please input waiting period: ","");
	if(str==undefined||str==null||str=="")return;
	mpiq_widget_${widgetName}_reset("waitingperiod",str);
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


//装载plan information
function mpiq_widget_${widgetName}_update_plan(id,isCopy){
	
//	var url="http://${serverName}:${port}/LTISystem/widgets/${widgetName}/basic_inf.action?"+mpiq_params;
	var url= "http://${serverName}:${port}/LTISystem/widgets/planwidget/basic_inf.action?planID="+id+"&basic_inf=1";
	mpiq_load_data_from_server(url,function(data){
		$("#mpiq_widget_${widgetName}_description${time}").val(data.description);
		if(isCopy)
			$("#create_mp_heardname").html(data.userName+"_"+data.name);
		else
			$("#create_mp_heardname").html(data.name);
		$("<input class=\"createorupdate\" type=\"button\" value=\"Update\" onclick=\"mpiq_widget_${widgetName}_save("+id+")\">").replaceAll(".createorupdate"); 
		var tr=""
		if(data.fundtableMapd!=undefined||data.fundtableMapd!=null){
			for(var key in data.fundtableMapd.items){
		           for(var key1 in data.fundtableMapd.items[key]){
		        	   if(data.fundtableMapd.items[key][key1].ID!=undefined||data.fundtableMapd.items[key][key1].ID!=null){
		        		   if(key1%2==0){
		        			   tr+="<tr class='odd'>";
		        		   }
		        		   if(key1%2==1){
		        			   tr+="<tr class='even'>";
		        		   }
				           tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+data.fundtableMapd.items[key][key1].assetClassName+"</textarea></td>";
				           tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' onblur='mpiq_widget_${widgetName}_out_text(this)'>"+data.fundtableMapd.items[key][key1].symbol+"</textarea></td>";
				           tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+data.fundtableMapd.items[key][key1].name+"</textarea></td>";
				           tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+parseInt(data.fundtableMapd.items[key][key1].quality*100)+"%</textarea></td>";
				           tr+="<td><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+data.fundtableMapd.items[key][key1].description+"</textarea></td>";
				           tr+="<td style='display:none;'><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+data.fundtableMapd.items[key][key1].redemption+"</textarea></td>";
				           tr+="<td style='display:none;'><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+data.fundtableMapd.items[key][key1].roundtripLimit+"</textarea></td>";
				           tr+="<td style='display:none;'><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+data.fundtableMapd.items[key][key1].waitingPeriod+"</textarea></td>";
				           tr+="<td style='display:none;'><textarea id='mp_chrows'  value='' class='mpiq_show_text' onclick='mpiq_widget_${widgetName}_edit_text(this)' >"+data.fundtableMapd.items[key][key1].memo+"</textarea></td>";
				           tr+="<td><input type='button' value='delete' onclick='mpiq_widget_${widgetName}_remove_item(this)' class='button_remove'></td>";
				           tr+="</tr> ";
		        	   }
		           }
	           }
		}
		$("#mpiq_widget_${widgetName}_fund_table${time}").append(tr);
		
		
	});
}