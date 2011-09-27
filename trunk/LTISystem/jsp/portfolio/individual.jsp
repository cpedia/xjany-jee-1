<%@ page pageEncoding = "UTF-8"%>
<%@ page contentType = "text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="authz" uri="/WEB-INF/authz.tld"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf8">
<meta name="portfolios" content="vf_current" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<META HTTP-EQUIV="expires" CONTENT="Fer, 1 Dec 2009 08:21:57 GMT">
<!-- new theme -->

<link href="../images/jquery.tooltip/jquery.tooltip.css" rel="stylesheet" type="text/css" />
<link href="images/jquery.ajaxAutoComplete/jquery.suggest.css" rel="stylesheet" type="text/css" />
<link href="images/css/portfolio.individual.parameter.css" rel="stylesheet" type="text/css" />
<link href="../images/jquery.ToggleVal/screen.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="images/css/individual.css" rel="stylesheet" type="text/css" media="screen"/>

<SCRIPT src="images/jquery.ajaxAutoComplete/jquery.dimensions.js" type="text/javascript"></SCRIPT>
<SCRIPT src="images/jquery.ajaxAutoComplete/jquery.ajaxContent.js" type="text/javascript"></SCRIPT>
<SCRIPT src="images/jquery.ajaxAutoComplete/jquery.suggest.js" type="text/javascript"></SCRIPT>
<SCRIPT src="images/jquery.ajaxAutoComplete/jquery.bgiframe.js" type="text/javascript"></SCRIPT>

<SCRIPT src="../images/jquery.ToggleVal/jquery.toggleval.js" type="text/javascript" ></SCRIPT>
<SCRIPT src="../images/jquery.tooltip/jquery.tooltip.js" type="text/javascript"></SCRIPT>
<SCRIPT src="../images/jquery.tooltip/PopDetails.js" type="text/javascript" ></SCRIPT>


<SCRIPT src="../images/floatWindow/interface.js" type="text/javascript"></SCRIPT>
<SCRIPT src="images/individual.js" type="text/javascript" ></SCRIPT>

<script type="text/javascript" src="../images/jquery.cookie.js"></script>
<script type="text/javascript" src="../images/translateEncode.js"></script>		

<s:set name="portfolio" value="#request.portfolio"></s:set>
<title><s:property value="#portfolio.name"/> individual page</title>
<!-- Initialize BorderLayout START-->
<iframe  src='' id="testframe" scrolling="no" width="0" height="0" frameborder="0" marginWidth="0" marginHeight="0" ></iframe>


		
<style>
#buttonDiv a{
    width:60px;
    height:15px;
    
    display:bolck;    
    font-size: 12px; 
        
    margin:2px;
    
    padding:2px;
    
    color:#000000;
    background:#7091d1;
    
    text-decoration:none;
    text-align:center;
    
    border:2px outset #ffffff;
}
#buttonDiv a:hover{
    font-weight:bold;
    color:#ffffff;
    background:#56acef;
    text-decoration:underline;
    
    border:2px outset #ffffff;
}

</style>
<script type="text/javascript">

$(document).ready(function(){
	if($.cookie('run_in_server_flag')=='false'){
		$('#run_in_server_flag').attr({'checked':false});
	}else{
		$('#run_in_server_flag').attr({'checked':true});
	}
	
	
	if($("#useThirdPartyAPI").val()!=''){
		$('#run_in_server_flag').attr({'checked':false,'disabled':true});
	}
	$("#useThirdPartyAPI").change(function() {
		if($("#useThirdPartyAPI").val()!=''){
			$('#run_in_server_flag').attr({'checked':false,'disabled':true});
		}else{
			$('#run_in_server_flag').attr({'disabled':false});
		}
	});
	$("#run_in_server_flag").change(function() {
		$.cookie('run_in_server_flag', $("#run_in_server_flag").attr('checked'));
	});
})

