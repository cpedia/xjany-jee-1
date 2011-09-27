<#assign groups = ["4", "5"]>
<#assign sizes=["100","0"]>

<#list sizes as size>
<#list groups as g>
#premiums ${g}
groupIDs=${g}
admin=false
userid=1
size=${size}
<#if size=="0">
paged=false
<#if g=="4">title=All LEVEL_1 Portfolios</#if>
<#if g=="5">title=All LEVEL_2 Portfolios</#if>
<#else>
<#if g=="4">title=LEVEL_1 Portfolios</#if>
<#if g=="5">title=LEVEL_2 Portfolios</#if>
</#if>
</#list>


<#assign users = ["0", "1"]>
<#list users as u>
#public ${u}
admin=false
userid=${u}
size=${size}
<#if size=="0">
title=All Public Portfolios
<#else>
title=Public Portfolios
</#if>

<#if size=="0">paged=false</#if>
</#list>

</#list>

#admin all
admin=true
userid=1
size=0
title=All Portfolios
paged=false