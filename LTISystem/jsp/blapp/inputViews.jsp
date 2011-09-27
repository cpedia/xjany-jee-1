<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="blm" content="vf_current"/>
<meta name="tools" content="vf_current" />
<meta name="submenu" content="tools"/>
<title>BL Main</title>
<script type="text/javascript" src="../images/jquery-1.2.6.pack.js"></script>
<!-- new theme -->
<script type="text/javascript" src="../images/jquery.ui/ui.tabs.packed.js"></script>
<script type="text/javascript" src="../images/jquery.dimensions.js"></script>
<script type="text/javascript" src="../images/jquery.bgiframe.js"></script>

<script src="../images/jquery.ui.dialog/ui.draggable.packed.js" type="text/javascript"></script>
<script src="../images/jquery.ui.dialog/ui.droppable.packed.js" type="text/javascript"></script>
<script src="../images/jquery.ui.dialog/ui.resizable.packed.js" type="text/javascript"></script>



<!-- Initialize BorderLayout END-->



<!-- These CSS are necessary START -->


<!-- These CSS are necessary END -->


<script type="text/javascript" src="swfobject_source.js"></script>


</head>

<body>

<s:form name="ViewForm" namespace="/jsp/blapp">
	<span id="ViewRef">
	<table>
	<s:if test="ViewList != null && ViewList.size()!=0">
		<tr>
			<td colspan="3">
				<s:select id="chooseView" name="ViewID" list="ViewList" listValue="Name" listKey="ID" theme="simple" onchange="formSubmit()"></s:select>
				<script type="text/javascript">
					function formSubmit(){
						//alert("hello!");
						document.ViewForm.action = "../blapp/CheckView.action";
						document.ViewForm.submit();
					}
				</script>
			</td>
			
		</tr>
	</s:if>
	<tr>
		<td width="20%">Forword Horizon</td>
		<td><s:textfield name="BLForm.Forward" readonly="true" theme="simple"></s:textfield>(Month)</td>
		<td rowspan="7">
				<font size="2">
				<p>
				For example, consider a portfolio holding just 4 assets, assets A, B, C and D, you can express four kinds of view for your portfolio. 
				</p>
				<p>
				View 1: "I believe that asset A will return 3%"
				You can rewrite View 1 as [1,0,0,0] .* [A ,B,C,D]=0.03, since 1*A+0*B+0*C+0*D=0.03 according to View 1. 
				This is a absolute view to one asset. The coefficient is [1, 0, 0, 0], the target value is 0.03. 
				</p>
				<p>
				View 2: "I believe that a combination of 20% A and 80% B will return 4%"
				You can rewrite View 2 as [0.2, 0.8, 0, 0].*[A, B, C, D] =0.04, since 0.2*A+0.8*B+0*C+0*D
				=0.04 according to View 2.
				This is a absolute view to a combination of assets. The coefficient is [0.2, 0.8, 0, 0], the target value is 0.03.
				</p>
				<p>
				View 3: "I believe that asset A will outperform asset B with 2%" 
				You can rewrite View 3 as [1,-1,0, 0].*[A,B,C,D]=0.02,since 1*A-1*B+0*C+0*D=0.02.
				This is a relative view to two assets. The coefficient is [1,-1,0, 0],the target value is 0.02.
				</p>
				<p>
				View 4: "I believe that the combination of 20% A and 80% B will outperform asset C with 2%"
				You can rewrite View 4 as [0.2, 0.8,-1, 0].*[A, B, C, D]=0.02, since 0.2*A+0.8*B-1*C+0*D=0.02 according to View 4.
				This is a relative view to a combination of asset. The coefficient is [0.2, 0.8,-1, 0], the target value is 0.02.
				</p>
				</font>
		</td>
	</tr>
	<tr>
		<td><s:text name="bl.name"></s:text></td>
		<td><input type="text" name="view.Name" value='<s:property value="view.Name"/>'/></td>
	</tr>
	<tr>
		<td colspan="2">
			<table>
				<thead>
					<th><s:text name="symbol"></s:text></th>
					<th><s:text name="bl.coefficient"></s:text></th>
				</thead>
				<tbody>
				<s:iterator value="view.view" status="st">
					<tr>
						<td><s:property value="symbol"/></td>
						<td>
							<s:textfield name="view.view[%{#st.index}].weight" theme="simple" required="true"></s:textfield>
							<s:hidden name="view.view[%{#st.index}].symbol" value="%{symbol}"></s:hidden>
						</td>
					</tr>
				</s:iterator>
				</tbody>
			</table>
		</td>
	</tr>
	
	<tr>
		<td colspan="2">
			<div>
				TIPS: My target value is the target return for each of my view.
			</div>
		</td>
	</tr>
	<tr>
		<td><s:text name="bl.target.return"></s:text></td>
		<td><input type="text" name="view.targetValue" value='<s:property value="view.targetValue"/>' /></td>
	</tr>
	<tr>
		<td colspan="2">
			<div>
				TIPS: My confidence level is my confidence for my view, which is between 0 and 1. If you are 100% sure for your view, please set 1; If you are 0% sure for your view, please set 0 or just delete your view.
			</div>
		</td>
	</tr>
	<tr>
		<td><s:text name="bl.confidence.level"></s:text></td>
		<td><input type="text" name="view.myViewOnWeight" value='<s:property value="view.myViewOnWeight"/>'/></td>
	</tr>
	</table>
	<p>
		<font color="red" size="2">You must save the view before you do the calculation!</font>
	</p>
	<s:submit action="ViewSave" align="left" key="save" theme="simple"></s:submit>

	<s:if test="ViewList != null">
		<s:submit action="Calculate" align="left" key="button.calcuate" theme="simple"></s:submit>
	</s:if>
	
	</span>
</s:form>

</body>
</html>