function e(){
	if($('#portfolio_3par').val()!='')$('#run_in_server_flag').attr({checked:false});
	if($('#run_in_server_flag').attr('checked')){
		e_in_server();
		$("#portfolioForm").submit();
	}else{
		e_in_local();
	}
}
function e_in_server(){
	$("#portfolioForm").attr({action:"../portfolio/Evaluate.action?action=evaluate"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").attr({target:"_blank"});
}

function e_in_local(){
	if($.browser.msie) {
		try {  
			//new  ActiveXObject("LTIEXECUTOR.LTIExecutorCtrl.1");
			//window.location.href='Edit.action?action=execute&local=true&ID=<s:property value="portfolio.ID"/>&useThirdPartyAPI='+$("#useThirdPartyAPI").val();
			$("#portfolioForm").attr({action:"../portfolio/EvaluateInLocal.action?action=execute"});
			$("#portfolioForm").attr({onsubmit:"return true"});
			$("#portfolioForm").attr({target:"_blank"});
			$("#portfolioForm").submit();
		}   
		catch(e){ 
			if(window.confirm("Download LTI Executor?\nAfter downloading LTI Executor, please install it.")){
				window.location.href="../help/install.html";  
			}
		}
	}
	else if($.browser.mozilla){
		var testframe=document.getElementById("testframe");
		testframe.src='ff_run_inlocal.jsp?portfolioID=<s:property value="portfolio.ID"/>&action=execute&useThirdPartyAPI='+$("#useThirdPartyAPI").val();
		
	}
}

function monitor(){
	if($('#portfolio_3par').val()!='')$('#run_in_server_flag').attr({checked:false});
	if($('#run_in_server_flag').attr('checked')){
		execute_in_server();
		$("#portfolioForm").submit();
	}else{
		execute_in_local();
	}
}
function update(){
	if($('#portfolio_3par').val()!='')$('#run_in_server_flag').attr({checked:false});
	if($('#run_in_server_flag').attr('checked')){
		update_in_server();
		$("#portfolioForm").submit();
	}else{
		update_in_local();
	}
}

function execute_in_server(){
	document.Edit.target='_self';
	$("#portfolioForm").attr({action:"../portfolio/Monitor.action?action=execute"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").attr({target:"_self"});
}

function update_in_server(){
	document.Edit.target='_self';
	$("#portfolioForm").attr({action:"../portfolio/Update.action?action=execute"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").attr({target:"_self"});
}

function execute_in_local(){
	if($.browser.msie) {
		try {  
			
			//window.location.href='Edit.action?action=execute&local=true&ID=<s:property value="portfolio.ID"/>&useThirdPartyAPI='+$("#useThirdPartyAPI").val();
			//original version//
			//new  ActiveXObject("LTIEXECUTOR.LTIExecutorCtrl.1");
			$("#portfolioForm").attr({action:"../portfolio/Local.action?action=execute"});
			$("#portfolioForm").attr({onsubmit:"return true"});
			$("#portfolioForm").attr({target:"_self"});
			$("#portfolioForm").submit();
		}   
		catch(e){ 
			if(window.confirm("Download LTI Executor?\nAfter downloading LTI Executor, please install it.")){
				window.location.href="../help/install.html";  
			}
		}
	}
	else if($.browser.mozilla){
		var testframe=document.getElementById("testframe");
		testframe.src='ff_run_inserver.jsp?portfolioID=<s:property value="portfolio.ID"/>&action=execute&useThirdPartyAPI='+$("#useThirdPartyAPI").val();
		
	}
}

function update_in_local(){
	if($.browser.msie) {
		try {  
			//new  ActiveXObject("LTIEXECUTOR.LTIExecutorCtrl.1");
			window.location.href='Edit.action?action=update&local=true&portfolioID=<s:property value="portfolio.PortfolioID"/>';
		}   
		catch(e){ 
			if(window.confirm("Download LTI Executor?\nAfter downloading LTI Executor, please install it.")){
				window.location.href="../help/install.html";  
			}
		}
	}
	else if($.browser.mozilla){
		var testframe=document.getElementById("testframe");
		testframe.src='ff_run_inserver.jsp?portfolioID=<s:property value="portfolio.ID"/>&action=update';
		
	}
}

</script>
<script>
function showhide(pane){
	$pane=$(pane);
	if($pane.is(':visible')){
	$pane.toggle();
	}
	else
	{ 
	$pane.toggle();
	}
}

/*delete an security*/
function delSecurity(assetID, securityID){
	$("#securityItem-"+assetID+"-"+(securityID)).hide();
	$("#securitySymbol-"+assetID+"-"+securityID).val("");
	$("#securityName-"+assetID+"-"+securityID).val("");
}

function delAsset(assetID){
	$("#"+(assetID+1)).hide();
	$("#assetName"+(assetID+1)).val("");
	$('#security-'+(assetID+1)).hide().children('td');
	for(i=0; i<securityNumber[assetID]; i++)
	{
		delSecurity(assetID, i);
	}
}

/*submit the form for save*/	
function save(){
	$("#portfolioForm").attr({action:"../portfolio/Save.action?action=save"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").attr({target:"_self"});
	$("#portfolioForm").submit();
}
function copy(){
	$("#portfolioForm").attr({action:"../portfolio/Copy.action?action=copy"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").attr({target:"_self"});
	$("#portfolioForm").submit();
}
function copyas(){
	$("#portfolioForm").attr({action:"../portfolio/CopyAs.action?action=copy"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").attr({target:"_self"});
	$("#portfolioForm").submit();
}

/*submit the form for save as*/
function saveAs(){
	$("#portfolioForm").attr({action:"../portfolio/SaveAs.action?action=saveas"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").attr({target:"_self"});
	$("#portfolioForm").submit();
}



/*delete the portfolio*/
function del(){
	$("#portfolioForm").attr({action:"../portfolio/Delete.action?action=delete"});
	$("#portfolioForm").attr({target:"_self"});
	$("#portfolioForm").attr({onsubmit:"return true"});
	$("#portfolioForm").submit();
}
function dataFormat(data){
	data = Math.round(parseFloat(data)*1000)/1000
	document.write(data);
}


function dataFormat1(data){
	data = Math.round(parseFloat(data)*1000)/1000
	return data;
}
/**
 * name: dataFormat2
 * function: format the data to percentage (##.##%)*/
function dataFormat2(data){
	data = data * 100;
	data = Math.round(parseFloat(data)*1000)/1000;
	if(data >= 0 || data <0)
		document.write(data + "%");
	else
		document.write(data);
}
</script>
</head>
<body>

<span class="head">
<div align="center"><font color="red"><s:fielderror></s:fielderror></font></div>
<div align="center"><font color="red"><s:actionerror/></font></div>
<div align="center"><font color="red"><s:property value="actionMessage"/></font></div>
<s:property value="#request.classTree" escape="false"/>
<div align="center">
	<s:if test="#request.action=='execute'||#request.action=='update'">
		<script  type="text/javascript">
		
		$(function () {
			var timestamp=(new Date()).getTime();
			var ip=document.location.hostname;
			var port=document.location.port;
			if(port=="")port='80';
			
			<s:if test="#request.local=='true'">
			//alert("ccd");
			//var runstring='ltisystem://<s:property value="#request.action"/>?portfolioID=<s:property value="portfolio.portfolioID"/>&version=1.6.0&useThirdPartyAPI=<%=request.getParameter("useThirdPartyAPI")%>&ip='+ip+':'+port+'&timestamp='+timestamp;
			//var runstring='http://localhost:8081/ltisystem://<s:property value="#request.action"/>?portfolioID=<s:property value="portfolio.portfolioID"/>&timestamp='+timestamp;
			var runstring='http://localhost:8081/<s:property value="#request.action"/>?portfolioID=<s:property value="portfolio.portfolioID"/>&timestamp='+timestamp;
			//var runstring2='http://127.0.0.1:52201/<s:property value="#request.action"/>?portfolioID=<s:property value="portfolio.portfolioID"/>&version=1.6.0&useThirdPartyAPI=<%=request.getParameter("useThirdPartyAPI")%>&ip='+ip+':'+port+'&timestamp='+timestamp;
			var detailstring='processing.jsp?portfolioID=<s:property value="#portfolio.portfolioID"/>'+'&timestamp='+timestamp+'&engineport=8081'+'&ip=127.0.0.1';
			</s:if>
			<s:else>	
			//alert(ip);
			var runstring='http://'+ip+':8081/<s:property value="#request.action"/>?portfolioID=<s:property value="portfolio.portfolioID"/>'+'&timestamp='+timestamp;
			var detailstring='processing.jsp?portfolioID=<s:property value="#portfolio.portfolioID"/>'+'&timestamp='+timestamp+'&engineport=8081'+'&ip='+ip;
			</s:else>
			
			var executorframe=document.getElementById("executorframe");
			executorframe.src=runstring;
			
			var detailframe=document.getElementById("detailframe"); 
			detailframe.src=detailstring;
		});
		</script>
		
		
		
		<div id="processingpane" style="height:auto !important;min-height:90px;height:90px;">
		<s:if test="#request.local=='true'">
		Running on local:
		</s:if>
		<s:else>
		Running on server:
		</s:else>
		<iframe align="center" src='' name="detail" id="detailframe" scrolling="no" width="100%" height="100%" frameborder="0" marginWidth="0" marginHeight="0" ></iframe>
		<iframe  src='' id="executorframe"  scrolling="no" width="0" height="0" frameborder="0" marginWidth="0" marginHeight="0" ></iframe>
		</div>
	</s:if>
</div>


<div align="center">
<s:form id="portfolioForm" theme="simple">
<s:if test="#portfolio.isOriginalPortfolio==false && portfolio.OriginalPortfolioID!=null">
<p>
<div align="center">
	<div id="originalPortfolioTitle" align="center">
		<s:if test="portfolio.state==2"><font color="red">(<s:text name="evaluated"></s:text>)</font></s:if> <s:text name="current.portfolio.information"></s:text> <s:if test="portfolio.state==1"> <font color="red">(<s:text name="live"></s:text>)</font></s:if>
	</div>
	<p>
		<div id="holdingTitle" align="center">
			<s:if test="isHolding == true">
				<s:text name="holding.tips.pre"></s:text> <s:property value="holdingDate"/>. <s:text name="holding.tips.tail"></s:text>
			</s:if>
		</div>
	</p>
	<p>
		<div id="originalPortfolioDescription" align="center">
			<s:text name="current.tips"></s:text>
			<s:url id="original_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
				<s:param name="ID" value="#portfolio.originalPortfolioID"></s:param>
			</s:url>
			<s:a href="%{original_url}"><s:text name="goto.original"></s:text></s:a>
		</div>
	</p>
</div>
</p>
</s:if>
<s:else>
<p>
<div align="center">
	<div id="originalPortfolioTitle" align="center">
		<s:if test="portfolio.state==2"><font color="red">(<s:text name="evaluated"></s:text>)</font></s:if><s:text name="original.portfolio.information"></s:text> <s:if test="portfolio.state==1 && #request.action!='execute'"> <font color="red">(<s:text name="live"></s:text>)</font></s:if>
	</div>
	<div id="holdingTitle" align="center" >
		<s:if test="isHolding == true">
			Holding on <s:property value="holdingDate"/> is not available.
		</s:if>
	</div>
	<s:if test="#portfolio.ID!=0">
		<div id="originalPortfolioDescription" align="center">
			<s:text name="original.tips"></s:text>
			<s:url id="current_url" namespace="/jsp/portfolio" action="Edit" includeParams="none">
				<s:param name="ID" value="#portfolio.portfolioID"></s:param>
			</s:url>
			<s:a href="%{current_url}"><s:text name="goto.current"></s:text></s:a>
		</div>		
	</s:if>
</div>
</p>
</s:else>
<s:if test="#portfolio.ID!=0 && portfolio.isOriginalPortfolio==true && portfolio.state!=2">
<div id="emailDiv" align="center">
	If you need to set email notification when transactions happen, please click the button.
	<s:if test="operation==true">
	<s:hidden id="notification" name="emailNotification"></s:hidden>
	<input id="emailalert" type="button" <s:if test="emailNotification == null || emailNotification == false"> value="email alert" </s:if> <s:else>value="remove alert"</s:else> />
	
	<script type="text/javascript">
	$("#emailalert").click(function(){
		var $alertbutton = $(this);
		var notification = $("#notification").val();
		
		if(notification.indexOf(false) != -1){
			$.ajax({
				type: "get",
				url: "../ajax/Email.action",
				data: "portfolioID="+<s:property value="portfolio.PortfolioID"/>,
				datatype: "html",
				success: function(result){
					if(result.indexOf("fail") == -1){
						$alertbutton.val("remove alert");
						$("#notification").val(true);
					}
				}
			})
		}
		else{
			$.ajax({
				type:	"get",
				url:	"../ajax/EmailRemove.action",
				data: "portfolioID="+<s:property value="portfolio.PortfolioID"/>,
				datatype: "html",
				success: function(result){
					if(result.indexOf("fail") == -1){
						$alertbutton.val("email alert");
						$("#notification").val(false);
					}
				}
			})
		}
	})
	</script>
	</s:if>
	<s:if test="operation==false">
	<input id="emailalert" type="button" value="email alert" disabled="true"/>
	</s:if>
	
</div>
</s:if>
<!-- show portfolio references -->
<table id="basic" style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid;" cellSpacing=0 cellPadding=0 width="80%" align="center">
<tr>
  <td colspan="7" align="center">
  	<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
		<s:textfield cssClass="sorlidBorder" id="portfolioName" name="portfolio.name" theme="simple" size="50"></s:textfield>
	</s:if>
	<s:elseif test="portfolio.isOriginalPortfolio==false && portfolio.state==2">
		<s:textfield cssClass="sorlidBorder" readonly="true" name="portfolio.name" theme="simple" size="50"></s:textfield>
	</s:elseif>
	<s:else>
		<s:property value="portfolio.name"/>
		<s:hidden id="portfolioName" name="portfolio.name"></s:hidden>
	</s:else>
	<s:if test="portfolio.IsModelPortfolio==true">
		<s:text name="belong"></s:text>
		<s:url id="mainStrategy_url" namespace="/jsp/strategy" action="View" includeParams="none">
			<s:param name="ID" value="portfolio.MainStrategyID"></s:param>
			<s:param name="action">view</s:param>
		</s:url>
		<s:a href="%{mainStrategy_url}"><s:property value="portfolio.MainStrategyName"/></s:a>
		
	</s:if>
	<s:hidden id="portfolioID" name="portfolio.ID"/>
  	<s:hidden name="portfolio.MainStrategyID"/>
  	<s:hidden name="portfolio.IsModelPortfolio"/>
  	<s:hidden name="portfolio.IsOriginalPortfolio"/>
	<s:hidden name="portfolio.PortfolioID"/>
	<s:hidden name="portfolio.State"></s:hidden>
  </td>
  <td align="right">
  	<s:if test="#portfolio.ID!=0 && portfolio.isOriginalPortfolio==true && portfolio.state!=2">
  		<a href="javascript:void(0);" onclick="del()"><s:text name="delete"></s:text></a>
  	</s:if>
  	<s:else>
  		&nbsp;
  	</s:else>
  </td>
</tr>
<tr>
	<td width="12%" rowspan="4" align="center"><s:text name="basic"></s:text></td>
	<td height="22" colspan="2" align="right"><s:text name="startingdate"></s:text></td>
	<td width="14%" align="right">
	<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
		<s:textfield cssClass="sorlidBorder" id="startingDate" name="portfolio.StartingDateString" theme="simple"></s:textfield>
	</s:if>
	<s:elseif test="portfolio.isOriginalPortfolio==false && portfolio.state==2">
		<s:textfield cssClass="sorlidBorder" readonly="true" name="portfolio.StartingDateString" theme="simple"></s:textfield>
	</s:elseif>
	<s:else>
		<s:property value="portfolio.startingDateString"/>
		<s:hidden name="portfolio.startingDateString"></s:hidden>
	</s:else>
	</td>
	<td width="15%" align="right"><s:text name="original.amount"></s:text></td>
	<td width="13%" align="right">
	<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
		<s:textfield cssClass="sorlidBorder" id="originalAmount" name="portfolio.OriginalAmount"  theme="simple"></s:textfield>
	</s:if>
	<s:elseif test="portfolio.isOriginalPortfolio==false && portfolio.state==2">
		<s:textfield cssClass="sorlidBorder" name="portfolio.OriginalAmount"  theme="simple"></s:textfield>
	</s:elseif>
	<s:else>
		<s:property value="portfolio.OriginalAmount"/>
	</s:else>
	</td>
	<td width="13%" align="right">
		<s:if test="portfolio.IsOriginalPortfolio==false">
			<s:text name="cash"></s:text>
		</s:if>
		<s:else>
			&nbsp;
		</s:else>
	</td>
	<td width="16%" align="right">
		<s:if test="portfolio.IsOriginalPortfolio==false">
		<script>
			dataFormat(<s:property value="#portfolio.cash"/>);
		</script>
		</s:if>
		<s:else>
			&nbsp;
		</s:else>
	</td>
</tr>

<tr>
    <td colspan="2" align="right"><s:text name="benchmark"></s:text></td>
    <td align="right">
    <s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
      	<s:textfield cssClass="sorlidBorder" name="portfolio.Benchmark" id="benchmark" theme="simple"></s:textfield>
    </s:if>
    <s:elseif test="portfolio.isOriginalPortfolio==false && portfolio.state==2">
      	<s:textfield cssClass="sorlidBorder" readonly="true" name="portfolio.Benchmark" theme="simple"></s:textfield>
    </s:elseif>
    <s:else>
    	<s:property value="portfolio.Benchmark"/>
    </s:else>
   	</td>
	<td align="right">
		<s:if test="portfolio.IsOriginalPortfolio==false">
			<s:text name="total.amount"></s:text>
		</s:if>
		<s:else>
			Check Confidence
		</s:else>
	</td>
    <td align="right">
		<s:if test="portfolio.IsOriginalPortfolio==false">
			<script>
				dataFormat(<s:property value="#portfolio.TotalAmount"/>);
			</script>
		</s:if>
		<s:else>
		
			<s:select id="confidenceSelect" list="{'Uncheck','Daily','Weekly','Monthly'}" theme="simple" name="portfolio.Confidence"></s:select>
		<script>
		 	$("#confidenceSelect").val("<s:property value="#portfolio.Confidence"/>");
		 	
		</script>
		</s:else>
    </td>
    <td align="right">
	<s:if test="portfolio.isOriginalPortfolio==true">
		<s:text name="enddate"></s:text>(For Evaluate)
	</s:if>
	<s:else>
		<s:text name="last.valid.date"></s:text>
	</s:else>
	</td>
	<td align="right">
	<s:if test="portfolio.isOriginalPortfolio==true">
		<s:textfield cssClass="sorlidBorder" id="endDate" name="portfolio.EndDateString" theme="simple"></s:textfield>
	</s:if>
	<s:else>
		<s:property value="portfolio.EndDateString"/>
	</s:else>
	</td>
	
</tr>
<tr>
	<td align="right" colspan="2"><s:text name="portfolio.symbol"></s:text></td>
	<td align="right">
		<s:if test="portfolio.isOriginalPortfolio==true">
			<s:textfield readonly="true" cssStyle="width:99%" cssClass="solidBorder" id="symbol" theme="simple" name="portfolio.Symbol"></s:textfield>
		</s:if>
		<s:else>
			<s:property value="portfolio.Symbol"/>&nbsp;
		</s:else>
	</td>
	<td align="right"><s:text name="portfolio.margin.rate"></s:text></td>
	<td align="right">
		<s:if test="portfolio.isOriginalPortfolio==true">
			<s:textfield id="marginRate" name="portfolio.MarginRateStr" cssStyle="width:99%" cssClass="solidBorder" theme="simple"></s:textfield>
		</s:if>
		<s:else>
			<s:property value="#portfolio.MarginRateStr"/>
		</s:else>
	</td>
	<td align="right"><s:text name="last.transaction.date"></s:text></td>
	<td align="right">
		<s:if test="portfolio.isOriginalPortfolio==true">
			&nbsp;
		</s:if>
		<s:else>
			<s:property value="portfolio.lastTransactionDate"/>&nbsp;
		</s:else>
	</td>

</tr>
<tr>
	<td align="right" colspan="2">User Name</td>
	<td align="right">
		<s:if test="portfolio.ID != 0">
		<s:property value="portfolio.userName"/>
		</s:if>
		<s:else>
			&nbsp;
		</s:else>
	</td>
	<td align="right" >Categories</td>
	<td align="right">
		<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
			<s:textfield theme="simple" name="portfolio.Categories"  cssClass="solidBorder"></s:textfield>
		</s:if>
		<s:else>
         <s:property value="portfolio.Categories"/>&nbsp;
		</s:else>
	</td>
	
	
	<td align="right">Update Mode</td>
	<td align="right">
		<s:select id="updatemodeSelect" list="#{'1':'ALIVE','2':'UNALIVE','3':'DEVELOPMENT','4':'LOWLEVEL','5':'EMAILALERT','6':'PRODUCTION','7':'DATA','8':'PRODUCTION_MODEL'}" theme="simple" name="portfolio.UpdateMode"></s:select>
		<script>
		 	$("#updatemodeSelect").val("<s:property value="#portfolio.UpdateMode"/>");
		</script>
	</td>
	
	<!-- 
	<td align="right"><s:text name="portfolio.isPublic"></s:text></td>
	<td align="right">
		<s:if test="portfolio.isOriginalPortfolio==true">
			<p>
				<input type="radio"  name="portfolio.isPublic" value="true" <s:if test="portfolio.isPublic == true"> checked</s:if> /><label>yes</label>
			</p>
			<p>
				<input type="radio" name="portfolio.isPublic" value="false" <s:if test="portfolio.isPublic == false"> checked</s:if>/><label>no</label>
			</p>
		</s:if>
		<s:else>
			<s:if test="portfolio.isPublic == true">
				yes
			</s:if>
			<s:else>
				no
			</s:else>
		</s:else>
	</td>
	-->	
</tr>
<!-- permission start -->
<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
	<tr>
	    <td align="center">Groups Permission</td>
		<td align="right" colspan="2"> delayed portfolio</td>
		<td align="right">
			<s:if test='action.equals("create")'>
				<!-- ANONYMOUS,USER -->
				<input type="text" value="" name="roleDelayed"/>
			</s:if>
			<s:else>
				<s:textfield title="ANONYMOUS,USER,Level_1,SUPER" cssClass="sorlidBorder" name="roleDelayed" theme="simple"></s:textfield>
			</s:else>
		</td>
		<td align="right"> realtime portfolio</td>
		<td align="right">
			<s:if test='action.equals("create")'>
				<!-- VF_B -->
				<input type="text" value="" name="roleRealtime"/>
			</s:if>
			<s:else>
				<s:textfield title="ANONYMOUS,USER,Level_1,SUPER" cssClass="sorlidBorder" name="roleRealtime" theme="simple"></s:textfield>
			</s:else>
		</td>
		<td align="right">save operation</td>
		<td align="right">
			<s:if test='action.equals("create")'>
				<!-- USER -->
				<input type="text" value="" name="roleOperation"/>
			</s:if>
			<s:else>
				<s:textfield title="ANONYMOUS,USER,Level_1,SUPER"  cssClass="sorlidBorder" name="roleOperation" theme="simple"></s:textfield>
			</s:else>
				
		</td>
	</tr>
</authz:authorize>
<authz:authorize ifNotGranted="ROLE_SUPERVISOR">
	<s:if test='action.equals("create")'>
		<input type="hidden" value="" name="roleDelayed"/>
		<input type="hidden" value="" name="roleRealtime"/>
		<input type="hidden" value="" name="roleOperation"/>
	</s:if>
	<s:else>
		<s:hidden name="roleDelayed"></s:hidden>
		<s:hidden name="roleRealtime"></s:hidden>
		<s:hidden name="roleOperation"></s:hidden>
	</s:else>
</authz:authorize>
<!-- permisstion end -->


<tr>
	<td width="12%" align="center"><s:text name="description"></s:text></td>
	<td width="88%" colspan="7" align="left">
	<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
		<textarea cols="100" name="portfolio.Description" style="width:99%;border:solid 1px"><s:property value="portfolio.Description"/></textarea>
	</s:if>
	<s:else>
		<textarea cols="100" readonly name="portfolio.Description" style="width:99%;background-color:white;border:solid 1px"><s:property value="portfolio.Description"/></textarea>
	</s:else>
	</td>
</tr>
<!-- 
<tr>
	<td width="12%" align="center"><s:text name="categories"></s:text></td>
	<td width="88%" colspan="7" align="left">
		<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
			<s:textfield theme="simple" name="portfolio.Categories" cssStyle="width:99%" cssClass="solidBorder"></s:textfield>
		</s:if>
		<s:else>
			<s:textfield theme="simple" name="portfolio.Categories" cssStyle="width:99%;background-color:white;border:solid 1px" cssClass="solidBorder" readonly="true"></s:textfield>
		</s:else>
	</td>
</tr>

 -->
<!-- MPT -->
<s:if test="#portfolio.isOriginalPortfolio==false">
	<tr bgcolor="#ffffee">
	        <td rowspan="6" align="Center" bgcolor="#ffffff">Portfolio <s:text name="statistic"></s:text></td>
	        <td colspan="2" align="right"><s:text name="rsquare"></s:text></td>
	        <td align="right">
		        <script>
		        dataFormat(<s:property value="#portfolio.RSquared"/>);
		        </script>
	        </td>
	        <td align="right"><s:text name="sharpe"></s:text></td>
	        <td align="right">
		        <script>
		        	dataFormat(<s:property value="#portfolio.SharpeRatio"/>);
		        </script>
	        </td>
			<td align="right"><s:text name="treynor"></s:text></td>
	        <td align="right">
		        <script>
		        	dataFormat(<s:property value="#portfolio.TreynorRatio"/>);
		        </script>	        
	        </td>
	</tr>
	<tr>
		<td colspan="2" align="right"><s:text name="standard.deviation"></s:text></td>
		<td align="right">
	        <script>
	        	dataFormat(<s:property value="#portfolio.StandardDeviation"/>);
	        </script>
		</td>
	  	<td align="right"><s:text name="AR"></s:text></td>
	  	<td align="right">
	  		<script>
	        	dataFormat2(<s:property value="#portfolio.AR"/>);
	        </script>
		</td>
	  	<td align="right"><s:text name="total.return"></s:text></td>
	  	<td align="right">
	  		<script>
	        	dataFormat2(<s:property value="#portfolio.TotalReturn"/>);
	        </script>
	  	</td>
	</tr>
	<tr bgcolor="#ffffee">
		<td colspan="2" align="right"><s:text name="alpha"></s:text></td>
		<td align="right">
	        <script>
	        	dataFormat2(<s:property value="#portfolio.Alpha"/>);
	        </script>
		</td>
	  	<td align="right"><s:text name="beta"></s:text></td>
	  	<td align="right">
	  		<script>
	        	dataFormat2(<s:property value="#portfolio.Beta"/>);
	        </script>
		</td>
	  	<td align="right">&nbsp;</td>
	  	<td align="right">
			&nbsp;
	  	</td>
	</tr>	
	<tr>
		<td width="7%" align="center">1 <s:text name="year"></s:text></td>
		<td width="10%" align="right"><s:text name="alpha"></s:text></td>
		<td align="right">
			<script>
	        	dataFormat2(<s:property value="#portfolio.Alpha1"/>);
	        </script>   
		</td>
	  	<td align="right"><s:text name="beta"></s:text></td>
	  	<td align="right">
	        <script>
	        	dataFormat2(<s:property value="#portfolio.Beta1"/>);
	        </script>	  		
	  	</td>
	  	<td align="right"><s:text name="AR"></s:text></td>
	  	<td align="right">
	        <script>
	        	dataFormat2(<s:property value="#portfolio.AR1"/>);
	        </script>	
	  	</td>
	</tr>
	<tr bgcolor="#ffffee">
	  <td align="center">3 <s:text name="years"></s:text></td>
		<td width="10%" align="right"><s:text name="alpha"></s:text></td>
		<td align="right">
			<script>
	        	dataFormat2(<s:property value="#portfolio.Alpha3"/>);
	        </script>   
		</td>
	  	<td align="right"><s:text name="beta"></s:text></td>
	  	<td align="right">
	        <script>
	        	dataFormat2(<s:property value="#portfolio.Beta3"/>);
	        </script>
	  	</td>
	  	<td align="right"><s:text name="AR"></s:text></td>
	  	<td align="right">
	        <script>
	        	dataFormat2(<s:property value="#portfolio.AR3"/>);
	        </script>	
	  	</td>
	</tr>
	<tr>
	  <td align="center">5 <s:text name="years"></s:text></td>
		<td width="10%" align="right"><s:text name="alpha"></s:text></td>
		<td align="right">
			<script>
	        	dataFormat2(<s:property value="#portfolio.Alpha5"/>);
	        </script>       
		</td>
	  	<td align="right"><s:text name="beta"></s:text></td>
	  	<td align="right">
	        <script>
	        	dataFormat2(<s:property value="#portfolio.Beta5"/>);
	        </script>	
	  	</td>
	  	<td align="right"><s:text name="AR"></s:text></td>
	  	<td align="right">
	        <script>
	        	dataFormat2(<s:property value="#portfolio.AR5"/>);
	        </script>	
	  	</td>
	</tr>
	<tr>
		<!-- Daily Data url -->
		<s:url id="dailyData_url" namespace="/jsp/portfolio" action="DailyDataMain" includeParams="none">
			<s:param name="portfolioID" value="#portfolio.ID"></s:param>
		</s:url>
		<td colspan="3"><s:a href="%{dailyData_url}"><s:text name="daily.data"></s:text></s:a></td>
		<!-- Daily Data Chart url -->
		<s:url id="dailyDataChart_url" action="Main" namespace="/jsp/flash" includeParams="none">
			<s:param name="portfolioID" value="#portfolio.ID"></s:param>
			<s:param name="portfolioName" value="#portfolio.name"></s:param>
		</s:url>
	    <td><s:a href="%{dailyDataChart_url}"><s:text name="chart"></s:text></s:a></td>
	    
	    <!-- Every Year MPT -->
	    <s:url id="MPT_url" namespace="/jsp/portfolio" action="MPTMain" includeParams="none">
			<s:param name="portfolioID" value="#portfolio.ID"></s:param>
		</s:url>
	    <td><s:a href="%{MPT_url}">Detailed Statistics</s:a></td>
	    <!-- confidence url -->
	    <s:url id="confidence_url" namespace="/jsp/portfolio" action="confidenceLevelCheck" includeParams="none">
	    	<s:param name="portfolioID" value="#portfolio.ID"></s:param>
	    </s:url>
	    <td><s:a href="%{confidence_url}">Confidence Check Result</s:a></td>
	    <!-- transaction url -->
	    <s:url id="transaction_url" namespace="/jsp/portfolio" action="TransactionMain" includeParams="none">
			<s:param name="portfolioID" value="#portfolio.ID"></s:param>
		</s:url>
	    <td><s:a href="%{transaction_url}"><s:text name="transaction"></s:text></s:a></td>
		
		<!-- Log url -->
		<s:url id="log_url" namespace="/jsp/portfolio" action="LogMain" includeParams="none">
			<s:param name="portfolioID" value="#portfolio.ID"></s:param>
		</s:url>
	  	<td align="left"><s:a href="%{log_url}"><s:text name="log"></s:text></s:a></td>
	</tr>
</s:if>	
</table>

<!-- portfolio level strategy information -->
<table align="center" id="strategy" style="BORDER-RIGHT: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellspacing=0 cellpadding=0 width="80%" >
		<tr>
          <td colspan="3" align="right">&nbsp;</td>
        </tr>
        <tr>
          <td width="14%" rowspan="3" align="center" ><s:text name="portfolio.strategies"></s:text></td>
          <td width="15%" align="left"><s:text name="rebalancing"></s:text></td>
          <td align="left">
          <s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
     		<s:textfield cssClass="sorlidBorder" name="portfolio.rebalancingStrategyName" id="rebalancingStrategy" theme="simple"></s:textfield>
     	  	<input id="rebalancingStrategyID" type="hidden" value='<s:property value="portfolio.rebalancingStrategyID"/>' />
     	  	<a id="rebalancingCheck" target="_blank" href='../strategy/View.action?ID=<s:property value="portfolio.rebalancingStrategyID"/>&action=view'><s:text name="check"></s:text></a>
     	  </s:if>
     	  <s:else>
     	  	<s:property value="portfolio.rebalancingStrategyName"/>
     	  	<s:url id="rebalancing_url" namespace="/jsp/strategy" action="View" includeParams="none">
          		<s:param name="ID" value="#portfolio.RebalancingStrategyID"></s:param>
          		<s:param name="action">view</s:param>
          	</s:url>
          	<!--<s:a href="%{rebalancing_url}">check</s:a>-->
          	<a target="_blank" href='<s:property value="rebalancing_url" escape="false"/>'><s:text name="check"></s:text></a>
     	  </s:else>
     	  <br>
          	<a href="javascript:void(0);" id="rbsp"><s:text name="parameters"></s:text></a>
          	<a class="rebalancingStrategyParameter" href="../ajax/StrategyParameter.action?name=portfolio.rebalancingStrategyName&q=STATIC">getParameter</a>
          	<br>          	
          	<!-- rebalancing strategy parameter pop window -->
			<!-- 由于加载是会先生成div，所以必须把div设置为不可视：display:none -->
			<div id="RBSPWindow" height="250px" style="display:none">
				<div height="3%" id="RBSPWindow_handle">			
					<a href="javascript:void(0);" id="RBSPWindow_close">[ x ]</a>
					<s:text name="rebalancing.parameters"></s:text>
				</div>
				<div id="RBSPWindow_content">
					<table>
					<s:if test="portfolio.rebalancingParameter!=null">
						<s:iterator value="portfolio.rebalancingParameter" status="r_st">
							<tr>
								<td>
									<s:property value="key"/>
								</td>
								<td>
								
								<s:if test="portfolio.isOriginalPortfolio==true">
									<s:if test="%{value.length()>20}">				
                                	<textarea cols=70 rows=4  name='portfolio.rebalancingParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
                                	</s:if>
                                	<s:else>
                                	<INPUT type="text" value='<s:property value="value"/>' name='portfolio.rebalancingParameter.<s:property value="key"/>'/>
                                	</s:else>
								</s:if>
								<s:else>
									<s:if test="%{value.length()>20}">				
                                	<textarea cols=70 rows=4 readonly name='portfolio.rebalancingParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
                                	</s:if>
                                	<s:else>
                                	<INPUT type="text" readonly value='<s:property value="value"/>' name='portfolio.rebalancingParameter.<s:property value="key"/>'/>
                                	</s:else>
								</s:else>		
								</td>								
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<s:text name="no.parameters"></s:text>
					</s:else>
					</table>
				</div>
				<div id="RBSPWindow_footer" align="center">
					<input id="RBSPWindow_confirm" type="button" value="confirm"/>
				</div>
			</div>
			<!-- end of the rebalancing strategy parameter pop window -->  	  
		  </td>	          	
        </tr>
        <tr>
          <td align="left"><s:text name="cashflow"></s:text></td>
          <td align="left">
          <s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
          	<s:textfield cssClass="sorlidBorder" name="portfolio.cashFlowStrategyName" id="cashFlow" theme="simple"></s:textfield>
          	<input type="hidden" id="cashFlowStrategyID" value='<s:property value="portfolio.cashFlowStrategyID"/>'/>
          	<a id="cashFlowCheck" target="_blank" href='../strategy/View.action?ID=<s:property value="portfolio.cashFlowStrategyID"/>&action=view'><s:text name="check"></s:text></a>
          </s:if>
          <s:else>
          	<s:property value="portfolio.cashFlowStrategyName"/>
          	<s:url id="cashflow_url" namespace="/jsp/strategy" action="View" includeParams="none">
          		<s:param name="ID" value="#portfolio.cashFlowStrategyID"></s:param>
          		<s:param name="action">view</s:param>
          	</s:url>
          	<!--<s:a href="%{cashflow_url}">check</s:a>-->
          	<a target="_blank" href='<s:property value="cashflow_url" escape="false"/>'><s:text name="check"></s:text></a>	
          </s:else>
          <br>
          	<a href="javascript:void(0);" id="csfp"><s:text name="parameters"></s:text></a>
          	<a class="cashFlowStrategyParameter" href="../ajax/StrategyParameter.action?name=portfolio.cashFlowStrategyName&q=STATIC">getParameter</a>
          	<br>
          	<!-- cash flow parameter pop window -->
			<!-- 由于加载是会先生成div，所以必须把div设置为不可视：display:none -->
			<div id="CFSPWindow" height="250px" style="display:none">
				<div height="3%" id="CFSPWindow_handle">			
					<a href="javascript:void(0);" id="CFSPWindow_close">[ x ]</a>
					<s:text name="cashflow.parameters"></s:text>
				</div>
				<div id="CFSPWindow_content">
				<s:if test="portfolio.cashFlowParameter!=null">
				<table>
					<s:iterator value="portfolio.cashFlowParameter" status="c_st">
					<tr>
						<td>
							<s:property value="key"/>
						</td>
						<td>
								<s:if test="portfolio.isOriginalPortfolio==true">
									<s:if test="%{value.length()>20}">				
                                	<textarea cols=70 rows=4  name='portfolio.cashFlowParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
                                	</s:if>
                                	<s:else>
                                	<INPUT type="text" value='<s:property value="value"/>' name='portfolio.cashFlowParameter.<s:property value="key"/>'/>
                                	</s:else>
								</s:if>
								<s:else>
									<s:if test="%{value.length()>20}">				
                                	<textarea cols=70 rows=4 readonly name='portfolio.cashFlowParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
                                	</s:if>
                                	<s:else>
                                	<INPUT type="text" readonly value='<s:property value="value"/>' name='portfolio.cashFlowParameter.<s:property value="key"/>'/>
                                	</s:else>
								</s:else>							
						</td>
					</tr>
					</s:iterator>
				</table>
				</s:if>
				<s:if test="portfolio.cashFlowParameter==null">
					<s:text name="no.parameters"></s:text>
				</s:if>
				</div>
				<div id="CFSPWindow_footer" align="center">
					<input id="CFSPWindow_confirm" type="button" value="confirm"/>
				</div>
			</div>
			<!-- end of the cashflow strategy parameter pop window -->  
          	
          </td>
        </tr>
        <tr>
          <td width="17%" align="left"><s:text name="asset.allocation"></s:text></td>
          <td width="65%" align="left">
          <s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
          	<s:textfield cssClass="sorlidBorder" id="assetAllocation" name="portfolio.assetAllocationStrategyName" theme="simple"></s:textfield>
          	<input type="hidden" id="assetAllocationStrategyID" value='<s:property value="portfolio.assetAllocationStrategyID"/>'/>
          	<a id="assetAllocationCheck" target="_blank" href='../strategy/View.action?ID=<s:property value="portfolio.assetAllocationStrategyID"/>&action=view'><s:text name="check"></s:text></a>
          </s:if>
          <s:else>
          	<s:property value="portfolio.assetAllocationStrategyName"/>
          	<s:url id="assetAllocation_url" namespace="/jsp/strategy" action="View" includeParams="none">
          		<s:param name="ID" value="#portfolio.assetAllocationStrategyID"></s:param>
          		<s:param name="action">view</s:param>
          	</s:url>
          	<!--<s:a href="%{assetAllocation_url}">check</s:a>-->
          	<a target="_blank" href='<s:property value="assetAllocation_url" escape="false"/>'><s:text name="check"></s:text></a>
          </s:else>	
          	<br>
          	<a href="javascript:void(0);" id="aasp"><s:text name="parameters"></s:text></a>
          	<a class="assetAllocationStrategyParameter" href="../ajax/StrategyParameter.action?name=portfolio.assetAllocationStrategyName&q=STATIC">getParameter</a>
	        <br>
          	
          	<!--asset allocation strategy parameter pop window -->
			<!-- 由于加载是会先生成div，所以必须把div设置为不可视：display:none -->
			<div id="AASPWindow" height="250px" style="display:none">
				<div height="3%" id="CFSPWindow_handle">			
					<a href="javascript:void(0);" id="AASPWindow_close">[ x ]</a>
					<s:text name="asset.allocation.parameters"></s:text>
				</div>
				<div id="AASPWindow_content">
				<s:if test="portfolio.AssetAllocationParameter!=null">
					<table>
						<s:iterator value="portfolio.AssetAllocationParameter" status="a_st">
							<tr>
								<td>
								<s:property value="key"/>
								</td>
								<td>
								<s:if test="portfolio.isOriginalPortfolio==true">
									<s:if test="%{value.length()>20}">				
                                	<textarea cols=70 rows=4  name='portfolio.assetAllocationParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
                                	</s:if>
                                	<s:else>
                                	<INPUT type="text" value='<s:property value="value"/>' name='portfolio.assetAllocationParameter.<s:property value="key"/>'/>
                                	</s:else>
								</s:if>
								<s:else>
									<s:if test="%{value.length()>20}">				
                                	<textarea cols=70 rows=4 readonly name='portfolio.assetAllocationParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
                                	</s:if>
                                	<s:else>
                                	<INPUT type="text" readonly value='<s:property value="value"/>' name='portfolio.assetAllocationParameter.<s:property value="key"/>'/>
                                	</s:else>
								</s:else>
								</td>
							</tr>
						</s:iterator>
					</table>
				</s:if>
				<s:else>
					<s:text name="no.parameters"></s:text>
				</s:else>
				</div>
				<div id="AASPWindow_footer" align="center">
					<input id="AASPWindow_confirm" type="button" value="confirm"/>
				</div>
			</div>
			<!-- end of the asset allocation strategy parameter pop window --> 	        	              
	      </td>
  		</tr>
</table>

<!-- asset modify script -->
<script>
	var assetNumber=0; 

	//there are 20 securties in an asset at most
	var MAX_NUMBER=100; 

	var securityNumber = new Array()
	
	for(i=0;i<MAX_NUMBER;i++){
		securityNumber[i]=0;
	}
	
	function createList(source,targetPane,selectName,hiddenObject,selectVal){
		//alert("hello");
		$source=$('#'+source);
		$hiddenObject=$('#'+hiddenObject);
		$targetPane=$('#'+targetPane);
		$targetPane.html('');
		
		var currentClassID = $hiddenObject.val();
		for(var i=0;i<$('#'+source+' .treeDepth').val();i++){
			//alert("hello");
			$targetPane.append("<select id='"+selectName+i+"'><option value='none'> -- Select -- </option></select>");
			
			$('#'+selectName+i).change( 
				function()
				{
					var $current=$(this);

					var currentNum= $current.attr("id").substring($current.attr("id").length-1,$current.attr("id").length);
					
					var $child=$('#'+selectName+eval(currentNum+'+'+'1'));
					$child.html('<option value="'+this.value+'"> -- Select -- </option>');
					$child.append($('#'+source+" ."+this.value).clone());					

					$child.hide();

					if($child.html()!=null && eval($child.html().length)!=0 && $(this).html()!=$child.html())$child.show();
					
					for(var j=eval(currentNum+'+'+'2');j<$('#'+source+' .treeDepth').val();j++){
						$('#'+selectName+j).html('');
						$('#'+selectName+j).hide();
					}
					$hiddenObject.val(this.value);
					//alert($hiddenObject.val());
				}
			);
		}

		$('#'+selectName+'0').html($('#'+source+" .0").clone());
		$('#'+selectName+'0').trigger("change");
		//$hiddenObject.val($('#'+source+' .defaultValue').val());

		
		
		if(!(typeof selectVal == "undefined")){
			var stack=new Array();
			var parentVal,currentVal;
			if(currentClassID!=selectVal)
				currentVal = currentClassID;
			else
				currentVal = selectVal;
			var elements = $('#'+source+'>option');
			
			for(var num=0;num<10;num++){
				var flag=false;
				for(var i=0;i<elements.length;i++){
					$element=$(elements[i]);
					
					if(eval($element.val())==eval(currentVal)){
					
						stack.push(currentVal);
						currentVal=$element.attr('class');
						flag=true;
						break;
					}
				}
				if(flag==false)
					break;
			}//end while
			
			var triggertimes=stack.length;
			for(var i=0;i<$('#'+source+' .treeDepth').val()&&i<=triggertimes;i++){

				$('#'+selectName+i).val(stack.pop());
				$('#'+selectName+i).trigger("change");
			}
			$hiddenObject.val(selectVal);	
		}
	}
	
	function triggerAjaxContent(pane,ajaxContent,suggest,name){
		$pane=$(pane);
		$ajaxContent=$(ajaxContent);
		$suggest=$(suggest);
		$pane.show();
		$ajaxContent.attr({
					href: '../ajax/GetStrategy.action?name='+name+'&q='+$suggest.val() 
		});
		$ajaxContent.trigger('click');
	}
	
	function makeSublist(parent,child,isSubselectOptional,childVal)
	{
		$("body").append("<select style='display:none' id='"+parent+child+"'></select>");
		$('#'+parent+child).html($("#"+child+" option"));
		
			var parentValue = $('#'+parent).attr('value');
			$('#'+child).html($("#"+parent+child+" .sub_"+parentValue).clone());
		
		childVal = (typeof childVal == "undefined")? "" : childVal ;
		$("#"+child+' option[@value="'+ childVal +'"]').attr('selected','selected');
		
		$('#'+parent).change( 
			function()
			{
				var parentValue = $('#'+parent).attr('value');
				$('#'+child).html($("#"+parent+child+" .sub_"+parentValue).clone());
				if(isSubselectOptional) $('#'+child).prepend("<option value='none'> -- Select -- </option>");
				$('#'+child).trigger("change");
	                        $('#'+child).focus();
			}
		);
	}
	var $window = jQuery(window);	
	function addAsset(){
		var $tr,$td,$a,$table,$security,$div,$th,$input;
		
		$tr = $(document.createElement("tr"));
		$tr.attr({id:assetNumber+1});
		$tr.addClass('assetlist');
		$('#assetList').append($tr);
		
		//asset name
		$td=$(document.createElement("td"));
		$tr.append($td);
		var $inputName=$(document.createElement("input"));
		$td.append($inputName);
		$inputName.attr({name:'portfolio.assetList['+assetNumber+'].Name'});
		$inputName.attr({id:'assetName'+(assetNumber+1)});
		
		//asset class type
		$td=$(document.createElement("td"));
		$td.attr({id:'assets'+(assetNumber+1)+'classIDPane'});
		$tr.append($td);
		$td.append('<input type="hidden" name="portfolio.AssetList['+assetNumber+'].ClassID" id="assetClassID'+(assetNumber+1)+'" value="1">' );
		$td.append('<input type="text" name="portfolio.AssetList['+assetNumber+'].ClassName" id="assetClassName'+(assetNumber+1)+'"  value="EQUITY">');
		$td.append('<input type="button" id="assetType'+(assetNumber+1)+'" value=\'<s:text name="button.change"></s:text>\'> ');
		
		/*asset type pop window*/
		$div = $(document.createElement("div"));
		$td.append($div);
		$div.attr({id: "ATWindow"+(assetNumber+1)});
		//$div.addClass("ATWindow");
		$div.attr({style: "display:none"});
		//the head of the window
		$div = $(document.createElement("div"));
		$("#ATWindow"+(assetNumber+1)).append($div);
		$div.attr({id:"ATWindow"+(assetNumber+1) +"_handle"});
		$div.addClass("ATWindow_handle");
		$div.html('<s:text name="choose.asset.type"></s:text>');
		
		$a = $(document.createElement("a"));
		$div.append($a);
		$a.attr({id:"ATWindow"+(assetNumber+1) +"_close"});
		$a.addClass("ATWindow_close");
		$a.html('[ x ]');
		
		
		//the content of the window
		$div = $(document.createElement("div"));
		$("#ATWindow"+(assetNumber+1)).append($div);
		$div.attr({id:"ATWindow"+(assetNumber+1) +"_content"});
		$div.addClass("ATWindow_content");
		
		//the footer of the window
		$div = $(document.createElement("div"));
		$("#ATWindow"+(assetNumber+1)).append($div);
		$div.attr({id:"ATWindow"+(assetNumber+1) +"_footer"});
		$div.attr("align","center");
		$('#ATWindow'+(assetNumber+1) +"_footer").append('<input type="button" value=\'<s:text name="button.confirm"></s:text>\' id="ATWindow'+(assetNumber+ 1)+'_confirm"/>');
		
		jQuery(function(){	
			$("#ATWindow"+(assetNumber+1)).Draggable(
			{
				zIndex: 	20,
				ghosting:	false,
				opacity: 	0.7,
				handle:		'#ATWindow'+(assetNumber+1) +"_handle"
			});
			
			
			
			$("#ATWindow"+(assetNumber+1)+"_close").click(function()
			{
				$(this).parent().parent().hide();
			});	
			
			$( "#ATWindow"+(assetNumber+1)+"_confirm").click(function()
			{
				var assetTypeId = $(this).attr('id');
				var cut = assetTypeId.indexOf('_');
				var assetID = assetTypeId.substring(8,cut);
				//alert(assetID);
				var assetClassID = $('#assetClassID'+assetID).val();
							
				//alert(assetClassID);
				
				if(assetClassID!=null){
					//alert("hello!");
					$.ajax({
						type:	"post",
						url:	"../ajax/GetAssetClassName.action?AssetClassID=" + assetClassID,
						datatype: "html",
						success: function(result){
							$('#assetClassName'+assetID).val(result);
						}
					})
				}
				for(i=1; i<=assetNumber+1; i++)
				{
					$("#assetType"+i).attr({disabled:false});
				}
				$(this).parent().parent().hide();
			});	
			
			$('#assetType'+(assetNumber+1)).click(function(){
				var assetTypeId = $(this).attr('id');
				var assetID = assetTypeId.substr(9);
				//alert(assetID);
				createList('classTree',"ATWindow"+(assetID) +"_content",'assets'+(assetID)+'classID123456','assetClassID'+(assetID),1);
				$(this).parent().find("div:eq(0)").show();
				for(i=1; i<=assetNumber+1; i++)
				{
					$("#assetType"+i).attr({disabled:true});
				}
				//alert(assetID);
			})
			
		})
		
		//asset strategy name
		$td=$(document.createElement("td"));
		$tr.append($td);
		var $inputAssetStrategy=$(document.createElement("input"));
		$inputAssetStrategy.val("STATIC");
		$td.append($inputAssetStrategy);
		$inputAssetStrategy.attr({name:'portfolio.AssetList['+assetNumber+'].AssetStrategyName'});
		$inputAssetStrategy.attr({id:'assetsStrategy'+(assetNumber+1)});
		
		//the hidden textfield for the strategy id
		$td.append('<input type="hidden" id="assetStrategyID'+(assetNumber+1)+'" value="0">');
		$td.append('<a id="check'+(assetNumber+1)+'" target="_blank" href="../strategy/View.action?ID=0&action=view"><s:text name="check"></s:text></a>' );
		
		/*asset strategy parameters pop window*/
		$div = $(document.createElement("div"));
		$td.append($div);
		$div.attr({id: "ASPWindow"+(assetNumber+1)});
		$div.addClass("ASPWindow");
		$div.attr({style: "display:none"});
		//the head of the window
		$div = $(document.createElement("div"));
		$("#ASPWindow"+(assetNumber+1)).append($div);
		$div.attr({id:"ASPWindow"+(assetNumber+1) +"_handle"});
		$div.addClass("ASPWindow_handle");
		$div.html('<s:text name="asset.parameters"></s:text>');
		
		$a = $(document.createElement("a"));
		$("#ASPWindow"+(assetNumber+1) +"_handle").append($a);
		$a.attr({id:"ASPWindow"+(assetNumber+1) +"_close"});
		$a.addClass("ASPWindow_close");
		$a.html('[ x ]');
		
		//the content of the window
		$div = $(document.createElement("div"));
		$("#ASPWindow"+(assetNumber+1)).append($div);
		$div.attr({id:"ASPWindow"+(assetNumber+1) +"_content"});
		$div.addClass("ASPWindow_content");
		
		//the footer of the window
		$div = $(document.createElement("div"));
		$("#ASPWindow"+(assetNumber+1)).append($div);
		$div.attr({id:"ASPWindow"+(assetNumber+1) +"_footer"});
		$div.attr("align","center");
		$('#ASPWindow'+(assetNumber+1) +"_footer").append('<input type="button" value=\'<s:text name="button.confirm"></s:text>\' id="ASPWindow'+(assetNumber+ 1)+'_confirm"/>');
		
		$a=$(document.createElement("a"));
		$td.append("<br>");
		$td.append($a);
		$a.attr({href:'javascript:void(0);'});
		$a.attr({id:'aspw'+(assetNumber+1)});
		$a.html('parameters');
		
		$a=$(document.createElement("a"));
		$td.append($a);
		$a.attr({href:'../ajax/GetStrategyParameter.action?q=STATIC'});
		$a.attr({id:'assets'+(assetNumber+1)+'assetStrategyParameter'})
		$a.addClass('assetStrategyParameter');
		$a.html('get parameters');

		
		jQuery(function(){
			/*$("#ASPWindow"+(assetNumber+1)).Draggable(
			{
				zIndex: 	20,
				ghosting:	false,
				opacity: 	0.7,
				handle:		'#ASPWindow'+(assetNumber+1) +"_handle"
			});*/	
			
			
			$("#ASPWindow"+(assetNumber+1)).css({
				position: 'absolute',
				width: 600,
				//top: 200,
				left: ($window.width()-600)/2
			});
			
			$("#ASPWindow"+(assetNumber+1)+"_close").click(function()
			{
				$(this).parent().parent().hide();
			});	
			
			$( "#ASPWindow"+(assetNumber+1)+"_confirm").click(function()
			{
				$(this).parent().parent().hide();
			});	
			
			$('#aspw'+(assetNumber+1)).click(function(){
				$(this).parent().find("div:eq(0)").show();
			})
			
		})
		
		jQuery(function() { $('#assets'+(assetNumber+1)+'assetStrategyParameter').ajaxContent({
				target:'#ASPWindow'+(assetNumber+1) +"_content",
				success: function(obj,target,msg){
					 //$(obj).css({color: 'blue'});
					 $(target).css({border:'1px solid #CCCCCC'});
					 //$(target).parent().show();
					 //alert(msg);
					 if(msg.indexOf("No Parameter") == -1){
					 	$(target).parent().show();
					 }
				},
					 
				error: function(target){
					$(target).css({color: 'red',fontSize:'18px',border:'1px solid #FF0000'});
				},
				errorMsg:'Something went wrong'
			});
		});	
		jQuery(function() {  jQuery('#assetsStrategy'+(assetNumber+1)).suggest("../ajax/GetStrategySuggestTxt.action?type=ASSET STRATEGY",
													{ parameter1:'#ASPWindow'+(assetNumber+1) +"_content",
													  parameter2:'#assets'+(assetNumber+1)+'assetStrategyParameter',
													  parameter3:'portfolio.AssetList['+assetNumber+'].assetStrategyParameter',
													  parameter4:"#assetStrategyID"+(assetNumber+1),
													  runOnSelect2:true
													});});	
		
		//target percentage											
		$td=$(document.createElement("td"));
		$tr.append($td);
		var $inputTartetPercentage=$(document.createElement("input"));
		$td.append($inputTartetPercentage);
		$inputTartetPercentage.attr({name:'portfolio.AssetList['+assetNumber+'].TargetPercentageStr'});
		$inputTartetPercentage.attr({id: "targetPercenttage"+assetNumber});
		
		//actual percentage
		$td=$(document.createElement("td"));
		$tr.append($td);
		
		//Amount
		$td=$(document.createElement("td"));
		$tr.append($td);
		
		//current securities
		$td=$(document.createElement("td"));
		$tr.append($td);
		$td.attr({id:"showsecurities_"+assetNumber});
		$a=$(document.createElement("a"));
		$td.append($a);
		$a.attr("href","javascript:void(0);");
		$a.attr("onclick","showhide('#security-"+(assetNumber+1)+"')");
		$a.html('<s:text name="asset.securities"></s:text>');
		
		//delete asset
		$td=$(document.createElement("td"));
		$tr.append($td);
		$a=$(document.createElement("a"));
		$td.append($a);
		$a.attr({id:"delAsset"+assetNumber});
		$a.attr("href","javascript:void(0);");
		$a.html('<s:text name="button.delete"></s:text>');
		$td.append('<input type="hidden" id="rowNum'+assetNumber+'" value="'+(assetNumber+2)+'">');
		
		$("a[id^=delAsset]").click(function(){
			//var asset = document.getElementById("assetList");
			$rowNum = $(this).parent().find("input");		//find the container of row number
			var row = $rowNum.val()
			$("#"+(row-1)).hide();
			$("#assetName"+(row-1)).val("");
			$("#security-"+(row-1)).hide();
		})
		
		//security list
		$tr=$(document.createElement("tr"));	
		$('#assetList').append($tr);
		$tr.attr({id: "security-"+(assetNumber+1)});
		$td=$(document.createElement("td"));
		$tr.append($td);
		$td.attr("colSpan",8);

		// table 
		$table = $(document.createElement("table"));
		$td.append($table);
		$table.attr({id:"security"+assetNumber});
		$table.attr({style:"width:100% height:100% cellpadding:0 cellspacing:0"});
		$table.attr("width","100%");
		$table.attr("height","100%");
		$table.addClass("security");
		$tr=$(document.createElement("tr"));
		$table.append($tr);
		
		$th=$(document.createElement("th"));
		$tr.append($th);
		$th.attr({style:"width:18%"});
		$th.html('<s:text name="symbol"></s:text>');
		
		$th=$(document.createElement("th"));
		$tr.append($th);
		$th.attr({style:"width:15%"});
		$th.html('<s:text name="share"></s:text>');
		
		$th=$(document.createElement("th"));
		$tr.append($th);
		$th.attr({style:"width:10%"});
		$th.html('<s:text name="price"></s:text>');
		
		$th=$(document.createElement("th"));
		$tr.append($th);
		$th.attr({style:"width:15%"});
		$th.html('<s:text name="reinvested"></s:text>');
		
		$th=$(document.createElement("th"));
		$tr.append($th);
		$th.attr({style:"width:18%"});
		$th.html('<s:text name="security.amount"></s:text>');
		
		$th=$(document.createElement("th"));
		$tr.append($th);
		$th.attr({style:"width:14%"});
		$th.html('<s:text name="in.asset.percentage"></s:text>');
		
		$th=$(document.createElement("th"));
		$tr.append($th);
		$th.attr({style:"width:10%"});
		$th.html('<s:text name="operation"></s:text>');
		
		//add security
		$tr=$(document.createElement("tr"));
		$table.append($tr);
		$td=$(document.createElement("td"));
		$tr.append($td);
		$td.attr("colSpan",8);
		$td.attr("align","center");
		$a=$(document.createElement("a"));
		$td.append($a);
		$a.attr("href","javascript:void(0);");
		$a.html('<s:text name="add.security"></s:text>');
		//$a.attr("onClick","addSecurity("+assetNumber+")");
		//$a.attr({onclick:'addSecurity('+assetNumber+')'});
		$a.attr({id:"addSecurity-"+assetNumber});
		$("#addSecurity-"+assetNumber).click(function(){
			var id = $(this).attr('id');
			var cut = id.indexOf("-");
			var assetID = id.substr(cut+1);
			addSecurity(assetID);
		})
		assetNumber++;
	}
	function addSecurity(assetID){
		var $securityList=$('#security'+assetID);
		var $tr,$td;
		$tr=$(document.createElement("tr"));
		$securityList.append($tr);
		$tr.attr({id:"securityItem-"+assetID+"-"+securityNumber[assetID]});
		$tr.attr({bgcolor:"#c3daf9"});
		$td=$(document.createElement("td"));
		$tr.append($td);
		
		/*append the security symbol*/
		var $securityInput=$(document.createElement("input"));
		$securityInput.attr({id:'securitySymbol-'+(assetID)+'-'+(securityNumber[assetID])});
		$securityInput.attr({name:'portfolio.AssetList['+assetID+'].SecurityList['+securityNumber[assetID]+'].Symbol'});
		$securityInput.addClass('securitySymbolClass-'+(assetID)+'-'+(securityNumber[assetID]))
		$td.append($securityInput);

		jQuery(function() {  jQuery('.securitySymbolClass-'+(assetID)+'-'+(securityNumber[assetID])).suggest("../ajax/GetSecuritySuggestTxt.action",
						{ 	haveSubTokens: true,
							runOnSelect3: true
						})});
	
		/*append the shares of the security*/	
		$td=$(document.createElement("td"));
		$tr.append($td);
		var $shares=$(document.createElement("input"));
		$shares.attr({id:'securityShares-'+(assetID)+'-'+(securityNumber[assetID])})
		$shares.attr({name:'portfolio.AssetList['+assetID+'].SecurityList['+securityNumber[assetID]+'].Shares'});
		$shares.attr({readonly:true});
		$td.append($shares);
		
		/*append the price of the security*/
		$td=$(document.createElement("td"));
		$tr.append($td);
		var $price=$(document.createElement("input"));
		$price.attr({id:'securityPrice-'+(assetID)+'-'+(securityNumber[assetID])})
		$price.attr({readonly:true});
		$td.append($price);
		
		/*append the reinvesting option*/
		$td=$(document.createElement("td"));
		$tr.append($td);
		var $reinvesting = $(document.createElement("select"));
		$reinvesting.attr({name:'portfolio.AssetList['+assetID+'].SecurityList['+securityNumber[assetID]+'].Reinvesting'});
		$reinvesting.append('<option value="false">No</option>');
		$reinvesting.append('<option value="true">yes</option>');
		$td.append($reinvesting);
		
		/*append the total amount of the security*/
		$td=$(document.createElement("td"));
		$tr.append($td);
		var $totalAmount=$(document.createElement("input"));
		$totalAmount.attr({id:'securityTotalAmount-'+(assetID)+'-'+(securityNumber[assetID])})
		$totalAmount.attr({name:'portfolio.AssetList['+assetID+'].SecurityList['+securityNumber[assetID]+'].OriginalAmount'});
		$td.append($totalAmount);
		
       $('#'+'securityTotalAmount-'+(assetID)+'-'+(securityNumber[assetID])).change(function(){
        	var amount = $(this).val();
        	var id = $(this).attr("id");
        	//get the post of the id
			var cut_index = id.indexOf("-");
			var post = id.substr(cut_index);
			var symbol = $("#securitySymbol"+post).val();
			//alert(symbol);
			if(symbol!=null){
				$.ajax({
					type:	"post",
					url:	"../ajax/GetPrice.action?Symbol=" + symbol + "&StartingDate =" +  $("#startingDate").val(),
					datatype: "html",
					success: function(result){
						if(result!=null && result!=0.0){
							var price = result;
		                	if(amount!=null && checkRate(amount)==true)
		                	{
		                		var shares = amount/price;
		                		//alert(shares);
		                		shares = dataFormat1(shares);
		                		$("#securityShares"+post).val(shares);
		                	}
						}
						else
						{
							$("#"+'securityPrice'+ post).val("");
							$('#'+'securityShares'+ post).val("");
						}
					}
				})
        	}
        })
        
        function checkRate(input)
		{
		     var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/   
		     if (!re.test(input))
		    {
		        alert("please input the number(e.g:0.02)");
		        return false;
		     }
		     else
		     	return true;
		}		
		
		/*append the percentage in the asset of the security*/
		$td=$(document.createElement("td"));
		$tr.append($td);
		
		/*append the delete function to the new security*/
		$td=$(document.createElement("td"));
		$tr.append($td);
		$a=$(document.createElement("a"));	
		$td.append($a);
		$a.attr({id:"delSecurity"+assetID+"-"+securityNumber[assetID]});
		//$a.attr("onClick","delSecurity("+assetID+","+securityNumber[assetID]+")")
		$a.attr("href","javascript:void(0);");
		
		/*delete the new security using link id... attention: new change the link id!*/	
		$("#"+"delSecurity"+assetID+"-"+securityNumber[assetID]).click(function(){
			var id = $(this).attr('id');
			var cut = id.indexOf("-");
			var assetID = id.substring(11, cut);
			var securityID = id.substr(cut+1);
			//alert(assetID + securityID);
			delSecurity(assetID, securityID);
		})
		
		
		function delSecurity(assetID, securityID){
			$("#securityItem-"+assetID+"-"+(securityID)).hide();
			$("#securitySymbol-"+assetID+"-"+securityID).val("");
			$("#securityName-"+assetID+"-"+securityID).val("");
		}
		$a.html('<s:text name="button.delete"></s:text>');
		$td.append('<input type="hidden" id="rowNum'+assetNumber+'" value="'+(assetNumber+2)+'">');
		
		securityNumber[assetID]=securityNumber[assetID]+1;
	}
	
</script>
<!-- asset  list --> 

<table id="assetList" cellpadding="0" cellspacing="0" width="96%" align="center">
<!-- the head of the list -->
<thead>
	<tr class="asset">
		<th width="15%"><s:text name="asset.name"></s:text></th>
		<th width="15%"><s:text name="asset.type"></s:text></th>
		<th width="25%"><s:text name="strategy.name"></s:text></th>
		<th width="5%"><s:text name="target.percentage"></s:text></th>
		<th width="10%"><s:text name="actual.percentage"></s:text></th>
		<th width="10%"><s:text name="total.amount"></s:text></th>
		<th width="10%"><s:text name="securities"></s:text></th>
		<th width="10%"><s:text name="operation"></s:text></th>
	</tr>
</thead>
<tbody>
<s:if test="portfolio.IsOriginalPortfolio==true && portfolio.state!=2 && (isOwner == true || #request.operation==true)">
	<tr class="assetlist">
		<td colspan="8" align="center"><a href="javascript:void(0);" onclick="addAsset()"><s:text name="add.asset"></s:text></a></td>
	</tr>
</s:if>
<!-- asset information -->
<s:if test="portfolio.AssetList!=null && portfolio.AssetList.size() > 0">
<s:iterator id="assets" value="portfolio.AssetList" status="st">
	<script type="text/javascript">
	jQuery(function(){
		$('#assetName<s:property value="#st.count"/>, #assetStrategyName<s:property value="#st.count"/>, #targetPercentage<s:property value="#st.count"/>').toggleVal("active");
	})
	</script>
	<tr class="assetlist" id='<s:property value="#st.count"/>'>
		<td>
			<s:if test="portfolio.IsOriginalPortfolio==true && portfolio.state!=2">
				<input class="sorlidBorder" name='portfolio.AssetList[<s:property value="#st.count-1"/>].Name' value='<s:property value="name"/>' id='assetName<s:property value="#st.count"/>' type="text"/>
			</s:if>
			<s:elseif test="portfolio.IsOriginalPortfolio==false && portfolio.state==2">
				<input readonly class="sorlidBorder" name='portfolio.AssetList[<s:property value="#st.count-1"/>].Name' value='<s:property value="name"/>' type="text"/>
			</s:elseif>
			<s:else>
				<s:property value="name"/>
			</s:else>
		</td>
		<td id='assets<s:property value="#st.count"/>classIDPane'>
			<s:if test="portfolio.IsOriginalPortfolio==true && portfolio.state!=2 && isOwner == true">
				<input name='portfolio.AssetList[<s:property value="#st.count-1"/>].ClassID' value='<s:property value="classID"/>' id='assetClassID<s:property value="#st.count"/>' type="hidden"/>
				<input name='portfolio.AssetList[<s:property value="#st.count-1"/>].ClassName' class="sorlidBorder" readonly value='<s:property value="className"/>' id='assetClassName<s:property value="#st.count"/>' type="text"/>
				<br>
				<input id="assetType<s:property value='#st.count'/>" type="button" value='<s:text name="button.change"></s:text>' />
				
				<!--asset type pop window -->
				<!-- 由于加载是会先生成div，所以必须把div设置为不可视：display:none -->
				<div id='ATWindow<s:property value="#st.count"/>' height="250px" class="ATWindow" style="display:none">
					<div height="3%" id='ATWindow<s:property value="#st.count"/>_handle' class="ATWindow_handle">			
						<a href="javascript:void(0);" id='ATWindow<s:property value="#st.count"/>_close' class="ATWindow_close">[ x ]</a>
						<s:text name="choose.asset.type"></s:text>
					</div>
					<div id='ATWindow<s:property value="#st.count"/>_content' class="ATWindow_content">
					</div>
					<div id='ATWindow<s:property value="#st.count"/>_footer' align="center">
						<input id='ATWindow<s:property value="#st.count"/>_confirm' type="button" value="confirm"/>
					</div>
				</div>	
				<script type="text/javascript">
					jQuery(function(){
						$('#ATWindow<s:property value="#st.count"/>').Draggable(
						{
							zIndex: 	20,
							ghosting:	false,
							opacity: 	0.7,
							handle:		'#ATWindow<s:property value="#st.count"/>_handle'
						});	
						
						$('#ATWindow<s:property value="#st.count"/>_close').click(function()
						{
							for(i=1; i<=assetNumber; i++)
							{
								$("#assetType"+i).attr({disabled:false});
							}
							$('#ATWindow<s:property value="#st.count"/>').hide();
						});
						
						$('#ATWindow<s:property value="#st.count"/>_confirm').click(function()
						{
							for(i=1; i<=assetNumber; i++)
							{
								$("#assetType"+i).attr({disabled:false});
							}
							$('#ATWindow<s:property value="#st.count"/>').hide();
							
							var assetClassID = $('#assetClassID<s:property value="#st.count"/>').val();
							
							//alert(assetClassID);
							if(assetClassID!=null){
								//alert("hello!");
								$.ajax({
									type:	"post",
									url:	"../ajax/GetAssetClassName.action?AssetClassID=" + assetClassID,
									datatype: "html",
									success: function(result){
										$('#assetClassName<s:property value="#st.count"/>').html('');
										$('#assetClassName<s:property value="#st.count"/>').val(result);
									}
								})
							}
						});	
						$('#assetType<s:property value="#st.count"/>').click(function(){
							createList('classTree','ATWindow<s:property value="#st.count"/>_content','assets<s:property value="#st.count"/>classID123456','assetClassID<s:property value="#st.count"/>','<s:property value="classID"/>');
							$('#ATWindow<s:property value="#st.count"/>').show();
							for(i=1; i<=assetNumber; i++)
							{
								$("#assetType"+i).attr({disabled:true});
							}
						})
					});
				</script>	
			</s:if>
			<s:else>
				<input name='portfolio.AssetList[<s:property value="#st.count-1"/>].ClassID' value='<s:property value="classID"/>' id='assetClassID<s:property value="#st.count"/>' type="hidden"/>
				<input class="sorlidBorder" readonly name='portfolio.AssetList[<s:property value="#st.count-1"/>].ClassName' value='<s:property value="className"/>' id='assetClassName<s:property value="#st.count"/>' type="text"/>
			</s:else>	

		</td>
		<td>
			<s:if test="portfolio.IsOriginalPortfolio==true && portfolio.state!=2">
				<input class="sorlidBorder" name='portfolio.AssetList[<s:property value="#st.count-1"/>].AssetStrategyName' value="<s:property value='assetStrategyName'/>" id='assetStrategyName<s:property value="#st.count"/>' type="text" />
				<input type="hidden" id='assetStrategyID<s:property value="#st.count"/>' value="<s:property value='assetStrategyID'/>" />
				<a id='check<s:property value="#st.count"/>' target="_blank" href="../strategy/View.action?ID=<s:property value='assetStrategyID'/>&action=view"><s:text name="check"></s:text></a>
			</s:if>
			<s:elseif test="portfolio.IsOriginalPortfolio==false && portfolio.state==2">
				<input readonly class="sorlidBorder" name='portfolio.AssetList[<s:property value="#st.count-1"/>].AssetStrategyName' value="<s:property value='assetStrategyName'/>" type="text" />
				<a id='check<s:property value="#st.count"/>' target="_blank" href="../strategy/View.action?ID=<s:property value='assetStrategyID'/>&action=view"><s:text name="check"></s:text></a>
			</s:elseif>
			<s:else>
				<s:url id="assetStrategy_url" action="View" namespace="/jsp/strategy" includeParams="none">
					<s:param name="ID" value="assetStrategyID"></s:param>
					<s:param name="action">view</s:param>
				</s:url>
				<!--<s:a href="%{assetStrategy_url}" cssStyle="target:_blank"> <s:property value='assetStrategyName'/> </s:a>-->
				<s:property value='assetStrategyName'/> 
				<a target="_blank" href='<s:property value="assetStrategy_url" escape = "false"/>'><s:text name="check"></s:text></a>
			</s:else>
			<br>
			<a href="javascript:void(0);" id='aspw<s:property value="#st.count"/>'><s:text name="parameters"></s:text></a>
			<script type="text/javascript">
				$('#aspw<s:property value="#st.count"/>').click(function(){
					$('#ASPWindow<s:property value="#st.count"/>').show();
				})
			</script>			
			<a class="assetStrategyParameter" id='assets<s:property value="#st.count"/>assetStrategyParameter' href='../ajax/GetStrategyParameter.action?name=portfolio.assetList[<s:property value="#st.count-1"/>].assetStrategyParameter&q=STATIC'>parameters</a>
			<!--asset strategy parameters pop window -->
			<!-- 由于加载是会先生成div，所以必须把div设置为不可视：display:none -->
			<div id='ASPWindow<s:property value="#st.count"/>' height="250px" class="ASPWindow" style="display:none">
				<div height="3%" id='ASPWindow<s:property value="#st.count"/>_handle' class="ACPWindow_handle">			
					<a href="javascript:void(0);" id='ASPWindow<s:property value="#st.count"/>_close' class="ASPWindow_close">[ x ]</a>
					<s:text name="asset.parameters"></s:text>
				</div>
				<div id='ASPWindow<s:property value="#st.count"/>_content' class="ASPWindow_content">
					<s:if test="assetStrategyParameter==null">
						<s:text name="no.parameters"></s:text>
					</s:if>
					<s:if test="assetStrategyParameter!=null">
                		<TABLE>
                		<s:iterator value="assetStrategyParameter" status="st_parameter">
                			<TR><TD>
                				<s:property value="key"/>
                				</TD>
                                <TD>
                               	
	                                <s:if test="portfolio.isOriginalPortfolio==true">
										<s:if test="%{value.length()>20}">				
	                                	<textarea cols=70 rows=4  name='portfolio.AssetList[<s:property value="#st.count-1"/>].assetStrategyParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
	                                	</s:if>
	                                	<s:else>
	                                	<INPUT type="text" value='<s:property value="value"/>' name='portfolio.AssetList[<s:property value="#st.count-1"/>].assetStrategyParameter.<s:property value="key"/>'/>
	                                	</s:else>
									</s:if>
									<s:else>
										<s:if test="%{value.length()>20}">				
	                                	<textarea cols=70 rows=4 readonly name='portfolio.AssetList[<s:property value="#st.count-1"/>].assetStrategyParameter.<s:property value="key"/>'><s:property value="value"/></textarea>
	                                	</s:if>
	                                	<s:else>
	                                	<INPUT type="text" readonly value='<s:property value="value"/>' name='portfolio.AssetList[<s:property value="#st.count-1"/>].assetStrategyParameter.<s:property value="key"/>'/>
	                                	</s:else>
									</s:else>
                                	
                                </TD>
                            </TR>
							</s:iterator>
                		</TABLE>
                	</s:if>
				</div>
				<div id='ASPWindow<s:property value="#st.count"/>_footer' align="center">
					<input id='ASPWindow<s:property value="#st.count"/>_confirm' type="button" value="confirm"/>
				</div>
			</div>
			<!-- end of the asset strategy parameters pop window -->
			<!-- inintialize the window -->
			<script type="text/javascript">
			jQuery(function(){
				/*$('#ASPWindow<s:property value="#st.count"/>').Draggable(
				{
					zIndex: 	20,
					ghosting:	false,
					opacity: 	0.7,
					handle:		'#ASPWindow<s:property value="#st.count"/>}_handle'
				});*/
				$('#ASPWindow<s:property value="#st.count"/>').css({
					position: 'absolute',
					width: 600,
					//top: 200,
					left: ($window.width()-600)/2
				});	
				
				$('#ASPWindow<s:property value="#st.count"/>_close').click(function()
				{
					$('#ASPWindow<s:property value="#st.count"/>').hide();
				});	
				
				$('#ASPWindow<s:property value="#st.count"/>_confirm').click(function()
				{
					$('#ASPWindow<s:property value="#st.count"/>').hide();
				});	
				
			});
			$('#assets<s:property value="#st.count"/>assetStrategyParameter').ajaxContent({
					target:'#ASPWindow<s:property value="#st.count"/>_content',
					success: function(obj,target,msg){
						 $(target).css({border:'1px solid #CCCCCC'});
						 $('#ASPWindow<s:property value="#st.count"/>').show();
					},
					error: function(target){
						 $(target).css({color: 'red',fontSize:'18px',border:'1px solid #FF0000'});
					},
					errorMsg: 'Something went wrong'
			});				
			jQuery(function() {  jQuery('#assetStrategyName<s:property value="#st.count"/>').suggest("../ajax/GetStrategySuggestTxt.action?type=ASSET STRATEGY",
														{ parameter1:'#ASPWindow<s:property value="#st.count"/>_content',
														  parameter2:'#assets<s:property value="#st.count"/>assetStrategyParameter',
														  parameter3:'portfolio.AssetList[<s:property value="#st.count-1"/>].assetStrategyParameter',
														  parameter4:'#assetStrategyID<s:property value="#st.count"/>',
														  runOnSelect2:true
														});});	
			</script>	
	 	</td>
		<td>
			<s:if test="portfolio.IsOriginalPortfolio==true && portfolio.state!=2">
				<input class="sorlidBorder" maxlength="5" name="portfolio.AssetList[<s:property value="#st.count-1"/>].TargetPercentageStr" value='<s:property value="targetPercentageStr"/>' id='targetPercentage<s:property value="#st.count"/>' type="textfield">
			</s:if>
			<s:elseif test="portfolio.IsOriginalPortfolio==false && portfolio.state==2">
				<input class="sorlidBorder" readonly maxlength="5" name="portfolio.AssetList[<s:property value="#st.count-1"/>].TargetPercentageStr" value='<s:property value="targetPercentageStr"/>' type="textfield">
			</s:elseif>
			<s:else>
				<s:property value="targetPercentageStr"/>
			</s:else>
		</td>
		<td>
			<script type="text/javascript">
			dataFormat2(<s:property value="Percentage"/>);
			</script>
		</td>
		<td>
		<script>
			dataFormat(<s:property value="Amount"/>);
		</script>
		</td>
		<td id='showSecurities-<s:property value="#st.count"/>'>
			<a href="javascript:void(0);" onclick="showhide('#security-<s:property value="#st.count"/>')"><s:text name="asset.securities"></s:text></a>
		</td>
		<td>
			<input type="hidden" id='rowNum<s:property value="#st.count"/>' value='<s:property value="#st.count+1"/>'/>
			<s:if test="portfolio.IsOriginalPortfolio==true && portfolio.state!=2">
				<a onclick="delAsset(<s:property value="#st.count-1"/>)" href="javascript:void(0);"><s:text name="delete"></s:text></a>
			</s:if>
			<s:else>
				&nbsp;
			</s:else>
		</td>
	</tr>
	<!-- security information -->
	<tr style="display:none" id='security-<s:property value="#st.count"/>'>
		<td height="38" colspan="8">
		  <table id='security<s:property value="#st.count-1"/>' width="100%" height="100%" cellpadding="0" cellspacing="0" class="security">
              <tr bgcolor="#c3daf9" style="">
                <th width="18%"><s:text name="symbol"></s:text></th>
                <th width="15%"><s:text name="share"></s:text></th>
                <th width="10%"><s:text name="price"></s:text></th>
                <th width="15%"><s:text name="reinvested"></s:text></th>
				<th width="18%"><s:text name="security.amount"></s:text></th>
				<th width="14%"><s:text name="in.asset.percentage"></s:text>(%)</th>
				<th width="10%"><s:text name="operation"></s:text></th>
              </tr>
              <tr>
              	<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
              		<td colspan="8" align="center"><a id="addSecurity-<s:property value='#st.count-1'/>" href="javascript:void(0);" onClick="addSecurity(<s:property value="#st.count-1"/>)"><s:text name="add.security"></s:text></a></td>
              	</s:if>
              </tr>	
              <s:iterator value="securityList" status="st_si" id="securityItems">
				<script type="text/javascript">
				jQuery(function(){
					$('#securitySymbol-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>, #securityTotalAmount-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>').toggleVal("active");
				})
				</script>
              <tr bgcolor="#c3daf9" id='securityItem-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'>
                <td id='shortName<s:property value="#st_si.count-1"/>'>
                	<s:if test="portfolio.IsOriginalPortfolio==true && portfolio.state!=2">
                	<INPUT id='securitySymbol-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
                         		name='portfolio.AssetList[<s:property value="#st.count-1"/>].securityList[<s:property value="#st_si.count-1"/>].Symbol'
                         		value='<s:property value="symbol"/>'
                         		class='securitySymbolClass-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
								autocomplete="off"/>
					</s:if>
					<s:elseif test="portfolio.IsOriginalPortfolio==false && portfolio.state==2">
                	<INPUT 		readonly
                				id='securitySymbol-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
                         		name='portfolio.AssetList[<s:property value="#st.count-1"/>].securityList[<s:property value="#st_si.count-1"/>].Symbol'
                         		value='<s:property value="symbol"/>'
                         		class='securitySymbolClass-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
								/>
					</s:elseif>
					<s:else>
						<s:property value="symbol"/>
					</s:else>
                	<INPUT id='securityName-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
                				type="hidden",
                         		name='portfolio.AssetList[<s:property value="#st.count-1"/>].securityList[<s:property value="#st_si.count-1"/>].SecurityName'
                         		value='<s:property value="securityName"/>'
                         		class='securityNameClass-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'/>
					<script type="text/javascript">
					jQuery(function(){
						jQuery('.securitySymbolClass-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>').suggest("../ajax/GetSecuritySuggestTxt.action",
												{	haveSubTokens: true,
													runOnSelect3:true
												});
					})
					
				
					</script>
                </td>
                <td>
                	<input class="sorlidBorder" type="text" id='securityShares-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
                		name='portfolio.AssetList[<s:property value="#st.count-1"/>].SecurityList[<s:property value="#st_si.count-1"/>].Shares'
                		value='<s:property value="shares"/>'
                		readonly
                	 />
                </td>
                <td>
                	<input class="sorlidBorder" type="hidden" id='securityPrice-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
                		value='<s:property value="price"/>'
                		readonly
                	 />
                	 <script>
                	 	dataFormat(<s:property value="price"/>);
                	 </script>
                </td>
                <td>

	            <SELECT name='portfolio.AssetList[<s:property value="#st.count-1"/>].SecurityList[<s:property value="#st_si.count-1"/>].Reinvesting'>
				         <OPTION value="false" <s:if test="reinvesting==false">selected</s:if> >No</OPTION>
				         <OPTION value="true" <s:if test="reinvesting==true">selected</s:if> >yes</OPTION>
				</SELECT>
                </td>
				<td>
				<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
				<INPUT class="sorlidBorder" name='portfolio.AssetList[<s:property value="#st.count-1"/>].SecurityList[<s:property value="#st_si.count-1"/>].OriginalAmount'
                			id='securityTotalAmount-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
                  					value='<s:property value="originalAmount"/>'/>
                <script>
                $('#securityTotalAmount-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>').change(function(){
                	var amount = $(this).val();
                	var id = $(this).attr("id");
                	//get the post of the id
					var cut_index = id.indexOf("-");
					var post = id.substr(cut_index);
					var symbol = $("#securitySymbol"+post).val();
					//alert(symbol);
					if(symbol!=null){
						$.ajax({
							type:	"post",
							url:	"../ajax/GetPrice.action?Symbol=" + symbol + "&StartingDate =" +  $("#startingDate").val(),
							datatype: "html",
							success: function(result){
								if(result!=null && result!=0.0){
									var price = result;
				                	if(amount!=null && checkRate(amount)==true)
				                	{
				                		var shares = amount/price;
				                		//alert(shares);
				                		shares = dataFormat1(shares);
				                		$("#securityShares"+post).val(shares);
				                	}
								}
								else
								{
									$("#"+'securityPrice'+ post).val("");
									$('#'+'securityShares'+ post).val("");
								}
							}
						})
                	}
                	
                })
                
                function checkRate(input)
				{
				     var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/   
				     if (!re.test(input))
				    {
				        alert("please input the number(e.g:0.02)");
				        return false;
				     }
				     else
				     	return true;
				}
                </script>
               	</s:if>
               	<s:elseif test="portfolio.isOriginalPortfolio==false && portfolio.state==2">
				<INPUT readonly class="sorlidBorder" name='portfolio.AssetList[<s:property value="#st.count-1"/>].SecurityList[<s:property value="#st_si.count-1"/>].OriginalAmount'
                			id='securityTotalAmount-<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>'
                  					value='<s:property value="originalAmount"/>'/>
                </s:elseif>
               	<s:else>
               		<script>
               			dataFormat(<s:property value="totalAmount"/>);
               		</script>
               		
               	</s:else>
                </td>
				<td>
					<script>
						dataFormat2(<s:property value="percentage"/>);
					</script>
				</td>
				<td>
					<input type="hidden" id='rowNum<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>' value='<s:property value="#st_si.count+1"/>'/>
					
					<s:if test="portfolio.isOriginalPortfolio==true">
						<a id='delSecurity<s:property value="#st.count-1"/>-<s:property value="#st_si.count-1"/>' href='javascript:void(0);' onclick='delSecurity(<s:property value="#st.count-1"/>,<s:property value="#st_si.count-1"/>)'>
						<s:text name="button.delete"></s:text>
						</a>
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</td>
              </tr>
              <!-- record each asset has how many securities -->
	      		<script>
				securityNumber[<s:property value="#st.count-1"/>]=securityNumber[<s:property value="#st.count-1"/>]+1;
	      		</script>              
              </s:iterator>
          </table>
		 </td>
	</tr>	
	<script>
		assetNumber++;
	</script>	
</s:iterator>
</s:if>
</tbody>
</table>

<s:if test="portfolio.isOriginalPortfolio==false">
<p>
<div align="center">
	<div align="center"><font size="2" face="Arial"><strong><s:text name="current.securities.list"></s:text></strong></font></div>
	<div align="center">
		<table id="securityTable" class="security">
			<tr>
				<th width="15%"><s:text name="asset.name"></s:text></th>
				<th width="15%"><s:text name="security.name"></s:text></th>
				<th width="15%"><s:text name="share"></s:text></th>
				<th width="10%"><s:text name="price"></s:text></th>
				<th width="18%"><s:text name="security.amount"></s:text></th>
				<th width="14%"><s:text name="in.asset.percentage"></s:text>(%)</th>
				<th width="15%"><s:text name="reinvested"></s:text></th>
			</tr>
			<s:iterator id="assets" value="portfolio.AssetList" status="st">
				<s:iterator value="securityList" status="st_si" id="securityItems">	
				
				<s:if test="SecurityType == 6">
					<s:url id="security_url" namespace="/jsp/portfolio" action="Edit">
						<s:param name="ID" value="PortfolioID"></s:param>
						<s:param name="action">view</s:param>
					</s:url>
				</s:if>
				<s:else>
					<s:url id="security_url" namespace="/jsp/fundcenter" action="View" includeParams="none">
						<s:param name="symbol" value="Symbol"></s:param>
						<s:param name="includeHeader">true</s:param>
						<s:param name="title" value="Symbol"></s:param>
					</s:url>
				</s:else>
				<tr>
					<td><s:property id="assets" value="name"/></td>
					<td><a target="_blank" href=<s:property value="security_url" escape="false"/> ><s:property value="Symbol"/></a></td>
					<td>
						<script>
	                	 	dataFormat(<s:property value="shares"/>);
	                	 </script>
					</td>
					<td>
						<script>
	                	 	dataFormat(<s:property value="price"/>);
	                	 </script>
					</td>
					<td>
						<script>
               				dataFormat(<s:property value="totalAmount"/>);
               			</script>
					</td>
					<td>
						<script>
							dataFormat2(<s:property value="percentage"/>);
						</script>
					</td>
					<td><s:property value="reinvesting"/></td>
				</tr>
				</s:iterator>
			</s:iterator>
		</table>
	</div>
</div>
</p>
</s:if>




<div style="margin-top:20px">
	<table width="100%">
	<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
		<tr>		
			<td>
				<s:if test='#request.isOwner==true || action.equals("create")'>
				<div id="portfolio_3par" align="center">
					Third party interface <s:textfield name="portfolio.useThirdPartyAPI" id='useThirdPartyAPI' theme='simple'></s:textfield><input type='checkbox' id='run_in_server_flag'>run on server
					<input type='checkbox' name='keepOriginalState' value="true">Keep Original State
				</div>
				</s:if>
				<div align="center" id="buttonDiv">
				<s:if test='#request.isOwner==true || action.equals("create")'>
	                  <a id="Save" href='javascript:save()'><s:text name="save"></s:text></a>
	            </s:if>
                  <script>
                  	function save_as(){
                  		$("#confirm_window").dialog({height:250,weight:300,title:"Fill In The New Name"});
                  	}
                  </script>
                <s:if test="operation == true ||#request.isOwner==true">
                  <a id="saveas" href='javascript:save_as()'><s:text name="save.as"></s:text></a> 
                </s:if>
                  <s:if test="#request.isOwner==true && ID != null && ID != 0">
	                  <a id="monitor" href='javascript:monitor()'><s:text name="monitor"></s:text></a> 
	                  <a id="update" href='javascript:update()'><s:text name="update"></s:text></a>
	                  <a id="update" href='javascript:e()'><s:text name="evaluate"></s:text></a>
                  </s:if>
                </div>
			</td>
		</tr>
	</s:if>
	<s:elseif test="portfolio.isOriginalPortfolio==false && portfolio.state==2">
	<tr>
		<td>
			<div align="center" id="buttonDiv">
				<a id="copy" href="javascript:copy()"><s:text name="save"></s:text></a>
				<a id="copyAs" href="javascript:copy_as()"><s:text name="save.as"></s:text></a>
				<script>
					function copy_as()
					{
						$("#confirm_window").dialog({height:250,weight:300,title:"Fill In The New Name"});
					}
                </script>
			</div>
		</td>
	</tr>
	</s:elseif>
	</table> 
</div>    
<div style="display:none">
	<div id="confirm_window" class="flora">
		<table width="100%" border="0" cellspacing="8" cellpadding="0" align="center">
			<tr>
				<td headers="40"></td><td></td>
			</tr> 
		    <tr> 
		    	<td align="left"><b>Name</b></td>
		    	<td>
		    	<p>
		    		<input id="changedName" type='text' maxlength="100"
					title="Please enter your username (at least 3 characters)"
					class="{required:true,minLength:3}" value='<s:property value="#portfolio.name"/>'>
				</p>
				<label id="Ntip"></label>
				</td>
			</tr>
			<tr> 
				<td colspan="2" width="100%">
					<div align="center"> 
						<input   type="button" id="saveas_confirm" onclick="confirmSaveAs()" value='<s:text name="button.confirm"></s:text>'  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; height: 70%; width: 60; 
						color: #000000; background-color:#00CCFF; border:1px solid #000000;"
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'" />
		                &nbsp; 
		                <input name="reset" type="button"  id="cancel" value='<s:text name="button.cancel"></s:text>'  height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; width: 60; 
						color: #000000; background-color: #00CCFF; border: 1px solid #000000;" 
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'" />
						<br>
	
					</div>
					<script>
						function confirmSaveAs(){
							var portfolioName = $("#changedName").val();
							
							$('#portfolioName').val(portfolioName);
							<s:if test="portfolio.isOriginalPortfolio==true && portfolio.state!=2">
							saveAs();
							</s:if>
							<s:elseif test="portfolio.isOriginalPortfolio==false && portfolio.state==2">
							copyas();
							</s:elseif>
							$("#confirm_window").dialog("close");
							
						}
						$("#cancel").click(function(){
							$("#confirm_window").dialog("close");
						});
					</script>
				</td>
			</tr>
		</table>
	</div>   
</div>

</s:form>
</div>

</body>
</html>