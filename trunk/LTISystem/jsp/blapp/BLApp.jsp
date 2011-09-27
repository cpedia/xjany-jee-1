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
<script type="text/javascript">

var indexCount=0;
var count = 0;
function addNewIndex(){
			indexCount=indexCount+2;
						
			var $tr=$(document.createElement("tr"));
			var $td=$(document.createElement("td"));

			$tr.append($td);
			$td.append('<input type="text" id="index' + (indexCount) + '" name="BLForm.investList[' + count + '].symbol" />')
			
			$td=$(document.createElement("td"));

			$td.append('<input type="text" id="index' + (indexCount+1) + '" name="BLForm.investList[' + count + '].weightStr"/>')
			
			$tr.append($td);
			
			$("#dataTableBody").append($tr);
			
			count++;
			
		}

<!-- Initialize BorderLayout END-->


<!-- These CSS are necessary START -->


<!-- These CSS are necessary END -->


</script>

<style type="text/css">
#BLAppForm{
	padding-left:10px;
	padding-right:10px;
}
</style>

</head>

<body>

<s:form id="BLAppForm" namespace="/jsp/blapp">
<p><font color="red"> <s:actionerror/> </font></p>
<span id="Basic">
	<p>
		Step 1: Basic Infomation
	</p>
	<table align="center">
		<tr>
			<td width="15%">Risk Aversion:</td>
			<td width="20%">
				<s:if test='BLForm.InvestList == null || action.equals("error")'>
					<input type="text" name="BLForm.RiskAversion" value='<s:property value="BLForm.RiskAversion"/>'/>
				</s:if>
				<s:else>
					<input type="text" name="BLForm.RiskAversion" readonly value='<s:property value="BLForm.RiskAversion"/>'/>
				</s:else>
			</td>
			<td width="45%">
				<div>TIPS: The larger your risk aversion is, the more percentages of return you require, with respected to one unit of risk. The usual level of risk aversion is 3.</div>
			</td>
		</tr>
		<tr>
			<td>Risk Free Symbol:</td>
			<td>
				<s:if test='BLForm.InvestList == null || action.equals("error")'>
					<input type="text" name="BLForm.RiskFreeSymbol" value='<s:property value="BLForm.RiskFreeSymbol"/>'/>
				</s:if>
				<s:else>
					<input type="text" readonly name="BLForm.RiskFreeSymbol" value='<s:property value="BLForm.RiskFreeSymbol"/>'/>
				</s:else>
			</td>
			<td>
				<div>TIPS: The usual risk free symbol is ^IRX, which is the symbol of 13_WEEK TREASURY BILL. You can use CASH instead as our LTI system does.</div>
			</td>
		</tr>
		<tr>
			<td>Backward Horizon:</td>
			<td>
				<s:if test='BLForm.InvestList == null || action.equals("error")'>
					<input type="text" name="BLForm.Backward" value='<s:property value="BLForm.Backward"/>'/>(Months)
				</s:if>
				<s:else>
					<input type="text" readonly name="BLForm.Backward" value='<s:property value="BLForm.Backward"/>'/>(Months)
				</s:else>
			</td>
			<td>
				<div>TIPS: Backward Horizon is the time interval for historical daily price, which is used to calculate historical covariance matrix and historical return; you’d better set a number more than 6, the time unit for backward horizon is month.</div>
			</td>
		</tr>
		<tr>
			<td>Forward Horizon:</td>
			<td>
				<s:if test='BLForm.InvestList == null || action.equals("error")'>
					<input type="text" name="BLForm.Forward" value='<s:property value="BLForm.Forward"/>'/>(Months)
				</s:if>
				<s:else>
					<input type="text" readonly name="BLForm.Forward" value='<s:property value="BLForm.Forward"/>'/>(Months)
				</s:else>
			</td>
			<td>
				<div>TIPS: Forward Horizon is the time interval for you to set your future expected return. The time unit for Forward horizon is month. The default value is 12 month, which means that your expected return is annualized rate.</div>
			</td>
		</tr>
	</table>
	<p>
		Step 2: Create Your Portfolio
	</p>
	<s:if test='BLForm.InvestList == null || action.equals("error")'>
		<input type="button" value="Create Your Portfolio" onclick="addPortfolio()" />
		<script type="text/javascript">
		function addPortfolio(){
			$("#Portfolio").show();
		}
		</script>
	</s:if>
