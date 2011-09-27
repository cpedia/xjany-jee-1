<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<s:url id="contenturl" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
	<s:param name="pageName"><s:property value='content'/></s:param>
</s:url>

<script>location.href='<s:property value="%{contenturl}"/>';</script>

