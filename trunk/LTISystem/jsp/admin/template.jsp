<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="decorator" uri="/WEB-INF/sitemesh-decorator.tld"%>
<%@taglib prefix="page" uri="/WEB-INF/sitemesh-page.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
     response.setHeader("Pragma", "no-cache");
     response.setHeader("Cache-Control", "no-cache");
     response.setDateHeader("Expires", 0);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		
		<style>
		
		/*基本信息*/
		body {font:12px Tahoma;margin:0px;text-align:center;background:#FFF;}
		
		/*页面层容器*/
		#container {width:100%}
		
		/*页面头部*/
		#Header {width:100%;margin:0 auto;height:100px;background:#FFCC99}
		
		/*页面主体*/
		#PageBody {width:100%;margin:0 auto;background:#CCFF00}
		
		/*侧边栏*/
		#Sidebar {width:25%;margin:0 auto;height:100%;background:#CCF120;float:left}
		
		/*主体内容*/
		#MainBody {width:75%;margin:0 auto;height:500px;background:#CCF880;float:left}
		
		/*页面底部*/
		#Footer {width:100%;margin:0 auto;height:50px;background:#00FFFF;float:left}
		
		</style>
	</head>
	
	<body>
	
		<div id="container"><!--页面层容器-->
		
			<div id="Header"><!--页面头部-->
			
			</div>
			
			<div id="PageBody"><!--页面主体-->
			
				<div id="Sidebar"><!--侧边栏-->
					1. User Manager
					2. Group Manager
				</div>
			
				<div id="MainBody"><!--主体内容-->
				
					
				</div>
			
			<div id="Footer"><!--页面底部-->
			
			</div>
			</div>

			
		</div>
		
	</body>
</html>
