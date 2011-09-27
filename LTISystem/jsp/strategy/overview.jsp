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
<s:if test="action!='create'">
<meta name="submenu" content="individualstrategy"/>
</s:if>
<meta name="strategies" content="vf_current"/>
<meta name="strategy" content="vf_current"/>
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

#fragment-1 a:hover {
	color: #9EC068;
	background-color: inherit;
}
#fragment-1 a:visited{
     color:#DC143C;
    background-color: inherit;
    text-decoration: underline;
}
#fragment-1 a{
    color:#2B60DE;
    background-color: inherit;
    text-decoration: underline;
}
#messagetable ul li{
 word-wrap:break-all;
width:100%;
}






#fragment-1 .title{font-family: "Arial"; width:20%; font-size: 14px;FONT-WEIGHT: bold; }
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


<table width="100%"><!-- container start -->
	<tr>
	<td width="65%" valign="top">
	    <s:hidden id="hideCode" name="hideCode" value="%{hideCode}"/>
	    <div id="fragment-1">
			<s:form id="OverViewForm" name="OverViewForm" method="post" theme="simple" onsubmit="checkForm()">
				<table  cellSpacing=0 cellPadding=3 width="100%" align="center">
					<s:hidden id="Action" value="%{action}"></s:hidden>
					<s:hidden id="ID" name="ID" value="%{ID}"/>
					<tr>
						<td></td>
						<td><s:fielderror id="action" name="action"></s:fielderror></td>
					</tr>
					
					<tr>
						<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'><s:text name="strategy.name"></s:text> </td>
						<td>
							<s:if test="isOwner!=true">
							<s:property value="name"/>
							<s:hidden name="name" id="strategyName"></s:hidden>
							</s:if>
							<s:else>
							<s:textfield theme="simple" name="name" id="strategyName" cssStyle="width:99%;overflow-x:visible;"></s:textfield>
							</s:else>
						</td>
					</tr>
					
					<tr>
						<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'><s:text name="strategy.username"></s:text> </td>
						<td>
							<s:if test="isOwner!=true">
							<s:property value="userName"/>
							</s:if>
							<s:else>
							<s:textfield theme="simple" name="userName" id="userNmae" cssStyle="width:99%;overflow-x:visible;" disabled="true"></s:textfield>
							</s:else>
						</td>
					</tr>		
							
					<tr>
						<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'><s:text name="strategy.class"></s:text></td>
						<td>
							<s:if test="isOwner!=true">
								<s:property value="className"/>
							</s:if>
							<s:else>
								<div id="strategyClassPane"></div>
								<s:property value="classTree" escape="false"/>
								<input type='hidden' name="strategyClassID" value='<s:property value="strategyClassID"/>' id='strategyClassID' />
		    					<script>
									createList('classTree', 'strategyClassPane', 'strategyClass123456', 'strategyClassID', '<s:property value="strategyClassID"/>');
								</script>
							</s:else>
						</td>
					</tr>

					<s:if test="isOwner==true">
					<tr>
						<td  style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'>Groups Permission</td>
						<td><text>Groups &nbsp; </text></td>
					</tr> 
					
						<tr>
							<td>&nbsp; &nbsp; read permission</td>
							<td><s:textfield title="ANONYMOUS,USER,Level_1,SUPER" name="roleRead" theme="simple" cssStyle="width:99%"></s:textfield></td>
						</tr>
						<tr>
							<td>&nbsp; &nbsp; save operation</td>
							<td><s:textfield title="ANONYMOUS,USER,Level_1,SUPER" name="roleOperation" theme="simple" cssStyle="width:99%"></s:textfield></td>
						</tr> 
						<tr>
							<td>&nbsp; &nbsp; show strategy code</td>
			 			    <td><s:textfield title="ANONYMOUS,USER,Level_1,SUPER" name="roleCode" theme="simple" cssStyle="width:99%"></s:textfield></td>
						</tr> 
					</s:if>
					
					<tr>
						<td  style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'>Styles</td>
						<td>
			 					<s:if test="isOwner!=true">
								<s:property value="categories"/>
								</s:if>
								<s:else>
								<s:textfield name="categories" theme="simple" cssStyle="width:99%"></s:textfield>
								</s:else>
						</td>
					</tr>
			 			
				 	<s:url action="ModelPortfolioMain.action" namespace="/jsp/strategy" id="url_modelportfolio" includeParams="none">
						<s:param name="strategyID" value="%{ID}"></s:param>
					</s:url>
					<s:if test="%{action!='create'}">
						<tr>
							<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'>
								Model Portfolio 
							</td>
							<td>
							<a style="font-size: 14px;" href='<s:property value="url_modelportfolio"/>'>more</a>
							</td>
						</tr>	
						<tr>
							<td colspan="2">
								<div style="width:98%;margin:10px">
								<s:action name="ModelPortfolioMain" namespace="/jsp/strategy" executeResult="true">
									<s:param name="strategyID" value="%{ID}"></s:param>
									<s:param name="size">10</s:param>
									<s:param name="includeHeader">false</s:param>
									<s:param name="includeJS">false</s:param>
								</s:action>
								</div>
								<span class="clear"></span>
							</td>
						</tr>	
					</s:if>
							
					<tr>
						<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'>
						<s:text name="Short Description"></s:text>
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
					
								<iframe id="ShortDescription___Frame" src="" width="99%" height="300" frameborder="no" scrolling="no">
								</iframe>
							</div>
							<div id='shortDescription_div_src'  style="display:none">
								<textarea id='shortDescription_src' style="width:99%;height:400px" onchange="ChangeShortDescriptionSrc()">
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
						<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'>
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
								<iframe id="Description___Frame" src="" width="99%" height="300" frameborder="no" scrolling="no">
								</iframe>
							</div>
							<div id='description_div_src'  style="display:none">
								<textarea id='description_src' style="width:99%;height:400px" onchange="ChangeDescriptionSrc()">
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
						<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'>
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
								<iframe id="SimilarIssues___Frame" src="" width="99%" height="300" frameborder="no" scrolling="no">
								</iframe>
							</div>
							<div id='similarIssues_div_src'  style="display:none">
								<textarea id='similarIssues_src' style="width:99%;height:400px" onchange="ChangeSimilarIssuesSrc()">
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
						<td style='font-family: Arial; width:20%; font-size: 14px;FONT-WEIGHT: bold;'>
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
									<iframe id="Reference___Frame" src="" width="99%" height="300" frameborder="no" scrolling="no">
									</iframe>
								</div>
								<div id='reference_div_src'  style="display:none">
									<textarea id='reference_src' style="width:99%;height:400px" onchange="ChangeReferenceSrc()">
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
		        
			 			<tr>
			 				<td colspan="2">
			 				<div align="center"> 
							<s:if test="%{isOwner}">
			 				<input type="button" onclick="save()" value='<s:text name="save"></s:text>' />
							<input type="button" onclick="del()" value='<s:text name="button.delete"></s:text>' />
							</s:if>
							<s:if test="operation==true">
			 				<input type="button" id="saveas" value='<s:text name="save.as"></s:text>' />
			 				</s:if>
			 				</div>
			 				</td>
			 			</tr>
			 			
			      </table>
			   </s:form>			
		</div><!-- fragment-1 end -->
 	</td>
	<td width="35%" valign="top" height="800">
		<s:url action="" namespace="/jsp/news" id="url_news" includeParams="none">
		    <s:param name="includeHeader">false</s:param>
			<s:param name="ID"><s:property value="ID"/></s:param>
		</s:url>
		<iframe src='/LTISystem/jsp/news/Strategy.action?includeHeader=false&ID=<s:property value="ID"/>' id="news" scrolling="no" width="332px" height="100%" style="" frameborder="0" marginWidth="0" marginHeight="0" ></iframe>
	</td>
	</tr>
</table><!-- container end -->
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
		    		<input id="changedName" type='text' maxlength="99"
					title="Please enter your username (at least 3 characters)"
					class="{required:true,minLength:3}"
					value='Copy of <s:property value="%{name}"/>'
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
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun'; height: 70%;  
						color: #000000; background-color:#00CCFF; border:1px solid #000000;"
						onMouseOver ="this.style.backgroundColor='#ffffff'" 
						onMouseOut ="this.style.backgroundColor='#00CCFF'" />
		                &nbsp;
		                <input name="reset" type="button"  id="cancel" value='<s:text name="button.cancel"></s:text>'  
		                height="70%"
						style="font-size: 9pt; FONT-FAMILY: Arial, 'simsun';  
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
 
<div>
<br>
</div>
</body>
</html>
