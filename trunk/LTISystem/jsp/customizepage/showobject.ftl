[#ftl]
<table>
[#list table as tr]
  <tr>
  [#list tr as td]
    [#if tr_index==0]
       <th>${td}</th>
    [#else]
       <td>${td}</td>
    [/#if]
  [/#list]
  </tr>
[/#list]
<table>