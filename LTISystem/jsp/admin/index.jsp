<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html xmlns="http://www.w3.org/1999/xhtml" >
	<head>
		
		<title>Administration</title>
		<script type="text/javascript" src="images/jquery-1.2.6.min.js"></script>
		<style>
		
		
		body {font:12px Tahoma;margin:0px;text-align:center;background-color: #DCDCDC;}
		
	
		#container {width:100%;}
		
	
		#Header {width:100%;margin:0 auto;height:0px;background:#FFCC99}
		

		#PageBody {width:100%;margin:0 auto;background-color: #DCDCDC;height:90%;

		}
		
		
		#Sidebar {width:15%;margin:0 auto;float:left;height:100%;
		
			font-family: Arial, Helvetica, sans-serif;
			font-size: 12px;
			line-height: 18px;
			background-color: #FFFFFF;
		}

		#MainBody {width:84%;margin:0 auto;float:left;height:100%;
		background-color: #DCDCDC;
		}
		
	
		#Footer {width:100%;margin:0 auto;height:10%;background:#00FFFF;float:left;
			font-family: Arial, Helvetica, sans-serif;
			font-size: 14px;
			font-weight: bold;
			line-height: 22px;
			color: #333333;
			background-color: #CCCCCC;
		}
		
		a:hover {
			text-decoration: underline;
		}
		a {
			color: #3366CC;
			text-decoration: none;
		}
		
		.sidebarHeader {
			font-family: Arial, Helvetica, sans-serif;
			font-size: 16px;
			line-height: 24px;
			color: #FFFFFF;
			background-color: #339999;
		}
		
		.sidebarFooter {
			font-family: Arial, Helvetica, sans-serif;
			font-size: 12px;
			line-height: 18px;
			background-color: #CCCCCC;
		}
		</style>
	</head>
	
	<body>
	
		<div id="container">
		
			<div id="Header">
			
			</div>
			
			<div id="PageBody">
			
				<div id="Sidebar">
					<table border="0">
						<tr>
							<td class="sidebarHeader">
								Administration
							</td>
						</tr>					
						<tr>
							<td class="sidebarFooter">
								<s:url action="Main.action" id="url_group_main" namespace="/jsp/admin/group">
								</s:url>
								<a href="<s:property value="%{url_group_main}"/>" target="detail">GrpM</a>
								
								&nbsp;
								
								<s:url action="Main.action" id="url_holiday_main" namespace="/jsp/admin/holiday">
								</s:url>
								<a href="<s:property value="%{url_holiday_main}"/>" target="detail">HolidayM</a>
								
								&nbsp;
								<s:url action="Main.action" id="url_user_main" namespace="/jsp/admin/user">
								</s:url>
								<a href="<s:property value="%{url_user_main}"/>" target="detail">UserM</a>
								
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<s:url action="Main.action" id="url_security_main" namespace="/jsp/admin/security">
								</s:url>
								<a href="<s:property value="%{url_security_main}"/>" target="detail">Security_M</a>
								
								&nbsp;
								<s:url action="Main.action" id="url_indicator_main" namespace="/jsp/admin/indicator">
								</s:url>
								<a href="<s:property value="%{url_indicator_main}"/>" target="detail">Indicator_M</a>
								
								
							</td class="sidebarFooter">
						</tr>	
						
						<tr>
							<td class="sidebarFooter">
								
								
								<s:url action="Main.action" id="url_portfolio_main" namespace="/jsp/admin/portfolio">
								</s:url>
								<a href="<s:property value="%{url_portfolio_main}"/>" target="detail">Portfolio Manager</a>
								
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<s:url action="Main.action" id="url_strategy_main" namespace="/jsp/admin/strategy">
								</s:url>
								<a href="<s:property value="%{url_strategy_main}"/>" target="detail">Strategy Manager</a>
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<s:url action="ExecutionState.action" id="url_executor_main" namespace="/jsp/admin/portfolio">
								</s:url>
								<a href="<s:property value="%{url_executor_main}"/>" target="detail">Daily Update State</a>
							</td>
						</tr>	
						<tr>
							<td class="sidebarFooter">
								<a href="executionengine/executionengine.html" target="detail" id='dds'>Daily Execution State</a>
							</td>
						</tr>																
						<tr>
							<td class="sidebarFooter">
								<s:url action="Main.action" id="url_assetclass_main" namespace="/jsp/admin/assetclass">
								</s:url>
								<a href="<s:property value="%{url_assetclass_main}"/>" target="detail">AC_M</a>
								
								&nbsp;
								<s:url action="Main.action" id="url_strategyclass_main" namespace="/jsp/admin/strategyclass">
								</s:url>
								<a href="<s:property value="%{url_strategyclass_main}"/>" target="detail">SC_M</a>
								
								&nbsp;
								<s:url action="View" id="siteUrl" namespace="/jsp/admin/news">
								</s:url>
								<a href="<s:property value="%{siteUrl}"/>" target="detail">News_M</a>
							</td>
						</tr>	
						<tr>
							<td class="sidebarFooter">
								<s:url action="operation_list.action" id="url_user_operation_list" namespace="/jsp/admin/user">
								</s:url>
								<a href='<s:property value="%{url_user_operation_list}"/>' target="_blank">Event Manager</a>
							</td>
						</tr>
					    <tr>
							<td class="sidebarFooter">
								<s:url action="Main.action" id="url_cp_main" namespace="/jsp/admin/customizepage">
								</s:url>
								<a href="<s:property value="%{url_cp_main}"/>" target="detail">Customize Page Manager</a>
							</td>
						</tr>
						<!--
					    <tr>
							<td class="sidebarFooter">
								<s:url action="Main.action" id="url_rc_main" namespace="/jsp/admin/regioncustomize">
								</s:url>
								<a href="<s:property value="%{url_rc_main}"/>" target="detail">Region Customize Manager</a>
							</td>
						</tr> 
					    <tr>
							<td class="sidebarFooter">
								<s:url action="Main" id="url_cr_main" namespace="/jsp/admin/customizeregion">
								</s:url>
								<a href="<s:property value="%{url_cr_main}"/>" target="detail">Customize Region Manager</a>
							</td>
						</tr>-->
						<tr>
							<td class="sidebarFooter">
								<s:url value="loaddata/loadsecuritydata.jsp" id="url_loaddata_main" >
								</s:url>
								<a href="<s:property value="%{url_loaddata_main}"/>" target="detail">Load Data</a>
							</td>
						</tr>					
						<tr>
							<td class="sidebarFooter">
								<s:url value="factors/factors.jsp" id="url_factors_main" >
								</s:url>
								<a href="<s:property value="%{url_factors_main}"/>" target="detail">Factors Manager</a>
							</td>
						</tr>
						<!--	
						<tr>					
							<td class="sidebarFooter">
								<s:url value="validate/validate.jsp" id="url_validate_main" >
								</s:url>
								<a href="<s:property value="%{url_validate_main}"/>" target="detail">Check Mpt</a>
							</td>
						</tr>
						-->
						<tr>
							<td class="sidebarFooter">
								<s:url action="Save.action" id="url_quartz_main" namespace="/jsp/admin/quartz">
								</s:url>
								<a href="<s:property value="%{url_quartz_main}"/>" target="detail">Quartz CronExpression</a>
							</td>
						</tr>	
						<!--	
						<tr>
							<td class="sidebarFooter">
								<s:url action="Main" id="url_home_main" namespace="/jsp/main">
								</s:url>
								<a href="<s:property value="%{url_home_main}"/>" target="_self">Home Page</a>
							</td>
						</tr>			
						
						<tr>
							<td class="sidebarFooter">
								<a href="../code/codeexecutor.jsp" target="detail">Code</a>
							</td>
						</tr>	
						-->					
						<tr>
							<td class="sidebarFooter">
								<a href="filebrowser/index.html">File Browser</a>
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<s:url action="DownloadUpdateLog.action" id="url_stock_download" namespace="/jsp/admin/stock">
								</s:url>
								<a href="<s:property value="%{url_stock_download}"/>" target="detail">Stock Admin</a>
							</td>
						</tr>	
						<tr>
							<td class="sidebarFooter">
								<s:url action="MarketEmail_mainPage.action" id="url_user_mainp" namespace="/jsp/admin/user">
								</s:url>
								<a href="<s:property value="%{url_user_mainp}"/>" target="detail">Market Email Admin</a>
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<s:url action="Fundtable_main.action" id="url_fl_upanddownload" namespace="/jsp/admin/fundlist">
								</s:url>
								<a href="<s:property value="%{url_fl_upanddownload}"/>" target="detail">Fund List</a>
							</td>
						</tr>						
						<tr>
							<td class="sidebarFooter">
								<s:url action="Clone13FCenter.action" id="url_cl_download" namespace="/jsp/admin/clonecenter">
								</s:url>
								<a href="<s:property value="%{url_cl_download}"/>" target="detail">Clone 13 Center</a>
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<s:url action="main.action" id="url_ac_link" namespace="/jsp/admin/articlecenter">
								</s:url>
								<a href="<s:property value="%{url_ac_link}"/>" target="detail">Article Center</a>
								
								&nbsp;
								<a href="/LTISystem/jsp/admin/strategy/ConsumerPlans.action" target="detail">Consumer Plans</a>
							</td>
						</tr>		
						<tr>
							<td class="sidebarFooter">
								<a href="stat_list.jsp" target="detail">Statistics</a>
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<a href="adjust_closeAndNav.jsp" target="detail">AdjustMent</a>
							</td>
						</tr>	
						<tr>
							<td class="sidebarFooter">
								<a href="email.jsp" target="detail">EMail Alert</a>
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<a href="/LTISystem/jsp/register/adminViewInvite.action?includeHeader=false" target="detail">InviteCode</a>
							</td>
						</tr>
						<tr>
							<td class="sidebarFooter">
								<s:url action="main.action" id="url_at_link" namespace="/jsp/admin/allocationtemplate">
								</s:url>
								<a href="<s:property value="%{url_at_link}"/>" target="detail">AllocationTemplate</a>
							</td>
						</tr>	
						
						<tr>
							<td class="sidebarFooter">
								<a href="/LTISystem/jsp/admin/thirdparty/main.html" target="detail">3rd p Resources</a>
								&nbsp;
								<a href="/LTISystem/jsp/admin/thirdparty/widgets.html" target="detail">3rd p (widgets)</a>
							
							</td>
							
						</tr>																																																																
					</table>
					

				</div>
				<div id="Line"></div>
				<div id="MainBody">
				
					<iframe src="" name="detail" id="detail" scrolling="yes" width="100%" height="100%" frameborder="0" marginWidth="0" marginHeight="0"></iframe>

					
				</div>
				
			</div>
			
			<div id="Footer">
				LTI System. Developed by LTI Team.
			</div>
			
		</div>
		
	</body>
</html>
