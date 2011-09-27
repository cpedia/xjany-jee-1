<%@page pageEncoding="utf8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="fck" uri="/WEB-INF/FCKeditor.tld" %>
<%@taglib prefix="authz" uri="/WEB-INF/authz.tld"%>
<html>
<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 
<title><s:property value="%{name}"/></title>
<meta name="submenu" content="individualstrategy"/>
<meta name="strategies" content="vf_current"/>
<meta name="code" content="vf_current"/>
<meta name="id" content='<s:property value="ID"/>'/>

<SCRIPT src="../images/multiselects.js" type=text/javascript></SCRIPT>
<SCRIPT src="../images/multilines.js" type=text/javascript></SCRIPT>
<SCRIPT src="images/save.js"></SCRIPT>
<SCRIPT src="../images/jquery.ToggleVal/jquery.toggleval.js" type="text/javascript" ></SCRIPT>
<link href="../images/jquery.ToggleVal/screen.css" rel="stylesheet" type="text/css" media="screen"/>


<style type="text/css">
 table,input,button,combox,select,textarea{
font-family: Arial, Helvetica, sans-serif;
font-size: 12px;
}


.breakword
{
 word-wrap:break-word; overflow:hidden;
}



/* links */
#fragment-1 a { 
color: #4284B0; 
    color: #2B60DE;
	background-color: inherit;
	text-decoration: none;
}
#fragment-1 a:hover {
	color: #9EC068;
	background-color: inherit;
}

#messagetable ul li{
 word-wrap:break-all;
width:800px;
}
#fragment-1 table .title{font-family: "Arial"; font-size: 14px;FONT-WEIGHT: bold; }
#fragment-1 table tr{height:30px} 
</style>
<% String currentPath=request.getContextPath()+"/jsp/strategy/FCKEditor/"; %>

</head>
<body>

<%--
<div class="fbbl_east">
<s:url action="Strategy.action" namespace="/jsp/news" id="url_news" includeParams="none">
	<s:param name="ID" value="%{ID}"></s:param>
</s:url>
<iframe id="news" scrolling="auto" width="100%" height="100%" style="" frameborder="0" marginWidth="0" marginHeight="0" src='<s:property value="url_news"/>'></iframe>
</div>

<div class="fbbl_south">
<s:if test='customizeRegion!=null && customizeRegion.southRegionName!=null && !customizeRegion.southRegionName.equalsIgnoreCase("Fixed")'>
	<s:url id="south_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
		<s:param name="pageName"><s:property value='customizeRegion.southRegionName'/></s:param>
	</s:url>
<iframe id="south" scrolling="yes" width="100%" height="99%" src='<s:property value="south_url"/>'></iframe>
</s:if>

<s:url action="getPost.action" namespace="/jsp/jforum" id="url_post" includeParams="none">
	<s:param name="ID" value="%{ID}"></s:param>
</s:url>
<iframe id="post" scrolling="yes" width="100%" height="99%" src='<s:property value="url_post"/>'></iframe>
</div>--%>

