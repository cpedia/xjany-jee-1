<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Group Page</title>
		<link href="../../images/style.css" rel="stylesheet" type="text/css" />
		<SCRIPT src="../../images/jquery.latest.js" type="text/javascript" ></SCRIPT>
	</head>
	<body>
		<script>
			keywordNumber = 0;
		</script>
		<table class="nav" width="100%">
			<td width="10%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/strategy" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Portfolio Manager</s:a>		
			</td>
		</table>	
	<p class="title"><s:property value="title"/></p>
	<p class="subtitle">Edit Keywords</p>
	<s:actionmessage/>
	<s:form id="keywordForm" method="post" namespace="/jsp/admin/strategy/keyword">
		<s:hidden name="strategyID"></s:hidden>
		<table id="keywordList" style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<tr>
				<td colspan="2"><a href="javascript:void(0);" onclick="addKeyword()">add keyword</a></td>
			</tr>
			<s:iterator value="keywords">
			<tr>
				<td>
					<s:property value="key"/>
				</td>
				<td>
					<select name='keywords.<s:property value="key"/>'>
						<option value="true" <s:if test="value==true">selected</s:if> >true</option>
						<option value="false" <s:if test="value==false">selected</s:if> >false</option>
					</select>
				</td>
			</tr>
			<script>
				keywordNumber++;
			</script>
			</s:iterator>
 		</table>
 		<script>
 		function addKeyword(){
 			$tr=$(document.createElement("tr"));
 			$("#keywordList").append($tr);
 			$td=$(document.createElement("td"));
 			$tr.append($td);
 			
 			$keywordInput = $(document.createElement("input"));
 			$keywordInput.attr({id:"keyword"+keywordNumber});
 			$td.append($keywordInput);
 			
 			$("#keyword"+keywordNumber).change(function(){
 				var ID = $(this).attr("id");
 				var cut = ID.indexOf("d");
 				var keywordID = ID.substr(cut+1);
 				//alert(keywordID);
 				var key = $(this).val();
 				//$(this).attr({name:"keywords."+key});
 				$("#liveOption"+keywordID).attr({name:"keywords."+key});
 			})
 			
 			$td=$(document.createElement("td"));
			$tr.append($td);
			var $live = $(document.createElement("select"));
			$live.attr({id:"liveOption"+keywordNumber});
			$live.append('<option value="false">false</option>');
			$live.append('<option value="true">true</option>');
			
			$td.append($live);
			keywordNumber++;
 		}
 		</script>

 		<div>
 			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid;" cellSpacing=0 cellPadding=0 width="95%" align="left">
	 			<tr><td align="center">
			 		<s:submit action="Save" value="Save">
					</s:submit>
			 	</td></tr>
	 		</table>
 		</div>
	 </s:form>
	</body>
</html>
