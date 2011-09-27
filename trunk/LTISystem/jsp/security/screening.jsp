<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="screening" content="vf_current"/>
<meta name="tools" content="vf_current" />
<meta name="submenu" content="tools"/>
<title>Security Screening Page</title>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>


<SCRIPT src="images/multiselector.js" type=text/javascript></SCRIPT>


<script type="text/javascript">

function YearSelector(selYear)
{
    this.selYear = selYear;
    this.InitYearSelect();
}

// 增加一个最大年份的属性
YearSelector.prototype.MinYear = 1970;

// 增加一个最大年份的属性
YearSelector.prototype.MaxYear = (new Date()).getFullYear();

// 初始化年份
YearSelector.prototype.InitYearSelect = function()
{
    var op = window.document.createElement("OPTION");
    // 设置OPTION对象的值
    op.value = -1;
    // 设置OPTION对象的内容
    op.innerHTML = '<s:text name="last.one.years"></s:text>';
    // 添加到年份select对象
    this.selYear.appendChild(op); 
    
    var op = window.document.createElement("OPTION");
    // 设置OPTION对象的值
    op.value = -3;
    // 设置OPTION对象的内容
    op.innerHTML = '<s:text name="last.three.years"></s:text>';
    // 添加到年份select对象
    this.selYear.appendChild(op);       
    

	var op = window.document.createElement("OPTION");
    // 设置OPTION对象的值
    op.value = -5;
    // 设置OPTION对象的内容
    op.innerHTML = '<s:text name="last.five.years"></s:text>';
    // 添加到年份select对象
    this.selYear.appendChild(op);
    
    // 循环添加OPION元素到年份select对象中
    for(var i = this.MaxYear; i >= this.MinYear; i--)
    {
        // 新建一个OPTION对象
        var op = window.document.createElement("OPTION");
        
        // 设置OPTION对象的值
        op.value = i;
        
        // 设置OPTION对象的内容
        op.innerHTML = i;
        
        // 添加到年份select对象
        this.selYear.appendChild(op);
    }
}
</script>
</head>

<body>
	<div style="height:550px; padding: 10px;">
	
<!--		<ul>
			<li><a href='#nomalScreening'><span>Basic Screening</span></a></li>
	        <li><a href="#advancedScreening"><span>Advanced Screening</span></a></li>
		</ul>-->
	    <div id="nomalScreening">
	    	<s:property value="classTree" escape="false"/>
		    <s:form theme="simple">
		    	<p><s:text name="security.name"></s:text><s:textfield name="Name" id="securityName" theme="simple"></s:textfield></p>
			
				<p>
					<s:text name="select.security.type"></s:text>
					<s:checkboxlist list="typeList" listKey="Type" listValue="Name" name="BasicType" theme="simple"></s:checkboxlist>
					<input type="button" value="More Options" onclick="MoreOptions()">
				</p>
				<script type="text/javascript">
					function MoreOptions(){
						var checkBox = document.getElementsByName("BasicType");
						var type = "choosed type are ";
						var assetClassShow = false;
						for(var i = 0; i < checkBox.length; i++){
							if(checkBox[i].checked == true){
								//alert(checkBox[i].value);
								var id = checkBox[i].value;
								$("#Extra" + id).show();
								if(id == 1 || id == 2 || id == 3 || id == 4){
									assetClassShow = true;
								}
							}
							else{
								var id = checkBox[i].value;
								$("#Extra" + id).hide();
							}
						}
						if(assetClassShow == true)
							$("#assetClass").show();
						else
							$("#assetClass").hide();
					}
				</script>
				<s:iterator value="TypeList" status="st1">
					<s:set name="ST" value="Type"></s:set>
					<div style="display:none" id='Extra<s:property value="#ST"/>'>
						<s:iterator value="ExtraAttrs.ExtraAttributes" status="st">
							
							
								<s:if test='SecurityType == #ST'>
								
								<p>
									<input type="checkbox" name='extraAttrs.ExtraAttributes[<s:property value="#st.count-1"/>].Choosed' value="true"/>
									<s:property value="AttributeName"/>: &nbsp;&nbsp;
									<s:if test="IsSingleValue == true && ShowType == 2">
										<s:textfield name="extraAttrs.ExtraAttributes[%{#st.index}].valueForStr" theme="simple"></s:textfield>
									</s:if>
									<s:elseif test="IsSingleValue == false && ShowType == 2">
										<s:text name="select.max.value"></s:text><s:textfield name="extraAttrs.ExtraAttributes[%{#st.index}].MaxValueStr" theme="simple"></s:textfield>
										<s:text name="select.min.value"></s:text><s:textfield name="extraAttrs.ExtraAttributes[%{#st.index}].MinValueStr" theme="simple"></s:textfield>
									</s:elseif>

								</p>
								
								</s:if>
							
						</s:iterator>
					</div>
				</s:iterator>
				<s:iterator value="ExtraAttrs.ExtraAttributes" status="st">
					
						<s:if test="SecurityType == 10 && ShowType == 1">
							<div id="assetClass" style="display:none">
								<input type="checkbox" name='isUseAssetClass' value="true"/> <s:property value="AttributeName"/>
								<div id='assetClassPane'></div>
								<input type="hidden" id='assetClassIDField' name='assetClassID' />
								<script type="text/javascript">
									createList('classTree','assetClassPane','assetclassID123456','assetClassIDField','<s:property value="assetClassID"/>');
								</script>
							</div>
						</s:if>
					
				</s:iterator>
