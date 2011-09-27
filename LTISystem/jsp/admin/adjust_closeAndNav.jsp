<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
<head>
<title>Statistics</title>
<script src="/LTISystem/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
<link href="/LTISystem/jsp/template/ed/css/jquery_UI/moss/jquery-ui-1.8.custom.css" rel="stylesheet" type="text/css" />
<script src="/LTISystem/jsp/template/ed/js/jquery_UI/jquery-ui-1.8.custom.min.js" type="text/javascript"></script>
<script>
<%
DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
String cur=df.format(new Date());

String filename1="search-plans-ok";
String filename2="search-plans-nook";

%>
function delete5500(name){
	$(name).parent(name).remove();
}
function addFile(){
	$('#Data5500Id').append("<div><input type='file' name='uploadFile'><input type='button' onclick='delete5500(this)' value='delete'><br></div>");
}
function testAdjPrice(){
	window.location.href = '/LTISystem/f401k_testAdjClose.action?includeHeader=false';
}
function testAdjNav(){
	window.location.href = '/LTISystem/f401k_testAdjNav.action?includeHeader=false&dateStr='+document.getElementById('dateStr').value;
}

function cashUpdate(){
    window.location.href = '/LTISystem/f401k_cashUpdateForPriceUpdate.action?includeHeader=false';
}

function callback(msg){
	alert("Upload Success!Now we begin to create plan.");
	window.location.href = '/LTISystem/f401k_createData5500.action?includeHeader=false&uploadFileFileName='+msg+"&type="+document.getElementById('type').value;
}
function callback1(msg){
	alert(msg);
}
function callbackAdjClose(msg){
	alert(msg);
}
function disable()
{
	var radioArea = $("#radioArea").val();
	var reg = new RegExp();
	reg =/[a-zA-Z]/;
	if(radioArea !=  "" && reg.exec(radioArea) != null)
	{
		nds = document.getElementsByTagName("input");
		for(var i=0; i<nds.length; i++)
			if(nds[i].type=="radio" && nds[i].name=="radio")
			{
				document.getElementById("securityRadio").checked = true;
				nds[i].disabled = true; 
			}
	}
	else
	{
		nds = document.getElementsByTagName("input");
 		for(var i = 0; i < nds.length; i++)
			if(nds[i].type == "radio" && nds[i].name == "radio")
			{
				//document.getElementById("portfolioRadio").checked = true;
				nds[i].disabled = true; 
			}
	}
}
var t;
function myrefresh()
{
	var urlProcess = "/LTISystem/f401k_process.action?includeHeader=false";
	$.ajax({
		type:'get',
		url:urlProcess,
		success:function(data){
			var list = new Array();
			list = data.split("sum=");
			if(list[0] == '0' || list[0] == '\r\n0' || list[0] == '\n0')
				list[0] = "waiting...";
			if(list[1] == '0' || list[1] == '0\r\n' || list[1] == '0\n')
				list[1] = "waiting...";
			$("#update_content").html("<tr><td>Current :"+list[0]+"</td><td>Total :"+list[1]+"</td><br></tr>");
			if(list[0] != "waiting..." && list[0] >= list[1] - 1){
				clearTimeout(t);
				$("#update_content").html("<tr><td>Finish</td><td>Total :"+list[1]+"</td><br></tr>");
			}
		}
	});
	t = setTimeout(myrefresh,1000);
}

