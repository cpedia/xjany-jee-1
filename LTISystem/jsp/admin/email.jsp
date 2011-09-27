
<html>
<head>
<title>EMail Alert</title>
<script src="/LTISystem/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>

<script>
$(function(){
	var host=window.location.host;
	$("a").each(function(){
		this.href="http://"+host+":8081/"+$(this).attr("t");
	});
})
function emailCheck()
{
	$("#appendThis").html("");
	var state = "";
	var portfolioIDs = $("#portfolioIDs").val();
	var arrayPortfolio = new Array();
	arrayPortfolio = portfolioIDs.split(",");	
	if(portfolioIDs == "")
	{
		alert("Can't be empty");
		return;
	}
	for(i=0;i<arrayPortfolio.length;i++)
	{
		var url = "/LTISystem/jsp/admin/group/emails/InsertPortfolio.action?portfolioIDs="+arrayPortfolio[i] + "&random=" + rand();
		$.ajax(
		{
			type:"get",
			url:url,
			success:function(data)
			{
				var list = new Array();
				list = data.split("id=");
				$("#appendThis").append("<tr><td>"+list[1]+"</td><td>"+list[0]+"</td><br></tr>");
			}
		});
	}
}
function rand()
{
	var rnd="";
	rnd += Math.floor(Math.random()*10);
	return rnd;
}
</script>
</head>
<body>
the Status will be re-set to true if you restart the engine.<br><br>
<br>
<a t="email?operation=info" target="_blank">Status</a>
<br>
<a t="email?operation=start" target="_blank">Start</a>
<br>
<a t="email?operation=stop" target="_blank">Stop</a>
<br>
<a t="Sendemail" target="_blank">Send Email(the status should be true.)</a>
<br>
<a t="Emailcontent" target="_blank">View Email</a>
<br>
<hr>
Set Email Alert (batch)
<br><br>
Instruction:  Please input a portfolio IDs, seperated by commas. They will be set as Admin's email alerts.<br><br>
<!--
IDs: <input type="text" name="portfolioIDs" id="portfolioIDs" style="overflow-x:visible;width:60;" /><br><br>
-->
IDs: <textarea id="portfolioIDs" name="portfolioIDs" style="overflow-x:visible;width:200;" onkeyup="value=value.replace(/[^\d,\,]/g,'')" ></textarea> <br><br>
<button onclick="emailCheck()">execute</button><br>
<table id="appendThis" cellspacing="1" cellpadding="0" border="1">
</table>
</body>
</html>