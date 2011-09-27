<#assign groups = ["4", "5"]>
<#list groups as g>
#premiums ${g}
includeCategory=false
includeClass=true
includePortfolio=true
year=-1,-3,-5
includeLastTransactionDate=true
includeLastValidDate=true
mpt=ar,sharperatio
size=0
groupIDs=${g}
admin=false
userid=1
</#list>

<#assign seq = ["3", "4", "5", "1","9","10","11","12","13","14","15","2"]>
<#list seq as x>
#public ${x}
includeCategory=false
includeClassName=false
includePortfolio=true
year=-1,-3,-5
mpt=ar,sharperatio
includeLastTransactionDate=true
includeLastValidDate=true
sortColumn=6
size=0
strategyClassID=${x}
admin=false
userid=1

#anonymous ${x}
includeCategory=false
includeClassName=false
includePortfolio=true
includeLastTransactionDate=true
includeLastValidDate=true
year=-1,-3,-5
mpt=ar,sharperatio
sortColumn=6
size=0
strategyClassID=${x}
admin=false
userid=0



#all for admin ${x}
includeCategory=false
includeClassName=false
includePortfolio=true
includeLastTransactionDate=true
includeLastValidDate=true
year=-1,-3,-5
mpt=ar,sharperatio
sortColumn=6
size=0
admin=true
strategyClassID=${x}
userid=1

</#list>  

<#assign sizes = ["0", "25"]>
<#assign userids = ["0", "1"]>
<#assign sortYears = ["-1", "-3","-5"]>
<#assign sortColumns = ["6","8","10"]>

<#list sizes as size>
<#list userids as userid>
<#list sortYears as sortYear>
<#list sortColumns as sortColumn>
#top style
includeCategory=true
includeClass=true
includePortfolio=true
year=-1,-3,-5
mpt=ar,sharperatio
sortColumn=${sortColumn}
size=${size}
sortYear=${sortYear}
admin=false
userid=${userid}
</#list>
</#list>
</#list>
</#list>


#F401K 1
includeCategory=true
includeClass=true
includePortfolio=true
userid=1
groupIDs=8
type=2
urlPrefix=%2FLTISystem%2Fjsp%2Ff401k%2FView.action%3FID%3D

