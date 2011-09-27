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
			<tr>
			<td width="10%">
				<s:url action="Main.action" id="url_main" namespace="/jsp/admin/portfolio" includeParams="none">
				</s:url>
				<s:a href="%{url_main}">Portfolio Manager</s:a>		
			</td>
			</tr>
		</table>	
	<p class="title"><s:property value="title"/></p>
	<p class="subtitle">Edit Keywords</p>
	<s:actionmessage theme="simple"/>
	<p class="clear">
	<s:form id="keywordForm" method="post" namespace="/jsp/admin/portfolio" theme="simple">
		<s:hidden name="portfolioID" theme="simple"></s:hidden>
		<p>
		<table id="keywordList" style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="95%" align="left">
			<tr>
				<td colspan="2"><a href="javascript:void(0);" onclick="addKeyword()">add keyword</a></td>
			</tr>
			<s:iterator value="keywords" status="st">
			<tr>
				<td>
					<input type="text" readonly value='<s:property value="key" />' name='keywords[<s:property value="#st.count-1"/>].Key'/>
				</td>
				<td>
					<select name='keywords[<s:property value="#st.count-1"/>].Valid'>
						<option value="true" <s:if test="valid==true">selected</s:if> >true</option>
						<option value="false" <s:if test="valid==false">selected</s:if> >false</option>
					</select>
				</td>
			</tr>
			<script>
				keywordNumber++;
			</script>
			</s:iterator>
 		</table>
 		</p>
 		<p class="clear"/>
 		<script>
 		function addKeyword(){
 			alert(keywordNumber);
 			$tr=$(document.createElement("tr"));
 			$("#keywordList").append($tr);
 			$td=$(document.createElement("td"));
 			$tr.append($td);
 			
 			$keywordInput = $(document.createElement("input"));
 			$keywordInput.attr({id:"keyword"+keywordNumber});
 			$keywordInput.attr({name:"keywords[" + keywordNumber + "].Key"});
 			$td.append($keywordInput);
 			
 			$("#keyword"+keywordNumber).change(function(){
 				var ID = $(this).attr("id");
 				var cut = ID.indexOf("d");
 				var keywordID = ID.substr(cut+1);
 				//alert(keywordID);
 				var key = $(this).val();
 				//$(this).attr({name:"keywords."+key});
 				$("#liveOption"+keywordID).attr({name:"keywords[" + keywordID + "].Valid"});
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
		
 		<p align="left" style="clear:both">
	 		<s:submit action="Save" value="Save" theme="simple">
			</s:submit>
 		</p>
	 </s:form>
	</body>
</html>
