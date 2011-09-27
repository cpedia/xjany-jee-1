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

<!-- these JSes are necessary START-->
<script type="text/javascript" src="../images/jquery.ui/1.7.1/ui.tabs.js"></script>

<SCRIPT src="../images/jquery.ToggleVal/jquery.toggleval.js" type="text/javascript" ></SCRIPT>
<link href="../images/jquery.ToggleVal/screen.css" rel="stylesheet" type="text/css" media="screen"/>

<SCRIPT src="../images/multiselects.js" type=text/javascript></SCRIPT>
<SCRIPT src="../images/multilines.js" type=text/javascript></SCRIPT>
<SCRIPT src="images/save.js"></SCRIPT>

<style type="text/css">
 table,input,button,combox,select,textarea{
font-family: Arial, Helvetica, sans-serif;
font-size: 12px;
}


<!-- These CSS are necessary START -->
body{
background:#DFE8F6;
}

.fbbl_north,
.fbbl_south,
.fbbl_east,
.fbbl_west,
.fbbl_center {
background:#FffFff;
}
.fbbl_north{
 height:90px;
}

 These CSS are necessary END -->



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
	display:inline;
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


<script type="text/javascript">


$(document).ready(function(){
    $(".fbbl_center").tabs();
   
  });
</script>

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

