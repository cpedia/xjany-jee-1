<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>

<fieldset>
	<legend><s:text name="Result"/></legend>
	<table>
		<thead bgcolor="yellow">
			<th>Assets</th>
			<s:iterator value="securityList" id="security">
			<th><s:property value="security"/></th>
			</s:iterator>
		</thead>
		<tr>
			<td>Weights</td>
			<s:iterator value="#request.weights" id="result" status="stat">
			<td id='w_result<s:property value="#stat.index"/>'>
			<s:property value="result"/>
			</td>
			<script>
			var num=$('#w_result<s:property value="#stat.index"/>').val();
			$('#w_result<s:property value="#stat.index"/>').val(num.toFixed(3));
			</script>
			</s:iterator>
		</tr>
	</table>
</fieldset>
<img src='../../<s:property value="filePathName"/>'>

<fieldset>
	<legend><s:text name="Correlation"/></legend>
	<table>
		<s:iterator id="matrix_row" value="#request.covarienceStrings">
		    <tr>
		    <td>
		    <s:iterator id="matrix_col" value="#request.matrix_row">
		       <td><s:property value="matrix_col"/></td>
		    </s:iterator>
		    </tr>
		</s:iterator>
	</table>
</fieldset>