</span>
	<div id="Portfolio" style="display:none">
		<s:if test='BLForm.InvestList == null || action.equals("error")'>
		<a href="#" onclick=addNewIndex()>Add A Security</a>
		<table id="dataTable">
			<thead><th>Symbol</th><th>Weight(decimal or percentage)</th></thead>
			<tbody id="dataTableBody">
			</tbody>
		</table>
		</s:if>
	</div>
	<s:else>
		<s:iterator value="BLForm.InvestList" status="st">
			<p>
			<s:property value="Symbol"/>
			<s:hidden name="BLForm.investList[%{#st.index}].symbol" value="%{Symbol}"></s:hidden>
			<s:if test="action == null">
				<s:textfield readonly="true" name="BLForm.investList[%{#st.index}].weight" value="%{Weight}" theme="simple"></s:textfield>
			</s:if>
			<s:else>
				<s:textfield name="BLForm.investList[%{#st.index}].weightStr" value="%{Weight}" theme="simple"></s:textfield>
			</s:else>
			</p>
		</s:iterator>
	</s:else>
<p>	
	<s:if test='BLForm.InvestList == null || action.equals("error")'>
		<s:submit id="BasicInfo" action="BasicSave" key="button.confirm" theme="simple"></s:submit>
	</s:if>
</p>
<p>
	<s:if test='BLForm.InvestList != null && action == null'>
		<p>
			Step 3: Create Your Views
		</p>
		<p>
			<a id="help" href="#">Help on How to Create a View</a>
			<script	type="text/javascript">
				$("#help").toggle(function() {
					$('#createViewHelp').show();
				}, function() {
					$('#createViewHelp').hide();
				});
			</script>
		</p>
		<div id="createViewHelp" style="display:none">
			<p>
				For example, consider a portfolio holding just 4 assets, assets A, B, C and D, you can express four kinds of view for your portfolio. 
			</p>
			<p>
				View 1: “I believe that asset A will return 3%”
				You can rewrite View 1 as [1,0,0,0] .* [A ,B,C,D]=0.03, since 1*A+0*B+0*C+0*D=0.03 according to View 1. 
				This is a absolute view to one asset. The coefficient is [1, 0, 0, 0], the target value is 0.03. 
			</p>
			<p>
				View 2: “I believe that a combination of 20% A and 80% B will return 4%”
				You can rewrite View 2 as [0.2, 0.8, 0, 0].*[A, B, C, D] =0.04, since 0.2*A+0.8*B+0*C+0*D
				=0.04 according to View 2.
				This is a absolute view to a combination of assets. The coefficient is [0.2, 0.8, 0, 0], the target value is 0.03.
			</p>
			<p>
				View 3: “I believe that asset A will outperform asset B with 2%” 
				You can rewrite View 3 as [1,-1,0, 0].*[A,B,C,D]=0.02,since 1*A-1*B+0*C+0*D=0.02.
				This is a relative view to two assets. The coefficient is [1,-1,0, 0],the target value is 0.02.
			</p>
			<p>
				View 4: “I believe that the combination of 20% A and 80% B will outperform asset C with 2%”
				You can rewrite View 4 as [0.2, 0.8,-1, 0].*[A, B, C, D]=0.02, since 0.2*A+0.8*B-1*C+0*D=0.02 according to View 4.
				This is a relative view to a combination of asset. The coefficient is [0.2, 0.8,-1, 0], the target value is 0.02.
			</p>			
		</div>
		<p>
			<input id="CreateNewView" type="button" value="Create A New View"/>
			<script	type="text/javascript">
				$("#CreateNewView").click(function(){
					var num = $("#chooseView").attr("length");
					for(i = 1; i <= num; i++){
						$("#view_" + i).hide();
					}
					$("#NewView").show();
					//$("#BasicInfo").hide();
				})
			</script>
			<s:if test="ViewList != null && ViewList.size()!=0">
				<s:select id="chooseView" list="ViewList" listValue="Name" listKey="viewID" theme="simple" onchange="showView()"></s:select>
				(Select and Check An Existing View)
				<script type="text/javascript">
					$("#chooseView").click(function(){
						var num = $("#chooseView").attr("length");
						if(num == 1){
							$("#NewView").hide();
							$("#view_" + 1).show();
						}
					})
					function showView(){
						//alert("hello!");
						var ViewID = $("#chooseView").val();
						//alert(ViewID);
						/*get the number of views*/
						var num = $("#chooseView").attr("length");
							
						$("#NewView").hide();
						for(i = 1; i <= num; i++){
							if(i == ViewID){
								$("#view_" + ViewID).show();						
							}
							else
							{
								$("#view_" + i).hide();
							}
						}
					}
				</script>
			</s:if>
		</p>
	</s:if>