function updatempt()
{
	var htm = "Waiting...";
	$("#update_content").html(htm);
	var radioArea = $("#radioArea").val();
	var radio = "";
	if(radioArea == "")
	{
		alert("Can't be empty");
		return;
	}
	if(radioArea == 0 || radioArea == 'a'){
		setTimeout(myrefresh,1000);
	}else{
		var listPortfolioid = new Array();
		listPortfolioid = radioArea.split(",");
		/* if(document.getElementById("portfolioRadio").checked == true)
			radio = "Portfolio";
		else */
			radio = "Security";
		
		for(i = 0; i < listPortfolioid.length; i++)
		{
			if(document.activeElement.name == "1")
			{
				var url = "/LTISystem/updateAr.action?includeHeader=false&radio=" + radio + "&radioArea=" + listPortfolioid[i];
				if(htm == "Waiting...")
					$("#update_content").html("");
				$.ajax(
				{
					type:"get",
					url:url,
					success:function(data)
					{
						var list = new Array();
						list = data.split("id=");
						$("#update_content").append("<tr><td>"+list[1]+"</td><td>"+list[0]+"</td><br></tr>");
						var trLength = $("#update_content tr").length;
						if(trLength == listPortfolioid.length){
							$("#update_content").append("<tr><td>Finish</td><td>Finish</td><br></tr>");
						}
					}
				});
			} 
			else if(document.activeElement.name == "2")
			{
				var url = "/LTISystem/f401k_updateMpt.action?includeHeader=false&radio=" + radio + "&radioArea=" + listPortfolioid[i];
				$.ajax(
				{
					type:"get",
					url:url,
					success:function(data)
					{
						var list = new Array();
						list = data.split("id=");
						$("#update_content").append("<tr><td>"+list[1]+"</td><td>"+list[0]+"</td><br></tr>");
						var trLength = $("#update_content tr").length;
						if(trLength == listPortfolioid.length){
							$("#update_content").append("<tr><td>Finish</td><td>Finish</td><br></tr>");
						}
					}
				});
			}
		}
	}
	var listPortfolioid = new Array();
	listPortfolioid = radioArea.split(",");
	/* if(document.getElementById("portfolioRadio").checked == true)
		radio = "Portfolio";
	else */
		radio = "Security";
	
	for(i = 0; i < listPortfolioid.length; i++)
	{
		if(document.activeElement.name == "1")
		{
			var url = "/LTISystem/updateAr.action?includeHeader=false&radio=" + radio + "&radioArea=" + listPortfolioid[i];
			$.ajax(
			{
				type:"get",
				url:url,
			});
		}
		else if(document.activeElement.name == "2")
		{
			var url = "/LTISystem/f401k_updateMpt.action?includeHeader=false&radio=" + radio + "&radioArea=" + listPortfolioid[i];
			$.ajax(
			{
				type:"get",
				url:url,
			});
		}
		else if (document.activeElement.name == "3")
			{
			var url = "/LTISystem/f401k_updateMpt.action?includeHeader=false&stop=1";
			$.ajax(
			{
				type:"get",
				url:url,
			});
			}
	}
}
</script>
</head>
<body>
<hr>
<form action="/LTISystem/f401k_upLoadData5500.action" method="post" id="form1" enctype="multipart/form-data" target="hidden_frame">
	<b>Upload Data5500</b>
	<div id="Data5500Id">
	<div>
	<input type="file" name="uploadFile"><input type='button' onclick='delete5500(this)' value='delete'><br>
	</div>
	</div>
	Type:<select name="type" id="type">
			<option value="Data5500">Data5500</option>
			<option value="SCH_H">SCH_H</option>
		</select>
		<br>
	<input type="button" id="addUpload" value="Add" onclick="addFile()">
	<input type="submit" value="Upload Data5500">
	<iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
  	<div id="messageByCreate">
  	</div>
	
</form>
<br>
<hr>
<form action="/LTISystem/f401k_upLoadTestPrice.action" method="post" id="form" enctype="multipart/form-data" target="hidden_frameAdjClose">
	<b>Upload TestAdjPrice File</b>
	<input type="file" name="uploadFile">
  	<input type="submit" value="Upload a TestAdjPrice file">
  	<input type="button" id="adjPrice" value="testAdjPrice" onclick="testAdjPrice()">
  	<iframe name='hidden_frameAdjClose' id="hidden_frameAdjClose" style='display:none'></iframe>
</form>
<br>
<form action="/LTISystem/f401k_upLoadAdjNav.action" method="post" id="form" enctype="multipart/form-data" target="hidden_frame1">
	<b>Upload TestAdjNav File</b>
	<input type="file" name="uploadFile">
	
  	<input type="submit" value="Upload a TestAdjNav file">
  	StartDate:<input type="text" id="dateStr">
  	<input type="button" id="adjNav" value="testAdjNav" onclick="testAdjNav()">
  	<iframe name='hidden_frame1' id="hidden_frame1" style='display:none'></iframe>
</form>
<br>
<input type="button" value = "Update Cash and TSPGFUND"  onclick="cashUpdate()">
<hr>
Input Security Symbol:<br>
<textarea id="radioArea" name="radioArea" style="resize: none;overflow-x:visible;width: 314px; height: 84px;" onkeyup="value=value.replace(/[^a-zA-Z,]+/,'');disable()"></textarea><br><br>
	<!--  <input type="radio" id="portfolioRadio" name="radio" value="Portfolio" />Portfolio
	<input type="radio" id="securityRadio" name="radio" value="Security" checked />Security-->
	<input type="hidden" id="securityRadio" name="radio" value="Security" /><br><br>
	<button onclick="updatempt()" name="1">updateAr</button>
	<button onclick="updatempt()" name="2">updateMpt</button>
	<button onclick="updatempt()" name="3">stop</button><br>
	<table id="update_content" cellspacing="1" cellpadding="0" border="1">
</table>
</body>
</html>