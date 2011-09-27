<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page  contentType="text/html; charset=UTF-8"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="blm" content="vf_current"/>
<meta name="tools" content="vf_current" />
<meta name="submenu" content="tools"/>
<title>BL Main</title>
<!-- new theme -->







<!-- Initialize BorderLayout END-->


<!-- These CSS are necessary START -->


<!-- These CSS are necessary END -->



</head>

<body>
<s:if test="Result != null">
	<table width="800 px">
		<thead>
			<th align="center">Symbol</th>
			<th align="center">Weight</th>
			<th align="center">Normalized Weight</th>
		</thead>
		<tbody>
			<s:iterator value="Result">
			<tr>
				<td align="center"><s:property value="Symbol"/></td>
				<td align="center"><s:property value="OrdinaryWeight"/></td>
				<td align="center"><s:property value="NormalizedWeight"/></td>
			</tr>
			</s:iterator>
			
		</tbody>
	</table>
</s:if>
<s:if test="covarience != null">
	<p>Covarience:</p>
	<p>
			<table style="border:1 solid #000000;">
				<s:iterator value="covarience.covarience">
					<tr>
						<s:iterator value="row">
							<td style="border:1 solid #000000;"><s:property value="Weight"/></td>
						</s:iterator>
					</tr>
				</s:iterator>
			</table>
	</p>
</s:if>
</body>
</html>
