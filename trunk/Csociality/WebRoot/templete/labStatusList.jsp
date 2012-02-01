<%@page contentType="text/html; charset=utf-8"%>
<html>
<head>
<%
  String path = request.getContextPath();
%>
<title>实验室情况列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<%=path%>/css/main.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=path%>/js/gtpt/hnt.js"></script>
<style type="text/css">
  <!--
    BODY
    {
    SCROLLBAR-FACE-COLOR: #BBD1FF;
    SCROLLBAR-HIGHLIGHT-COLOR: #F0F5FF;
    SCROLLBAR-SHADOW-COLOR: #F0F5FF;
    SCROLLBAR-3DLIGHT-COLOR: #666666;
    SCROLLBAR-ARROW-COLOR: #000000;
    SCROLLBAR-TRACK-COLOR: #F0F5FF;
    SCROLLBAR-DARKSHADOW-COLOR: #666666;
    }
  -->
</style>
</head>
<script language="JavaScript" type="text/JavaScript">
//查询条件显示情况
function ShowValue(SearchName){
//  alert(SearchName);
  var show=document.getElementsByName("show");
  show[0].style.display="none";
  document.forms[0].SearchValue.value = "";
 if (SearchName != ""){
    show[0].style.display="";
  }
}
</script>
<body>
    <div id="calendar" style="Z-INDEX: 1; BEHAVIOR: url(<%=request.getContextPath()%>/Library/calendar.htc)" CanMove="true">  </div>
<form action="" method="POST">
<input type="hidden" id="context_path" name="contextPath" value="<%=path%>" />
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="borderBlue">
  <tr>
    <td align="left" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" bgcolor="#66CCFF" align="center">
            <table width="98%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="font12W"> 混凝土监管：实验室情况列表
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td align="center" valign="top" class="lineHgrayS">
            <table width="98%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                  <table border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td>查询条件：</td>
                      <td>
                        <select name="SearchName" onChange="ShowValue(this.value)" style="width:120">
                           <option value="" style="color:#0000ff">不限</option>
                          <option <%=(("carType".equals("carType"))?"selected":"")%> value="reciveno">企业编号</option>
                          <option <%=(("power".equals("power"))?"selected":"")%> value="power">单位名称</option>
                        </select>
                      </td>
                      <td>
                        <div  id="show" name="show" <%=((!"reciveno".equals("reciveno")) && (!"power".equals("power"))?"":"style=display:none")%> >
                          <input  class="input" size="20" name="SearchValue" />
                          </div>  </td>
                      <td bgcolor="#f1f1f1">
                        <input type="radio" name="state" >所有
                        <input type="radio" name="state" >待备案
                        <input type="radio" name="state" >已备案
                        <input type="radio" name="state" >退回修改
                      </td>
                 <td>&nbsp;<input type="submit" class="small_bt_bg1" value="查询"></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr >
    <td height="25" align="right">
    <%--
        <input type="button" value="新增" class="small_bt_bg2" onclick="hntInfo('HNTEmployee','2')"/>&nbsp;
        <input type="button" style="CURSOR: pointer" class="small_bt_bg2" value="导出EXCEL" onclick=""/>&nbsp;&nbsp;
     --%>
    </td>
  </tr>
  <tr>
    <td align="center" valign="top">
    
    <table width="98%" border="0" cellpadding="3" cellspacing="1" bgcolor="#CCCCCC" class="font12">
	[#list table?keys as tr]
	<tr class="${table?keys}">
	[#assign td = table[tr]]   
	[#list td?keys as tdkey]
	   <td class="${td?keys}">${td[tdkey]}</td>
	[/#list]
	</tr>
	[/#list]
	</table>

</td></tr></table></td></tr></table></form>
</body>
</html>