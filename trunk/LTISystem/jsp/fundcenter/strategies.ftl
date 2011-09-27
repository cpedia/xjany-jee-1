<#if strategies??>
	<#list strategies as strategy>
		<#if strategy?? && strategy.ID??><p style="padding:0;margin:2px"><a href="/LTISystem/jsp/strategy/View.action?ID=${strategy.ID}&action=view" target="_blank">${strategy.name!}</a></p></#if>
	</#list>
<#else>
	<p><strong>no quoted strategies!</strong></p>
</#if>