<div class="fbbl_center">
    <ul>
        <li><a href="#fragment-1"><span>Strategy Over View</span></a></li>
        <s:if test="hideCode==false">
        	<li><a href="#fragment-3"><span>Strategy Code</span></a></li>
        </s:if>
        
        <li><a target="_blank" href="/LTISystem/jsp/news/labels/strategy_${ID}.html?includeHeader=false" ><span>News</span></a></li>
        
    </ul>
    
    
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
			
			        	
			<table  cellSpacing=0 cellPadding=3 width="95%">
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
	 					<tr>
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
	 				<td><s:textarea cols="100" rows="1" name="variable" id="variable" label="Variable " onclick="editmode('variable')" onmouseout="setRows('variable')" cssStyle="overflow:visible;height:auto!important;"></s:textarea></td>

	 			</tr>
	 			<tr>
	 				<td><s:text name="strategy.functions"></s:text> </td>
	 				<td><s:textarea cols="100" rows="1" name="function" id="function" onclick="editmode('function')" onmouseout="setRows('function')"  label="Function " cssStyle="overflow:visible;"></s:textarea></td>
	 			</tr>
	 			<tr>
	 				<td><s:text name="strategy.initial"></s:text> </td>
	 				<td>
	 				<textarea  style="overflow:visible;" cols="100" rows="100" id="init" name="init" onclick="setRows('init')"><s:property value="init"/></textarea>

	 				
	 				</td>
	 			</tr>
	 			<tr>
	 				<td><s:text name="strategy.common.action"></s:text></td>
	 				<td><s:textarea cols="100" id="commentAction" name="commentAction" onmouseout="setRows('commentAction')"label="Common Action "></s:textarea></td>
	 			</tr>
	 			<tr><td colspan="3"><input type="button" style="overflow-x:visible;width:20%" onClick="addConditionAction()" name="Submit" value='<s:text name="add.condition.actions"></s:text>' /></td></tr>
	 			<tr><Td colspan="2">
	 				<table width="100%"  id="addConditionItem">
			 			<s:iterator status="st_c" value="#request.conditionAction" id="condiction">
			 			<s:hidden id="conditionCount" value="%{conditionAction.size()}"></s:hidden>
				 			<tr id='deleteCondition<s:property value="#st_c.count-1"/>'>
				 				<td>
				 					<s:text name="strategy.condition"></s:text> <s:property value="#st_c.count"/>
				 					<input  type=button onclick='deleteConditionAction(<s:property value="#st_c.count-1"/>)' value='<s:text name="button.delete"></s:text>' id='parameterbutton<s:property value="#st_c.count-1"/>}' name='parameter<s:property value="#st_c.count-1"/>'>
			                    
				 				
				 				</td>
				 				<td>
				 					<textarea id='preconditionAction<s:property value="#st_c.count-1"/>' name='conditionAction[<s:property value="#st_c.count-1"/>].pre'  cols="100" rows="1" Style="overflow:visible;" ><s:property value="%{pre}"/></textarea> 
				 				</td>
				 			</tr>
				 			<tr id='deleteAction<s:property value="#st_c.count-1"/>'>
				 				<td><s:text name="strategy.action"></s:text> <s:property value="#st_c.count"/></td>
				 				<td>
				 					<textarea id='postconditionAction<s:property value="#st_c.count-1"/>' name='conditionAction[<s:property value="#st_c.count-1"/>].post' cols="100" rows="20" style="overflow:visible;"><s:property value="%{post}"/></textarea>
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
	 				<td><s:textarea cols="100" rows="1" id="defaultAction" name="defaultAction" label="Default Action "  cssStyle="overflow:visible;" onclick="editmode('defaultAction')" onmouseout="setRows('defaultAction')"></s:textarea></td>
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
    
    
    <div id="fragment-1">
		<s:form id="OverViewForm" name="OverViewForm" method="post" theme="simple" onsubmit="checkForm()">
		  <table  cellSpacing=0 cellPadding=3 width="95%" align="center">
			<s:hidden id="Action" value="%{action}"></s:hidden>
			<s:hidden id="ID" name="ID" value="%{ID}"/>
			<tr>
			<td width="200"></td>
			<td><s:fielderror id="action" name="action"></s:fielderror></td>
			</tr>
				<tr>
					<td class="title"><s:text name="strategy.name"></s:text> </td>
					<td><s:textfield theme="simple" name="name" id="strategyName" cssStyle="width:100%;overflow-x:visible;"></s:textfield></td>
				</tr>
				<tr>
					<td class="title"><s:text name="strategy.username"></s:text> </td>
					<td><s:textfield theme="simple" name="userName" id="userNmae" cssStyle="width:100%;overflow-x:visible;" disabled="true"></s:textfield></td>
				</tr>				
				<tr>
					<td class="title"><s:text name="strategy.class"></s:text></td>
					<td>
						<div id="strategyClassPane"></div>
						<s:property value="classTree" escape="false"/>
						<input type='hidden' name="strategyClassID" value='<s:property value="strategyClassID"/>' id='strategyClassID' />
    					<script>
							createList('classTree', 'strategyClassPane', 'strategyClass123456', 'strategyClassID', '<s:property value="strategyClassID"/>');
						</script>
					</td>
				</tr>
				
				<!-- 
				<s:if test="%{isOwner}">
				<tr>
					<td class="title"><s:text name="create.as.public"></s:text></td>
					<td>
						<s:select list="{true,false}" name="isPublic" theme="simple"></s:select>
					</td>
				</tr>	
				<tr>
					<td class="title"><s:text name="create.as.codepublic"></s:text></td>
					<td>
						<s:select list="{false,true}" name="isCodePublic" id="isCodePublic" theme="simple"></s:select>
					</td>
				</tr>	
				</s:if>
				<s:else>
					<tr>
					<td class="title"><s:text name="create.as.public"></s:text></td>
					<td>
						<s:textfield name="isPublic" readonly="true"></s:textfield>
					</td>
					<text>sign in the groups, using "," to split. &nbsp; (IS_AUTHENTICATED_ANONYMOUSLY,Level_1)</text>
				</tr>	
				</s:else> 
				 -->
				<tr>
				 <td  class="title">Permission</td>
				 <td><text>Groups &nbsp; </text></td>
				</tr> 
			    <s:if test="isOwner==true||read==true">
				  <tr>
				   <td>&nbsp; &nbsp; role_strategy_read</td>
		           <td><s:textfield title="SUPER,ANONYMOUS,Level_1" name="roleRead" theme="simple" cssStyle="width:40%"></s:textfield></td>
				  </tr> 
			    </s:if>
				<s:if test="isOwner==true||code==true">
				<tr>
				  <td>&nbsp; &nbsp; role_strategy_code</td>
	 			 <td><s:textfield title="SUPER,ANONYMOUS,Level_1" name="roleCode" theme="simple" cssStyle="width:40%"></s:textfield></td>
				</tr> 
				</s:if>
			   <s:if test="isOwner==true||operation=true">
				 <tr>
				 <td>&nbsp; &nbsp; role_strategy_operation</td>
	 			 <td><s:textfield title="SUPER,ANONYMOUS,Level_1" name="roleOperation" theme="simple" cssStyle="width:40%"></s:textfield></td>
			     </tr> 
			   </s:if>
				
	 			<tr>
	 				<td  class="title">Styles</td>
	 				<td><s:textfield name="categories" theme="simple" cssStyle="width:100%"></s:textfield></td>
	 			</tr>
	 							
			   <tr>
				<td class="title">
				<s:text name="shortDescription"></s:text>
				</td>
				<td>
					<s:if test="%{isOwner}">
					<input type="button" onclick="ShowShortDescription()" id="shortDescription_button" value='<s:text name="edit"></s:text>' />
					<input type="button" onclick="ShowShortDescriptionSrc()"  id="shortDescription_button_src" value='Edit Source'/>
					<input type="button" onclick="ClearShortDescription()"  id="shortDescription_button_clr" value='Clear'/>
			
					</s:if>
				</td>
			   </tr>				
			  <tr>
				<td colspan="2">
					<div id="shortDescription_div_text" style=''><s:property value="shortDescription" escape="false"/></div>
					<s:if test="%{isOwner}">
					<div id="shortDescription_div" style="display:none">
						<s:hidden  id="ShortDescription" name="shortDescription" ></s:hidden>
						<input type="hidden" id="ShortDescription___Config" value="">
			
						<iframe id="ShortDescription___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no">
						</iframe>
					</div>
					<div id='shortDescription_div_src'  style="display:none">
						<textarea id='shortDescription_src' style="width:100%;height:400px" onchange="ChangeShortDescriptionSrc()">
						<s:property value="shortDescription" escape="true"/>
						</textarea>
					</div>
					</s:if>
				</td>
			 </tr>			

			<script>
				function ShowShortDescriptionSrc(){
					$('#shortDescription_div_text').hide();
					$('#shortDescription_div_src').show();
					$('#shortDescription_button').attr({disabled:"yes"});
					$('#shortDescription_button_src').attr({disabled:"yes"});
					$('#shortDescription_button_clr').attr({disabled:"yes"});
				}
				function ChangeShortDescriptionSrc(){
					$('#ShortDescription').val($('#shortDescription_src').val());
				}
				function ShowShortDescription(){
					$("#ShortDescription___Frame").attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName=ShortDescription&Toolbar=Default"});
					$('#shortDescription_div_text').hide();
					$('#shortDescription_div').show();
					$('#shortDescription_button').attr({disabled:"yes"});
					$('#shortDescription_button_src').attr({disabled:"yes"});
					$('#shortDescription_button_clr').attr({disabled:"yes"});
					
				}		
				function ClearShortDescription(){
					$('#shortDescription_div_text').html("clear");
					$('#ShortDescription').val('');
					$('#shortDescription_button').attr({disabled:"yes"});
					$('#shortDescription_button_src').attr({disabled:"yes"});
					$('#shortDescription_button_clr').attr({disabled:"yes"});
				}	
						
			</script>
			<tr>
				<td class="title">
				<s:text name="description"></s:text>
				</td>
				<td>
					<s:if test="%{isOwner}">
					<input type="button" onclick="ShowDescription()" id="description_button" value='<s:text name="edit"></s:text>' />
					<input type="button" onclick="ShowDescriptionSrc()"  id="description_button_src" value='Edit Source'/>
			
					<input type="button" onclick="ClearDescription()"  id="description_button_clr" value='Clear'/>
					</s:if>
				</td>
			</tr>
							
			<tr>
				<td colspan="2">
					<div id="description_div_text" style=''><s:property value="description" escape="false"/></div>
					<s:if test="%{isOwner}">
					<div id="description_div" style="display:none">
						<s:hidden  id="Description" name="description" ></s:hidden>
			
						<input type="hidden" id="Description___Config" value="">
						<iframe id="Description___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no">
						</iframe>
					</div>
					<div id='description_div_src'  style="display:none">
						<textarea id='description_src' style="width:100%;height:400px" onchange="ChangeDescriptionSrc()">
						<s:property value="description" escape="true"/>
						</textarea>
					</div>
					</s:if>
				</td>
			</tr>	
					
			<script>
				function ShowDescriptionSrc(){
					$('#description_div_text').hide();
					$('#description_div_src').show();
					$('#description_button').attr({disabled:"yes"});
					$('#description_button_src').attr({disabled:"yes"});
					$('#description_button_clr').attr({disabled:"yes"});
				}
				function ChangeDescriptionSrc(){
					$('#Description').val($('#description_src').val());
				}
				function ShowDescription(){
					$("#Description___Frame").attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName=Description&Toolbar=Default"});
					$('#description_div_text').hide();
					$('#description_div').show();
					$('#description_button').attr({disabled:"yes"});
					$('#description_button_src').attr({disabled:"yes"});
					$('#description_button_clr').attr({disabled:"yes"});
					
				}		
				function ClearDescription(){
					$('#description_div_text').html("clear");
					$('#Description').val('');
					$('#description_button').attr({disabled:"yes"});
					$('#description_button_src').attr({disabled:"yes"});
					$('#description_button_clr').attr({disabled:"yes"});
				}	
						
			</script>
			
			<tr>
				<td class="title">
				<s:text name="similarIssues"></s:text>
				</td>
				<td>
					<s:if test="%{isOwner}">
					<input type="button" onclick="ShowSimilarIssues()" id="similarIssues_button" value='<s:text name="edit"></s:text>' />
					<input type="button" onclick="ShowSimilarIssuesSrc()"  id="similarIssues_button_src" value='Edit Source'/>
			
					<input type="button" onclick="ClearSimilarIssues()"  id="similarIssues_button_clr" value='Clear'/>
					</s:if>
				</td>
			</tr>	
						
			<tr>
				<td colspan="2">
					<div id="similarIssues_div_text" style=''><s:property value="similarIssues" escape="false"/></div>
					<s:if test="%{isOwner}">
					<div id="similarIssues_div" style="display:none">
						<s:hidden  id="SimilarIssues" name="similarIssues" ></s:hidden>
			
						<input type="hidden" id="SimilarIssues___Config" value="">
						<iframe id="SimilarIssues___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no">
						</iframe>
					</div>
					<div id='similarIssues_div_src'  style="display:none">
						<textarea id='similarIssues_src' style="width:100%;height:400px" onchange="ChangeSimilarIssuesSrc()">
						<s:property value="similarIssues" escape="true"/>
						</textarea>
					</div>
					</s:if>
				</td>
			</tr>
						
			<script>
				function ShowSimilarIssuesSrc(){
					$('#similarIssues_div_text').hide();
					$('#similarIssues_div_src').show();
					$('#similarIssues_button').attr({disabled:"yes"});
					$('#similarIssues_button_src').attr({disabled:"yes"});
					$('#similarIssues_button_clr').attr({disabled:"yes"});
				}
				function ChangeSimilarIssuesSrc(){
					$('#SimilarIssues').val($('#similarIssues_src').val());
				}
				function ShowSimilarIssues(){
					$("#SimilarIssues___Frame").attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName=SimilarIssues&Toolbar=Default"});
					$('#similarIssues_div_text').hide();
					$('#similarIssues_div').show();
					$('#similarIssues_button').attr({disabled:"yes"});
					$('#similarIssues_button_src').attr({disabled:"yes"});
					$('#similarIssues_button_clr').attr({disabled:"yes"});
					
				}		
				function ClearSimilarIssues(){
					$('#similarIssues_div_text').html("clear");
					$('#SimilarIssues').val('');
					$('#similarIssues_button').attr({disabled:"yes"});
					$('#similarIssues_button_src').attr({disabled:"yes"});
					$('#similarIssues_button_clr').attr({disabled:"yes"});
				}	
						
			</script>
		
			<tr>
				<td class="title">
				<s:text name="reference"></s:text>
				</td>
				<td>
					<s:if test="%{isOwner}">
					<input type="button" onclick="ShowReference()" id="reference_button" value='<s:text name="edit"></s:text>' />
					<input type="button" onclick="ShowReferenceSrc()"  id="reference_button_src" value='Edit Source'/>
			
					<input type="button" onclick="ClearReference()"  id="reference_button_clr" value='Clear'/>
					</s:if>
				</td>
			</tr>	
					
				<tr>
					<td colspan="2">
						<div id="reference_div_text" style=''><s:property value="reference" escape="false"/></div>
						<s:if test="%{isOwner}">
						<div id="reference_div" style="display:none">
							<s:hidden  id="Reference" name="reference" ></s:hidden>
				
							<input type="hidden" id="Reference___Config" value="">
							<iframe id="Reference___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no">
							</iframe>
						</div>
						<div id='reference_div_src'  style="display:none">
							<textarea id='reference_src' style="width:100%;height:400px" onchange="ChangeReferenceSrc()">
							<s:property value="reference" escape="true"/>
							</textarea>
						</div>
						</s:if>
					</td>
				</tr>	
						
				<script>
					function ShowReferenceSrc(){
						$('#reference_div_text').hide();
						$('#reference_div_src').show();
						$('#reference_button').attr({disabled:"yes"});
						$('#reference_button_src').attr({disabled:"yes"});
						$('#reference_button_clr').attr({disabled:"yes"});
					}
					function ChangeReferenceSrc(){
						$('#Reference').val($('#reference_src').val());
					}
					function ShowReference(){
						$("#Reference___Frame").attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName=Reference&Toolbar=Default"});
						$('#reference_div_text').hide();
						$('#reference_div').show();
						$('#reference_button').attr({disabled:"yes"});
						$('#reference_button_src').attr({disabled:"yes"});
						$('#reference_button_clr').attr({disabled:"yes"});
						
					}		
					function ClearReference(){
						$('#reference_div_text').html("clear");
						$('#Reference').val('');
						$('#reference_button').attr({disabled:"yes"});
						$('#reference_button_src').attr({disabled:"yes"});
						$('#reference_button_clr').attr({disabled:"yes"});
					}	
				</script>
								
 		<s:url action="ModelPortfolioMain.action" namespace="/jsp/strategy" id="url_modelportfolio" includeParams="none">
			<s:param name="strategyID" value="%{ID}"></s:param>
		</s:url>
		
        
        <s:if test="%{action!='create'}">
        	<tr>
				<td class="title">
				Model Portfolio <a href='<s:property value="url_modelportfolio"/>'>more</a>
				</td>
				<td>
				
				</td>
			</tr>	
				<tr>
					<td colspan="2">
						<s:action name="ModelPortfolioMain" namespace="/jsp/strategy" executeResult="true">
							<s:param name="strategyID" value="%{ID}"></s:param>
							<s:param name="size">10</s:param>
							<s:param name="includeHeader">false</s:param>
						</s:action>
					</td>
				</tr>	
        </s:if>
        
        
	 			<tr>
	 				<td colspan="2">
	 				<center>
					<s:if test="%{isOwner}">
	 				<input type="button" onclick="save()" value='<s:text name="save"></s:text>' />
					<input type="button" onclick="del()" value='<s:text name="button.delete"></s:text>' />
					</s:if>
	 				<input type="button" id="saveas" value='<s:text name="save.as"></s:text>' />
	 				</center>
	 				</td>
	 			</tr>
	 			
	      </table>
	   </s:form>
   </div>
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
					class="{required:true,minLength:3}"
					value='<s:property value="%{name}"/>'
					>
				</p>
				<label id="Ntip"></label>
				</td>
			</tr>
			<tr> 
				<td colspan="2" width="100%">
					<div align="center"> 
						<input   type="button" id="saveas_confirm" onclick="confirmSaveAs()" value='<s:text name="button.confirm"></s:text>'  
						height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; height: 70%; width: 60; 
						color: #000000; background-color:#00CCFF; border:1px solid #000000;"
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'" />
		                &nbsp;
		                <input name="reset" type="button"  id="cancel" value='<s:text name="button.cancel"></s:text>'  
		                height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; width: 60; 
						color: #000000; background-color: #00CCFF; border: 1px solid #000000;" 
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'" />
						<br>
	
					</div>
					<script>
						function confirmSaveAs(){
							var strategyName = $("#changedName").val();
							
							$('#strategyName').val(strategyName);
							document.OverViewForm.action='../strategy/SaveAs.action?action=saveas';
							$("#confirm_window").dialog("close");
							//$("#init").val(init_panel.getCode());
							document.OverViewForm.submit();
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
<script>
$("#saveas").click(function(){
	 $("#confirm_window").dialog({height:250,weight:300,title:"Fill In The New Name"});
});
</script>
</body>
</html>