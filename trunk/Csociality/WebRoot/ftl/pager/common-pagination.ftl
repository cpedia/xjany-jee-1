<#macro pagination>
<div class="common-pagination">
    <#if (currentPage>1)>
        <a href="javascript:void(0)" onclick="pagination(${currentPage}-1)" class="enable"><span>上一页</span></a>
    <#else>
        <a class="disable"><span>上一页</span></a>
    </#if>  
    <#if (totalPages<9)>       <#-->总页数小于9的情况每页都显示 <-->
        <#list 1..(totalPages) as pages>
            <#if (pages==currentPage)>    <#-->如果是当前页 <-->
                <a class="current"><span>${currentPage}</span></a>
            <#else>
                <a href="javascript:void(0)" onclick="pagination(${pages})"><span>${pages}</span></a>
            </#if>
        </#list>
    <#else>
        <#if (currentPage<5)>
            <#list 1..5 as pages>
                 
                <#if (pages==currentPage)>
                    <a class="current"><span>${currentPage}</span></a>
                <#else>
                    <a href="javascript:void(0)" onclick="pagination(${pages})"><span>${pages}</span></a>
                </#if>
                 
            </#list>
             
            <#if (currentPage==4)>
                <a href="javascript:void(0)" onclick="pagination(6)"><span>6</span></a>
            </#if>
             
            <span class="points">...</span>
            <a href="javascript:void(0)"onclick="pagination(${totalPages})"><span>${totalPages}</span></a>
             
        <#elseif (currentPage>=5&¤tPage<(totalPages-3))>
            <a href="javascript:void(0)" onclick="pagination('1')"><span>1</span></a>
            <span class="points">...</span>
            <#list (currentPage-2)..(currentPage+2) as pages>
                <#if (pages==currentPage)>
                    <a class="current"><span>${currentPage}</span></a>
                <#else>
                    <a href="javascript:void(0)" onclick="pagination(${pages})"><span>${pages}</span></a>
                </#if>
            </#list>
            <span class="points">...</span>
            <a href="javascript:void(0)" onclick="pagination(${totalPages})"><span>${totalPages}</span></a>
        <#else>
            <a href="javascript:void(0)" onclick="pagination('1')"><span>1</span></a>
            <span class="points">...</span>
            <#if (currentPage==totalPages-3)>
                <a href="javascript:void(0)" onclick="pagination(${currentPage}-3)"><span>${currentPage-2}</span></a>
            </#if>
            <#list (totalPages-4)..(totalPages) as pages>
                <#if (pages==currentPage)>
                    <a class="current"><span>${currentPage}</span></a>
                <#else>
                    <a href="javascript:void(0)" onclick="pagination(${pages})"><span>${pages}</span></a>
                </#if>
            </#list>
        </#if>
    </#if>   
    <#if (currentPage<totalPages)>
        <a href="javascript:void(0)" onclick="pagination(${currentPage}+1)" class="enable"><span>下一页</span></a>
    <#else>
        <a class="disable"><span>下一页</span></a>
    </#if>
    <div>
 </#macro>