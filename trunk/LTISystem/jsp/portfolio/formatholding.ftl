<html>
<head>
<title>Input Holdings By Text Area (Beta)</title>
<script>
function parse(){
	$.ajax({
	   type: "post",
	   data: $("#pform").serialize(),
	   url: "parseholding.action?ID=${ID?string.computer}&includeHeader=false",
	   success: function(msg){
	   		if(confirm($.trim(msg)+" Go back to the page for editing hodlings?")){
	   			window.location.href=$("#link").attr("href");
	   		}
	   },
	   error: function(){
	   		alert("The operation cannot continue for some reasons, please contact our surpport team."
	   	)}
	 });

}

</script>

</head>

<body>
<h1>${portfolio.name}</h1>
<pre>
Example:
asset1	spy 2000.0
asset1	vfinx	3000.0
asset2	vbinx	2000.0

Please note that this is a beta version and be careful to use it for existing portfolios, 
if you have any questions or problems, please let me know or file a bug in bugzilla.
</pre>

<div style="margin:10px;padding:10px">
	<a id="link" href="/LTISystem/jsp/portfolio/EditHolding.action?ID=${ID?string.computer}">Go Back to the page for editing Hodings</a>
	<form id="pform">
		<textarea style="width:100%;height:400px" name="holdingText">${holdingText?html}</textarea>
	</form>
	<p><a href="javascript:void(0)" class="uiButton" onclick="parse()">Parse</a></p>
</div>

</body>

</html>