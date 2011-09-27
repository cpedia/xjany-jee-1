<a href='${raalink}'>example</a><br><br>

<#if links?exists > 
       <#list links as link>
       <a href='${link}'>example</a><br>
       </#list>
</#if>