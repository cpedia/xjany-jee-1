<#import "common-pagination.ftl" as allBase>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
  <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>仿QQ分页效果</title>
        <script src="js/jquery-1.3.2.min.js" type="text/javascript"></script>
        <link href="css/pagination.css" rel="stylesheet" type="text/css" media="screen">
        <script type="text/javascript">
            function setRecordNum(obj){             //设置每页显示记录数
                var showRecordNum=obj.value;
                $("#show").load("paginationAction.action",{
                    sendTime:(new Date()).getTime(),
                    showRecordNum:showRecordNum
                });
            }
             
            function pagination(currentPage){       //分页，实际应用中当然需要加入必要的参数的。
                var currentPage=currentPage;
                var showRecordNum=$("#showRecordNum").val();
                $("#show").load("paginationAction.action",{
                    sendTime : (new Date()).getTime(),
                    currentPage:currentPage,
                    showRecordNum:showRecordNum
                });
            }
        </script>
         
  </head>
  
   
  <body>
    
   
  <div id="show">
  <@allBase.pagination></@allBase.pagination>
   <br/>
    <table>
        <tr>
            <th>学生学号</th>
            <th>学生姓名</th>
        </tr>
        <#list stuList as sl>
        <tr>
            <td>${sl.stuId}</td>
            <td>${sl.stuName}</td>
         
        </tr>
        </#list>
         
    </table>
  </div>
   
   
    <#assign currentRecord=showRecordNum>
     每页显示记录数：
    <select id="showRecordNum" style="width:50px;" onchange="return setRecordNum(this)">
        <option <#if currentRecord==5> selected="selected"  </#if> value="5">5条</option>
        <option <#if currentRecord==10> selected="selected" </#if> value="10">10条</option>
        <option <#if currentRecord==15> selected="selected" </#if> value="15">15条</option>
        <option <#if currentRecord==20> selected="selected" </#if> value="20">20条</option>
    </select>
  </body>
</html>