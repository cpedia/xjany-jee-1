<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="verify-v1" content="5p50UotfJnmCe0OpO/3NS0IcIE4Wi/ktqd6+Z/th2+Y=" />
<title>ValidFi Comprehensive Financial Strategies, Quantitative Platform, Strategy-Based Fund Analysis</title>
<link rel="stylesheet" href="/LTISystem/UserFiles/Image/vf.css" type="text/css" />
<script type="text/javascript" src="/LTISystem/jsp/images/jquery-1.2.6.pack.js"></script>
<!-- table sorter -->
		<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.tablesorter/style.css" type="text/css" />
		<script type="text/javascript" src="/LTISystem/jsp/images/jquery.tablesorter/jquery.tablesorter.js"></script>
		<script type="text/javascript" src="/LTISystem/jsp/images/jquery.tablesorter/jquery.tablesorter.pager.js"></script>
		<link rel="stylesheet" href="/LTISystem/jsp/images/jquery.tablesorter/addons/pager/jquery.tablesorter.pager.css" type="text/css" />
<script>
$(function () {
$("#performance").html("Loading Data...");
			//alert(window.location.search);
			var paraStr = window.location.search.substr(1).split("&");
			//alert(paraStr.length);
			var symbol="";
			for(i = 0; i < paraStr.length; i++){
				var pair = paraStr[i].split("=");
				if(pair[0].toLowerCase() == "symbol"){
					symbol = pair[1];
					break;
				}
			}
			$("#performance").load("../fundcenter/Partial.action?symbol=" + symbol + "&chosenYear=-1,-3,-5");
			//alert("hello!");
});	
</script>
</head>
<body>
<div id="wrap"> 
			<div id="content-wrap" style="width:80%"> 
				<div id="main" style="width:48%">
					<h2>Performance</h2>
					<div id="performance">
					</div>
				</div> 
				
				<div id="rightbar" style="width:48%">
					<h2>Magna aliquam erat volutpat</h2>
					<p>Magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.</p>
					<p>Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.</p>
					<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</p>
				</div>
			</div>
			<div id="sidebar" style="width:18%">
				<ul class=".sidemenu"> 
					<li class="first"><a href="#">Home</a></li> 
					<li><a href="#">About</a></li> 
					<li><a href="#">News</a></li> 
					<li><a href="#">Products</a></li> 
					<li><a href="#">Services</a></li> 
					<li><a href="#">Clients</a></li> 
					<li><a href="#">Case Studies</a></li> 
				</ul>
			</div>
		</div>

</body>
</html>