<#ftl strip_whitespace=true>
<label><input type="checkbox"<#rt/>
 value="${rkey}"<#rt/>
<#if valueList?seq_contains(rkey)> checked="checked"</#if><#rt/>
<#include "/ftl/pony/ui/common-attributes.ftl"/><#rt/>
<#include "/ftl/pony/ui/scripting-events.ftl"/><#rt/>
/>${rvalue}</label><#if hasNext> </#if>