<s:hidden id="hideCode" name="hideCode" value="%{hideCode}"/>
<div id="mainForm">
<div class="fbbl_center">

    
   <s:if test="hideCode==false">
    <div id="fragment-3" >
        <s:form id="CodeForm" method="post" theme="simple">

			<authz:authorize ifAnyGranted="ROLE_SUPERVISOR">
			<s:url id="url_vs" action="ViewSourceFile" namespace="/jsp/ajax" includeParams="none">
        		<s:param name="strategyID" value="#request.ID"/>
        	</s:url>
        	<s:a href="%{url_vs}" >View Source Code</s:a>
			<br>
			<a href='http://www.validfi.com:8081/dailystart?portfolioID=0&filter=SpecailStrategyPortfoliosFilter&strategyID=<s:property value="ID"/>'>Monitor all</a>
			</authz:authorize>
			
			        	
			<table  cellSpacing=0 cellPadding=3 width="100%" style="margin:0 auto">
				<s:hidden id="Action2" name="action" value="%{action}"/>
				<s:hidden id="ID2" name="ID" value="%{ID}"/>
				<s:hidden id="Name2" name="name" value="%{name}"/>
				<tr>
				<td ><s:text name="excute.message"></s:text></td>
				<td style="word-break: break-all;width:70%;word-wrap:break-word;border:1px #8DB6CD solid;" name="Message"><s:property value="actionMessage"/></td>
				</tr>
				<tr>
				<td colspan="2" width="100%" style="color:red"><s:fielderror id="CompileError" name="CompileError"></s:fielderror></td>
				</tr>
	 			<tr><td colspan="2"><input style="overflow-x:visible;width:13%;" type="button" onClick="addParameter()" name="Submit" value='<s:text name="add.parameters"></s:text>'></td></tr>
	 			
	 			<tr><td colspan="2">
	 				<table width="100%" id="addParameterItem">
	 					<tr width="930px;">
	 						<td><s:text name="parameter.type"></s:text></td>
	 						<td><s:text name="parameter.name"></s:text></td>
	 						<td><s:text name="parameter.default.value"></s:text></td>
	 						<td><s:text name="parameter.description"></s:text></td>
	 						<td><s:text name="parameter.remove"></s:text></td>
	 					</tr>
			 			<s:iterator status="st_p" value="#request.parameter" id="parameter">
				 			<tr id='parameter<s:property value="#st_p.count-1"/>'>
				 				<td>
				 					<input type="text" id='parameterfirst<s:property value="#st_p.count-1"/>' name='parameter[<s:property value="#st_p.count-1"/>].first' value='<s:property value="%{first}"/>'> 
				 				</td>
				 				<td>
				 					<input type="text" id='parametersecond<s:property value="#st_p.count-1"/>'  name='parameter[<s:property value="#st_p.count-1"/>].second' value='<s:property value="%{second}"/>'> 
				 				</td>
				 				<td>
				 					<input type="text" id='parameterthird<s:property value="#st_p.count-1"/>'  name='parameter[<s:property value="#st_p.count-1"/>].third' value='<s:property value="%{third}"/>'> 
				 				</td>
				 				<td>
				 				<s:textarea cols="30"  id='parameterfourth%{#st_p.count-1}' name="parameter[%{#st_p.count-1}].fourth" value="%{fourth}"></s:textarea>
				 				</td>	
			                    <td>
									<input type=button onclick='deleteParameter(<s:property value="#st_p.count-1"/>)' value='<s:text name="button.delete"></s:text>' id='parameterbutton<s:property value="#st_p.count-1"/>}' name='parameter<s:property value="#st_p.count-1"/>'>
			                    </td>			 						 							 							 				
				 			</tr>
				 			<script>
				 				n=<s:property value="#st_p.count"/>;
				 			</script>				 			
			 			</s:iterator> 	
		 			</table>
	 			</td></tr>		
	 			<tr>
	 				<td width="20%"><s:text name="strategy.variables"></s:text> </td>
	 				<td><s:textarea cols="130" rows="1" name="variable" id="variable" label="Variable " onclick="editmode('variable')" onmouseout="setRows('variable')" cssStyle="overflow:visible;height:auto!important;"></s:textarea></td>

	 			</tr>
	 			<tr>
	 				<td><s:text name="strategy.functions"></s:text> </td>
	 				<td><s:textarea cols="130" rows="1" name="function" id="function" onclick="editmode('function')" onmouseout="setRows('function')"  label="Function " cssStyle="overflow:visible;"></s:textarea></td>
	 			</tr>
	 			<tr>
	 				<td><s:text name="strategy.initial"></s:text> </td>
	 				<td>
	 				<textarea  style="overflow:visible;" cols="130" rows="100" id="init" name="init" onclick="setRows('init')"><s:property value="init"/></textarea>

	 				
	 				</td>
	 			</tr>
	 			<tr>
	 				<td><s:text name="strategy.common.action"></s:text></td>
	 				<td><s:textarea cols="130" id="commentAction" name="commentAction" onmouseout="setRows('commentAction')"label="Common Action "></s:textarea></td>
	 			</tr>
	 			<tr><td colspan="3"><input type="button" style="overflow-x:visible;width:20%" onClick="addConditionAction()" name="Submit" value='<s:text name="add.condition.actions"></s:text>' /></td></tr>
	 			<tr><td colspan="2">
	 				<table id="addConditionItem" width="93%" align="right">
			 			<s:iterator status="st_c" value="#request.conditionAction" id="condiction">
			 			<s:hidden id="conditionCount" value="%{conditionAction.size()}"></s:hidden>
				 			<tr id='deleteCondition<s:property value="#st_c.count-1"/>'>
				 				<td>
				 					<s:text name="strategy.condition"></s:text> <s:property value="#st_c.count"/>
				 					<input  type=button onclick='deleteConditionAction(<s:property value="#st_c.count-1"/>)' value='<s:text name="button.delete"></s:text>' id='parameterbutton<s:property value="#st_c.count-1"/>}' name='parameter<s:property value="#st_c.count-1"/>'>
			                    
				 				
				 				</td>
				 				<td>
				 					<textarea id='preconditionAction<s:property value="#st_c.count-1"/>' name='conditionAction[<s:property value="#st_c.count-1"/>].pre'  cols="130" rows="1" Style="overflow:visible;" ><s:property value="%{pre}"/></textarea> 
				 				</td>
				 			</tr>
				 			<tr id='deleteAction<s:property value="#st_c.count-1"/>'>
				 				<td><s:text name="strategy.action"></s:text> <s:property value="#st_c.count"/></td>
				 				<td>
				 					<textarea id='postconditionAction<s:property value="#st_c.count-1"/>' name='conditionAction[<s:property value="#st_c.count-1"/>].post' cols="130" rows="20" style="overflow:visible;"><s:property value="%{post}"/></textarea>
				 				</td>
				 			</tr> 		
				 			<script>
				 				m=<s:property value="#st_c.count"/>;
				 			</script>	 			
			 			</s:iterator>
		 			</table>
	 			</Td></tr>
	 			<tr>
	 				<td><s:text name="strategy.default.action"></s:text></td>
	 				<td><s:textarea cols="130" rows="1" id="defaultAction" name="defaultAction" label="Default Action "  cssStyle="overflow:visible;" onclick="editmode('defaultAction')" onmouseout="setRows('defaultAction')"></s:textarea></td>
	 			</tr>
	 			
	 			<tr>
	 				<td colspan="2">
	 				<center>
	 				<s:if test="%{isOwner}">
	 				<s:submit key="save" action="UpdateCode" onclick="savecode()"></s:submit>
	 				<s:submit key="compile" action="Compile" onclick="compile()"></s:submit>
					<s:submit key="save.compile" action="SaveAndCompile" onclick="compile()"></s:submit>
	 				</s:if>
	 				</center>
	 				</td>
	 			</tr>
	 			</table>			
		</s:form>
       </div>
    </s:if>  
    <s:else>
    	The code is not public.
    </s:else>
</div>
</div>
</body>
</html>