<!--				<p>
					<s:text name="assetClass"></s:text>
					
					<select name="isUseAssetClass">
						<option value="true" selected>Used</option>
						<option value="false">Not Used</option>
					</select>
					<p>
						<div id="assetClassPane"></div>
						<input type="hidden" id="assetClassID" name="assetClassID" />
						<script type="text/javascript">
							createList('classTree','assetClassPane','assetclassID654321','assetClassID','1');
						</script>
					</p>
				</p>-->
			
				<!--<p><s:text name="select.security.mpt"></s:text>
				<s:checkboxlist id="showlist" list="sortList" listKey="Value" listValue="Name" name="ShowList" theme="simple"></s:checkboxlist></p>-->
		
				<table id="">
					<thead>
						<th></th>
						<th>MPT</th>
						<th>Interval</th>
						<th>
							<p>Max Value</p>				
							<p>(using percentage, e.g: 40%)</p>
						</th>
						<th>
							<p>Min Value</p>
							<p>(using percentage, e.g: 40%)</p>
						</th>
					</thead>
					<tbody>
						<s:iterator value="MPTList" status="st">
						<tr>
							<td>
								<input type="checkbox" name='MPTList2[<s:property value="#st.count-1"/>].Choosed' value="true">
							</td>
							<td><s:property value="Name"/><s:hidden name="MPTList2[%{#st.index}].Name"></s:hidden></td>
							<td>
								<select id='selYear<s:property value="#st.count"/>' name='MPTList2[<s:property value="#st.count-1"/>].Year'></select>
								<script type="text/javascript">
									var selYear = window.document.getElementById('selYear<s:property value="#st.count"/>');				
									// 新建一个YearSelector类的实例，将三个select对象传进去
									new YearSelector(selYear);
								</script>
							</td>
							<td><s:textfield name="MPTList2[%{#st.index}].MaxStr" theme="simple"></s:textfield></td>
							<td><s:textfield name="MPTList2[%{#st.index}].MinStr" theme="simple"></s:textfield></td>
						</tr>
						</s:iterator>
					</tbody>
				</table>
				
						<s:submit key="button.confirm" action="NormalScreening" theme="simple"></s:submit>
			</s:form>
	    </div>
    
<!--	    <div id="advancedScreening">
		<s:property value="classTree" escape="false"/>
		<s:form id="screeningform" theme="simple">
			<p><s:text name="security.name"></s:text><s:textfield name="Name" id="securityName" theme="simple"></s:textfield></p>
			<p><s:text name="select.security.type"></s:text><s:checkboxlist list="typeList" listKey="Type" listValue="Name" name="AdvancedType" theme="simple" onclick="moreOpt()"></s:checkboxlist></p>
			
			<table id="MPTCondition">
				<thead>
					<th>Selected</th>
					<th>MPT</th>
					<th>Starting Date</th>
					<th>End Date</th>
					<th>
						<p>Max Value</p>				
						<p>(using percentage, e.g: 40%)</p>
					</th>
					<th>
						<p>Min Value</p>
						<p>(using percentage, e.g: 40%)</p>
					</th>
				</thead>
				<tbody>
					<s:iterator value="MPTList" status="st">
					<tr>
						<td>
							<input type="checkbox" name='MPTList[<s:property value="#st.count-1"/>].Choosed' value="true">
						</td>
						<td><s:property value="Name"/><s:hidden name="MPTList[%{#st.index}].Name"></s:hidden></td>
						<td><s:textfield name="MPTList[%{#st.index}].StartingDate"  theme="simple"></s:textfield></td>
						<td><s:textfield name="MPTList[%{#st.index}].EndDate" theme="simple"></s:textfield></td>
						<td><s:textfield name="MPTList[%{#st.index}].MaxStr" theme="simple"></s:textfield></td>
						<td><s:textfield name="MPTList[%{#st.index}].MinStr" theme="simple"></s:textfield></td>
					</tr>
					</s:iterator>
				</tbody>
			</table>
			<s:submit key="button.confirm" action="AdvancedScreening" theme="simple"></s:submit>
		</s:form>
		</div>-->
	</div>
	
	

</body>
</html>