</p>


<s:if test="ViewList != null && ViewList.size() != 0">
	<s:iterator value="ViewList" status="st">
		<div style="display:none"  id='view_<s:property value="#st.count"/>'>
			<span id="ViewRef">
				<table width="100%">
					<tr>
						<td><s:text name="bl.name"></s:text></td>
						<td><!--<input type="text" name="view.Name" value='<s:property value="view.Name"/>'/>-->
							<s:textfield name="viewList[%{#st.index}].name" value="%{Name}" theme="simple"></s:textfield>
							<s:hidden name="viewList[%{#st.index}].viewID" value="%{viewID}"></s:hidden>
						</td>
					</tr>
					<tr>
						<table>
							<thead>
								<th><s:text name="symbol"></s:text></th>
								<th><s:text name="bl.coefficient"></s:text></th>
							</thead>
							<tbody>
							<s:iterator value="View" status="st_view">
								<tr>
									<td><s:property value="symbol"/></td>
									<td>
										<s:textfield name="viewList[%{#st.index}].view[%{#st_view.index}].weight" theme="simple" required="true"></s:textfield>
										<s:hidden name="viewList[%{#st.index}].view[%{#st_view.index}].symbol" value="%{symbol}"></s:hidden>
									</td>
								</tr>
							</s:iterator>
							</tbody>
						</table>
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
						<td>
						<!--<input type="text" name="view.targetValue" value='<s:property value="view.targetValue"/>' />-->
							<s:textfield name="viewList[%{#st.index}].targetValue" value="%{TargetValue}"></s:textfield>
						</td>
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
						<td>
							<!--<input type="text" name="view.myViewOnWeight" value='<s:property value="view.myViewOnWeight"/>'/>-->
							<s:textfield name="viewList[%{#st.index}].myViewOnWeight" value="%{MyViewOnWeight}"></s:textfield>
						</td>
					</tr>
				</table>
			</span>
		</div>
	</s:iterator>
</s:if>
<div id="NewView" style="display:none">
	<span id="ViewRef">
	<table width="100%">
		<tr>
			<td><s:text name="bl.name"></s:text></td>
			<td><input type="text" name="view.Name" value='<s:property value="view.Name"/>'/></td>
		</tr>
		<tr>
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
		<s:submit id="saveViwe" action="ViewSave" key="save" theme="simple"></s:submit>
		<script type="text/javascript">
			$("#saveViwe").click(function(){
				$("#BLAppForm").attr({target:"_self"});
			})
		</script>
	</p>
	</span>
</div>
<p>
	
	<s:if test='BLForm.InvestList != null && action == null'>
		<p>
			<font color="red" size="2">You must save the view before you do the calculation!</font>
		</p>
		<!--<s:submit key="button.calcuate" action="Calculate" theme="simple"></s:submit>-->
		<input type="button" id="Calculation" value='<s:text name="button.calcuate"></s:text>' />
		<script type="text/javascript">
			$("#Calculation").click(function(){
				
				$("#BLAppForm").attr({action:"../blapp/Calculate.action"});
				$("#BLAppForm").attr({onsubmit:"return true"});
				$("#BLAppForm").attr({target:"_blank"});
				$("#BLAppForm").submit();
			})
			
		</script>
	</s:if>
</p>
</s:form>
</body>
</html>
