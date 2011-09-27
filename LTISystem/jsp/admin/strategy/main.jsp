<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Strategy Main Page</title>
		<script src="/LTISystem/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
		<SCRIPT src="../images/common.js" type=text/javascript></SCRIPT>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />
		<script>
			function assignadminplantouser(){
				alert("onclick");
				$.ajax({
					type: "post",
					url: "/jsp/admin/strategy/AssignAdminPlanToUser.action?includeHeader=false",
					data: $("#plantouserform").serialize(),
					datatype: "html",
					success: function(result){
						alert("successful");
					}
				});
			}
		</script>
	</head>
	
	<body onload="adjHiehgt()">
	<div>
		<s:form namespace="/jsp/admin/strategy">
			<s:textfield name="key" theme="simple"></s:textfield>
			<s:submit action="Search" value="search" theme="simple"></s:submit>
		</s:form>
	</div>
	
	<div>
	    <s:form action="AssignAdminPlanToUser" id="plantouserform" namespace="/jsp/admin/strategy">
			PlanID:<s:textfield name="planID" theme="simple"></s:textfield>
			UserID:<s:textfield name="userID" theme="simple"></s:textfield>
			<s:submit onclick="assignadminplantouser()" value="Assign Admin Plan To User" theme="simple"></s:submit>
		</s:form>
	</div>
	
	<div>
	    <s:form namespace="/jsp/admin/strategy">
		SimilarIssues:<s:textfield name="planID" theme="simple"></s:textfield>
		<s:submit action="SimilarIssues" value="Execute" theme="simple"></s:submit>
		</s:form>
	</div>
	
	<s:url action="Save.action" id="url_create" namespace="/jsp/admin/strategy">
		<s:param name="action">create</s:param>
	</s:url>
	<s:a href="%{url_create}">Create</s:a>
	
		<table width="100%">
			<tr class="trHeader">
				<td>
					ID
				</td>
				<td>
					Name
				</td>	
				<td>
					User
				</td>		
																																				
			</tr>		
			<s:iterator value="#request.strategies.items">
				<tr>
					<td>
						<s:property value="ID"/>
					</td>
					<td>
						<s:url action="Save.action" id="urladdr" namespace="/jsp/strategy" >
							<s:param name="ID" value="ID"></s:param>	
							<s:param name="action">view</s:param>						
						</s:url>
						<s:a href="%{urladdr}"><s:property value="name"/></s:a>
					</td>
					<td>
						<s:url action="View" id="userurl" namespace="/jsp/admin/strategy/user" >
							<s:param name="strategyID" value="ID"></s:param>							
						</s:url>
						<s:property value="userName"/><s:a cssStyle="margin-left:5px;" href="%{userurl}">change</s:a>
					</td>
																	
				</tr>
			</s:iterator>

		</table>
		<center>
		Change to
			<s:url action="Main.action" id="url_first">
				<s:param name="startIndex" value="#request.strategies.firstParameter.startIndex"></s:param>
			</s:url>
							
			<s:a href="%{url_first}">
				First
			</s:a>	
							
			<s:iterator value="#request.strategies.parameters10" id="parameter" status="st">
				<s:url action="Main.action" id="pageurl">
					<s:param name="startIndex" value="#parameter.startIndex"></s:param>
				</s:url>
				<s:if test="#request.strategies.startIndex==#parameter.startIndex"><class style="font-style:italic;font-variant:small-caps;font-weight:bold;"></s:if>											
				<s:a href="%{pageurl}">
					<s:property value="#parameter.startIndex/#request.strategies.pageSize+1"/>
				</s:a>
				<s:if test="#request.strategies.startIndex==#parameter.startIndex"></class></s:if>	
			</s:iterator>
			
			<s:url action="Main.action" id="url_last">
				<s:param name="startIndex" value="#request.strategies.lastParameter.startIndex"></s:param>
			</s:url>
							
			<s:a href="%{url_last}">
				Last
			</s:a>	
		<s:url action="Main.action" id="url_m" includeParams="none">
		</s:url>								
		Page
		<form action='<s:property value="%{url_m}"/>' method="post" onSubmit="this.startIndex.value=(this.startIndex.value-1)*this.pageSize.value;return true;">
			Page <input type="text" name="startIndex" value=""/>
			<input type="hidden" name="pageSize" value='<s:property value="#request.strategies.pageSize"/>'/>
			<input type="submit" value="Go"/>
		<form>
		</center>	
		
	</body>
</html>
